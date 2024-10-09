package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesBinding;

public class VacanciesFragment extends Fragment {

    private View rootView;

    private FragmentVacanciesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVacanciesBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();
        rootView = binding.getRoot();

        setupKeyboardListener();

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
                    binding.searchVacancieByNameEditText.clearFocus(); // Clear focus
                    binding.searchVacancieInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }

}