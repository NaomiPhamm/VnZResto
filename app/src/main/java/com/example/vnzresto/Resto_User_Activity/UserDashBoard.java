package com.example.vnzresto.Resto_User_Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.vnzresto.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class UserDashBoard extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageButton notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        notificationButton = findViewById(R.id.notifictionBotton);

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);


        NavigationUI.setupWithNavController(bottomNavigationView, navController);



        notificationButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashBoard.this, NotificationActivity.class);
            startActivity(intent);
        });
    }
}
