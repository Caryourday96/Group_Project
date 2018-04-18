package alonquin.cst2335.group_project;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static alonquin.cst2335.group_project.MovieDatabaseHelper.KEY_MESSAGE;

public class MoviesMain extends Activity {
    protected static final String ACTIVITY_NAME = "MoviesMain";
    Cursor cursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_main);

        final ListView listView = findViewById(R.id.listView);
        final ArrayList<String> movieList = new ArrayList<>();

        String [] movieName={"max1", "max2"};
        Integer [] imageID = {R.drawable.madmax1, R.drawable.madmax2};
        Movie movieClass = new Movie(this,  R.layout.activity_movie, 2, movieList);
        listView.setAdapter(movieClass);

        SQLiteDatabase db;

        boolean isTablet = true;
        //FrameLayout frameLayout;
        //boolean frameLayoutExists = true;

        Button movieDetails = findViewById(R.id.movieDetails);
        movieDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //when button is clicked launch MoviesMain
                Intent MoviesMain = new Intent(MoviesMain.this, MovieDetails.class);
                startActivity(MoviesMain);
            }
        });

//public void showMovies(){
            final MovieDatabaseHelper cdbHelper = new MovieDatabaseHelper(this);
            db = cdbHelper.getWritableDatabase();
            final ContentValues contentValues = new ContentValues();
/*
            cursor = db.query(MovieDatabaseHelper.TABLE_NAME, new String[]{MovieDatabaseHelper.KEY_id, MovieDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null);

            cursor.moveToFirst();
                do{
                    String message = cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_MESSAGE));
                    int IDs = cursor.getInt(cursor.getColumnIndex(MovieDatabaseHelper.KEY_id)); //return IDs
                    movieList.add(message);
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_MESSAGE)));
                    //lab 7
                    //chatMsgArray.add(IDs);
                    Log.i(ACTIVITY_NAME, "return database IDs: " + cursor.getString(cursor.getColumnIndex(MovieDatabaseHelper.KEY_id)));
                    cursor.moveToNext();
                } while(!cursor.isAfterLast());


            Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
            for(int i = 0; i < cursor.getColumnCount(); i++){
                Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
            }*/
      //  }//end showMovies

        //set up progress bar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /*****************************************************************************/


       // listView.setAdapter(movieAdapter); //listView becomes MovieAdapter object


        //pass msg and ID of item to fragment
        //frameLayout = findViewById(R.id.frameLayout);

        //Lab 7 check if frameLayout exists
        //isTablet = (frameLayout !=null);

        //when a listview item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Bundle infoToPass = new Bundle();
                String msg = String.valueOf(parent.getItemAtPosition(position));
                cursor.moveToPosition(position);
                id = cursor.getLong(cursor.getColumnIndex(MovieDatabaseHelper.KEY_id));

                //put info into bundles
                infoToPass.putString("data message", msg);
                infoToPass.putLong("id", id);
               // infoToPass.putBoolean("isTablet", isTablet);

               /* if(isTablet){ //if on tablet
                    FragmentManager fM = getFragmentManager();
                    FragmentTransaction fT = fM.beginTransaction();
                    MessageFragment msgFragment = new MessageFragment();
                    msgFragment.setArguments(infoToPass);

                    fT.addToBackStack(null); //undo transaction on back button
                    fT.replace(R.id.frameLayout, msgFragment);
                    fT.commit();

                }else{ //if on phone send bundle to MessageDetails class
                    Intent intent = new Intent(MoviesMain.this, MessageDetails.class);
                    intent.putExtras(infoToPass); //send bundle to the next activity
                    startActivityForResult(intent, 50);
                } //end isTablet*/
            }//end onClick
        });

        class MovieAdapter extends ArrayAdapter<String>{
            private String [] movieName;
            private Integer [] imageID;

            private MovieAdapter(Context ctx, String [] movieName, Integer [] imageID) {
                super(ctx, R.layout.activity_movies_main, movieName);
                this.movieName = movieName;
                this.imageID = imageID;
            } //ChatAdapter constructor

           /* public int getCount(){  //returns number of rows in ListView
                //number of strings in array list (chatMsgArray)
                //return movieArray.size();
            }

            public String getItem(int position){
                //return movieArray.get(position);

            }*/

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }

            class ViewHolder{

            }

            //@Override
            /*public View getView( LayoutInflater inflater, int position, View convertView, ViewGroup parent){
                // inflater = MovieAdapter.this.getLayoutInflater();
                //getLayoutInflater().inflate(MovieAdapter.this,null);
                //inflater.inflate(MovieAdapter.this, null);
                //View page = inflater.inflate()
                View result;
                result = inflater.inflate(R.layout.activity_movie, null);
/*
        if (position % 2 == 0) {
            result = inflater.inflate(R.layout.chat_row_incoming, null);
        } else {
            result = inflater.inflate(R.layout.chat_row_outgoing, null);
        }*/

            // TextView message = result.findViewById(R.id.message_text);
            // message.setText(getItem(position)); //get the string at position
            // return result;
            //  } //end getView*/
/*
        public long getId(int position){
            return position;
        }
*/
            /*****************Lab 7**************/
            //return database ID of item at position
            public long getItemId(int position){
                cursor.moveToPosition(position);
                return cursor.getLong(cursor.getColumnIndex(MovieDatabaseHelper.KEY_id));
            }

        } //end ChatAdapter
/****************************************************/


        AlertDialog.Builder builder = new AlertDialog.Builder(MoviesMain.this);
        //chain together setters to set the dialog characteristics
        builder.setMessage(R.string.dialog_message) //in strings.xml
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //user clicked OK button
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //user cancelled dialog
                    }
                })
                .show();



    } //end onCreate
}
