package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.JobsOfInterest;
import project.interdisciplinary.inclusesapp.Presentation.LoginUser;
import project.interdisciplinary.inclusesapp.Presentation.RegisterUser2;
import project.interdisciplinary.inclusesapp.Presentation.RegisterUserActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.api.CepApi;
import project.interdisciplinary.inclusesapp.data.api.OnCepDataReceivedListener;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaApi;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Cep;
import project.interdisciplinary.inclusesapp.data.models.CreateEnterpriseRequest;
import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterEnterpriseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterEnterprise extends AppCompatActivity {

    private ActivityRegisterEnterpriseBinding binding;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent actualIntent = getIntent();
        Bundle infosUser = actualIntent.getExtras();
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityRegisterEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginEnterpriseBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.textViewLoginEnterpriseBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        //continue button
        binding.continueButton.setOnClickListener(
                v -> {
                    String cepInput = binding.cepEditText.getText().toString().trim();
                    boolean socialReasonFilled, numberFilled, sectorFilled;

                    //verificar setor
                    String sectorInput = binding.sectorEditText.getText().toString();
                    if(sectorInput.isEmpty()){
                        sectorFilled = false;
                    }
                    else {
                        sectorFilled = true;
                    }
                    //verificar razão social
                    String socialReasonInput = binding.sectorEditText.getText().toString();
                    if(socialReasonInput.isEmpty()){
                        socialReasonFilled = false;
                    }
                    else {
                        socialReasonFilled = true;
                    }

                    //verificar numero
                    String numInput = binding.numLocEditText.getText().toString();
                    if(numInput.isEmpty()){
                        numberFilled = false;
                    }
                    else {
                        numberFilled = true;
                    }

                    // Verificar se o campo do CEP está vazio
                    if (cepInput.isEmpty()) {
                        Toast.makeText(RegisterEnterprise.this, "Por favor, insira um CEP.", Toast.LENGTH_SHORT).show();
                    } else if (cepInput.length() != 9) {
                        // Verificação simples se o CEP tem o formato esperado (#####-###)
                        Toast.makeText(RegisterEnterprise.this, "CEP inválido. Insira um CEP válido.", Toast.LENGTH_SHORT).show();
                    } else {
                      // Se o CEP está preenchido corretamente, continue
                        callApiRetrofitCep(new OnCepDataReceivedListener() {
                            @Override
                            public void onCepDataReceived(Cep cep) {

                                if (cep != null) {
                                    if (!Boolean.getBoolean(cep.getErro())) {

                                        // CEP encontrado, navega para HomeEnterprise
                                        binding.cepInputLayout.setErrorEnabled(false);
                                        binding.cepInputLayout.setError(null);

                                        // Agora sim, pode navegar
                                        if(socialReasonFilled && numberFilled && sectorFilled){
                                            String name = infosUser.getString("name");
                                            String email = infosUser.getString("email");
                                            String password = infosUser.getString("password");
                                            String phone = infosUser.getString("phone_number");
                                            String cnpj = infosUser.getString("cnpj");
                                            String cleanCep = cepInput.replaceAll("[^\\d]", ""); // Remove qualquer caractere que não seja numérico

                                            CreateEnterpriseRequest enterprise = new CreateEnterpriseRequest(cnpj,socialReasonInput,sectorInput,cep.getLogradouro(),
                                                    cep.getEstado(),cep.getLocalidade(),cleanCep,Integer.parseInt(numInput),name,password,email,phone);
                                            Log.e("retorno",enterprise.toString() );
                                            callApiRetrofitRegister(enterprise, new EmpresaCallback() {
                                                @Override
                                                public void onSuccess(JsonObject jsonObject) {
                                                    Intent intent = new Intent(RegisterEnterprise.this, HomeEnterprise.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Throwable throwable) {
                                                    Log.e("ERRO", throwable.getMessage());
                                                }

                                            });
                                        }
                                    }else {
                                        // CEP não encontrado, mostra erro
                                        binding.cepInputLayout.setErrorEnabled(true);
                                        binding.cepInputLayout.setError("CEP inválido. Verifique o valor inserido.");
                                    }
                                } else {
                                    // CEP não encontrado, mostra erro
                                    binding.cepInputLayout.setErrorEnabled(true);
                                    binding.cepInputLayout.setError("CEP inválido. Verifique o valor inserido.");
                                }
                            }
                        }, cepInput);
                    }
                }
        );

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, LoginUser.class);
                    startActivity(intent);
                    finish();
                }
        );

        // Formatar o número de CEP
        binding.cepEditText.addTextChangedListener(new TextWatcher() {
            private static final String CEP_FORMAT = "#####-###";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

                StringBuilder formattedCEP = new StringBuilder();

                for (int i = 0, j = 0; i < cleanInput.length() && j < CEP_FORMAT.length(); i++) {
                    char c = cleanInput.charAt(i);
                    char f = CEP_FORMAT.charAt(j);
                    if (f == '#') {
                        formattedCEP.append(c);
                        j++;
                    } else {
                        formattedCEP.append(f);
                        j++;
                        i--;
                    }
                }

                binding.cepEditText.removeTextChangedListener(this);
                binding.cepEditText.setText(formattedCEP.toString());
                binding.cepEditText.setSelection(formattedCEP.length());
                binding.cepEditText.addTextChangedListener(this);
            }
        });

    }

    private void callApiRetrofitCep(OnCepDataReceivedListener listener, String cep) {
        // Remove formatação do CEP (tira os hífens e pontos, deixando apenas os números)
        String cleanCep = cep.replaceAll("[^\\d]", ""); // Remove qualquer caractere que não seja numérico

        String urlApi = "https://viacep.com.br/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CepApi api = retrofit.create(CepApi.class);
        Call<JsonObject> call = api.getCep(cleanCep); // Passa o CEP limpo
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if(responseBody.has("erro")) {
                        // Se o CEP não existir, mostrar mensagem de erro
                        binding.cepInputLayout.setError("CEP inválido. Verifique o valor inserido.");
                        listener.onCepDataReceived(null);
                    }
                    else {
                        Gson gson = new Gson();
                        Cep apiResponse = gson.fromJson(responseBody, Cep.class);
                        listener.onCepDataReceived(apiResponse);
                    }

                } else if (response.code() == 400) {
                    // Se o código de resposta for 400, mostrar mensagem de erro no campo de CEP
                    binding.cepInputLayout.setError("CEP inválido! Verifique o valor inserido.");
                    listener.onCepDataReceived(null);
                } else {
                    listener.onCepDataReceived(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                listener.onCepDataReceived(null);
            }
        });
    }

    private void callApiRetrofitRegister(CreateEnterpriseRequest enterpriseRequest, EmpresaCallback callback) {
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

        EmpresaApi api = retrofit.create(EmpresaApi.class);
        Call<JsonObject> call = api.register(enterpriseRequest);
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