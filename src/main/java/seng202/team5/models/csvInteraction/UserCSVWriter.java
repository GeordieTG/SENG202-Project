package seng202.team5.models.csvInteraction;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.models.data.User;
import java.util.ArrayList;

/**
 * A class which writes csv files containing User data
 */
public class UserCSVWriter extends CSVCommon{

    /**
     * Constructor for creating the User csv writer for the application
     */
    protected UserCSVWriter() {
        super(false);
    }

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Writes the users' data to a .csv file
     * @param users Users that are registered in the database
     * @param fileDirectory Path to where the file will be created
     */
    public void writeUserDataToFile(ArrayList<User> users, String fileDirectory) {
        try {
            selectFile(fileDirectory);
            printWriter.println("Username,Password,Firstname,Lastname,Email,favourites,journeys,vehicles,totalco2,totaldistancetravelled");
            for (User user : users) {
                Gson gson = new Gson();
                String favouriteChargers = gson.toJson(user.getFavouriteChargerLocationList());
                String vehicles = gson.toJson(user.getVehicleList());
                String journeys = gson.toJson(user.getSavedJourneyList());

                printWriter.println(user.getUsername() + "," + user.getHashedPassword() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + favouriteChargers + ","  + journeys + "," + vehicles + "," + user.getTotalCO2() + "," + user.getTotalDistance());
            }
            csvClose();
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
