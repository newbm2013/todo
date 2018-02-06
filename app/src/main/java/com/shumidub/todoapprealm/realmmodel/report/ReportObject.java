package com.shumidub.todoapprealm.realmmodel.report;

import io.realm.RealmObject;

/** Можно не помещать их в лист или другой контейнер, а просто создавать в реалме.
 *
 *  id и сортировка ао System.currentTime + create method NextValue
 *
 *  Отображают: дату, количество баллов за день, вес,
 *  {в будущем можно баллы по категориям: дух-сотка, ворк, финанс, health, eyes, connection with UNV},
 *  текст - отчет за день, success list
 *
 *  Могут открываться в полный размер, создаваться, редактироваться, удаляться.
 *  Сделать через свайп вправо или пока через ActionMode
 *
 *  Еще нужен reportController and card
 *
 *  also in the future - week count
 */

public class ReportObject extends RealmObject implements IReportObject {

    private long id;
    private String date;
    private int countOfDay;
    private String reportText;
    private int soulRating;
    private int healthRating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCountOfDay() {
        return countOfDay;
    }

    public void setCountOfDay(int countOfDay) {
        this.countOfDay = countOfDay;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public int getSoulRating() {
        return soulRating;
    }

    public void setSoulRating(int soulRating) {
        this.soulRating = soulRating;
    }

    public int getHealthRating() {
        return healthRating;
    }

    public void setHealthRating(int healthRating) {
        this.healthRating = healthRating;
    }

}
