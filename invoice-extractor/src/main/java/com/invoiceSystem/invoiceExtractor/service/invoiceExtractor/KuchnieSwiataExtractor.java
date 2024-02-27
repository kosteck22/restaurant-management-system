package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;


import com.invoiceSystem.invoiceExtractor.entity.*;
import com.invoiceSystem.invoiceExtractor.exception.InvoiceParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceParsingUtils.*;

@Component
public class KuchnieSwiataExtractor implements InvoiceDataExtractor {
    private static final Logger logger = LoggerFactory.getLogger(KuchnieSwiataExtractor.class);
    private final TextractService textractService;

    private static class InvoiceInfo {
        static final int INVOICE_NUMBER_INDEX = 1;
    }

    private static class OrderInfo {
        static final int ORDER_DETAIL_BLOCK_OFFSET = 10;
        static final int ORDER_TOTAL_NET_PRICE_OFFSET = 1;
        static final int ORDER_TOTAL_VAT_OFFSET = 2;
        static final int ORDER_TOTAL_GROSS_PRICE_OFFSET = 3;
    }

    private static class OrderDetailInfo {
        static final int NAME_OFFSET = 1;
        static final int QUANTITY_OFFSET = 3;
        static final int UNIT_OF_MEASURE_OFFSET = 4;
        static final int NET_PRICE_OFFSET = 5;
        static final int NET_PRICE_TOTAL_OFFSET = 6;
        static final int VAT_OFFSET = 7;
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

    public KuchnieSwiataExtractor(TextractService textractService) {
        this.textractService = textractService;
    }

    @Override
    public Invoice extractInvoiceData(byte[] file) {
        List<String> pagesText = textractService.analyzeImg(file);
        return Invoice.builder()
                .number(findInvoiceNumber(pagesText))
                .createdAt(findDate(pagesText))
                .buyer(findBuyer())
                .seller(findSeller())
                .order(findOrder(pagesText))
                .build();

    }

    private static String findInvoiceNumber(List<String> pagesText) {
        String s = pagesText.get(InvoiceInfo.INVOICE_NUMBER_INDEX);
        return s.split(" ")[1];
    }

    private LocalDate findDate(List<String> pagesText) {
        return pagesText.stream()
                .filter(text -> (text != null && text.contains("Data wystawienia")))
                .findFirst()
                .map(t -> {
                    String date = t.split(" ")[2];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    return LocalDate.parse(date, formatter);
                })
                .orElse(null);
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

    private Order findOrder(List<String> pagesText) {
        List<OrderDetails> orderDetails = new ArrayList<>();
        int currentIndex = findOrderStartIndex(pagesText);
        if (currentIndex == -1) {
            logger.error("Could not find Order table in given img");
            throw new InvoiceParseException("Could not find Order table in given img");
        }

        while (isOrderDetail(pagesText, currentIndex)) {
            Integer positionIndex = parseIntFromText(pagesText.get(currentIndex));
            if (positionIndex == null) {
                currentIndex += 1;
            }

            OrderDetails orderDetail = buildOrderDetail(pagesText, currentIndex);
            orderDetails.add(orderDetail);
            currentIndex += OrderInfo.ORDER_DETAIL_BLOCK_OFFSET;
        }

        return buildOrder(pagesText, orderDetails, currentIndex);
    }

    private Order buildOrder(List<String> pagesText, List<OrderDetails> orderDetails, int currentIndex) {
        return Order.builder()
                .orderDetails(orderDetails)
                .netPriceTotal(parsePriceUnitFromText(pagesText.get(currentIndex + OrderInfo.ORDER_TOTAL_NET_PRICE_OFFSET)))
                .vatTotal(parsePriceUnitFromText(pagesText.get(currentIndex + OrderInfo.ORDER_TOTAL_VAT_OFFSET)))
                .grossPriceTotal(parsePriceUnitFromText(pagesText.get(currentIndex + OrderInfo.ORDER_TOTAL_GROSS_PRICE_OFFSET)))
                .build();
    }

    private OrderDetails buildOrderDetail(List<String> pagesText, int index) {
        return OrderDetails.builder()
                .positionNumber(parseIntFromText(pagesText.get(index)))
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
                .unitOfMeasure(pagesText.get(index + OrderDetailInfo.UNIT_OF_MEASURE_OFFSET))
                .netPrice(parsePriceUnitFromText(pagesText.get(index + OrderDetailInfo.NET_PRICE_OFFSET)))
                .vat(getVatRate(pagesText.get(index + OrderDetailInfo.VAT_OFFSET)))
                .build();
    }

    private static boolean isOrderDetail(List<String> pagesText, int index) {
        return !"RAZEM".equals(pagesText.get(index));
    }

    private int findOrderStartIndex(List<String> pagesText) {
        boolean beginOfTable = false;
        for (int i = 0; i < pagesText.size(); i++) {
            if (pagesText.get(i) != null && "Poz".equals(pagesText.get(i))) {
                beginOfTable = true;
            }
            if (beginOfTable && pagesText.get(i) != null && "1".equals(pagesText.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
