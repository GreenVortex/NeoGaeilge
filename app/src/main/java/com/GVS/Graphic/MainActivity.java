package com.GVS.Graphic;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mEmailText,mPasswordText,mUsername;
    Button mLoginButton,mRegisterButton;
    FirebaseAuth fAuth;
    TextView  txt_animation;
    ProgressBar progress;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting object ID's
        mEmailText = findViewById(R.id.EmailText);
        mPasswordText = findViewById(R.id.PasswordText);
        mUsername = findViewById(R.id.Username);
        mLoginButton = findViewById(R.id.LoginButton);
        mRegisterButton = findViewById(R.id.RegisterButton);
        fAuth = FirebaseAuth.getInstance();
        txt_animation = findViewById(R.id.Square01);
        progress = findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = findViewById(R.id.progressBar);
                String email,password;
                email = mEmailText.getText().toString().trim();
                password = mPasswordText.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmailText.setError("Please supply an email");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    mPasswordText.setError("Please supply an email");
                    return;
                }

                if(password.length() < 6)
                {
                    mPasswordText.setError("Min 6 Charachters required");
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"User logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Content.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error encountered while logging in", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = findViewById(R.id.progressBar);
                final String email,password,username;
                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                email = mEmailText.getText().toString().trim();
                password = mPasswordText.getText().toString().trim();
                username = mUsername.getText().toString().trim();


                if(TextUtils.isEmpty(email))
                {
                    mEmailText.setError("Please supply an email");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    mPasswordText.setError("Please supply a password");
                    return;
                }

                if(password.length() < 6)
                {
                    mPasswordText.setError("Min 6 Charachters required");
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {   String userID;
                            Toast.makeText(getApplicationContext(),"User Created", Toast.LENGTH_SHORT).show();
                            fAuth.signInWithEmailAndPassword(email,password);
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("UID",userID);
                            user.put("Username",username);
                            user.put("Email",email);
                            user.put("Registration password",password);
                            user.put("Date Registered",currentDate);
                            user.put("Time of registration",currentTime);

                            documentReference.set(user);
                            startActivity(new Intent(getApplicationContext(),Content.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error encountered while creating user", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

        //animation
        startAnimation();
    }

            private void startAnimation()
            {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                    txt_animation.startAnimation(animation);
            }

    public void TextThing(View v) {
        final Button RegBtn = (Button) findViewById(R.id.RegisterButton);
        final Button LoginBtn = (Button) findViewById(R.id.LoginButton);
        final TextView ChangerText = (TextView) findViewById(R.id.JoinText);
        final TextView ChangerTextBack = (TextView) findViewById(R.id.ReturnLogin);
        final EditText Username = (EditText) findViewById(R.id.Username);
        RegBtn.setVisibility(View.VISIBLE);
        LoginBtn.setVisibility(View.GONE);
        ChangerText.setVisibility(View.GONE);
        ChangerTextBack.setVisibility(View.VISIBLE);
        Username.setVisibility(View.VISIBLE);
    }

    public void TextThingBack(View v) {
        final Button RegBtn = (Button) findViewById(R.id.RegisterButton);
        final Button LoginBtn = (Button) findViewById(R.id.LoginButton);
        final TextView ChangerText = (TextView) findViewById(R.id.JoinText);
        final TextView ChangerTextBack = (TextView) findViewById(R.id.ReturnLogin);
        final EditText Username = (EditText) findViewById(R.id.Username);
        RegBtn.setVisibility(View.INVISIBLE);
        LoginBtn.setVisibility(View.VISIBLE);
        ChangerText.setVisibility(View.VISIBLE);
        ChangerTextBack.setVisibility(View.GONE);
        Username.setVisibility(View.INVISIBLE);
    }

}
