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
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ItemCoursesViewHolder> {
    private List<AvaliacaoCurso> listaAvaliacaoCurso = new ArrayList<>();

    public CoursesAdapter(List<AvaliacaoCurso> listaAvaliacaoCurso) {
        this.listaAvaliacaoCurso = listaAvaliacaoCurso;
    }

    @NonNull
    @Override
    public CoursesAdapter.ItemCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar o layout do item
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new ItemCoursesViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.ItemCoursesViewHolder holder, int position) {
        AvaliacaoCurso avaliacaoCurso = listaAvaliacaoCurso.get(position);

        // setagem dos dados do item de curso
        holder.nameCourse.setText(avaliacaoCurso.getCurso().getNome());
        holder.avaliacaoCurso.setRating((float) avaliacaoCurso.getNota());
        holder.avaliacaoTextView.setText(String.valueOf( avaliacaoCurso.getNota()));

        holder.btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: Lógica para iniciar o curso
                // Iniciar o curso
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAvaliacaoCurso.size();
    }

    public class ItemCoursesViewHolder extends RecyclerView.ViewHolder {

        private Button btnInit;
        private TextView nameCourse;

        private RatingBar avaliacaoCurso;

        private TextView avaliacaoTextView;

        private TextView skillsTextView;

        private TextView youLearnTextView;

        public ItemCoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            btnInit = itemView.findViewById(R.id.initCourseButton);
            nameCourse = itemView.findViewById(R.id.nameCourseTextViewItem);
            avaliacaoCurso = itemView.findViewById(R.id.courseRatingBar);
            avaliacaoTextView = itemView.findViewById(R.id.numberRatingTextViewItem);
            skillsTextView = itemView.findViewById(R.id.skillsTextView);
            youLearnTextView = itemView.findViewById(R.id.youLearnTextView);

            skillsTextView.setVisibility(View.GONE);
            youLearnTextView.setVisibility(View.GONE);
        }
    }
}