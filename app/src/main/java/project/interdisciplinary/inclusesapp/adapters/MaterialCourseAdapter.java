package project.interdisciplinary.inclusesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;

public class MaterialCourseAdapter extends RecyclerView.Adapter<MaterialCourseAdapter.ItemMaterialCourseViewHolder> {
    private List<MaterialCurso> listaMaterialCursos = new ArrayList<>();

    public MaterialCourseAdapter(List<MaterialCurso> listaMaterialCursos) {
        this.listaMaterialCursos = listaMaterialCursos;
    }

    @NonNull
    @Override
    public MaterialCourseAdapter.ItemMaterialCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_material, parent, false);
        return new ItemMaterialCourseViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialCourseAdapter.ItemMaterialCourseViewHolder holder, int position) {
        MaterialCurso material = listaMaterialCursos.get(position);

        // setagem dos dados do item de classe
        holder.nameMaterial.setText(material.getNome());

        // Adicionar o listener de click para levar a tela de material do curso
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo: Lógica para abrir o material
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMaterialCursos.size();
    }

    public class ItemMaterialCourseViewHolder extends RecyclerView.ViewHolder {

        private TextView nameMaterial;
        public ItemMaterialCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameMaterial = itemView.findViewById(R.id.titleMaterialCourseItem);
        }
    }
}