package edu.umb.cs443.smash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class HighScore extends Fragment {

    long easy_score, hard_score;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scores, container, false);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "true_north.ttf");
        //Get Saved Data
        SharedPreferences sPref = getContext().getSharedPreferences("SMASH", Context.MODE_PRIVATE);

        //Load Saved Data
        easy_score = sPref.getLong("EasyScore",0);
        hard_score = sPref.getLong("HardScore",0);

        //Set Views to corressponding ViewIDs
        ImageView home = (ImageView) v.findViewById(R.id.home);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView easy = (TextView) v.findViewById(R.id.easy);
        TextView hard = (TextView) v.findViewById(R.id.hard);

        title.setTypeface(tf);
        easy.setTypeface(tf);
        hard.setTypeface(tf);

        easy.setText("Easy : " + easy_score);
        hard.setText("Hard : " + hard_score);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Menu())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return v;
    }
}
