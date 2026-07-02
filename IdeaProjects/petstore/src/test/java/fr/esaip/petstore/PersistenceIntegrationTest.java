package fr.esaip.petstore;

import fr.esaip.petstore.domain.Address;
import fr.esaip.petstore.domain.Animal;
import fr.esaip.petstore.domain.Cat;
import fr.esaip.petstore.domain.PetStore;
import fr.esaip.petstore.domain.ProdType;
import fr.esaip.petstore.domain.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceIntegrationTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUpEntityManagerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:petstore;MODE=MySQL;DB_CLOSE_DELAY=-1");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.password", "");
        properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        emf = Persistence.createEntityManagerFactory("petstorePU", properties);
    }

    @AfterAll
    static void tearDownEntityManagerFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void persistsAndQueriesAnimalsByStoreName() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Product product = new Product("P01", "Croquettes Premium", ProdType.FOOD, new BigDecimal("45.99"));
            PetStore petStore = new PetStore("Cat Paradise", "Alice");
            petStore.setAddress(new Address("12", "Rue des Chats", "75001", "Paris"));
            petStore.addProduct(product);

            Cat cat = new Cat("CHIP-999");
            cat.setBirth(new Date());
            cat.setCouleur("Roux");
            petStore.addAnimal(cat);

            em.persist(product);
            em.persist(petStore);
            transaction.commit();

            List<Animal> animals = em.createQuery(
                    "SELECT a FROM Animal a WHERE a.petStore.name = :name",
                    Animal.class
            ).setParameter("name", "Cat Paradise").getResultList();

            assertEquals(1, animals.size());
            assertEquals("Roux", animals.get(0).getCouleur());
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            em.close();
        }
    }
}