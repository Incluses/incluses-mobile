package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.CreateVacancieEnterprise;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.VacanciesAdapter;
import project.interdisciplinary.inclusesapp.adapters.VacanciesEnterpriseAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateVacancieEnterpriseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateVacancieEnterpriseFragment extends Fragment {

    private View rootView;
    private DatabaseFirebase firebase = new DatabaseFirebase();
    private Retrofit retrofit;
    private String token;
    private Empresa empresa;

    private FragmentCreateVacancieEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateVacancieEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        rootView = binding.getRoot();

        setupKeyboardListener();

        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String empresaJson = preferences.getString("empresa", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!empresaJson.isEmpty()) {
            Gson gson = new Gson();
            empresa = gson.fromJson(empresaJson, Empresa.class);  // Converte o JSON de volta para o objeto Empresa

            Log.e("Empresa", "ID: " + empresa.getId());
        } else {
            Log.e("Empresa", "Nenhuma empresa encontrada no SharedPreferences.");
        }
        binding.btnAllVacanciesEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VacanciesEnterpriseFragment vacanciesEnterpriseFragment = new VacanciesEnterpriseFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, vacanciesEnterpriseFragment).commit();
            }
        });

        // Adicionando o TextWatcher para buscar as vagas conforme o usuário digita
        binding.searchVacanciesCreatedByNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nome = charSequence.toString().trim();
                if (!nome.isEmpty()) {
                    setupAdapterYourVacancies(empresa.getId(), new VacanciesCallback() {
                        @Override
                        public void onSuccess(List<Vaga> vacanciesResponse) {
                            List<Vaga> vacanciesSearcheds = new ArrayList<>();
                            for (Vaga vaga : vacanciesResponse) {
                                if(vaga.getNome().toLowerCase().contains(nome.toLowerCase())) {
                                    vacanciesSearcheds.add(vaga);
                                }
                            }
                            // Atualiza o Adapter no RecyclerView
                            binding.myVacanciesRecyclerView.setAdapter(new VacanciesEnterpriseAdapter(vacanciesSearcheds));
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
                    setupAdapterYourVacancies(empresa.getId(), new VacanciesCallback() {
                        @Override
                        public void onSuccess(List<Vaga> vacanciesResponse) {
                            binding.myVacanciesRecyclerView.setAdapter(new VacanciesEnterpriseAdapter(vacanciesResponse));
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
        binding.myVacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Chama o setupAdapter para buscar as vagas
        binding.createVacanciesMyVacancieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateVacancieEnterprise createVacancieEnterprise = new CreateVacancieEnterprise();
                Intent intent = new Intent(getContext(), createVacancieEnterprise.getClass());
                startActivity(intent);
            }
        });

        setupAdapterYourVacancies(empresa.getId(), new VacanciesCallback() {
            @Override
            public void onSuccess(List<Vaga> vacanciesResponse) {
                // Aqui você define o Adapter no RecyclerView
                binding.myVacanciesRecyclerView.setAdapter(new VacanciesEnterpriseAdapter(vacanciesResponse));
            }

            @Override
            public void onFailure(Throwable throwable) {
                firebase.saveError(new Error("Erro ao buscar vagas: " + throwable.getMessage()));
                Log.e("Error", throwable.getMessage());
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void setupAdapterYourVacancies(UUID fkEmpresa, VacanciesCallback callback) {

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
        Call<List<Vaga>> call = api.getVacanciesByEnterprise(token, fkEmpresa);
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
                    binding.searchVacanciesCreatedByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacanciesCreatedInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }

}