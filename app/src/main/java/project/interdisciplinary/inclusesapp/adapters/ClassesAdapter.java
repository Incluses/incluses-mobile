package project.interdisciplinary.inclusesapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.Presentation.CourseClass;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ItemClassesDownloadsViewHolder> {
    private List<MaterialCurso> listaMateriais = new ArrayList<>();

    public ClassesAdapter(List<MaterialCurso> listaMateriais) {
        this.listaMateriais = listaMateriais;
    }

    @NonNull
    @Override
    public ClassesAdapter.ItemClassesDownloadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classe_course, parent, false);
        return new ItemClassesDownloadsViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesAdapter.ItemClassesDownloadsViewHolder holder, int position) {
        MaterialCurso materialCurso = listaMateriais.get(position);

        // setagem dos dados do item de classe
        holder.nameClasse.setText(materialCurso.getNome());
        Bundle bundle = new Bundle();
        bundle.putString("materialCurso", materialCurso.toString());
        // Adicionar o listener de click para executar o metodo para baixar o conteudo no hardware
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseClass.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMateriais.size();
    }

    public class ItemClassesDownloadsViewHolder extends RecyclerView.ViewHolder {

        private TextView nameClasse;
        public ItemClassesDownloadsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameClasse = itemView.findViewById(R.id.titleClasseCourseItem);
        }
    }
}