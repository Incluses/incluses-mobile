package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.CreatePost;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.PostagensAdapter;
import project.interdisciplinary.inclusesapp.adapters.VacanciesAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentFeedBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedFragment extends Fragment {

    private View rootView;
    private Retrofit retrofit;

    private String token;

    private Perfil perfilObj;
    private FragmentFeedBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();

        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!perfilJson.isEmpty()) {
            Gson gson = new Gson();
            perfilObj = gson.fromJson(perfilJson, Perfil.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Perfil", "ID: " + perfilObj.getId());
        } else {
            Log.e("Perfil", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        // Define o LayoutManager uma vez
        binding.feedPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter(new PostagemCallback() {
            @Override
            public void onSuccess(List<JsonObject> postagens) {
                // Define o Adapter no RecyclerView
                binding.feedPostsRecyclerView.setAdapter(new PostagensAdapter(postagens, getContext()));
            }

            @Override
            public void onSuccessVerifyLike(Boolean booleanResponse) {

            }

            @Override
            public void onSuccessInsert(JsonObject jsonObject) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Erro", throwable.getMessage());
            }
        });

        binding.createPostPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePost createPost = new CreatePost();
                startActivity(new Intent(getActivity(), createPost.getClass()));
            }
        });

        return view;
    }

    private void setupAdapter(PostagemCallback callback) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://api-mongo-incluses.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostagemApi api = retrofit.create(PostagemApi.class);
        Call<List<JsonObject>> call = api.getPostagens();
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JsonObject> responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar postagens: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable throwable) {
                Toast.makeText(getContext(), "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
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
                    binding.searchPostsProfileByNameEditText.clearFocus(); // Clear focus
                    binding.searchPostsProfileInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
}