package pl.com.chodera.myweather.common;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseFragment extends Fragment {

    private Realm realmInstance;

    public Realm getRealmInstance() {
        if (realmInstance == null || realmInstance.isClosed()) {

            Realm.init(getContext());
            final RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
            realmInstance = Realm.getInstance(realmConfiguration);
        }

        return realmInstance;
    }

    protected void setupToolbar(final Toolbar toolbar) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected void changeToBackNavigationMode() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(null);
    }
}
