package com.evanco.levelupservice.dao;

import com.evanco.levelupservice.exception.AmbiguousResultException;
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

    // clear level up table in database
    @Before
    public void setUp() throws Exception{
        levelUpDao.getAllLevelUps()
                .stream()
                .forEach(l->levelUpDao.deleteLevelUp(l.getLevelUpId()));
    }

    // tests addLevelUp(), getLevelUp() and deleteLevelUp()
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

    // tests if will return null if try to get level up with non-existent level up id
    @Test
    public void getLevelUpWithNonExistentId() {
        LevelUp levelUp = levelUpDao.getLevelUp(500);
        assertNull(levelUp);
    }

    // tests if will throw exception if id provided does not exist when trying to delete level up
    @Test(expected  = NotFoundException.class)
    public void deleteLevelUpWithNonExistentId() {
        levelUpDao.deleteLevelUp(500);
    }

    // tests updateLevelUp()
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

    // tests if will throw exception if id provided does not exist when trying to update level up
    @Test(expected  = IllegalArgumentException.class)
    public void updateLevelUpWithIllegalArgumentException() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(500);
        levelUp.setCustomerId(5);
        levelUp.setPoints(20);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));

        levelUpDao.updateLevelUp(levelUp);

    }

    // tests getAllLevelUps()
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

    // test if it will return an empty array when there are no level ups
    @Test
    public void getAllLevelUpsWhenNoLevelUps() {
        assertEquals(0, levelUpDao.getAllLevelUps().size());
    }

    // tests getLevelUpPointsByCustomerId()
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

    // tests if will throw exception if the customer id does not exist
    @Test
    public void getLevelUpByCustomerWithNonExistentId() {
        Integer levelUp = levelUpDao.getLevelUpPointsByCustomerId(500);
        assertNull(levelUp);
    }

    // tests if will throw exception if there are multiple rows with the customer id
    @Test(expected  = AmbiguousResultException.class)
    public void getLevelUpByCustomerWithMultipleCustomerId() {

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(80);
        levelUp.setMemberDate(LocalDate.of(2019,1,26));
        levelUpDao.addLevelUp(levelUp);

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setCustomerId(1);
        levelUp2.setPoints(120);
        levelUp2.setMemberDate(LocalDate.of(2019,1,26));
        levelUpDao.addLevelUp(levelUp2);

        Integer levelUpPoints = levelUpDao.getLevelUpPointsByCustomerId(1);
    }

}