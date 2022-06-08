package com.example.proyectofinciclo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinciclo.fragments.AudiosFragment;
import com.example.proyectofinciclo.fragments.CuentaFragment;
import com.example.proyectofinciclo.fragments.NotasFragment;
import com.example.proyectofinciclo.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //Toolbar UI
    ImageView imgUser;

    ImageView imgUserNav;
    TextView txvUserName;
    TextView txvUserEmail;

    ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseAuth  firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgUser = findViewById(R.id.img_user);
        toolbar = findViewById(R.id.toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //UI set resources
        imgUser.setImageResource(R.drawable.usuario);

        View nview = navigationView.getHeaderView(0);
        imgUserNav = nview.findViewById(R.id.img_user_nav);
        txvUserName = nview.findViewById(R.id.text_view_nombreusuario);
        txvUserEmail = nview.findViewById(R.id.textView_correo);
        imgUserNav.setImageResource(R.drawable.usuario);

        if (firebaseUser!=null){
            txvUserEmail.setText(firebaseUser.getEmail());
        }


        getSupportFragmentManager().beginTransaction().add(R.id.content, new NotasFragment()).commit();
        //Configuracion del toolbar
        setSupportActionBar(toolbar);


        //Configuracion del toggle
        actionBarDrawerToggle = setUpDrawerToggle();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //Para cambiar el navview segun el item seleccionado en el drawer
        navigationView.setNavigationItemSelectedListener(this);

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser==null){
                    Intent intent = new Intent(getApplicationContext(),SignIn.class);
                    startActivity(intent);
                }
            }
        });

    }

    private  ActionBarDrawerToggle setUpDrawerToggle(){

        return new ActionBarDrawerToggle(

                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close

        );
    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        selectItemNav(item);
        return true;

    }


    private void selectItemNav(MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (item.getItemId()){

            case R.id.nav_item_notas:

                ft.replace(R.id.content, new NotasFragment()).commit();
                break;

            case R.id.nav_item_audios:

                ft.replace(R.id.content, new AudiosFragment()).commit();
                break;
            case R.id.nav_item_cuenta:

                ft.replace(R.id.content, new CuentaFragment()).commit();
                break;

        }

        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}