package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCoursesBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateCourseBinding;

public class CreateCourseFragment extends Fragment {

    private View rootView;

    private FragmentCreateCourseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateCourseBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();

        binding.btnCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoursesFragment coursesFragment = new CoursesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, coursesFragment).commit();
            }
        });

        binding.createCourseMyCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCourseActivity createCourseActivity = new CreateCourseActivity();
                Intent intent = new Intent(getContext(), createCourseActivity.getClass());
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupKeyboardListener() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is opened
                } else {
                    // Keyboard is closed
                    binding.searchCoursesCreatedByNameEditText.clearFocus(); // Clear focus
                    binding.searchCoursesCreatedInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
}