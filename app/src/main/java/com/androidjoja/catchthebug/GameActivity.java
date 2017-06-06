package com.androidjoja.catchthebug;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends Activity {

    private boolean gameIsRunning;
    private int round;
    private int points;
    private int bugs;
    private int caughtBugs;
    private int time;
    private float scale;
    private Random randomGenerator = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        scale = getResources().getDisplayMetrics().density;
        startGame();
    }

    private void startGame() {
        gameIsRunning = true;
        round = 0;
        points = 0;
        startRound();
    }

    private void startRound() {
        bugs = 0;
        round += 1;
        bugs = round * 10;
        caughtBugs = 0;
        time = 60;
        refreshScreen();
    }

    private void refreshScreen() {
        TextView tvPoints = (TextView) findViewById(R.id.points_text);
        tvPoints.setText(Integer.toString(points));

        TextView tvRound = (TextView) findViewById(R.id.round_text);
        tvRound.setText(Integer.toString(round));

        TextView tvHits = (TextView) findViewById(R.id.hits);
        tvHits.setText(Integer.toString(caughtBugs));

        TextView tvTime = (TextView) findViewById(R.id.time);
        tvTime.setText(Integer.toString(time));

        FrameLayout flHits =(FrameLayout) findViewById(R.id.bar_hits);
        FrameLayout flTime =(FrameLayout) findViewById(R.id.bar_time);

        ViewGroup.LayoutParams lpHits = flHits.getLayoutParams();
        lpHits.width = Math.round(scale * 300 * Math.min(caughtBugs, bugs) / bugs);

        ViewGroup.LayoutParams lpTime = flTime.getLayoutParams();
        lpTime.width = Math.round(scale * time * 300  / 60);

    }

    private void countdown() {
        time = time - 1;
        float randomDigit = randomGenerator.nextFloat();
        double probability = bugs * 1.5;
        if(probability > 1) {
            showBug();
            if(randomDigit < probability - 1) {
                showBug();
            }
        } else {
            if(randomDigit < probability) {
                showBug();
            }
        }
    }

    private void showBug() {

    }
}
