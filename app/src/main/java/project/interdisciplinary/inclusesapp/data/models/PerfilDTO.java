package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class PerfilDTO {
    private String nome;
    private String senha;
    private String email;
    private UUID tipoPerfilId;
    private String telefone;

    // getters e setters


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getTipoPerfilId() {
        return tipoPerfilId;
    }

    public void setTipoPerfilId(UUID tipoPerfilId) {
        this.tipoPerfilId = tipoPerfilId;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
