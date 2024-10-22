package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class Curso {

    private UUID id;
    private String descricao;
    private UUID fkPerfilId;
    private String nome;
    private Perfil perfil;

    // Construtores
    public Curso() {
    }

    public Curso(UUID id, String descricao, UUID fkPerfilId, String nome, Perfil perfil) {
        this.id = id;
        this.descricao = descricao;
        this.fkPerfilId = fkPerfilId;
        this.nome = nome;
        this.perfil = perfil;
    }


    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"," +
                "\"descricao\": \"" + descricao + "\"," +
                "\"fkPerfilId\": \"" + fkPerfilId + "\"," +
                "\"nome\": \"" + nome + "\"," +
                "\"perfil\": " + (perfil != null ? perfil.toString() : "null") +
                "}";
    }

}