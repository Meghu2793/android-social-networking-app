package example.com.hw07;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
Assignment  : HW07
File Name   : FriendsAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.RecyclerViewHolder> {

    private ArrayList<Friend> mData;
    private Context mContext;

    public FriendsAdapter(Context mContext, ArrayList<Friend> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public FriendsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);
        FriendsAdapter.RecyclerViewHolder viewHolder = new FriendsAdapter.RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.RecyclerViewHolder holder, int position) {
        final Friend frnd =(Friend) mData.get(position);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.friendName.setText(frnd.getFriendName());
        holder.friend = frnd;

        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(frnd);
                notifyDataSetChanged();
                mRootRef.child("users").child(firebaseUser.getUid()).child("friends").child("currentFriends").child(frnd.getId()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        TextView friendName;
        ImageButton addFriend;
        Friend friend;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            friendName = (TextView)itemView.findViewById(R.id.textViewCurrUsername);
            addFriend = (ImageButton) itemView.findViewById(R.id.imagebuttonCurrFriends);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendsProfile.class);
                    intent.putExtra("id", friend.getId());
                    intent.putExtra("name", friend.getFriendName());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
