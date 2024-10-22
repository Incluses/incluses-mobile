package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class TipoPerfil {

    private UUID id;
    private String nome;

    // Construtores
    public TipoPerfil() {
    }

    public TipoPerfil(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + "\"," +
                "\"nome\": \"" + nome + "\"" +
                "}";
    }

}
