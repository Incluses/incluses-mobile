package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class Endereco {

    private UUID id;
    private String rua;
    private String estado;
    private String cidade;
    private String cep;
    private Integer numero;

    // Construtores
    public Endereco() {
    }

    public Endereco(UUID id, String rua, String estado, String cidade, String cep, Integer numero) {
        this.id = id;
        this.rua = rua;
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.numero = numero;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
