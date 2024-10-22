package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.AddMaterialCourse;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.ClassesAdapter;
import project.interdisciplinary.inclusesapp.adapters.MaterialCourseAdapter;
import project.interdisciplinary.inclusesapp.adapters.MyCoursesAdapter;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.FragmentDetailsCourseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailsCourseFragment extends Fragment {

    private String token;
    private View rootView;
    private Curso curso;
    private FragmentDetailsCourseBinding binding;

    private double avaliation;

    private Retrofit retrofit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsCourseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        rootView = binding.getRoot();
        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        setupKeyboardListener();
        avaliation = getArguments().getDouble("avaliation");
        curso = ConvertersToObjects.convertStringToCurso(getArguments().getString("curso"));

        Log.e("curso", getArguments().getString("curso"));
        Log.e("curso", curso.toString());
        binding.titleCourseDetailsCourse.setText(curso.getNome());
        binding.numberPontuationDetailsCourse.setText(String.valueOf(avaliation));
        binding.courseDetailsRatingBar.setRating((float) avaliation);
        binding.listMaterialsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setUpAdapter(curso.getId(),new MaterialCursoCallback() {
            @Override
            public void onSuccessFind(List<MaterialCurso> list) {
                binding.listMaterialsRecyclerView.setAdapter(new MaterialCourseAdapter(list,getArguments().getBoolean("isEmpresa")));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(JsonObject jsonObject) {

            }
        });
        binding.addMaterialDetailsCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMaterialCourse.class);
                startActivity(intent);
            }
        });
        binding.discardButtonDetailsCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.concludeButtonDetailsCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;}

    private void setupKeyboardListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is opened
                } else {
                    // Keyboard is closed
                    binding.searchMaterialCourseByNameEditText.clearFocus(); // Clear focus
                    binding.searchMaterialCourseInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
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
