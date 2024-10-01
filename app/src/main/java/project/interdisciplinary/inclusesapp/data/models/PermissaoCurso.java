package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class PermissaoCurso {

    private UUID id;
    private Boolean permissao = false;
    private Curso curso;

    // Construtores
    public PermissaoCurso() {
    }

    public PermissaoCurso(UUID id, Boolean permissao, Curso curso) {
        this.id = id;
        this.permissao = permissao;
        this.curso = curso;
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
