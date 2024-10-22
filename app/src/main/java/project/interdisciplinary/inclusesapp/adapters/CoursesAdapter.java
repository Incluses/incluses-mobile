package project.interdisciplinary.inclusesapp.adapters;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.CourseInitialPage;
import project.interdisciplinary.inclusesapp.Presentation.CoursesViewed;
import project.interdisciplinary.inclusesapp.Presentation.Fragments.CoursesFragment;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.AvaliacaoCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.AvaliacaoCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaApi;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import project.interdisciplinary.inclusesapp.data.models.Cep;
import project.interdisciplinary.inclusesapp.data.models.CreateEnterpriseRequest;
import project.interdisciplinary.inclusesapp.data.models.CreateInscricaoCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.InscricaoCurso;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ItemCoursesViewHolder> {
    private List<Curso> listaCurso = new ArrayList<>();

    private Retrofit retrofit;
    private String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2FvQGV4YW1wbGUuY29tIiwicm9sZSI6IlJPTEVfRU1QUkVTQSIsImV4cCI6MTcyOTI1MjYwN30.MCJAqA8lPJSawRDehgT8bKthkRAVOFZQ27XELA0KjZHFd8ZlQrdFEZfOzYSVv2HTA6UTCmZetEAwTiu25lrJkA";
    private String idPerfil = "54d087dd-6356-4462-bdac-2c8cf5ef0494";


    public CoursesAdapter(List<Curso> listaCurso) {
        this.listaCurso = listaCurso;
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
        Curso curso = listaCurso.get(position);
        if (holder.itemView.getContext() instanceof CoursesViewed) {
            holder.btnInit.setText("Continuar");
        } else if (holder.itemView.getContext() instanceof CourseInitialPage) {
            holder.btnInit.setText("Iniciar");
        }
        callApiRetrofitFindUser(UUID.fromString(idPerfil), new UsuarioCallback() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                Gson gson = new Gson();
                Usuario apiResponse = gson.fromJson(jsonObject, Usuario.class);
                final UUID idUser = apiResponse.getId();
                holder.btnInit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo: Lógica para iniciar o curso
                        // Iniciar o curso
                        Bundle bundle = new Bundle();
                        bundle.putString("idCurso", curso.getId().toString());
                        if (holder.itemView.getContext() instanceof CoursesViewed) {
                            Intent intent = new Intent(v.getContext(), CourseInitialPage.class);
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        }
                        else {
                            CreateInscricaoCursoRequest inscricaoCursoRequest = new CreateInscricaoCursoRequest(idUser,curso.getId());
                            callApiRetrofitInsertInscricao(inscricaoCursoRequest, new InscricaoCursoCallback() {
                                @Override
                                public void onSuccess(JsonObject jsonObject) {
                                    Intent intent = new Intent(v.getContext(), CourseInitialPage.class);
                                    intent.putExtras(bundle);
                                    v.getContext().startActivity(intent);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccessFind(List<InscricaoCurso> list) {

                                }
                            });
                        }
                    }
                });
                callApiRetrofitFindAvalicao(apiResponse.getId(), new AvaliacaoCursoCallback() {
                    @Override
                    public void onSuccessFind(List<AvaliacaoCurso> list) {
                        boolean set = false;
                        for (int i = 0; i < list.size(); i++){
                            if(curso.getId().equals(list.get(i).getFkCursoId())){
                                holder.avaliacaoCurso.setRating((float) list.get(i).getNota());
                                holder.avaliacaoTextView.
                                        setText(String.valueOf( list.get(i).getNota()));
                                set = true;
                            }
                        }
                        holder.nameCourse.setText(curso.getNome());
                        if (!set){
                            holder.avaliacaoCurso.setRating(0);
                            holder.avaliacaoTextView.setText(String.valueOf( 0));
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });



        // setagem dos dados do item de curso



    }

    @Override
    public int getItemCount() {
        return listaCurso.size();
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
    private void callApiRetrofitFindUser(UUID userId ,UsuarioCallback callback) {
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

        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.getUserByProfileFk(token,userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    private void callApiRetrofitFindAvalicao(UUID userId , AvaliacaoCursoCallback callback) {
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
        Call<List<AvaliacaoCurso>> call = api.findByFkUser(token,userId);
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
    private void callApiRetrofitInsertInscricao(CreateInscricaoCursoRequest inscricaoCursoRequest ,InscricaoCursoCallback callback) {
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

        InscricaoCursoApi api = retrofit.create(InscricaoCursoApi.class);
        Call<JsonObject> call = api.insertInscricaoCurso(token,inscricaoCursoRequest);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
//    private void callApiRetrofitAlterAvaliacao(UUID avaliacaoId,float value ,AvaliacaoCursoCallback callback) {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        String urlApi = "https://incluses-api.onrender.com/";
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(urlApi)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Map<String,Double> request = new HashMap<>();
//        request.put("nota", Double.parseDouble(String.valueOf(value)));
//        AvaliacaoCursoApi api = retrofit.create(AvaliacaoCursoApi.class);
//        Call<JsonObject> call = api.updateAvaliacao(token,avaliacaoId, request);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e("retorno", String.valueOf(response.code()));
//                if (response.isSuccessful() && response.body() != null) {
//                    JsonObject responseBody = response.body();
//                    callback.onSuccess(responseBody); // Sucesso
//                } else if (response.code() == 401) {
//                }
//                else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable throwable) {
//                Log.e("ERRO", throwable.getMessage());
//                callback.onFailure(throwable); // Falha por erro de requisição
//            }
//        });
//    }
//   private void callApiRetrofitInsertAvaliacao(AvaliacaoCurso avaliacaoCurso ,AvaliacaoCursoCallback callback) {
//       OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        String urlApi = "https://incluses-api.onrender.com/";
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(urlApi)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        AvaliacaoCursoApi api = retrofit.create(AvaliacaoCursoApi.class);
//        Call<JsonObject> call = api.insertAvaliacao(token,avaliacaoCurso);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e("retorno", String.valueOf(response.code()));
//                if (response.isSuccessful() && response.body() != null) {
//                    JsonObject responseBody = response.body();
//                    callback.onSuccess(responseBody); // Sucesso
//                } else if (response.code() == 401) {
//                }
//                else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable throwable) {
//                Log.e("ERRO", throwable.getMessage());
//                callback.onFailure(throwable); // Falha por erro de requisição
//            }
//        });
//    }
}