package com.example.vnzresto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vnzresto.Resto_Admin_Activity.AdminDashBoard;
import com.example.vnzresto.Resto_User_Activity.UserDashBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
EditText login_email, login_pw;
Button login_btn, signup_btn,forgotpw_btn;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        login_email = findViewById(R.id.login_email);
        login_pw = findViewById(R.id.login_pw);
        login_btn = findViewById(R.id.login_btn);
        signup_btn =findViewById(R.id.signup_btn);
        forgotpw_btn = findViewById(R.id.forgotpw_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        forgotpw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

    }
    private void loginUser(){
        String email = login_email.getText().toString().trim();
        String pw = login_pw.getText().toString().trim();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pw)){
            Toast.makeText(this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();
           return;
        }
        firebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"login account",Toast.LENGTH_SHORT).show();
                    String currentEmail = firebaseAuth.getCurrentUser().getEmail();
                    if(currentEmail!= null && currentEmail.equals("admin@gmail.com")){
                        Intent intent = new Intent(Login.this, AdminDashBoard.class);
                        startActivity(intent);

                    }else{
                        Intent intent = new Intent (Login.this, UserDashBoard.class);
                        startActivity(intent);
                    }
                    finish();
                }else{
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "";
                    if (errorMessage.contains("password")) {
                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    } else if (errorMessage.contains("no user record")) {
                        Toast.makeText(Login.this, "No account found with this email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Login failed" ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}