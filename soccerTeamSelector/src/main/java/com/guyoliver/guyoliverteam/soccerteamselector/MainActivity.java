package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

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
                //check if there are players in the list
                List<Player> playersList = PlayersDatabase.getInstance(this).getPlayersFromDatabase();
                if (playersList.size() < 1) {
                    Toast.makeText(view.getContext(), "No Players in the list, add players", Toast.LENGTH_LONG);
                } else {
                    startActivity(new Intent(this, SelectPlayersForNextMatch.class));
                }

                break;
            case R.id.buttonSettings:
                startActivity(new Intent(this, Settings.class));
        }
    }

}
