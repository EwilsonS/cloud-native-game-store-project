package com.evanco.levelupservice.dao;

import com.evanco.levelupservice.model.LevelUp;

import java.util.List;

public interface LevelUpDao {

    LevelUp addLevelUp(LevelUp levelUp);

    LevelUp getLevelUp(int id);

    void updateLevelUp(LevelUp levelUp);

    void deleteLevelUp(int id);

    List<LevelUp> getAllLevelUps();

    Integer getLevelUpPointsByCustomerId(int id);
}
