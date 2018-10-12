package petshop.backend.entities.enums;

public enum Specie {
    DOG("dog"),CAT("cat"),HORSE("horse");

    private String specieName;

    Specie(String specieName) {
        this.specieName = specieName;
    }

    public String getSpecieName() {
        return specieName;
    }
}