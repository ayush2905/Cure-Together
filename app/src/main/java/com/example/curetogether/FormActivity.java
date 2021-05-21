package com.example.curetogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.curetogether.model.User;
import com.example.curetogether.utility.CustomDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextGender;
    private EditText editTextDisease, editTextRecovered;
    private Spinner spinnerDisease, spinnerRecovered;
    private CustomDialog dialog;
    private List<String> diseases = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        changeStatusBarColor();

        dialog = new CustomDialog(this);
        getDiseaseList();
        Button submitButton = findViewById(R.id.SubmitButton);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGender = findViewById(R.id.editTextGender);
        spinnerDisease = findViewById(R.id.spinnerDisease);
        spinnerRecovered = findViewById(R.id.spinnerRecovered);
        editTextDisease = findViewById(R.id.editTextDisease);
        editTextRecovered = findViewById(R.id.editTextRecovered);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfo();
            }
        });

    }

    private void getDiseaseList() {
        dialog.start();
        FirebaseDatabase.getInstance().getReference()
                .child("diseases")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        dialog.stop();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            diseases.add(dataSnapshot.getKey());
                        }
                        while (diseases.contains("")) {
                            diseases.remove("");
                        }
                        Collections.sort(diseases);
                        diseases.add(0, "Enter above if option not available");
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(FormActivity.this, android.R.layout.simple_spinner_item, diseases);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDisease.setAdapter(spinnerAdapter);
                        spinnerRecovered.setAdapter(spinnerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        dialog.stop();
                        Toast.makeText(FormActivity.this, "Problem fetching disease list", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addInfo() {
        if (editTextName.getText() == null || editTextAge.getText() == null || editTextGender.getText() == null) {
            showEmptyWarning();
            return;
        }
        if (editTextName.getText().toString().trim().equals("") || editTextAge.getText().toString().trim().equals("") || editTextGender.getText().toString().trim().equals("")) {
            showEmptyWarning();
            return;
        }
        if (editTextDisease.getText() == null && (spinnerDisease.getSelectedItemPosition() == 0 || spinnerDisease.getSelectedItemPosition() == -1)
                && editTextRecovered.getText() == null  && (spinnerRecovered.getSelectedItemPosition() == 0 || spinnerRecovered.getSelectedItemPosition() != -1)) {
            showEmptyWarning();
            return;
        }
        if (editTextDisease.getText().toString().equals("") && (spinnerDisease.getSelectedItemPosition() == 0 || spinnerDisease.getSelectedItemPosition() == -1)
                && editTextRecovered.getText().toString().equals("") && (spinnerRecovered.getSelectedItemPosition() == 0 || spinnerRecovered.getSelectedItemPosition() != -1)) {
            showEmptyWarning();
            return;
        }
        String disease = editTextDisease.getText().toString();
        String recovered = editTextRecovered.getText().toString();
        if (disease.trim().equals("")) {
            if (spinnerDisease.getSelectedItemPosition() != 0 && spinnerDisease.getSelectedItemPosition() != -1)
                disease = spinnerDisease.getSelectedItem().toString();
        }
        if (recovered.trim().equals("")) {
            if (spinnerRecovered.getSelectedItemPosition() != -1 && spinnerRecovered.getSelectedItemPosition() != 0)
                recovered = spinnerRecovered.getSelectedItem().toString();
        }
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("NAME", name);
        intent.putExtra("AGE", age);
        intent.putExtra("GENDER", gender.toUpperCase());
        intent.putExtra("DISEASE", disease.toUpperCase());
        intent.putExtra("RECOVERED", recovered.toUpperCase());
        startActivity(intent);
        finish();
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