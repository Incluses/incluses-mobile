package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.Presentation.CreateCourseActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.CreateVacancieEnterprise;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentCreateVacancieEnterpriseBinding;

public class CreateVacancieEnterpriseFragment extends Fragment {

    private View rootView;

    private FragmentCreateVacancieEnterpriseBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateVacancieEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        rootView = binding.getRoot();

        setupKeyboardListener();

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
                    binding.searchVacanciesCreatedByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacanciesCreatedInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }

}