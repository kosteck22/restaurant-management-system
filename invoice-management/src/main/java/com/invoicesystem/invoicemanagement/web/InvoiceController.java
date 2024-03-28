package com.invoicesystem.invoicemanagement.web;

import com.invoicesystem.invoicemanagement.service.IInvoiceService;
import com.invoicesystem.invoicemanagement.web.dto.InvoiceDto;
import com.invoicesystem.invoicemanagement.web.dto.TotalSumsDto;
import com.invoicesystem.invoicemanagement.web.dto.request.InvoiceRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    private final IInvoiceService invoiceService;

    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<Page<InvoiceDto>> getInvoicesAsPage(@PageableDefault(size = 20) Pageable pageable) {
        Page<InvoiceDto> invoicePage = invoiceService.getAsPage(pageable);
        if (invoicePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(invoicePage);
        }

        return ResponseEntity.ok(invoicePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(invoiceService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InvoiceDto> delete(@PathVariable("id") String id) {
        invoiceService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        String id = invoiceService.save(invoiceRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> update(@Valid @RequestBody InvoiceRequest invoiceRequest,
                                             @PathVariable("id") String id) {
        InvoiceDto invoice = invoiceService.update(invoiceRequest, id);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary(@RequestParam(name = "from", required = false) LocalDate startDate,
                                     @RequestParam(name = "to", required = false) LocalDate endDate,
                                     @RequestParam(name = "companyName", required = false) String companyName) {
        TotalSumsDto summary = invoiceService.summary(startDate, endDate, companyName);
        return ResponseEntity.ok(summary);
    }
}
