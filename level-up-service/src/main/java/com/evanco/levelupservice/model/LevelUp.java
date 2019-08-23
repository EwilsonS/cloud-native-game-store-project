package com.evanco.levelupservice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Objects;

public class LevelUp {

    private int levelUpId;

    // wrapper and not primitive int to validate if supplied
    @NotNull(message = "Please supply a customer id.")
    @Positive
    private Integer customerId;

    // wrapper and not primitive int to validate if supplied
    @NotNull(message = "Please supply points.")
    private Integer points;

    @NotNull(message = "Please supply a member date.")
    private LocalDate memberDate;

    // constructors

    public LevelUp() {
    }

    public LevelUp(int levelUpId, int customerId, int points, LocalDate memberDate) {
        this.levelUpId = levelUpId;
        this.customerId = customerId;
        this.points = points;
        this.memberDate = memberDate;
    }

    // getters and setters

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
        // helps avoid NullPointer Exceptions when doing calculations
        if (points == null) {
            return 0;
        }
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

    // override methods

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
