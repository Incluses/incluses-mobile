package project.interdisciplinary.inclusesapp.data.models;

import java.io.Serializable;

public class Error implements Serializable {
    private int id = 0;
    private String error;

    public Error( String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
