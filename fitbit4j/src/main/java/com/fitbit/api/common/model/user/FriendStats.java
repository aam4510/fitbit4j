package com.fitbit.api.common.model.user;

public class FriendStats {
    private UserInfo user;
    private StatisticInfo summary;
    private StatisticInfo average;

    public FriendStats(UserInfo user, StatisticInfo summary, StatisticInfo average) {
        this.user = user;
        this.summary = summary;
        this.average = average;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public StatisticInfo getSummary() {
        return summary;
    }

    public void setSummary(StatisticInfo summary) {
        this.summary = summary;
    }

    public StatisticInfo getAverage() {
        return average;
    }

    public void setAverage(StatisticInfo average) {
        this.average = average;
    }

    public static class StatisticInfo {
        private String steps;
        private String calories;
        private String distance;
        private String activeScore;

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getActiveScore() {
            return activeScore;
        }

        public void setActiveScore(String activeScore) {
            this.activeScore = activeScore;
        }
    }
}
