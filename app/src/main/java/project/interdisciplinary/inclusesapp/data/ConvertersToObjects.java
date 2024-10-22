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
        Vaga vaga = new Vaga();

        // Remover a parte 'Vaga{' e '}'
        vagaString = vagaString.replace("Vaga{", "").replace("}", "");

        // Separar os campos
        String[] fields = vagaString.split(", ");

        // Iterar pelos campos e setar os valores no objeto Vaga
        for (String field : fields) {
            String[] keyValue = field.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1].replace("'", "") : ""; // Remove aspas simples

            switch (key) {
                case "id":
                    vaga.setId(UUID.fromString(value));
                    break;
                case "descricao":
                    vaga.setDescricao(value);
                    break;
                case "nome":
                    vaga.setNome(value);
                    break;
                case "fkEmpresaId":
                    vaga.setFkEmpresaId(UUID.fromString(value));
                    break;
                case "fkTipoVagaId":
                    vaga.setFkTipoVagaId(UUID.fromString(value));
                    break;
                // Adicionar outros campos conforme necessário
            }
        }

        return vaga;
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

    public static Arquivo convertStringToArquivo(String arquivoString) {
        Arquivo arquivo = new Arquivo();

        // Remover a parte 'Arquivo{' e '}'
        arquivoString = arquivoString.replace("Arquivo{", "").replace("}", "");

        // Separar os campos
        String[] fields = arquivoString.split(", ");

        // Iterar pelos campos e setar os valores no objeto Arquivo
        for (String field : fields) {
            String[] keyValue = field.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1].replace("'", "") : ""; // Remove aspas simples

            switch (key) {
                case "id":
                    arquivo.setId(UUID.fromString(value));
                    break;
                case "nome":
                    arquivo.setNome(value);
                    break;
                case "s3Url":
                    arquivo.setS3Url(value);
                    break;
                case "s3Key":
                    arquivo.setS3Key(value);
                    break;
                case "tamanho":
                    arquivo.setTamanho(value);
                    break;
                case "fkTipoArquivoId":
                    arquivo.setFkTipoArquivoId(UUID.fromString(value));
                    break;
                case "tipoArquivo":
                    // Implementar lógica para setar o objeto TipoArquivo a partir de uma string, se necessário
                    break;
            }
        }

        return arquivo;
    }
}
