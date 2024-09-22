package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.CreateVacancieEnterprise;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateVacancieEnterpriseBinding;

public class CreateVacancieEnterpriseFragment extends Fragment {

    private FragmentCreateVacancieEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateVacancieEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnAllVacanciesEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VacanciesEnterpriseFragment vacanciesEnterpriseFragment = new VacanciesEnterpriseFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, vacanciesEnterpriseFragment).commit();
            }
        });

        binding.createVacanciesMyVacancieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateVacancieEnterprise createVacancieEnterprise = new CreateVacancieEnterprise();
                Intent intent = new Intent(getContext(), createVacancieEnterprise.getClass());
                startActivity(intent);
            }
        });


        return view;
    }
}