package pl.com.chodera.myweather.common;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.realm.Realm;
import pl.com.chodera.myweather.model.db.DatabaseHelper;

public class BaseFragment extends Fragment {

    private Realm realmInstance;

    @Override
    public void onDetach() {
        super.onDetach();

        if (DatabaseHelper.isRealmInstanceOpened(realmInstance)) {
            realmInstance.close();
        }
    }

    protected Realm getRealmInstance() {
        if (DatabaseHelper.isRealmInstanceClosed(realmInstance)) {
            realmInstance = Realm.getDefaultInstance();
        }
        return realmInstance;
    }

    protected void setupToolbar(final Toolbar toolbar) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    protected void changeToBackNavigationMode() {
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeAsUpIndicator(null);
    }
}
