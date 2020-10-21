package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class PlayersActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewViewPlayers;
    EditText editTextName;
    Spinner spinnerDefensePlayerLevel, spinnerAttackPlayerLevel, spinnerPlayMakerPlayerLevel,
            spinnerFitnessPlayerLevel;
    Integer defaultPlayersCounter;
    boolean isLoadFile = false;

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
        findViewById(R.id.buttonLoadPlayerFromFile).setOnClickListener(this);
        findViewById(R.id.buttonSavePlayerToFile).setOnClickListener(this);
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

    protected void OpenFileAndManagePlayers() {
        setContentView(R.layout.activity_main);
        Intent intent;

        if (isLoadFile) {
            intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_CREATE_DOCUMENT);
        }
        startActivityForResult(Intent.createChooser(intent, "Select a *.csv file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData(); //The uri with the location of the file
            if (isLoadFile) {
                LoadPlayersFromFile(selectedFile);
            }
            else
            {
                SavePlayersToFile(selectedFile);
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SavePlayersToFile(Uri selectedFile) {
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(selectedFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<Player> resultList = PlayersDatabase.getInstance(this.getApplicationContext()).getPlayersFromDatabase();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            String csvLine = "name,attack,defense,playMaker,fitness\r\n";
            writer.write(csvLine);
            for (Player entry: resultList) {
                /*name,attack,defense,playMaker,fitness"*/
                csvLine = entry.getName() + "," + entry.getAttack() + "," + entry.getDefense() + "," + entry.getPlayMaker() + "," + entry.getFitness() +"\r\n";
                writer.write(csvLine);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                writer.close();
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }


    private void LoadPlayersFromFile(Uri selectedFile) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(selectedFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        //skip first line as title
        String[] guy;
        for (int i=1; i < resultList.size(); i++) {
            guy = (String[]) resultList.get(i);
            PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb(guy[0],Integer.parseInt(guy[1]),Integer.parseInt(guy[2]),
                    Integer.parseInt(guy[3]),Integer.parseInt(guy[4]));
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

        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("רועי אוזן", 55, 64, 45, 45);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אור אהרוני", 69, 69, 61, 69);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אורי יוסף", 33, 45, 24, 50);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלון בלוך", 58, 74, 48, 69);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלירן אהרוני", 68, 59, 59, 69);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אריק נקש", 51, 33, 34, 46);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיא אוליבר", 58, 75, 53, 78);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עומר אבישר", 61, 58, 54, 64);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("גיל מלא", 56, 65, 47, 58);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דניאל מילשטיין", 72, 78, 57, 85);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוני יפת", 62, 61, 51, 65);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יוסי אוחנה", 81, 51, 78, 54);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("ירון בן ארי", 85, 66, 83, 76);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("מוני בראל", 91, 68, 85, 82);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עודד אהרוני", 54, 71, 46, 71);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עופר ששון", 62, 41, 46, 63);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("עידן הנדלמן", 70, 73, 64, 76);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("יניב בן שושן", 74, 63, 69, 71);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אוהד אהרוני", 73, 65, 71, 75);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אבישי זמיר", 78, 65, 73, 67);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אלי טופחי", 96, 76, 93, 85);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דודי ירחי", 96, 76, 93, 85);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("דויד", 60, 60, 70, 60);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("אריק (אור)", 50, 40, 30, 40);
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("רז קרני", 80, 70, 80, 70);
/*
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
        PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb("ארבל דר", 9,8, 8, 9);
        */
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
            case R.id.buttonLoadPlayerFromFile:
                isLoadFile = true;
                OpenFileAndManagePlayers();
                defaultPlayersCounter = 0;
                break;
            case R.id.buttonSavePlayerToFile:
                isLoadFile = false;
                //OpenFileNew();
                OpenFileAndManagePlayers();
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
