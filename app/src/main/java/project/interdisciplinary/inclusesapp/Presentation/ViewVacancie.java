package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoVacancie;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoVacancieCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.CriarInscricaoVagaDTO;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.ActivityViewVacancieBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewVacancie extends AppCompatActivity {

    private ActivityViewVacancieBinding binding;

    private Retrofit retrofit;

    private String token;

    private String perfil;
    private Usuario userObj;

    private boolean inscrito = false;

    private UUID idVaga;
    private UUID idCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityViewVacancieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Botão de voltar
        binding.imageViewViewVacancieBackButton.setOnClickListener(v -> finish());
        binding.textViewViewVacancieBack.setOnClickListener(v -> finish());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        perfil = preferences.getString("perfil", "");
        String usuarioJson = preferences.getString("usuario", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!usuarioJson.isEmpty()) {
            Gson gson = new Gson();
            userObj = gson.fromJson(usuarioJson, Usuario.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Usuario", "ID: " + userObj.getId());
        } else {
            Log.e("Usuario", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        // Pegar dados vindos do bundle como String
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String vagaString = bundle.getString("vaga");
//            if (bundle.getString("type", "user").equals("enterprise")) {
//                binding.btnSubscribeVacancie.setVisibility(View.GONE);
//            }
            if (vagaString != null) {
                // Método para converter a String de volta em um objeto Vaga ou extrair os campos
                Vaga vaga = ConvertersToObjects.convertStringToVaga(vagaString);


                if (vaga != null) {
                    // Exibir os dados na UI
                    binding.titleViewVacancie.setText(vaga.getNome());
                    binding.descriptionViewVacancie.setText(vaga.getDescricao());
                    idVaga = vaga.getId();
                    if (bundle.getString("type", "user").equals("enterprise") || inscrito) {
                        binding.btnSubscribeVacancie.setVisibility(View.GONE);
                    } else {
                        binding.btnSubscribeVacancie.setOnClickListener(v -> {

                            Usuario currentUser = new Usuario(userObj.getId(),userObj.getCpf(),
                                    userObj.getFkPerfilId(),
                                    userObj.getFkCurriculoId(), userObj.getDtNascimento(),
                                    userObj.getPronomes(), userObj.getNomeSocial());

                            idCurrentUser = currentUser.getId();

                            CriarInscricaoVagaDTO inscricaoVaga = new CriarInscricaoVagaDTO(idCurrentUser, idVaga);

                            insertSubscription(new InscricaoVacancieCallback() {
                                @Override
                                public void onSuccess(InscricaoVaga inscricaoVaga) {
                                    Toast.makeText(ViewVacancie.this, "Inscrito com sucesso!", Toast.LENGTH_SHORT).show();
                                    binding.btnSubscribeVacancie.setVisibility(View.GONE);
                                }

                                @Override
                                public void onSuccessFind(List<InscricaoVaga> inscricaoVaga) {

                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Log.e("Error", throwable.getMessage());
                                    Toast.makeText(ViewVacancie.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }, inscricaoVaga);
                        });
                    }
                } else {
                    Toast.makeText(this, "Erro ao carregar a vaga", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Erro ao carregar a vaga", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void insertSubscription(InscricaoVacancieCallback callback, CriarInscricaoVagaDTO inscricaoVaga) {

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

        InscricaoVacancie api = retrofit.create(InscricaoVacancie.class);
        Call<InscricaoVaga> call = api.insertInscricaoVaga(token, inscricaoVaga);
        call.enqueue(new Callback<InscricaoVaga>() {
            @Override
            public void onResponse(Call<InscricaoVaga> call, Response<InscricaoVaga> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InscricaoVaga responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else if (response.code() == 400) {
                    Toast.makeText(ViewVacancie.this, "Você já está inscrito!", Toast.LENGTH_SHORT).show();
                    binding.btnSubscribeVacancie.setVisibility(View.GONE);
                    callback.onFailure(new Exception("Registro ja existe!"));
                }


                else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao fazer a inscrição: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<InscricaoVaga> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}
