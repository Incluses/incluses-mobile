package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.DetailsCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCoursesEnterpriseBinding;

public class CoursesEnterpriseFragment extends Fragment {

    private boolean search = false;

    private String nome = "";

    public CoursesEnterpriseFragment() {}
    public CoursesEnterpriseFragment(boolean search, String nome) {
        this.search = search;
        this.nome = nome;
    }

    private FragmentCoursesEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoursesEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        binding.btnCreateEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseEnterpriseFragment createCourseEnterpriseFragment = new CreateCourseEnterpriseFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewEnterprise, createCourseEnterpriseFragment).commit();
            }
        });


        return view;
    }
}