package alonquin.cst2335.group_project;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Mydatabase extends SQLiteOpenHelper {


    public static final String NAME_OF_DATABASE = "Patients.db";
    public static final String TABLE_NAME = "patients_table";

    public static final String id = "ID";
    public static final String doctor_choice = "doctor_choice";
    public static final String name = "name";
    public static final String address = "address";
    public static final String birthday = "birthday";
    public static final String phoneNumber = "phone_number";
    public static final String healthCardNumber = "health_card_number";
    public static final String description = "description";
    public static final String addQuestion1 = "addQuestion1";
    public static final String addQuestion2 = "addQuestion2";


    public Mydatabase(Context context) {
        super(context, NAME_OF_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                doctor_choice + " TEXT, "
                + name + " TEXT," +
                address + " TEXT, " +
                birthday + " TEXT, " +
                phoneNumber + " TEXT, " +
                healthCardNumber + " TEXT, " +
                description + " TEXT, " +
                addQuestion1 + " TEXT, " +
                addQuestion2 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean InsertPatient(String drChoice, String Nm, String adr, String Bday, String pN, String hcN, String desc, String q1, String q2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(doctor_choice, drChoice);
        contentValues.put(name, Nm);
        contentValues.put(address, adr);
        contentValues.put(birthday, Bday);
        contentValues.put(phoneNumber, pN);
        contentValues.put(healthCardNumber, hcN);
        contentValues.put(description, desc);
        contentValues.put(addQuestion1, q1);
        contentValues.put(addQuestion2, q2);

        long result = db.insert(TABLE_NAME, null, contentValues);


        return result != -1;
        //     db.close();
    }


    public ArrayList<Patient> getAllStudent() {
        ArrayList<Patient> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String doctorChoice = c.getString(1);
                String nme = c.getString(2);
                String addrss = c.getString(3);
                String bday = c.getString(4);
                String pNumber = c.getString(5);
                String hcNumber = c.getString(6);
                String desc = c.getString(7);
                String q1 = c.getString(8);
                String q2 = c.getString(9);

                list.add(new Patient(id, doctorChoice, nme, addrss, bday, pNumber, hcNumber, desc, q1, q2));
            } while (c.moveToNext());
        }
        db.close();
        return list;
    }

    public void DeletePatient(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.delete(TABLE_NAME, id + "=?", new String[]{ID + ""});
        db.close();
        Log.d("Deleted", i + "");
    }

    public void UpdateData(int ID, String doctorC, String Nm, String adr, String bDay, String phN, String hcNumb, String desc, String qu1, String qu2) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(doctor_choice, doctorC);
        values.put(name, Nm);
        values.put(address, adr);
        values.put(birthday, bDay);
        values.put(phoneNumber, phN);
        values.put(healthCardNumber, hcNumb);
        values.put(description, desc);
        values.put(addQuestion1, qu1);
        values.put(addQuestion2, qu2);
        long i = db.update(TABLE_NAME, values, id + "=?", new String[]{ID + ""});
        Log.d("Update", i + "");

    }

}
