package alonquin.cst2335.group_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

//references: https://www.journaldev.com/9629/android-progressbar-example

public class Update extends AppCompatActivity {
    Mydatabase db;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        db = new Mydatabase(getApplication());
        Intent intent = getIntent();

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus + "/" + progressBar.getMax());
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        final int id = intent.getIntExtra("ID", 1);
        final String doctorChoice = intent.getStringExtra("doctor_choice");
        final String name = intent.getStringExtra("name");
        final String address = intent.getStringExtra("address");
        final String birthday = intent.getStringExtra("birthday");
        final String phoneNumber = intent.getStringExtra("phone_number");
        final String healthCardN = intent.getStringExtra("health_card_number");
        final String desc = intent.getStringExtra("description");
        final String q1 = intent.getStringExtra("addQuestion1");
        final String q2 = intent.getStringExtra("addQuestion2");

        EditText edtid = findViewById(R.id.editText);
        final EditText etDoctorChoice = findViewById(R.id.editText2);
        final EditText etName = findViewById(R.id.editText3);
        final EditText etAddress = findViewById(R.id.editText4);
        final EditText etBirthday = findViewById(R.id.editText5);
        final EditText etPhoneNumber = findViewById(R.id.editText6);
        final EditText etHealthCardNumber = findViewById(R.id.editText7);
        final EditText etDesc = findViewById(R.id.editText8);
        final EditText etq1 = findViewById(R.id.editText9);
        final EditText etq2 = findViewById(R.id.editText10);

        edtid.setText(id + "");
        etDoctorChoice.setText(doctorChoice);
        etName.setText(name);
        etAddress.setText(address);
        etBirthday.setText(birthday);
        etPhoneNumber.setText(phoneNumber);
        etHealthCardNumber.setText(healthCardN);
        etDesc.setText(desc);
        etq1.setText(q1);
        etq2.setText(q2);
        edtid.setEnabled(false);

        Button btnupdate = findViewById(R.id.btnUpdate);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = etName.getText().toString();
                String newDChoice = etDoctorChoice.getText().toString();
                String newAddress = etAddress.getText().toString();
                String newBday = etBirthday.getText().toString();
                String newPhoneNumber = etPhoneNumber.getText().toString();
                String newHealthCard = etHealthCardNumber.getText().toString();
                String newDesc = etDesc.getText().toString();
                String newQ1 = etq1.getText().toString();
                String newQ2 = etq2.getText().toString();

                db.UpdateData(id, newDChoice, newName, newAddress, newBday, newPhoneNumber,
                        newHealthCard, newDesc, newQ1, newQ2);

                Intent myIntent = new Intent(Update.this, PIForm.class);
                startActivity(myIntent);

            }
        });
    }
}