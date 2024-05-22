package com.ken.carracing;

import android.annotation.SuppressLint;
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
    private EditText confirmedPasswordInput;
    private Button btnCreate;
    private Button btnCancel;

    private HashMap<String, String> accounts = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        usernameInput = findViewById(R.id.inp_username);
        passwordInput = findViewById(R.id.inp_password);
        confirmedPasswordInput = findViewById(R.id.inp_repassword);
        btnCreate = findViewById(R.id.btn_create);
        btnCancel = findViewById(R.id.btn_cancel);

        accounts = (HashMap<String, String>) getIntent().getSerializableExtra("accounts");

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLoginPage();
            }
        });
    }

    private void register() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmedPassword = confirmedPasswordInput.getText().toString().trim();

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

        if (TextUtils.isEmpty(confirmedPassword)) {
            passwordInput.setError("Re-password is required");
            passwordInput.requestFocus();
            return;
        }

        if(!password.equals(confirmedPassword)){
            confirmedPasswordInput.setError("Re-password does not match password");
            confirmedPasswordInput.requestFocus();
            return;
        }

        if (accounts.containsKey(username)) {
            usernameInput.setError("Username already exists");
            usernameInput.requestFocus();
            return;
        }

        accounts.put(username, password);

        Toast.makeText(this, "Register successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("accounts", accounts);
        startActivity(intent);
        finish();
    }

    private void changeLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
