package alonquin.cst2335.group_project;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OCStationInformation extends Activity {

    protected static final String ACTIVITY_NAME = "OCStationInformation";

    int stationNumber;
    String stationName;

    ArrayList<OCRoute> routesData = new ArrayList<>();

    /*    all info in routes:
    int routeno;
    String destination;
    String coordinates;
    String speed;
    String startTime;
    String adjustedTime;
    */
    ArrayList<String> routesList = new ArrayList<>();
    ListView routes;
    ProgressBar progressBar;
    TextView stationNameView;
    Button refresh;
    private Context ctx;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocstation_information);

        ctx = this;

        OCDatabaseHelper dbHelper = new OCDatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();

        routes = findViewById(R.id.routesView);
        progressBar = findViewById(R.id.progressBar);
        stationNameView = findViewById(R.id.stationName);
        refresh = findViewById(R.id.refreshButton);

        RouteAdapter adapter = new RouteAdapter(this);
        routes.setAdapter(adapter);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.i(ACTIVITY_NAME, "Error: no stop number could be found");
        } else {
            stationNumber = Integer.parseInt(extras.getString("stationNumber"));
            stationName = extras.getString("stationNumber");  // todo  get real data

            stationNameView.setText("Station name: " + stationName);
        }
        // TODO : open database and get info based on station name
        cursor = db.rawQuery("SELECT " +
                OCDatabaseHelper.ROUTE_NO + " FROM " +
                OCDatabaseHelper.TABLE_NAME_ROUTES, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "Current cursor position: " + cursor.getPosition());
            String newRoute = "Route Number: ";
            newRoute = newRoute.concat(cursor.getString(0));
            routesList.add(newRoute);
            cursor.moveToNext();
        }


        refresh.setOnClickListener((e) -> {
            // TODO : actually refresh tho
            Toast toast = Toast.makeText(ctx, "Refreshing...", Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    @Override
    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }


    @Override
    protected void onPause() {
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }


    public class RouteAdapter extends ArrayAdapter<String> {
        public RouteAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return (routesList.size());
        }

        @Override
        public String getItem(int position) {
            return routesList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCStationInformation.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.oc_route, null);

            TextView routeText = result.findViewById(R.id.route_text_routeno);
            // TODO   routeText should contain all data for route, not just number
            routeText.setText(getItem(position));

            return result;
        }

        @Override
        public long getItemId(int position) {
            //    cursor.moveToPosition(position);
            //   return (long)Integer.parseInt(cursor.getString(1));
            return position;
        }

    }
}
