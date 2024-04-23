package com.example.scripters_society;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class activity_principal extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    CommentFragment commentFragment = new CommentFragment();
    PerfilFragment perfilFragment = new PerfilFragment();
    SearchFragment searchFragment = new SearchFragment();
    public int idHome;
    public int idComment;
    public int idUser;
    public int idBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        idHome = R.id.home;
        idComment = R.id.comment;
        idUser = R.id.user;
        idBuscar = R.id.buscar;

        bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == idHome) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment,homeFragment).commit();
        } else if (id == idBuscar) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment,searchFragment).commit();
        } else if (id == idComment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment,commentFragment).commit();
        } else if (id == idUser) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment,perfilFragment).commit();
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}