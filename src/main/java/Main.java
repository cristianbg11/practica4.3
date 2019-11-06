import INF.UsuarioEntity;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session sesion = getSession();

        port(8080);
        staticFiles.location("/publico");
        EntityManager em = getSession();

        if (sesion.find(UsuarioEntity.class, 1)==null){
            em.getTransaction().begin();
            UsuarioEntity admin = new UsuarioEntity(1, "admin", "1234", true, true, "Cristian");
            em.persist(admin);
            em.getTransaction().commit();;
        }
        post("/insertar", (request, response) -> {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.username = request.queryParams("username");
            usuario.nombre = request.queryParams("nombre");
            usuario.password = request.queryParams("password");
            usuario.administrador = Boolean.parseBoolean(request.queryParams("administrador"));
            usuario.autor = Boolean.parseBoolean(request.queryParams("username"));
            em.persist(usuario);
            em.getTransaction().commit();
            response.redirect("/");
            return "Usuario Creado";
        });

        post("/crear-articulo", (request, response)-> {
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            Articulo articulo = new Articulo();
            Etiqueta etiqueta = new Etiqueta();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            articulo.titulo = request.queryParams("titulo");
            articulo.cuerpo = request.queryParams("cuerpo");
            articulo.autor = usuario;
            articulo.fecha = format.parse(request.queryParams("fecha"));
            jpa.insertArticulo(articulo);
            //List <Articulo> articulos = articulo.getAllArticles();
            //etiqueta.etiqueta = request.queryParams("etiqueta");
            String[] tags = request.queryParams("etiqueta").split(",");
            List<String> tagList = Arrays.asList(tags);
            //etiqueta.articulo.id = articulo.getLastArticle();
            //etiqueta.articulo = articulos.get(articulos.size()-1);
            etiqueta.articulo_id = jpa.getLastArticle();
            jpa.insertEtiqueta(etiqueta, tagList, tagList.size());
            response.redirect("/index");
            return "Articulo Creado";
        });

        get("/delete", (request, response)-> {
            int id_articulo = Integer.parseInt(request.queryParams("id_post"));
            jpa.deleteArticulo(id_articulo);
            response.redirect("/articulo");
            return "Articulo Borrado";
        });

        post("/update", (request, response)-> {
            Articulo articulo = new Articulo();
            Etiqueta etiqueta = new Etiqueta();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            articulo.titulo = request.queryParams("titulo");
            articulo.cuerpo = request.queryParams("cuerpo");
            articulo.fecha = format.parse(request.queryParams("fecha"));
            int id_articulo = Integer.parseInt(request.queryParams("id_post"));
            jpa.editArticulo(id_articulo, articulo);
            jpa.deleteEtiqueta(id_articulo);
            String[] tags = request.queryParams("etiqueta").split(",");
            List<String> tagList = Arrays.asList(tags);
            etiqueta.articulo_id = id_articulo;
            jpa.insertEtiqueta(etiqueta, tagList, tagList.size());
            response.redirect("/post?id_post="+id_articulo);
            return "Articulo Actualizado";
        });

        get("/", (request, response)-> {
            //response.redirect("/login.html");
            if (request.cookie("CookieUsuario") != null){
                //String id = request.cookie("CookieUsuario");
                List<Usuario> usuario = jpa.getUser(1);
                spark.Session session=request.session(true);
                session.attribute("usuario", usuario.get(0));
                response.redirect("/index");
            }
            return renderContent("publico/login.html");
        });

        get("/edita", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            int id = Integer.parseInt(request.queryParams("id_post"))-1;
            List<Articulo> articulos = jpa.getArticle(id);
            attributes.put("usuario",usuario);
            attributes.put("post",articulos.get(0));
            return new ModelAndView(attributes, "articuloedit.ftl");

        } , new FreeMarkerEngine());

        post("/sesion", (request, response)-> {
            List<Usuario> users = jpa.getAllUsers();
            String username = request.queryParams("user");
            String password = request.queryParams("pass");
            spark.Session session=request.session(true);

            for(Usuario usuario : users){
                if (usuario.username.equals(username) && usuario.password.equals(password)){
                    session.attribute("usuario", usuario);
                    if (request.queryParams("recordatorio") !=null && request.queryParams("recordatorio").equals("si") ){
                        Map<String, String> cookies=request.cookies();
                        //response.cookie("/", "CookieUsuario", String.valueOf(usuario.id), 604800, true);
                        for (String key : cookies.keySet()) {
                            if (key != null) {
                                response.removeCookie(key);
                                response.cookie("/", "CookieUsuario", cookies.get(key), 604800, false);
                            }
                        }
                    }
                    response.redirect("/index");
                }
            }
            response.redirect("/");
            return 0;
        });


        get("/index", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            List<Articulo> articulos = jpa.getLastArticles();
            attributes.put("usuario",usuario);
            attributes.put("articulos",articulos);
            return new ModelAndView(attributes, "index.ftl");

        } , new FreeMarkerEngine());

        get("/salir", (request, response)->{
            //creando cookie en para un minuto
            spark.Session session=request.session(true);
            session.invalidate();
            response.removeCookie("CookieUsuario");
            response.redirect("/");
            return "Sesion finalizada";
        });

        get("/user", (request, response)-> {
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            Map<String, Object> attributes = new HashMap<>();
            List<Usuario> users = jpa.getAllUsers();
            attributes.put("users",users);
            attributes.put("usuario",usuario);
            return new ModelAndView(attributes, "usuarios.ftl");

        } , new FreeMarkerEngine());

        get("/articulo", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            List<Articulo> articulos = jpa.getAllArticles();
            attributes.put("usuario",usuario);
            attributes.put("articulos",articulos);
            return new ModelAndView(attributes, "articulos.ftl");

        } , new FreeMarkerEngine());

        get("/post", (request, response)-> {
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            int id = Integer.parseInt(request.queryParams("id_post"))-1;
            List<Articulo> articulos = jpa.getArticle(id);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("usuario",usuario);
            attributes.put("post",articulos.get(0));
            return new ModelAndView(attributes, "post.ftl");

        } , new FreeMarkerEngine());

        get("/crear", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            if(usuario==null){
                response.redirect("/");
            } else if (usuario.administrador==false){
                response.redirect("/index");
            }
            attributes.put("usuario",usuario);
            return new ModelAndView(attributes, "crear.ftl");

        } , new FreeMarkerEngine());

        post("/comentar", (request, response) -> {
            spark.Session session=request.session(true);
            Usuario usuario = (Usuario)(session.attribute("usuario"));
            Comentario comentario = new Comentario();
            comentario.comentario = request.queryParams("comentario");
            comentario.autor = usuario;
            comentario.articulo_id = Integer.parseInt(request.queryParams("articulo_id"));
            jpa.insertComentario(comentario);
            response.redirect("/post?id_post="+(comentario.articulo_id));
            return "Comentario Creado";
        });

        /*
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }
        */
    }

    private static String renderContent(String htmlFile) throws IOException, URISyntaxException {
        URL url = Main.class.getResource(htmlFile);
        Path path = Paths.get(url.toURI());
        return new String(Files.readAllBytes(path), Charset.defaultCharset());
    }
}