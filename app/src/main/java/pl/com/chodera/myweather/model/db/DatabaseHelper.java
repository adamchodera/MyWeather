package pl.com.chodera.myweather.model.db;

import io.realm.Realm;
import io.realm.RealmObject;

public class DatabaseHelper {

    public static <T extends RealmObject> T createAndSaveObject(final Realm realmInstance, final Class<T> clazz, final Object primaryKeyValue) {
        realmInstance.beginTransaction();
        T object = realmInstance.createObject(clazz, primaryKeyValue);
        realmInstance.copyToRealm(object);
        realmInstance.commitTransaction();
        return object;
    }

    public static void deleteObjectFromRealm(final Realm realm, final RealmObject object) {
        if (object == null) {
            return;
        }

        if (object.isValid()) {
            realm.beginTransaction();
            object.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public static boolean isRealmInstanceOpened(final Realm realmInstance) {
        return realmInstance != null && !realmInstance.isClosed();
    }

    public static boolean isRealmInstanceClosed(final Realm realmInstance) {
        return realmInstance == null || realmInstance.isClosed();
    }
}
