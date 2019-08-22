package com.evanco.levelupservice.service;

import com.evanco.levelupservice.dao.LevelUpDao;
import com.evanco.levelupservice.dao.LevelUpDaoJdcbTemplateImpl;
import com.evanco.levelupservice.model.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class LevelUpServiceTest {

    LevelUpService levelUpService;
    LevelUpDao levelUpDao;

    @Before
    public void setUp() throws Exception {

        // configure mock objects
        setUpLevelUpMock();

        // Passes mock objects
        levelUpService = new LevelUpService(levelUpDao);

    }

    // tests addLevelUp()
    @Test
    public void addLevelUp() {
        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));

        levelUp = levelUpService.addLevelUp(levelUp);

        LevelUp levelUp1 = levelUpService.getLevelUp(levelUp.getLevelUpId());

        assertEquals(levelUp, levelUp1);
    }

    // tests getLevelUp()
    @Test
    public void getLevelUp() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));

        LevelUp levelUp1 = levelUpService.getLevelUp(levelUp.getLevelUpId());

        assertEquals(levelUp, levelUp1);
    }

    // tests getAllLevelUps()
    @Test
    public void getAllLevelUps() {

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));

        levelUpService.addLevelUp(levelUp);

        levelUp = new LevelUp();
        levelUp.setCustomerId(2);
        levelUp.setPoints(30);
        levelUp.setMemberDate(LocalDate.of(2019, 2, 15));


        levelUpService.addLevelUp(levelUp);

        List<LevelUp> fromService = levelUpService.getAllLevelUps();

        assertEquals(2, fromService.size());

    }

    // tests deleteLevelUp()
    @Test
    public void deleteLevelUp() {
        LevelUp levelUp = levelUpService.getLevelUp(1);
        levelUpService.deleteLevelUp(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(levelUpDao).deleteLevelUp(postCaptor.capture());
        assertEquals(levelUp.getLevelUpId(), postCaptor.getValue().intValue());
    }

    // tests updateLevelUp()
    @Test
    public void updateLevelUp() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(100);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));


        levelUpService.updateLevelUp(levelUp);
        ArgumentCaptor<LevelUp> postCaptor = ArgumentCaptor.forClass(LevelUp.class);
        verify(levelUpDao).updateLevelUp(postCaptor.capture());
        assertEquals(levelUp.getPoints(), postCaptor.getValue().getPoints());

    }

    // tests if will return null if try to get level up with non-existent id
    @Test
    public void getLevelUpWithNonExistentId() {
        LevelUp levelUp = levelUpService.getLevelUp(500);
        assertNull(levelUp);
    }

    // tests getLevelUpPointsByCustomerId()
    @Test
    public void getLevelUpPointsByCustomerId() {

        LevelUp levelUp = new LevelUp();
        levelUp.setLevelUpId(1);
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setLevelUpId(2);
        levelUp2.setCustomerId(2);
        levelUp2.setPoints(30);
        levelUp2.setMemberDate(LocalDate.of(2019, 2, 15));

        Integer pointsForCust1 = levelUpService.getLevelUpPointsByCustomerId(levelUp.getLevelUpId());

        assertEquals(10, (int) pointsForCust1);

        Integer pointsForCust2 = levelUpService.getLevelUpPointsByCustomerId(levelUp2.getLevelUpId());

        assertEquals(30, (int) pointsForCust2);
    }

    // Create mocks

    public void setUpLevelUpMock() {

        levelUpDao = mock(LevelUpDaoJdcbTemplateImpl.class);

        LevelUp levelUp = new LevelUp();
        levelUp.setCustomerId(1);
        levelUp.setPoints(10);
        levelUp.setMemberDate(LocalDate.of(2019, 1, 20));

        LevelUp levelUp2 = new LevelUp();
        levelUp2.setLevelUpId(1);
        levelUp2.setCustomerId(1);
        levelUp2.setPoints(10);
        levelUp2.setMemberDate(LocalDate.of(2019, 1, 20));

        LevelUp levelUp3 = new LevelUp();
        levelUp3.setCustomerId(2);
        levelUp3.setPoints(30);
        levelUp3.setMemberDate(LocalDate.of(2019, 2, 15));

        LevelUp levelUp4 = new LevelUp();
        levelUp4.setLevelUpId(2);
        levelUp4.setCustomerId(2);
        levelUp4.setPoints(30);
        levelUp4.setMemberDate(LocalDate.of(2019, 2, 15));

        doReturn(levelUp2).when(levelUpDao).addLevelUp(levelUp);
        doReturn(levelUp4).when(levelUpDao).addLevelUp(levelUp3);
        doReturn(levelUp2).when(levelUpDao).getLevelUp(1);
        doReturn(levelUp4).when(levelUpDao).getLevelUp(2);

        doReturn(10).when(levelUpDao).getLevelUpPointsByCustomerId(1);
        doReturn(30).when(levelUpDao).getLevelUpPointsByCustomerId(2);

        List<LevelUp> levelUpList = new ArrayList<>();
        levelUpList.add(levelUp2);
        levelUpList.add(levelUp4);

        doReturn(levelUpList).when(levelUpDao).getAllLevelUps();

    }

}
