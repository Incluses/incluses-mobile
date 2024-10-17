package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class CriarVagaDTO {
    private String descricao;
    private String nome;
    private UUID empresaId;
    private UUID tipoVagaId;

    // Getters e Setters
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

    public UUID getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(UUID empresaId) {
        this.empresaId = empresaId;
    }

    public UUID getTipoVagaId() {
        return tipoVagaId;
    }

    public void setTipoVagaId(UUID tipoVagaId) {
        this.tipoVagaId = tipoVagaId;
    }
}

