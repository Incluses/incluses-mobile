package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CriarInscricaoCursoDTO {
    private UUID usuarioId;
    private UUID cursoId;

    // Getters e Setters
    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getCursoId() {
        return cursoId;
    }

    public void setCursoId(UUID cursoId) {
        this.cursoId = cursoId;
    }
}

