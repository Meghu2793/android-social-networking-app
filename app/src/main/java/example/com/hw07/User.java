package example.com.hw07;

/*
Assignment  : HW07
File Name   : User.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class User {
    String fname,lname, email, dob, pass;

    public User(String fname, String lname, String email, String dob, String pass) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.dob = dob;
        this.pass = pass;
    }

    public User(){

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
