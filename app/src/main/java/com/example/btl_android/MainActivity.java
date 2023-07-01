package com.example.btl_android;
import com.facebook.stetho.Stetho;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.adapters.RestaurantListAdapter;
import com.example.btl_android.model.RestaurantModel;
import com.example.btl_android.helper.RestaurantHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RestaurantListAdapter.RestaurantListClickListener {
    private RestaurantHelper restaurantHelper;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initRecyclerView(restaurantModelList);

        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Restaurant List");
        }
        restaurantHelper = new RestaurantHelper(this);

        insertRestaurantsFromRawFile();
        List<RestaurantModel> restaurantModelList = getRestaurantData();
        initRecyclerView(restaurantModelList);
    }

    private void initRecyclerView(List<RestaurantModel> restaurantModelList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<RestaurantModel> getRestaurantData() {
        ArrayList<RestaurantModel> restaurants = restaurantHelper.getAllRestaurants();

        List<RestaurantModel> restaurantModelList = new ArrayList<>();
        for (RestaurantModel restaurant : restaurants) {
            RestaurantModel restaurantModel = new RestaurantModel(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getAddress(),
                    restaurant.getImage()
            );
            restaurantModelList.add(restaurantModel);
        }

        return restaurantModelList;
    }

    private void insertRestaurantsFromRawFile() {
        try {
            InputStream is = getResources().openRawResource(R.raw.restaurent);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray jsonArray = new JSONArray(jsonString.toString());
            restaurantHelper.insertRestaurantsIntoDatabase(jsonArray);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
        Intent intent = new Intent(MainActivity.this, RestaurantMenuActivity.class);
        intent.putExtra("RestaurantModel", restaurantModel);

        startActivity(intent);
    }
}