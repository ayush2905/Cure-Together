package com.example.curetogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.curetogether.home.HomeActivity;
import com.example.curetogether.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextMobile;
    private EditText editTextPassword;
    private Button cirRegisterButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        FirebaseApp.initializeApp(this);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVerificationId != null) {
                    verifyPhoneNumberWithCode();
                } else {
                    startPhoneNumberVerification();
                    Toast.makeText(RegisterActivity.this, "Hold on tight!\nSending OTP", Toast.LENGTH_LONG).show();
                }
            }


        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                cirRegisterButton.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "verification failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(RegisterActivity.this, "Code sent", Toast.LENGTH_LONG).show();
                mVerificationId = s;
                cirRegisterButton.setEnabled(true);
                cirRegisterButton.setText("Verify Code");
            }
        };

    }

    private void verifyPhoneNumberWithCode() {
        if (editTextPassword.getText() == null)
            return;
        if (editTextPassword.getText().toString().trim().equals(""))
            return;
        cirRegisterButton.setEnabled(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, editTextPassword.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            userIsLoggedIn();
                        else
                            Toast.makeText(RegisterActivity.this, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            storeDataInDatabase();
        } else {
            Toast.makeText(RegisterActivity.this, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void storeDataInDatabase() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String age = intent.getStringExtra("AGE");
        String gender = intent.getStringExtra("GENDER");
        String disease = intent.getStringExtra("DISEASE");
        String recovered = intent.getStringExtra("RECOVERED");
        User user;
        user = new User(name, age, gender, disease, recovered);
        user.setUserId(FirebaseAuth.getInstance().getUid());
        store(user);
    }

    private void store(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_INFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DISEASE", user.getUserDisease());
        editor.apply();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("diseases")
                .child(user.getUserDisease())
                .setValue(user);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("diseases")
                .child(user.getUserRecovered())
                .setValue(user);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("user")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startPhoneNumberVerification() {
        if (editTextMobile.getText() == null)
            return;
        if (editTextMobile.getText().toString().trim().equals(""))
            return;
        cirRegisterButton.setEnabled(false);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(editTextMobile.getText().toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}