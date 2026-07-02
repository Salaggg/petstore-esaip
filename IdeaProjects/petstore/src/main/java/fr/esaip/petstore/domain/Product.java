package fr.esaip.petstore.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProdType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToMany(mappedBy = "products")
    private List<PetStore> petStores = new ArrayList<>();

    public Product() {
    }

    public Product(String code, String label, ProdType type, BigDecimal price) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public ProdType getType() { return type; }
    public void setType(ProdType type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public List<PetStore> getPetStores() { return petStores; }
    public void setPetStores(List<PetStore> petStores) {
        List<PetStore> currentPetStores = new ArrayList<>(this.petStores);
        currentPetStores.forEach(this::removePetStore);

        if (petStores != null) {
            petStores.forEach(this::addPetStore);
        }
    }

    public void addPetStore(PetStore petStore) {
        if (petStore == null || petStores.contains(petStore)) {
            return;
        }

        petStores.add(petStore);

        if (!petStore.getProducts().contains(this)) {
            petStore.getProducts().add(this);
        }
    }

    public void removePetStore(PetStore petStore) {
        if (petStore == null || !petStores.remove(petStore)) {
            return;
        }

        petStore.getProducts().remove(this);
    }
}