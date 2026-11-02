package ma.ikram.bankservice;

import ma.ikram.bankservice.entities.CompteBancaire;
import ma.ikram.bankservice.entities.TypeCompteBancaire;
import ma.ikram.bankservice.repositories.CompteBancaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class BanqueJerseyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanqueJerseyApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CompteBancaireRepository repository) {
        return args -> {
            repository.save(new CompteBancaire(null, 15400.0, "MAD", new Date(), TypeCompteBancaire.COURANT));
            repository.save(new CompteBancaire(null, 45200.75, "EUR", new Date(), TypeCompteBancaire.EPARGNE));
            repository.save(new CompteBancaire(null, 98000.0, "USD", new Date(), TypeCompteBancaire.PROFESSIONNEL));

            System.out.println("=== Initialisation BankService Ikram (JAX-RS / Jersey) ===");
            repository.findAll().forEach(compte -> {
                System.out.println("[Compte Initialise] ID=" + compte.getId() + " | Solde=" + compte.getSolde() + " " + compte.getDevise() + " | Type=" + compte.getTypeCompte());
            });
        };
    }
}
