package example.com.hw07;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
File Name   : LoginActivity.java
Full Name   : Monisha Javali Veerabhadran, Megha Krishnamurthy
Group       : Group 18
*/

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    Button signUp, cancel;
    Boolean error = false;
    EditText fname, lname, email, dob, pass, con_pass;
    String fname_str, lname_str, email_str, dob_str, pass_str, con_pass_str;
    Calendar myCalendar;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("My Social App");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon_menu);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fname = (EditText) findViewById(R.id.editTextFirstName);
        lname = (EditText) findViewById(R.id.editTextLastName);
        email = (EditText) findViewById(R.id.editTextRegUserName);
        pass = (EditText) findViewById(R.id.editTextRegPwd1);
        dob = (EditText) findViewById(R.id.birthday);
        con_pass = (EditText) findViewById(R.id.editTextRegPwd2);

        signUp = (Button)findViewById(R.id.buttonSignUpReg);
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
                //str = sdf.format(myCalendar.getTime());

                dob.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(LoginActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar cal = Calendar.getInstance();
                cal.set(1850, 1, 1);
                cal.getTimeInMillis();
                dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
                dpd.show();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname_str = fname.getText().toString();
                lname_str = lname.getText().toString();
                email_str = email.getText().toString();
                pass_str = pass.getText().toString();
                con_pass_str = con_pass.getText().toString();
                dob_str = dob.getText().toString();

                if (fname_str.isEmpty() || fname_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter First name", Toast.LENGTH_SHORT).show();
                }
                if (lname_str.isEmpty() || lname_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter Last name", Toast.LENGTH_SHORT).show();
                }
                if (email_str.isEmpty() || email_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                if (dob_str.isEmpty() || dob_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter Date of Birth", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, "You should be more than 13 years of age to SignUp. Sorry!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("demo", dob_str);
                }
                if (pass_str.isEmpty() || pass_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                if (con_pass_str.isEmpty() || con_pass_str == null) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Enter Confirmation Password", Toast.LENGTH_SHORT).show();
                }
                if (!pass_str.equals(con_pass_str)) {
                    Toast.makeText(LoginActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass_str.equals(con_pass_str)) {
                    Toast.makeText(LoginActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!error) {
                    final User user = new User(fname_str, lname_str, email_str, dob_str, pass_str);
                    try {
                        auth.createUserWithEmailAndPassword(email_str, pass_str)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = auth.getCurrentUser();
                                            String UID = firebaseUser.getUid();
                                            String username = fname_str;

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fname_str).build();
                                            firebaseUser.updateProfile(profileUpdates);

                                            mRootRef.child("users").child(UID).child("profiles").setValue(user);
                                            Toast.makeText(LoginActivity.this, "User has been created", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("user", email_str);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.d("demo ", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "User exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        Log.d("Demo", "COming inside exception");
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
