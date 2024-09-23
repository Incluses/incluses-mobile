package project.interdisciplinary.inclusesapp.Presentation.Enterprise.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.CreatePost;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentFeedEnterpriseBinding;


public class FeedEnterpriseFragment extends Fragment {
    private FragmentFeedEnterpriseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedEnterpriseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.createPostPostsEnterpriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePost createPost = new CreatePost();
                startActivity(new Intent(getActivity(), createPost.getClass()));
            }
        });

        return view;
    }
}