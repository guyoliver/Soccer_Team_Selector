package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Comparator;
import java.util.List;

public class SelectPlayersForNextMatch extends AppCompatActivity {

    private ListView lv;
    private List<Player> playersList;
    private SelectionAdapter selectionAdapter;
    private Button btnselect, btndeselect, btnnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players_for_next_match);

        lv = (ListView) findViewById(R.id.lv_select_players);
        btnselect = (Button) findViewById(R.id.select);
        btndeselect = (Button) findViewById(R.id.deselect);
        btnnext = (Button) findViewById(R.id.next);

        playersList = PlayersDatabase.getInstance(this.getApplicationContext()).getPlayersFromDatabase();
        playersList.sort(Comparator.comparing(Player::isPlayerPermanent).reversed().thenComparing(Player::getName));
        selectionAdapter = new SelectionAdapter(this,playersList);
        lv.setAdapter(selectionAdapter);

        //Select all players
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // playersList = getModel(true);
                playersList.clear();
                playersList.addAll(PlayersDatabase.getInstance(v.getContext()).setAllPlayersIsPlayNextMatchToDatabase(true));
                selectionAdapter = new SelectionAdapter(SelectPlayersForNextMatch.this,playersList);
                lv.setAdapter(selectionAdapter);
            }
        });

        //Deselect all players
        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playersList.clear();
                playersList.addAll(PlayersDatabase.getInstance(v.getContext()).setAllPlayersIsPlayNextMatchToDatabase(false));
                selectionAdapter = new SelectionAdapter(SelectPlayersForNextMatch.this,playersList);
                lv.setAdapter(selectionAdapter);
            }
        });

        //move to next step
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer counter=0;
                //save all selected players in DB and count number pf players
                for (Player player: playersList) {
                    PlayersDatabase.getInstance(v.getContext()).updatePlayerIsPlayNextMatch(((Integer)player.getId()).toString(), player.isPlayNextMatch());
                    if (player.isPlayNextMatch())
                        counter++;
                }

                //before moving to next step, checking that there are 15 names sets
                Integer totalNeededPlayer = SettingDatabase.getInstance(v.getContext()).getNumberOfTeams()*
                                             SettingDatabase.getInstance(v.getContext()).getNumberOfPlayersPerTeam();
                if (totalNeededPlayer == counter) {
                    Intent intent = new Intent(SelectPlayersForNextMatch.this, SelectionNextMatchResults.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(v.getContext()," Please select " + totalNeededPlayer + " players, you checked " + counter + " players", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
/*
    private ArrayList<Player> getModel(boolean isSelect){
        ArrayList<Player> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){

            Player player = new Player();
            player.setSelected(isSelect);
            player.setAnimal(animallist[i]);
            list.add(player);
        }
        return list;
    }
*/
}