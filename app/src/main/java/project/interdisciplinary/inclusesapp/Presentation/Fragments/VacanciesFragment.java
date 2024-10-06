package project.interdisciplinary.inclusesapp.Presentation.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.FragmentChatBinding;
import project.interdisciplinary.inclusesapp.databinding.FragmentVacanciesBinding;

public class VacanciesFragment extends Fragment {

    private boolean search = false;

    private String nomeVaga;

    public VacanciesFragment() {}
    public VacanciesFragment(boolean search, String nomeVaga) {
        this.search = search;
        this.nomeVaga = nomeVaga;
    }

    private FragmentVacanciesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVacanciesBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();

        Log.e("Teste", "Nome: " + nomeVaga);

//        //Verificação se é procura de vaga
//        if (search) {
//            Lógica para procurar vaga
//        }

        return view;
    }
}