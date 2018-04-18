package alonquin.cst2335.group_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;

public class DisplayQuiz extends Toolbar {
    AlertDialog.Builder builder;
    String quizQuestion, quizAnswer, type, quizAns1, quizAns2, quizAns3, quizAns4, enteredAns, prompt;
    long quizID;
    TextView quizDetail, yourAnswerHere;
    EditText enterAnswer;
    Snackbar snackbar;
    FragmentManager fm;
    FragmentTransaction ft;
    mcDisplayFragment mcDis;
    tfDisplayFragment tfDis;
    nuDisplayFragment nuDis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quiz);
        initToolbar();

        final Context context = this;
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        mcDis = new mcDisplayFragment();
        tfDis = new tfDisplayFragment();
        nuDis = new nuDisplayFragment();

        builder = new AlertDialog.Builder(this);
        enterAnswer = (EditText) findViewById(R.id.enterAnswer);
        quizDetail = (TextView) findViewById(R.id.quizDetail);
        yourAnswerHere = (TextView) findViewById(R.id.youAnswerHere);
        Bundle infoPassed = getIntent().getExtras();
        quizID = infoPassed.getLong("ID");
        quizQuestion = infoPassed.getString("quiz");
        type = infoPassed.getString("type");
        quizAnswer = infoPassed.getString("correctAns");
        quizDetail.setText("ID:" + quizID + "  " + quizQuestion);
        if (type.equalsIgnoreCase("mc")) {
            quizAns1 = infoPassed.getString("ans1");
            quizAns2 = infoPassed.getString("ans2");
            quizAns3 = infoPassed.getString("ans3");
            quizAns4 = infoPassed.getString("ans4");
            Bundle info = new Bundle();
            info.putString("ans1", quizAns1);
            info.putString("ans2", quizAns2);
            info.putString("ans3", quizAns3);
            info.putString("ans4", quizAns4);
            mcDis.setArguments(info);
            ft.replace(R.id.quizContainer, mcDis);
            ft.addToBackStack("");
            ft.commit();
        } else if (type.equalsIgnoreCase("tf")) {
            enterAnswer.setVisibility(View.INVISIBLE);
            yourAnswerHere.setVisibility(View.INVISIBLE);
            ft.replace(R.id.quizContainer, tfDis);
            ft.addToBackStack("");
            ft.commit();
        } else if (type.equalsIgnoreCase("nu")) {

            ft.replace(R.id.quizContainer, nuDis);
            ft.addToBackStack("");
            ft.commit();
        }


        Button checkAnswer = (Button) findViewById(R.id.checkAnswer);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.quizDisplay_layout);
                if (type.equalsIgnoreCase("tf")) {
                    enteredAns = tfDis.getChoice();
                } else {
                    enteredAns = enterAnswer.getText().toString();
                }
                int duration = Snackbar.LENGTH_SHORT;
                if (enteredAns.equalsIgnoreCase(quizAnswer)) {
                    prompt = "You are correct!";
                } else {
                    prompt = "You didn't select the correct answer.";
                }
                snackbar = Snackbar.make(v, prompt, duration);
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem importItem = (MenuItem) menu.findItem(R.id.import_ques);
        importItem.setVisible(false);
        MenuItem statItem = (MenuItem) menu.findItem(R.id.menu_stat);
        statItem.setVisible(false);
        menu.findItem(R.id.menu_help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(DisplayQuiz.this)
                        .setTitle("Help")
                        .setMessage("Activity developed by Doaa Aldhaheri " + "\n" +
                                "Version number: v1.0" + "\n" +
                                "MC: type the corresponding answer in full;  TF: select true/false; numeric: enter the number")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });
        return true;
    }
}
