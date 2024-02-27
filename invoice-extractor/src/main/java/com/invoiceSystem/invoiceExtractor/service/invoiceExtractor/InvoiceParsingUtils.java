package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class InvoiceParsingUtils {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceParsingUtils.class);

    public static Integer parseIntFromText(String text) {
        try {
            return Integer.parseInt(text);
            //return Integer.valueOf(text.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            logger.error("Error parsing integer from: {}", text, e);
            return null;
        }
    }

    public static BigDecimal parsePriceUnitFromText(String text) {
        try {
            Number number = NumberFormat.getNumberInstance(Locale.GERMANY).parse(text);
            return new BigDecimal(number.toString());
        } catch (ParseException e) {
            logger.error("Error parsing price unit from: {}", text, e);
            return BigDecimal.ZERO;
        }
    }

    public static Integer getVatRate(String text) {
        return parseIntFromText(text.replaceAll("[^\\d]", ""));
    }
}
