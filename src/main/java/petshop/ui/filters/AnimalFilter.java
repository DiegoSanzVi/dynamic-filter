package petshop.ui.filters;

import petshop.backend.entities.Owner;
import petshop.backend.entities.enums.Specie;

public class AnimalFilter extends Filter {

    private Specie specie;
    private Owner owner;

    public void setSpecie(Specie specie) {
        this.specie = specie;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Specie getSpecie() {
        return specie;
    }
}
