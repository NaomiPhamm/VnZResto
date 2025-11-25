package com.example.vnzresto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vnzresto.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText signup_name,signup_email,signup_phone,signup_pw, signup_cpw,signup_address;
    Button signupreg_btn,loginreg_btn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        signup_name  = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_phone  = findViewById(R.id.signup_phone);
        signup_address = findViewById(R.id.signup_address);
        signup_pw  = findViewById(R.id.signup_pw);
        signup_cpw  = findViewById(R.id.signup_cpw);
        signupreg_btn = findViewById(R.id.signupreg_btn);
        loginreg_btn = findViewById(R.id.loginreg_btn);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        loginreg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        signupreg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){
        String name = signup_name.getText().toString().trim();
        String email = signup_email.getText().toString().trim();
        String phone = signup_phone.getText().toString().trim();
        String address = signup_address.getText().toString().trim();
        String pass = signup_pw.getText().toString().trim();
        String confirmPass = signup_cpw.getText().toString().trim();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(email)|| TextUtils.isEmpty(phone)|| TextUtils.isEmpty(pass)|| TextUtils.isEmpty(confirmPass)){
            Toast.makeText(this, "Please fill all the field", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signup_email.setError("Invalid Email");
            signup_email.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phone).matches()){
            signup_phone.setError("Invalid Phone");
            signup_phone.requestFocus();
            return;
        }
        if (phone.length()<10){
            signup_phone.setError("Phone should be 10 digits");
        }
        if(pass.length()<8){
            signup_pw.setError("Password should be more than 8 characters");
            return;
        }
        if(!pass.equals(confirmPass)){
            signup_cpw.setError("Passwords does not match");
            signup_cpw.requestFocus();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserModel userModel = new UserModel(name,email,phone,address,pass);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("user").child(id).setValue(userModel);
                    Toast.makeText(Register.this, "success register",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();
                }else {
                    Toast.makeText(Register.this,"SOmething wrong",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}








































































