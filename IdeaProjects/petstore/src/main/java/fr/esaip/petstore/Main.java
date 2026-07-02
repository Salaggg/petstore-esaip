package fr.esaip.petstore;

import fr.esaip.petstore.domain.Address;
import fr.esaip.petstore.domain.Animal;
import fr.esaip.petstore.domain.Cat;
import fr.esaip.petstore.domain.Fish;
import fr.esaip.petstore.domain.FishLivEnv;
import fr.esaip.petstore.domain.PetStore;
import fr.esaip.petstore.domain.ProdType;
import fr.esaip.petstore.domain.Product;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("petstorePU", buildPersistenceProperties());
        EntityManager em = emf.createEntityManager();

        try {
            seedDatabaseIfEmpty(em);
            printAnimalsByStore(em, "Cat Paradise");
        } finally {
            em.close();
            emf.close();
        }
    }

    static Map<String, Object> buildPersistenceProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", resolveConfig("petstore.db.url", "PETSTORE_DB_URL", "jdbc:mysql://localhost:3306/petstore"));
        properties.put("javax.persistence.jdbc.user", resolveConfig("petstore.db.user", "PETSTORE_DB_USER", "root"));
        properties.put("javax.persistence.jdbc.password", resolveConfig("petstore.db.password", "PETSTORE_DB_PASSWORD", ""));
        properties.put("javax.persistence.jdbc.driver", resolveConfig("petstore.db.driver", "PETSTORE_DB_DRIVER", "com.mysql.cj.jdbc.Driver"));
        properties.put("hibernate.hbm2ddl.auto", resolveConfig("petstore.ddl", "PETSTORE_DDL_AUTO", "update"));
        properties.put("hibernate.show_sql", resolveConfig("petstore.showSql", "PETSTORE_SHOW_SQL", "false"));
        properties.put("hibernate.format_sql", "true");
        return properties;
    }

    private static void seedDatabaseIfEmpty(EntityManager em) {
        Long petStoreCount = em.createQuery("SELECT COUNT(ps) FROM PetStore ps", Long.class).getSingleResult();
        if (petStoreCount > 0) {
            return;
        }

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Product p1 = new Product("P01", "Croquettes Premium", ProdType.FOOD, new BigDecimal("45.99"));
            Product p2 = new Product("P02", "Arbre a chat", ProdType.ACCESSORY, new BigDecimal("89.50"));
            Product p3 = new Product("P03", "Filtre Aquarium", ProdType.CLEANING, new BigDecimal("15.00"));

            PetStore store1 = new PetStore("Cat Paradise", "Alice");
            store1.setAddress(new Address("12", "Rue des Chats", "75001", "Paris"));
            store1.addProduct(p1);
            store1.addProduct(p2);

            PetStore store2 = new PetStore("Aqua Blue", "Bob");
            store2.setAddress(new Address("45", "Avenue de l'Ocean", "13000", "Marseille"));
            store2.addProduct(p3);

            PetStore store3 = new PetStore("Tout Pour Tous", "Charlie");
            store3.setAddress(new Address("8", "Boulevard des Croquettes", "69000", "Lyon"));
            store3.addProduct(p1);

            Cat c1 = new Cat("CHIP-999");
            c1.setBirth(new Date());
            c1.setCouleur("Roux");
            store1.addAnimal(c1);

            Cat c2 = new Cat("CHIP-888");
            c2.setBirth(new Date());
            c2.setCouleur("Noir");
            store1.addAnimal(c2);

            Fish f1 = new Fish(FishLivEnv.SEA_WATER);
            f1.setBirth(new Date());
            f1.setCouleur("Bleu et Jaune");
            store2.addAnimal(f1);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(store1);
            em.persist(store2);
            em.persist(store3);

            transaction.commit();
            System.out.println("Donnees inserees avec succes !");
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        }
    }

    private static void printAnimalsByStore(EntityManager em, String storeName) {
        TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a WHERE a.petStore.name = :name", Animal.class);
        query.setParameter("name", storeName);

        List<Animal> animals = query.getResultList();
        System.out.println();
        System.out.println("--- Recherche des animaux du magasin '" + storeName + "' ---");

        for (Animal animal : animals) {
            System.out.println("Animal trouve : ID=" + animal.getId() + ", Couleur=" + animal.getCouleur());
        }
    }

    private static String resolveConfig(String systemPropertyName, String environmentName, String defaultValue) {
        String systemPropertyValue = System.getProperty(systemPropertyName);
        if (systemPropertyValue != null && !systemPropertyValue.isBlank()) {
            return systemPropertyValue;
        }

        String environmentValue = System.getenv(environmentName);
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }

        return defaultValue;
    }
}