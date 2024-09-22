package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateCourseEnterpriseBinding;

public class CreateCourseEnterpriseFragment extends Fragment {

    private FragmentCreateCourseEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateCourseEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnCoursesEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesEnterpriseFragment coursesEnterpriseFragment = new CoursesEnterpriseFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, coursesEnterpriseFragment).commit();
            }
        });

        binding.createCourseMyCourseEnterpriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseActivity createCourseActivity = new CreateCourseActivity();
                Intent intent = new Intent(getContext(), createCourseActivity.getClass());
                startActivity(intent);

                //Todo: Fazer um bundle pra a tela de criação de curso identificar se é o usuario ou a empresa que esta criando o curso
            }
        });

        return view;
    }
}