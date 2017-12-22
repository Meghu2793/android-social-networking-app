package example.com.hw07;

import android.app.Fragment;
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
File Name   : RequestPendingFragment.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class RequestPendingFragment extends android.support.v4.app.Fragment {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<Friend> req_friends;

    public RequestPendingFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String UID = firebaseUser.getUid();

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.requestPendingFriend);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot.child(UID).child("friends");
                if(snapshot != null){
                    if(snapshot.hasChild("requests")){
                        DataSnapshot requests = dataSnapshot.child(UID).child("friends").child("requests");
                        ArrayList<Friend> requestFriend = new ArrayList<>();
                        for(DataSnapshot snapshot1: requests.getChildren()){
                            Friend friend = new Friend();
                            friend.setId(snapshot1.child("id").getValue(String.class));
                            friend.setFriendName(snapshot1.child("friendName").getValue(String.class));
                            friend.setStatus(snapshot1.child("status").getValue(String.class));
                            requestFriend.add(friend);
                        }

                        req_friends = requestFriend;
                        if(req_friends!=null && !req_friends.isEmpty()){
                            mAdapter = new RequestPendingAdapter(getActivity(), req_friends);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"No Requests Present",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
//                    Toast.makeText(getActivity(),"No Friends",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestspending_fragment, container, false);
        return view;
    }

}
