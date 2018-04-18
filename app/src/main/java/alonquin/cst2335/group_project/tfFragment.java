package alonquin.cst2335.group_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class tfFragment extends Fragment {

    String question,answer;
    EditText questionFiled;
    RadioGroup radioGroup;

    public tfFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_tf_fragment, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        radioGroup = (RadioGroup)view.findViewById(R.id.RGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.trueAns){
                    answer = "true";
                }
                else if (i == R.id.falseAns){
                    answer = "false";
                }
            }
        });
        return view;
    }

    public String[] getData(){
        String[] data = new String[2];
        question = questionFiled.getText().toString();
        data[0] = question;
        data[1] = answer;
        return data;
    }

}