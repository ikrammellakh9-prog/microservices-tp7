package ma.ikram.bankservice;

import jakarta.ws.rs.core.Response;
import ma.ikram.bankservice.entities.CompteBancaire;
import ma.ikram.bankservice.entities.TypeCompteBancaire;
import ma.ikram.bankservice.repositories.CompteBancaireRepository;
import ma.ikram.bankservice.rest.CompteRestJerseyResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompteRestJerseyResourceTest {

    @Mock
    private CompteBancaireRepository repository;

    @InjectMocks
    private CompteRestJerseyResource resource;

    private CompteBancaire c1;
    private CompteBancaire c2;

    @BeforeEach
    void setUp() {
        c1 = new CompteBancaire(1L, 12000.0, "MAD", new Date(), TypeCompteBancaire.COURANT);
        c2 = new CompteBancaire(2L, 65000.0, "EUR", new Date(), TypeCompteBancaire.EPARGNE);
    }

    @Test
    @DisplayName("Test 1: Récupération de tous les comptes bancaires (GET /banque-api/comptes)")
    void testGetComptes() {
        when(repository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CompteBancaire> list = resource.getComptes();

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("MAD", list.get(0).getDevise());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 2: Récupération d'un compte par ID (GET /banque-api/comptes/1)")
    void testGetCompteById() {
        when(repository.findById(1L)).thenReturn(Optional.of(c1));

        Response resp = resource.getCompte(1L);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(c1, resp.getEntity());
    }

    @Test
    @DisplayName("Test 3: Compte inexistant retourne 404 (GET /banque-api/comptes/99)")
    void testGetCompteNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Response resp = resource.getCompte(99L);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    @DisplayName("Test 4: Création d'un nouveau compte (POST /banque-api/comptes)")
    void testAddCompte() {
        CompteBancaire newAcc = new CompteBancaire(null, 5000.0, "USD", new Date(), TypeCompteBancaire.PROFESSIONNEL);
        CompteBancaire savedAcc = new CompteBancaire(3L, 5000.0, "USD", newAcc.getDateCreation(), TypeCompteBancaire.PROFESSIONNEL);
        when(repository.save(any(CompteBancaire.class))).thenReturn(savedAcc);

        Response resp = resource.addCompte(newAcc);

        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertEquals(savedAcc, resp.getEntity());
    }

    @Test
    @DisplayName("Test 5: Mise à jour compte (PUT /banque-api/comptes/1)")
    void testUpdateCompte() {
        CompteBancaire updateData = new CompteBancaire(null, 18000.0, "MAD", new Date(), TypeCompteBancaire.COURANT);
        when(repository.findById(1L)).thenReturn(Optional.of(c1));
        when(repository.save(any(CompteBancaire.class))).thenReturn(c1);

        Response resp = resource.updateCompte(1L, updateData);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(18000.0, c1.getSolde());
    }

    @Test
    @DisplayName("Test 6: Suppression compte (DELETE /banque-api/comptes/1)")
    void testDeleteCompte() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        Response resp = resource.deleteCompte(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
    }
}
