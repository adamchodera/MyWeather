package pl.com.chodera.myweather.model.db;

import java.util.ArrayList;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Opens databases created by Realm 2 (schema version 0) without deleting favorites.
 * The model is still a single required primary key {@code name}.
 */
public class FavoriteLocationMigration implements RealmMigration {

    public static final long SCHEMA_VERSION = 1L;

    private static final String CLASS_NAME = "FavoriteLocation";
    private static final String NAME_FIELD = "name";

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion >= SCHEMA_VERSION) {
            return;
        }

        RealmSchema schema = realm.getSchema();
        RealmObjectSchema favoriteLocation = schema.get(CLASS_NAME);
        if (favoriteLocation == null) {
            schema.create(CLASS_NAME)
                    .addField(NAME_FIELD, String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED);
            return;
        }

        if (!favoriteLocation.hasField(NAME_FIELD)) {
            favoriteLocation.addField(NAME_FIELD, String.class, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED);
        } else {
            if (favoriteLocation.hasPrimaryKey() && !NAME_FIELD.equals(favoriteLocation.getPrimaryKey())) {
                favoriteLocation.removePrimaryKey();
            }
            if (!favoriteLocation.hasPrimaryKey()) {
                if (!favoriteLocation.isRequired(NAME_FIELD)) {
                    favoriteLocation.setRequired(NAME_FIELD, true);
                }
                favoriteLocation.addPrimaryKey(NAME_FIELD);
            }
        }

        for (String fieldName : new ArrayList<>(favoriteLocation.getFieldNames())) {
            if (!NAME_FIELD.equals(fieldName)) {
                favoriteLocation.removeField(fieldName);
            }
        }
    }

    @Override
    public int hashCode() {
        return FavoriteLocationMigration.class.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FavoriteLocationMigration;
    }
}
