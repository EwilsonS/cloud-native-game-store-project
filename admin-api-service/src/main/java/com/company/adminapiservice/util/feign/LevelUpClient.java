package com.company.adminapiservice.util.feign;

import com.company.adminapiservice.util.messages.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    @RequestMapping(value = "/levelups", method = RequestMethod.POST)
    public LevelUp addLevelUp(@RequestBody @Valid LevelUp levelUp);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.GET)
    public LevelUp getLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.PUT)
    public void updateLevelUp(@PathVariable int id, @RequestBody @Valid LevelUp levelUp);

    @RequestMapping(value = "/levelups/{id}", method = RequestMethod.DELETE)
    public void deleteLevelUp(@PathVariable int id);

    @RequestMapping(value = "/levelups", method = RequestMethod.GET)
    public List<LevelUp> getAllLevelUps();

    @RequestMapping(value = "/levelups/customer/{id}", method = RequestMethod.GET)
    public Integer getLevelUpPointsByCustomerId(@PathVariable int id);

}
