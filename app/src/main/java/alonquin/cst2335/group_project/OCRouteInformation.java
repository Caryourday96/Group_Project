package alonquin.cst2335.group_project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OCRouteInformation extends Activity {
    public static final String ACTIVITY_NAME = "OCRouteInformation";

    OCRoute route;

    TextView routenoDestination;
    TextView direction;
    TextView startTime;
    TextView adjustedTime;
    TextView coordinates;
    TextView speed;
    Button refresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocroute_information);


        routenoDestination = (TextView)findViewById(R.id.routenoDestinationView);
        direction = (TextView)findViewById(R.id.directionView);
        startTime = (TextView)findViewById(R.id.startTimeView);
        adjustedTime = (TextView)findViewById(R.id.adjustedTimeView);
        coordinates = (TextView)findViewById(R.id.coordinatesView);
        speed = (TextView)findViewById(R.id.speedView);
        refresh = (Button)findViewById(R.id.refreshRouteButton);



        Bundle extras = getIntent().getExtras();
    /*
        route = new OCRoute(
            extras.getString("stationNum"), extras.getString("routeno"), extras.getString("destination"),
            extras.getString("coordinates"), extras.getString("speed"), extras.getString("startTime"),
            extras.getString("adjustedTime"), extras.getString("direction")
        );
    */
        route = new OCRoute (extras.getString("routeno"), extras.getString("destination"),
                extras.getString("direction"), extras.getString("stationNum")
                );
        route.updateData();

    //    while (!route.isReady()) {}

        setDisplay();



        refresh.setOnClickListener( (e) -> {
            Toast toast = Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT);
            toast.show();
            route.updateData();
            setDisplay();
        });
    }

    private void setDisplay() {
        routenoDestination.setText("Route " + route.getRouteno() + " " + route.getDestination());
        direction.setText("Direction: " + route.getDirection());
        startTime.setText("Start time: " + route.getStartTime());
        adjustedTime.setText("Adjusted time: " + route.getAdjustedTime());
        coordinates.setText("Latitude/Longitude: " + route.getCoordinates());
        speed.setText("GPS Speed: " + route.getSpeed());
    }
}
