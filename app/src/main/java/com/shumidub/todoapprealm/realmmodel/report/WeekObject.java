package com.shumidub.todoapprealm.realmmodel.report;

import io.realm.RealmObject;


public class WeekObject extends RealmObject {

    /** number of week as id also*/
    private int weekNumber;
    private int count;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
