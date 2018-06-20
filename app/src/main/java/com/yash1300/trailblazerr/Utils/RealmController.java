package com.yash1300.trailblazerr.Utils;

import com.yash1300.trailblazerr.Models.DaimokuSession;
import com.yash1300.trailblazerr.Models.Date;

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
/*

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
*/

}
