package INF;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ETIQUETA", schema = "PUBLIC", catalog = "PRACTICA4")
public class EtiquetaEntity {
    private long id;
    public String etiqueta;
    private Integer articuloId;
    public ArticuloEntity articuloByArticuloId;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ETIQUETA", nullable = true, length = 255)
    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtiquetaEntity that = (EtiquetaEntity) o;
        return id == that.id &&
                Objects.equals(etiqueta, that.etiqueta) &&
                Objects.equals(articuloId, that.articuloId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, etiqueta, articuloId);
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
