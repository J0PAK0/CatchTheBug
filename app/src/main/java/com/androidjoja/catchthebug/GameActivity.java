package com.androidjoja.catchthebug;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener, Runnable {

    private boolean gameIsRunning;
    private int round;
    private int points;
    private int bugs;
    private int caughtBugs;
    private int time;
    private float scale;
    private Random randomGenerator = new Random();
    private ViewGroup gameArea;
    private static final long MAXAGE_BUG = 2000;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startGame();
        scale = getResources().getDisplayMetrics().density;
        gameArea = (ViewGroup) findViewById(R.id.game_area);
    }

    @Override
    public void onClick(View bug) {
        caughtBugs++;
        points += 100;
        refreshScreen();
        gameArea.removeView(bug);
    }

    @Override
    public void run() {
        countdown();
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
        handler.postDelayed(this, 1000);
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

        removeBugs();
        refreshScreen();
        if(!checkEndOfGame()) {
            if(!checkEndOfRound()) {
                handler.postDelayed(this, 1000);
            }
        }
    }

    private void removeBugs() {
        int number = 0;
        while(number < gameArea.getChildCount()) {
            ImageView bug = (ImageView) gameArea.getChildAt(number);
            Date birthdate= (Date) bug.getTag(R.id.birthdate);
            long age = (new Date()).getTime() - birthdate.getTime();
            if(age > MAXAGE_BUG) {
                gameArea.removeView(bug);
            } else {
                number++;
            }
        }
    }

    private void showBug() {
        int width = gameArea.getWidth();
        int height = gameArea.getHeight();
        int bugWidth = (int) Math.round(scale * 50);
        int bugHeight = (int) Math.round(scale * 50);
        int left = randomGenerator.nextInt(width - bugWidth);
        int top = randomGenerator.nextInt(height - bugHeight);

        ImageView bug = new ImageView(this);
        bug.setImageResource(R.drawable.bug);
        bug.setOnClickListener(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bugWidth, bugHeight);
        params.leftMargin = left;
        params.topMargin = top;
        params.gravity = Gravity.TOP + Gravity.START;
        bug.setTag(R.id.birthdate, new Date());
        gameArea.addView(bug, params);

    }

    private boolean checkEndOfGame() {
        if(time == 0 && caughtBugs < bugs) {
            gameOver();
            return true;
        }
        return false;
    }

    private void gameOver() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.game_over);
        dialog.show();
        gameIsRunning = false;
    }

    private boolean checkEndOfRound() {
        if(caughtBugs >= bugs) {
            startRound();
            return true;
        }
        return false;
    }
}
