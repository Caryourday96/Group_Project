package alonquin.cst2335.group_project;

/**
 * Created by Ben on 2018-03-29.
 */

public class OCRoute {

    int routeno;
    String destination;
    String coordinates;
    String speed;
    String startTime;
    String adjustedTime;

    public OCRoute(int routeno, String destination, String coordinates, String speed, String startTime, String adjustedTime) {
        this.routeno = routeno;
        this.destination = destination;
        this.coordinates = coordinates;
        this.speed = speed;
        this.startTime = startTime;
        this.adjustedTime = adjustedTime;
    }

    public OCRoute(int routeno) {
        this.routeno = routeno;
        // TODO   get information about other stuff based on the route number
    }

}
