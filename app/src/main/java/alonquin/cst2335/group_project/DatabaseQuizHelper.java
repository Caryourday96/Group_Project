package alonquin.cst2335.group_project;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseQuizHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "myDatabase.db";
    static final int VERSION_NUM = 1;
    static final String TABLE_NAME ="quizTable4";
    static final String KEY_ID = "_ID";
    static final String KEY_QUIZ = "QUIZ";
    static final String KEY_QUIZTP = "QUIZTP";
    static final String KEY_ANSWER1 = "ANS1";
    static final String KEY_ANSWER2 = "ANS2";
    static final String KEY_ANSWER3 = "ANS3";
    static final String KEY_ANSWER4 = "ANS4";
    static final String KEY_CORRECT_ANS = "CORRECT_ANS";


    public DatabaseQuizHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_QUIZ + " TEXT not null," +
                KEY_QUIZTP + " TEXT," + KEY_ANSWER1 + " TEXT," + KEY_ANSWER2 + " TEXT," +
                KEY_ANSWER3 + " TEXT," + KEY_ANSWER4 + " TEXT," + KEY_CORRECT_ANS + " TEXT" + ");");
        Log.i("QuizDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("QuizDatabaseHelper", "Calling onUprade, oldVersion="
                + oldVer + "newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }
}