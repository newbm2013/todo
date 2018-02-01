package com.shumidub.todoapprealm.realmcontrollers.reportcontroller;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.realmmodel.report.ReportObject;

import java.util.List;

public class ReportRealmController  {


    public static List<ReportObject> getReportList() {
        App.initRealm();
        return App.realm.where(ReportObject.class).findAllSorted("id");
    }


    public static void getReport(long id) {

    }


    public static long addReport(long id, String date, int dayCount, String textReport) {
        return 0;
    }


    public static long addReport(int dayCount, String textReport) {
        return 0;
    }


    public static void editReport(long id, String date, int dayCount, String textReport) {

    }

    public static void delReport(long id) {

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
