package example.com.hw07;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
Assignment  : HW07
File Name   : UpdateProfileActivity.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class UpdateProfileActivity extends AppCompatActivity {

    EditText fname,lname,pass1,pass2,dob;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    Calendar myCalendar;
    Boolean error = false;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String user_id = firebaseUser.getUid();
    String fname_str, lname_str, email_str, dob_str, pass_str, con_pass_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        fname = (EditText)findViewById(R.id.editTextUpdateFirstName);
        lname = (EditText)findViewById(R.id.editTextUpdateLastName);
        pass1 = (EditText)findViewById(R.id.editTextUpdatePwd1);
        pass2 = (EditText)findViewById(R.id.editTextUpdatePwd2);
        dob =(EditText) findViewById(R.id.birthdayUpdate);

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(UpdateProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar cal = Calendar.getInstance();
                cal.set(1850, 1, 1);
                cal.getTimeInMillis();
                dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
                dpd.show();
            }
        });

        findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname_str = fname.getText().toString();
                lname_str = lname.getText().toString();
                pass_str = pass1.getText().toString();
                con_pass_str = pass2.getText().toString();
                dob_str = dob.getText().toString();

                if (fname_str.isEmpty() || fname_str == null) {
                    error = true;
                    Toast.makeText(UpdateProfileActivity.this, "Enter First name to Update", Toast.LENGTH_SHORT).show();
                }
                if (lname_str.isEmpty() || lname_str == null) {
                    error = true;
                    Toast.makeText(UpdateProfileActivity.this, "Enter Last name to Update", Toast.LENGTH_SHORT).show();
                }
                if (dob_str.isEmpty() || dob_str == null) {
                    error = true;
                    Toast.makeText(UpdateProfileActivity.this, "Enter Date of Birth", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar dob1 = Calendar.getInstance();
                    Calendar today = Calendar.getInstance();
                    String[] date = dob_str.split("/");
                    String month = date[0];
                    String day = date[1];
                    String year = date[2];
                    dob1.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                    int age = today.get(Calendar.YEAR) - dob1.get(Calendar.YEAR);
                    if (Integer.parseInt(month) == (today.get(Calendar.MONTH) + 1) && Integer.parseInt(day) > today.get(Calendar.DAY_OF_MONTH)) {
                        age--;
                    }
                    if (age < 13) {
                        error = true;
                        Toast.makeText(UpdateProfileActivity.this, "You should be more than 13 years of age to SignUp. Sorry!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("demo", dob_str);
                }
                if (pass_str.isEmpty() || pass_str == null) {
                    error = true;
                    Toast.makeText(UpdateProfileActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                if (con_pass_str.isEmpty() || con_pass_str == null) {
                    error = true;
                    Toast.makeText(UpdateProfileActivity.this, "Enter Confirmation Password", Toast.LENGTH_SHORT).show();
                }
                if (!pass_str.equals(con_pass_str)) {
                    Toast.makeText(UpdateProfileActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!error) {
                    final User user = new User(fname_str, lname_str, email_str, dob_str, pass_str);

                    firebaseUser = auth.getCurrentUser();
                    String UID = firebaseUser.getUid();
                    firebaseUser.updatePassword(pass_str);

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fname_str +" "+ lname_str)
                            .build();

                    firebaseUser.updateProfile(profileUpdates);

                    mRootRef.child("users").child(user_id).child("profiles").setValue(user);

                    auth.signOut();


                    Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                    //intent.putExtra("user", username_str);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.menu_logout).setEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        menu.findItem(R.id.menu_logout).setEnabled(false);
        return super.onCreateOptionsMenu(menu);

    }
}