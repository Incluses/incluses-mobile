package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;
import java.util.UUID;

public class Arquivo implements Serializable {
    private UUID id;

    private String nome;

    private String s3Url;

    private String s3Key;


    private String tamanho;

    private UUID fkTipoArquivoId;

    private TipoArquivo tipoArquivo;

    public Arquivo(String nome, String tamanho, UUID fkTipoArquivoId) {
        this.nome = nome;
        this.s3Url = "a";
        this.s3Key = "a";
        this.tamanho = tamanho;
        this.fkTipoArquivoId = fkTipoArquivoId;
    }

    // Getters and Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getTamanho() {
        return tamanho;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkTipoArquivoId() {
        return fkTipoArquivoId;
    }

    public void setFkTipoArquivoId(UUID fkTipoArquivoId) {
        this.fkTipoArquivoId = fkTipoArquivoId;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public TipoArquivo getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(TipoArquivo tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + (id != null ? id.toString() : "null") + "\"," +
                "\"nome\": \"" + nome + "\"," +
                "\"s3Url\": \"" + s3Url + "\"," +
                "\"s3Key\": \"" + s3Key + "\"," +
                "\"tamanho\": \"" + tamanho + "\"," +
                "\"fkTipoArquivoId\": \"" + (fkTipoArquivoId != null ? fkTipoArquivoId.toString() : "null") + "\"," +
                "\"tipoArquivo\": " + (tipoArquivo != null ? tipoArquivo.toString() : "null") +
                "}";
    }

}

