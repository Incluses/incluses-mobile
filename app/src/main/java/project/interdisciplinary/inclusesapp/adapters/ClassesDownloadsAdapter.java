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

public class ClassesDownloadsAdapter extends RecyclerView.Adapter<ClassesDownloadsAdapter.ItemClassesDownloadsViewHolder> {
    private List<Arquivo> listaArquivos = new ArrayList<>();

    public ClassesDownloadsAdapter(List<Arquivo> listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    @NonNull
    @Override
    public ClassesDownloadsAdapter.ItemClassesDownloadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classe_course, parent, false);
        return new ItemClassesDownloadsViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesDownloadsAdapter.ItemClassesDownloadsViewHolder holder, int position) {
        Arquivo Arquivo = listaArquivos.get(position);

        // setagem dos dados do item de classe
        holder.nameClasse.setText(Arquivo.getNome());

        // Adicionar o listener de click para executar o metodo para baixar o conteudo no hardware
        //holder.itemView.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return listaArquivos.size();
    }

    public class ItemClassesDownloadsViewHolder extends RecyclerView.ViewHolder {

        private TextView nameClasse;
        public ItemClassesDownloadsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameClasse = itemView.findViewById(R.id.titleClasseCourseItem);
        }
    }
}