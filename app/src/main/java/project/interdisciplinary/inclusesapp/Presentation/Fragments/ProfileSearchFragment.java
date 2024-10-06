package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentProfileSearchBinding;

public class ProfileSearchFragment extends Fragment {

    private boolean search = false;

    private String nome;

    public ProfileSearchFragment() {}
    public ProfileSearchFragment(boolean search, String nome) {
        this.search = search;
        this.nome = nome;
    }

    private FragmentProfileSearchBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileSearchBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        //        //Verificação se é procura do perfil
//        if (search) {
//            Lógica para procurar o perfil
//        }


        return view;
    }
}