package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.account.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button buttonlogin,buttonrester;
    private EditText editemail,editpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editemail = (EditText) findViewById(R.id.editTextText);
        editpass =  (EditText) findViewById(R.id.editTextTextPassword);
        buttonrester = (Button) findViewById(R.id.buttonRegister);
        buttonlogin = (Button) findViewById(R.id.buttonLogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        buttonrester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });

    }
    private void singup(){
        Intent in =new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(in);
    }
    private void login(){
        String email,pass;
        email = editemail.getText().toString();
        pass = editpass.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter pass!!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Logged in successfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}