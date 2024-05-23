package com.ken.carracing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ken.carracing.adaptors.CarRankItemAdaptor;
import com.ken.carracing.models.CarRankItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends Activity {

    private ArrayList<CarRankItem> resultList = new ArrayList<>();
    ListView lvCarRank;
    TextView tvBonusCoin;
    TextView tvTitle;
    Button btnBack;
    private String username = "";
    private int currentCoin = 0;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        currentCoin = (int) getIntent().getSerializableExtra("coin");
        username = (String) getIntent().getSerializableExtra("username");

        resultList = (ArrayList<CarRankItem>) getIntent().getSerializableExtra("result");
        lvCarRank = findViewById(R.id.lv_car_rank);
        tvBonusCoin = findViewById(R.id.tv_bonus_coin);
        tvTitle = findViewById(R.id.tv_result_title);
        btnBack = findViewById(R.id.btn_back);

        CarRankItemAdaptor adaptor = new CarRankItemAdaptor(this, R.layout.car_rank_item_activity, resultList);
        lvCarRank.setAdapter(adaptor);

        int bonus = 0;
        for(int i = 1; i < 5; i++){
            bonus += Integer.parseInt(resultList.get(i).getBonusCoin().trim());
        }

        currentCoin += bonus;

        if(bonus >= 0){
            tvBonusCoin.setTextColor(Color.rgb(121, 180, 101));
            tvBonusCoin.setText("+" + bonus + " coins");
            tvTitle.setText("YOU WIN");
        }else{
            tvBonusCoin.setTextColor(Color.rgb(200, 16, 46));
            tvBonusCoin.setText(bonus  + " coins");
            tvTitle.setText("YOU LOSE");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainLayout();
            }
        });
    }

    private void changeMainLayout(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("coin", currentCoin);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
