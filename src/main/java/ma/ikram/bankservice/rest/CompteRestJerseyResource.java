package ma.ikram.bankservice.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.ikram.bankservice.entities.CompteBancaire;
import ma.ikram.bankservice.repositories.CompteBancaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/banque-api")
public class CompteRestJerseyResource {

    @Autowired
    private CompteBancaireRepository compteRepository;

    @Path("/comptes")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<CompteBancaire> getComptes() {
        return compteRepository.findAll();
    }

    @Path("/comptes/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCompte(@PathParam("id") Long id) {
        return compteRepository.findById(id)
                .map(compte -> Response.ok(compte).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Path("/comptes")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCompte(CompteBancaire compte) {
        if (compte.getDevise() == null || compte.getDevise().isEmpty()) {
            compte.setDevise("MAD");
        }
        CompteBancaire saved = compteRepository.save(compte);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @Path("/comptes/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateCompte(@PathParam("id") Long id, CompteBancaire compte) {
        return compteRepository.findById(id)
                .map(existingCompte -> {
                    existingCompte.setSolde(compte.getSolde());
                    existingCompte.setDateCreation(compte.getDateCreation());
                    existingCompte.setTypeCompte(compte.getTypeCompte());
                    if (compte.getDevise() != null) {
                        existingCompte.setDevise(compte.getDevise());
                    }
                    CompteBancaire updated = compteRepository.save(existingCompte);
                    return Response.ok(updated).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @Path("/comptes/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteCompte(@PathParam("id") Long id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
