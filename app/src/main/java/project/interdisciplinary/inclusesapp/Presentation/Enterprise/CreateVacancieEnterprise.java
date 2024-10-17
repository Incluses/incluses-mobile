package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.CriarVagaDTO;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Endereco;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Setor;
import project.interdisciplinary.inclusesapp.data.models.TipoPerfil;
import project.interdisciplinary.inclusesapp.data.models.TipoVaga;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateVacancieEnterpriseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateVacancieEnterprise extends AppCompatActivity {

    private Retrofit retrofit;
    private String token;

    private ActivityCreateVacancieEnterpriseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCreateVacancieEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAutoComplete();

        binding.cancelCreateVacancieButton.setOnClickListener(v -> finish());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");


        // Configurando o botão "Next" para criar a vaga
        binding.nextCreateVacancieButton.setOnClickListener(v -> {
            String nameVacancie = binding.nameCreateVacancieEditText.getText().toString();
            String descriptionVacancie = binding.descripitionCreateVacancieEditText.getText().toString();
            String typeVacancie = binding.typeAutoCompleteTextView.getText().toString();

            if (!nameVacancie.isEmpty() && !descriptionVacancie.isEmpty() && !typeVacancie.isEmpty()) {
                // Criar o objeto CriarVagaDTO para enviar à API
                CriarVagaDTO criarVagaDTO = new CriarVagaDTO();
                criarVagaDTO.setNome(nameVacancie);
                criarVagaDTO.setDescricao(descriptionVacancie);
                criarVagaDTO.setEmpresaId(UUID.fromString("4b5e7937-6c1b-4394-aad5-846084e22c46"));
                criarVagaDTO.setTipoVagaId(UUID.fromString("ae37a7e5-51b3-4a3a-b147-317844999139"));

                // Chama o método para inserir a vaga no backend
                insertVacancy(new VacanciesCallback() {
                    @Override
                    public void onSuccess(List<Vaga> vagas) {
                        Toast.makeText(CreateVacancieEnterprise.this, "Vaga criada com sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(CreateVacancieEnterprise.this, "Erro ao criar vaga!", Toast.LENGTH_LONG).show();
                    }
                }, criarVagaDTO);
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void insertVacancy(VacanciesCallback callback, CriarVagaDTO vagaDTO) {
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

        VacanciesApi api = retrofit.create(VacanciesApi.class);
        Call<JsonObject> call = api.insertVacancie(token, vagaDTO);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(null); // Aqui você pode definir o retorno adequado
                } else if (response.code() == 401) {
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    callback.onFailure(new Exception("Erro ao criar vaga: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Toast.makeText(CreateVacancieEnterprise.this, "Erro ao criar vaga", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable);
            }
        });
    }


    private void setupAutoComplete() {
        String[] states = getResources().getStringArray(R.array.type_vacancie_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        binding.typeAutoCompleteTextView.setAdapter(adapter);
        binding.typeAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
