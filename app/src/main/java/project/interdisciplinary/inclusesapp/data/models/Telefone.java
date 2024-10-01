package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class Telefone {

    private UUID id;
    private String telefone;
    private UUID fkPerfilId;
    private Perfil perfil;

    // Construtores
    public Telefone() {
    }

    public Telefone(UUID id, String telefone, UUID fkPerfilId) {
        this.id = id;
        this.telefone = telefone;
        this.fkPerfilId = fkPerfilId;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
