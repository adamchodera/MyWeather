package pl.com.chodera.myweather.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Adam Chodera on 2016-03-16.
 */
public class FavoriteLocation extends RealmObject {

    @PrimaryKey
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
