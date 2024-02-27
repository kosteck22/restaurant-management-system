package com.invoicesystem.companyservice.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.invoicesystem.companyservice.entity.Company;
import com.invoicesystem.companyservice.exception.ResourceNotFoundException;
import com.invoicesystem.companyservice.web.client.AleoApiClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AleoCompanyProvider implements CompanyProvider {

    private static final Logger logger = LoggerFactory.getLogger(AleoCompanyProvider.class);

    private final AleoApiClient aleoApiClient;

    public AleoCompanyProvider(AleoApiClient aleoApiClient) {
        this.aleoApiClient = aleoApiClient;
    }

    @Override
    public Optional<Company> findCompanyByNip(String nip) {
        logger.info("Searching for company with NIP: {}", nip);
        String html = aleoApiClient.getSite(nip);
        if (html == null) {
            logger.error("No data found for NIP: {}", nip);
            return Optional.empty();
        }

        try {
            JsonObject contentObject = extractCompanyDataFromHtml(nip, html);
            if (contentObject == null) {
                logger.error("No data found in html for NIP: {}", nip);
                return Optional.empty();
            }

            return Optional.of(createCompanyFromJsonObject(contentObject));
        } catch (ResourceNotFoundException ex) {
            return Optional.empty();
        }
    }

    private Company createCompanyFromJsonObject(JsonObject contentObject) {
        JsonObject identification = contentObject.getAsJsonObject("identification");
        JsonObject address = contentObject.getAsJsonObject("address");
        JsonElement regon = identification.get("regon");

        return createCompany(
                identification.get("nip").getAsString(),
                contentObject.get("name").getAsString(),
                regon == null ? null : identification.get("regon").getAsString(),
                address.get("city").getAsString(),
                address.get("address").getAsString(),
                address.get("zipCode").getAsString()
        );
    }

    private static JsonObject extractCompanyDataFromHtml(String nip, String html) {
        Document doc = Jsoup.parse(html);
        Element scriptElement = doc.selectFirst("script[type=application/json]");
        if (scriptElement == null) {
            throw getResourceNotFoundException(nip);
        }

        String jsonData = scriptElement.data().replace("&q;", "\"");
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject companyCatalog = jsonObject.getAsJsonObject("company-catalog-search-result-%s-1".formatted(nip));
        if (!companyCatalog.getAsJsonArray("content").isJsonArray()) {
            throw getResourceNotFoundException(nip);
        }

        return companyCatalog.getAsJsonArray("content").get(0).getAsJsonObject();
    }

    private static ResourceNotFoundException getResourceNotFoundException(String nip) {
        return new ResourceNotFoundException("Information about company with tax id number %s not found".formatted(nip));
    }

    private static Company createCompany(String nip, String name, String regon, String city, String street, String zipCode) {
        return Company.builder()
                .name(name)
                .nip(nip)
                .regon(regon)
                .city(city)
                .street(street)
                .postalCode(zipCode).build();
    }
}
