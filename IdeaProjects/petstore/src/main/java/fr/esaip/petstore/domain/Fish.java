package fr.esaip.petstore.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Fish extends Animal {

    @Enumerated(EnumType.STRING)
    private FishLivEnv livingEnv;

    public Fish() {
    }

    public Fish(FishLivEnv livingEnv) {
        this.livingEnv = livingEnv;
    }

    public FishLivEnv getLivingEnv() { return livingEnv; }
    public void setLivingEnv(FishLivEnv livingEnv) { this.livingEnv = livingEnv; }
}