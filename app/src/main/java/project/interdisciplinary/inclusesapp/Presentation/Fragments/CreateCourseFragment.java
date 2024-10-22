package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.MyCoursesAdapter;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoCallback;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.databinding.FragmentCoursesBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateCourseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCourseFragment extends Fragment {

    private View rootView;

    private Retrofit retrofit;
    private String token;

    private UUID idPerfil;

    private FragmentCreateCourseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateCourseBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();
        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        String perfil = preferences.getString("perfil", "");
        idPerfil = ConvertersToObjects.convertStringToPerfil(perfil).getId();

        binding.btnCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesFragment coursesFragment = new CoursesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, coursesFragment).commit();
            }
        });

        binding.createCourseMyCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseActivity createCourseActivity = new CreateCourseActivity();
                Intent intent = new Intent(getContext(), createCourseActivity.getClass());
                startActivity(intent);
            }
        });

        binding.mycoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setUpAdapter(idPerfil, new CursoCallback() {
            @Override
            public void onSuccessFind(List<Curso> list) {
                binding.mycoursesRecyclerView.setAdapter(new MyCoursesAdapter((AppCompatActivity) requireActivity(),list,false));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(JsonObject jsonObject) {

            }
        });
        return view;
    }

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
                    binding.searchCoursesCreatedByNameEditText.clearFocus(); // Clear focus
                    binding.searchCoursesCreatedInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
    private void setUpAdapter(UUID idPerfil, CursoCallback callback) {
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

        CursoApi api = retrofit.create(CursoApi.class);
        Call<List<Curso>> call = api.findCursosByFkPerfil(token,idPerfil);
        call.enqueue(new Callback<List<Curso>>() {


            @Override
            public void onResponse(Call<List<Curso>> call, Response<List<Curso>> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    List<Curso> responseBody = response.body();
                    callback.onSuccessFind(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<Curso>> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}