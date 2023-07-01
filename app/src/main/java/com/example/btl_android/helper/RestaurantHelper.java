package com.example.btl_android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.example.btl_android.model.RestaurantModel;
import com.example.btl_android.model.Menu;

import java.util.ArrayList;

public class RestaurantHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurant_final.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESTAURANT = "restaurants";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_CHECKOUT = "checkouts";

    private static final String CREATE_TABLE_RESTAURANT = "CREATE TABLE " + TABLE_RESTAURANT + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, image TEXT)";
    private static final String CREATE_TABLE_CHECKOUT = "CREATE TABLE " + TABLE_CHECKOUT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, city TEXT, state TEXT, cardNumber TEXT, cardExpiry TEXT, cardPin TEXT, totalAmount TEXT, productIds TEXT)";
    private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, url TEXT, restaurant_id INTEGER, FOREIGN KEY (restaurant_id) REFERENCES restaurant(id))";

    public RestaurantHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_CHECKOUT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable_checkout_Query = "DROP TABLE IF EXISTS " + TABLE_CHECKOUT;
        String dropTable_product_Query = "DROP TABLE IF EXISTS " + TABLE_PRODUCT;
        String dropTable_restaurant_Query = "DROP TABLE IF EXISTS " + TABLE_RESTAURANT;
        db.execSQL(dropTable_checkout_Query);
        db.execSQL(dropTable_product_Query);
        db.execSQL(dropTable_restaurant_Query);
        onCreate(db);
    }

    public void insertCheckoutInfo(String name, String address, String city, String cardNumber, String cardExpiry, String cardPin, String totalAmount, String productIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("cardNumber", cardNumber);
        values.put("cardExpiry", cardExpiry);
        values.put("cardPin", cardPin);
        values.put("totalAmount", totalAmount);
        values.put("productIds", productIds);

        long result = db.insert(TABLE_CHECKOUT, null, values);
        db.close();

        if (result == -1) {
            Log.d("FAIL", "Failed to insert Checkout info into database");
        } else {
            Log.d("SUCCESS", "Checkout info inserted successfully into database");
        }
    }

    public ArrayList<Menu> getMenuWithId(int restaurant_id) {
        ArrayList<Menu> menuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PRODUCT + " WHERE restaurant_id = ?";

        String[] selectionArgs = { String.valueOf(restaurant_id) };
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
                String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));

                Menu menu = new Menu(id, name, price, url);
                menuList.add(menu);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return menuList;
    }

    public void insertRestaurantsIntoDatabase(JSONArray jsonArray) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject restaurantObject = jsonArray.getJSONObject(i);

                String name = restaurantObject.getString("name");
                String address = restaurantObject.getString("address");
                String image = restaurantObject.getString("image");

                ContentValues restaurantValues = new ContentValues();
                restaurantValues.put("name", name);
                restaurantValues.put("address", address);
                restaurantValues.put("image", image);

                long restaurantId = db.insert(TABLE_RESTAURANT, null, restaurantValues);

                JSONArray menusArray = restaurantObject.getJSONArray("menus");

                for (int j = 0; j < menusArray.length(); j++) {
                    JSONObject menuObject = menusArray.getJSONObject(j);

                    String menuName = menuObject.getString("name");
                    double price = menuObject.getDouble("price");
                    String url = menuObject.getString("url");

                    ContentValues productValues = new ContentValues();
                    productValues.put("name", menuName);
                    productValues.put("price", price);
                    productValues.put("url", url);
                    productValues.put("restaurant_id", restaurantId);

                    db.insert(TABLE_PRODUCT, null, productValues);
                }
            }
        } catch (JSONException e) {
            Log.e("InsertError", "Error inserting data into SQLite database: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public ArrayList<RestaurantModel> getAllRestaurants() {
        ArrayList<RestaurantModel> restaurantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String tableName = "restaurants";
        String[] columns = { "id", "name", "address", "image" };

        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                RestaurantModel restaurant = new RestaurantModel(id, name, address, image);
                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return restaurantList;
    }
}
