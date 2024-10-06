package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import project.interdisciplinary.inclusesapp.Presentation.CreatePost;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentFeedEnterpriseBinding;


public class FeedEnterpriseFragment extends Fragment {

    private View rootView;
    private FragmentFeedEnterpriseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        rootView = binding.getRoot();

        setupKeyboardListener();

        binding.createPostPostsEnterpriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePost createPost = new CreatePost();
                startActivity(new Intent(getActivity(), createPost.getClass()));
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
                    binding.searchPostsProfileEnterpriseByNameEditText.clearFocus(); // Clear focus
                    binding.searchPostsProfileEnterpriseInputLayout.clearFocus(); // Clear focus on TextInputLayout
                }
            }
        });
    }

}