package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class PermissaoVaga {

    private UUID id;
    private Boolean permissao = false;
    private Vaga vaga;

    // Construtores
    public PermissaoVaga() {
    }

    public PermissaoVaga(UUID id, Boolean permissao, Vaga vaga) {
        this.id = id;
        this.permissao = permissao;
        this.vaga = vaga;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getPermissao() {
        return permissao;
    }

    public void setPermissao(Boolean permissao) {
        this.permissao = permissao;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }
}
