package alonquin.cst2335.group_project;


        import android.os.Bundle;
        import android.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.RadioGroup;

public class mcFragment extends Fragment {

    String question,answer1,answer2,answer3,answer4, correctAns;
    EditText questionFiled;
    EditText answerField1;
    EditText answerField2;
    EditText answerField3;
    EditText answerField4;
    RadioGroup correctAnswer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_mc_fragment, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        answerField1 = (EditText)view.findViewById(R.id.enterAnswer1);
        answerField2 = (EditText)view.findViewById(R.id.enterAnswer2);
        answerField3 = (EditText)view.findViewById(R.id.enterAnswer3);
        answerField4 = (EditText)view.findViewById(R.id.enterAnswer4);
        correctAnswer = (RadioGroup)view.findViewById(R.id.correctAnswerIs);
        correctAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup correctAnswer, int i) {
                if(i == R.id.chooseA){
                    correctAns = "a";
                }
                else if (i == R.id.chooseB){
                    correctAns = "b";
                }
                else if (i == R.id.chooseC){
                    correctAns = "c";
                }
                else if (i == R.id.chooseD){
                    correctAns = "d";
                }
            }
        });
        return view;
    }

    public String[] getData(){
        String[] data = new String[6];
        question = questionFiled.getText().toString();
        answer1 = answerField1.getText().toString();
        answer2 = answerField2.getText().toString();
        answer3 = answerField3.getText().toString();
        answer4 = answerField4.getText().toString();

        data[0] = question;
        data[1] = answer1;
        data[2] = answer2;
        data[3] = answer3;
        data[4] = answer4;
        data[5] = correctAns;

        return data;
    }

}