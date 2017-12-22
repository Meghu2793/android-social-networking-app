package example.com.hw07;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
Assignment  : HW07
File Name   : FriendsFragment.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class FriendsFragment extends android.support.v4.app.Fragment {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<Friend> currFriends;

    public FriendsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String UID = firebaseUser.getUid();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.friends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child(UID).child("friends");
                if(dataSnapshot1 !=null) {
                    if(dataSnapshot1.hasChild("currentFriends")){
                        DataSnapshot currentFriends = dataSnapshot.child(UID).child("friends").child("currentFriends");
                        ArrayList<Friend> currentFriendList = new ArrayList<>();
                        for(DataSnapshot snapshot1: currentFriends.getChildren()){
                            Friend friend = new Friend();
                            friend.setId(snapshot1.child("id").getValue(String.class));
                            friend.setFriendName(snapshot1.child("friendName").getValue(String.class));
                            friend.setStatus(snapshot1.child("status").getValue(String.class));
                            currentFriendList.add(friend);
                        }

                        currFriends = currentFriendList;
                        if(currFriends!=null && !currFriends.isEmpty()){
                            mAdapter = new FriendsAdapter(getActivity(), currFriends);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                    else {
//                        Toast.makeText(getContext(),"No Current Friends Present",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
//                    Toast.makeText(getActivity(),"No Friends",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
