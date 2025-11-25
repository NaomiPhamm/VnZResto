package com.example.vnzresto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    Button resetpw_btn,back_btn;
    EditText reset_email;
    ProgressBar forgetpwpb;
    FirebaseAuth mAuth;
    String strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        resetpw_btn = findViewById(R.id.resetpw_btn);
        reset_email = findViewById(R.id.reset_email);
        back_btn = findViewById(R.id.back_btn);
        forgetpwpb = findViewById(R.id.forgetpwpb);
        mAuth = FirebaseAuth.getInstance();
        resetpw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = reset_email.getText().toString().trim();
                if(!TextUtils.isEmpty(strEmail)){
                    resetpw();
                }else{
                    reset_email.setError("Email can not be empty");
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onBackPressed();

            }
        });

    }
    private void resetpw(){
        forgetpwpb.setVisibility(View.VISIBLE);
        resetpw_btn.setVisibility(View.INVISIBLE);
        mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotPassword.this,"Reset Password link has been sent to your email",Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(ForgotPassword.this, Login.class );
              startActivity(intent);
              finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this,"Something wrong",Toast.LENGTH_SHORT).show();
                forgetpwpb.setVisibility(View.VISIBLE);
                resetpw_btn.setVisibility(View.INVISIBLE);
            }
        });
    }
}





























