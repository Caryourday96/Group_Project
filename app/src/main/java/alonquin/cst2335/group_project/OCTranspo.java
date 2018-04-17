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
            ContentValues newData = new ContentValues();


            newData.put(OCDatabaseHelper.STATION_NAME, "NAME_NOT_FOUND");

            // TODO: validate station input to ensure integer value
            newData.put(OCDatabaseHelper.STATION_NO, stationInput.getText().toString());

            db.insert(OCDatabaseHelper.TABLE_NAME, OCDatabaseHelper.STATION_NAME, newData);

            String newStation = "Station number ";
            newStation = newStation.concat(stationInput.getText().toString());
            stationsList.add(newStation);
            stationsNumbers.add(stationInput.getText().toString());
            stationInput.setText("");
            adapter.notifyDataSetChanged();


            // TODO:  real life implementation of that stuff below
            //   *************************************************   //
        /*      FOR FOLLOWING CODE BLOCK:
                Author: mkyong
                url: https://www.mkyong.com/android/android-custom-dialog-example/
        */


            Dialog dialog = new Dialog(ctx);
            dialog.setContentView(R.layout.oc_custom_dialog);
            dialog.setTitle("Add Stop");

            // change this junko around

            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
            text.setText("Stop not found! Probably because I haven't yet implemented code to find stops");

            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            image.setImageResource(R.drawable.ic_launcher_foreground);

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

            dialogButton.setOnClickListener((x) -> {
                dialog.dismiss();

                Snackbar welcome = Snackbar.make(findViewById(android.R.id.content),
                        "I need a better reason to use a snackbar!", Snackbar.LENGTH_SHORT);
                welcome.show();
            });
            dialog.show();
            //   *************************************************   //

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



