package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesEnterpriseBinding;

public class VacanciesEnterpriseFragment extends Fragment {

    private View rootView;

    private FragmentVacanciesEnterpriseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVacanciesEnterpriseBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        rootView = binding.getRoot();

        setupKeyboardListener();

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
                    binding.searchVacancieEnterpriseByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacancieEnterpriseInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }

}