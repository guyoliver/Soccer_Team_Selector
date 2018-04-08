package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends AppCompatActivity  implements View.OnClickListener{

    Spinner spinnerNumberOfTeams, spinnerNumberOfPlayersPerTeam;
    SeekBar defenseAttackRationSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerNumberOfTeams = (Spinner) findViewById(R.id.spinnerNumberOfTeams);
        spinnerNumberOfPlayersPerTeam = (Spinner) findViewById(R.id.spinnerNumberOfPlayersPerTeam);
        defenseAttackRationSeekBar=(SeekBar)findViewById(R.id.defenseAttackRationSeekBar);

        findViewById(R.id.buttonSaveSettings).setOnClickListener(this);
        // perform seek bar change listener event used for getting the progress value
        defenseAttackRationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Settings.this, "Defense / Attack ration is (for Attack) :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //load saved data
        loadSettingToView(this.getApplicationContext());

    }
    //load data to view
    void loadSettingToView(Context context) {
        //load saved number of teams;
        SettingDatabase settingDb = SettingDatabase.getInstance(context);

        Integer numberOfTeams = settingDb.getNumberOfTeams();
        Integer numberOfPlayersPerTeams = settingDb.getNumberOfPlayersPerTeam();
        Integer defenseAttackRation = settingDb.getRatioAttackDefense();

        spinnerNumberOfTeams.setSelection(((ArrayAdapter)spinnerNumberOfTeams.getAdapter()).getPosition(numberOfTeams.toString()));
        spinnerNumberOfPlayersPerTeam.setSelection(((ArrayAdapter)spinnerNumberOfPlayersPerTeam.getAdapter()).getPosition(numberOfPlayersPerTeams.toString()));
        defenseAttackRationSeekBar.setProgress(defenseAttackRation);

    }
    //save data from view into database
    void saveSettingToDatabase(Context context) {

        Integer numberOfTeams = Integer.parseInt(spinnerNumberOfTeams.getSelectedItem().toString());
        Integer numberOfPlayersPerTeams = Integer.parseInt(spinnerNumberOfPlayersPerTeam.getSelectedItem().toString());
        Integer defenseAttackRation = defenseAttackRationSeekBar.getProgress();

        //save settings to DB
        SettingDatabase settingDb = SettingDatabase.getInstance(context);
        settingDb.saveSettingsToDb(numberOfTeams, numberOfPlayersPerTeams, defenseAttackRation);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSaveSettings:
                //load saved data
                saveSettingToDatabase(view.getContext());
                //close activity
                finish();
                break;
        }
    }
}
