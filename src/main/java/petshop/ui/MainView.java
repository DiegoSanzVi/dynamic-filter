package petshop.ui;

import javax.annotation.PostConstruct;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import petshop.backend.entities.Owner;
import petshop.backend.entities.enums.Specie;
import petshop.backend.repositories.OwnerRepository;
import petshop.ui.filters.AnimalFilter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * A Designer generated component for the main-view.html template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("main-view")
@HtmlImport("src/main-view.html")
@Route("")
public class MainView extends PolymerTemplate<MainView.MainViewModel> {

    @Id("animals-grid")
    private AnimalsGrid animalsGrid;

    @Id("specie-cb")
    private ComboBox<Specie> specieCB;

    @Id("animal-searcher")
    private TextField animalSearcher;

    @Id("owner-cb")
    private ComboBox<Owner> ownerCb;


    @Autowired
    private OwnerRepository ownerRepository;


    /**
     * Creates a new MainView.
     */
    public MainView() {

        specieCB.setItems(Specie.values());

        specieCB.addValueChangeListener( event -> {
            animalsGrid.setFilter(generateFilter());
        });

        animalSearcher.setValueChangeMode(ValueChangeMode.EAGER);
        animalSearcher.addValueChangeListener(event -> {
            animalsGrid.setFilter(generateFilter());
        });

        ownerCb.addValueChangeListener(event -> {
           if ( event.isFromClient() ) {
               animalsGrid.setFilter(generateFilter());
           }
        });
    }

    @PostConstruct
    public void init(){
        List<Owner> owners = ownerRepository.findAll();
        owners.sort(Comparator.comparing(Owner::toString));
        ownerCb.setItems(owners);
    }

    public AnimalFilter generateFilter(){
        AnimalFilter animalFilter = new AnimalFilter();

        animalFilter.setText(animalSearcher.getValue());
        animalFilter.setSpecie(specieCB.getValue());
        animalFilter.setOwner(ownerCb.getValue());

        return animalFilter;
    }


    /**
     * This model binds properties between MainView and main-view.html
     */
    public interface MainViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
    }
}
