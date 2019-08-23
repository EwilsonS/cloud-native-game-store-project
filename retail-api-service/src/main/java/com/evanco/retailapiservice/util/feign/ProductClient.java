package com.evanco.retailapiservice.util.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service")
public interface ProductClient {

    // get products in inventory
}
