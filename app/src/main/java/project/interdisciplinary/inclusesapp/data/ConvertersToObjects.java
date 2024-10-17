package project.interdisciplinary.inclusesapp.data;

import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
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
                    perfil.setId(UUID.fromString(value));
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
                    perfil.setFkTipoPerfilId(UUID.fromString(value));
                    break;
                case "fkFtPerfilId":
                    perfil.setFkFtPerfilId(UUID.fromString(value));
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
                                tipoPerfil.setId(UUID.fromString(tipoValue));
                                break;
                            case "nome":
                                tipoPerfil.setNome(tipoValue);
                                break;
                        }
                    }
                    perfil.setTipoPerfil(tipoPerfil);
                    break;
                case "fotoPerfil":
                    // Assumindo que a fotoPerfil tenha id, nome, s3Url, etc.
                    String[] fotoPerfilData = value.replace("Arquivo{", "").replace("}", "").split(", ");
                    Arquivo arquivo = new Arquivo();
                    for (String arquivoField : fotoPerfilData) {
                        String[] arquivoKeyValue = arquivoField.split("=");
                        String arquivoKey = arquivoKeyValue[0];
                        String arquivoValue = arquivoKeyValue.length > 1 ? arquivoKeyValue[1].replace("'", "") : "";

                        switch (arquivoKey) {
                            case "id":
                                arquivo.setId(UUID.fromString(arquivoValue));
                                break;
                            case "nome":
                                arquivo.setNome(arquivoValue);
                                break;
                            case "s3Url":
                                arquivo.setS3Url(arquivoValue);
                                break;
                            case "s3Key":
                                arquivo.setS3Key(arquivoValue);
                                break;
                            case "tamanho":
                                arquivo.setTamanho(arquivoValue);
                                break;
                            case "fkTipoArquivoId":
                                arquivo.setFkTipoArquivoId(UUID.fromString(arquivoValue));
                                break;
                        }
                    }
                    perfil.setFotoPerfil(arquivo);
                    break;
            }
        }

        return perfil;
    }

}
