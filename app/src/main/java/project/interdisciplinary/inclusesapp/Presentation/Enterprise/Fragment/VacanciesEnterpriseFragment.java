package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesEnterpriseBinding;

public class VacanciesEnterpriseFragment extends Fragment {

    private FragmentVacanciesEnterpriseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVacanciesEnterpriseBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        binding.btnYourVacancies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateVacancieEnterpriseFragment createVacancieEnterpriseFragment = new CreateVacancieEnterpriseFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, createVacancieEnterpriseFragment).commit();
            }
        });


        return view;
    }
}