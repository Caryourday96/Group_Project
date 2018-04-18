package alonquin.cst2335.group_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OCStationInformation extends Activity {

    private static boolean deleteStation = false;
    private static String lastStation = "";

    public static String getRouteSummaryForStop = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=";

    protected static final String ACTIVITY_NAME = "OCStationInformation";

    int stationNumber;
    String stationName = "";

    private Context ctx = this;
    ArrayList<OCRoute> allRoutes = new ArrayList<>();
    ArrayList<String> routesInfo = new ArrayList<String>();
    ListView routes;
    ProgressBar progressBar;
    int progress;
    TextView stationNameView;


    Button delete;

    public static void resetDeleteStation() {
        deleteStation = false;
    }

    public static boolean getDeleteStation() {
        return deleteStation;
    }

    public static String getDeletedStationNo() { return lastStation; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocstation_information);

        routes = (ListView) findViewById(R.id.routesView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progress = 0;
        stationNameView = (TextView) findViewById(R.id.stationName);
        delete = (Button) findViewById(R.id.deleteStationButton);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.i(ACTIVITY_NAME, "Error: no stop number could be found");
        } else {
            stationNumber = Integer.parseInt(extras.getString("stationNumber"));
            stationNameView.setText("Station name: " + stationName);
        }

        new OCQuery().execute("");


        RouteAdapter adapter = new RouteAdapter(this);
        routes.setAdapter(adapter);


        delete.setOnClickListener((e) -> {
            Log.i(ACTIVITY_NAME, "Delete button clicked!");
            deleteStation = true;
            lastStation = Integer.toString(stationNumber);
            finish();
        });


        routes.setOnItemClickListener((parent, view, position, id) -> {
            String s = routesInfo.get(position);
            Log.i(ACTIVITY_NAME, "Message: " + s);
            Intent i = new Intent(OCStationInformation.this, OCRouteInformation.class);
            i.putExtra("routeno", allRoutes.get(position).getRouteno());
            i.putExtra("destination", allRoutes.get(position).getDestination());
            i.putExtra("stationNum", allRoutes.get(position).getStationNum());
            i.putExtra("direction", allRoutes.get(position).getDirection());
            startActivity(i);
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

    private void updateProgressBar(int u, int max) {
        progress += u;
        if (progress > max)
            progress = max;
        progressBar.setProgress(progress);
    }

    private void stationNotFoundProcedure() {
        //   *************************************************   //
        /*      FOR FOLLOWING CODE BLOCK:
                Author: mkyong
                url: https://www.mkyong.com/android/android-custom-dialog-example/
        */
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.oc_custom_dialog);
        dialog.setTitle("Station not found");


        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(getString(R.string.oc_station_w_number) + " " + stationNumber + " "+ getString(R.string.oc_station_not_found));

        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_launcher_foreground);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener((x) -> {
            dialog.dismiss();
        });
        dialog.show();
        //   *************************************************   //
    }





    public class RouteAdapter extends ArrayAdapter<String> {
        public RouteAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return (routesInfo.size());
        }

        @Override
        public String getItem(int position) {
            return routesInfo.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCStationInformation.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.oc_route, null);

            TextView routeText = (TextView)result.findViewById(R.id.route_text);

            routeText.setText (getItem(position) );
            return result;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

    }



    public class OCQuery extends AsyncTask<String, Integer, String> {
        public String connStationNumber;

        public ArrayList<OCRoute> routesList = new ArrayList<OCRoute>();

        private String currentRouteno;
        private String currentRouteDirection;
        private String currentRouteDestination;


        @Override
        protected String doInBackground(String... array) {
            Log.i(ACTIVITY_NAME, "background activity begun..");

            connStationNumber = Integer.toString(stationNumber); // test with algonquin stop for now.

            try {
                //      URL url = new URL(getRouteSummaryForStop + connStationNumber);
                URL url = new URL(getRouteSummaryForStop.concat(connStationNumber));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                updateProgressBar(10, 10);

                Log.i(ACTIVITY_NAME, "attempting parse..");
                parse(conn.getInputStream());
                Log.i(ACTIVITY_NAME, "parse complete");
            } catch (Exception e) {
                Log.i(ACTIVITY_NAME, "Error: " + e.toString());
                return ("Error: " + e.toString());
            }
            return null;
        }

        protected void parse(InputStream in) throws XmlPullParserException, IOException {
            String lastTag = "";
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");


                int eventType = xpp.getEventType();
                updateProgressBar(10,20);

                Log.i(ACTIVITY_NAME, "Attempting parse: ");
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            Log.i(ACTIVITY_NAME, "Tag found.");
                            lastTag = xpp.getName();
                            updateProgressBar(3,80);
                            Log.i(ACTIVITY_NAME, "Tag is " + lastTag);
                            break;
                        case XmlPullParser.TEXT:
                            if (lastTag.equals("StopDescription")) {
                                Log.i(ACTIVITY_NAME, "Station name found: ");
                                stationName = xpp.getText();
                                updateProgressBar(12,80);
                            } else if (lastTag.equals("RouteNo")) {
                                currentRouteno = xpp.getText();
                                updateProgressBar(12,80);
                            } else if (lastTag.equals("Direction")) {
                                currentRouteDirection = xpp.getText();
                                updateProgressBar(10,80);
                            } else if (lastTag.equals("RouteHeading")) {
                                currentRouteDestination = xpp.getText();
                                updateProgressBar(10,80);
                                routesList.add(new OCRoute(currentRouteno, currentRouteDestination, currentRouteDirection, Integer.toString(stationNumber)));
                            }
                            break;
                        default:
                            break;
                    }
                    xpp.next();
                    eventType = xpp.getEventType();
                }
            } finally {
                in.close();
                Log.i(ACTIVITY_NAME, "closed input stream");
                updateProgressBar(100,90);
            }
        }


        @Override
        protected void onPostExecute(String result) {

            RouteAdapter adapter = new RouteAdapter(ctx);
            ListView routesview = (ListView)findViewById(R.id.routesView);
            routesview.setAdapter(adapter);

            stationNameView.setText("Station name: " + stationName);

            for (OCRoute r : routesList) {
                String newRoute = "";
                newRoute = newRoute.concat(r.getRouteno());
                newRoute = newRoute.concat(" ");
                newRoute = newRoute.concat(r.getDestination());
                routesInfo.add(newRoute);
                adapter.notifyDataSetChanged();
                allRoutes.add(r);
                updateProgressBar(2,100);
            }
            updateProgressBar(100,100);

            if (stationName.equals(""))
                stationNotFoundProcedure();
        }
    }
}
