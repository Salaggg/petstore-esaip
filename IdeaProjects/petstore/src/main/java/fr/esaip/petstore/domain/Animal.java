package fr.esaip.petstore.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date birth;

    private String couleur;

    @ManyToOne
    private PetStore petStore;

     //construteur
    public Animal() {
    }

    public Animal(Date birth, String couleur) {
        this.birth = birth;
        this.couleur = couleur;
    }

    // Getters Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getBirth() { return birth; }
    public void setBirth(Date birth) { this.birth = birth; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public PetStore getPetStore() { return petStore; }
    public void setPetStore(PetStore petStore) { this.petStore = petStore; }
}