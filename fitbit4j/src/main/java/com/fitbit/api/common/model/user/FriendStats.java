package com.fitbit.api.common.model.user;

public class FriendStats {
    private UserInfo user;
    private StatisticInfo summary = new StatisticInfo();
    private StatisticInfo average = new StatisticInfo();

    public FriendStats(UserInfo user) {
        this.user = user;
    }

    public StatisticInfo getSummary() {
        return summary;
    }

    public StatisticInfo getAverage() {
        return average;
    }

    public UserInfo getUser() {
        return user;
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
