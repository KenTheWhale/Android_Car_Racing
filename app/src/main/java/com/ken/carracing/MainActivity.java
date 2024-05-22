package com.ken.carracing;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarCar1, seekBarCar2, seekBarCar3, seekBarCar4;
    private int car1Money, car2Money, car3Money, car4Money, userMoney, totalBet;
    private Button startRaceButton, resetRaceButton;
    private EditText car1Bet, car2Bet, car3Bet, car4Bet;
    private TextView moneyView;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean raceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //find views
        seekBarCar1 = findViewById(R.id.seekBarCar1);
        seekBarCar2 = findViewById(R.id.seekBarCar2);
        seekBarCar3 = findViewById(R.id.seekBarCar3);
        seekBarCar4 = findViewById(R.id.seekBarCar4);
        car1Bet = findViewById(R.id.car1Text);
        car2Bet = findViewById(R.id.car2Text);
        car3Bet = findViewById(R.id.car3Text);
        car4Bet = findViewById(R.id.car4Text);
        moneyView = findViewById(R.id.money);
        startRaceButton = findViewById(R.id.startRaceButton);
        resetRaceButton = findViewById(R.id.resetRaceButton);
        //seekbar setting
        seekBarCar1.setEnabled(false);
        seekBarCar2.setEnabled(false);
        seekBarCar3.setEnabled(false);
        seekBarCar4.setEnabled(false);
        //update user money
        userMoney = 10000;
        updateUserMoneyDisplay();
        //start race
        startRaceButton.setOnClickListener(v -> {
            checkBetMoney();
            totalBet = car1Money + car2Money + car3Money + car4Money;
            if(totalBet <= userMoney){
                if (!raceRunning) {
                    raceRunning = true;
                    startRace(result -> {
                        handleRaceResult(result);
                        raceRunning = false;
                    });
                }
            }
        });
        //reset cars
        resetRaceButton.setOnClickListener(v -> {
            seekBarCar1.setProgress(0);
            seekBarCar2.setProgress(0);
            seekBarCar3.setProgress(0);
            seekBarCar4.setProgress(0);
        });
    }
    private void checkBetMoney(){
        try {
            car1Money = Integer.parseInt(car1Bet.getText().toString());
        }catch (NumberFormatException exception){
            car1Money = 0;
        }
        try {
            car2Money = Integer.parseInt(car2Bet.getText().toString());
        }catch (NumberFormatException exception){
            car2Money = 0;
        }
        try {
            car3Money = Integer.parseInt(car3Bet.getText().toString());
        }catch (NumberFormatException exception){
            car3Money = 0;
        }
        try {
            car4Money = Integer.parseInt(car4Bet.getText().toString());
        }catch (NumberFormatException exception){
            car4Money = 0;
        }
    }
    private void updateUserMoneyDisplay() {
        moneyView.setText("" + userMoney);
    }
    private void handleRaceResult(String result) {
        if (result.contains("car 1")) {
            userMoney += car1Money - car2Money - car3Money - car4Money;
        } else if (result.contains("car 2")) {
            userMoney += car2Money - car1Money - car3Money - car4Money;
        } else if (result.contains("car 3")) {
            userMoney += car3Money - car1Money - car2Money - car4Money;
        } else if (result.contains("car 4")) {
            userMoney += car4Money - car1Money - car2Money - car3Money;
        }
        updateUserMoneyDisplay();
    }
    public interface RaceResultCallback {
        void onRaceResult(String result);
    }
    private void startRace(RaceResultCallback raceResultCallback) {
        seekBarCar1.setProgress(0);
        seekBarCar2.setProgress(0);
        seekBarCar3.setProgress(0);
        seekBarCar4.setProgress(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (seekBarCar1.getProgress() >= seekBarCar1.getMax() ||
                        seekBarCar2.getProgress() >= seekBarCar2.getMax() ||
                        seekBarCar3.getProgress() >= seekBarCar3.getMax() ||
                        seekBarCar4.getProgress() >= seekBarCar4.getMax()) {
                    String raceResult;
                    if (seekBarCar1.getProgress() >= seekBarCar1.getMax() &&
                            seekBarCar2.getProgress() >= seekBarCar2.getMax() &&
                            seekBarCar3.getProgress() >= seekBarCar3.getMax() &&
                            seekBarCar4.getProgress() >= seekBarCar4.getMax()) {
                        raceResult = "tie";
                    } else if (seekBarCar1.getProgress() >= seekBarCar1.getMax()) {
                        raceResult = "car 1";
                    } else if (seekBarCar2.getProgress() >= seekBarCar2.getMax()) {
                        raceResult = "car 2";
                    } else if (seekBarCar3.getProgress() >= seekBarCar3.getMax()) {
                        raceResult = "car 3";
                    } else {
                        raceResult = "car 4";
                    }
                    raceResultCallback.onRaceResult(raceResult);
                } else {
                    int progressCar1 = random.nextInt(5) + 1;
                    int progressCar2 = random.nextInt(5) + 1;
                    int progressCar3 = random.nextInt(5) + 1;
                    int progressCar4 = random.nextInt(5) + 1;
                    animateSeekBar(seekBarCar1, seekBarCar1.getProgress(), seekBarCar1.getProgress() + progressCar1);
                    animateSeekBar(seekBarCar2, seekBarCar2.getProgress(), seekBarCar2.getProgress() + progressCar2);
                    animateSeekBar(seekBarCar3, seekBarCar3.getProgress(), seekBarCar3.getProgress() + progressCar3);
                    animateSeekBar(seekBarCar4, seekBarCar4.getProgress(), seekBarCar4.getProgress() + progressCar4);
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }
    private void animateSeekBar(SeekBar seekBar, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(100);
        animator.addUpdateListener(animation -> seekBar.setProgress((int) animation.getAnimatedValue()));
        animator.start();
    }
}