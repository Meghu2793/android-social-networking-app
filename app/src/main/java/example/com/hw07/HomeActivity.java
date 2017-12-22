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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.StreamHandler;

/*
Assignment  : HW07
File Name   : HomeActivity.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static private ArrayList<Post> postsList;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String user, post_msg_, username;
    EditText post_msg;
    Post post, result;
    TextView usernameTab;
    static String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("My Social App");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon_menu);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        usernameTab = (TextView) findViewById(R.id.user_name);
        usernameTab.setText(firebaseUser.getDisplayName());
        usernameTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                profile.putExtra("UID", firebaseUser.getUid());
                startActivity(profile);
            }
        });

        postsList = new ArrayList<Post>();

        post_msg = (EditText) findViewById(R.id.editTextSendMessage);

//        mRootRef.child("users").child(firebaseUser.getUid()).child("messages").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                result = dataSnapshot.getValue(Post.class);
//                result.setKey(dataSnapshot.getKey());
//                mRootRef.child("users").child(firebaseUser.getUid()).child("messages").child(dataSnapshot.getKey()).child("key").setValue(result.getKey());
//                Log.d("demo", "Val " + result.getKey().toString());
//                postsList.add(result);
//
//                Log.d("demo", "Postlist " + postsList.toString());
//                mRecyclerView = (RecyclerView) findViewById(R.id.rvposts);
//                mRecyclerView.setHasFixedSize(true);
//                mLayoutManager = new LinearLayoutManager(HomeActivity.this);
//                mRecyclerView.setLayoutManager(mLayoutManager);
//
//                Collections.sort(postsList, new Comparator<Post>() {
//                    @Override
//                    public int compare(Post o1, Post o2) {
//                        return o2.getTime().compareTo(o1.getTime());
//                    }
//                });
////                for (Post res : postsList) {
//                    if (!getDaysAge(result.getTime())) {
//                        mAdapter = new PostsAdapter(HomeActivity.this, postsList);
//                        mRecyclerView.setAdapter(mAdapter);
//                    }
////                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        mRootRef.child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot dataSnapshot1 = dataSnapshot.child(firebaseUser.getUid()).child("friends");
//                if (dataSnapshot1 != null) {
//                    if (dataSnapshot1.hasChild("currentFriends")) {
//                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("currentFriends").getChildren()) {
//                            String uid = dataSnapshot2.getKey();
//                            Log.d("demo", "UID " + uid);
//                            for (DataSnapshot d : dataSnapshot.child(uid).child("messages").getChildren()) {
//                                Post res = d.getValue(Post.class);
//                                Log.d("demo", "REs " + res.toString());
//                                postsList.add(res);
//                            }
//                        }
//                        Collections.sort(postsList, new Comparator<Post>() {
//                            @Override
//                            public int compare(Post o1, Post o2) {
//                                return o2.getTime().compareTo(o1.getTime());
//                            }
//                        });
//                    }
//    }


//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        findViewById(R.id.imageButtonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Log.d("demo", "date  " + date);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String dateVal = sdf.format(date);
                Log.d("demo", "dateVal  " + dateVal);

                post = new Post();
                user = firebaseUser.getUid();
                username = firebaseUser.getDisplayName();
                post_msg_ = post_msg.getText().toString();
                if (post_msg_ == null || post_msg_.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Enter a Message!", Toast.LENGTH_SHORT).show();
                } else {
                    post.setUserId(user);
                    post.setUsername(username);
                    post.setPost_message(post_msg_);
                    post.setTime(dateVal);
                    post.setKey("");
                }

                Map<String, Object> postValues = post.toMap();
                mRootRef.child("users").child(firebaseUser.getUid()).child("messages").push().setValue(postValues);

                Toast.makeText(HomeActivity.this, "Message added!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.imagebuttonFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ManageFriendsTabbedActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        postsList = new ArrayList<>();
        mRootRef.child("users").child(firebaseUser.getUid()).child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result = dataSnapshot.getValue(Post.class);
                result.setKey(dataSnapshot.getKey());
                mRootRef.child("users").child(firebaseUser.getUid()).child("messages").child(dataSnapshot.getKey()).child("key").setValue(result.getKey());
                Log.d("demo", "Val " + result.getKey().toString());
                postsList.add(result);

                Log.d("demo", "Postlist " + postsList.toString());
                mRecyclerView = (RecyclerView) findViewById(R.id.rvposts);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                Collections.sort(postsList, new Comparator<Post>() {
                    @Override
                    public int compare(Post o1, Post o2) {
                        return o2.getTime().compareTo(o1.getTime());
                    }
                });
//                for (Post res : postsList) {
                if (!getDaysAge(result.getTime())) {
                    mAdapter = new PostsAdapter(HomeActivity.this, postsList);
                    mRecyclerView.setAdapter(mAdapter);
                }
//                }
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

        mRootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child(firebaseUser.getUid()).child("friends");
                if (dataSnapshot1 != null) {
                    if (dataSnapshot1.hasChild("currentFriends")) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("currentFriends").getChildren()) {
                            String uid = dataSnapshot2.getKey();
                            Log.d("demo", "UID " + uid);
                            for (DataSnapshot d : dataSnapshot.child(uid).child("messages").getChildren()) {
                                Post res = d.getValue(Post.class);
                                Log.d("demo", "REs " + res.toString());
                                postsList.add(res);
                            }
                        }
                        Collections.sort(postsList, new Comparator<Post>() {
                            @Override
                            public int compare(Post o1, Post o2) {
                                return o2.getTime().compareTo(o1.getTime());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_logout).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }
}
