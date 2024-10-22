package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.ActivityEditMaterialCourseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMaterialCourse extends AppCompatActivity {

    private Retrofit retrofit;
    private String token;
    private ActivityEditMaterialCourseBinding binding;

    private boolean titleFilled, descriptionFilled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditMaterialCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentExtras = getIntent();
        Bundle extras = intentExtras.getExtras();
        MaterialCurso materialCurso = ConvertersToObjects.convertStringToMaterialCurso(extras.getString("materialCurso"));

        SharedPreferences preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        binding.descriptionEditTextEditMaterial.setText(materialCurso.getDescricao());
        binding.titleEditTextEditMaterial.setText(materialCurso.getNome());
        binding.submitButtonEditMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTitle = binding.titleEditTextEditMaterial.getText().toString();
                String inputDescription = binding.descriptionEditTextEditMaterial.getText().toString();
                Map<String, Object> request = new HashMap<>();

                if(!inputTitle.equals(materialCurso.getNome())){
                    request.put("nome", inputTitle);
                    titleFilled = true;
                }
                else if (!inputDescription.equals(materialCurso.getDescricao())) {
                    request.put("descricao", inputDescription);
                    descriptionFilled = true;
                }
                if (titleFilled || descriptionFilled){
                    callApiRetrofitAlterAvaliacao(materialCurso.getId(), request, new MaterialCursoCallback() {
                        @Override
                        public void onSuccessFind(List<MaterialCurso> list) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            materialCurso.setNome(inputTitle);
                            materialCurso.setDescricao(inputDescription);
                            finish();
                        }
                    });
                }
            }
        });
    }
    private void callApiRetrofitAlterAvaliacao(UUID materialId, Map<String, Object> request, MaterialCursoCallback callback) {
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
        MaterialCursoApi api = retrofit.create(MaterialCursoApi.class);
        Call<JsonObject> call = api.updateMaterialCurso(token,materialId, request);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}