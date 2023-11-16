package seng202.team5.models.csvInteraction;

import org.junit.jupiter.api.Test;
import seng202.team5.models.data.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


public class UserCSVWriterTest {

    /**
     * Default User used for testing
     * @return default User
     */
    public User defaultUser() {
        User user = new User();
        Location location1 = new Location((float)-43.73745,(float)170.1009);
        Location location2 = new Location((float)-43.59049,(float)172.6302);

        ArrayList<Location> chargers = new ArrayList<>(Arrays.asList(location1,location2));
        user.setFavouriteChargerLocationList(chargers);

        Vehicle v = new Vehicle();
        Vehicle v2 = new Vehicle();
        Vehicle v3 = new Vehicle();
        ArrayList<Vehicle> vehicles = new ArrayList<>(Arrays.asList(v,v2,v3));
        user.setVehicleList(vehicles);

        Journey j = new Journey();
        Journey j1 = new Journey();
        Journey j2 = new Journey();
        ArrayList<Journey> journeys = new ArrayList<>(Arrays.asList(j,j1,j2));
        user.setSavedJourneyList(journeys);

        return user;
    }

    /**
     * Tests a base case of one default user
     */
    @Test
    public void defaultOneUserTest() {

        User user = defaultUser();

        ArrayList<User> users = new ArrayList<>(List.of(user));
        try {
            UserCSVWriter writer = new UserCSVWriter();
            writer.writeUserDataToFile(users, "src/test/resources/testCSVData/writingTest.csv");
        } catch (Exception ignored) {fail();}

    }

    /**
     * Tests multiples users at once
     */
    @Test
    public void defaultMultipleUsersTest()  {
        User user1 = defaultUser();
        User user2 = defaultUser();

        ArrayList<User> users = new ArrayList<>(Arrays.asList(user1, user2));

        try {
            UserCSVWriter writer = new UserCSVWriter();
            writer.writeUserDataToFile(users, "src/main/resources/localCSVData/writingTest.csv");
        } catch (Exception ignored) {fail();}
    }

    /**
     * Boundary Testing - Tests with an empty array list
     */
    @Test
    public void emptyArrayListTest() {
        User user = defaultUser();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        user.setVehicleList(vehicles);


        ArrayList<User> users = new ArrayList<>(List.of(user));

        try {
            UserCSVWriter writer = new UserCSVWriter();
            writer.writeUserDataToFile(users, "src/main/resources/localCSVData/writingTest.csv");
        } catch (Exception ignored) {fail();}
    }

    /**
     * Boundary Testing - Tests adding a completely empty user
     */
    @Test
    public void emptyUserTest() {
        User user1 = new User();
        ArrayList<User> users = new ArrayList<>(List.of(user1));

        try {
            UserCSVWriter writer = new UserCSVWriter();
            writer.writeUserDataToFile(users, "src/main/resources/localCSVData/writingTest.csv");
        } catch (Exception ignored) {fail();}
    }
}

