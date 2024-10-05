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
import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ItemMyCoursesViewHolder> {
    private List<AvaliacaoCurso> listaAvaliacaoCurso = new ArrayList<>();

    public MyCoursesAdapter(List<AvaliacaoCurso> listaAvaliacaoMeuCurso) {
        this.listaAvaliacaoCurso = listaAvaliacaoMeuCurso;
    }

    @NonNull
    @Override
    public MyCoursesAdapter.ItemMyCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_course, parent, false);
        return new ItemMyCoursesViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCoursesAdapter.ItemMyCoursesViewHolder holder, int position) {
        AvaliacaoCurso avaliacaoCurso = listaAvaliacaoCurso.get(position);

        // setagem dos dados do item de curso
        holder.nameMyCourse.setText(avaliacaoCurso.getCurso().getNome());
        holder.avaliacaoMeuCurso.setRating((float) avaliacaoCurso.getNota());
        holder.avaliacaoMeuCursoTextView.setText(String.valueOf( avaliacaoCurso.getNota()));

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Lógica para iniciar o curso
                // Editar o curso
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAvaliacaoCurso.size();
    }

    public class ItemMyCoursesViewHolder extends RecyclerView.ViewHolder {

        private Button btnDetails;
        private TextView nameMyCourse;

        private RatingBar avaliacaoMeuCurso;

        private TextView avaliacaoMeuCursoTextView;

        private TextView acessMyCourseTextView;



        public ItemMyCoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDetails = itemView.findViewById(R.id.editMyCourseButtonItem);
            nameMyCourse = itemView.findViewById(R.id.nameMyCourseTextViewItem);
            avaliacaoMeuCurso = itemView.findViewById(R.id.myCourseRatingBar);
            avaliacaoMeuCursoTextView = itemView.findViewById(R.id.numberRatingMyCourseTextView);
            acessMyCourseTextView = itemView.findViewById(R.id.acessMyCourseTextView);

            acessMyCourseTextView.setVisibility(View.GONE);
        }
    }
}