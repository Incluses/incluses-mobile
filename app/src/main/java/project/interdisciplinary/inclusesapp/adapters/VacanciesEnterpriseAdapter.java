package project.interdisciplinary.inclusesapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.Presentation.ViewVacancie;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
public class VacanciesEnterpriseAdapter extends RecyclerView.Adapter<VacanciesEnterpriseAdapter.ItemVacancieViewHolder> {
    private List<Vaga> vagas = new ArrayList<>();

    public VacanciesEnterpriseAdapter(List<Vaga> vagas) {
        this.vagas = vagas;
    }

    @NonNull
    @Override
    public VacanciesEnterpriseAdapter.ItemVacancieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacancies, parent, false);
        return new ItemVacancieViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VacanciesEnterpriseAdapter.ItemVacancieViewHolder holder, int position) {
        Vaga vaga = vagas.get(position);

        // setagem dos dados do item de curso
        holder.nameVacancie.setText(vaga.getNome());
        holder.nameEnterprise.setText(vaga.getEmpresa().getPerfil().getNome());
        holder.typeWork.setText(vaga.getTipoVaga().getNome());

        holder.btnView.setText("Analisar");
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("vaga", vaga.toString());
                bundle.putString("type", "enterprise");
                Intent intent = new Intent(v.getContext(), ViewVacancie.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vagas.size();
    }

    public class ItemVacancieViewHolder extends RecyclerView.ViewHolder {

        private Button btnView;

        private TextView nameVacancie;
        private TextView nameEnterprise;
        private TextView typeWork;

        private TextView accessVacancieTextView;



        public ItemVacancieViewHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.viewVacancieButton);
            nameVacancie = itemView.findViewById(R.id.nameVacancieTextView);
            nameEnterprise = itemView.findViewById(R.id.enterpriseVacancieTextView);
            typeWork = itemView.findViewById(R.id.modeVacancieTextView);

            accessVacancieTextView = itemView.findViewById(R.id.acessVacancieTextView);

            accessVacancieTextView.setVisibility(View.GONE);
        }
    }
}