package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.ClassesAdapter;
import project.interdisciplinary.inclusesapp.adapters.CoursesAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.InscricaoCurso;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.ActivityCourseInitialPageBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseInitialPage extends AppCompatActivity {

    private String token;
    private Retrofit retrofit;
    private ActivityCourseInitialPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_initial_page);

        SharedPreferences preferences = getApplication().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCourseInitialPageBinding.inflate(getLayoutInflater());
        binding.classesDownloadsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setContentView(binding.getRoot());
        Intent extrasIntent = getIntent();
        Bundle bundle = extrasIntent.getExtras();
        UUID cursoId = UUID.fromString(bundle.getString("idCurso"));
        setUpAdapter(cursoId, new MaterialCursoCallback() {
            @Override
            public void onSuccessFind(List<MaterialCurso> list) {
                binding.classesDownloadsRecyclerView.setAdapter(new ClassesAdapter(list));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(JsonObject jsonObject) {

            }
        });
    }
    private void setUpAdapter(UUID cursoId, MaterialCursoCallback callback) {
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
        Call<List<MaterialCurso>> call = api.findMaterialCursoByCurso(token, cursoId);
        call.enqueue(new Callback<List<MaterialCurso>>() {
            @Override
            public void onResponse(Call<List<MaterialCurso>> call, Response<List<MaterialCurso>> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    List<MaterialCurso> responseBody = response.body();
                    callback.onSuccessFind(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }
            @Override
            public void onFailure(Call<List<MaterialCurso>> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }

}