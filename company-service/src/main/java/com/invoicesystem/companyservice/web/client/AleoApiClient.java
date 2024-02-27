package com.invoicesystem.companyservice.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "aleo-client-provider", url = "https://aleo.com/pl/firmy")
public interface AleoApiClient {

    @GetMapping
    String getSite(@RequestParam(name = "phrase") String nip);
}
