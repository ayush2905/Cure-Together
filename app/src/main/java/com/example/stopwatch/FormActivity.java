package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {
    Button SubmitButton;
    EditText editTextName;
    EditText editTextAge;
    EditText editTextGender;
    Spinner spinnerDisease;

    DatabaseReference databaseUser;
    user User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        databaseUser = FirebaseDatabase.getInstance().getReference().child("user");

        changeStatusBarColor();
        SubmitButton = (Button) findViewById(R.id.SubmitButton);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        spinnerDisease = (Spinner) findViewById(R.id.spinnerDisease);
        User = new user();
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo();
                //onSubmitClick();
            }
        });

    }

    //DatabaseReference usersRef= databaseUser.child("users");
    public void addInfo() {
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String disease = spinnerDisease.getSelectedItem().toString();


        String id = databaseUser.push().getKey();
        user User = new user(id, name, age, gender, disease);


        databaseUser.push().setValue(User);

        Toast.makeText(this, "user added", Toast.LENGTH_LONG).show();


    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));

        }
    }

    public void onSubmitClick() {

        startActivity(new Intent(this, QueryActivity.class));

    }
}