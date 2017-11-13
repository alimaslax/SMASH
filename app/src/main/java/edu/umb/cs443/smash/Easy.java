package edu.umb.cs443.smash;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class Easy extends Fragment {

    int color;
    public static long timer, inc;
    public long sco = 0;
    public int a[];
    //array of buttons
    public Button grid[];
    public TextView time, score;
    public Random r;
    final int[] count = {0};
    public CountDownTimer countDownTimer;
    public Typeface tf;
    public MediaPlayer mp;

    public Vibrator buzz;
    public View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.easy_game, container, false);
        mp = MediaPlayer.create(getContext(), R.raw.sound);
        timer = 20000;
        inc = 1000;

        //Loads animation effects from xml files
        final Animation anim_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        final Animation anim_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        //Array of 16 buttons
        grid = new Button[16];

        a = new int[16];
        r = new Random();

        //Set Font to True_North
        tf = Typeface.createFromAsset(getContext().getAssets(), "true_north.ttf");

        //Get the SystemService for viberation
        buzz = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //Load the Relative Layout for the pause button
        final RelativeLayout p = (RelativeLayout) v.findViewById(R.id.pause);

        //Load  ImageView buttons
        final ImageView home = (ImageView) v.findViewById(R.id.ic_home);
        final ImageView play = (ImageView) v.findViewById(R.id.ic_play);
        ImageView pause = (ImageView) v.findViewById(R.id.ic_pause);

        //Disables board and shows hidden views
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableBoard();
                play.setEnabled(true);
                home.setEnabled(true);
                play.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);
                p.startAnimation(anim_in);
                onPause();
            }
        });

        //Enable board and set views to hidden
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBoard();
                play.setEnabled(false);
                home.setEnabled(false);
                play.setVisibility(View.GONE);
                p.startAnimation(anim_out);
                p.setVisibility(View.GONE);
                onResume();
            }
        });

        //Replace the Activity with a new Menu
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Menu())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        //initialize all variables
        initTimer();
        initButton();
        initBoard();
        startGame();

        return v;
    }

    //Array of Buttons disabled
    public void disableBoard() {
        for (int i = 0; i < grid.length; i++)
            grid[i].setEnabled(false);
    }

    //Array of Buttons enabled
    public void enableBoard() {
        for (int i = 0; i < grid.length; i++)
            grid[i].setEnabled(true);
    }

    //Random Color is chosen and the array of buttons are initialized
    private void initButton() {
        for (int i = 0; i < grid.length; i++) {
            a[i] = r.nextInt(4);
        }
        color = r.nextInt(4);

        grid[0] = (Button) v.findViewById(R.id.btn1);
        grid[1] = (Button) v.findViewById(R.id.btn2);
        grid[2] = (Button) v.findViewById(R.id.btn3);
        grid[3] = (Button) v.findViewById(R.id.btn4);
        grid[4] = (Button) v.findViewById(R.id.btn5);
        grid[5] = (Button) v.findViewById(R.id.btn6);
        grid[6] = (Button) v.findViewById(R.id.btn7);
        grid[7] = (Button) v.findViewById(R.id.btn8);
        grid[8] = (Button) v.findViewById(R.id.btn9);
        grid[9] = (Button) v.findViewById(R.id.btn10);
        grid[10] = (Button) v.findViewById(R.id.btn11);
        grid[11] = (Button) v.findViewById(R.id.btn12);
        grid[12] = (Button) v.findViewById(R.id.btn13);
        grid[13] = (Button) v.findViewById(R.id.btn14);
        grid[14] = (Button) v.findViewById(R.id.btn15);
        grid[15] = (Button) v.findViewById(R.id.btn16);

    }

    //Randomly picks a color for each button in array
    private void initBoard() {

        for (int i = 0; i < grid.length; i++) {
            switch (a[i]) {
                case 0:
                    grid[i].setBackground(getResources().getDrawable(R.drawable.blue,null));
                    break;
                case 1:
                    grid[i].setBackground(getResources().getDrawable(R.drawable.red,null));
                    break;
                case 2:
                    grid[i].setBackground(getResources().getDrawable(R.drawable.orange,null));
                    break;
                case 3:
                    grid[i].setBackground(getResources().getDrawable(R.drawable.green,null));
                    break;
            }
        }

        ImageView c_indi = (ImageView) v.findViewById(R.id.indi_color);

        switch (color) {
            case 0:
                c_indi.setImageDrawable(getResources().getDrawable(R.drawable.blue,null));
                break;
            case 1:
                c_indi.setImageDrawable(getResources().getDrawable(R.drawable.red,null));
                break;
            case 2:
                c_indi.setImageDrawable(getResources().getDrawable(R.drawable.orange,null));
                break;
            case 3:
                c_indi.setImageDrawable(getResources().getDrawable(R.drawable.green,null));
                break;
        }

        score = (TextView) v.findViewById(R.id.score);
        score.setText(Long.toString(sco));
        score.setTypeface(tf);
        time = (TextView) v.findViewById(R.id.time);
        time.setTypeface(tf);
    }


    private void startGame() {
        //Onclick Listener for all buttons in array
        for (int i = 0; i < grid.length; ++i) {
            final int finalI = i;
            grid[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if same color take the button out the list
                    if (a[finalI] == color) {
                        mp.start();
                        grid[finalI].setVisibility(View.INVISIBLE);
                        a[finalI] = 10;
                    } else {
                        //if different color vibrate
                        buzz.vibrate(200);
                        if (timer != 0)
                            countDownTimer.cancel();
                        timer -= 3000;
                        initTimer();
                    }
                    count[0]++;
                    check();
                    mp.stop();
                }
            });
        }

    }

    //Check if no more buttons on board
    private void check() {
        int j = 0;

        for (int i = 0; i < grid.length; i++) {
            if (a[i] != color)
                j++;
        }

        //Add 16 to score and restart the game
        if (j == 16) {
            sco = sco + count[0];
            if (timer != 0)
                countDownTimer.cancel();
            timer += 1000;
            initTimer();
            score.setText(Long.toString(sco));
            initVisibility();
            initButton();
            initBoard();
            startGame();
        }
    }

    // timer
    private void initTimer() {
        countDownTimer = new CountDownTimer(timer, inc) {
            @Override
            public void onTick(long t) {
                time.setText(Long.toString(t / 1000));
                timer = t;
            }

            @Override
            public void onFinish() {
                stopGame(sco);
            }
        }.start();
    }


    //Make all buttons visible
    private void initVisibility() {
        for (int i = 0; i < grid.length; i++)
            grid[i].setVisibility(View.VISIBLE);
    }

    //Replace Current Activity with GameOver
    private void stopGame(long score) {

        GameOver g = new GameOver();
        //Pass on the Level and The score
        g.setLevel(1);
        g.setCscore(score);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, g)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    @Override
    public void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (timer != 0)
            countDownTimer.cancel();
        initTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}

