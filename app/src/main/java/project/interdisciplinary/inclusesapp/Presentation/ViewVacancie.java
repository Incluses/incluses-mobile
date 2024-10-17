package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

    private String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwicm9sZSI6IlJPTEVfRU1QUkVTQSIsImV4cCI6MTcyOTAyMDc0OH0.aBpWGuDQUEvXisme2cL59do3M_qixLYUdh5LNuSjR7agEEhpkuKacowzRmgQx85-Y-Z23akV6z_srQuc8lW2TQ";

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

        // Pegar dados vindos do bundle como String
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String vagaString = bundle.getString("vaga");
            if (vagaString != null) {
                // Método para converter a String de volta em um objeto Vaga ou extrair os campos
                Vaga vaga = ConvertersToObjects.convertStringToVaga(vagaString);

                if (vaga != null) {
                    // Exibir os dados na UI
                    binding.titleViewVacancie.setText(vaga.getNome());
                    binding.descriptionViewVacancie.setText(vaga.getDescricao());

                    if (bundle.getString("type", "user").equals("enterprise")) {
                        binding.btnSubscribeVacancie.setVisibility(View.GONE);
                    } else {
                        binding.btnSubscribeVacancie.setOnClickListener(v -> {

                            // TODO: Adicionar funcionalidade de inscrição

                            Usuario currentUser = new Usuario(UUID.fromString("555da7cc-80c9-4967-870c-1d0b37765af2"),"12345678901",
                                    UUID.fromString("54d087dd-6356-4462-bdac-2c8cf5ef0494"),
                                    UUID.fromString("a11fefb6-06f4-4384-a0a9-649a1970cbab"), "1990-01-01T00:00:00.000+00:00",
                                    "teste", "teste");
                            idVaga = vaga.getId();
                            idCurrentUser = new Usuario().getId();
                            UUID idInscricaoVaga = UUID.randomUUID();
                            InscricaoVaga inscricaoVaga = new InscricaoVaga(idInscricaoVaga, UUID.fromString("555da7cc-80c9-4967-870c-1d0b37765af2") , idVaga, currentUser, vaga);

                            insertSubscription(new InscricaoVacancieCallback() {
                                @Override
                                public void onSuccess(InscricaoVaga inscricaoVaga) {
                                    Toast.makeText(ViewVacancie.this, "Inscrito com sucesso!", Toast.LENGTH_SHORT).show();
                                    binding.btnSubscribeVacancie.setVisibility(View.GONE);
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

    private void insertSubscription(InscricaoVacancieCallback callback, InscricaoVaga inscricaoVaga) {

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
                } else {
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
