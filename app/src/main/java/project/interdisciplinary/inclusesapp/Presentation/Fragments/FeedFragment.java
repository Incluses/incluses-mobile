package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.Presentation.CreatePost;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment {
    private FragmentFeedBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        binding.createPostPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePost createPost = new CreatePost();
                startActivity(new Intent(getActivity(), createPost.getClass()));
            }
        });

        return view;
    }
}