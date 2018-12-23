package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlayersActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewViewPlayers;
    EditText editTextName;
    Spinner spinnerDefensePlayerLevel, spinnerAttackPlayerLevel, spinnerPlayMakerPlayerLevel,
            spinnerFitnessPlayerLevel;
    Integer defaultPlayersCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        textViewViewPlayers = (TextView) findViewById(R.id.textViewViewPlayers);
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerAttackPlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerAttack);
        spinnerDefensePlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerDefense);
        spinnerPlayMakerPlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerPlayMaker);
        spinnerFitnessPlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerFitness);

        findViewById(R.id.buttonAddPlayer).setOnClickListener(this);
        findViewById(R.id.buttonAddDefaultPlayers).setOnClickListener(this);

        textViewViewPlayers.setOnClickListener(this);
        defaultPlayersCounter = 0;

    }

    //In this method we will do the create operation
    private void addPlayer() {
        String name = editTextName.getText().toString().trim();
        int attack = Integer.parseInt(spinnerAttackPlayerLevel.getSelectedItem().toString());
        int defense = Integer.parseInt(spinnerDefensePlayerLevel.getSelectedItem().toString());
        int playmaker = Integer.parseInt(spinnerPlayMakerPlayerLevel.getSelectedItem().toString());
        int fitness = Integer.parseInt(spinnerFitnessPlayerLevel.getSelectedItem().toString());
        if (inputsAreCorrect(name)) {
            if (PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb(name, attack,
                    defense, playmaker, fitness))
            {
                Toast.makeText(this, "Player Added Successfully", Toast.LENGTH_SHORT).show();
            } else //failed
            {
                Toast.makeText(this, "Player Added Failed!!!!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //this method will validate the name and salary
    //dept does not need validation as it is a spinner and it cannot be empty
    private boolean inputsAreCorrect(String name) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;
        }

        return true;
    }

    void addDefaultPlayers(){
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("רועי אוזן", 6, 6, 5, 5);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אור אהרוני", 7, 7, 6, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אורי יוסף", 3, 5, 2, 5);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלון בלוך", 6, 7, 5, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלירן אהרוני", 7, 6, 6, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אריק נקש", 5, 3, 3, 5);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיא אוליבר", 6, 8, 5, 8);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עומר אבישר", 6, 6, 5, 6);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיל מלא", 6, 7, 5, 6);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דניאל מילשטיין", 7, 8, 6, 8);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוני יפת", 6, 6, 5, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוסי אוחנה", 8, 5, 8, 5);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("ירון בן ארי", 9, 7, 8, 8);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("מוני בראל", 9, 7, 9, 8);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עודד אהרוני", 5, 7, 5, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עופר ששון", 6, 4, 5, 6);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עידן הנדלמן", 7, 7, 6, 8);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יניב בן שושן", 7, 6, 7, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אוהד אהרוני", 7, 7, 7, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אבישי זמיר", 8, 6, 7, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלי טופחי", 10, 8, 9, 9);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דויד", 6, 6, 7, 6);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אריק (אור)", 5, 4, 3, 4);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("רז קרני", 8, 7, 8, 7);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPlayer:
                addPlayer();
                defaultPlayersCounter = 0;
                break;
            case R.id.textViewViewPlayers:
                startActivity(new Intent(this, ViewAllPlayersActivity.class));
                defaultPlayersCounter = 0;
                break;
            case R.id.buttonAddDefaultPlayers:
                if (7 == defaultPlayersCounter) {
                    addDefaultPlayers();
                    defaultPlayersCounter++;
                }
                else
                    defaultPlayersCounter++;
                break;

        }
    }
}
