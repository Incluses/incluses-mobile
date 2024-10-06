package project.interdisciplinary.inclusesapp.Presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EnterpriseProfileActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.HomeEnterprise;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.ChatFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.FeedFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.ProfileSearchFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.VacanciesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private View rootView;

    private ActivityResultLauncher<String> notificationPermissionLauncher;

    private String NOTIFICATION_NAME = "Incluses";
    private String NOTIFICATION_DESC = "Complete seu cadastro! Na tela de Editar Conta!";

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

        // Inicializa o launcher para solicitar a permissão de notificação
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Se a permissão foi concedida, enviar a notificação
                        toNotify();
                    } else {
                        // Se a permissão foi negada, mostrar uma mensagem
                        Toast.makeText(Home.this, "Permissão de notificação negada", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Solicitar permissão de notificação na inicialização
        requestNotificationPermission();


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
                    Intent intent = new Intent(Home.this, UserPerfil.class);
                    intent.putExtra("user_type", "user");
                    startActivity(intent);
                } else if (id == R.id.configurationsMoreOptionsMenu) {
                    Intent intent = new Intent(Home.this, ScreenConfigurations.class);
                    intent.putExtra("user_type", "user");
                    startActivity(intent);
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
                } else if (id == R.id.coursesInitializedMoreOptionsMenu) {
                    startActivity(new Intent(Home.this, CoursesViewed.class));
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
                    // Teclado aberto
                } else {
                    // Teclado fechado
                    binding.nameEditText.clearFocus(); // Limpar o foco
                    binding.searchInputLayout.clearFocus(); // Limpar foco no TextInputLayout
                }
            }
        });
    }

    // Método para solicitar a permissão de notificação
    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar se a permissão já foi concedida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Pedir permissão
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Se já tiver permissão, enviar a notificação
                toNotify();
            }
        } else {
            // Para versões anteriores ao Android 13, não é necessário pedir permissão
            toNotify();
        }
    }

    // Método para enviar a notificação
    public void toNotify() {
        // Criar Intent para abrir a Activity EditAccount
        Intent intent = new Intent(this, EditAccount.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Configurar a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "canal_id")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(NOTIFICATION_NAME)
                .setContentText(NOTIFICATION_DESC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Configurar o canal de notificação (necessário para Android 8 e superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel("canal_id", "Canal de notificação", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(canal);
        }

        // Exibir a notificação
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Caso a permissão ainda não tenha sido concedida
            return;
        }

        // Mostrar a notificação
        notificationManagerCompat.notify(1, builder.build());
    }
}