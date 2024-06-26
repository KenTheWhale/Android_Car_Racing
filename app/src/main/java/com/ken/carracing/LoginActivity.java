package com.ken.carracing;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;

public class LoginActivity extends Activity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button btnSignIn;
    private Button btnSignUp;

    private AudioManager audioManager;
    MediaPlayer song;

    private  HashMap<String, String> accounts = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
//        song = MediaPlayer.create(LoginActivity.this,R.raw.sword);
//        song.start();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int savedVolume = preferences.getInt("volume_level", 50);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0);

        accounts.put("user1", "123");
        accounts.put("user2", "abc");
        accounts.put("user3", "test");

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        if((getIntent().getSerializableExtra("accounts")) != null){
            accounts = (HashMap<String, String>) (getIntent().getSerializableExtra("accounts"));
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignUpPage();
            }
        });
    }
    private void Login() {

        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

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

        if (checkAccount(username, password)) {
//            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", username);

            displayAlert("Login successfull", intent);
        } else {
            displayAlert("Invalid username or password", "close");
        }
    }

    private boolean checkAccount(String username, String password) {
        return accounts.containsKey(username) && accounts.get(username).equals(password);
    }

    private void changeSignUpPage(){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("accounts", accounts);
        startActivity(intent);
        finish();
    }
    private void displayAlert(String message, Intent intent) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(message)
                .setIcon(R.drawable.success)
                .setCancelable(false)
                .create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        }, 2000); // 2000 milliseconds = 2 seconds
    }

    private void displayAlert(String message, String closeBtnName) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setPositiveButton(closeBtnName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
