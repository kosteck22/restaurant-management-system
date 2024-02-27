package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;

import com.invoiceSystem.invoiceExtractor.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceParsingUtils.*;

@Component
public class AgroHurtExtractor implements InvoiceDataExtractor {
    private final TextractService textractService;

    private static final String FOLDER_NAME = "agro-hurt";

    private static class OrderInfo {
        static final int ORDER_DETAIL_BLOCK_OFFSET = 10;
        static final int ORDER_TOTAL_NET_PRICE_OFFSET = 1;
        static final int ORDER_TOTAL_VAT_OFFSET = 2;
        static final int ORDER_TOTAL_GROSS_PRICE_OFFSET = 3;
    }

    private static class OrderDetailInfo {
        static final int NAME_OFFSET = 1;
        static final int QUANTITY_OFFSET = 2;
        static final int UNIT_OF_MEASURE_OFFSET = 3;
        static final int NET_PRICE_OFFSET = 4;
        static final int NET_PRICE_TOTAL_OFFSET = 7;
        static final int VAT_OFFSET = 6;
        static final int VAT_TOTAL_OFFSET = 8;
        static final int GROSS_PRICE_TOTAL_OFFSET = 9;
    }

    private static class BuyerInfo {
        static final String COMPANY_NAME = "Zielona Baza Joanna Michno";
        static final String COMPANY_NIP = "5170201668";
        static final String COMPANY_REGON = "";
        static final String COMPANY_STREET = "ul. Kurpiowska 3/37";
        static final String COMPANY_CITY = "Rzeszow";
        static final String COMPANY_POSTAL_CODE = "35-620";
    }

    private static class SellerInfo {
        static final String COMPANY_NAME = "KUCHNIE ŚWIATA SPÓŁKA AKCYJNA";
        static final String COMPANY_NIP = "1180039859";
        static final String COMPANY_REGON = "";
        static final String COMPANY_STREET = "ul. SŁODOWIEC 10/10";
        static final String COMPANY_CITY = "Warszawa";
        static final String COMPANY_POSTAL_CODE = "01-708";
    }

    public AgroHurtExtractor(TextractService textractService) {
        this.textractService = textractService;
    }

    @Override
    public Invoice extractInvoiceData(byte[] file) {
        List<String> pagesText = textractService.extractTextFromDocument(file, FOLDER_NAME);
        return Invoice.builder()
                .number(findInvoiceNumber(pagesText))
                .createdAt(findDate(pagesText))
                .buyer(findBuyer())
                .seller(findSeller())
                .order(findOrder(pagesText))
                .build();
    }

    private String findInvoiceNumber(List<String> pagesText) {
        return pagesText.stream()
                .filter(s -> s.contains("Faktura VAT"))
                .findFirst()
                .map(s -> s.split(" ")[2])
                .orElse(null);
    }

    private LocalDate findDate(List<String> pagesText) {
        return pagesText.stream()
                .filter(s -> s.matches("\\d{4}-\\d{2}-\\d{2}"))
                .findFirst()
                .map(d -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(d, formatter);
                })
                .orElse(null);

    }

    private Company findBuyer() {
        return Company.builder()
                .name(BuyerInfo.COMPANY_NAME)
                .nip(BuyerInfo.COMPANY_NIP)
                .regon(BuyerInfo.COMPANY_REGON)
                .street(BuyerInfo.COMPANY_STREET)
                .city(BuyerInfo.COMPANY_CITY)
                .postalCode(BuyerInfo.COMPANY_POSTAL_CODE)
                .build();
    }

    private Company findSeller() {
        return Company.builder()
                .name(SellerInfo.COMPANY_NAME)
                .nip(SellerInfo.COMPANY_NIP)
                .regon(SellerInfo.COMPANY_REGON)
                .street(SellerInfo.COMPANY_STREET)
                .city(SellerInfo.COMPANY_CITY)
                .postalCode(SellerInfo.COMPANY_POSTAL_CODE)
                .build();
    }

    private Order findOrder(List<String> pagesText) {
        List<OrderDetails> orderDetails = new ArrayList<>();
        int currentIndex = 0;


        while ((currentIndex = findOrderStartIndex(pagesText, currentIndex)) != -1) //Loop until last order page
        {
            while (currentIndex < pagesText.size() && isOrderDetail(pagesText.get(currentIndex))) {
                OrderDetails orderDetail = buildOrderDetail(pagesText, currentIndex);
                if (orderDetail != null) {
                    orderDetails.add(orderDetail);
                    currentIndex += OrderInfo.ORDER_DETAIL_BLOCK_OFFSET;
                } else {
                    currentIndex++; //Increment index if current line isn't valid
                }
            }
        }

        return buildOrder(pagesText, orderDetails);
    }

    private Order buildOrder(List<String> pagesText, List<OrderDetails> orderDetails) {
        return Order.builder()
                .orderDetails(orderDetails)
                .netPriceTotal(parsePriceUnitFromText(getTotal(pagesText, OrderInfo.ORDER_TOTAL_NET_PRICE_OFFSET)))
                .vatTotal(parsePriceUnitFromText(getTotal(pagesText, OrderInfo.ORDER_TOTAL_VAT_OFFSET)))
                .grossPriceTotal(parsePriceUnitFromText(getTotal(pagesText, OrderInfo.ORDER_TOTAL_GROSS_PRICE_OFFSET)))
                .build();
    }

    private String getTotal(List<String> pagesText, int offset) {
        int summaryIndex = getSummaryIndex(pagesText);
        if (summaryIndex > -1) {
            return pagesText.get(summaryIndex + offset);
        }

        return null;
    }

    private int getSummaryIndex(List<String> pagesText) {
        for (int i = 0; i < pagesText.size(); i++) {
            if ("Razem:".equals(pagesText.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean isOrderDetail(String text) {
        return !"Ciag dalszy na nastepnej stronie".equals(text) && !"Razem:".equals(text);
    }

    private OrderDetails buildOrderDetail(List<String> pagesText, int index) {
        //invalid index position
        Integer positionNumber = parseIntFromText(pagesText.get(index));
        if (positionNumber == null) {
            return null;
        }

        return OrderDetails.builder()
                .positionNumber(positionNumber)
                .product(buildProduct(pagesText, index))
                .quantity(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.QUANTITY_OFFSET)))
                .discount(0)
                .netTotal(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.NET_PRICE_TOTAL_OFFSET)))
                .vatTotal(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.VAT_TOTAL_OFFSET)))
                .grossPriceTotal(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.GROSS_PRICE_TOTAL_OFFSET)))
                .build();
    }

    private Product buildProduct(List<String> pagesText, int index) {
        return Product.builder()
                .name(pagesText.get(index + OrderDetailInfo.NAME_OFFSET))
                .netPrice(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.NET_PRICE_OFFSET)))
                .unitOfMeasure(pagesText.get(index + OrderDetailInfo.UNIT_OF_MEASURE_OFFSET))
                .vat(getVatRate(pagesText.get(index + OrderDetailInfo.VAT_OFFSET)))
                .build();
    }

    private int findOrderStartIndex(List<String> pagesText, int startIndex) {
        for (int i = startIndex; i < pagesText.size(); i++) {
            if ("Lp".equals(pagesText.get(i))) {
                int orderDetailPosition = i + OrderInfo.ORDER_DETAIL_BLOCK_OFFSET;
                return parseIntFromText(pagesText.get(orderDetailPosition)) != null ? orderDetailPosition : orderDetailPosition + 1;
            }
        }
        return -1;
    }
}
