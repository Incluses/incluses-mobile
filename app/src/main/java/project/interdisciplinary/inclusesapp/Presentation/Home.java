package project.interdisciplinary.inclusesapp.Presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.interdisciplinary.inclusesapp.Presentation.Fragments.ChatFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.FeedFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.ProfileSearchFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.VacanciesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private View rootView;

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.fragmentContainerView);
    }

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

        // Set up the NavigationItemSelectedListener for Navigation Drawer
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;
                int selectedIndex = 0;

                if (id == R.id.feedMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof FeedFragment)) {
                        selectedFragment = new FeedFragment();
                        selectedIndex = 0;
                        updateBottomNavigationSelection(R.id.itemHome);
                    } else {
                        Toast.makeText(Home.this, "Você já está no Feed", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.perfilMoreOptionsMenu) {
                    startActivity(new Intent(Home.this, UserPerfil.class));

                } else if (id == R.id.configurationsMoreOptionsMenu) {
                    startActivity(new Intent(Home.this, ScreenConfigurations.class));

                } else if (id == R.id.positionsAndSalariesMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof VacanciesFragment)) {
                        selectedFragment = new VacanciesFragment();
                        selectedIndex = 1;
                        updateBottomNavigationSelection(R.id.itemVacancies);
                    } else {
                        Toast.makeText(Home.this, "Você já está em Vagas", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.coursesMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof CoursesFragment)) {
                        selectedFragment = new CoursesFragment();
                        selectedIndex = 2;
                        updateBottomNavigationSelection(R.id.itemCourses); // Sincronize com o BottomNavigation
                    } else {
                        Toast.makeText(Home.this, "Você já está em Cursos", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.messagesAndInterviewsMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof ChatFragment)) {
                        selectedFragment = new ChatFragment();
                        selectedIndex = 3;
                        updateBottomNavigationSelection(R.id.itemChat); // Sincronize com o BottomNavigation
                    } else {
                        Toast.makeText(Home.this, "Você já está no Bate-papo", Toast.LENGTH_SHORT).show();
                    }
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    updateNavigationViewSelection(id);
                    moveBar(selectedIndex);
                }

                binding.drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });


        binding.nameEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    replaceFragment(new ProfileSearchFragment());
                }
                return false;
            }
        });


        // Set up the BottomNavigationView to switch fragments
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int selectedIndex = 0;

                if (item.getItemId() == R.id.itemHome) {
                    selectedFragment = new FeedFragment();
                    selectedIndex = 0;
                } else if (item.getItemId() == R.id.itemVacancies) {
                    selectedFragment = new VacanciesFragment();
                    selectedIndex = 1;
                } else if (item.getItemId() == R.id.itemCourses) {
                    selectedFragment = new CoursesFragment();
                    selectedIndex = 2;
                } else if (item.getItemId() == R.id.itemChat) {
                    selectedFragment = new ChatFragment();
                    selectedIndex = 3;
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    moveBar(selectedIndex);
                    return true;
                }
                return false;
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

    private void moveBar(int index) {
        View bar = findViewById(R.id.animated_bar);
        int itemWidth = binding.bottomNavigation.getWidth() / binding.bottomNavigation.getMenu().size();
        int newX = itemWidth * index + (itemWidth - bar.getWidth()) / 2;

        bar.animate()
                .x(newX)
                .setDuration(300)
                .start();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    private void updateNavigationViewSelection(int itemId) {
        Menu menu = binding.navView.getMenu();
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setChecked(true);
        }
    }

    private void updateBottomNavigationSelection(int itemId) {
        Menu menu = binding.bottomNavigation.getMenu();
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
                    binding.searchInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
}