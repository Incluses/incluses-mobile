package project.interdisciplinary.inclusesapp.Presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rootView = binding.getRoot();

        setupKeyboardListener();

        binding.perfilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, UserPerfil.class));
            }
        });

        // Set up the drawer layout
        binding.moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });



        // Set up the NavigationItemSelectedListener
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.feedMoreOptionsMenu) {
                    Toast.makeText(Home.this, "Você já esta no Feed", Toast.LENGTH_SHORT).show();
//                } else if (id == R.id.positionsAndSalariesMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, PositionsAndSalariesActivity.class));
//                } else if (id == R.id.entrepreneurshipAndCoursesMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, coursesActivity.class));
//                } else if (id == R.id.messagesAndInterviewsMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, MessagesAndInterviewsActivity.class));
                } else if (id == R.id.perfilMoreOptionsMenu) {
                    startActivity(new Intent(Home.this, UserPerfil.class));
                }
//                } else if (id == R.id.centerOfHelpMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, HelpCenterActivity.class));
//                } else if (id == R.id.termsAndPrivacyPolicyMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, TermsAndPrivacyPolicyActivity.class));
//                } else if (id == R.id.configurationsMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, ConfigurationsActivity.class));
//                } else if (id == R.id.coursesInitializedMoreOptionsMenu) {
//                    startActivity(new Intent(Home.this, CoursesInitializedActivity.class));
//                }

                // Clear the selection
                binding.navView.setCheckedItem(R.id.feedMoreOptionsMenu);

                // Close the drawer when the item is clicked
                binding.drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // Update navigation view selection
        updateNavigationViewSelection(R.id.feedMoreOptionsMenu); // Define the default selected item
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assuming you want to show the feed item as selected when returning
        updateNavigationViewSelection(R.id.feedMoreOptionsMenu);
    }

    private void updateNavigationViewSelection(int itemId) {
        Menu menu = binding.navView.getMenu();
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setChecked(true);
        }
    }

    private void setupKeyboardListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is opened
                } else {
                    // Keyboard is closed
                    binding.nameEditText.clearFocus(); // Clear focus
                    binding.nameInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
}