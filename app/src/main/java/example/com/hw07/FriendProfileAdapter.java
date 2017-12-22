package example.com.hw07;

import android.content.Context;
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
File Name   : FriendProfileAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class FriendProfileAdapter extends RecyclerView.Adapter<FriendProfileAdapter.RecViewHolder>  {
    private ArrayList<Post> mData;
    private Context mContext;

    public FriendProfileAdapter(Context mContext, ArrayList<Post> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public FriendProfileAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friendwall, parent, false);
        FriendProfileAdapter.RecViewHolder viewHolder = new FriendProfileAdapter.RecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendProfileAdapter.RecViewHolder holder, int position) {
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
        public RecViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewFrndWallUsername);
            time = (TextView) itemView.findViewById(R.id.textViewFrndWallTime);
            msg = (TextView) itemView.findViewById(R.id.textviewFrndWallPost_msg);
        }
    }
}

