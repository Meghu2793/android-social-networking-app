package example.com.hw07;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/*
Assignment  : HW07
File Name   : ManageFriendsTabbedActivity.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class ManageFriendsTabbedActivity extends AppCompatActivity {

    private static final String TAG = "ManageFriendsTabbedActivity";
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends_tabbed);

        getSupportActionBar().setTitle("My Social App");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon_menu);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.d("demo","TAG OnCreate Starting");
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.imagebuttonHomeFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageFriendsTabbedActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        menu.findItem(R.id.menu_logout).setEnabled(true);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout: FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ManageFriendsTabbedActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.menu_logout).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        sectionPageAdapter.addFragment(new FriendsFragment(),"Friends");
        sectionPageAdapter.addFragment(new AddNewFriendFragment(),"Add New Friend");
        sectionPageAdapter.addFragment(new RequestPendingFragment(),"Request Pending");
        viewPager.setAdapter(sectionPageAdapter);
    }
}
