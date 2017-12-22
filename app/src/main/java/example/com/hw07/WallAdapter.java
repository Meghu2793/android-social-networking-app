package example.com.hw07;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/*
Assignment  : HW07
File Name   : WallAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.RecViewHolder> {
    private ArrayList<Post> mData;
    private Context mContext;

    public WallAdapter(Context mContext, ArrayList<Post> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public WallAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile, parent, false);
        WallAdapter.RecViewHolder viewHolder = new WallAdapter.RecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WallAdapter.RecViewHolder holder, int position) {
        final Post posts = mData.get(position);
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String dateValue = posts.getTime();
        PrettyTime p = new PrettyTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        String datetime = p.format(new Date(millis));
        holder.name.setText(posts.getUsername());
        holder.msg.setText(posts.getPost_message());
        holder.time.setText(datetime);

        Collections.sort(mData, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to delete the post?")
                        .setCancelable(true)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            mRootRef.child("users").child(firebaseUser.getUid()).child("messages").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                        if (posts.getKey().equals(userSnapshot.getKey())) {
                                                            mData.remove(posts);
                                                            notifyDataSetChanged();
                                                            mRootRef.child("users").child(firebaseUser.getUid()).child("messages").child(posts.getKey()).removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class RecViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, msg;
        ImageButton imageButton;

        public RecViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewWallUsername);
            time = (TextView) itemView.findViewById(R.id.textViewWallTime);
            msg = (TextView) itemView.findViewById(R.id.textviewWallPost_msg);
            imageButton = (ImageButton) itemView.findViewById(R.id.imagebuttonWallDelete);
        }
    }
}
