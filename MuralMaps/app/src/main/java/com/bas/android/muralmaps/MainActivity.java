package com.bas.android.muralmaps;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FragmentTransaction mPagerAdapter;
    private FragmentManager frag;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Button toggleButton;
    private Spinner filterSpinner;
    private boolean state;
    public Realm realm;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login("cs188","MyBeautifulBulldogApp");

        getResources().getDrawable(R.drawable.common_full_open_on_phone);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void login(final String username, final String password) {
//        if(SyncUser.currentUser() != null) {
//
//            Realm realm = Realm.getDefaultInstance();
//            if(realm != null) {
//                realm.close();
//                Realm.deleteRealm(realm.getConfiguration());
//            }
//
//            SyncUser.currentUser().logout();
//        }

        SyncCredentials myCredentials = SyncCredentials.usernamePassword(username, password, false);
        SyncUser.loginAsync(myCredentials, "http://52.205.194.154:9080", new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                SyncConfiguration configuration = new SyncConfiguration
                        .Builder(user, "http://52.205.194.154:9080/~/mural_maps")
                        .disableSSLVerification().waitForInitialRemoteData().schemaVersion((long) 13.0).build();
                Realm.setDefaultConfiguration(configuration);

                Realm.getInstanceAsync(configuration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {

                        //successfully created a user
                        final User user = new User();
                        user.setUsername("default");
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(user);
                                initializeScreen();
                            }
                        });

                        realm.close();
                    }
                });
            }

            @Override
            public void onError(ObjectServerError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initializeScreen() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).equalTo("username", getIntent().getStringExtra("username")).findFirst();

        toggleButton = (Button) findViewById(R.id.toggleButton);
        filterSpinner = (Spinner) findViewById(R.id.filterList);

        frag = getSupportFragmentManager();
        mPagerAdapter = frag.beginTransaction();
        mPagerAdapter.add(R.id.container, new Maps_tab() , "TAG_MAP");
        mPagerAdapter.commit();
        toggleButton.setText("List View");
        state = true;

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Most Popular");
        arrayList.add("Favorites");
        arrayList.add("All");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        toggleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(state)
                {
                    mPagerAdapter = frag.beginTransaction();
                    mPagerAdapter.replace(R.id.container, new ArtListFragment(), "TAG_List");
                    toggleButton.setText("Map View");
                    state = false;
                    mPagerAdapter.commit();

                }
                else
                {
                    mPagerAdapter = frag.beginTransaction();
                    mPagerAdapter.replace(R.id.container, new Maps_tab(), "TAG_List");
                    toggleButton.setText("List View");
                    state = true;
                    mPagerAdapter.commit();

                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewArt.class);
                startActivity(intent);
            }
        });
    }
}
