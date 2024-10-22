package project.interdisciplinary.inclusesapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CreateCourseFragment;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.DetailsCourseFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.AvaliacaoCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.AvaliacaoCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ItemMyCoursesViewHolder> {
    private List<Curso> listaCurso = new ArrayList<>();
    private Retrofit retrofit;

    private boolean isEmpresa;
    private AppCompatActivity activity;
    private String token;


    public MyCoursesAdapter(AppCompatActivity activity,List<Curso> listaCurso, boolean isEmpresa) {
        this.activity = activity;
        this.listaCurso = listaCurso;
        this.isEmpresa = isEmpresa;
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
        Curso curso  = listaCurso.get(position);

        // setagem dos dados do item de curso
        holder.nameMyCourse.setText(curso.getNome());


        SharedPreferences preferences = holder.itemView.getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        Bundle extras = new Bundle();
        extras.putString("curso", curso.toString());
        callApiRetrofitFindAvalicao(curso.getId(), new AvaliacaoCursoCallback() {
            @Override
            public void onSuccessFind(List<AvaliacaoCurso> list) {
                boolean exists = false;
                double soma = 0;
                for (int i = 0; i < list.size(); i++){
                    if(curso.getId().equals(list.get(i).getFkCursoId())){
                      soma += list.get(i).getNota();
                      exists = true;
                    }
                }
                if (exists){
                    holder.avaliacaoMeuCurso.setRating((float) soma/list.size());
                    holder.avaliacaoMeuCursoTextView.setText(String.valueOf(soma/ list.size()));
                    extras.putDouble("avaliation",soma/ list.size() );
                }
                else {
                    holder.avaliacaoMeuCurso.setRating((float) soma);
                    holder.avaliacaoMeuCursoTextView.setText(String.valueOf(soma));
                    extras.putDouble("avaliation",soma);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
            @Override
            public void onSuccess(JsonObject jsonObject) {
            }
        });

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsCourseFragment detailsCourseFragment = new DetailsCourseFragment();
                extras.putBoolean("isEmpresa",isEmpresa);
                detailsCourseFragment.setArguments(extras);


                if(isEmpresa){
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerViewEnterprise, detailsCourseFragment)
                            .commit();
                }
                else {
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, detailsCourseFragment)
                            .commit();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return listaCurso.size();
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

        }
    }

    private void callApiRetrofitFindAvalicao(UUID cursoId , AvaliacaoCursoCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://incluses-api.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AvaliacaoCursoApi api = retrofit.create(AvaliacaoCursoApi.class);
        Call<List<AvaliacaoCurso>> call = api.findByFkCurso(token,cursoId);
        call.enqueue(new Callback<List<AvaliacaoCurso>>() {
            @Override
            public void onResponse(Call<List<AvaliacaoCurso>> call, Response<List<AvaliacaoCurso>> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    List<AvaliacaoCurso> responseBody = response.body();
                    callback.onSuccessFind(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<AvaliacaoCurso>> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}