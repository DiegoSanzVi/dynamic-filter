package petshop.backend;

import javax.annotation.PostConstruct;

import java.util.Arrays;

import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import petshop.backend.entities.Animal;
import petshop.backend.entities.Owner;
import petshop.backend.entities.enums.Specie;
import petshop.backend.repositories.AnimalRepository;
import petshop.backend.repositories.OwnerRepository;

@Component
public class TestDataGenerator {
	private static Logger log = LoggerFactory.getLogger(TestDataGenerator.class);

	private AnimalRepository animalRepository;
	private OwnerRepository ownerRepository;
	private final Fairy fairy = Fairy.create();

	@Autowired
	public TestDataGenerator(AnimalRepository animalRepository, OwnerRepository ownerRepository){
		this.animalRepository = animalRepository;
		this.ownerRepository = ownerRepository;
	}


	@PostConstruct
	public void initTestDatabase() {
		log.info("Initializing the database ...");
		Animal animal;
		Person person, ownerPerson;
		Owner owner;
		Specie[] species = Specie.values();

		for (int i = 0; i < 100; i++){
			animal = new Animal();
			person = fairy.person();
			ownerPerson = fairy.person();
			animal.setAge(person.age());
			animal.setDescription(fairy.textProducer().limitedTo(15).sentence());
			animal.setName(person.firstName());
			animal.setSpecie(species[i%species.length]);

			owner = new Owner();
			owner.setFirstName(ownerPerson.firstName());
			owner.setLastName(ownerPerson.lastName());
			owner.setAnimals(Arrays.asList(animal));
			animal.setOwner(owner);


			ownerRepository.save(owner);
		}
	}
}
