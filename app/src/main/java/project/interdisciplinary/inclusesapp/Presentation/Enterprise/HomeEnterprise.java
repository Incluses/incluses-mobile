package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.EditAccount;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.CoursesEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.CreateCourseEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.FeedEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment.VacanciesEnterpriseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Home;
import project.interdisciplinary.inclusesapp.Presentation.ScreenConfigurations;
import project.interdisciplinary.inclusesapp.Presentation.UserPerfil;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaApi;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeEnterpriseBinding;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeEnterprise extends AppCompatActivity {

    private ActivityHomeEnterpriseBinding binding;
    private View rootView;

    private Retrofit retrofit;

    private String token;
    private String perfil;
    private Empresa empresa;

    private ActivityResultLauncher<String> notificationPermissionLauncher;

    private String NOTIFICATION_NAME = "Incluses";
    private String NOTIFICATION_DESC = "Complete seu cadastro! Na tela de Editar Conta!";


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

        //Pegando os dados do SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        token = preferences.getString("token", "");
        perfil = preferences.getString("perfil", "");

        findUserEmpresa(String.valueOf(ConvertersToObjects.convertStringToPerfil(perfil).getId()), new EmpresaCallback() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                Gson gson = new Gson();
                empresa = gson.fromJson(jsonObject, Empresa.class);
                editor.putString("empresa", jsonObject.toString());
                editor.apply();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
            }
        });


        // Inicializa o launcher para solicitar a permissão de notificação
        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Se a permissão foi concedida, enviar a notificação
                        if (empresa.getPerfil().getBiografia() == null && empresa.getPerfil().getFotoPerfil() == null) {
                            toNotify();
                        }
                    } else {
                        // Se a permissão foi negada, mostrar uma mensagem
                        Toast.makeText(HomeEnterprise.this, "Permissão de notificação negada", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Solicitar permissão de notificação na inicialização
        requestNotificationPermission();


//        setupKeyboardListener();

        // Configuração do clique na imagem de perfil
        binding.perfilImageViewEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeEnterprise.this, EnterpriseProfileActivity.class);
                intent.putExtra("user_type", "enterprise");
                startActivity(intent);
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

                if (id == R.id.feedMoreOptionsMenuEnterprise) {
                    if (!(getCurrentFragment() instanceof FeedEnterpriseFragment)) {
                        selectedFragment = new FeedEnterpriseFragment();
                        selectedIndex = 0;
                        updateBottomNavigationSelection(R.id.itemHome);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está no Feed", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.perfilMoreOptionsMenuEnterprise) {
                    Intent intent = new Intent(HomeEnterprise.this, EnterpriseProfileActivity.class);
                    intent.putExtra("user_type", "enterprise");
                    startActivity(intent);
                } else if (id == R.id.termsAndPrivacyPolicyMoreOptionsMenuEnterprise) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://siteincluses.onrender.com/termosdeprivacidade"));
                    startActivity(browserIntent);
                } else if (id == R.id.configurationsMoreOptionsMenuEnterprise) {
                    Intent intent = new Intent(HomeEnterprise.this, ScreenConfigurations.class);
                    intent.putExtra("user_type", "enterprise");
                    startActivity(intent);

                } else if (id == R.id.positionsAndSalariesMoreOptionsMenuEnterprise) {
                    if (!(getCurrentFragment() instanceof VacanciesEnterpriseFragment)) {
                        selectedFragment = new VacanciesEnterpriseFragment();
                        selectedIndex = 1;
                        updateBottomNavigationSelection(R.id.itemVacancies);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está em Vagas", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.coursesMoreOptionsMenuEnterprise) {
                    if (!(getCurrentFragment() instanceof CoursesEnterpriseFragment)) {
                        selectedFragment = new CoursesEnterpriseFragment();
                        selectedIndex = 2;
                        updateBottomNavigationSelection(R.id.itemCourses);
                    } else {
                        Toast.makeText(HomeEnterprise.this, "Você já está em Cursos", Toast.LENGTH_SHORT).show();
                    }

                } else if (id == R.id.messagesAndInterviewsMoreOptionsMenuEnterprise) {
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

//        // Configuração para o clique no campo de texto
//        binding.nameEditTextEnterprise.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    replaceFragment(new ProfileSearchFragment());
//                }
//                return false;
//            }
//        });

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

    private void findUserEmpresa(String fkPerfil, EmpresaCallback callback) {

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

        EmpresaApi api = retrofit.create(EmpresaApi.class);
        Call<JsonObject> call = api.getEnterpriseByProfileFk(token, fkPerfil);
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
                    callback.onFailure(new Exception("Erro ao buscar empresa: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}
