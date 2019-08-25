package com.evanco.levelupqueueconsumer.util.feign;

import com.evanco.levelupqueueconsumer.util.messages.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "level-up-service")
public interface LevelUpClient {

    @PostMapping(value = "/levelups")
    LevelUp addLevelUp(@RequestBody LevelUp levelUp);

    @PutMapping(value = "/levelups/{id}")
    void updateLevelUp(@PathVariable("id") int id, @RequestBody LevelUp levelUp);

}
