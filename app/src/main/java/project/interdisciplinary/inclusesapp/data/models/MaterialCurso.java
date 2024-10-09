package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;
import java.util.UUID;

public class MaterialCurso  implements Serializable {

    private UUID id;
    private String nome;
    private UUID fkCursoId;
    private UUID fkArquivoId;
    private String descricao;

    private Curso curso;
    private Arquivo arquivo;

    // Construtores
    public MaterialCurso() {
    }

    public MaterialCurso(UUID id, String nome, UUID fkCursoId, UUID fkArquivoId, String descricao, Curso curso, Arquivo arquivo) {
        this.id = id;
        this.nome = nome;
        this.fkCursoId = fkCursoId;
        this.fkArquivoId = fkArquivoId;
        this.descricao = descricao;
        this.curso = curso;
        this.arquivo = arquivo;
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

    public UUID getFkCursoId() {
        return fkCursoId;
    }

    public void setFkCursoId(UUID fkCursoId) {
        this.fkCursoId = fkCursoId;
    }

    public UUID getFkArquivoId() {
        return fkArquivoId;
    }

    public void setFkArquivoId(UUID fkArquivoId) {
        this.fkArquivoId = fkArquivoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
}
