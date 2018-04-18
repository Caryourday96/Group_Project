package alonquin.cst2335.group_project;


import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;


public class MCQuizCreator extends Toolbar {

    Button save, mc, tf, nu, goback;
    FrameLayout container;
    AlertDialog.Builder builder;
    mcFragment mcF;
    tfFragment tfF;
    nuFragment nuF;
    FragmentManager fm;
    DatabaseQuizHelper helper;
    FragmentTransaction ft;
    String selectedType;
    String[] info;
    ProgressBar importProgress;
    protected static SQLiteDatabase db;
    ImportQuiz importQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcquiz_creator);
        initToolbar();
        helper = new DatabaseQuizHelper(this);
        db = helper.getWritableDatabase();
        save = findViewById(R.id.saveButton);
        mc = findViewById(R.id.mc);
        tf = findViewById(R.id.tf);
        nu = findViewById(R.id.nu);
        container = findViewById(R.id.container);
        importProgress = findViewById(R.id.importProgress);
        mcF = new mcFragment();
        tfF = new tfFragment();
        nuF = new nuFragment();
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        builder = new AlertDialog.Builder(this);
       goback = findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCQuizCreator.this, Switcher.class);

            }
        });

        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "mc";
                ft.replace(R.id.container, mcF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "tf";
                ft.replace(R.id.container, tfF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "nu";
                ft.replace(R.id.container, nuF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MCQuizCreator.this, QuizActivity.class);
                //Intent intent = new Intent(MCQuizCreator.this, DisplayQuiz.class);

                if (selectedType.equalsIgnoreCase("mc")) {
                    info = mcF.getData();
                    intent.putExtra("type", "mc");
                    intent.putExtra("question", info[0]);
                    intent.putExtra("ans1", info[1]);
                    intent.putExtra("ans2", info[2]);
                    intent.putExtra("ans3", info[3]);
                    intent.putExtra("ans4", info[4]);
                    intent.putExtra("correctAns", info[5]);
                } else if (selectedType.equalsIgnoreCase("tf")) {
                    info = tfF.getData();
                    intent.putExtra("type", "tf");
                    intent.putExtra("question", info[0]);
                    intent.putExtra("ans", info[1]);
                } else if (selectedType.equalsIgnoreCase("nu")) {
                    info = nuF.getData();
                    intent.putExtra("type", "nu");
                    intent.putExtra("question", info[0]);
                    intent.putExtra("ans", info[1]);
                   intent.putExtra("decimal", info[2]);
                }
                setResult(RESULT_OK, intent);
               // finish();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem statItem = menu.findItem(R.id.menu_stat);
        statItem.setVisible(false);
        menu.findItem(R.id.menu_help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(MCQuizCreator.this)
                        .setTitle("Help")
                        .setMessage("Activity developed by Doaa Aldhaheri " + "\n" +
                                "Version number: v1.0" + "\n" +
                                "First select a quiz type by clicking one of the buttons on top."
                                + "Then enter your questions and answers and click 'SAVE QUESTION'.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });


        menu.findItem(R.id.import_ques).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                importQuiz = new ImportQuiz();
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.dialog_new_message, null);
                builder.setView(dialogLayout);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        importQuiz.execute();
                        Toast.makeText(MCQuizCreator.this, "Quiz imported successfully", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Did not import quiz", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class ImportQuiz extends AsyncTask<String[], Integer, String[]> {
        HttpURLConnection conn;

        protected String[] doInBackground(String[]... strings) {
            try {
                URL url = new URL("http://torunski.ca/CST2335/QuizInstance.xml");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), "utf-8");
                int eventType = parser.getEventType();
                String tagName;
                ArrayList<String> result = new ArrayList<>();
                ArrayList<String> answers = new ArrayList<>();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    parser.next();
                    tagName = parser.getName();
                    if (tagName == null) {
                        tagName = "";
                    }
                    eventType = parser.getEventType();
                    if (eventType == START_TAG) {
                        if (tagName.equalsIgnoreCase("MultipleChoiceQuestion")) {
                            result.add(parser.getAttributeValue(null, "correct"));
                            result.add(parser.getAttributeValue(null, "question"));
                            onProgressUpdate(25);
                        } else if (tagName.equalsIgnoreCase("Answer")) {
                            answers.add(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("NumericQuestion")) {
                            result.add(parser.getAttributeValue(null, "accuracy"));
                            result.add(parser.getAttributeValue(null, "question"));
                            result.add(parser.getAttributeValue(null, "answer"));
                            onProgressUpdate(50);
                        } else if (tagName.equalsIgnoreCase("TrueFalseQuestion")) {
                            result.add(parser.getAttributeValue(null, "question"));
                            result.add(parser.getAttributeValue(null, "answer"));
                            onProgressUpdate(75);
                        }
                        onProgressUpdate(100);
                    } else if (eventType == END_TAG) {
                        if (tagName.equalsIgnoreCase("MultipleChoiceQuestion")) {
                            result.addAll(answers);
                            answers.clear();//now everything is in result
                        }
                    }
                }
                conn.disconnect();
                String[] finalResult = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    finalResult[i] = result.get(i);
                }
                return finalResult;
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            importProgress.setVisibility(View.VISIBLE);
            importProgress.setProgress(values[0]);
        }

        protected void onPostExecute(String[] result) {
            String mcQuestion, mcCorrect, mcAns1, mcAns2, mcAns3, mcAns4, nuDecimal, nuQuestion, nuAns, nuFormatedAns, tfQuestion, tfAns;
            ContentValues cv = new ContentValues();
            mcCorrect = result[0];
            mcQuestion = result[1];
            mcAns1 = result[2];
            mcAns2 = result[3];
            mcAns3 = result[4];
            mcAns4 = result[5];
            nuDecimal = result[6];
            nuQuestion = result[7];
            nuAns = result[8];
            nuFormatedAns = QuizActivity.formatStringNumber(nuAns, nuDecimal);
            tfQuestion = result[9];
            tfAns = result[10];
            cv.put(DatabaseQuizHelper.KEY_QUIZ, mcQuestion);
            cv.put(DatabaseQuizHelper.KEY_QUIZTP, "mc");
            cv.put(DatabaseQuizHelper.KEY_ANSWER1, mcAns1);
            cv.put(DatabaseQuizHelper.KEY_ANSWER2, mcAns2);
            cv.put(DatabaseQuizHelper.KEY_ANSWER3, mcAns3);
            cv.put(DatabaseQuizHelper.KEY_ANSWER4, mcAns4);
            cv.put(DatabaseQuizHelper.KEY_CORRECT_ANS, mcCorrect);
            db.insert("quizTable4", "NullColumn", cv);
            cv.clear();
            cv.put(DatabaseQuizHelper.KEY_QUIZ, nuQuestion);
            cv.put(DatabaseQuizHelper.KEY_QUIZTP, "nu");
            cv.put(DatabaseQuizHelper.KEY_ANSWER1, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER2, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER3, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER4, 0);
            cv.put(DatabaseQuizHelper.KEY_CORRECT_ANS, nuFormatedAns);
            db.insert("quizTable4", "NullColumn", cv);
            cv.clear();
            cv.put(DatabaseQuizHelper.KEY_QUIZ, tfQuestion);
            cv.put(DatabaseQuizHelper.KEY_QUIZTP, "tf");
            cv.put(DatabaseQuizHelper.KEY_ANSWER1, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER2, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER3, 0);
            cv.put(DatabaseQuizHelper.KEY_ANSWER4, 0);
            cv.put(DatabaseQuizHelper.KEY_CORRECT_ANS, tfAns);
            db.insert("quizTable4", "NullColumn", cv);
            importProgress.setVisibility(View.INVISIBLE);
        }
    }

}


