package alonquin.cst2335.group_project;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class mcDisplayFragment extends Fragment {
    TextView ans1, ans2, ans3, ans4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mc_display_fragment, container, false);
        ans1 = view.findViewById(R.id.Answer1);
        ans2 = view.findViewById(R.id.Answer2);
        ans3 = view.findViewById(R.id.Answer3);
        ans4 = view.findViewById(R.id.Answer4);
        String answer1 = getArguments().getString("ans1");
        String answer2 = getArguments().getString("ans2");
        String answer3 = getArguments().getString("ans3");
        String answer4 = getArguments().getString("ans4");
        ans1.setText(answer1);
        ans2.setText(answer2);
        ans3.setText(answer3);
        ans4.setText(answer4);
        return view;
    }

}