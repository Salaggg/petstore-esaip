package fr.esaip.petstore;

import fr.esaip.petstore.domain.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Connexio BDD
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("petstorePU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            // 3 Adresses
            Address a1 = new Address("12", "Rue des Chats", "75001", "Paris");
            Address a2 = new Address("45", "Avenue de l'Océan", "13000", "Marseille");
            Address a3 = new Address("8", "Boulevard des Croquettes", "69000", "Lyon");

            //3 Produits
            Product p1 = new Product("P01", "Croquettes Premium", ProdType.FOOD, 45.99);
            Product p2 = new Product("P02", "Arbre à chat", ProdType.ACCESSORY, 89.50);
            Product p3 = new Product("P03", "Filtre Aquarium", ProdType.CLEANING, 15.00);

            // 3 Animaleries (PetStores)
            PetStore store1 = new PetStore("Cat Paradise", "Alice");
            store1.setAddress(a1);
            store1.getProducts().add(p1);
            store1.getProducts().add(p2);

            PetStore store2 = new PetStore("Aqua Blue", "Bob");
            store2.setAddress(a2);
            store2.getProducts().add(p3);

            PetStore store3 = new PetStore("Tout Pour Tous", "Charlie");
            store3.setAddress(a3);
            store3.getProducts().add(p1);

            // 4. Création de 3 Animaux
            Cat c1 = new Cat("CHIP-999");
            c1.setBirth(new Date());
            c1.setCouleur("Roux");
            c1.setPetStore(store1);

            Cat c2 = new Cat("CHIP-888");
            c2.setBirth(new Date());
            c2.setCouleur("Noir");
            c2.setPetStore(store1); // Appartient aussi à Cat Paradise

            Fish f1 = new Fish(FishLivEnv.SEA_WATER);
            f1.setBirth(new Date());
            f1.setCouleur("Bleu et Jaune");
            f1.setPetStore(store2);

            // 5. Sauvegarde en BDD (Persistance)
            em.persist(a1); em.persist(a2); em.persist(a3);
            em.persist(p1); em.persist(p2); em.persist(p3);
            em.persist(store1); em.persist(store2); em.persist(store3);
            em.persist(c1); em.persist(c2); em.persist(f1);

            em.getTransaction().commit();
            System.out.println("Données insérées avec succès !");

            // Extraire tous les animaux de l'animalerie "Cat Paradise"
            System.out.println("\n--- Recherche des animaux du magasin 'Cat Paradise' ---");
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a WHERE a.petStore.name = :name", Animal.class);
            query.setParameter("name", "Cat Paradise");

            List<Animal> animaux = query.getResultList();
            for (Animal animal : animaux) {
                System.out.println("Animal trouvé : ID=" + animal.getId() + ", Couleur=" + animal.getCouleur());
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}