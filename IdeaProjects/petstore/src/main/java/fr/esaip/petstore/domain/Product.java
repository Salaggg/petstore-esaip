package fr.esaip.petstore.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String label;

    @Enumerated(EnumType.STRING) // Permet de stocker le texte (ex: "FOOD") en BDD au lieu d'un chiffre
    private ProdType type;

    private double price;

    // Relation ManyToMany : un produit peut être dans plusieurs PetStores
    // (Le mappedBy indique que la relation est gérée par la classe PetStore)
    @ManyToMany(mappedBy = "products")
    private List<PetStore> petStores = new ArrayList<>();

    // --- Constructeurs ---
    public Product() {
    }

    public Product(String code, String label, ProdType type, double price) {
        this.code = code;
        this.label = label;
        this.type = type;
        this.price = price;
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public ProdType getType() { return type; }
    public void setType(ProdType type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public List<PetStore> getPetStores() { return petStores; }
    public void setPetStores(List<PetStore> petStores) { this.petStores = petStores; }
}