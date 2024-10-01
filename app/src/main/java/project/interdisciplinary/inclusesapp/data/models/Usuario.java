package project.interdisciplinary.inclusesapp.data.models;

import java.util.Date;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private String cpf;
    private UUID fkPerfilId;
    private UUID fkCurriculoId;
    private Date dtNascimento;
    private String pronomes;
    private String nomeSocial;

    private Perfil perfil;
    private Arquivo curriculo;

    // Construtores
    public Usuario() {
    }

    public Usuario(UUID id, String cpf, UUID fkPerfilId, UUID fkCurriculoId, Date dtNascimento, String pronomes, String nomeSocial) {
        this.id = id;
        this.cpf = cpf;
        this.fkPerfilId = fkPerfilId;
        this.fkCurriculoId = fkCurriculoId;
        this.dtNascimento = dtNascimento;
        this.pronomes = pronomes;
        this.nomeSocial = nomeSocial;
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public UUID getFkPerfilId() {
        return fkPerfilId;
    }

    public void setFkPerfilId(UUID fkPerfilId) {
        this.fkPerfilId = fkPerfilId;
    }

    public UUID getFkCurriculoId() {
        return fkCurriculoId;
    }

    public void setFkCurriculoId(UUID fkCurriculoId) {
        this.fkCurriculoId = fkCurriculoId;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getPronomes() {
        return pronomes;
    }

    public void setPronomes(String pronomes) {
        this.pronomes = pronomes;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Arquivo getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(Arquivo curriculo) {
        this.curriculo = curriculo;
    }
}
