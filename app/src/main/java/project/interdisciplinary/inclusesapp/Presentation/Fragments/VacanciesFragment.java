package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.LoginUser;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.VacanciesAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginApi;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VacanciesFragment extends Fragment {

    private View rootView;

    private Retrofit retrofit;

    private String token;

    private FragmentVacanciesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVacanciesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();

        SharedPreferences preferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        // Define o LayoutManager uma vez
        binding.vacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Chama o setupAdapter para buscar as vagas
        setupAdapter(new VacanciesCallback() {
            @Override
            public void onSuccess(List<Vaga> vacanciesResponse) {
                // Aqui você define o Adapter no RecyclerView
                binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Error", throwable.getMessage());
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                    binding.searchVacancieByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacancieInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });

        setJobButtonClickListeners();

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

    private void setJobButtonClickListeners() {
        // Array com os IDs de todos os botões
        int[] buttonIds = {
                R.id.btnAllJobs,
                R.id.btnRecommended,
                R.id.btnPresencial,
                R.id.btnRemote,
                R.id.btnHibrid
        };

        // Loop para adicionar o listener de clique para cada botão
        for (int id : buttonIds) {
            Button button = rootView.findViewById(id);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialButton selectedButton = (MaterialButton) view;

                        // Desmarca todos os botões antes de marcar o selecionado
                        deselectAllButtons(buttonIds);

                        // Marca o botão selecionado com as cores dark_blue
                        selectedButton.setTextColor(getResources().getColor(R.color.white)); // Cor de texto selecionado
                        selectedButton.setBackgroundTintList(getResources().getColorStateList(R.color.dark_blue)); // Fundo selecionado
                        selectedButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue))); // Contorno selecionado

                        // Lógica para diferentes botões
                        if (view.getId() == R.id.btnAllJobs) {
                            handleAllJobsClick();
                        } else if (view.getId() == R.id.btnRecommended) {
                            handleRecommendedClick();
                        } else if (view.getId() == R.id.btnPresencial) {
                            handlePresencialClick();
                        } else if (view.getId() == R.id.btnRemote) {
                            handleRemoteClick();
                        } else if (view.getId() == R.id.btnHibrid) {
                            handleHibridClick();
                        }
                    }
                });
            }
        }
    }

    private void deselectAllButtons(int[] buttonIds) {
        // Desmarca todos os botões
        for (int id : buttonIds) {
            MaterialButton button = rootView.findViewById(id);
            if (button != null) {
                // Cor original dos botões
                button.setTextColor(getResources().getColor(R.color.black)); // Cor original
                button.setBackgroundTintList(getResources().getColorStateList(R.color.white)); // Fundo original
                button.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.gray))); // Contorno original
            }
        }
    }


    private void handleAllJobsClick() {
        // Implementa o comportamento para o botão "Todas as vagas"
        Toast.makeText(getContext(), "Todas as vagas selecionadas", Toast.LENGTH_SHORT).show();
    }

    private void handleRecommendedClick() {
        // Implementa o comportamento para o botão "Recomendados"
        Toast.makeText(getContext(), "Vagas recomendadas selecionadas", Toast.LENGTH_SHORT).show();
    }

    private void handlePresencialClick() {
        // Implementa o comportamento para o botão "Presenciais"
        Toast.makeText(getContext(), "Vagas presenciais selecionadas", Toast.LENGTH_SHORT).show();
    }

    private void handleRemoteClick() {
        // Implementa o comportamento para o botão "Remotas"
        Toast.makeText(getContext(), "Vagas remotas selecionadas", Toast.LENGTH_SHORT).show();
    }

    private void handleHibridClick() {
        // Implementa o comportamento para o botão "Hibrídas"
        Toast.makeText(getContext(), "Vagas híbridas selecionadas", Toast.LENGTH_SHORT).show();
    }

}