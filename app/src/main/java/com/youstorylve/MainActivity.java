package com.youstorylve;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private static MyTouchIvent mTouchGridView;
    int timer = 0;
    Timer t = new Timer();
    TimerTask tt;
    Button restart;
    private static final int ROW = 4;
    private static final int DIMENSIONS = ROW * ROW;

    private static int mColumnWidth, mColumnHeight;

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";

    private static String[] gridList;
    static TextView txtMove;
    static TextView txtTime;
    static int moveCnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMove = findViewById(R.id.txtMotion);
        txtTime = findViewById(R.id.txtTime);
        restart = findViewById(R.id.restart);
        restart.setVisibility(View.INVISIBLE);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
        tt = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer++;
                        txtTime.setText(timer + " seconds");
                        if (isSolved()) {
                            Toast.makeText(getApplicationContext(), "WIN!", Toast.LENGTH_SHORT).show();
                            t.cancel();
                            restart.setVisibility(View.VISIBLE);
                        }
                    }
                });
            };
        };
        t.scheduleAtFixedRate(tt,500,1000);

    initialize();
        tileListed();
        setSizes();
    }

    public void onPause() {
        super.onPause();
        finish();
    }


    private void initialize() {
        mTouchGridView = findViewById(R.id.grid);
        mTouchGridView.setNumColumns(ROW);

        gridList = new String[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            gridList[i] = String.valueOf(i);
        }
    }

    private void tileListed() {
        int index;
        String tempor;
        Random random = new Random();
        for (int i = gridList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            tempor = gridList[index];
            gridList[index] = gridList[i];
            gridList[i] = tempor;
        }
    }

    private void setSizes() {
        ViewTreeObserver vto = mTouchGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTouchGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mTouchGridView.getMeasuredWidth();
                int displayHeight = mTouchGridView.getMeasuredHeight();

                int statusbarHeight = getSBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / ROW;
                mColumnHeight = requiredHeight / ROW;

                getBackgroundImage(getApplicationContext());
            }
        });
    }

    private int getSBarHeight(Context context) {
        int height = 0;
        int getResId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (getResId > 0) {
            height = context.getResources().getDimensionPixelSize(getResId);
        }

        return height;
    }

    private static void getBackgroundImage(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button btn;

        for (int i = 0; i < gridList.length; i++) {
            btn = new Button(context);

            if (gridList[i].equals("0"))
                btn.setBackgroundResource(R.drawable.image_part_001);
            else if (gridList[i].equals("1"))
                btn.setBackgroundResource(R.drawable.image_part_002);
            else if (gridList[i].equals("2"))
                btn.setBackgroundResource(R.drawable.image_part_003);
            else if (gridList[i].equals("3"))
                btn.setBackgroundResource(R.drawable.image_part_004);
            else if (gridList[i].equals("4"))
                btn.setBackgroundResource(R.drawable.image_part_005);
            else if (gridList[i].equals("5"))
                btn.setBackgroundResource(R.drawable.image_part_006);
            else if (gridList[i].equals("6"))
                btn.setBackgroundResource(R.drawable.image_part_007);
            else if (gridList[i].equals("7"))
                btn.setBackgroundResource(R.drawable.image_part_008);
            else if (gridList[i].equals("8"))
                btn.setBackgroundResource(R.drawable.image_part_009);
            else if (gridList[i].equals("9"))
                btn.setBackgroundResource(R.drawable.image_part_010);
            else if (gridList[i].equals("10"))
                btn.setBackgroundResource(R.drawable.image_part_011);
            else if (gridList[i].equals("11"))
                btn.setBackgroundResource(R.drawable.image_part_012);
            else if (gridList[i].equals("12"))
                btn.setBackgroundResource(R.drawable.image_part_013);
            else if (gridList[i].equals("13"))
                btn.setBackgroundResource(R.drawable.image_part_014);
            else if (gridList[i].equals("14"))
                btn.setBackgroundResource(R.drawable.image_part_015);
            else if (gridList[i].equals("15"))
                btn.setBackgroundResource(R.drawable.image_part_016);

            buttons.add(btn);
        }

        mTouchGridView.setAdapter(new MyAdapter(buttons, mColumnWidth, mColumnHeight));
    }

    private static void swap(Context context, int currentPosition, int swap) {
        String newPosition = gridList[currentPosition + swap];
        gridList[currentPosition + swap] = gridList[currentPosition];
        gridList[currentPosition] = newPosition;
        getBackgroundImage(context);


    }

    public static void moveTiles(Context context, String direction, int position) {
        moveCnt++;
        txtMove.setText("Moves: " + moveCnt);
        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, ROW);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < ROW - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, ROW);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == ROW - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, ROW);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > ROW - 1 && position < DIMENSIONS - ROW &&
                position % ROW == 0) {
            if (direction.equals(up)) swap(context, position, -ROW);
            else if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, ROW);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == ROW * 2 - 1 || position == ROW * 3 - 1) {
            if (direction.equals(up)) swap(context, position, -ROW);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= DIMENSIONS - ROW - 1) swap(context, position,
                        ROW);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == DIMENSIONS - ROW) {
            if (direction.equals(up)) swap(context, position, -ROW);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - ROW) {
            if (direction.equals(up)) swap(context, position, -ROW);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(up)) swap(context, position, -ROW);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context, position, ROW);
        }
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < gridList.length; i++) {
            if (gridList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }


}
