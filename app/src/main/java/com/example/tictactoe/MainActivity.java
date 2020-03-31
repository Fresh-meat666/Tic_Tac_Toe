package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button mBoardButtons[];
    // Various text displayed
    private TextView mInfoTextView;
    private TextView mHumanWin;
    private TextView mAndroidWin;
    private TextView mTie;
    // Restart Button
    private Button startButton;
    private MediaPlayer mediaPlayer;
    // Game Over
    Boolean mGameOver;
    Boolean humanFirst = true;
    int humanWin = 0;
    int androidWin = 0;
    int tie = 0;
    int level;


    //--- Set up the game board.
    private void startNewGame() {
        mGameOver = false;
        mGame.clearBoard();
        //---Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        //---Computer goes first
        if (humanFirst) {
            mInfoTextView.setText(R.string.your_turn);
            int move = mGame.getComputerMove(level);
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            humanFirst = false;
        }
        //---Human goes first
        else {
            mInfoTextView.setText(R.string.human_first);
            humanFirst = true;
        }
    }

    //--- OnClickListener for Restart a New Game Button
    public void newGame(View v) {
        startNewGame();
    }

    //--- set the clicked buttons to unclickable and marked.
    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("android.resource://"
                    + getPackageName() + "/" + R.raw.click);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            if (mGameOver == false) {
                if (mBoardButtons[location].isEnabled()) {
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    //--- If no winner yet, let the computer make a move
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.android_turn);
                        int move = mGame.getComputerMove(level);
                        setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                        winner = mGame.checkForWinner();
                    }
                    if (winner == 0) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
                        mInfoTextView.setText(R.string.your_turn);
                    } else if (winner == 1) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
                        mInfoTextView.setText(R.string.tie);
                        tie += 1;
                        mTie.setText(getBaseContext().getString(R.string.aTie, tie));
                        mGameOver = true;
                    } else if (winner == 2) {
                        mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
                        mInfoTextView.setText(R.string.human_win);
                        mInfoTextView.animate().scaleX(2f).scaleY(2f).setDuration(2000);
                        uri = Uri.parse("android.resource://"
                                + getPackageName() + "/" + R.raw.victory);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        humanWin += 1;
                        mHumanWin.setText(getBaseContext().getString(R.string.humanWin, humanWin));
                        mGameOver = true;
                    } else {
                        mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
                        mInfoTextView.setText(R.string.android_win);
                        mInfoTextView.animate().scaleX(2f).scaleY(2f).setDuration(2000);
                        uri = Uri.parse("android.resource://"
                                + getPackageName() + "/" + R.raw.defeat);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        androidWin += 1;
                        mAndroidWin.setText(getBaseContext().getString(R.string.androidWin,
                                androidWin));
                        mGameOver = true;
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the aaction bar if it is present
    getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_level1:
                Toast.makeText(this, "You set the AI difficulty to Easy !", Toast.LENGTH_LONG).show();
                level = 1;
                return true;
            case R.id.menu_level2:
                Toast.makeText(this, "You set the AI difficulty to Harder !", Toast.LENGTH_LONG).show();
                level = 2;
                return true;
            case R.id.menu_level3:
                Toast.makeText(this, "You set the AI difficulty to Expert !", Toast.LENGTH_LONG).show();
                level = 3;
                return true;
            case R.id.menu_quit:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();

        mBoardButtons = new Button[mGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.button0);
        mBoardButtons[1] = (Button) findViewById(R.id.button1);
        mBoardButtons[2] = (Button) findViewById(R.id.button2);
        mBoardButtons[3] = (Button) findViewById(R.id.button3);
        mBoardButtons[4] = (Button) findViewById(R.id.button4);
        mBoardButtons[5] = (Button) findViewById(R.id.button5);
        mBoardButtons[6] = (Button) findViewById(R.id.button6);
        mBoardButtons[7] = (Button) findViewById(R.id.button7);
        mBoardButtons[8] = (Button) findViewById(R.id.button8);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mHumanWin = findViewById(R.id.humanWin);
        mHumanWin.setText(getBaseContext().getString(R.string.humanWin, humanWin));
        mAndroidWin = findViewById(R.id.androidWin);
        mAndroidWin.setText(getBaseContext().getString(R.string.androidWin, androidWin));
        mTie = findViewById(R.id.tie);
        mTie.setText(getBaseContext().getString(R.string.aTie, tie));

        mGame = new TicTacToeGame();
        startNewGame();
    }
}
