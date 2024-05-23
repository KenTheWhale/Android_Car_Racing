package com.ken.carracing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BuyCoinActivity extends Activity {

    TextView coin;
    int currentCoin = 0;
    Button btn100Coin, btn220Coin, btn350Coin, btn480Coin, btnBack;
    String username = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_coin_activity);

        currentCoin = (Integer) getIntent().getSerializableExtra("coin");
        username = (String) getIntent().getSerializableExtra("username");

        coin = findViewById(R.id.tv_coin);
        btn100Coin = findViewById(R.id.btn_100_coin);
        btn220Coin = findViewById(R.id.btn_220_coin);
        btn350Coin = findViewById(R.id.btn_350_coin);
        btn480Coin = findViewById(R.id.btn_480_coin);
        btnBack = findViewById(R.id.btn_back);

        displayCurrentCoin(currentCoin);

        btn100Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCoin = calculateCoin(currentCoin, 100);
                displayCurrentCoin(currentCoin);
            }
        });

        btn220Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCoin = calculateCoin(currentCoin, 220);
                displayCurrentCoin(currentCoin);
            }
        });

        btn350Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCoin = calculateCoin(currentCoin, 350);
                displayCurrentCoin(currentCoin);
            }
        });

        btn480Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCoin = calculateCoin(currentCoin, 480);
                displayCurrentCoin(currentCoin);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePlayLayout();
            }
        });
    }

    private void displayCurrentCoin(int currentCoin){
        coin.setText("Coin: " + currentCoin);
    }

    private int calculateCoin(int currentCoin, int purchasedCoin){
        return currentCoin + purchasedCoin;
    }

    private void changePlayLayout(){
        if(currentCoin == 0){
            new AlertDialog.Builder(this)
                    .setTitle("Please purchase to play this game")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("coin", currentCoin);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
