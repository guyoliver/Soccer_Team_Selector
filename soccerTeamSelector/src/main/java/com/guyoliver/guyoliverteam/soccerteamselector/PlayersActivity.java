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
    Spinner spinnerDefensePlayerLevel, spinnerAttackPlayerLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        textViewViewPlayers = (TextView) findViewById(R.id.textViewViewPlayers);
        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerDefensePlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerDefense);
        spinnerAttackPlayerLevel = (Spinner) findViewById(R.id.spinnerPlayerAttack);

        findViewById(R.id.buttonAddPlayer).setOnClickListener(this);
        findViewById(R.id.buttonAddDefaultPlayers).setOnClickListener(this);

        textViewViewPlayers.setOnClickListener(this);

    }

    //In this method we will do the create operation
    private void addPlayer() {
        String name = editTextName.getText().toString().trim();
        int defense = Integer.parseInt(spinnerDefensePlayerLevel.getSelectedItem().toString());
        int attack = Integer.parseInt(spinnerAttackPlayerLevel.getSelectedItem().toString());
        if (inputsAreCorrect(name)) {
            if (PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb(name, defense, attack))
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
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלי",17, 25);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("מוני",10, 22);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוסי",12, 22);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אסי",15, 20);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("ארבל",17, 20);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אוהד",12, 20);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דניאל",25, 15);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלירן",20, 15);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יניב",15, 17);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיל",17, 12);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("ירון",12, 20);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עידן",12, 17);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אוזן",17, 10);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיא אוליבר",20, 12);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלון",17, 12);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אור",12, 12);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עופר",7, 17);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוני",17, 12);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עודד",20, 7);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אריק",7, 10);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיא רונן",17, 10);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אורי",10, 5);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPlayer:
                addPlayer();
                break;
            case R.id.textViewViewPlayers:
                startActivity(new Intent(this, ViewAllPlayersActivity.class));
                break;
            case R.id.buttonAddDefaultPlayers:
                addDefaultPlayers();
                break;

        }
    }
}
