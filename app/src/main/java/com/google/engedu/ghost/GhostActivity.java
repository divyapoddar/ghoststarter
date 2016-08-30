package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String TAG = "GhostActivity";
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    TextView text;
    TextView label;
    String word;

    Button btChallenge,btRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        label = (TextView) findViewById(R.id.gameStatus);
        text = (TextView) findViewById(R.id.ghostText);
        btChallenge = (Button) findViewById(R.id.btChallenge);
        btRestart = (Button) findViewById(R.id.btRestart);
        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (savedInstanceState != null) {
            Log.i(TAG, "onCreate: " + savedInstanceState.getBoolean("userTurn"));
            text.setText(savedInstanceState.getString("word"));
            userTurn = savedInstanceState.getBoolean("userTurn");
            if (userTurn)
                label.setText(USER_TURN);
            else label.setText("");
        } else {
            onStart(null);
        }
    }

    private void computerTurn() {
        userTurn = false;
        label.setText(COMPUTER_TURN);
        word = text.getText().toString().toLowerCase();
        Log.i(TAG, "computerTurn: prevWord:" + word);
        if (word.length() > 3 && dictionary.isWord(word)) {
            label.setText("Computer challenged you and Computer won.!!");
            btChallenge.setEnabled(false);

        } else {
            String newWord = dictionary.getAnyWordStartingWith(word);
            Log.i(TAG, "computerTurn: newWord:" + newWord);
            if (newWord == null) {
                label.setText("Dont bluff.You lose.!!");
                btChallenge.setEnabled(false);
            } else {
                word = newWord.substring(0, word.length() + 1);
                text.setText(word);
                Log.i(TAG, "computerTurn: else newWord:" + word);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        btChallenge.setEnabled(true);
        text.setText(dictionary.getAnyWordStartingWith("").substring(0, 4));
        word = text.getText().toString().trim();
        userTurn = random.nextBoolean();
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        word = text.getText().toString().trim();
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            word = word.concat(event.getDisplayLabel() + "").toLowerCase();
            Log.i(TAG, "onKeyUp: " + word);
            text.setText(word);
            computerTurn();
        } else {
            Toast.makeText(this, "Invalid key press", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void challenge(View view) {
        word = text.getText().toString().toLowerCase();
        if (word.length() > 3 && dictionary.isWord(word)) {
            label.setText("You won.!!");
        } else if (word.length() < 4) {
            label.setText("Too Small word");
        } else {
            String newWord = dictionary.getAnyWordStartingWith(word);
            if (newWord == null) {
                label.setText("Invalid word. You cannot challenge");
            } else {
                word = newWord.substring(0, word.length());
                label.setText(word + " You lose!!");
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("word", text.getText().toString().trim());
        outState.putBoolean("userTurn", userTurn);
        Log.i(TAG, "onSaveInstanceState: " + userTurn);
    }
}
