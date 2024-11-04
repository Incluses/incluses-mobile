package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.ActivityCourseClassBinding;

public class CourseClass extends AppCompatActivity {

    private ActivityCourseClassBinding binding;

    private DatabaseFirebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new DatabaseFirebase();

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCourseClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar o botão de voltar
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> finish());
        binding.textViewLoginUserBack.setOnClickListener(v -> finish());

        Intent extrasIntent = getIntent();
        Bundle bundle = extrasIntent.getExtras();
        MaterialCurso materialCurso = ConvertersToObjects.convertStringToMaterialCurso(bundle.getString("materialCurso"));
        binding.titleCourseClass.setText(materialCurso.getNome());
        binding.descriptionCourseClass.setText(materialCurso.getDescricao());
        binding.attachamentNameCourseClass.setText(materialCurso.getArquivo().getNome());
        binding.attachmentCourseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                firebase.getFileUriFromFirebase(materialCurso.getArquivo().getId().toString(),
                        uri -> {
                            intent.setDataAndType(uri, "*/*");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(CourseClass.this, "Não há aplicativo disponível para abrir este arquivo.", Toast.LENGTH_SHORT).show();
                            }
                        },
                        e -> {
                            Log.e("Firebase", "Erro ao obter URL de download", e);
                        }
                );


            }
        });
    }
}