package alonquin.cst2335.group_project;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizActivity extends Toolbar {

    protected static final String ACTIVITY_NAME = "QuizMain";
    ListView listViewMain;
    Button newQuiz;
    ArrayList<String> quizMessage;
    ContentValues cv;
    static QuizAdapter quizAdapter;
    DatabaseQuizHelper helper;
    SQLiteDatabase db;
    Cursor c;
    String ans1, ans2, ans3, ans4, question, questionType, ans, decimal, formatedAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initToolbar();


         listViewMain = (ListView) findViewById(R.id.listviewMain);
              newQuiz = (Button) findViewById(R.id.newQuiz);
          quizMessage = new ArrayList<>();
                   cv = new ContentValues();
          quizAdapter = new QuizAdapter(this);
                listViewMain.setAdapter(quizAdapter);
                helper = new DatabaseQuizHelper(this);
                db = helper.getWritableDatabase();

                try {
                    helper.onCreate(db);
                } catch (SQLiteException e) {
                }

                c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID,
                                helper.KEY_QUIZ, helper.KEY_QUIZTP, helper.KEY_ANSWER1, helper.KEY_ANSWER2,
                                helper.KEY_ANSWER3, helper.KEY_ANSWER4, helper.KEY_CORRECT_ANS},
                        null, null, null, null, null, null);
                c.moveToFirst();
                if (c != null && c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        quizMessage.add(c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                        c.moveToNext();
                    }
                }
                Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
                for (int i = 0; i < c.getColumnCount(); i++) {
                    Log.i(ACTIVITY_NAME, c.getColumnName(i));
                }
                listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID,
                                        helper.KEY_QUIZ, helper.KEY_QUIZTP, helper.KEY_ANSWER1, helper.KEY_ANSWER2,
                                        helper.KEY_ANSWER3, helper.KEY_ANSWER4, helper.KEY_CORRECT_ANS},
                                null, null, null, null, null, null);
                        String type = quizAdapter.getQuizType(i);
                        Bundle bundle = new Bundle();
                        bundle.putLong("ID", quizAdapter.getItemId(i));
                        bundle.putInt("position", i);
                        bundle.putString("quiz", quizAdapter.getQuiz(i));
                        bundle.putString("correctAns", quizAdapter.getQuizAnswer(i));
                        bundle.putString("type", type);

                        if (type.equalsIgnoreCase("mc")) {
                            bundle.putString("ans1", quizAdapter.getAns1(i));
                            bundle.putString("ans2", quizAdapter.getAns2(i));
                            bundle.putString("ans3", quizAdapter.getAns3(i));
                            bundle.putString("ans4", quizAdapter.getAns4(i));
                        }
                        Intent displayQuiz = new Intent(QuizActivity.this, DisplayQuiz.class);
                        displayQuiz.putExtras(bundle);
                        startActivity(displayQuiz);
                    }
                });

                newQuiz.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent newQuiz = new Intent(QuizActivity.this, MCQuizCreator.class);
                        startActivityForResult(newQuiz, 1);
                    }
                });
            }

            @Override
            protected void onResume() {
                super.onResume();
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 1) {
                    if (resultCode == RESULT_OK) {
                        questionType = data.getStringExtra("type");
                        question = data.getStringExtra("question");
                        quizMessage.add(question);
                        ContentValues cv = new ContentValues();
                        if (questionType.equalsIgnoreCase("mc")) {
                            ans1 = data.getStringExtra("ans1");
                            ans2 = data.getStringExtra("ans2");
                            ans3 = data.getStringExtra("ans3");
                            ans4 = data.getStringExtra("ans4");
                            ans = data.getStringExtra("correctAns");
                            cv.put(helper.KEY_QUIZ, question);
                            cv.put(helper.KEY_QUIZTP, questionType);
                            cv.put(helper.KEY_ANSWER1, ans1);
                            cv.put(helper.KEY_ANSWER2, ans2);
                            cv.put(helper.KEY_ANSWER3, ans3);
                            cv.put(helper.KEY_ANSWER4, ans4);
                            cv.put(helper.KEY_CORRECT_ANS, ans);
                        } else if (questionType.equalsIgnoreCase("tf")) {
                            ans = data.getStringExtra("ans");
                            cv.put(helper.KEY_QUIZ, question);
                            cv.put(helper.KEY_QUIZTP, questionType);
                            cv.put(helper.KEY_ANSWER1, 0);
                            cv.put(helper.KEY_ANSWER2, 0);
                            cv.put(helper.KEY_ANSWER3, 0);
                            cv.put(helper.KEY_ANSWER4, 0);
                            cv.put(helper.KEY_CORRECT_ANS, ans);
                        } else if (questionType.equalsIgnoreCase("nu")) {
                            ans = data.getStringExtra("ans");
                            decimal = data.getStringExtra("decimal");
                            formatedAns = formatStringNumber(ans, decimal);
                            cv.put(helper.KEY_QUIZ, question);
                            cv.put(helper.KEY_QUIZTP, questionType);
                            cv.put(helper.KEY_ANSWER1, 0);
                            cv.put(helper.KEY_ANSWER2, 0);
                            cv.put(helper.KEY_ANSWER3, 0);
                            cv.put(helper.KEY_ANSWER4, 0);
                            cv.put(helper.KEY_CORRECT_ANS, formatedAns);
                        }
                        db.insert(helper.TABLE_NAME, "null Replacement Value", cv);
                        quizAdapter.notifyDataSetChanged();
                        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID,
                                        helper.KEY_QUIZ, helper.KEY_QUIZTP, helper.KEY_ANSWER1, helper.KEY_ANSWER2,
                                        helper.KEY_ANSWER3, helper.KEY_ANSWER4, helper.KEY_CORRECT_ANS},
                                null, null, null, null, null, null);
                        c.moveToFirst();
                    }
                }
            }

            public static String formatStringNumber(String ans, String decimal) {
                float number = Float.parseFloat(ans);
                int numberDecimal = Integer.parseInt(decimal);
                String formatedAnswer;
                switch (numberDecimal) {
                    case 0:
                        formatedAnswer = String.format("%.0f", number);
                        break;
                    case 1:
                        formatedAnswer = String.format("%.1f", number);
                        break;
                    case 2:
                        formatedAnswer = String.format("%.2f", number);
                        break;
                    case 3:
                        formatedAnswer = String.format("%.3f", number);
                        break;
                    case 4:
                        formatedAnswer = String.format("%.4f", number);
                        break;
                    case 5:
                        formatedAnswer = String.format("%.5f", number);
                        break;
                    default:
                        formatedAnswer = String.format("%.0f", number);
                        break;
                }
                return formatedAnswer;
            }

            public String getMaxQuestionLength(){
                Cursor cursor1 = db.rawQuery("SELECT MAX(LENGTH(QUIZ)) AS max FROM "+ helper.TABLE_NAME,null);
                cursor1.moveToFirst();
                return cursor1.getString(cursor1.getColumnIndex("max"));
            }

            public String getMinQuestionLength(){
                Cursor cursor2 = db.rawQuery("SELECT MIN(LENGTH(QUIZ))AS min FROM "+ helper.TABLE_NAME,null);
                cursor2.moveToFirst();
                return cursor2.getString(cursor2.getColumnIndex("min"));
            }

            public String getAvgQuestionLength(){
                Cursor cursor3 = db.rawQuery("SELECT AVG(LENGTH(QUIZ))AS average FROM "+ helper.TABLE_NAME,null);
                cursor3.moveToFirst();
                return cursor3.getString(cursor3.getColumnIndex("average"));
            }

            @Override
            public boolean onPrepareOptionsMenu(Menu menu) {
                MenuItem importItem = (MenuItem) menu.findItem(R.id.import_ques);
                importItem.setVisible(false);
                MenuItem statsItem = (MenuItem) menu.findItem(R.id.menu_stat);

                statsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String maxLength = getMaxQuestionLength();
                        String minLength = getMinQuestionLength();
                        String avgLength = getAvgQuestionLength();

                        new AlertDialog.Builder(QuizActivity.this)
                                .setTitle("Statistics")
                                .setMessage("The longest question has " + maxLength + " characters" + "\n" +
                                        "The shortest question has " + minLength + " characters" + "\n" +
                                        "The average questions have " + avgLength + " characters" )
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        return true;
                    }
                });

                menu.findItem(R.id.menu_help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        new AlertDialog.Builder(QuizActivity.this)
                                .setTitle("Help")
                                .setMessage("Activity developed by Can Shi " + "\n" +
                                        "Version number: v1.0" + "\n" +
                                        "This activity is designed to create 3 types of quizzes, " +
                                        "multiple choice, true or false, and numeric " +
                                        "You can view, add, update and delete quiz records. " +
                                        "You can import multiple quiz records from the Internet. ")
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

            @Override
            public void onDestroy() {
                super.onDestroy();
                db.close();
            }

            public class QuizAdapter extends ArrayAdapter<String> {
                QuizAdapter(Context ctx) {
                    super(ctx, 0);
                }

                public int getCount() {
                    return quizMessage.size();
                }

                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = QuizActivity.this.getLayoutInflater();
                    View result = null;
                    result = inflater.inflate(R.layout.quiz_row, null);

                    TextView quiz_row = (TextView) result.findViewById(R.id.quizRow);
                    quiz_row.setText(quizMessage.get(position));
                    return result;
                }

                public long getId(int position) {
                    return position;
                }

                public long getItemId(int position) {
                    c.moveToPosition(position);
                    return c.getLong(c.getColumnIndex(helper.KEY_ID));
                }

                public String getQuiz(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_QUIZ));
                }

                public String getQuizType(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_QUIZTP));
                }

                public String getQuizAnswer(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_CORRECT_ANS));
                }

                public String getAns1(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_ANSWER1));
                }

                public String getAns2(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_ANSWER2));
                }

                public String getAns3(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_ANSWER3));
                }

                public String getAns4(int position) {
                    c.moveToPosition(position);
                    return c.getString(c.getColumnIndex(helper.KEY_ANSWER4));
                }

            }

        }