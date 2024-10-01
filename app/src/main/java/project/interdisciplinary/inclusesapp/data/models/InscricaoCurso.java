package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class InscricaoCurso {

    private UUID id;
    private UUID fkCursoId;
    private UUID fkUsuarioId;

    private Curso curso;
    private Usuario usuario;

    // Construtores
    public InscricaoCurso() {
    }

    public InscricaoCurso(UUID id, UUID fkCursoId, UUID fkUsuarioId, Curso curso, Usuario usuario) {
        this.id = id;
        this.fkCursoId = fkCursoId;
        this.fkUsuarioId = fkUsuarioId;
        this.curso = curso;
        this.usuario = usuario;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFkCursoId() {
        return fkCursoId;
    }

    public void setFkCursoId(UUID fkCursoId) {
        this.fkCursoId = fkCursoId;
    }

    public UUID getFkUsuarioId() {
        return fkUsuarioId;
    }

    public void setFkUsuarioId(UUID fkUsuarioId) {
        this.fkUsuarioId = fkUsuarioId;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
