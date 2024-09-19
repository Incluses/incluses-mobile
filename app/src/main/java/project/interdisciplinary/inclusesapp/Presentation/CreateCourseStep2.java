package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateCourseStep2Binding;

public class CreateCourseStep2 extends AppCompatActivity {

    private ActivityCreateCourseStep2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateCourseStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelCreateMyCourse2Button.setOnClickListener(v -> finish());

    }
}