package pl.com.chodera.myweather.model.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Adam Chodera on 2016-03-16.
 */
public class FavoriteLocation extends RealmObject {

    @Required
    @PrimaryKey
    private String name;

    public FavoriteLocation() {
    }

    public FavoriteLocation(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
