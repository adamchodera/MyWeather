package pl.com.chodera.myweather.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.com.chodera.myweather.R;

/**
 * Created by Adam Chodera on 2016-03-15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Realm realmInstance;

    public void changeToBackNavigationMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);
    }

    public void changeToWithLogoNavigationMode() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logo);
    }

    public Realm getRealmInstance() {
        if (realmInstance == null || realmInstance.isClosed()) {

            // TODO generate keystore and save secretly
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
//                    .encryptionKey(key) // TODO set generated key
                    .build();

            realmInstance = Realm.getInstance(realmConfiguration);
        }

        return realmInstance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        realmInstance.close();
    }
}
