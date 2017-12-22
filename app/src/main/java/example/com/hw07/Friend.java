package example.com.hw07;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/*
Assignment  : HW07
File Name   : Friend.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class Friend {
    String id, friendName, status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> friendResult = new HashMap<>();
        friendResult.put("id", id);
        friendResult.put("friendName", friendName);
        friendResult.put("status", status);

        return friendResult;
    }
}
