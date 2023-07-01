package com.example.btl_android.model;
import android.os.Parcel;
import android.os.Parcelable;

public class CheckoutModel implements Parcelable {

    private int id;
    private String name;
    private String address;
    private String city;
    private String cardNumber;
    private String cardExpiry;
    private String cardPin;
    private String totalAmount;
    private String productIds;


    public CheckoutModel(String name, String address, String city, String cardNumber, String cardExpiry, String cardPin, String totalAmount, String productIds) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardPin = cardPin;
        this.totalAmount = totalAmount;
        this.productIds = productIds;
    }

    protected CheckoutModel(Parcel in) {
        name = in.readString();
        address =in.readString();
        city = in.readString();
        cardNumber =in.readString();
        cardExpiry =in.readString();
        cardPin =in.readString();
        totalAmount = in.readString();
        productIds =in.readString();
    }

    public static final Creator<CheckoutModel> CREATOR = new Creator<CheckoutModel>() {
        @Override
        public CheckoutModel createFromParcel(Parcel in) {
            return new CheckoutModel(in);
        }

        @Override
        public CheckoutModel[] newArray(int size) {
            return new CheckoutModel[size];
        }
    };

    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(cardNumber);
        dest.writeString(cardExpiry);
        dest.writeString(cardPin);
        dest.writeString(totalAmount);
        dest.writeString(productIds);
    }
}
