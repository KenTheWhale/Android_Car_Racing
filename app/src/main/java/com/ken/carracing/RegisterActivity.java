package com.ken.carracing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText birthdayInput;
    private EditText idCardInput;
    private Button btnSignUp;
    private Button btnCancel;

    private final HashMap<String, String> accounts = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        birthdayInput = findViewById(R.id.birthdayInput);
        idCardInput = findViewById(R.id.idCardInput);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnCancel = findViewById(R.id.btnCancel);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String birthday = birthdayInput.getText().toString().trim();
        String idCard = idCardInput.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(birthday)) {
            birthdayInput.setError("Birthday is required");
            birthdayInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(idCard)) {
            idCardInput.setError("ID Card is required");
            idCardInput.requestFocus();
            return;
        }

        if (accounts.containsKey(username)) {
            usernameInput.setError("Username already exists");
            usernameInput.requestFocus();
            return;
        }

        accounts.put(username, password);

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
