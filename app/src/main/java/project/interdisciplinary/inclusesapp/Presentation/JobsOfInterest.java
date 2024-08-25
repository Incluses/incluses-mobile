package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
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
                    Intent intent = new Intent(JobsOfInterest.this, RegisterUserActivity.class);
                    startActivity(intent);
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(JobsOfInterest.this, RegisterUserActivity.class);
                    startActivity(intent);
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

        // Loop for add the listener of click for it button
        for (int id : buttonIds) {
            Button button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button.setTextColor(getResources().getColor(R.color.white));

                        // Definir a cor do backgroundTint
                        button.setBackgroundTintList(getResources().getColorStateList(R.color.purple));

                        // Definir a cor do stroke
                        // Verifique se o fundo do botão é um `MaterialButton`

                        MaterialButton materialButton = (MaterialButton) button;
                        materialButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.purple)));
                    }
                });
            }
        }
    }
}
