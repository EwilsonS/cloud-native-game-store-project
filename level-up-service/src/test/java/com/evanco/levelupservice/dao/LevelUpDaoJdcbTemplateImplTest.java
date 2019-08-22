package com.evanco.levelupservice.dao;

import com.evanco.levelupservice.exception.NotFoundException;
import com.evanco.levelupservice.model.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelUpDaoJdcbTemplateImplTest {

    @Autowired
    private LevelUpDao levelUpDao;

    @Before
    public void setUp() throws Exception{
        levelUpDao.getAllLevelUps()
                .stream()
                .forEach(l->levelUpDao.deleteLevelUp(l.getLevelUpId()));
    }

    @Test
    public void addGetDeleteLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));

        // add
        levelUp = levelUpDao.addLevelUp(levelUp);

        // get
        LevelUp levelUp1 = levelUpDao.getLevelUp(levelUp.getLevelUpId());
        assertEquals(levelUp, levelUp1);

        //delete
        levelUpDao.deleteLevelUp(levelUp.getLevelUpId());
        assertEquals(0, levelUpDao.getAllLevelUps().size());
    }

    @Test
    public void updateLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));
        levelUp = levelUpDao.addLevelUp(levelUp);

        levelUp.setPoints(500);
        levelUpDao.updateLevelUp(levelUp);

        assertEquals(500, levelUpDao.getLevelUp(levelUp.getLevelUpId()).getPoints());
    }

    @Test
    public void getAllLevelUps() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));
        levelUpDao.addLevelUp(levelUp);
        levelUpDao.addLevelUp(levelUp);
        levelUpDao.addLevelUp(levelUp);

        assertEquals(3, levelUpDao.getAllLevelUps().size());
    }

    @Test
    public void getLevelUpPointsByCustomerId() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(5);
        levelUp.setPoints(80);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));
        levelUp = levelUpDao.addLevelUp(levelUp);

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setCustomerId(10);
        levelUp2.setPoints(120);
        levelUp2.setMemberDate(LocalDate.of(2019,1,26));
        levelUp2 = levelUpDao.addLevelUp(levelUp2);

        int points = levelUpDao.getLevelUpPointsByCustomerId(levelUp2.getCustomerId());
        assertEquals(120, points);
    }

//    @Test(expected  = NotFoundException.class)
//    public void getLevelUpByCustomerWithNonExistentId() {
//        Integer levelUp = levelUpDao.getLevelUpPointsByCustomerId(500);
//    }
}