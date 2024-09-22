package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityJobsOfInterestBinding;

public class JobsOfInterest extends AppCompatActivity {

    private ActivityJobsOfInterestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityJobsOfInterestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // button back
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

        binding.skipButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(JobsOfInterest.this, Home.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.continueButton.setOnClickListener(v -> {
                    Intent intent = new Intent(JobsOfInterest.this, Home.class);
                    startActivity(intent);
                    finish();
                }
        );

        // Action "selected" for all buttons is finish with "JobButton"
        setJobButtonClickListeners();
    }

    private void setJobButtonClickListeners() {
        // Array com os IDs de todos os botões
        int[] buttonIds = {
                R.id.analistJobButton,
                R.id.engineerDataJobButton,
                R.id.cientistJobButton,
                R.id.ciberSecurityJobButton,
                R.id.designerUxJobButton,
                R.id.devFrontJobButton,
                R.id.devAppJobButton,
                R.id.devBackJobButton,
                R.id.engineerDevopsJobButton,
                R.id.devIosJobButton,
                R.id.managerProjTiJobButton,
                R.id.devAndroidJobButton,
                R.id.devWarehousesDataJobButton,
                R.id.engineerNetworkJobButton,
                R.id.architectSolutJobButton,
                R.id.merchantDigitalJobButton,
                R.id.especialistOpSellJobButton,
                R.id.analistMarketingJobButton,
                R.id.representativeDevSellJobButton,
                R.id.representativeSellJobButton,
                R.id.managerProjJobButton,
                R.id.accountantJobButton,
                R.id.analistInteligenceAdminJobButton,
                R.id.especialistSuportTiJobButton,
                R.id.especialistResourcesHumansJobButton
        };

        // Loop para adicionar o listener de clique para cada botão
        for (int id : buttonIds) {
            Button button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    private boolean isSelected = false; // Flag para armazenar o estado do botão

                    @Override
                    public void onClick(View view) {
                        MaterialButton materialButton = (MaterialButton) button;
                        if (isSelected) {
                            button.setTextColor(getResources().getColor(R.color.black)); // Altere para a cor original
                            materialButton.setBackgroundTintList(getResources().getColorStateList(R.color.white)); // Altere para a cor de fundo original
                            materialButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.gray))); // Altere para a cor do contorno original
                        } else {
                            button.setTextColor(getResources().getColor(R.color.white));
                            materialButton.setBackgroundTintList(getResources().getColorStateList(R.color.purple));
                            materialButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                        }

                        isSelected = !isSelected;
                    }
                });
            }
        }
    }
}
