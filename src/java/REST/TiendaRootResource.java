/*
 * @author ricmart
 * @author migbay
 */
package REST;

import dominio.Configuracionpc;
import dominio.Empleado;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import persistencia.ConfiguracionpcFacadeLocal;
import persistencia.EmpleadoFacadeLocal;


@Path("TiendaRoot")
public class TiendaRootResource implements ContainerResponseFilter {

    // Definimos las fachadas de las interfaces a las que hemos accedido mediante Lookups al final de este archivo
    ConfiguracionpcFacadeLocal configuracionpcFacade = lookupConfiguracionpcFacadeLocal();
    EmpleadoFacadeLocal empleadoFacade = lookupEmpleadoFacadeLocal();

    public TiendaRootResource() { }
    
    /**
     * Dejamos el path "/" de nuestra API sin utilizar, ya que nos parece más consistente
    **/
    @GET
    @Produces("application/json")
    public String getJson() {
        throw new UnsupportedOperationException();
    }

    /**
     * Se ha cambiado la última línea de este método para aceptar la cabecera "Authorization" que se usa para almacenar la contraseña del empleado
     * @param requestContext
     * @param response 
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "*");
    }

    /**
     * Método que verifica el login de un empleado
     * @param nif NIF/CIF del empleado de la empresa (Recibido desde la url)
     * @param password Contraseña del empleado (Se recoge mediante el header "Authorization")
     * @return HTTP Status OK (200) en el caso de que encuentre al usuario
     */
    @GET
    @Path("/login/{nif}")
    public Response login(@PathParam("nif") String nif, @HeaderParam("Authorization") String password) {

        ResponseBuilder res = Response.status(Response.Status.ACCEPTED);

        Empleado e = empleadoFacade.find(nif);

        if (e == null) {
            System.out.println("Empleado no encontrado");
            return res.status(Response.Status.NOT_FOUND).build();
        }

        if (!password.equals(e.getUsuario().getPassword())) {
            System.out.println("Password no encontrado");
            return res.status(Response.Status.UNAUTHORIZED).build();
        }

        return res.status(Response.Status.OK).build();
    }

    /**
     * Método que devuelve un objeto JSON con los datos relevantes de un empleado para esta práctica
     * @param nif NIF/CIF del empleado que se quiere recoger
     * @return Devuelve una Response con JSON como body con los datos del empleado ya tratados y HTTP Status OK (200) o NOT_FOUND(400) si no se encuentra
     */
    @GET
    @Path("/empleados/{nif}")
    @Produces("application/json")
    public Response getEmpleado(@PathParam("nif") String nif) {
        
        ResponseBuilder res = Response.status(Response.Status.ACCEPTED);

        Empleado e = empleadoFacade.find(nif);
        
        if (e == null) {
            System.out.println("Empleado no encontrado");
            return res.status(Response.Status.NOT_FOUND).build();
        }
        
        // Creo un objeto modificado con los datos que se necesitan del empleado para esta practica
        // con el objetivo de facilitar el tratamiento de la informacion en el cliente
        
        // Devolvemos todos los datos en String para que sea más fácil tratarlos ya que incluso las fechas se pueden 
        // definir en JavaScript utilizando Strings.
        JsonObject empleadoMod = Json.createObjectBuilder()
            .add("nifcif", e.getNifcif())
            .add("rol", e.getRol().getNombrerol())
            .add("fechacontratacion", e.getFechacontratacion().toString())
            .add("pais", e.getUsuario().getPais())
            .build();
        
        // Definimos el cuerpo de la respuesta como el JsonObject Empleado Modificado que hemos creado
        res.entity(empleadoMod);
        res.status(Response.Status.OK);
        return res.build();
    }
    
