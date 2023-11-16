package seng202.team5.views.SupplementaryGUIControllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import java.util.List;

/**
 * A custom ListCell to represent a journey as a list of stops
 */
public class JourneyStopListCell extends ListCell<String> {

    /**
     * The HBox of the cell
     */
    private final HBox hbox = new HBox();

    /**
     * Generates the list cell for use within a ListView
     */
    public JourneyStopListCell() {
        super();
        /*
         * The button to click when the user wants to remove the stop
         */
        Button delete = new Button("x");
         //The button to click when the user wants to shift the stop downwards
         Button shiftDown = new Button("↓");
         // The button to click when the user wants to shift the stop upwards
         Button shiftUp = new Button("↑");
         // The pane
         Pane pane = new Pane();
         // The label to display the item
         Label label = new Label("");
        hbox.getChildren().addAll(label, pane, shiftUp, shiftDown, delete);
        HBox.setHgrow(pane, Priority.ALWAYS);
        shiftUp.setOnAction(event -> shift(-1));
        shiftDown.setOnAction(event -> shift(1));
        delete.setOnAction(event -> onDelete());
    }

    /**
     * Deletes the current item from the ListView
     */
    private void onDelete() {
        if (this.getListView().getItems().size() > 2) {
            this.getListView().getItems().remove(this.getItem());
        }
    }

    /**
     * Shifts the current cell within the listView up (negative) or down (positive) based on the direction
     * @param direction The direction within the list to shift.
     */
    private void shift(int direction) {
        List<String> currentList = this.getListView().getItems();
        int currentIndex = currentList.indexOf(this.getItem());
        if (currentIndex + direction >= 0 && currentIndex + direction < currentList.size()) {
            String outer = currentList.get(currentIndex + direction);
            String current = this.getItem();
            currentList.set(currentIndex + direction, current);
            currentList.set(currentIndex, outer);
        }
    }


    /**
     * Whenever the item within the cell is changed this function is called
     * @param item The new item of the cell
     * @param empty Boolean for if the cell is empty
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item);
            setGraphic(hbox);
        } else {
            setText("");
            setGraphic(null);
        }
    }
}
