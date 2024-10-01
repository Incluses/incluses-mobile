package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class Configuracao {

    private UUID id;
    private Boolean notificacao;
    private UUID fkPerfilId;
    private Perfil perfil;

    // Construtores
    public Configuracao() {
    }

    public Configuracao(UUID id, Boolean notificacao, UUID fkPerfilId, Perfil perfil) {
        this.id = id;
        this.notificacao = notificacao;
        this.fkPerfilId = fkPerfilId;
        this.perfil = perfil;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Boolean notificacao) {
        this.notificacao = notificacao;
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