    /**
     * Devuelve la lista con todas las configuraciones de la base de datos
     * @return Response con la lista de configuraciones como Array de JSON en el cuerpo de la respuesta y HTTP Status OK (200)
     */
    @GET
    @Path("/configuraciones")
    @Produces("application/json")
    public Response getListaConf() {

        ResponseBuilder res = Response.status(Response.Status.ACCEPTED);

        List<Configuracionpc> listaConf = configuracionpcFacade.findAll();
        if (listaConf == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Configuracionpc[] arrayConf = new Configuracionpc[listaConf.size()];
        for (int i = 0; i < arrayConf.length; i++) {
            arrayConf[i] = listaConf.get(i);
        }
        
        res.status(Response.Status.OK);
        res.entity(arrayConf);
        return res.build();

    }

    /**
     * Devuelve una configuración mediante su identificador
     * @param idConf Identificador de la configuración solicitada
     * @return Devuelve la configuración en el cuerpo de la respuesta y HTTP Status OK (200) o NOT_FOUND(401)
     */
    @GET
    @Path("/configuraciones/{id}")
    @Produces("application/json")
    public Response getConf(@PathParam("id") int idConf) {

        ResponseBuilder res = Response.status(Response.Status.ACCEPTED);

        Configuracionpc c = configuracionpcFacade.find(idConf);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        res.status(Response.Status.OK);
        res.entity(c);
        return res.build();

    }

    /**
     * Único método POST de la API, que añade una configuración nueva a la base de datos
     * @param configuracionpc JsonObject que contiene los datos necesarios para crear una configuración
     * @return HTTP POST Status OK (201) o INTERNAL_SERVER_ERROR(500)
     */
    @POST
    // Hemos utilizado la ruta /configuraciones por consistencia, aunque podría haberse mantenido la ruta "/"
    @Path("/configuraciones")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addConfiguracion(JsonObject configuracionpc) {

        ResponseBuilder respuesta = Response.noContent();

        try {

            Configuracionpc nuevaConf = new Configuracionpc();
            nuevaConf.setIdconfiguracion(getIdAddConf());
            nuevaConf.setTipocpu(configuracionpc.getString("tipocpu"));
            nuevaConf.setVelocidadcpu(configuracionpc.getInt("velocidadcpu"));
            nuevaConf.setVelocidadtarjetagrafica(configuracionpc.getInt("velocidadtarjetagrafica"));
            nuevaConf.setMemoriatarjetagrafica(configuracionpc.getInt("memoriatarjetagrafica"));
            nuevaConf.setCapacidadram(configuracionpc.getInt("capacidadram"));
            nuevaConf.setCapacidaddd(configuracionpc.getInt("capacidaddd"));
            nuevaConf.setPrecio(configuracionpc.getJsonNumber("precio").bigDecimalValue().floatValue());
            
            configuracionpcFacade.create(nuevaConf);

            respuesta.status(Response.Status.CREATED);

            return respuesta.build();

        } catch (Exception e) {
            respuesta.status(Response.Status.INTERNAL_SERVER_ERROR);
            return respuesta.build();
        }
    }

    /**
     * Actualiza una configuración (Sólo memoria y velocidad de la tarjeta gráfica)
     * @param idConf Identificador de la configuración a editar
     * @param confMod JsonObject que contiene los elementos de la configuración, de los cuales sólo se editarán la memoria y velocidad de la tarjeta gráfica en la base de datos
     * @return HTTP Status OK (200), INTERNAL_SERVER_ERROR (500) o NOT_FOUND (401)
     */
    @Path("/configuraciones/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editConfiguracion(@PathParam("id") int idConf, JsonObject confMod) {

        ResponseBuilder res = Response.noContent();

        try {
            Configuracionpc c = configuracionpcFacade.find(idConf);

            if (c == null) {
                return res.status(Response.Status.NOT_FOUND).build();
            }

            int newVelTarGraf = confMod.getInt("velocidadtarjetagrafica");
            int newMemTarGraf = confMod.getInt("memoriatarjetagrafica");

            if (newVelTarGraf != 0) {
                c.setVelocidadtarjetagrafica(newVelTarGraf);
            }

            if (newMemTarGraf != 0) {
                c.setMemoriatarjetagrafica(newMemTarGraf);
            }

            configuracionpcFacade.edit(c);

            res.status(Response.Status.OK);
            return res.build();

        } catch (Exception e) {
            res.status(Response.Status.INTERNAL_SERVER_ERROR);
            return res.build();
        }
    }

    /**
     * Borra una configuración de la base de datos
     * @param idConf Identificador de la cofiguración que se desea borrar
     * @return HTTP Status OK (200), INTERNAL_SERVER_ERROR (500) o NOT_FOUND (401)
     */
    @Path("/configuraciones/delete/{id}")
    @DELETE
    public Response delConfiguracion(@PathParam("id") int idConf) {

        ResponseBuilder res = Response.noContent();

        try {
            Configuracionpc c = (Configuracionpc) configuracionpcFacade.find(idConf);
            
            if(c == null){
                return res.status(Response.Status.NOT_FOUND).build();
            }
          
            configuracionpcFacade.remove(c);

            res.status(Response.Status.OK);
            return res.build();

        } catch (Exception e) {
            res.status(Response.Status.INTERNAL_SERVER_ERROR);
            return res.build();
        }
    }

    // ------------------------------------- AUX FUNCTIONS ---------------------------------------------
    
    /**
     * Este método genera un nuevo identificador para las configuraciones que se añaden a la base de datos
     * @return Nuevo identificador para la configuración que se desea añadir
     */
    private Integer getIdAddConf() {
        int newIdConf;
        List<Configuracionpc> listaConf = configuracionpcFacade.findAll();
        newIdConf = listaConf.get(listaConf.size() - 1).getIdconfiguracion() + 1;
        return newIdConf;
    }

    // ------------------------------------- LOOKUP FUNCTIONS --------------------------------------------- 
    
    
    private ConfiguracionpcFacadeLocal lookupConfiguracionpcFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ConfiguracionpcFacadeLocal) c.lookup("java:global/TiendaPC_Practica2/ConfiguracionpcFacade!persistencia.ConfiguracionpcFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private EmpleadoFacadeLocal lookupEmpleadoFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (EmpleadoFacadeLocal) c.lookup("java:global/TiendaPC_Practica2/EmpleadoFacade!persistencia.EmpleadoFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
