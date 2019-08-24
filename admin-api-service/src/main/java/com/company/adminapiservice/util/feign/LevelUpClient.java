package com.company.adminapiservice.util.feign;

import com.company.adminapiservice.util.messages.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    @RequestMapping(value = "/levelups", method = RequestMethod.POST)
    LevelUp addLevelUp(@RequestBody @Valid LevelUp levelUp);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.GET)
    LevelUp getLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.PUT)
    void updateLevelUp(@PathVariable int id, @RequestBody @Valid LevelUp levelUp);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.DELETE)
    void deleteLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelups", method = RequestMethod.GET)
    List<LevelUp> getAllLevelUps();

    @RequestMapping(value = "/levelups/customer/{id}", method = RequestMethod.GET)
    Integer getLevelUpPointsByCustomerId(@PathVariable int id);

}
