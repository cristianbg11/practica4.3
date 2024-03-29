package INF;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "COMENTARIO", schema = "PUBLIC", catalog = "PRACTICA4")
public class ComentarioEntity {
    private long id;
    public String comentario;
    private Integer usuarioId;
    private Integer articuloId;
    public UsuarioEntity usuarioByUsuarioId;
    public ArticuloEntity articuloByArticuloId;
    public Integer me_gusta;
    public Integer dislike;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "COMENTARIO", nullable = true)
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Basic
    @Column(name = "ME_GUSTA", nullable = true)
    public Integer getMe_gusta() {
        return me_gusta;
    }

    public void setMe_gusta(Integer me_gusta) {
        this.me_gusta = me_gusta;
    }

    @Basic
    @Column(name = "DISLIKE", nullable = true)
    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComentarioEntity that = (ComentarioEntity) o;
        return id == that.id &&
                Objects.equals(comentario, that.comentario) &&
                Objects.equals(usuarioId, that.usuarioId) &&
                Objects.equals(articuloId, that.articuloId)&&
                Objects.equals(me_gusta, that.me_gusta) &&
                Objects.equals(dislike, that.dislike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comentario, usuarioId, articuloId);
    }

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    public UsuarioEntity getUsuarioByUsuarioId() {
        return usuarioByUsuarioId;
    }

    public void setUsuarioByUsuarioId(UsuarioEntity usuarioByUsuarioId) {
        this.usuarioByUsuarioId = usuarioByUsuarioId;
    }

    @ManyToOne
    @JoinColumn(name = "ARTICULO_ID", referencedColumnName = "ID")
    public ArticuloEntity getArticuloByArticuloId() {
        return articuloByArticuloId;
    }

    public void setArticuloByArticuloId(ArticuloEntity articuloByArticuloId) {
        this.articuloByArticuloId = articuloByArticuloId;
    }
}
