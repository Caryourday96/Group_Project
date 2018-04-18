package alonquin.cst2335.group_project;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

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

        routenoDestination = (TextView) findViewById(R.id.routenoDestinationView);
        direction = (TextView) findViewById(R.id.directionView);
        startTime = (TextView) findViewById(R.id.startTimeView);
        adjustedTime = (TextView) findViewById(R.id.adjustedTimeView);
        coordinates = (TextView) findViewById(R.id.coordinatesView);
        speed = (TextView) findViewById(R.id.speedView);
        refresh = (Button) findViewById(R.id.refreshRouteButton);


        Bundle extras = getIntent().getExtras();

        route = new OCRoute(extras.getString("routeno"), extras.getString("destination"),
                extras.getString("direction"), extras.getString("stationNum")
        );

        new Update().executeOnExecutor( ((r) -> {r.run();}),"");


        refresh.setOnClickListener((e) -> {
            Toast toast = Toast.makeText(this, getString(R.string.oc_refresh), Toast.LENGTH_SHORT);
            toast.show();
            route.updateData();
            setDisplay();
        });
    }

    private void setDisplay() {
        routenoDestination.setText(getString(R.string.oc_route) + route.getRouteno() + " " + route.getDestination());
        direction.setText(getString(R.string.oc_direction) + route.getDirection());
        startTime.setText(getString(R.string.oc_starttime) + route.getStartTime());
        adjustedTime.setText(getString(R.string.oc_adjustedtime) + route.getAdjustedTime());
        coordinates.setText(getString(R.string.oc_latlong) + route.getCoordinates());
        speed.setText(getString(R.string.oc_gpsspeed) + route.getSpeed());
    }

    public class Update extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            route.updateData();
            if (route.getStartTime() == null || route.getSpeed() == null || route.getCoordinates() == null || route.getAdjustedTime() == null) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.i(ACTIVITY_NAME, e.toString());
                }
                route.updateData();
            }
            setDisplay();
            return null;
        }
    }
}
