package petshop.ui;

import org.springframework.beans.factory.annotation.Autowired;
import petshop.backend.entities.Animal;
import petshop.backend.services.AnimalService;
import petshop.ui.filters.AnimalFilter;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class AnimalsGrid extends Grid<Animal> {


    @Autowired
    private AnimalService animalService;
    /**
     * Data provider that fetches animals from the DB dynamically accordingly to a animal filter.
     */
    private ConfigurableFilterDataProvider<Animal, Void, AnimalFilter> dataProvider;

    public AnimalsGrid(){

        addColumn(Animal::getName).setHeader("Animal name:").setSortProperty("name").setSortable(true);
        addColumn(Animal::getSpecie).setHeader("Specie:").setSortProperty("specie").setSortable(true);
        addColumn(Animal::getDescription).setHeader("Description").setSortProperty("description").setSortable(true);
        addColumn(Animal::getAge).setHeader("Age").setSortProperty("age").setSortable(true);
        addColumn(Animal::getOwner).setHeader("Owner").setSortProperty("owner").setSortable(true);

        initDataProvider();
    }


    private void initDataProvider() {
        // Note: findAnyMatching(OR) or findAllMatching(AND)
        dataProvider = DataProvider.<Animal, AnimalFilter>fromFilteringCallbacks(
                query -> animalService.findAllMatching(query.getFilter(), query.getOffset(), query.getLimit(), query.getSortOrders()),
                query -> (int) animalService.countAllMatching(query.getFilter())).withConfigurableFilter();

        setDataProvider(dataProvider);
    }

    public void setFilter(AnimalFilter animalFilter) {
        dataProvider.setFilter(animalFilter);
    }
}
