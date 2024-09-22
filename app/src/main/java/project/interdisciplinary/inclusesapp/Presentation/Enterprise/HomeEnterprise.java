package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.CoursesEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.FeedEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.VacanciesEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.ScreenConfigurations;
import project.interdisciplinary.inclusesapp.Presentation.UserPerfil;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeEnterpriseBinding;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.*;

public class HomeEnterprise extends AppCompatActivity {

    private ActivityHomeEnterpriseBinding binding;
    private View rootView;

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.fragmentContainerViewEnterprise);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityHomeEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rootView = binding.getRoot();

        setupKeyboardListener();

        // Configuração do clique na imagem de perfil
        binding.perfilImageViewEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeEnterprise.this, UserPerfil.class));
            }
        });

        // Configuração do clique no ícone de "mais opções"
        binding.moreImageViewEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.drawerLayoutEnterprise.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayoutEnterprise.closeDrawer(GravityCompat.END);
                } else {
                    binding.drawerLayoutEnterprise.openDrawer(GravityCompat.END);
                }
            }
        });

        // Configuração do NavigationItemSelectedListener para o Drawer de navegação
        binding.navViewEnterprise.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment selectedFragment = null;
                int selectedIndex = 0;

                if (id == R.id.feedMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof FeedEnterpriseFragment)) {
                        selectedFragment = new FeedEnterpriseFragment();
                        selectedIndex = 0;
                        updateBottomNavigationSelection(R.id.itemHome);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está no Feed", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.perfilMoreOptionsMenu) {
                    startActivity(new Intent(HomeEnterprise.this, UserPerfil.class));

                } else if (id == R.id.configurationsMoreOptionsMenu) {
                    startActivity(new Intent(HomeEnterprise.this, ScreenConfigurations.class));

                } else if (id == R.id.positionsAndSalariesMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof VacanciesEnterpriseFragment)) {
                        selectedFragment = new VacanciesEnterpriseFragment();
                        selectedIndex = 1;
                        updateBottomNavigationSelection(R.id.itemVacancies);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está em Vagas", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.coursesMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof CoursesEnterpriseFragment)) {
                        selectedFragment = new CoursesEnterpriseFragment();
                        selectedIndex = 2;
                        updateBottomNavigationSelection(R.id.itemCourses);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está em Cursos", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.messagesAndInterviewsMoreOptionsMenu) {
                    if (!(getCurrentFragment() instanceof ChatFragment)) {
                        selectedFragment = new ChatFragment();
                        selectedIndex = 3;
                        updateBottomNavigationSelection(R.id.itemChat);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está no Bate-papo", Toast.LENGTH_SHORT).show();
                    }
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    updateNavigationViewSelection(id);
                    moveBar(selectedIndex);
                }

                binding.drawerLayoutEnterprise.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // Configuração para o clique no campo de texto
        binding.nameEditTextEnterprise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    replaceFragment(new ProfileSearchFragment());
                }
                return false;
            }
        });

        // Configuração do BottomNavigationView para alternar os fragmentos
        binding.bottomNavigationEnterprise.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int selectedIndex = 0;

                if (item.getItemId() == R.id.itemHome) {
                    selectedFragment = new FeedEnterpriseFragment();
                    selectedIndex = 0;
                } else if (item.getItemId() == R.id.itemVacancies) {
                    selectedFragment = new VacanciesEnterpriseFragment();
                    selectedIndex = 1;
                } else if (item.getItemId() == R.id.itemCourses) {
                    selectedFragment = new CoursesEnterpriseFragment();
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

        // Definir o item padrão selecionado na NavigationView
        updateNavigationViewSelection(R.id.feedMoreOptionsMenu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationViewSelection(R.id.feedMoreOptionsMenu);
    }

    private void moveBar(int index) {
        View bar = findViewById(R.id.animated_barEnterprise);
        int itemWidth = binding.bottomNavigationEnterprise.getWidth() / binding.bottomNavigationEnterprise.getMenu().size();
        int newX = itemWidth * index + (itemWidth - bar.getWidth()) / 2;

        bar.animate()
                .x(newX)
                .setDuration(300)
                .start();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerViewEnterprise, fragment);
        fragmentTransaction.commit();
    }

    private void updateNavigationViewSelection(int itemId) {
        Menu menu = binding.navViewEnterprise.getMenu();
        MenuItem item = menu.findItem(itemId);
        if (item != null) {
            item.setChecked(true);
        }
    }

    private void updateBottomNavigationSelection(int itemId) {
        Menu menu = binding.bottomNavigationEnterprise.getMenu();
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
                    // Teclado aberto
                } else {
                    // Teclado fechado
                    binding.nameEditTextEnterprise.clearFocus(); // Limpar o foco
                    binding.nameInputLayoutEnterprise.clearFocus(); // Limpar foco no TextInputLayout
                }
            }
        });
    }
}
