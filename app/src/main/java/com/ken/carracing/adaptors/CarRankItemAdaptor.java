package com.ken.carracing.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ken.carracing.R;
import com.ken.carracing.models.CarRankItem;

import java.util.ArrayList;

public class CarRankItemAdaptor extends BaseAdapter {

    private Context context;

    private int layout;

    private ArrayList<CarRankItem> items;

    public CarRankItemAdaptor(Context context, int layout, ArrayList<CarRankItem> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView tvRank = convertView.findViewById(R.id.tv_rank);
        TextView tvCarName = convertView.findViewById(R.id.tv_car_name);
        TextView tvBetCoin = convertView.findViewById(R.id.tv_bet_coin);
        TextView tvBonusCoin = convertView.findViewById(R.id.tv_coin_bonus);

        CarRankItem item = items.get(position);
        tvRank.setText(item.getRank());
        tvCarName.setText(item.getName());
        tvBetCoin.setText(item.getBetCoin());
        tvBonusCoin.setText(item.getBonusCoin());

        return convertView;
    }
}
