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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EnterpriseProfileActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.HomeEnterprise;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.ChatFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.FeedFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.ProfileSearchFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.VacanciesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private View rootView;
    private Retrofit retrofit;

    private String token;
    private Usuario usuario;
    private String perfil;
    private DatabaseFirebase firebase = new DatabaseFirebase();

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

        //Pegando os dados do SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        token = preferences.getString("token", "");
        perfil = preferences.getString("perfil", "");

        findUser(String.valueOf(ConvertersToObjects.convertStringToPerfil(perfil).getId()), new UsuarioCallback() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                Gson gson = new Gson();
                usuario = gson.fromJson(jsonObject, Usuario.class);
                editor.putString("usuario", jsonObject.toString());
                editor.apply();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
            }
        });

        // Solicitar permissão de notificação na inicialização
        requestNotificationPermission();

        // Inicializa o launcher para solicitar a permissão de notificação
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Se a permissão foi concedida, enviar a notificação
                        if (usuario != null && (usuario.getNomeSocial() == null || usuario.getPerfil().getBiografia() == null || usuario.getPerfil().getFotoPerfil() == null)) {
                            toNotify();
                        }
                    } else {
                        // Se a permissão foi negada, mostrar uma mensagem
                        Toast.makeText(Home.this, "Permissão de notificação negada", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.perfilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, UserPerfil.class);
                intent.putExtra("user_type", "user");
                startActivity(intent);
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

                } else if (id == R.id.termsAndPrivacyPolicyMoreOptionsMenu) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://siteincluses.onrender.com/termosdeprivacidade"));
                    startActivity(browserIntent);
                } else if (id == R.id.perfilMoreOptionsMenu) {
                    Intent intent = new Intent(Home.this, UserPerfil.class);
                    intent.putExtra("user_type", "user");
                    intent.putExtra("token", token);
                    startActivity(intent);
                } else if (id == R.id.configurationsMoreOptionsMenu) {
                    Intent intent = new Intent(Home.this, ScreenConfigurations.class);
                    intent.putExtra("user_type", "user");
                    intent.putExtra("token", token);
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

    // Método para solicitar a permissão de notificação
    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar se a permissão já foi concedida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Pedir permissão
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Verificar a condição dos campos antes de enviar a notificação
                if (usuario != null && (usuario.getNomeSocial() == null || usuario.getPerfil().getBiografia() == null || usuario.getPerfil().getFotoPerfil() == null)) {
                    toNotify();
                }
            }
        } else {
            // Para versões anteriores ao Android 13, fazer a verificação antes de enviar a notificação
            if (usuario != null && (usuario.getNomeSocial() == null || usuario.getPerfil().getBiografia() == null || usuario.getPerfil().getFotoPerfil() == null)) {
                toNotify();
            }
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

    private void findUser(String fkPerfil, UsuarioCallback callback) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://incluses-api.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.getUserByProfileFk(token, UUID.fromString(fkPerfil));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar usuario: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_LONG).show();
                firebase.saveError(new Error("Erro ao buscar usuario: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}