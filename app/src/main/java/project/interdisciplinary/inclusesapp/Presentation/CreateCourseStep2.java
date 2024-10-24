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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.CreateMaterialCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateCourseStep2Binding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCourseStep2 extends AppCompatActivity {

    private ActivityCreateCourseStep2Binding binding;

    private UUID idCurso;

    private Retrofit retrofit;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityCreateCourseStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent extrasIntent = getIntent();
        Bundle bundle = extrasIntent.getExtras();
        idCurso = UUID.fromString(bundle.getString("idCurso"));
        SharedPreferences preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        binding.nextCreateMyCourse2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameFilled = false, descriptionFilled =  false;
                String nameInput = binding.titleMyCourseEditText.getText().toString();
                String descriptionInput = binding.descriptionEditTextMyCourse.getText().toString();
                if (!nameInput.isEmpty()){
                    nameFilled = true;
                }
                else {
                    nameFilled = false;
                }
                if (!descriptionInput.isEmpty()){
                    descriptionFilled = true;
                }
                else {
                    descriptionFilled = false;
                }
                if (nameFilled && descriptionFilled){
                    CreateMaterialCursoRequest createMaterialCursoRequest = new CreateMaterialCursoRequest(
                            descriptionInput,nameInput,idCurso,
                            UUID.fromString("a11fefb6-06f4-4384-a0a9-649a1970cbab"));
                    callApiRetrofitInsertMaterial(createMaterialCursoRequest, new MaterialCursoCallback() {
                        @Override
                        public void onSuccessFind(List<MaterialCurso> list) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            finish();
                        }
                    });
                }
            }
        });



        binding.cancelCreateMyCourse2Button.setOnClickListener(v -> finish());

    }

    private void callApiRetrofitInsertMaterial(CreateMaterialCursoRequest materialCurso , MaterialCursoCallback callback) {
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
        Call<JsonObject> call = api.insertMaterialCurso(token,materialCurso);
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