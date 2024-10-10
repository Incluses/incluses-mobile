package project.interdisciplinary.inclusesapp.data.models;

public class CreateEnterpriseRequest {
    private String cnpj;
    private String razaoSocial;
    private String setor;
    private String rua;
    private String estado;
    private String cidade;
    private String cep;
    private Integer numero;
    private String nome;
    private String senha;
    private String email;
    private String telefone;

    public CreateEnterpriseRequest(String cnpj, String razaoSocial,
                                   String setor, String rua,
                                   String estado, String cidade, String cep,
                                   Integer numero, String nome, String senha,
                                   String email, String telefone) {
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.setor = setor;
        this.rua = rua;
        this.estado = estado;
        this.cidade = cidade;
        this.cep = cep;
        this.numero = numero;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters e Setters
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
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
        return "CreateEnterpriseRequest{" +
                "cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", setor='" + setor + '\'' +
                ", rua='" + rua + '\'' +
                ", estado='" + estado + '\'' +
                ", cidade='" + cidade + '\'' +
                ", cep='" + cep + '\'' +
                ", numero=" + numero +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
