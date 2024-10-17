package project.interdisciplinary.inclusesapp.data.models;

public class LoginResponse {
    private String token;
    private String type;

    private Perfil perfil;;

    public LoginResponse(String token, String type, Perfil perfil) {
        this.token = token;
        this.type = type;
        this.perfil = perfil;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
