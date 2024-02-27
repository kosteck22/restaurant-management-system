package com.invoicesystem.invoicemanagement.service.dao;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import com.invoicesystem.invoicemanagement.web.dto.ProductSearchDto;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomInvoiceRepositoryImpl implements CustomInvoiceRepository {

    private final MongoTemplate mongoTemplate;

    public CustomInvoiceRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Invoice> findInvoicesByCriteria(LocalDate startDate, LocalDate endDate, List<String> sellerIds) {
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();

        if (startDate != null && endDate != null) {
            criteriaList.add(Criteria.where("createdAt").gte(startDate).lte(endDate));
        } else if (startDate != null) {
            criteriaList.add(Criteria.where("createdAt").gte(startDate));
        } else if (endDate != null) {
            criteriaList.add(Criteria.where("createdAt").lte(endDate));
        }

        if (!sellerIds.isEmpty()) {
            criteriaList.add(Criteria.where("sellerId").in(sellerIds));
        }

        if (!criteriaList.isEmpty()) {
            criteria = criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        Query query = Query.query(criteria);
        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);
        return invoices;
    }

    @Override
    public List<ProductSearchDto> findProductsByCriteria(LocalDate startDate, LocalDate endDate, String productName) {
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();

        if (productName != null && !productName.isEmpty()) {
            criteriaList.add(Criteria.where("order.orderDetails.product.name").regex(productName, "i"));
        }
        if (startDate != null) {
            criteriaList.add(Criteria.where("createdAt").gte(startDate));
        }
        if (endDate != null) {
            criteriaList.add(Criteria.where("createdAt").lte(endDate));
        }

        if (!criteriaList.isEmpty()) {
            criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        }

        MatchOperation match = Aggregation.match(criteria);
        UnwindOperation unwind = Aggregation.unwind("order.orderDetails");
        ProjectionOperation project = projectProductFields();

        Aggregation aggregation = Aggregation.newAggregation(match, unwind, match, project);
        AggregationResults<ProductSearchDto> results = mongoTemplate.aggregate(aggregation, Invoice.class, ProductSearchDto.class);

        return results.getMappedResults();
    }

    private static ProjectionOperation projectProductFields() {
        return Aggregation.project()
                .andExpression("_id").as("invoiceNumber")
                .andExpression("order.orderDetails.product.name").as("name")
                .andExpression("order.orderDetails.quantity").as("quantity")
                .andExpression("order.orderDetails.product.netPrice").as("netPrice")
                .andExpression("order.orderDetails.discount").as("discount")
                .andExpression("order.orderDetails.netTotal").as("netTotal")
                .andExpression("order.orderDetails.product.vat").as("vat")
                .andExpression("order.orderDetails.vatTotal").as("vatTotal")
                .andExpression("order.orderDetails.grossPriceTotal").as("grossPriceTotal")
                .andExclude("_id");
    }
}
