package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.RegisterEnterprise;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityVerifyNumberBinding;

public class VerifyNumber extends AppCompatActivity {

    private ActivityVerifyNumberBinding binding;

    private TextInputEditText otpEditText1, otpEditText2, otpEditText3, otpEditText4;

    private String phoneNumber;
    private String userType;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String cpf;
    private String cnpj;

    private String verificationCode;

    private static final int SMS_PERMISSION_CODE = 1; // Código de solicitação de permissão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityVerifyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recuperar número de telefone do Bundle
        phoneNumber = getIntent().getStringExtra("phone_number");
        userType = getIntent().getStringExtra("user_type");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone_number");
        cpf = getIntent().getStringExtra("cpf");
        cnpj = getIntent().getStringExtra("cnpj");

        Bundle infos = new Bundle();
        infos.putString("name", name);
        infos.putString("email", email);
        infos.putString("password", password);
        infos.putString("phone_number", phone);
        infos.putString("cpf", cpf);
        infos.putString("cnpj", cnpj);

        // Verificar permissão de SMS antes de enviar
        if (checkSmsPermission()) {
            sendSms(phoneNumber);
        } else {
            requestSmsPermission();
        }

        // Inicializar os campos de texto para o código OTP
        otpEditText1 = binding.fieldCode1VerifyNumberEditText;
        otpEditText2 = binding.fieldCode2VerifyNumberEditText;
        otpEditText3 = binding.fieldCode3VerifyNumberEditText;
        otpEditText4 = binding.fieldCode4VerifyNumberEditText;

        setupOTPInputs();

        binding.imageViewVerifyNumberBackButton.setOnClickListener(v -> finish());
        binding.textViewVerifyNumberBack.setOnClickListener(v -> finish());

        binding.resendCodeText.setOnClickListener(v -> {
            sendSms(phoneNumber);
        });

        // Verificar o código digitado quando o botão de continuar for clicado
        binding.toVerifyButton.setOnClickListener(v -> {

            if (verifyCode()) {

                Toast.makeText(this, "Código verificado com sucesso!", Toast.LENGTH_SHORT).show();
                if (userType.equals("user")) {
                    Intent intent = new Intent(this, RegisterUser2.class);
                    intent.putExtras(infos);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(this, RegisterEnterprise.class);
                    intent.putExtras(infos);
                    startActivity(intent);
                    finish();
                }

            }else {
                binding.subtitleSmsVerifyNumber.setText("Código incorreto. Tente novamente.");
                binding.subtitleSmsVerifyNumber.setTextColor(getResources().getColor(R.color.red_error));
                binding.resendCodeText.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Código incorreto. Tente novamente.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupOTPInputs() {
        // Adiciona um TextWatcher para cada campo para mover o foco automaticamente
        otpEditText1.addTextChangedListener(new OTPTextWatcher(otpEditText1, null, otpEditText2));
        otpEditText2.addTextChangedListener(new OTPTextWatcher(otpEditText2, otpEditText1, otpEditText3));
        otpEditText3.addTextChangedListener(new OTPTextWatcher(otpEditText3, otpEditText2, otpEditText4));
        otpEditText4.addTextChangedListener(new OTPTextWatcher(otpEditText4, otpEditText3, null)); // Último campo não tem próximo
    }

    private class OTPTextWatcher implements TextWatcher {
        private View currentView;
        private View previousView;
        private View nextView;

        OTPTextWatcher(View currentView, View previousView, View nextView) {
            this.currentView = currentView;
            this.previousView = previousView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                // Se há apenas um caractere, mover para o próximo campo
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0 && previousView != null) {
                // Se o campo está vazio e há um campo anterior, mover para o campo anterior
                previousView.requestFocus();
            }
        }
    }

    private boolean verifyCode() {
        // Recuperar o código digitado pelo usuário
        String enteredCode = otpEditText1.getText().toString() +
                otpEditText2.getText().toString() +
                otpEditText3.getText().toString() +
                otpEditText4.getText().toString();

        // Verificar se o código digitado corresponde ao código enviado
        if (verificationCode != null && verificationCode.equals(enteredCode)) {
            return true;
        } else {
            return false;
        }
    }

    // Método para verificar se a permissão de SMS foi concedida
    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Método para solicitar a permissão de SMS
    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
    }

    // Método para enviar SMS com código de verificação
    private void sendSms(String phoneNumber) {
        // Gerar um código aleatório de 4 dígitos (1000 a 9999)
        Random random = new Random();
        int num = random.nextInt(9000) + 1000; // Gera um número entre 1000 e 9999
        verificationCode = String.valueOf(num);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, verificationCode, null, null);

        // Exibir mensagem de confirmação
        Toast.makeText(VerifyNumber.this, "SMS Enviado!", Toast.LENGTH_SHORT).show();
    }

    // Método para tratar a resposta do usuário à solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se a permissão foi concedida, enviar o SMS
                sendSms(phoneNumber);
            } else {
                // Se a permissão foi negada, mostrar mensagem ao usuário
                Toast.makeText(this, "Permissão de SMS negada. Não é possível enviar o código.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
