package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.databinding.ActivityCourseClassBinding;

public class CourseClass extends AppCompatActivity {

    private ActivityCourseClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCourseClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent extrasIntent = getIntent();
        Bundle bundle = extrasIntent.getExtras();
        MaterialCurso materialCurso = ConvertersToObjects.convertStringToMaterialCurso(bundle.getString("materialCurso"));
        binding.titleCourseClass.setText(materialCurso.getNome());
        binding.descriptionCourseClass.setText(materialCurso.getDescricao());
        binding.attachamentNameCourseClass.setText(materialCurso.getNome());
    }
}