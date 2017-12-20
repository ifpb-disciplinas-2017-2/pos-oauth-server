package br.edu.ifpb;

import br.edu.ifpb.domain.Autor;
import br.edu.ifpb.infra.Autores;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 20/12/2017, 13:36:31
 */
// http://localhost:8080/rest-server/api/autor
@Path("autor")
@Stateless
public class ResourceAutor {

    // GET - Recuperar
    // POST - criar
    // DELETE - remover
    // PUT - atualizar
    @Inject
    private Autores autores;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response autor() {

        GenericEntity<List<Autor>> entity
                = new GenericEntity<List<Autor>>(autores.todos()) {
        };

        return Response.ok()
                .entity(entity)
                .build();
    }

    // GET/{letra} http://localhost:8080/rest-server/api/autor/C
    @Path("{letra}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response autorPorNome(@PathParam("letra") String letra) {

        List<Autor> lista = autores.todos().stream()
                .filter(a -> a.getNome().toLowerCase().startsWith(letra.toLowerCase()))
                .collect(Collectors.toList());

        GenericEntity<List<Autor>> entity
                = new GenericEntity<List<Autor>>(lista) {
        };

        return Response.ok()
                .entity(entity)
                .build();
    }

    // POST  http://localhost:8080/rest-server/api/autor
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response novo(@Context UriInfo uriInfo, Autor autor) {
//    public Response novo(@FormParam("nome") String nome, @FormParam("cpf") String cpf) {
//        Autor autor = new Autor(nome, cpf);

        this.autores.novo(autor);
        URI location = uriInfo.getRequestUriBuilder() // ../api/autor
                                .path(autor.getNome())      // ../api/autor/Madruga
                                .build();
        return Response
                .created(location)// 201
                .entity(autor)
                .build();
    }

}
