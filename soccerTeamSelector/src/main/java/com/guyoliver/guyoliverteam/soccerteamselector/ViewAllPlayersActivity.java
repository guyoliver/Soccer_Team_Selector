package com.guyoliver.guyoliverteam.soccerteamselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllPlayersActivity extends AppCompatActivity {

    ListView listViewPlayers;
    List<Player> playersList;
    PlayerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.list_layout_player);
        setContentView(R.layout.activity_view_all_players);

        listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
        playersList = new ArrayList<>();

        //this method will display the employees in the list
        showPlayersFromDatabase();

    }


    //show all players
    private void showPlayersFromDatabase() {
        playersList = PlayersDatabase.getInstance(this.getApplicationContext()).getPlayersFromDatabase();

        //creating the adapter object
        adapter = new PlayerAdapter(this, R.layout.list_layout_player, playersList);

        //adding the adapter to listview
        listViewPlayers.setAdapter(adapter);
    }


}
