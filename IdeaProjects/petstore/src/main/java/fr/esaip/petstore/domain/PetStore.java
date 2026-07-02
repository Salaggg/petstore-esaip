package fr.esaip.petstore.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PetStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String managerName;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "petstore_product",
            joinColumns = @JoinColumn(name = "petstore_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "petStore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();

    public PetStore() {
    }

    public PetStore(String name, String managerName) {
        this.name = name;
        this.managerName = managerName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) {
        List<Product> currentProducts = new ArrayList<>(this.products);
        currentProducts.forEach(this::removeProduct);

        if (products != null) {
            products.forEach(this::addProduct);
        }
    }

    public List<Animal> getAnimals() { return animals; }
    public void setAnimals(List<Animal> animals) {
        List<Animal> currentAnimals = new ArrayList<>(this.animals);
        currentAnimals.forEach(this::removeAnimal);

        if (animals != null) {
            animals.forEach(this::addAnimal);
        }
    }

    public void addProduct(Product product) {
        if (product == null || products.contains(product)) {
            return;
        }

        products.add(product);

        if (!product.getPetStores().contains(this)) {
            product.getPetStores().add(this);
        }
    }

    public void removeProduct(Product product) {
        if (product == null || !products.remove(product)) {
            return;
        }

        product.getPetStores().remove(this);
    }

    public void addAnimal(Animal animal) {
        if (animal != null) {
            animal.setPetStore(this);
        }
    }

    public void removeAnimal(Animal animal) {
        if (animal != null && animal.getPetStore() == this) {
            animal.setPetStore(null);
        }
    }
}