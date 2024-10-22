package project.interdisciplinary.inclusesapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.TipoPerfil;
import project.interdisciplinary.inclusesapp.data.models.Vaga;

public class ConvertersToObjects {

    public static Vaga convertStringToVaga(String vagaString) {
        Gson gson = new Gson();
        return gson.fromJson(vagaString, Vaga.class);
    }

    public static Perfil convertStringToPerfil(String perfilString) {
        Gson gson = new Gson();
        return gson.fromJson(perfilString, Perfil.class);
    }

    public static MaterialCurso convertStringToMaterialCurso(String materialCursoString) {
        Gson gson = new Gson();
        Log.e("JSON", "convertStringToMaterialCurso: " + materialCursoString );
        return gson.fromJson(materialCursoString, MaterialCurso.class);
    }


    public static Curso convertStringToCurso(String cursoString) {
        Gson gson = new Gson();
        return gson.fromJson(cursoString, Curso.class);
    }
}
