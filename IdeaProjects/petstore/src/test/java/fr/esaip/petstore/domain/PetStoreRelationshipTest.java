package fr.esaip.petstore.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetStoreRelationshipTest {

    @Test
    void addProductKeepsBothSidesInSync() {
        PetStore petStore = new PetStore("Cat Paradise", "Alice");
        Product product = new Product("P01", "Croquettes Premium", ProdType.FOOD, new BigDecimal("45.99"));

        petStore.addProduct(product);

        assertTrue(petStore.getProducts().contains(product));
        assertTrue(product.getPetStores().contains(petStore));

        petStore.removeProduct(product);

        assertFalse(petStore.getProducts().contains(product));
        assertFalse(product.getPetStores().contains(petStore));
    }

    @Test
    void setPetStoreMovesAnimalBetweenStores() {
        PetStore firstStore = new PetStore("Cat Paradise", "Alice");
        PetStore secondStore = new PetStore("Aqua Blue", "Bob");
        Cat cat = new Cat("CHIP-999");

        firstStore.addAnimal(cat);

        assertSame(firstStore, cat.getPetStore());
        assertTrue(firstStore.getAnimals().contains(cat));

        cat.setPetStore(secondStore);

        assertSame(secondStore, cat.getPetStore());
        assertFalse(firstStore.getAnimals().contains(cat));
        assertTrue(secondStore.getAnimals().contains(cat));
    }
}