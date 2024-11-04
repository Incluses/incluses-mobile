package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.HomeEnterprise;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginApi;
import project.interdisciplinary.inclusesapp.data.dbApi.LoginCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterUser2Binding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterUser2 extends AppCompatActivity {

    Date date = null;

    private DatabaseFirebase firebase = new DatabaseFirebase();

    private Retrofit retrofit;
    private ActivityRegisterUser2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Bundle infosUser = getIntent().getExtras();

        Log.i("RegisterUser2", "onCreate: " + infosUser.getString("name"));
        Log.i("RegisterUser2", "onCreate: " + infosUser);

        binding = ActivityRegisterUser2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAutoComplete();

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    finish();
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    finish();
                }
        );


        //input born date
        binding.dateBornEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterUser2.this, R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                binding.dateBornEditText.setText(selectedDate);

                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);

                                int age = calculateAge(dob);

                                if (age < 14) {
                                    binding.dateBornInputLayout.setError(getString(R.string.error_underage));
                                } else {
                                    binding.dateBornInputLayout.setError(null);
                                    date = new Date(year,month,dayOfMonth);
                                }
                            }
                        },
                        year, month, dayOfMonth

                );

                dialog.show();
            }
        });

        //continue button
        binding.continueButton.setOnClickListener(
                v -> {
                    boolean dateFilled, pronounFilled, situationFilled, socialNameFilled;
                    // validando data
                    if(date == null){
                        dateFilled = false;
                        binding.dateBornEditText.setError("Selecione sua data de nascimennto",
                                getResources().getDrawable(R.drawable.ic_calendar));
                    }
                    else {
                        dateFilled = true;
                        binding.dateBornInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                        binding.dateBornInputLayout.setErrorIconDrawable(null); // Remove o ícone de
                    }

                    // validando pronome
                    String inputPronoun = binding.pronounEditText.getText().toString();
                    if (!inputPronoun.isEmpty()){
                        pronounFilled = true;
                    }
                    else {
                        pronounFilled = false;
                    }
                    // validando situacao

                    String inputSituation = binding.laborSituationAutoCompleteTextView.getText().toString();
                    if (!inputSituation.isEmpty()){
                        situationFilled = true;
                    }
                    else {
                        situationFilled = false;
                    }

                    // validando nome social
                    String inputSocialName = binding.nameSocialEditText.getText().toString();
                    if(!inputSituation.isEmpty()){
                        socialNameFilled = true;
                    }
                    else {
                        socialNameFilled = false;
                    }
                    if(dateFilled && pronounFilled && situationFilled){
                        CreateUserRequest createUserRequest;
                        String name = infosUser.getString("name");
                        String email = infosUser.getString("email");
                        String password = infosUser.getString("password");
                        String phone = infosUser.getString("phone_number");
                        String cpf = infosUser.getString("cpf");
                        if(socialNameFilled){
                            createUserRequest = new CreateUserRequest(cpf, date,
                                    inputPronoun, inputSocialName,
                                    name, password, email, phone
                            );
                        }
                        else {
                            createUserRequest = new CreateUserRequest(cpf, date,
                                    inputPronoun, null,
                                    name, password, email, phone
                            );
                        }
                        callApiRetrofitRegister(createUserRequest, new UsuarioCallback() {
                            @Override
                            public void onSuccess(JsonObject response) {
                                LoginRequest login = new LoginRequest(email, password);
                                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                callApiRetrofitLogin(login, new LoginCallback() {
                                    @Override
                                    public void onSuccess(LoginResponse loginResponse) {
                                        String token = loginResponse.getToken();
                                        Perfil perfil = loginResponse.getPerfil();

                                        editor.putString("token", token);
                                        editor.putString("perfil", perfil.toString());
                                        editor.putBoolean("isLogged", true);
                                        editor.putBoolean("isEnterprise", false);
                                        editor.apply();

                                        String type = loginResponse.getType();
                                        Intent intent = new Intent(RegisterUser2.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccessAdmin(JsonObject loginResponse) {
                                        // Não utilizado aqui
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        firebase.saveError(new Error("Erro ao carregar o token: " + throwable.getMessage()));
                                        Log.e("LoginError", throwable.getMessage());
                                        Toast.makeText(RegisterUser2.this, "Erro no login", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Toast.makeText(RegisterUser2.this, "erro", Toast.LENGTH_LONG).show();
                                Log.e("EU",throwable.getMessage());

                            }
                        });
                    }

                }
        );

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, LoginUser.class);
                    startActivity(intent);
                    finish();
                }
        );
    }
    private int calculateAge(Calendar dob) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private void setupAutoComplete() {
        // Array of states
        String[] states = getResources().getStringArray(R.array.states_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        binding.laborSituationAutoCompleteTextView.setAdapter(adapter);
        binding.laborSituationAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void callApiRetrofitRegister(CreateUserRequest user, UsuarioCallback callback) {
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

        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.register(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno registro", String.valueOf(response.code()));
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
    private void callApiRetrofitLogin(LoginRequest login, LoginCallback callback) {
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

        LoginApi api = retrofit.create(LoginApi.class);
        Call<LoginResponse> call = api.login(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e("retorno login", String.valueOf(response.code()));

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Login ou senha inválidos!"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}