package com.yash1300.trailblazerr.Utils;


import com.yash1300.trailblazerr.Models.DaimokuSession;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController realmInstance = null;
    private Realm realm;

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (realmInstance == null)
            realmInstance = new RealmController();
        return realmInstance;
    }

    public RealmResults<DaimokuSession> getHistory() {
        return realm.where(DaimokuSession.class).findAll();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.clear(DaimokuSession.class);
        realm.commitTransaction();
    }

    public boolean isEmptyHistory() {
        return realm.allObjects(DaimokuSession.class).isEmpty();
    }

    public DaimokuSession findSessionByDate(String date) {
        return realm.where(DaimokuSession.class).equalTo("date", date).findFirst();
    }

    public void addOrUpdateASession(String date, long secs) {
        RealmResults<DaimokuSession> results = this.getHistory();
        long todaySecs = 0;
        if (this.findSessionByDate(date) != null) {
            todaySecs = results.get(results.size()-1).getDuration_in_secs();
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new DaimokuSession(date, todaySecs+secs));
        realm.commitTransaction();
    }

    public DaimokuSession yesterdaySession(String todayDate) {
        RealmResults<DaimokuSession> results = this.getHistory();
        if (this.findSessionByDate(todayDate) != null) {
            return results.get(results.size()-2);
        }
        return results.get(results.size()-1);
    }

}
