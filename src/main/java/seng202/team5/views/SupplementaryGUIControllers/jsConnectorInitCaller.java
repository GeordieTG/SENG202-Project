package seng202.team5.views.SupplementaryGUIControllers;

import seng202.team5.views.windowControllers.MainController;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows us to prepare a command for the map view so that as soon as it boots from another screen we can pass a command
 * for the jsConnector to call
 */
public class jsConnectorInitCaller {

    final ArrayList<String> commands = new ArrayList<String>();
    final ArrayList<Object[]> params = new ArrayList<Object[]>();

    public void addMapInitCommand(String string) {
        commands.add(string);
    }

    public void addMapInitParams(Object[] objects) {
        params.add(objects);
    }
    public List<String> getJSConnectorFunctions() {
        return commands;
    }
    public ArrayList<Object[]> getFunctionArguments() {
        return params;
    }

    protected void startMap() {
        MainController.getMostRecentMainController().openMapWindow(this);
    }

}
