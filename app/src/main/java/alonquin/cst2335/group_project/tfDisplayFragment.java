package alonquin.cst2335.group_project;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class tfDisplayFragment extends Fragment {
    String choice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tf_display_fragment, container, false);
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.tfRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.selectTrue){
                    choice = "true";
                }
                else if (i == R.id.selectFalse){
                    choice = "false";
                }
            }
        });

        return view;
    }

    public String getChoice(){
        return choice;
    }

}
