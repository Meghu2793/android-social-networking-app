package example.com.hw07;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/*
Assignment  : HW07
File Name   : Post.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class Post {
    String username,time, post_message,userId,key;

//    public Post(String username, String post_message, String userId) {
//        this.username = username;
//        this.post_message = post_message;
//        this.userId = userId;
//    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost_message() {
        return post_message;
    }

    public void setPost_message(String post_message) {
        this.post_message = post_message;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);
        result.put("post_message", post_message);
        result.put("time", time);
        result.put("key",key);

        return result;
    }
}
