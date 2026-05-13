package fr.esaip.petstore.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PetStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String managerName;

    // --- RELATIONS ---

    // Relation 1 à 1 avec Address
    @OneToOne
    private Address address;

    // Relation Plusieurs à Plusieurs avec Product
    @ManyToMany
    @JoinTable(
            name = "petstore_product",
            joinColumns = @JoinColumn(name = "petstore_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    // Relation 1 à Plusieurs avec Animal
    @OneToMany(mappedBy = "petStore")
    private List<Animal> animals = new ArrayList<>();

    // --- Constructeurs ---
    public PetStore() {
    }

    public PetStore(String name, String managerName) {
        this.name = name;
        this.managerName = managerName;
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public List<Animal> getAnimals() { return animals; }
    public void setAnimals(List<Animal> animals) { this.animals = animals; }
}