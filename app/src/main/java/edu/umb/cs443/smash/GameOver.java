package edu.umb.cs443.smash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOver extends Fragment {

    //scores
    long hscore,cscore;
    int level;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_over, container, false);

        //New Preferences with an editor
        final SharedPreferences sPref = getContext().getSharedPreferences("SMASH", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sPref.edit();

        //get the previous score based on level
        switch (level){
            case 1:
                hscore = sPref.getLong("EasyScore",0);
                break;
            case 2:
                hscore = sPref.getLong("HardScore",0);
                break;
        }

        //If the score is higher than before replace it
        if (cscore > hscore) {
            switch (level){
                case 1:
                    editor.putLong("EasyScore", cscore);
                    editor.commit();
                    break;
                case 2:
                    editor.putLong("HardScore", cscore);
                    editor.commit();
                    break;
            }
            hscore = cscore;
        }

        if(hscore==0)
            hscore=cscore;

        //Set the Fonts to True_north
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "true_north.ttf");

        //Display Scores
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTypeface(tf);
        TextView score = (TextView) v.findViewById(R.id.score);
        score.setTypeface(tf);
        TextView h = (TextView) v.findViewById(R.id.h_score);
        h.setTypeface(tf);
        TextView sc = (TextView) v.findViewById(R.id.curr_score);
        sc.setText(Long.toString(cscore));
        sc.setTypeface(tf);
        TextView hsc = (TextView) v.findViewById(R.id.high_score);
        hsc.setText(Long.toString(hscore));
        hsc.setTypeface(tf);

        ImageView replay = (ImageView) v.findViewById(R.id.replay);

        //Get the current level being played and replay it
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (level){
                    case 1:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new Easy())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                        break;
                    case 2:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new Hard())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                        break;
                }
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

    public void setLevel(int l){
        level = l;
    }

    public void setCscore(long score){
        cscore = score;
    }
}
