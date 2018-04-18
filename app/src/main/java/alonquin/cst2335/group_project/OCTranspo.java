package alonquin.cst2335.group_project;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class OCTranspo extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "OCTranspoActivity";

    ArrayList<String> stationsList = new ArrayList<>();
    ArrayList<String> stationsNumbers = new ArrayList<>();
    ListView stations;
    EditText stationInput;
    Button addStation;


    private Context ctx;
    private SQLiteDatabase db;
    private Cursor cursor;

    private int currentStationIndex = 0;

    StationAdapter adapter;

    boolean menuOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;

        OCDatabaseHelper dbHelper = new OCDatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_octranspo);

        stations = (ListView) findViewById(R.id.stationsView);
        stationInput = (EditText) findViewById(R.id.stationNoInput);
        addStation = (Button) findViewById(R.id.addStationNoButton);
        adapter = new StationAdapter(this);
        stations.setAdapter(adapter);


        Log.i(ACTIVITY_NAME, "Attempted query:    SELECT " +
                OCDatabaseHelper.STATION_NAME + ", " +
                OCDatabaseHelper.STATION_NO + " FROM " +
                OCDatabaseHelper.TABLE_NAME);

        cursor = db.rawQuery("SELECT " +
                OCDatabaseHelper.STATION_NAME + ", " +
                OCDatabaseHelper.STATION_NO + " FROM " +
                OCDatabaseHelper.TABLE_NAME, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "Current cursor position: " + cursor.getPosition());
            String newStation = "Station number ";
            newStation = newStation.concat(cursor.getString(1));

            stationsList.add(newStation);
            stationsNumbers.add(cursor.getString(1));


            cursor.moveToNext();
        }


        addStation.setOnClickListener((e) -> {
            String s = stationInput.getText().toString();
            boolean valid = false;
            try {
                int a = Integer.parseInt(s);
                valid = true;
            } catch (Exception ex) {
                valid = false;
            }
            if (s.length() == 0)
                valid = false;

            if (valid) {
                ContentValues newData = new ContentValues();

                newData.put(OCDatabaseHelper.STATION_NAME, "NAME_NOT_FOUND");
                newData.put(OCDatabaseHelper.STATION_NO, s);

                db.insert(OCDatabaseHelper.TABLE_NAME, OCDatabaseHelper.STATION_NAME, newData);

                String newStation = "Station number ";
                newStation = newStation.concat(s);
                stationsList.add(newStation);
                stationsNumbers.add(s);
                stationInput.setText("");
                adapter.notifyDataSetChanged();
            } else {
                Snackbar badinput = Snackbar.make(findViewById(android.R.id.content), getString(R.string.oc_badinput), Snackbar.LENGTH_SHORT);
                badinput.show();
                stationInput.setText("");
            }
        });


        stations.setOnItemClickListener((parent, view, position, id) -> {
            String s = stationsList.get(position);
            Log.i(ACTIVITY_NAME, "Message: " + s);
            String stationNumber = stationsNumbers.get(position);
            Intent i = new Intent(OCTranspo.this, OCStationInformation.class);
            i.putExtra("stationNumber", stationNumber);
            currentStationIndex = position;
            startActivity(i);
        });


    }

    @Override
    protected void onResume() {
        Log.i(ACTIVITY_NAME, "In onResume()");

        if (OCStationInformation.getDeleteStation() == true) {
            Log.i(ACTIVITY_NAME, "Deleting station no " + currentStationIndex);
            String[] params = new String[1];
            params[0] = stationsNumbers.get(currentStationIndex);
            db.delete(OCDatabaseHelper.TABLE_NAME, OCDatabaseHelper.STATION_NO + "=?", params);

            adapter = new StationAdapter(this);
            stations.setAdapter(adapter);

            stationsList.remove(currentStationIndex);
            stationsNumbers.remove(currentStationIndex);
            adapter.notifyDataSetChanged();


            Snackbar welcome = Snackbar.make(findViewById(android.R.id.content),
                    "Station " + OCStationInformation.getDeletedStationNo() + " has been deleted", Snackbar.LENGTH_SHORT);
            welcome.show();

            OCStationInformation.resetDeleteStation();
        }

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
        db.close();
        super.onDestroy();
    }


    // FOLLOWING METHOD ChangeFragment written by 'ProgrammingKnowledge', Mar 5\ 2015.
    // URL: https://www.youtube.com/watch?v=FF-e6CnBwYY
    public void ChangeFragment (View view) {
        Log.i(ACTIVITY_NAME, "Changing fragment..");

        Fragment fragment;
        if (view == findViewById(R.id.toggleButton)) {
            Log.i(ACTIVITY_NAME, "Togglebutton was clickerood..");
            ListView stationsView = (ListView)(findViewById(R.id.stationsView));
            if (menuOn) {
                fragment = new OCStationsFragment();
                stationsView.setVisibility(View.VISIBLE);
            } else {
                fragment = new OCInfoFragment();
                stationsView.setVisibility(View.INVISIBLE);
            }

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentSpace, fragment);
            ft.commit();



            menuOn = !menuOn;
        }
    }
    // *************** //


    public class StationAdapter extends ArrayAdapter<String> {
        public StationAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return (stationsList.size());
        }

        @Override
        public String getItem(int position) {
            return stationsList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspo.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.oc_station, null);

            TextView stationText = (TextView) result.findViewById(R.id.station_text);
            stationText.setText(getItem(position));

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



