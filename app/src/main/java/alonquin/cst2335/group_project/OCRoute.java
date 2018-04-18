package alonquin.cst2335.group_project;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ben on 2018-03-29.
 */

public class OCRoute {

    private String stationNum;
    private String routeno;
    private String destination;
    private String coordinates;
    private String speed;
    private String startTime;
    private String adjustedTime;
    private String direction;

    private boolean ready = false;

    public static String getRouteInfo = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=";
    public static String getRouteInfoTrailer = "&routeNo=";

    public OCRoute (String stationNum, String routeno, String destination, String coordinates, String speed, String startTime, String adjustedTime, String direction) {
        this.stationNum = ((stationNum != null) ? stationNum : "Information unavailable");
        this.routeno = ((routeno != null) ? routeno : "Information unavailable");
        this.destination = ((destination != null) ? destination : "Information unavailable");
        this.coordinates = ((coordinates != null) ? coordinates : "Information unavailable");
        this.speed = ((speed != null) ? speed : "Information unavailable");
        this.startTime = ((startTime != null) ? startTime : "Information unavailable");
        this.adjustedTime = ((adjustedTime != null) ? adjustedTime : "Information unavailable");
        this.direction = ((direction != null) ? direction : "Information unavailable");

        ready = true;
    }

    public OCRoute(String routeno, String destination, String direction, String stationNum) {
        this.routeno = routeno;
        this.destination = destination;
        this.direction = direction;
        this.stationNum = stationNum;
    }

    public void updateData() {
        new OCRouteQuery().execute("");
    }


    public String getRouteno() { return routeno; }
    public String getDestination() {
        return destination;
    }
    public String getStationNum() { return stationNum; }
    public String getCoordinates() { return coordinates; }
    public String getSpeed() { return speed; }
    public String getStartTime() { return startTime; }
    public String getAdjustedTime() { return adjustedTime; }
    public String getDirection() { return direction; }

    public boolean isReady () {
        return ready;
    }




    public class OCRouteQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... array) {
            Log.i("OCRoute constructor", "background activity begun..");


            try {
                String urlstring = getRouteInfo.concat(stationNum);
                urlstring = urlstring.concat(getRouteInfoTrailer);
                urlstring = urlstring.concat(routeno);
                URL url = new URL(urlstring);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                Log.i("OCRoute constructor", "attempting parse..");
                parse(conn.getInputStream());
                Log.i("OCRoute constructor", "parse complete");
            } catch (Exception e) {
                Log.i("OCRoute constructor", "Error: " + e.toString());
                return ("Error: " + e.toString());
            }
            return null;
        }

        protected void parse(InputStream in) throws XmlPullParserException, IOException {
            String lastTag = "";
            boolean cont = true;
            boolean foundDirection = false;

            String fullCoordinates = "";
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");


                int eventType = xpp.getEventType();

                while ((eventType != XmlPullParser.END_DOCUMENT) && cont) {

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            lastTag = xpp.getName();
                            break;
                        case XmlPullParser.TEXT:
                            if (lastTag.equals("Direction") && xpp.getText().equals(direction)) {
                                foundDirection = true;
                            } else if (foundDirection) {
                                Log.i("TagValue", xpp.getText());
                                if (lastTag.equals("TripDestination"))
                                    destination = xpp.getText();
                                else if (lastTag.equals("TripStartTime"))
                                    startTime = xpp.getText();
                                else if (lastTag.equals("AdjustedScheduleTime"))
                                    adjustedTime = xpp.getText();
                                else if (lastTag.equals("Latitude"))
                                    fullCoordinates = (xpp.getText().concat("/"));
                                else if (lastTag.equals("Longitude"))
                                    coordinates = fullCoordinates.concat(xpp.getText());
                                else if (lastTag.equals("GPSSpeed")) {
                                    speed = xpp.getText();
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (xpp.getName().equals("Trip") && foundDirection) {
                                cont = false;
                                Log.i("Route", "breaking from parse");
                            }
                            break;
                        default:
                            break;
                    }
                    xpp.next();
                    eventType = xpp.getEventType();
                }
                Log.i("FinalValues", destination +" "+
                        startTime +" "+
                        adjustedTime +" "+
                        coordinates +" "+
                        speed);
            } finally {
                in.close();
                Log.i("OCRoute constructor","closed input stream");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            stationNum = ((stationNum != null) ? stationNum : "Information unavailable");
            routeno = ((routeno != null) ? routeno : "Information unavailable");
            destination = ((destination != null) ? destination : "Information unavailable");
            coordinates = ((coordinates != null) ? coordinates : "Information unavailable");
            speed = ((speed != null) ? speed : "Information unavailable");
            startTime = ((startTime != null) ? startTime : "Information unavailable");
            adjustedTime = ((adjustedTime != null) ? adjustedTime : "Information unavailable");
            direction = ((direction != null) ? direction : "Information unavailable");

            ready = true;
        }
    }
}
