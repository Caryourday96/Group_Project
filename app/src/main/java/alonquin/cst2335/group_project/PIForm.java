package alonquin.cst2335.group_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class PIForm extends AppCompatActivity {

    Mydatabase mydatabase;
    ArrayList<Patient> list = new ArrayList<>();
    Custom adapter;


    RadioGroup radioGroup;
    RadioButton KLradioBdoctorOffice;
    RadioButton KLradioBDentist;
    RadioButton KLradioBOptom;

    String strDoctorChoice = "";
    String addQestion1 = "";
    String addQestion2 = "";

    EditText sergeries;
    EditText allergies;

    EditText glassesDate;
    EditText glassesStore;

    CheckBox yesBraces;
    CheckBox noBraces;
    CheckBox yesBenefits;
    CheckBox noBenefits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piform);

        mydatabase = new Mydatabase(getApplication());

        final EditText edtName = findViewById(R.id.edtName);
        final EditText edtAddress = findViewById(R.id.edtAddress);
        final EditText edtBirthday = findViewById(R.id.edtBirthday);
        final EditText editPhoneNumber = findViewById(R.id.edtPhoneNumber);
        final EditText edtHealthCard = findViewById(R.id.edtHealthCard);
        final EditText edtDesc = findViewById(R.id.edtDesc);

        sergeries = findViewById(R.id.sergeries);
        allergies = findViewById(R.id.allergies);
        glassesDate = findViewById(R.id.glassesDate);
        glassesStore = findViewById(R.id.glassesStore);
        yesBraces = findViewById(R.id.checkBoxYesBraces);
        noBraces = findViewById(R.id.checkBoxNoBraces);
        yesBenefits = findViewById(R.id.checkBoxYesBenefits);
        noBenefits = findViewById(R.id.checkBoxNoBenefits);

        KLradioBdoctorOffice = findViewById(R.id.checkBoxDocOffice);
        KLradioBDentist = findViewById(R.id.checkBoxDent);
        KLradioBOptom = findViewById(R.id.checkBoxOptom);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (group.getCheckedRadioButtonId()) {
                    case R.id.checkBoxDocOffice:
                        strDoctorChoice = "Doctor's Office";
                        break;

                    case R.id.checkBoxOptom:
                        strDoctorChoice = "Optometrist";
                        break;

                    case R.id.checkBoxDent:
                        strDoctorChoice = "Dentist";
                        break;
                }
            }
        });


        KLradioBdoctorOffice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PIForm.this);
                LayoutInflater inflater = PIForm.this.getLayoutInflater();
                View inflated = inflater.inflate(R.layout.form_elements, null);
                allergies = inflated.findViewById(R.id.allergies);
                sergeries = inflated.findViewById(R.id.sergeries);
                builder.setView(inflated)

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                addQestion1 = sergeries.getText().toString();
                                addQestion2 = allergies.getText().toString();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });

        KLradioBOptom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PIForm.this);
                LayoutInflater inflater = PIForm.this.getLayoutInflater();
                View inflated = inflater.inflate(R.layout.form_elements_opt, null);
                glassesDate = inflated.findViewById(R.id.glassesDate);
                glassesStore = inflated.findViewById(R.id.glassesStore);

                builder.setView(inflated)

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                addQestion1 = glassesDate.getText().toString();
                                addQestion2 = glassesStore.getText().toString();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });

        KLradioBDentist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PIForm.this);
                LayoutInflater inflater = PIForm.this.getLayoutInflater();

                View inflated = inflater.inflate(R.layout.form_elements_dent, null);
                yesBenefits = inflated.findViewById(R.id.checkBoxYesBenefits);
                noBenefits = inflated.findViewById(R.id.checkBoxNoBenefits);
                yesBraces = inflated.findViewById(R.id.checkBoxYesBraces);
                noBraces = inflated.findViewById(R.id.checkBoxNoBraces);


                builder.setView(inflater.inflate(R.layout.form_elements_dent, null))

                        // action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                yesBraces.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
                                        Toast.makeText(getBaseContext(), "CheckBox is checked", Toast.LENGTH_SHORT).show();

                                        addQestion1 = "Yes Braces";
                                    }
                                });

                                yesBraces.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
                                        addQestion1 = "No Braces";
                                    }
                                });

                                yesBenefits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
                                        addQestion2 = "Yes Benefits";
                                    }
                                });

                                yesBenefits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
                                        addQestion2 = "No Benefits";
                                    }
                                });


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });


        ListView listView = findViewById(R.id.listview);
        Button btnThem = findViewById(R.id.btnThem);

        btnThem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = edtName.getText().toString();
                String strAddress = edtAddress.getText().toString();
                String strBday = edtBirthday.getText().toString();
                String strPNumber = editPhoneNumber.getText().toString();
                String strHealthCard = edtHealthCard.getText().toString();
                String strDesc = edtDesc.getText().toString();

                boolean isInserted = mydatabase.InsertPatient(strDoctorChoice, strName, strAddress,
                        strBday, strPNumber, strHealthCard, strDesc, addQestion1, addQestion2);

                if (isInserted) {
                    Toast.makeText(PIForm.this, "Patient is inserted", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(PIForm.this, "Patient IS NOT inserted", Toast.LENGTH_LONG).show();

                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });
        list = mydatabase.getAllStudent();
        adapter = new Custom(getApplication(), list);
        listView.setAdapter(adapter);
    }
}