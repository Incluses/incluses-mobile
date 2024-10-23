package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CreateCursoRequest {
    private String descricao;
    private String nome;
    private UUID perfilId;


    public CreateCursoRequest(String descricao, String nome, UUID perfilId) {
        this.descricao = descricao;
        this.nome = nome;
        this.perfilId = perfilId;
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

    public UUID getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(UUID perfilId) {
        this.perfilId = perfilId;
    }
}

