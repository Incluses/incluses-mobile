package project.interdisciplinary.inclusesapp.data.models;


import java.util.Date;

public class CreateUserRequest {

    private String cpf;

    private String dtNascimento;

    private String pronomes;
    private String nomeSocial;

    private String nome;

    private String senha;
    private String email;

    private String telefone;

    public CreateUserRequest(){
    }
    public CreateUserRequest(String cpf, Date dtNascimento, String pronomes, String nomeSocial, String nome, String senha, String email, String telefone) {
        this.cpf = cpf;
        this.dtNascimento = dtNascimento.getYear() + "-" +
                String.format("%02d", dtNascimento.getMonth()) + "-" +
                String.format("%02d", dtNascimento.getDay());        this.pronomes = pronomes;
        this.nomeSocial = nomeSocial;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento.getYear() + "-" +
                        String.format("%02d", dtNascimento.getMonth()) + "-" +
                        String.format("%02d", dtNascimento.getDay());

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "cpf='" + cpf + '\'' +
                ", dtNascimento=" + dtNascimento +
                ", pronomes='" + pronomes + '\'' +
                ", nomeSocial='" + nomeSocial + '\'' +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}


