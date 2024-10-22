package project.interdisciplinary.inclusesapp.data;

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
        Perfil perfil = new Perfil();

        // Remover a parte 'Perfil{' e '}'
        perfilString = perfilString.replace("Perfil{", "").replace("}", "");

        // Separar os campos
        String[] fields = perfilString.split(", ");

        // Iterar pelos campos e setar os valores no objeto Perfil
        for (String field : fields) {
            String[] keyValue = field.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1].replace("'", "") : ""; // Remove aspas simples

            switch (key) {
                case "id":
                    // Verificação de null e vazio antes de converter para UUID
                    if (value != null && !value.isEmpty()) {
                        perfil.setId(UUID.fromString(value));
                    }
                    break;
                case "nome":
                    perfil.setNome(value);
                    break;
                case "senha":
                    perfil.setSenha(value);
                    break;
                case "email":
                    perfil.setEmail(value);
                    break;
                case "biografia":
                    perfil.setBiografia(value);
                    break;
                case "fkTipoPerfilId":
                    // Verificação de null e vazio antes de converter para UUID
                    if (value != null && !value.isEmpty()) {
                        perfil.setFkTipoPerfilId(UUID.fromString(value));
                    }
                    break;
                case "tipoPerfil":
                    // Assumindo que o tipoPerfil tenha uma representação simples com id e nome
                    String[] tipoPerfilData = value.replace("TipoPerfil{", "").replace("}", "").split(", ");
                    TipoPerfil tipoPerfil = new TipoPerfil();
                    for (String tipoField : tipoPerfilData) {
                        String[] tipoKeyValue = tipoField.split("=");
                        String tipoKey = tipoKeyValue[0];
                        String tipoValue = tipoKeyValue.length > 1 ? tipoKeyValue[1].replace("'", "") : "";

                        switch (tipoKey) {
                            case "id":
                                // Verificação de null e vazio antes de converter para UUID
                                if (tipoValue != null && !tipoValue.isEmpty()) {
                                    tipoPerfil.setId(UUID.fromString(tipoValue));
                                }
                                break;
                            case "nome":
                                tipoPerfil.setNome(tipoValue);
                                break;
                        }
                    }
                    perfil.setTipoPerfil(tipoPerfil);
                    break;
            }
        }

        return perfil;
    }

    public static MaterialCurso convertStringToMaterialCurso(String materialCursoString) {
        MaterialCurso materialCurso = new MaterialCurso();

        // Remover a parte 'MaterialCurso{' e '}'
        materialCursoString = materialCursoString.replace("MaterialCurso{", "").replace("}", "");

        // Separar os campos
        String[] fields = materialCursoString.split(", ");

        // Iterar pelos campos e setar os valores no objeto MaterialCurso
        for (String field : fields) {
            String[] keyValue = field.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1].replace("'", "") : ""; // Remove aspas simples

            switch (key) {
                case "id":
                    materialCurso.setId(UUID.fromString(value));
                    break;
                case "nome":
                    materialCurso.setNome(value);
                    break;
                case "fkCursoId":
                    materialCurso.setFkCursoId(UUID.fromString(value));
                    break;
                case "fkArquivoId":
                    materialCurso.setFkArquivoId(UUID.fromString(value));
                    break;
                case "descricao":
                    materialCurso.setDescricao(value);
                    break;
                case "curso":
                    materialCurso.setCurso(convertStringToCurso(value));
                    break;
                case "arquivo":
                    materialCurso.setArquivo(convertStringToArquivo(value));
                    break;
            }
        }

        return materialCurso;
    }

    public static Curso convertStringToCurso(String cursoString) {
        Curso curso = new Curso();

        // Remover a parte 'Curso{' e '}'
        cursoString = cursoString.replace("Curso{", "").replace("}", "");

        // Separar os campos
        String[] fields = cursoString.split(", ");

        // Iterar pelos campos e setar os valores no objeto Curso
        for (String field : fields) {
            String[] keyValue = field.split("=");
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1].replace("'", "") : ""; // Remove aspas simples

            switch (key) {
                case "id":
                    curso.setId(UUID.fromString(value));
                    break;
                case "descricao":
                    curso.setDescricao(value);
                    break;
                case "fkPerfilId":
                    curso.setFkPerfilId(UUID.fromString(value));
                    break;
                case "nome":
                    curso.setNome(value);
                    break;
                case "perfil":
                    // Implementar lógica para setar o objeto Perfil a partir de uma string, se necessário
                    break;
            }
        }

        return curso;
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