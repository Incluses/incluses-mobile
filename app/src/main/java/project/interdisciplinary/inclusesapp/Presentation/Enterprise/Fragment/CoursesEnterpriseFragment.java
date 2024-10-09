package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.DetailsCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCoursesEnterpriseBinding;

public class CoursesEnterpriseFragment extends Fragment {

    private View rootView;

    private FragmentCoursesEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCoursesEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();

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
                    binding.searchCoursesEnterpriseByNameEditText.clearFocus(); // Clear focus
                    binding.searchCoursesEnterpriseInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }
}