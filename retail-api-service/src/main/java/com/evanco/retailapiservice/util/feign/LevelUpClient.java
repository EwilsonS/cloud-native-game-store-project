package com.evanco.retailapiservice.util.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {


    // get levelup points by customer id
    @GetMapping(value = "/levelups/customer/{id}")
    int getLevelUpPointsByCustomerId(@PathVariable("id") int id);


}
