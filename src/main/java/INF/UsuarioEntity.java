package INF;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "USUARIO", schema = "PUBLIC", catalog = "PRACTICA4")
public class UsuarioEntity {
    private int id;
    public String username;
    public String password;
    public Boolean administrador;
    public Boolean autor;
    public String nombre;
    private Collection<ArticuloEntity> articulosById;
    private Collection<ComentarioEntity> comentariosById;

    public UsuarioEntity(int i, String admin, String s, boolean b, boolean b1, String cristian) {
    }

    public UsuarioEntity() {

    }

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "USERNAME", nullable = true, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "ADMINISTRADOR", nullable = true)
    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    @Basic
    @Column(name = "AUTOR", nullable = true)
    public Boolean getAutor() {
        return autor;
    }

    public void setAutor(Boolean autor) {
        this.autor = autor;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = true, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(administrador, that.administrador) &&
                Objects.equals(autor, that.autor) &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, administrador, autor, nombre);
    }

    @OneToMany(mappedBy = "usuarioByUsuarioId")
    public Collection<ArticuloEntity> getArticulosById() {
        return articulosById;
    }

    public void setArticulosById(Collection<ArticuloEntity> articulosById) {
        this.articulosById = articulosById;
    }

    @OneToMany(mappedBy = "usuarioByUsuarioId")
    public Collection<ComentarioEntity> getComentariosById() {
        return comentariosById;
    }

    public void setComentariosById(Collection<ComentarioEntity> comentariosById) {
        this.comentariosById = comentariosById;
    }
}
