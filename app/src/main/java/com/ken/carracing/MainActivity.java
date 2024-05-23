package com.ken.carracing;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ken.carracing.models.CarRankItem;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar sbCar1, sbCar2, sbCar3, sbCar4;
    private int car1Coin, car2Coin, car3Coin, car4Coin, currentCoin = 0, totalBet;
    private Button btnStart, btnReset;
    private EditText etCar1, etCar2, etCar3, etCar4;
    private TextView tvCurrentCoin;
    private TextView tvUsername;
    private Handler handler = new Handler();
    private Random random = new Random();
    private boolean isRunning = false;
    private ArrayList<String> carRank = new ArrayList<>();
    private ArrayList<Integer> carRankCoin = new ArrayList<>();
    private String username = "";

    private ImageView imgHelp, imgSetting;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);

        username = (String) getIntent().getSerializableExtra("username");

        if(getIntent().getSerializableExtra("coin") == null){
            Intent intent = new Intent(this, BuyCoinActivity.class);
            intent.putExtra("coin", currentCoin);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }else{
            currentCoin = (int) getIntent().getSerializableExtra("coin");
            if(currentCoin <= 0){
                currentCoin = 0;
                Intent intent = new Intent(this, BuyCoinActivity.class);
                intent.putExtra("coin", currentCoin);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }

            imgHelp = (ImageView) findViewById(R.id.img_Help);
            imgSetting = (ImageView) findViewById(R.id.img_Setting);


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
            imgHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHelpDialog();
                }
            });
            imgSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    intent.putExtra("coin", currentCoin);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Hướng dẫn cách chơi");

        SpannableString message = new SpannableString(
                "Welcome to our racing track\n" +
                        "Please place your bets in the corresponding boxes below for the race car you trust\n" +
                        "The reward corresponds to the following amount\n" +
                        "The car reaching top 1 receives 200% of the bet amount\n" +
                        "The car reaching top 2 receives 150% of the bet amount\n" +
                        "The cars that reach the top 3 will be loss 150% of the bet amount\n" +
                        "The cars that reach the top 4 will be loss 200% of the bet amount\n" +
                        "Hope you have fun participating"
        );

        String[] lines = message.toString().split("\n");
        int start = 0;
        for (String line : lines) {
            if (line.startsWith("Welcome to our racing track") || line.startsWith("Hope you have fun participating")) {
                message.setSpan(new StyleSpan(Typeface.BOLD), start, start + line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (line.startsWith("The car")) {
                message.setSpan(new ForegroundColorSpan(Color.RED), start, start + line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            start += line.length() + 1;
        }

        builder.setMessage(message);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                if (!checkIfAllCarsFinishTheRace(sbCar1, sbCar2, sbCar3, sbCar4) || carRank.size() < 4) {

                    if (checkIfCarFinishTheRace(sbCar1) && !carRank.contains("Car 1")) {
                        carRank.add("Car 1");
                        carRankCoin.add(car1Coin);
                    }

                    if (checkIfCarFinishTheRace(sbCar2) && !carRank.contains("Car 2")) {
                        carRank.add("Car 2");
                        carRankCoin.add(car2Coin);
                    }

                    if (checkIfCarFinishTheRace(sbCar3) && !carRank.contains("Car 3")) {
                        carRank.add("Car 3");
                        carRankCoin.add(car3Coin);
                    }

                    if (checkIfCarFinishTheRace(sbCar4) && !carRank.contains("Car 4")) {
                        carRank.add("Car 4");
                        carRankCoin.add(car4Coin);
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
                    changeResultLayout();
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

    private int calculateBonus(int position, int betCoin){
        switch (position){
            case 1: return betCoin * 2;
            case 2: return (int) (betCoin * 1.5);
            case 3: return (int) (betCoin * -1.5);
            default: return betCoin * -2;
        }
    }

    private void changeResultLayout(){
        ArrayList<CarRankItem> resultList = new ArrayList<>();
        resultList.add(new CarRankItem("Name", "Top", "Bet", "Bonus"));
        for(int i = 0; i < 4; i++){
            resultList.add(new CarRankItem(carRank.get(i), "" + (i + 1), "" + carRankCoin.get(i), "" + calculateBonus(i + 1, carRankCoin.get(i))));
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("result", resultList);
        intent.putExtra("coin", currentCoin);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

}