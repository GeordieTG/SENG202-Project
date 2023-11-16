package seng202.team5.managers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import seng202.team5.models.databaseInteraction.Filter;
import java.util.ArrayList;

/**
 * Abstraction of filter functionality
 */
public class FilterManager {

    /**
     * Takes parameters of all checkboxes/sliders from the filter dropdown and returns a list of filter objects matching the selected options
     * @param alwaysOpenCheck Checkbox to filter alwaysOpen
     * @param carParkNeedsPaymentCheck Checkbox to filter carParkNeedsPayment
     * @param touristAttractionCheck Checkbox to filter touristAttraction
     * @param numberOfParksSlider Slider to filter minimum number of parks
     * @param maxTimeLimitSlider Slider to filter minimum time limit
     * @return list of filter objects
     */
    public ArrayList<Filter> filter(CheckBox alwaysOpenCheck, CheckBox carParkNeedsPaymentCheck, CheckBox touristAttractionCheck, Slider numberOfParksSlider, Slider maxTimeLimitSlider, CheckBox type1TetheredCheckBox, CheckBox type2SocketedCheckBox, CheckBox type2CCSCheckBox, CheckBox type2TetheredCheckBox, CheckBox CHAdeMOCheckBox) {

        ArrayList<Filter> filters = new ArrayList<>();

        // Always Open
        if (alwaysOpenCheck.isSelected()) {
            Filter one = new Filter("alwaysOpen", "=",  "1");
            filters.add(one);
        }

        // Needs Payment
        if (carParkNeedsPaymentCheck.isSelected()) {
            Filter two = new Filter("carParkNeedsPayment", "=", "0");
            filters.add(two);
        }

        // Is Tourist Attraction
        if (touristAttractionCheck.isSelected()) {
            Filter three = new Filter("touristAttraction", "=", "1");
            filters.add(three);
        }

        // Minimum Car Park Count
        if (numberOfParksSlider.getValue() != 0) {
            Filter four = new Filter("carParkCount", ">=", String.valueOf(numberOfParksSlider.getValue()));
            filters.add(four);
        }

        // Max Time Limit
        if (maxTimeLimitSlider.getValue() != 0) {
            Filter five = new Filter("maxTimeLimit", ">=", String.valueOf(maxTimeLimitSlider.getValue()));
            filters.add(five);
        }

        // Type 1 Tethered
        if (type1TetheredCheckBox.isSelected()) {
            Filter six = new Filter("connectors", "LIKE", "'%Type 1 Tethered%'");
            filters.add(six);
        }

        // Type 2 Socketed
        if (type2SocketedCheckBox.isSelected()) {
            Filter seven = new Filter("connectors", "LIKE", "'%Type 2 Socketed%'");
            filters.add(seven);
        }

        // Type 2 CCS
        if (type2CCSCheckBox.isSelected()) {
            Filter eight = new Filter("connectors", "LIKE", "'%Type 2 CCS%'");
            filters.add(eight);
        }

        // Type 2 Tethered
        if (type2TetheredCheckBox.isSelected()) {
            Filter nine = new Filter("connectors", "LIKE", "'%Type 2 Tethered%'");
            filters.add(nine);
        }

        // CHAdeMO
        if (CHAdeMOCheckBox.isSelected()) {
            Filter ten = new Filter("connectors", "LIKE", "'%CHAdeMO%'");
            filters.add(ten);
        }

        return filters;
    }
}
