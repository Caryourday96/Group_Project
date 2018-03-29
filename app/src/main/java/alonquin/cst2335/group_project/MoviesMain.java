package alonquin.cst2335.group_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MoviesMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_main);

       // final ListView movieList = (ListView) findViewById(movieList);
        String [] movies = new String[] {"Spiderman", };

    } //end onCreate
}
