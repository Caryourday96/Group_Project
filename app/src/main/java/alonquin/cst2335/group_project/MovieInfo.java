package alonquin.cst2335.group_project;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static alonquin.cst2335.group_project.Switcher.ACTIVITY_NAME;

public class MovieInfo extends AppCompatActivity {
    //protected static final String ACTIVITY_NAME = "MovieInfo";
    final CharSequence toastText = "Launching movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Button launchMovies = findViewById(R.id.launchMovies);
        launchMovies.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, toastText, duration);
                toast.show(); //display your message box

                //when button is clicked launch MoviesMain
                Intent MoviesMain = new Intent(MovieInfo.this, MoviesMain.class);
                                startActivity(MoviesMain);
            }
        });

        //set up snackbar
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to the Movies!!",
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    } //end onCreate


} //end class
