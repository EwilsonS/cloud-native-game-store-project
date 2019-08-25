package com.evanco.retailapiservice.util.feign;

import com.evanco.retailapiservice.model.LevelUp;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    // get levelUp
    @GetMapping(value = "/levelups/{id}")
    LevelUp getLevelup(@PathVariable("id") int id);

    // get levelup object by level up id
    @GetMapping(value = "/levelups/customer/{id}")
    LevelUp getLevelUpByCustomerId(@PathVariable("id") int id);

}
