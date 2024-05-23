package com.ken.carracing.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CarRankItem implements Parcelable {
    private String name;

    private String rank;

    private String betCoin;

    private String bonusCoin;

    public CarRankItem(String name, String rank, String betCoin, String bonusCoin) {
        this.name = name;
        this.rank = rank;
        this.betCoin = betCoin;
        this.bonusCoin = bonusCoin;
    }

    protected CarRankItem(Parcel in) {
        name = in.readString();
        rank = in.readString();
        betCoin = in.readString();
        bonusCoin = in.readString();
    }

    public static final Creator<CarRankItem> CREATOR = new Creator<CarRankItem>() {
        @Override
        public CarRankItem createFromParcel(Parcel in) {
            return new CarRankItem(in);
        }

        @Override
        public CarRankItem[] newArray(int size) {
            return new CarRankItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBetCoin() {
        return betCoin;
    }

    public void setBetCoin(String betCoin) {
        this.betCoin = betCoin;
    }

    public String getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(String bonusCoin) {
        this.bonusCoin = bonusCoin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(rank);
        dest.writeString(betCoin);
        dest.writeString(bonusCoin);
    }
}
