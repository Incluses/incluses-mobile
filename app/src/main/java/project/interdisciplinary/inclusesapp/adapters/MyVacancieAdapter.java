package project.interdisciplinary.inclusesapp.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Vaga;

public class MyVacancieAdapter extends RecyclerView.Adapter<MyVacancieAdapter.ItemMyVacancieViewHolder> {
    private List<Vaga> vagas = new ArrayList<>();

    public MyVacancieAdapter(List<Vaga> vagas) {
        this.vagas = vagas;
    }

    @NonNull
    @Override
    public MyVacancieAdapter.ItemMyVacancieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_vacancie, parent, false);
        return new ItemMyVacancieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVacancieAdapter.ItemMyVacancieViewHolder holder, int position) {
        Vaga vaga = vagas.get(position);

        // setagem dos dados do item de curso
        holder.nameMyVacancie.setText(vaga.getNome());
        holder.nameEnterprise.setText(vaga.getEmpresa().getPerfil().getNome());
        holder.typeWork.setText(vaga.getTipoVaga().getNome());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Lógica para editar a vaga
                // Editar a vaga
            }
        });
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Lógica para excluir a vaga
                // Encerrar a vaga
            }
        });
    }

    @Override
    public int getItemCount() {
        return vagas.size();
    }

    public class ItemMyVacancieViewHolder extends RecyclerView.ViewHolder {

        private Button btnEdit;
        private Button btnClose;
        private TextView nameMyVacancie;
        private TextView nameEnterprise;
        private TextView typeWork;

        private TextView accessMyVacancieTextView;



        public ItemMyVacancieViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.editMyCourseButtonItem);
            btnClose = itemView.findViewById(R.id.btnCloseMyVacancie);
            nameMyVacancie = itemView.findViewById(R.id.nameMyVacancieTextView);
            nameEnterprise = itemView.findViewById(R.id.nameEnterpriseMyVacancieTextView);
            typeWork = itemView.findViewById(R.id.typeWorkMyVacancieItem);

            accessMyVacancieTextView = itemView.findViewById(R.id.accessMyVacancieTextView);

            accessMyVacancieTextView.setVisibility(View.GONE);
        }
    }
}
