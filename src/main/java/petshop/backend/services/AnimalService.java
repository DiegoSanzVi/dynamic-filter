package petshop.backend.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import petshop.backend.entities.Animal;
import petshop.backend.entities.Owner;
import petshop.backend.entities.enums.Specie;
import petshop.backend.repositories.AnimalRepository;
import petshop.backend.repositories.FilterService;
import petshop.ui.filters.AnimalFilter;

import com.vaadin.flow.data.provider.QuerySortOrder;

@Service
public class AnimalService implements FilterService<Animal,AnimalFilter> {

//    @Autowired
//    @Qualifier("entityManagerFactory")
//    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager entityManager;

    private AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository){
        this.animalRepository = animalRepository;
    }

    @Override
    public Stream<Animal> findMatching(Optional<AnimalFilter> filter, int offset, int limit, List<QuerySortOrder> sortOrders, boolean all) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
        Root<Animal> root = cq.from(Animal.class);

        cq.select(root);

        // apply filters if a filter is present
        addFilters(filter, cb, cq, root, all);

        if ( sortOrders == null || sortOrders.isEmpty() ) {
            // apply default sort ordering
            applyDefaultSortOrder(cb, cq, root);
        }else{
            applySortOrder(cb, cq, root, sortOrders);
        }

        TypedQuery<Animal> animalsQuery = entityManager.createQuery(cq).setMaxResults(limit).setFirstResult(offset);

        List<Animal> animals = animalsQuery.getResultList();

        return animals.stream();
    }

    @Override
    public long countMatching(Optional<AnimalFilter> filter, boolean all) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Animal> root = cq.from(Animal.class);

        cq.select(cb.count(root));

        // apply filters if a filter is present
        addFilters(filter, cb, cq, root, all);

        TypedQuery<Long> projectsQuery = entityManager.createQuery(cq);

        return projectsQuery.getSingleResult();
    }

    @Override
    public List<Predicate> generatePredicates(AnimalFilter animalFilter, CriteriaBuilder cb, Root<Animal> root) {
        if ( animalFilter == null){
            return new ArrayList<>();
        }

        String text = animalFilter.getText();
        Specie specie = animalFilter.getSpecie();
        Owner owner = animalFilter.getOwner();

        List<Predicate> predicates = new ArrayList<>();

        // text
        if (text != null && !text.trim().isEmpty()) {
            String re = "%" + text.toLowerCase().trim() + "%";

            List<Predicate> termPredicates = new ArrayList<>();

            termPredicates.add(cb.like(cb.lower(root.get("name").as(String.class)), re));
            termPredicates.add(cb.like(cb.lower(root.get("description").as(String.class)), re));

            predicates.add(cb.or(termPredicates.toArray(new Predicate[0])));
        }

        if (specie != null) {
            predicates.add(cb.equal(root.get("specie"),specie));
        }

        if (owner != null){
            predicates.add(cb.equal(root.get("owner"),owner));
        }

        // other predicates(if needed)

        // end

        return predicates;
    }

    public void applyDefaultSortOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root<Animal> root) {

        List<Order> orders = new ArrayList<>();

        orders.add(criteriaBuilder.asc(root.get("name")));
        orders.add(criteriaBuilder.asc(root.get("specie")));
        orders.add(criteriaBuilder.asc(root.get("age")));

        criteriaQuery.orderBy(orders);
     }

    public List<Animal> findAll(){
        return getAnimalRepository().findAll();
    }

    public AnimalRepository getAnimalRepository() {
        return animalRepository;
    }
}