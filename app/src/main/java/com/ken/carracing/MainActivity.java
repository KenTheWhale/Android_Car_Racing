package com.ken.carracing;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar sbCar1, sbCar2, sbCar3, sbCar4;
    private int car1Coin, car2Coin, car3Coin, car4Coin, currentCoin = 100, totalBet;
    private Button btnStart, btnReset;
    private EditText etCar1, etCar2, etCar3, etCar4;
    private TextView tvCurrentCoin;
    private TextView tvUsername;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean isRunning = false;

    private ArrayList<String> carRank = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        String username = (String) getIntent().getSerializableExtra("username");

        sbCar1 = findViewById(R.id.sb_car_1);
        sbCar2 = findViewById(R.id.sb_car_2);
        sbCar3 = findViewById(R.id.sb_car_3);
        sbCar4 = findViewById(R.id.sb_car_4);

        etCar1 = findViewById(R.id.et_car_1);
        etCar2 = findViewById(R.id.et_car_2);
        etCar3 = findViewById(R.id.et_car_3);
        etCar4 = findViewById(R.id.et_car_4);

        tvCurrentCoin = findViewById(R.id.tv_coin);
        tvUsername = findViewById(R.id.tv_username);

        tvUsername.setText("Username: " + username);

        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);

        sbCar1.setEnabled(false);
        sbCar2.setEnabled(false);
        sbCar3.setEnabled(false);
        sbCar4.setEnabled(false);

        updateUserMoneyDisplay();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCoinsAreEmpty(etCar1, etCar2, etCar3, etCar4)) return;

                initData(etCar1);
                initData(etCar2);
                initData(etCar3);
                initData(etCar4);

                car1Coin = Integer.parseInt(etCar1.getText().toString());
                car2Coin = Integer.parseInt(etCar2.getText().toString());
                car3Coin = Integer.parseInt(etCar3.getText().toString());
                car4Coin = Integer.parseInt(etCar4.getText().toString());

                totalBet = car1Coin + car2Coin + car3Coin + car4Coin;
                if (totalBet <= currentCoin) {
                    if (!isRunning) {
                        isRunning = true;
                        startRace();
                        isRunning = false;
                    }
                } else {
                    displayAlert("Your bet coin is over the current coin", "Close");
                }
            }
        });
        //reset cars
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbCar1.setProgress(0);
                sbCar2.setProgress(0);
                sbCar3.setProgress(0);
                sbCar4.setProgress(0);

                etCar1.setText("");
                etCar2.setText("");
                etCar3.setText("");
                etCar4.setText("");
            }
        });
    }

    private void updateUserMoneyDisplay() {
        tvCurrentCoin.setText("Money: " + currentCoin);
    }

    private void startRace() {
        sbCar1.setProgress(0);
        sbCar2.setProgress(0);
        sbCar3.setProgress(0);
        sbCar4.setProgress(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!checkIfAllCarsFinishTheRace(sbCar1, sbCar2, sbCar3, sbCar4)) {

                    if (checkIfCarFinishTheRace(sbCar1)) {
                        carRank.add("1");
                    }

                    if (checkIfCarFinishTheRace(sbCar2)) {
                        carRank.add("2");
                    }

                    if (checkIfCarFinishTheRace(sbCar3)) {
                        carRank.add("3");
                    }

                    if (checkIfCarFinishTheRace(sbCar4)) {
                        carRank.add("4");
                    }

                    int progress1 = random.nextInt(5) + 1;
                    int progress2 = random.nextInt(5) + 1;
                    int progress3 = random.nextInt(5) + 1;
                    int progress4 = random.nextInt(5) + 1;

                    animateSeekBar(sbCar1, sbCar1.getProgress(), sbCar1.getProgress() + progress1);
                    animateSeekBar(sbCar2, sbCar2.getProgress(), sbCar2.getProgress() + progress2);
                    animateSeekBar(sbCar3, sbCar3.getProgress(), sbCar3.getProgress() + progress3);
                    animateSeekBar(sbCar4, sbCar4.getProgress(), sbCar4.getProgress() + progress4);
                    handler.postDelayed(this, 100);
                } else {
                    return;
                }
            }
        }, 100);
    }

    private boolean checkIfCarFinishTheRace(SeekBar currentCar) {
        return currentCar.getProgress() >= currentCar.getMax();
    }

    private boolean checkIfAllCarsFinishTheRace(SeekBar car1, SeekBar car2, SeekBar car3, SeekBar car4) {
        return checkIfCarFinishTheRace(car1)
                && checkIfCarFinishTheRace(car2)
                && checkIfCarFinishTheRace(car3)
                && checkIfCarFinishTheRace(car4);
    }

    private void animateSeekBar(SeekBar seekBar, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(100);
        animator.addUpdateListener(animation -> seekBar.setProgress((int) animation.getAnimatedValue()));
        animator.start();
    }

    private void initData(EditText coin) {
        if (coin.getText().toString().isEmpty()) {
            coin.setText("0");
        }
    }

    private boolean checkCoinsAreEmpty(EditText coin1, EditText coin2, EditText coin3, EditText coin4) {
        if (coin1.getText().toString().isEmpty() && coin2.getText().toString().isEmpty()
                && coin3.getText().toString().isEmpty() && coin4.getText().toString().isEmpty()) {
            displayAlert("There must be at least 1 car bet", "Close");
            return true;
        }
        return false;
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