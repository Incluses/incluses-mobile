package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;
import java.util.UUID;

public class Arquivo implements Serializable {
    private UUID id;

    private String nome;
    private String tamanho;

    private UUID fkTipoArquivoId;

    private TipoArquivo tipoArquivo;

    public Arquivo(String nome, String tamanho, UUID fkTipoArquivoId) {
        this.nome = nome;
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
                "\"tamanho\": \"" + tamanho + "\"," +
                "\"fkTipoArquivoId\": \"" + (fkTipoArquivoId != null ? fkTipoArquivoId.toString() : "null") + "\"," +
                "\"tipoArquivo\": " + (tipoArquivo != null ? tipoArquivo.toString() : "null") +
                "}";
    }

}

