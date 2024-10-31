package project.interdisciplinary.inclusesapp.data.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Postagem {

    private UUID id;

    private UUID arquivoId;
    private UUID perfilId;
    private String titulo;
    private String legenda;
    private List<Like> likes;
    private List<Comentario> comentarios;
    public Postagem() {
        this.id = UUID.randomUUID(); // Gerar como String
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getArquivoId() {
        return arquivoId;
    }

    public void setArquivoId(UUID arquivoId) {
        this.arquivoId = arquivoId;
    }

    public UUID getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(UUID perfilId) {
        this.perfilId = perfilId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }


    public static class Like {
        private UUID perfilId;
        private Date dataLike;

        // Getters e Setters

        public UUID getPerfilId() {
            return perfilId;
        }

        public Date getDataLike() {
            return dataLike;
        }

        public void setDataLike(Date dataLike) {
            this.dataLike = dataLike;
        }

        public void setPerfilId(UUID perfilId) {
            this.perfilId = perfilId;
        }
    }

    public static class Comentario {
        private UUID perfilId;
        private String comentario;

        // Getters e Setters

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }

        public void setPerfilId(UUID perfilId) {
            this.perfilId = perfilId;
        }

        public UUID getPerfilId() {
            return perfilId;
        }

        public String getComentario() {
            return comentario;
        }
    }
}
