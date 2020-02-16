package fi.metropolia.simppa.watertracker;

public class DailyGoal {

    private int dailygoal;

    public DailyGoal(int dailygoal) {
        this.dailygoal = dailygoal;
    }

    public void setDailygoal(int message) {
        this.dailygoal = message;
    }

    public int getDailygoal() {
        return this.dailygoal;
    }
}