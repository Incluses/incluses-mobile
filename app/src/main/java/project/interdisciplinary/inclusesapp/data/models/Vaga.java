package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;
import java.util.UUID;

public class Vaga implements Serializable {

    private UUID id;
    private String descricao;
    private String nome;
    private UUID fkEmpresaId;
    private UUID fkTipoVagaId;

    private Empresa empresa;
    private TipoVaga tipoVaga;

    // Construtores
    public Vaga() {
    }

    @Override
    public String toString() {
        return "Vaga{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", nome='" + nome + '\'' +
                ", fkEmpresaId=" + fkEmpresaId +
                ", fkTipoVagaId=" + fkTipoVagaId +
                ", empresa=" + empresa +
                ", tipoVaga=" + tipoVaga +
                '}';
    }

    public Vaga(UUID id, String descricao, String nome, UUID fkEmpresaId, UUID fkTipoVagaId) {
        this.id = id;
        this.descricao = descricao;
        this.nome = nome;
        this.fkEmpresaId = fkEmpresaId;
        this.fkTipoVagaId = fkTipoVagaId;
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getFkEmpresaId() {
        return fkEmpresaId;
    }

    public void setFkEmpresaId(UUID fkEmpresaId) {
        this.fkEmpresaId = fkEmpresaId;
    }

    public UUID getFkTipoVagaId() {
        return fkTipoVagaId;
    }

    public void setFkTipoVagaId(UUID fkTipoVagaId) {
        this.fkTipoVagaId = fkTipoVagaId;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TipoVaga getTipoVaga() {
        return tipoVaga;
    }

    public void setTipoVaga(TipoVaga tipoVaga) {
        this.tipoVaga = tipoVaga;
    }
}
