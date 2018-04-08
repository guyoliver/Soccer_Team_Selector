package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private PlayersDatabase playersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playersDatabase = PlayersDatabase.getInstance(this);
        //playersDatabase.openCreateDatabase(this);

        findViewById(R.id.buttonManagePlayers).setOnClickListener(this);
        findViewById(R.id.buttonSelectPlayersForNextMatch).setOnClickListener(this);
        findViewById(R.id.buttonSettings).setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonManagePlayers:
                //calling an activity using <intent-filter> action name
                startActivity(new Intent(this, PlayersActivity.class));
                break;
            case R.id.buttonSelectPlayersForNextMatch:

                startActivity(new Intent(this, SelectPlayersForNextMatch.class));

                break;
            case R.id.buttonSettings:
                startActivity(new Intent(this, Settings.class));
        }
    }

}
