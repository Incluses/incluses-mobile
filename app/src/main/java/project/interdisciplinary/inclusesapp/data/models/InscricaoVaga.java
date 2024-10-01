package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class InscricaoVaga {

    private UUID id;
    private UUID fkUsuarioId;
    private UUID fkVagaId;

    private Usuario usuario;
    private Vaga vaga;

    // Construtores
    public InscricaoVaga() {
    }

    public InscricaoVaga(UUID id, UUID fkUsuarioId, UUID fkVagaId, Usuario usuario, Vaga vaga) {
        this.id = id;
        this.fkUsuarioId = fkUsuarioId;
        this.fkVagaId = fkVagaId;
        this.usuario = usuario;
        this.vaga = vaga;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkUsuarioId() {
        return fkUsuarioId;
    }

    public void setFkUsuarioId(UUID fkUsuarioId) {
        this.fkUsuarioId = fkUsuarioId;
    }

    public UUID getFkVagaId() {
        return fkVagaId;
    }

    public void setFkVagaId(UUID fkVagaId) {
        this.fkVagaId = fkVagaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }
}
