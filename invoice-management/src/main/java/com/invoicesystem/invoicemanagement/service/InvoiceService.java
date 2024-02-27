package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import com.invoicesystem.invoicemanagement.entity.Order.OrderDetails;
import com.invoicesystem.invoicemanagement.exception.DuplicateResourceException;
import com.invoicesystem.invoicemanagement.exception.RequestValidationException;
import com.invoicesystem.invoicemanagement.exception.ResourceNotFoundException;
import com.invoicesystem.invoicemanagement.service.dao.InvoiceRepository;
import com.invoicesystem.invoicemanagement.web.client.CompanyClient;
import com.invoicesystem.invoicemanagement.web.dto.CompanyDto;
import com.invoicesystem.invoicemanagement.web.dto.InvoiceDto;
import com.invoicesystem.invoicemanagement.web.dto.TotalSumsDto;
import com.invoicesystem.invoicemanagement.web.dto.request.CompanyRequest;
import com.invoicesystem.invoicemanagement.web.dto.request.InvoiceRequest;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.invoicesystem.invoicemanagement.service.FinanceUtilities.sum;
import static com.invoicesystem.invoicemanagement.service.FinanceUtilities.validateDate;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Service
public class InvoiceService implements IInvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;
    private final CompanyService companyService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceCreatorService invoiceCreatorService;

    public InvoiceService(InvoiceRepository invoiceRepository, CompanyService companyService, InvoiceMapper invoiceMapper, InvoiceCreatorService invoiceCreatorService) {
        this.invoiceRepository = invoiceRepository;
        this.companyService = companyService;
        this.invoiceMapper = invoiceMapper;
        this.invoiceCreatorService = invoiceCreatorService;
    }

    @Override
    public InvoiceDto get(String id) {
        return invoiceRepository.findById(id)
                .map(i -> {
                    CompanyDto buyer = companyService.getCompanyById(i.getBuyerId());
                    CompanyDto seller = companyService.getCompanyById(i.getSellerId());
                    return invoiceMapper.toDto(i, buyer, seller);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for given id: %s".formatted(id)));
    }

    @Override
    public Page<InvoiceDto> getAsPage(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(invoiceMapper::toDto);
    }

    @Override
    public String save(InvoiceRequest request) {
        checkIfInvoiceAlreadyExist(request.number());
        String buyerCompanyId = companyService.saveOrGetCompanyId(request.buyer(), "Buyer");
        String sellerCompanyId = companyService.saveOrGetCompanyId(request.seller(), "Seller");
        Invoice invoice = invoiceCreatorService.create(request, buyerCompanyId, sellerCompanyId);

        return invoiceRepository.save(invoice).getId();
    }

    @Override
    public InvoiceDto update(InvoiceRequest request, String id) {
        Invoice invoiceFromDB = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with given id {%s} not found".formatted(id)));
        String buyerCompanyId = companyService.saveOrGetCompanyId(request.buyer(), "Buyer");
        String sellerCompanyId = companyService.saveOrGetCompanyId(request.seller(), "Seller");
        Invoice invoice = invoiceCreatorService.create(request, buyerCompanyId, sellerCompanyId);
        invoice.setId(invoiceFromDB.getId());

        return invoiceMapper.toDto(invoiceRepository.save(invoice));
    }

    @Override
    public TotalSumsDto summary(LocalDate startDate, LocalDate endDate, String companyName) {
        validateDate(startDate, endDate);
        List<String> companiesIdsForName = fetchCompaniesIdsOrThrow(companyName);

        List<Invoice> invoices = invoiceRepository.findInvoicesByCriteria(startDate, endDate, companiesIdsForName);
        return calculateInvoiceTotals(invoices);
    }

    @Override
    public void delete(String id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice with given id %s not found".formatted(id)));
        invoiceRepository.delete(invoice);
    }

    private List<String> fetchCompaniesIdsOrThrow(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> companyIds = companyService.getCompaniesIdsForName(companyName);
        if (companyIds.isEmpty()) {
            logger.error("No companies found for name: {}", companyName);
            throw new ResourceNotFoundException("Companies not found for given name: " + companyName);
        }

        return companyIds;
    }

    private TotalSumsDto calculateInvoiceTotals(List<Invoice> invoices) {
        BigDecimal netPriceTotal = sum(invoices, i -> i.getOrder().getNetPriceTotal());
        BigDecimal vatTotal = sum(invoices, i -> i.getOrder().getVatTotal());
        BigDecimal grossPriceTotal = sum(invoices, i -> i.getOrder().getGrossPriceTotal());
        Map<Integer, BigDecimal> vatByRate = FinanceUtilities.groupAndSum(
                invoices.stream()
                        .flatMap(i -> i.getOrder().getOrderDetails().stream())
                        .collect(Collectors.toList()),
                o -> o.getProduct().getVat().getRate(),
                OrderDetails::getVatTotal
        );

        return new TotalSumsDto(netPriceTotal, vatTotal, grossPriceTotal, vatByRate);
    }

    private void checkIfInvoiceAlreadyExist(String number) {
        invoiceRepository.findByNumber(number)
                .ifPresent(i -> {
                    throw new DuplicateResourceException("Invoice with number %s already exists".formatted(number));
                });
    }
}