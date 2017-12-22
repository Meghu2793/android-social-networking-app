package example.com.hw07;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
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
File Name   : PostsAdapter.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.RecViewHolder> {
    private ArrayList<Post> mData;
    private Context mContext;

    public PostsAdapter(Context mContext, ArrayList<Post> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_posts, parent, false);
        RecViewHolder viewHolder = new RecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecViewHolder holder, int position) {
        final Post posts = (Post) mData.get(position);
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
        Log.d("demo", "date  on parsing " + date);
        long millis = date.getTime();
        String datetime = p.format(new Date(millis));
        Log.d("demo", "datetime in adapter display" + datetime);

        holder.name.setText(posts.getUsername());
        holder.post_message.setText(posts.getPost_message());
        holder.time.setText(datetime);

//        Collections.sort(mData, new Comparator<Post>() {
//            @Override
//            public int compare(Post o1, Post o2) {
//                return o2.getTime().compareTo(o1.getTime());
//            }
//        });

        if (!firebaseUser.getDisplayName().equalsIgnoreCase(posts.getUsername())) {
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FriendsProfile.class);
                    intent.putExtra("id", posts.getUserId());
                    intent.putExtra("name", posts.getUsername());
                    mContext.startActivity(intent);
                }
            });
        }
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
        TextView name, time, post_message;

        public RecViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewUsername);
            time = (TextView) itemView.findViewById(R.id.textViewPostTime);
            post_message = (TextView) itemView.findViewById(R.id.textviewPost_msg);
        }
    }
}
