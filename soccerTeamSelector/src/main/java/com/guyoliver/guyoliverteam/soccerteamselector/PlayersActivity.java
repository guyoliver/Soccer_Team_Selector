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
            spinnerFitnessPlayerLevel, spinnerPlayerPermanent;
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
        spinnerPlayerPermanent = (Spinner) findViewById(R.id.spinnerPlayerPermanent);

        findViewById(R.id.buttonAddPlayer).setOnClickListener(this);
        findViewById(R.id.buttonLoadPlayerFromFile).setOnClickListener(this);
        findViewById(R.id.buttonSavePlayerToFile).setOnClickListener(this);

        textViewViewPlayers.setOnClickListener(this);

    }

    //In this method we will do the create operation
    private void addPlayer() {
        String name = editTextName.getText().toString().trim();
        int attack = Integer.parseInt(spinnerAttackPlayerLevel.getSelectedItem().toString());
        int defense = Integer.parseInt(spinnerDefensePlayerLevel.getSelectedItem().toString());
        int playmaker = Integer.parseInt(spinnerPlayMakerPlayerLevel.getSelectedItem().toString());
        int fitness = Integer.parseInt(spinnerFitnessPlayerLevel.getSelectedItem().toString());
        String stringPlayerPermanent = spinnerPlayerPermanent.getSelectedItem().toString();
        int playerPermanent = 0;

        if (stringPlayerPermanent.equals("true"))
            playerPermanent = 1;

        if (inputsAreCorrect(name)) {
            if (PlayersDatabase.getInstance(this.getApplicationContext()).addPlayerToDb(name, attack,
                    defense, playmaker, fitness, playerPermanent))
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
            String csvLine = "name,attack,defense,playMaker,fitness,playerPermanent\r\n";
            writer.write(csvLine);
            for (Player entry: resultList) {
                /*name,attack,defense,playMaker,fitness, playerPermanent"*/
                int playerPermanent = 0;
                if (entry.isPlayerPermanent())
                    playerPermanent = 1;

                csvLine = entry.getName() + "," + entry.getAttack() + "," + entry.getDefense() + "," + entry.getPlayMaker() + "," + entry.getFitness() + "," + playerPermanent +"\r\n";
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
                    Integer.parseInt(guy[3]),Integer.parseInt(guy[4]), Integer.parseInt(guy[5]));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddPlayer:
                addPlayer();
                break;
            case R.id.textViewViewPlayers:
                startActivity(new Intent(this, ViewAllPlayersActivity.class));
                break;
            case R.id.buttonLoadPlayerFromFile:
                isLoadFile = true;
                OpenFileAndManagePlayers();
                break;
            case R.id.buttonSavePlayerToFile:
                isLoadFile = false;
                //OpenFileNew();
                OpenFileAndManagePlayers();
                break;

        }
    }
}
