package com.invoicesystem.companyservice.web;

import com.invoicesystem.companyservice.service.ICompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final ICompanyService companyService;

    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CompanyDto> get(@PathVariable("id") String id) {
        CompanyDto companyDto = companyService.get(id);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("nip/{nip}")
    public ResponseEntity<CompanyDto> getCompanyByNip(@PathVariable("nip") String nip) {
        CompanyDto companyDto = companyService.getByNip(nip);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<String>> getIdsForCompanyName(@PathVariable("name") String name) {
        List<String> ids = companyService.getIdsForCompanyName(name);
        return ResponseEntity.ok(ids);
    }

    @PostMapping
    public ResponseEntity<String> saveOrGetId(@RequestBody CompanyDto requestCompany) {
        String id = companyService.saveOrGetId(requestCompany);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(id);
    }
}
