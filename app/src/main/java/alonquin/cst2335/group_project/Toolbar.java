package alonquin.cst2335.group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class Toolbar extends AppCompatActivity {
    private MenuItem helpMenu;

    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.mcquiz:
                intent=new Intent(Toolbar.this, MCQuizCreator.class);
                startActivity(intent);
                break;

            case R.id.piform:
                intent=new Intent(Toolbar.this, PIForm.class);
                startActivity(intent);
                break;
            case R.id.octranspo:
                intent=new Intent(Toolbar.this, OCTranspo.class);
                startActivity(intent);
                break;
            default:

        }
        return true;
    }
}
