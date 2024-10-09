package project.interdisciplinary.inclusesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.Perfil;

public class ProfilesSearchAdapter extends RecyclerView.Adapter<ProfilesSearchAdapter.ItemProfilesSearchViewHolder> {
    private List<Perfil> perfilList = new ArrayList<>();

    public ProfilesSearchAdapter(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    @NonNull
    @Override
    public ProfilesSearchAdapter.ItemProfilesSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_search, parent, false);
        return new ItemProfilesSearchViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilesSearchAdapter.ItemProfilesSearchViewHolder holder, int position) {
        Perfil perfil = perfilList.get(position);

        // setagem dos dados do item de classe
//        holder.perfilPhotoImageView.setImageResource(perfil.getFotoPerfil().getS3Url());
        holder.nameProfile.setText(perfil.getNome());
        holder.descriptionProfile.setText(perfil.getBiografia());

        // Adicionar o listener de click para executar algo
        //holder.itemView.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return perfilList.size();
    }

    public class ItemProfilesSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView nameProfile;

        private TextView descriptionProfile;

        private ImageView perfilPhotoImageView;
        public ItemProfilesSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProfile = itemView.findViewById(R.id.nameProfileSearchTextView);
            descriptionProfile = itemView.findViewById(R.id.descriptionProfileSearchTextview);
            perfilPhotoImageView = itemView.findViewById(R.id.perfilPhotoImageView);
        }
    }
}