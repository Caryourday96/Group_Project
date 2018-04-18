package alonquin.cst2335.group_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class Switcher extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Switcher";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);


        Button startQuiz = findViewById(R.id.mcquiz);
        Button MovieInfo = findViewById(R.id.movie);
        Button OCTranspo = findViewById(R.id.octranspo);
        Button PIForm = findViewById(R.id.piform);

        startQuiz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Quiz App");
//                Intent MCQuizCreator = new Intent(getApplicationContext(), MCQuizCreator.class);
                Intent MCQuizCreator = new Intent(Switcher.this, MCQuizCreator.class);
                startActivity(MCQuizCreator);
            }
        });

        MovieInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Movie Information App");
                Intent MovieInfo = new Intent(Switcher.this, MovieInfo.class);
                startActivity(MovieInfo);
            }
        });

        OCTranspo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked OCTranspo");
                Intent OCTranspo = new Intent(Switcher.this, OCTranspo.class);
                startActivity(OCTranspo);
            }
        });

        PIForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Patient Information");
                Intent PIForm = new Intent(Switcher.this, PIForm.class);
                startActivity(PIForm);
            }
        });


    }
}

