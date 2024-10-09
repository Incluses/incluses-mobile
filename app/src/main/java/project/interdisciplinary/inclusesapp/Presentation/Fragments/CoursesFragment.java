package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCoursesBinding;

public class CoursesFragment extends Fragment {
    private FragmentCoursesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseFragment createCourseFragment = new CreateCourseFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, createCourseFragment).commit();
            }
        });

        return view;
    }
}