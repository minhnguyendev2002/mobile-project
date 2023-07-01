package com.example.btl_android;


import static java.lang.Float.parseFloat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.adapters.PlaceYourOrderAdapter;
import com.example.btl_android.helper.RestaurantHelper;
import com.example.btl_android.model.Menu;
import com.example.btl_android.model.RestaurantModel;
import com.example.btl_android.model.CheckoutModel;

import java.util.List;

public class PlaceyouroderActivity extends AppCompatActivity {
    private RestaurantHelper restaurantHelper;
    private EditText inputName, inputAddress, inputCity, inputCardNumber, inputCardExpiry, inputCardPin ;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean isDeliveryOn;
    private PlaceYourOrderAdapter placeYourOrderAdapter;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeyouroder);
        button = (Button) findViewById(R.id.buttonPlaceYourOrder);

        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(restaurantModel.getName());
            actionBar.setSubtitle(restaurantModel.getAddress());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        restaurantHelper = new RestaurantHelper(this);

        inputName = findViewById(R.id.inputName);
        inputAddress = findViewById(R.id.inputAddress);
        inputCity = findViewById(R.id.inputCity);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputCardExpiry = findViewById(R.id.inputCardExpiry);
        inputCardPin = findViewById(R.id.inputCardPin);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(restaurantModel);
            }
        });

        initRecyclerView(restaurantModel);
        calculateTotalAmount(restaurantModel);
    }
private void calculateTotalAmount(RestaurantModel restaurantModel) {
    if(restaurantModel == null || restaurantModel.getMenus() == null || restaurantModel.getMenus().isEmpty()) {
        // Handle null case or empty list here
        return;
    }

    float subTotalAmount = 0f;

    for(Menu m : restaurantModel.getMenus()) {
        subTotalAmount += m.getPrice() * m.getTotalInCart();
    }

    tvSubtotalAmount.setText("$"+String.format("%.2f", subTotalAmount));
    tvTotalAmount.setText("$"+String.format("%.2f", subTotalAmount));
}


    private void onPlaceOrderButtonClick(RestaurantModel restaurantModel) {
        if(TextUtils.isEmpty(inputName.getText().toString())) {
            inputName.setError("Please enter name ");
            return;
        }else if(TextUtils.isEmpty(inputAddress.getText().toString())) {
            inputAddress.setError("Please enter address ");
            return;
        }else if(TextUtils.isEmpty(inputCity.getText().toString())) {
            inputCity.setError("Please enter city ");
            return;
        }else if(TextUtils.isEmpty(inputCardNumber.getText().toString())) {
            inputCardNumber.setError("Please enter card number ");
            return;
        }else if( TextUtils.isEmpty(inputCardExpiry.getText().toString())) {
            inputCardExpiry.setError("Please enter card expiry ");
            return;
        }else if( TextUtils.isEmpty(inputCardPin.getText().toString())) {
            inputCardPin.setError("Please enter card pin/cvv ");
            return;
        }

        String name = inputName.getText().toString();
        String address = inputAddress.getText().toString();
        String city = inputCity.getText().toString();
        String cardNumber = inputCardNumber.getText().toString();
        String cardExpiry = inputCardExpiry.getText().toString();
        String cardPin = inputCardPin.getText().toString();

        String total = tvTotalAmount.getText().toString();

        restaurantHelper.insertCheckoutInfo(name, address, city, cardNumber, cardExpiry, cardPin, total, getMenuIds(restaurantModel.getMenus()));

        //start success activity..
        Intent i = new Intent(PlaceyouroderActivity.this, SuccessOrderActivity.class);
        i.putExtra("RestaurantModel", restaurantModel);
        startActivityForResult(i, 1000);
    }

    private String getMenuIds(List<Menu> menus) {
        StringBuilder idString = new StringBuilder();

        for (Menu menu : menus) {
            idString.append(menu.getId()).append(",");
        }

        if (idString.length() > 0) {
            idString.deleteCharAt(idString.length() - 1);
        }

        return idString.toString();
    }

    private void initRecyclerView(RestaurantModel restaurantModel) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeYourOrderAdapter = new PlaceYourOrderAdapter(restaurantModel.getMenus());
        cartItemsRecyclerView.setAdapter(placeYourOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}