package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;
import java.util.UUID;

public class Perfil implements Serializable {

    private UUID id;
    private String nome;
    private String senha;
    private String email;
    private String biografia;
    private UUID fkTipoPerfilId;
    private UUID fkFtPerfilId;

    private TipoPerfil tipoPerfil;
    private Arquivo fotoPerfil;

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                ", biografia='" + biografia + '\'' +
                ", fkTipoPerfilId=" + fkTipoPerfilId +
                ", fkFtPerfilId=" + fkFtPerfilId +
                ", tipoPerfil=" + tipoPerfil +
                ", fotoPerfil=" + fotoPerfil +
                '}';
    }

    // Construtores
    public Perfil() {
    }

    public Perfil(UUID id, String nome, String senha, String email, String biografia, UUID fkTipoPerfilId, UUID fkFtPerfilId, TipoPerfil tipoPerfil, Arquivo fotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.biografia = biografia;
        this.fkTipoPerfilId = fkTipoPerfilId;
        this.fkFtPerfilId = fkFtPerfilId;
        this.tipoPerfil = tipoPerfil;
        this.fotoPerfil = fotoPerfil;
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

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public UUID getFkTipoPerfilId() {
        return fkTipoPerfilId;
    }

    public void setFkTipoPerfilId(UUID fkTipoPerfilId) {
        this.fkTipoPerfilId = fkTipoPerfilId;
    }

    public UUID getFkFtPerfilId() {
        return fkFtPerfilId;
    }

    public void setFkFtPerfilId(UUID fkFtPerfilId) {
        this.fkFtPerfilId = fkFtPerfilId;
    }

    public TipoPerfil getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(TipoPerfil tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Arquivo getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Arquivo fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
