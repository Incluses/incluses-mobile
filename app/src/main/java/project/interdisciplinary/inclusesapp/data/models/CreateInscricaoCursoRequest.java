package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CreateInscricaoCursoRequest {


    private UUID usuarioId;
    private UUID cursoId;

    public CreateInscricaoCursoRequest(UUID usuarioId, UUID cursoId) {
        this.usuarioId = usuarioId;
        this.cursoId = cursoId;
    }

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
