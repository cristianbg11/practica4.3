package INF;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "ARTICULO", schema = "PUBLIC", catalog = "PRACTICA4")
public class ArticuloEntity {
    public long id;
    public String titulo;
    public String cuerpo;
    public Integer usuarioId;
    public Date fecha;
    public UsuarioEntity usuarioByUsuarioId;
    private Collection<ComentarioEntity> comentariosById;
    private Collection<EtiquetaEntity> etiquetasById;
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
    @Column(name = "TITULO", nullable = true, length = 100)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Basic
    @Column(name = "CUERPO", nullable = true)
    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    @Basic
    @Column(name = "FECHA", nullable = true)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        ArticuloEntity that = (ArticuloEntity) o;
        return id == that.id &&
                Objects.equals(titulo, that.titulo) &&
                Objects.equals(cuerpo, that.cuerpo) &&
                Objects.equals(usuarioId, that.usuarioId) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(me_gusta, that.me_gusta) &&
                Objects.equals(dislike, that.dislike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, cuerpo, usuarioId, fecha);
    }

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    public UsuarioEntity getUsuarioByUsuarioId() {
        return usuarioByUsuarioId;
    }

    public void setUsuarioByUsuarioId(UsuarioEntity usuarioByUsuarioId) {
        this.usuarioByUsuarioId = usuarioByUsuarioId;
    }

    @OneToMany(mappedBy = "articuloByArticuloId", cascade = CascadeType.ALL)
    public Collection<ComentarioEntity> getComentariosById() {
        return comentariosById;
    }

    public void setComentariosById(Collection<ComentarioEntity> comentariosById) {
        this.comentariosById = comentariosById;
    }

    @OneToMany(mappedBy = "articuloByArticuloId", cascade = CascadeType.ALL)
    public Collection<EtiquetaEntity> getEtiquetasById() {
        return etiquetasById;
    }

    public void setEtiquetasById(Collection<EtiquetaEntity> etiquetasById) {
        this.etiquetasById = etiquetasById;
    }
}
