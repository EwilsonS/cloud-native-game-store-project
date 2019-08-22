package com.evanco.levelupservice.model;

import java.time.LocalDate;
import java.util.Objects;

public class LevelUp {
    private int levelUpId;
    private int customerId;
    private int points;
    private LocalDate memberDate;

    public int getLevelUpId() {
        return levelUpId;
    }

    public void setLevelUpId(int levelUpId) {
        this.levelUpId = levelUpId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDate getMemberDate() {
        return memberDate;
    }

    public void setMemberDate(LocalDate memberDate) {
        this.memberDate = memberDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelUp levelUp = (LevelUp) o;
        return getLevelUpId() == levelUp.getLevelUpId() &&
                getCustomerId() == levelUp.getCustomerId() &&
                getPoints() == levelUp.getPoints() &&
                getMemberDate().equals(levelUp.getMemberDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevelUpId(), getCustomerId(), getPoints(), getMemberDate());
    }

    @Override
    public String toString() {
        return "LevelUp{" +
                "levelUpId=" + levelUpId +
                ", customerId=" + customerId +
                ", points=" + points +
                ", memberDate=" + memberDate +
                '}';
    }
}
