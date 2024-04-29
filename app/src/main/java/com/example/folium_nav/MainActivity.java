package com.example.folium_nav;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.folium_nav.Fragments.CartFragment;
import com.example.folium_nav.Fragments.CompassFragment;
import com.example.folium_nav.Fragments.HomeFragment;
import com.example.folium_nav.Fragments.ItemsFragment;
import com.example.folium_nav.Fragments.LightSensorFragment;
import com.example.folium_nav.Fragments.WelcomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Set up the Toolbar and Drawer
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Account Authorization
        auth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if (auth.getCurrentUser() != null) {
            loadHomeFragment();
        } else {
            loadWelcomeFragment();
        }

    }

    private void loadWelcomeFragment() {
        // Replace the content frame with the WelcomeFragment
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, welcomeFragment)
                .commit();

        // Hide the toolbar
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Home");
        }

        // Hide the drawer
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void loadHomeFragment() {
        // Set up the Toolbar and Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        setToolbar();
        setDefaultFragmentForDefault();
        setUpNavBar();

        // Initialize userName after inflating the layout
        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.nav_user_name);

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        userName.setText(email);

        // Load Fragments
        setDefaultFragmentForDefault();

    }

    private void setUpNavBar() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                boolean fragmentTransaction = false;
                int id = item.getItemId();

                if (id == R.id.menu_Home) {
                    changeFragment(new HomeFragment(), item);
                    getSupportActionBar().setTitle("Home");
                    drawerLayout.closeDrawers();
                } else if (id == R.id.menu_all_plants) {
                    navigateToFragment(null, "All Plants");
                } else if (id == R.id.menu_air_purifying) {
                    navigateToFragment("Air-Purifing", "Air-Purifying");
                }
                else if (id == R.id.menu_cacti) {
                    navigateToFragment("cacti", "Cacti");
                }
                else if (id == R.id.menu_foliage) {
                    navigateToFragment("foliage", "Foliage");
                }
                else if (id == R.id.menu_flowering) {
                    navigateToFragment("flowering", "Flowering");
                }
                else if (id == R.id.menu_succulents) {
                    navigateToFragment("succulents", "Succulents");
                }
                else if (id == R.id.menu_tool_light) {
                    changeFragment(new LightSensorFragment(), item);
                    getSupportActionBar().setTitle("Light Sensor");
                    drawerLayout.closeDrawers();
                } else if (id == R.id.menu_tool_compass) {
                    changeFragment(new CompassFragment(), item);
                    getSupportActionBar().setTitle("Compass");
                    drawerLayout.closeDrawers();

            } else if (id == R.id.menu_cart) {
                changeFragment(new CartFragment(), item);
                getSupportActionBar().setTitle("Cart");
                drawerLayout.closeDrawers();
            }
                else if (id == R.id.menu_reset_pass) {

                    resetPassword();

                    Toast.makeText(MainActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();

                }
                else if (id == R.id.menu_logout) {

                    auth.signOut();
                    loadWelcomeFragment();

                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                }

                if (fragmentTransaction) {
                    changeFragment(fragment, item);
                }

                return true;
            }
        });
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setDefaultFragmentForDefault(){
        changeFragment(new HomeFragment(), navigationView.getMenu().getItem(0));
    }

    private void changeFragment(Fragment fragment, MenuItem item){
        getSupportFragmentManager()
                .beginTransaction().
                replace(R.id.content_frame, fragment)
                .commit();
        item.setChecked(true);
    }

    private void navigateToFragment(String type, String title) {
        ItemsFragment itemsFragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("title", title);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, itemsFragment)
                .addToBackStack(null)
                .commit();

        drawerLayout.closeDrawers();
    }

    private void resetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_login_forgot, null);
        TextInputEditText emailBox = dialogView.findViewById(R.id.forgotPassEmailTIET);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.resetPassBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString();
                if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(view.getContext(), "Enter your registered email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(), "Check you Email", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Unable to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialogView.findViewById(R.id.cancelResetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();

    }
}