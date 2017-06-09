package net.mingninja.scarniesdice;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int user_total_score,user_turn_score,computer_total_score,computer_turn_score, max_to_hold;
    private boolean player;
    public static final int DICE_FACES = 6;
    private static HashMap<Integer,Integer> dice_images;
    View view;
    TextView scoreKeep, status;
    ImageView dice;
    Button rollButton, resetButton, holdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_total_score = 0;
        user_turn_score = 0;
        computer_total_score = 0;
        computer_turn_score = 0;
        max_to_hold = 0;
        player = true;
        scoreKeep = (TextView) findViewById(R.id.score_keep);
        status = (TextView) findViewById(R.id.game_status);
        dice = (ImageView) findViewById(R.id.dice_image);
        resetButton = (Button) findViewById(R.id.reset_button);
        holdButton = (Button) findViewById(R.id.hold_button);
        rollButton = (Button) findViewById(R.id.roll_button);
        dice_images = new HashMap<Integer,Integer>();
        dice_images.put(1,R.drawable.dice1);
        dice_images.put(2,R.drawable.dice2);
        dice_images.put(3,R.drawable.dice3);
        dice_images.put(4,R.drawable.dice4);
        dice_images.put(5,R.drawable.dice5);
        dice_images.put(6,R.drawable.dice6);
        view = View.inflate(getApplicationContext(), R.layout.activity_main, null);
    }

    public int randDice() {
        Random rand = new Random();
        return rand.nextInt(DICE_FACES) + 1;
    }
    public void rollDice(View v) { //TODO randomly select dice value, update display to display image
        status.setText("");
        int diceValue = randDice();
        updateDiceView(diceValue);

        if(diceValue != 1) {
            //dice values are added to turn score
            if (player) {
                user_turn_score += diceValue;
                updateScoreLabel(user_turn_score, computer_total_score);
                status.setText("You rolled a "+diceValue);
            }else{
                computer_turn_score += diceValue;
                updateScoreLabel(user_total_score, computer_turn_score);
                status.setText("Computer rolled a "+diceValue);
            }
        }else { //if rolled die is 1
            if(player) {
                user_turn_score = user_total_score; //turn score resets to last total score
                updateScoreLabel(user_turn_score, computer_total_score);
                status.setText("You rolled a one");
                player = false;
                max_to_hold = computer_turn_score + 20;
                computerTurn();
            }else{
                computer_turn_score = computer_total_score;
                updateScoreLabel(user_total_score,computer_turn_score);
                status.setText("Computer rolled a one");
                player = true;
                holdButton.setEnabled(true);
                rollButton.setEnabled(true);

            }
        }
        Log.v("player: ","Player: "+String.valueOf(player));
     }

    public void holdTurn(View v) {
        if(player) {
            user_total_score =+ user_turn_score;
            updateScoreLabel(user_turn_score, computer_total_score);
            status.setText("You hold");
            player = false;
            max_to_hold = computer_turn_score + 20;
            computerTurn();
        }else{
            computer_total_score =+ computer_turn_score;
            updateScoreLabel(user_total_score, computer_turn_score);
            status.setText("Computer holds");
        }
    }

    public void resetGame(View v) {
        scoreKeep.setText(R.string.default_score);
        user_total_score = 0;
        user_turn_score = 0;
        computer_total_score = 0;
        computer_turn_score = 0;
        holdButton.setEnabled(true);
        rollButton.setEnabled(true);
    }

    public void updateDiceView(int diceValue){
        dice.setImageResource(dice_images.get(diceValue));

    }

    public void updateScoreLabel(int userScore, int computerScore){
        scoreKeep.setText("Your score: "+userScore+" Computer score: "+computerScore);
    }

    public void computerTurn() {
            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    Log.v("Runnable","runnable");
                    if(!player) {
                        holdButton.setEnabled(false);
                        rollButton.setEnabled(false);

                        if (computer_turn_score < max_to_hold && !player) {
//                        rollDice(MainActivity.class.findViewById(android.R.id.content));
                            // rollDice(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));
                            Log.v("computer runs: ", String.valueOf(computer_turn_score));
                            rollDice(view.findViewById(android.R.id.content));
                        }

                        if (computer_turn_score >= max_to_hold && !player) {
                            holdTurn(view.findViewById(android.R.id.content));
                            computer_total_score = computer_turn_score;
                            player = true;
                            holdButton.setEnabled(true);
                            rollButton.setEnabled(true);
                        }
                    }
                    handler.postDelayed(this, 500);
                    Log.v("current player: ",String.valueOf(player));
                    Log.v("computer turn score: ",String.valueOf(computer_turn_score));
                    Log.v("max to hold",String.valueOf(max_to_hold));

                }
            };
            handler.postDelayed(run,0);
   }
}
