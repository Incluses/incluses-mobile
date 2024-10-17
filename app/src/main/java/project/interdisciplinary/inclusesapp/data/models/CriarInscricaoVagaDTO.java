package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CriarInscricaoVagaDTO {
    private UUID usuarioId;
    private UUID vagaId;

    // Getters e Setters
    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getVagaId() {
        return vagaId;
    }

    public void setVagaId(UUID vagaId) {
        this.vagaId = vagaId;
    }
}

