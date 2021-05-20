package com.example.curetogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.curetogether.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class FormActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextGender;
    private Spinner spinnerDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        changeStatusBarColor();

        Button submitButton = (Button) findViewById(R.id.SubmitButton);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextGender = (EditText) findViewById(R.id.editTextGender);
        spinnerDisease = (Spinner) findViewById(R.id.spinnerDisease);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo();
            }
        });

    }

    private void addInfo() {
        if (editTextName.getText() == null || editTextAge.getText() == null || editTextGender.getText() == null) {
            showEmptyWarning();
        }
        if (editTextName.getText().toString().trim().equals("") || editTextAge.getText().toString().trim().equals("") || editTextGender.getText().toString().trim().equals("")) {
            showEmptyWarning();
        }
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String disease = spinnerDisease.getSelectedItem().toString();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("AGE", age);
        intent.putExtra("GENDER", gender);
        intent.putExtra("DISEASE", disease);
        intent.putExtra("RECOVERED", false);
        startActivity(intent);
    }

    private void showEmptyWarning() {
        Toast.makeText(this, "Incomplete details", Toast.LENGTH_LONG).show();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
}