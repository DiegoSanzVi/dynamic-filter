package petshop.application;

public interface BackendProperties {
	String PET_SHOP_PERSISTENCE_UNIT = "petshop";
	String PET_SHOP_PROPERTIES_PREFIX = "spring.datasource";
	String PET_SHOP_REPO_PACKAGE = "petshop.backend.repositories";
	String PET_SHOP_ENTITY_PACKAGE = "petshop.backend.entities";
}