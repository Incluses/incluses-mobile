package project.interdisciplinary.inclusesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Vaga;

public class VacanciesAdapter extends RecyclerView.Adapter<VacanciesAdapter.ItemVacancieViewHolder> {
    private List<Vaga> vagas = new ArrayList<>();

    public VacanciesAdapter(List<Vaga> vagas) {
        this.vagas = vagas;
    }

    @NonNull
    @Override
    public VacanciesAdapter.ItemVacancieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacancies, parent, false);
        return new ItemVacancieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VacanciesAdapter.ItemVacancieViewHolder holder, int position) {
        Vaga vaga = vagas.get(position);

        // setagem dos dados do item de curso
        holder.nameVacancie.setText(vaga.getNome());
        holder.nameEnterprise.setText(vaga.getEmpresa().getPerfil().getNome());
        holder.typeWork.setText(vaga.getTipoVaga().getNome());

        holder.btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Lógica para editar a vaga
                // Editar a vaga
            }
        });
    }

    @Override
    public int getItemCount() {
        return vagas.size();
    }

    public class ItemVacancieViewHolder extends RecyclerView.ViewHolder {

        private Button btnSubscribe;

        private TextView nameVacancie;
        private TextView nameEnterprise;
        private TextView typeWork;

        private TextView accessVacancieTextView;



        public ItemVacancieViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSubscribe = itemView.findViewById(R.id.subscribeVacancieButton);
            nameVacancie = itemView.findViewById(R.id.nameVacancieTextView);
            nameEnterprise = itemView.findViewById(R.id.enterpriseVacancieTextView);
            typeWork = itemView.findViewById(R.id.modeVacancieTextView);

            accessVacancieTextView = itemView.findViewById(R.id.acessVacancieTextView);

            accessVacancieTextView.setVisibility(View.GONE);
        }
    }
}