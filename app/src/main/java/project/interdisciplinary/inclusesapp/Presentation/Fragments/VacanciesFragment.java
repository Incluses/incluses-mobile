package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.LoginUser;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.VacanciesAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoVacancie;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoVacancieCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginApi;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesApi;
import project.interdisciplinary.inclusesapp.data.dbApi.VacanciesCallback;
import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;
import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
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

    private Usuario userObj;

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
        String usuarioJson = preferences.getString("usuario", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!usuarioJson.isEmpty()) {
            Gson gson = new Gson();
            userObj = gson.fromJson(usuarioJson, Usuario.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Usuario", "ID: " + userObj.getId());
        } else {
            Log.e("Usuario", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        // Adicionando o TextWatcher para buscar as vagas conforme o usuário digita
        binding.searchVacancieByNameEditText.addTextChangedListener(new TextWatcher() {
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
                            binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("Error", throwable.getMessage());
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Se o campo de busca estiver vazio, carrega todas as vagas
                    setupAdapter(new VacanciesCallback() {
                        @Override
                        public void onSuccess(List<Vaga> vacanciesResponse) {
                            binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
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
        binding.vacanciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicialmente, carrega todas as vagas
        setupAdapter(new VacanciesCallback() {
            @Override
            public void onSuccess(List<Vaga> vacanciesResponse) {
                // Define o Adapter no RecyclerView
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

    private void setJobButtonClickListeners() {
        // Array com os IDs de todos os botões
        int[] buttonIds = {
                R.id.btnAllJobs,
                R.id.btnInscritas,
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
                            setupAdapter(new VacanciesCallback() {
                                @Override
                                public void onSuccess(List<Vaga> vacanciesResponse) {
                                    // Usa o binding para definir o adapter
                                    binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    // Trate o erro
                                    Log.e("Error", throwable.getMessage());
                                }
                            });
                        } else if (view.getId() == R.id.btnInscritas) {
                            setupAdapterBySubscribesVacancies(userObj.getId(), new InscricaoVacancieCallback() {
                                @Override
                                public void onSuccess(InscricaoVaga inscricaoVaga) {

                                }

                                @Override
                                public void onSuccessFind(List<InscricaoVaga> inscricaoVaga) {
                                    List<Vaga> vacanciesSubscribes = new ArrayList<>();
                                    for (InscricaoVaga i : inscricaoVaga) {
                                        vacanciesSubscribes.add(i.getVaga());
                                    }
                                    binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesSubscribes));
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Log.e("Error", throwable.getMessage());
                                }
                            });
                        } else if (view.getId() == R.id.btnPresencial) {
                            setupAdapterByNameVacanciesType("Presencial", new VacanciesCallback() {
                                @Override
                                public void onSuccess(List<Vaga> vacanciesResponse) {
                                    // Usa o binding para definir o adapter
                                    binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    // Trate o erro
                                    Log.e("Error", throwable.getMessage());
                                }
                            });
                        } else if (view.getId() == R.id.btnRemote) {
                            setupAdapterByNameVacanciesType("Remoto", new VacanciesCallback() {
                                @Override
                                public void onSuccess(List<Vaga> vacanciesResponse) {
                                    // Usa o binding para definir o adapter
                                    binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    // Trate o erro
                                    Log.e("Error", throwable.getMessage());
                                }
                            });
                        } else if (view.getId() == R.id.btnHibrid) {
                                setupAdapterByNameVacanciesType("Hibrido",  new VacanciesCallback() {
                                    @Override
                                    public void onSuccess(List<Vaga> vacanciesResponse) {
                                        // Usa o binding para definir o adapter
                                        binding.vacanciesRecyclerView.setAdapter(new VacanciesAdapter(vacanciesResponse));
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        // Trate o erro
                                        Log.e("Error", throwable.getMessage());
                                    }
                                });
                        }
                    }
                });
            }
        }
    }

    private void setupAdapterByNameVacanciesType(String typeSegmentation,VacanciesCallback callback) {

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
        Call<List<Vaga>> call = api.getVacanciesByNameType(token, typeSegmentation);
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

    private void setupAdapterBySubscribesVacancies(UUID fkUsuario, InscricaoVacancieCallback callback) {

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

        InscricaoVacancie api = retrofit.create(InscricaoVacancie.class);
        Call<List<InscricaoVaga>> call = api.selectInscricaoVagaByFkUsuario(token, fkUsuario);
        call.enqueue(new Callback<List<InscricaoVaga>>() {
            @Override
            public void onResponse(Call<List<InscricaoVaga>> call, Response<List<InscricaoVaga>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InscricaoVaga> responseBody = response.body();
                    callback.onSuccessFind(responseBody);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar vagas: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<InscricaoVaga>> call, Throwable throwable) {
                Toast.makeText(getContext(), "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
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
}