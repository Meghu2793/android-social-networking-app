package example.com.hw07;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/*
Assignment  : HW07
File Name   : FriendsProfile.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class FriendsProfile extends AppCompatActivity {

    String friendId, name;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Post> friendProfile = new ArrayList<>();
    TextView header, toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);

        getSupportActionBar().setTitle("My Social App");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon_menu);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(getIntent().getExtras().containsKey("id")){
            friendId = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
        }

        header = (TextView) findViewById(R.id.textView3);
        header.setText(name+ "'s Posts");
        toolbarText = (TextView) findViewById(R.id.frnd_username);
        toolbarText.setText(name);

        mRootRef.child("users").child(friendId).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post result = dataSnapshot.getValue(Post.class);
                friendProfile.add(result);
                mRecyclerView = (RecyclerView) findViewById(R.id.rvpostsFrndWall);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(FriendsProfile.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                Collections.sort(friendProfile, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o2.getTime().compareTo(o1.getTime());
                    }
                });

                if(!getDaysAge(result.getTime())) {
                    mAdapter = new FriendProfileAdapter(FriendsProfile.this, friendProfile);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.imagebuttonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendsProfile.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean getDaysAge(String msgDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(msgDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        boolean previousDate = false;
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -3);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(convertedDate);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
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
                Intent intent = new Intent(FriendsProfile.this, MainActivity.class);
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
}
