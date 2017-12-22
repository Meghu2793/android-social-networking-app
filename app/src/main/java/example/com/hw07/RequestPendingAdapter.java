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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
Assignment  : HW07
File Name   : RequestPendingAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class RequestPendingAdapter extends RecyclerView.Adapter<RequestPendingAdapter.RecyclerViewHolder>{

    private ArrayList<Friend> mData;
    private Context mContext;

    public RequestPendingAdapter(Context mContext, ArrayList<Friend> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RequestPendingAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_friend_layout, parent, false);
        RequestPendingAdapter.RecyclerViewHolder viewHolder = new RequestPendingAdapter.RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestPendingAdapter.RecyclerViewHolder holder, final int position) {
        final Friend frnd =(Friend) mData.get(position);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.reqFriendName.setText(frnd.getFriendName());
        if(frnd.getStatus().equalsIgnoreCase("sent")){
            holder.accept.setVisibility(View.INVISIBLE);
        } else if(frnd.getStatus().equalsIgnoreCase("received")) {
            holder.accept.setVisibility(View.VISIBLE);
        }

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frnd.setStatus("");
                mData.remove(frnd);
                notifyDataSetChanged();
                mRootRef.child("users").child(firebaseUser.getUid()).child("friends").child("currentFriends").child(frnd.getId()).setValue(frnd);
                Friend temp1 = new Friend();
                temp1.setId(firebaseUser.getUid());
                temp1.setFriendName(firebaseUser.getDisplayName());
                temp1.setStatus("");
                mRootRef.child("users").child(frnd.getId()).child("friends").child("currentFriends").child(firebaseUser.getUid()).setValue(temp1);

                mRootRef.child("users").child(firebaseUser.getUid()).child("friends").child("requests").child(frnd.getId()).removeValue();
                mRootRef.child("users").child(frnd.getId()).child("friends").child("requests").child(firebaseUser.getUid()).removeValue();
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(frnd);
                notifyDataSetChanged();
                mRootRef.child("users").child(firebaseUser.getUid()).child("friends").child("requests").child(frnd.getId()).removeValue();
                mRootRef.child("users").child(frnd.getId()).child("friends").child("requests").child(firebaseUser.getUid()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView reqFriendName;
        ImageButton accept,reject;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            reqFriendName = (TextView)itemView.findViewById(R.id.friend_name);
            accept = (ImageButton) itemView.findViewById(R.id.accept_yes);
            reject = (ImageButton) itemView.findViewById(R.id.accept_no);
        }
    }
}
