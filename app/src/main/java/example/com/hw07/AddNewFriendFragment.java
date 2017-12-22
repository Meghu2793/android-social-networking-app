package example.com.hw07;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Assignment  : HW07
File Name   : AddNewFriendFragment.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class AddNewFriendFragment extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String userId;
    ArrayList<Friend> friends;
//    static ArrayList<Friend> removed_addFriend = new ArrayList<>();

    public AddNewFriendFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addnewfriend_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = firebaseUser.getUid();
//        for(UserInfo profile : firebaseUser.getProviderData()){
//            Log.d("demo","Providers "+profile.getUid());
//        }
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.addNewFriend);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Friend> friendArrayList = new ArrayList<>();
                DataSnapshot friendsData = dataSnapshot.child(firebaseUser.getUid()).child("friends");
                DataSnapshot requests = null;
                DataSnapshot currentFriends = null;
                if (friendsData.hasChild("requests")) {
                    requests = dataSnapshot.child(firebaseUser.getUid()).child("friends").child("requests");
                }
                if (friendsData.hasChild("currentFriends")) {
                    currentFriends = dataSnapshot.child(firebaseUser.getUid()).child("friends").child("currentFriends");
                }

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.d("demo", "USERID " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Friend friend = new Friend();
                    if ((userSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) || (requests != null && requests.hasChild(userSnapshot.getKey()))
                            || (currentFriends != null && currentFriends.hasChild(userSnapshot.getKey()))) {

                    } else {
                        friend.setId(userSnapshot.getKey());
                        friend.setFriendName((String) userSnapshot.child("profiles").child("fname").getValue());
                        friendArrayList.add(friend);
                    }
                }
                friends = friendArrayList;
                if (friends != null && !friends.isEmpty()) {
                    mAdapter = new AddFriendAdapter(getActivity(), friends);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}