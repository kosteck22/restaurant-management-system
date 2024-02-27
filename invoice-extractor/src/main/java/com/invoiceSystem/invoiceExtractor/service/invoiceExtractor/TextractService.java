package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;

import com.invoiceSystem.invoiceExtractor.service.AmazonS3Utils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TextractService {
    private final TextractClient textractClient;
    private static final String BUCKET_NAME;

    static {
        BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
    }

    public TextractService(TextractClient textractClient) {
        this.textractClient = textractClient;
    }

    public List<String> extractTextFromDocument(byte[] file, String folderName) {
        String fileName = UUID.randomUUID().toString();

        //Saving file to S3
        AmazonS3Utils.uploadFile(folderName, fileName, file, BUCKET_NAME);

        //
        String jobId = startDocumentTextDetection(folderName + "/" + fileName);

        List<String> result = getJobResults(jobId);

        //Delete file after extracted text from it
        AmazonS3Utils.deleteFile(folderName, fileName, BUCKET_NAME);

        return result;
    }

    public List<String> analyzeImg(byte[] img) {
        AnalyzeDocumentRequest request = AnalyzeDocumentRequest.builder()
                .document(Document.builder().bytes(SdkBytes.fromByteArray(img)).build())
                .featureTypes(FeatureType.TABLES, FeatureType.FORMS)
                .build();

        AnalyzeDocumentResponse response = textractClient.analyzeDocument(request);

        return response.blocks().stream()
                .filter(block ->
                        BlockType.LINE.equals(block.blockType()))
                .map(Block::text)
                .collect(Collectors.toList());
    }

    private String startDocumentTextDetection(String documentKey) {
        StartDocumentTextDetectionRequest request = StartDocumentTextDetectionRequest.builder()
                .documentLocation(DocumentLocation.builder()
                        .s3Object(S3Object.builder()
                                .bucket(BUCKET_NAME)
                                .name(documentKey)
                                .build())
                        .build())
                .build();

        StartDocumentTextDetectionResponse response = textractClient.startDocumentTextDetection(request);
        return response.jobId();
    }

    private boolean waitForJobToComplete(String jobId) throws InterruptedException {
        GetDocumentTextDetectionResponse response;
        String status = "";

        do {
            Thread.sleep(5000); // Wait for 5 seconds before checking the job status again

            response = textractClient.getDocumentTextDetection(GetDocumentTextDetectionRequest.builder()
                    .jobId(jobId)
                    .build());
            status = response.jobStatusAsString();

            System.out.println("Job status: " + status);
        } while (!status.equals("SUCCEEDED") && !status.equals("FAILED"));

        return status.equals("SUCCEEDED");
    }

    private List<String> getJobResults(String jobId) {
        List<String> pagesText = new ArrayList<>();
        String nextToken = null;

        try {
            boolean isComplete = waitForJobToComplete(jobId);
            if (!isComplete) {
                System.out.println("Textract job did not complete successfully.");
                return pagesText;
            }

            do {
                GetDocumentTextDetectionRequest textDetectionRequest = GetDocumentTextDetectionRequest.builder()
                        .jobId(jobId)
                        .nextToken(nextToken)
                        .build();

                GetDocumentTextDetectionResponse detectionResponse = textractClient.getDocumentTextDetection(textDetectionRequest);
                nextToken = detectionResponse.nextToken();

                List<Block> blocks = detectionResponse.blocks();
                for (Block block : blocks) {
                    if (BlockType.LINE.equals(block.blockType())) {
                        String lineText = block.text();
                        pagesText.add(lineText);
                    }
                }
            } while (nextToken != null);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("The thread waiting for the job to complete was interrupted");
        }

        return pagesText;
    }
}
