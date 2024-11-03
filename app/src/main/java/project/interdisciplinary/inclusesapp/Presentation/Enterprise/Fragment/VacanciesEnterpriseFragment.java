package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.VacanciesAdapter;
import project.interdisciplinary.inclusesapp.adapters.VacanciesEnterpriseAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesEnterpriseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VacanciesEnterpriseFragment extends Fragment {

    private View rootView;

    private DatabaseFirebase firebase = new DatabaseFirebase();
    private Retrofit retrofit;

    private String token;


    private FragmentVacanciesEnterpriseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVacanciesEnterpriseBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        rootView = binding.getRoot();

        setupKeyboardListener();

        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        binding.btnYourVacancies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateVacancieEnterpriseFragment createVacancieEnterpriseFragment = new CreateVacancieEnterpriseFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, createVacancieEnterpriseFragment).commit();
            }
        });

        // Adicionando o TextWatcher para buscar as vagas conforme o usuário digita
        binding.searchVacancieEnterpriseByNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nome = charSequence.toString().trim();
                if (!nome.isEmpty()) {
                    setupAdapterByNameVacancies(nome, new VacanciesCallback() {
                        @Override
                        public void onSuccess(List<Vaga> vacanciesResponse) {
                            // Atualiza o Adapter no RecyclerView
                            binding.vacanciesRecyclerViewEnterprise.setAdapter(new VacanciesEnterpriseAdapter(vacanciesResponse));
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            firebase.saveError(new Error("Erro ao buscar vagas: " + throwable.getMessage()));
                            Log.e("Error", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Se o campo de busca estiver vazio, carrega todas as vagas
                    setupAdapter(new VacanciesCallback() {
                        @Override
                        public void onSuccess(List<Vaga> vacanciesResponse) {
                            binding.vacanciesRecyclerViewEnterprise.setAdapter(new VacanciesEnterpriseAdapter(vacanciesResponse));
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            firebase.saveError(new Error("Erro ao buscar vagas: " + throwable.getMessage()));
                            Log.e("Error", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Define o LayoutManager uma vez
        binding.vacanciesRecyclerViewEnterprise.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialmente, carrega todas as vagas
        setupAdapter(new VacanciesCallback() {
            @Override
            public void onSuccess(List<Vaga> vacanciesResponse) {
                // Define o Adapter no RecyclerView
                binding.vacanciesRecyclerViewEnterprise.setAdapter(new VacanciesEnterpriseAdapter(vacanciesResponse));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Error", throwable.getMessage());
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setupAdapter(VacanciesCallback callback) {

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
        Call<List<Vaga>> call = api.getVacancies(token);//Mudar aqui
        call.enqueue(new Callback<List<Vaga>>() {
            @Override
            public void onResponse(Call<List<Vaga>> call, Response<List<Vaga>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vaga> responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar vagas: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Vaga>> call, Throwable throwable) {
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
                    binding.searchVacancieEnterpriseByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacancieEnterpriseInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
    private void setupAdapterByNameVacancies(String nome,VacanciesCallback callback) {

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
        Call<List<Vaga>> call = api.getVacanciesByName(token, nome);
        call.enqueue(new Callback<List<Vaga>>() {
            @Override
            public void onResponse(Call<List<Vaga>> call, Response<List<Vaga>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vaga> responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar vagas: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Vaga>> call, Throwable throwable) {
                Toast.makeText(getContext(), "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}