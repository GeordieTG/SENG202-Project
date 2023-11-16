package seng202.team5.managers;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Journey;
import seng202.team5.models.data.Location;
import java.util.ArrayList;

/**
 * Extracted table creation functionality
 */
public class TableManager {

    /**
     * Extracted way to generate and populate the charger table
     */
    public static void initChargerTable(TableView<Charger> table, ArrayList<Charger> chargers) {

        TableColumn<Charger, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Charger, Integer> latCol = new TableColumn<>("Latitude");
        latCol.setCellValueFactory(new PropertyValueFactory<>("latitude"));

        TableColumn<Charger, Integer> longCol = new TableColumn<>("Longitude");
        longCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        TableColumn<Charger, String> operatorCol = new TableColumn<>("Operator");
        operatorCol.setCellValueFactory(new PropertyValueFactory<>("operator"));

        TableColumn<Charger, String> ownerCol = new TableColumn<>("Owner");
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));

        TableColumn<Charger, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Charger, Boolean> openCol = new TableColumn<>("Always Open");
        openCol.setCellValueFactory(new PropertyValueFactory<>("alwaysOpen"));

        TableColumn<Charger, Integer> carParkCol = new TableColumn<>("Number of Car Parks");
        carParkCol.setCellValueFactory(new PropertyValueFactory<>("carParkCount"));

        TableColumn<Charger, Boolean> paymentCol = new TableColumn<>("Does It Need Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("carParkNeedsPayment"));

        TableColumn<Charger, Integer> timeCol = new TableColumn<>("Maximum Time Limit");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("maxTimeLimit"));

        TableColumn<Charger, Boolean> touristCol = new TableColumn<>("Any Attractions");
        touristCol.setCellValueFactory(new PropertyValueFactory<>("touristAttraction"));

        table.getColumns().add(nameCol);
        table.getColumns().add(addressCol);
        table.getColumns().add(operatorCol);
        table.getColumns().add(ownerCol);
        table.getColumns().add(openCol);
        table.getColumns().add(carParkCol);
        table.getColumns().add(paymentCol);
        table.getColumns().add(timeCol);
        table.getColumns().add(touristCol);
        table.getColumns().add(latCol);
        table.getColumns().add(longCol);

        table.setItems(FXCollections.observableList(chargers));



    }

    /**
     * Extracted way to generate and populate the journeys table
     *
     */
    public static void initJourneyTable(TableView<Journey> table, ArrayList<Journey> journeys) {

        TableColumn<Journey, String> titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Journey, ArrayList<Location>> stopsCol = new TableColumn("Stops");
        stopsCol.setCellValueFactory(new PropertyValueFactory<>("stops"));

        TableColumn<Journey, Float> CO2Col = new TableColumn("CO2 Emitted (kg)");
        CO2Col.setCellValueFactory(new PropertyValueFactory<>("CO2Emitted"));

        table.getColumns().add(titleCol);
        table.getColumns().add(stopsCol);
        table.getColumns().add(CO2Col);

        table.setItems(FXCollections.observableList(journeys));
    }
}
