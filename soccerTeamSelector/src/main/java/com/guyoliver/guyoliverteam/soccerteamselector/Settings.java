package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity  implements View.OnClickListener{

    Spinner spinnerNumberOfTeams, spinnerNumberOfPlayersPerTeam;
    EditText editTextAttack, editTextDefense, editTextPlaymaker, editTextFitness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerNumberOfTeams = (Spinner) findViewById(R.id.spinnerNumberOfTeams);
        spinnerNumberOfPlayersPerTeam = (Spinner) findViewById(R.id.spinnerNumberOfPlayersPerTeam);
        editTextAttack = (EditText)findViewById(R.id.editTextAttack);
        editTextDefense = (EditText) findViewById(R.id.editTextDefense);
        editTextPlaymaker = (EditText) findViewById(R.id.editTextPlayMaker);
        editTextFitness = (EditText) findViewById(R.id.editTextFitness);


        findViewById(R.id.buttonSaveSettings).setOnClickListener(this);

        //load saved data
        loadSettingToView(this.getApplicationContext());

    }
    //load data to view
    void loadSettingToView(Context context) {
        //load saved number of teams;
        SettingDatabase settingDb = SettingDatabase.getInstance(context);

        Integer numberOfTeams = settingDb.getNumberOfTeams();
        Integer numberOfPlayersPerTeams = settingDb.getNumberOfPlayersPerTeam();
        Integer attackFactor = settingDb.getAttackFactor();
        Integer defenseFactor = settingDb.getDefenseFactor();
        Integer playmakerFactor = settingDb.getPlayMakerFactor();
        Integer fitnessFactor = settingDb.getFitnessFactor();

        spinnerNumberOfTeams.setSelection(((ArrayAdapter)spinnerNumberOfTeams.getAdapter()).getPosition(numberOfTeams.toString()));
        spinnerNumberOfPlayersPerTeam.setSelection(((ArrayAdapter)spinnerNumberOfPlayersPerTeam.getAdapter()).getPosition(numberOfPlayersPerTeams.toString()));
        editTextAttack.setText(attackFactor.toString());
        editTextDefense.setText(defenseFactor.toString());
        editTextPlaymaker.setText(playmakerFactor.toString());
        editTextFitness.setText(fitnessFactor.toString());

    }
    //save data from view into database
    boolean saveSettingToDatabase(Context context) {

        Integer numberOfTeams = Integer.parseInt(spinnerNumberOfTeams.getSelectedItem().toString());
        Integer numberOfPlayersPerTeams = Integer.parseInt(spinnerNumberOfPlayersPerTeam.getSelectedItem().toString());
        Integer attackFactor = Integer.parseInt(editTextAttack.getText().toString());
        Integer defenseFactor = Integer.parseInt(editTextDefense.getText().toString());
        Integer playmakerFactor = Integer.parseInt(editTextPlaymaker.getText().toString());
        Integer fitnessFactor = Integer.parseInt(editTextFitness.getText().toString());

        Integer totalFactor = attackFactor + defenseFactor + playmakerFactor + fitnessFactor;
        //validity check
        if (totalFactor != 100)
        {
            Toast.makeText(context, "please update factors to total 100, current total " + totalFactor, Toast.LENGTH_SHORT);
            return false;
        }

        //save settings to DB
        SettingDatabase settingDb = SettingDatabase.getInstance(context);
        settingDb.saveSettingsToDb(numberOfTeams, numberOfPlayersPerTeams, attackFactor,
                defenseFactor, playmakerFactor, fitnessFactor);

        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSaveSettings:
                //load saved data
                if (true == saveSettingToDatabase(view.getContext())) {
                    //close activity
                    finish();
                }
                else {
                    //user need to update - error msg
                }
                break;
        }
    }
}
