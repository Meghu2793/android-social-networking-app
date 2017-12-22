package example.com.hw07;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
Assignment  : HW07
File Name   : AddFriendAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.RecyclerViewHolder>{

    private ArrayList<Friend> mData;
    private Context mContext;

    public AddFriendAdapter(Context mContext, ArrayList<Friend> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addnewfriend_layout, parent, false);
        AddFriendAdapter.RecyclerViewHolder viewHolder = new AddFriendAdapter.RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        final Friend frnd =(Friend) mData.get(position);

        holder.friendName.setText(frnd.getFriendName());
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frnd.setStatus("sent");
                final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                mRootRef.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mRootRef.child("users").child(firebaseUser.getUid()).child("friends").child("requests").child(frnd.getId()).setValue(frnd);
                        Friend temp = new Friend();
                        temp.setId(firebaseUser.getUid());
                        temp.setFriendName(firebaseUser.getDisplayName());
                        temp.setStatus("received");
                        mRootRef.child("users").child(frnd.getId()).child("friends").child("requests").child(firebaseUser.getUid()).setValue(temp);
                        mData.remove(frnd);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        ImageButton addFriend;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            friendName = (TextView)itemView.findViewById(R.id.textViewFriendName);
            addFriend = (ImageButton) itemView.findViewById(R.id.imagebuttonFriendAdd);
        }
    }
}
