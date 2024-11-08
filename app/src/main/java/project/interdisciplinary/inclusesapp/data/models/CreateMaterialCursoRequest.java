package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CreateMaterialCursoRequest {
    private String descricao;
    private String nome;
    private UUID cursoId;
    private UUID arquivoId;

    public CreateMaterialCursoRequest(String descricao, String nome, UUID cursoId, UUID arquivoId) {
        this.descricao = descricao;
        this.nome = nome;
        this.cursoId = cursoId;
        this.arquivoId = arquivoId;
    }

    // Getters e Setters

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getCursoId() {
        return cursoId;
    }

    public void setCursoId(UUID cursoId) {
        this.cursoId = cursoId;
    }

    public UUID getArquivoId() {
        return arquivoId;
    }

    public void setArquivoId(UUID arquivoId) {
        this.arquivoId = arquivoId;
    }
}
