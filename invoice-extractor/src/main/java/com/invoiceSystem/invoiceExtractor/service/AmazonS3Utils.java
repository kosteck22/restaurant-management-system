package com.invoiceSystem.invoiceExtractor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class AmazonS3Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Utils.class);

    public static void uploadFile(String folderName, String fileName, byte[] file, String bucketName) {
        try (S3Client client = S3Client.builder().build()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folderName + "/" + fileName)
                    .build();

            client.putObject(request, RequestBody.fromBytes(file));
        }
    }

    public static void deleteFile(String folderName, String fileName, String bucketName) {
        try (S3Client client = S3Client.builder().build()) {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folderName + "/" + fileName)
                    .build();

            client.deleteObject(request);
        }
    }
}
