package edu.umb.cs443.smash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Menu extends Fragment {

    public static boolean isPlaying;
    public boolean music;

    ImageView easy, hard, sound, score, help, bckground;
    MediaPlayer mp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get xml layout
        View v = inflater.inflate(R.layout.menu, container, false);

        final SharedPreferences sPref = getContext().getSharedPreferences("SMASH", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sPref.edit();

        final Animation anim_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        final Animation anim_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        music = sPref.getBoolean("MUSIC", true);
        isPlaying = music;
        mp = MediaPlayer.create(getContext(), R.raw.back_music);
        if(music) {
            mp.start();
            mp.setLooping(true);
            mp.setVolume(100, 100);
        }

        //Set each ImageView with corresponding ViewID
        TextView title = (TextView) v.findViewById(R.id.title);
        sound = (ImageView) v.findViewById(R.id.volume);
        easy = (ImageView) v.findViewById(R.id.ic_easy);
        hard = (ImageView) v.findViewById(R.id.ic_med);
        score = (ImageView) v.findViewById(R.id.high_score);
        help = (ImageView) v.findViewById(R.id.info);
        bckground = (ImageView) v.findViewById(R.id.r_image);
        final TextView in_1 = (TextView) v.findViewById(R.id.t_1);
        final TextView in_2 = (TextView) v.findViewById(R.id.t_2);
        //Get the Relavtive layout from xml
        final RelativeLayout r_info = (RelativeLayout) v.findViewById(R.id.r_info);

        //Set the font
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "true_north.ttf");
        title.setTypeface(tf);
        in_1.setTypeface(tf);
        in_2.setTypeface(tf);

        //Replace Activity with new Easy_Game
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Easy())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        //Replace Activity with new Hard Game
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Hard())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        // Turns Music on and off Respectivtly changing the Imageview as Well
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    sound.setImageDrawable(getResources().getDrawable(R.drawable.sound_off,null));
                    mp.pause();
                    isPlaying = false;
                    editor.putBoolean("MUSIC", false);
                    editor.commit();
                } else {
                    sound.setImageDrawable(getResources().getDrawable(R.drawable.sound_on,null));
                    mp.start();
                    mp.setLooping(true);
                    mp.setVolume(100, 100);
                    isPlaying = true;
                    editor.putBoolean("MUSIC",true);
                    editor.commit();
                }
            }
        });

        if (!isPlaying) {
            sound.setImageDrawable(getResources().getDrawable(R.drawable.sound_off,null));

        } else {
            sound.setImageDrawable(getResources().getDrawable(R.drawable.sound_on,null));
        }

        //Replace Acitivty with new HighScore

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HighScore())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        //Sets bckground to black and displays hidden text
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                bckground.setEnabled(true);
                bckground.setVisibility(View.VISIBLE);
                in_1.setVisibility(View.VISIBLE);
                in_2.setVisibility(View.VISIBLE);
                r_info.setVisibility(View.VISIBLE);
                r_info.startAnimation(anim_in);
            }
        });

        //Action Listener to quit Help if anywhere is pressed
        bckground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableButtons();
                bckground.setEnabled(false);
                r_info.startAnimation(anim_out);
                bckground.setVisibility(View.INVISIBLE);
            }
        });
        return v;
    }

    //Disable all buttons
    public void disableButtons(){
        easy.setEnabled(false);
        hard.setEnabled(false);
        score.setEnabled(false);
        sound.setEnabled(false);
        help.setEnabled(false);
    }

    //Enable all buttons
    public void enableButtons(){
        easy.setEnabled(true);
        hard.setEnabled(true);
        score.setEnabled(true);
        sound.setEnabled(true);
        help.setEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }
}
