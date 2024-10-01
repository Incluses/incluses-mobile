package project.interdisciplinary.inclusesapp.data.models;

import java.util.UUID;

public class Empresa {

    private UUID id;
    private String cnpj;
    private String razaoSocial;
    private UUID fkPerfilId;
    private String website;
    private UUID fkEnderecoId;
    private UUID fkSetorId;
    private Perfil perfil;
    private Endereco endereco;
    private Setor setor;

    // Construtores
    public Empresa() {
    }

    public Empresa(UUID id, String cnpj, String razaoSocial, UUID fkPerfilId, String website, UUID fkEnderecoId, UUID fkSetorId, Perfil perfil, Endereco endereco, Setor setor) {
        this.id = id;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.fkPerfilId = fkPerfilId;
        this.website = website;
        this.fkEnderecoId = fkEnderecoId;
        this.fkSetorId = fkSetorId;
        this.perfil = perfil;
        this.endereco = endereco;
        this.setor = setor;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public UUID getFkEnderecoId() {
        return fkEnderecoId;
    }

    public void setFkEnderecoId(UUID fkEnderecoId) {
        this.fkEnderecoId = fkEnderecoId;
    }

    public UUID getFkSetorId() {
        return fkSetorId;
    }

    public void setFkSetorId(UUID fkSetorId) {
        this.fkSetorId = fkSetorId;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
}
