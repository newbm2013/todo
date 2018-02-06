package com.shumidub.todoapprealm.realmcontrollers.reportcontroller;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;

import java.util.List;

import static com.shumidub.todoapprealm.App.realm;

public class ReportRealmController  {


    public static List<ReportObject> getReportList() {
        App.initRealm();
        return App.realm.where(ReportObject.class).findAllSorted("id");
    }


    public static ReportObject getReport(long id) {
        App.initRealm();
        return App.realm.where(ReportObject.class).equalTo("id", id).findFirst();
    }


    public static long addReport(String date, int dayCount, String textReport, int soulRaiting, int healthRaiting) {

        long id = getValidId();
        App.initRealm();
        App.realm.executeTransaction((realm -> {
            ReportObject reportObject = App.realm.createObject(ReportObject.class);
            reportObject.setId(id);
            reportObject.setDate(date);
            reportObject.setCountOfDay(dayCount);
            reportObject.setReportText(textReport);
            reportObject.setSoulRating(soulRaiting);
            reportObject.setHealthRating(healthRaiting);
            App.realm.insert(reportObject);
        }));
        return id;
    }


    public static void editReport(long id, String date, int dayCount, String textReport, int soulRaiting, int healthRaiting) {
        App.initRealm();
        ReportObject reportObject = App.realm.where(ReportObject.class).equalTo("id", id).findFirst();
        App.realm.executeTransaction((realm)->{
            reportObject.setDate(date);
            reportObject.setCountOfDay(dayCount);
            reportObject.setReportText(textReport);
            reportObject.setSoulRating(soulRaiting);
            reportObject.setHealthRating(healthRaiting);
        });
    }

    public static void delReport(long id) {
        App.initRealm();
        ReportObject reportObject = App.realm.where(ReportObject.class).equalTo("id", id).findFirst();
        realm.executeTransaction((transaction)-> reportObject.deleteFromRealm());

    }

    public static long getValidId() {
        long validId = System.currentTimeMillis();
        App.initRealm();
        while ( App.realm.where(ReportObject.class)
                .equalTo("id", validId).findFirst() != null){
            validId ++;
        }
        return validId;
    }
}
