package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.HomeEnterprise;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginApi;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginUserBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginUser extends AppCompatActivity {

    private ActivityLoginUserBinding binding;
    private DatabaseFirebase firebase = new DatabaseFirebase();
    private Retrofit retrofit;
    private String adminEmail;
    private String passwordAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Força o tema para o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Botão de voltar
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewLoginUserBack.setOnClickListener(v -> {
            finish();
        });

        // Ação para registrar novo usuário
        binding.registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginUser.this, RegisterUserActivity.class));
            finish();
        });
        binding.googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(LoginUser.this, "Disponível na próxima versão!", Toast.LENGTH_SHORT).show();
        });
        binding.facebookLoginButton.setOnClickListener(v -> {
            Toast.makeText(LoginUser.this, "Disponível na próxima versão!", Toast.LENGTH_SHORT).show();
        });
        binding.forgotPasswordTextView.setOnClickListener(v -> {
            Toast.makeText(LoginUser.this, "Disponível na próxima versão!", Toast.LENGTH_SHORT).show();
        });

        // Botão para continuar
        binding.continueButton.setOnClickListener(v -> {
            String emailInput = binding.emailEditText.getText().toString().trim();
            String passwordInput = binding.passwordEditText.getText().toString().trim();

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginUser.this, "Por favor, insira um Email ou Senha.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Exibe o ProgressBar
            binding.progressBar.setVisibility(View.VISIBLE);

            // Chama a API de admin primeiro
            callApiRetrofitLoginAdm(emailInput, new LoginCallback() {
                @Override
                public void onSuccess(LoginResponse loginResponse) {
                    // Não utilizado aqui
                }

                @Override
                public void onSuccessAdmin(JsonObject loginResponse) {
                    // Oculta o ProgressBar após o sucesso
                    binding.progressBar.setVisibility(View.GONE);

                    adminEmail = loginResponse.get("email").getAsString();
                    passwordAdmin = loginResponse.get("password").getAsString();

                    // Verifica se o login é de administrador
                    if (emailInput.equals(adminEmail) && passwordInput.equals(passwordAdmin)) {
                        // Redireciona para a área restrita
                        Intent intent = new Intent(LoginUser.this, AreaRestritaActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        LoginRequest login = new LoginRequest(emailInput, passwordInput);
                        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        callApiRetrofitLogin(login, new LoginCallback() {
                            @Override
                            public void onSuccess(LoginResponse loginResponse) {
                                // Oculta o ProgressBar após o sucesso
                                binding.progressBar.setVisibility(View.GONE);
                                
                                String token = loginResponse.getToken();
                                Perfil perfil = loginResponse.getPerfil();
                                String type = loginResponse.getType();

                                editor.putString("token", token);
                                editor.putString("perfil", perfil.toString());


                                if (type.equals("ROLE_EMPRESA")) {
                                    Intent intent = new Intent(LoginUser.this, HomeEnterprise.class);
                                    startActivity(intent);
                                    finish();
                                } else if (type.equals("ROLE_USUARIO")) {
                                    Intent intent = new Intent(LoginUser.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onSuccessAdmin(JsonObject loginResponse) {
                                // Não utilizado aqui
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                firebase.saveError(new Error("Erro ao logar: " + throwable.getMessage()));
                                Log.e("LoginError", throwable.getMessage());
                                Toast.makeText(LoginUser.this, "Erro no login", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    if (emailInput != null && passwordInput != null) {


                        LoginRequest login = new LoginRequest(emailInput, passwordInput);
                        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        callApiRetrofitLogin(login, new LoginCallback() {
                            @Override
                            public void onSuccess(LoginResponse loginResponse) {
                                String token = loginResponse.getToken();
                                Perfil perfil = loginResponse.getPerfil();

                                editor.putString("token", token);
                                editor.putString("perfil", perfil.toString());
                                editor.putBoolean("isLogged", true);
                                String type = loginResponse.getType();
                                boolean isEnterprise;
                                if (type.equals("ROLE_EMPRESA")){
                                    isEnterprise = true;
                                }
                                else {
                                    isEnterprise = false;
                                }
                                editor.putBoolean("isEnterprise", isEnterprise);
                                editor.apply();
                                editor.apply();

                                if (type.equals("ROLE_EMPRESA")) {
                                    Intent intent = new Intent(LoginUser.this, HomeEnterprise.class);
                                    startActivity(intent);
                                    finish();
                                } else if (type.equals("ROLE_USUARIO")) {
                                    Intent intent = new Intent(LoginUser.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onSuccessAdmin(JsonObject loginResponse) {
                                // Não utilizado aqui
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                binding.progressBar.setVisibility(View.GONE);
                                firebase.saveError(new Error("Erro ao logar: " + throwable.getMessage()));
                                Log.e("LoginError", throwable.getMessage());
                                Toast.makeText(LoginUser.this, "Erro no login", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        });
    }

    private void callApiRetrofitLogin(LoginRequest login, LoginCallback callback) {
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

        LoginApi api = retrofit.create(LoginApi.class);
        Call<LoginResponse> call = api.login(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    binding.emailInputLayout.setError("Login ou senha inválidos!");
                    binding.passwordInputLayout.setError("Login ou senha inválidos!");
                    callback.onFailure(new Exception("Login ou senha inválidos!"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                binding.emailInputLayout.setError("Login ou senha inválidos!");
                binding.passwordInputLayout.setError("Login ou senha inválidos!");
                callback.onFailure(throwable);
            }
        });
    }

    private void callApiRetrofitLoginAdm(String email, LoginCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://apiredis-incluses.onrender.com/api-redis/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginApi api = retrofit.create(LoginApi.class);
        Call<JsonObject> call = api.findAdmin(email);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessAdmin(response.body());
                } else {
                    callback.onFailure(new Exception("Login ou senha inválidos!"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
