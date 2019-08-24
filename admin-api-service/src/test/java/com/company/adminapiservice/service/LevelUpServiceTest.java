package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.LevelUpClient;
import com.company.adminapiservice.util.messages.LevelUp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class LevelUpServiceTest {

    @InjectMocks
    private LevelUpService levelUpService;

    @Mock
    private LevelUpClient levelUpClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpLevelUpClientMock();

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
        verify(levelUpClient).deleteLevelUp(postCaptor.capture());
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


        levelUpService.updateLevelUp(levelUp.getLevelUpId(), levelUp);
        ArgumentCaptor<LevelUp> postCaptor = ArgumentCaptor.forClass(LevelUp.class);
        verify(levelUpClient).updateLevelUp(any(Integer.class), postCaptor.capture());
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

    // tests default constructor for test coverage
    // so developers know something went wrong if less than 100%
    @Test
    public void createADefaultInventory() {

        Object levelUpObj = new LevelUpService();

        assertEquals(true , levelUpObj instanceof LevelUpService);

    }

    // Create mocks

    public void setUpLevelUpClientMock() {

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

        doReturn(levelUp2).when(levelUpClient).addLevelUp(levelUp);
        doReturn(levelUp4).when(levelUpClient).addLevelUp(levelUp3);
        doReturn(levelUp2).when(levelUpClient).getLevelUp(1);
        doReturn(levelUp4).when(levelUpClient).getLevelUp(2);

        doReturn(10).when(levelUpClient).getLevelUpPointsByCustomerId(1);
        doReturn(30).when(levelUpClient).getLevelUpPointsByCustomerId(2);

        List<LevelUp> levelUpList = new ArrayList<>();
        levelUpList.add(levelUp2);
        levelUpList.add(levelUp4);

        doReturn(levelUpList).when(levelUpClient).getAllLevelUps();

    }

}


