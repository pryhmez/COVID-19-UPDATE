package com.example.covid_19_update;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.covid_19_update.ui.emergencylines.EmergencyLines;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiagnoseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiagnoseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    QuestionsAdapter adapter;

    List<Question> list = new ArrayList<>();
    private Hotlines questions = new Hotlines();
    private Button btnCheck, btnCall, btnCancel;
    private TextView tv_percentage, tv_comment;
    private  String avg, comment;
    private Dialog myDialog;

    public DiagnoseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiagnoseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiagnoseFragment newInstance(String param1, String param2) {
        DiagnoseFragment fragment = new DiagnoseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_diagnose, container, false);

        btnCheck = root.findViewById(R.id.btn_check);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_fruits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String[] questionsArr = questions.getQuestionsArr();
        String[] pointArr = questions.getPointsArr();


        for (int i=0;i<questionsArr.length;i++){

            Question quest = new Question(questionsArr[i],pointArr[i],false);
            list.add(quest);
        }

//        Log.d("darule", String.valueOf(list));
        adapter = new QuestionsAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionsAdapter myadapter = new QuestionsAdapter(getActivity(), list);
                 avg = String.valueOf(myadapter.getAverage());
                Log.d("chef", avg);
                showDialog();
            }
        });

        return root;
    }


    public void showDialog() {
        myDialog = new Dialog(getActivity());
//        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.score_dialog);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setTitle("RESULT");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnCall = myDialog.findViewById(R.id.btn_call);
        btnCancel = myDialog.findViewById(R.id.btn_cancel);
        tv_comment = myDialog.findViewById(R.id.tv_comment);
        tv_percentage = myDialog.findViewById(R.id.tv_testpercentage);
        int noAvg = Integer.parseInt(avg);

        if(noAvg <= 20) {
            comment = "Your symptoms are mild, please self isolate. If you are not yet satisfied click on the call button to speak with health professionals";
        }else if(noAvg <= 50 && noAvg > 20) {
            comment = "You need to get tested for COVID-19. If you are unable to test at the moment, please self isolate. Call emergency now";
        }else if(noAvg <= 70 && noAvg > 50) {
            comment = "You need to get tested for COVID-19. If you are unable to test at the moment, please self isolate. Proceed to call emergency";
        }else {
            btnCancel.setVisibility(View.GONE);
            comment = "You are advised to call emergency urgently in order to get tested";
        }

        tv_percentage.setText(avg + "%");
        tv_comment.setText(comment);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                EmergencyLines nextFrag= new EmergencyLines();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.diagnoses, nextFrag, "findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_emergencylines);
                myDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.cancel();
            }
        });
        myDialog.show();
    }
}


