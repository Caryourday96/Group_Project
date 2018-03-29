package alonquin.cst2335.group_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static alonquin.cst2335.group_project.Switcher.ACTIVITY_NAME;

public class MovieInfo extends AppCompatActivity {
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
                launchMoviesMainActivity();
            }
        });
    } //end onCreate

    protected void launchMoviesMainActivity(){
        Log.i(ACTIVITY_NAME, "User clicked LaunchMovies button");
        Intent intent = new Intent(MovieInfo.this, MoviesMain.class);
        startActivityForResult(intent, 50);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == requestCode && resultCode == RESULT_OK) {
            Bundle extras =data.getExtras();
        } //end if
    }
}
