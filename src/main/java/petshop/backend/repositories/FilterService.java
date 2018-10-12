package petshop.backend.repositories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

public interface FilterService<T, F> {


    default long countAllMatching(Optional<F> filter) {
        return countMatching(filter, true);
    }

    default Stream<T> findAnyMatching(Optional<F> filter, int offset, int limit,
                                      List<QuerySortOrder> sortOrders) {
        return findMatching(filter, offset, limit, sortOrders, false);
    }

    default Stream<T> findAllMatching(Optional<F> filter, int offset, int limit,
                                    List<QuerySortOrder> sortOrders) {
        return findMatching(filter, offset, limit, sortOrders, true);

    }


    long countMatching(Optional<F> filter, boolean all);


    Stream<T> findMatching(Optional<F> filter, int offset, int limit,
                         List<QuerySortOrder> sortOrders, boolean all);

    default List<Predicate> generatePredicates(F filter, CriteriaBuilder cb, Root<T> root) {
        return new ArrayList<>();
    }


    default void addFilters(Optional<F> filter, CriteriaBuilder cb, CriteriaQuery cq, Root<T> root, boolean all) {
        if (filter.isPresent()) {
            List<Predicate> filters = generatePredicates(filter.get(), cb, root);
            if (filters != null && !filters.isEmpty()) {
                if (all) {
                    cq.where(cb.and(filters.toArray(new Predicate[filters.size()])));
                } else {
                    cq.where(cb.or(filters.toArray(new Predicate[filters.size()])));
                }
            }
        }
    }

    default void applySortOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Root<T> root, List<QuerySortOrder> sortOrders) {

        List<Order> orders = new ArrayList<>();

        for (QuerySortOrder sortOrder : sortOrders) {
            String field = sortOrder.getSorted();
            if (sortOrder.getDirection() == SortDirection.ASCENDING) {
                orders.add(criteriaBuilder.asc(root.get(field)));
            } else {
                orders.add(criteriaBuilder.desc(root.get(field)));
            }
        }

        criteriaQuery.orderBy(orders);
    }
}
