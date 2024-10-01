package project.interdisciplinary.inclusesapp.data.models;

public class LoginRequest {

    private String email;
    private String senha;

    public LoginRequest(String username, String senha) {


        this.email = username;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

