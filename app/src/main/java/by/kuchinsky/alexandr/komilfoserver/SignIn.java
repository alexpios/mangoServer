package by.kuchinsky.alexandr.komilfoserver;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import by.kuchinsky.alexandr.komilfoserver.Common.Common;
import by.kuchinsky.alexandr.komilfoserver.Model.User;
import info.hoang8f.widget.FButton;

public class SignIn extends AppCompatActivity {
FButton sign;
EditText ePhone, ePass;

FirebaseDatabase db;
DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sign = (FButton)findViewById(R.id.btnSignIn);
        ePhone=(MaterialEditText)findViewById(R.id.edtPhone);
        ePass=(MaterialEditText)findViewById(R.id.edtPassword);


        //init Firebase

        db= FirebaseDatabase.getInstance();
       users =  db.getReference("User");
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(ePhone.getText().toString(), ePass.getText().toString());
            }
        });


    }

    private void signInUser(String phone, String pass) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();
        final String UserPhone = phone;
        final String UserPass = pass;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(UserPhone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(UserPhone).getValue(User.class);
                    user.setPhone(UserPhone);
                    if(Boolean.parseBoolean(user.getIsStaff())){ //if isStaff == true
                        if (user.getPassword().equals(UserPass)){
                           // Login ok
                            Intent startuem = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(startuem);
                            finish();
                        }
                        else Toast.makeText(SignIn.this, "Wrong Password! Try again!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(SignIn.this, "Please Login with Staff account", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Данный пользователь не существует", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
