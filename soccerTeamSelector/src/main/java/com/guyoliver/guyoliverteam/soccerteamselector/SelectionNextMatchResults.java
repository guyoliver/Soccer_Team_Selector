package com.guyoliver.guyoliverteam.soccerteamselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectionNextMatchResults extends AppCompatActivity {

    private static final String TAG = SelectionNextMatchResults.class.getName();

    private TextView tv;

    //need to get from setting
    Integer m_NumberOfPlayersPerTeam = 5;
    Integer m_NumberOfTeams = 3;
    Integer m_AttackDefenseRation = 50;
    List <Team > teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_next_match_results);

        tv = (TextView) findViewById(R.id.textviewResults);
        List<Player> playersList = PlayersDatabase.getInstance(this.getApplicationContext()).getPlayersFromDatabase();

        //load setting data
        m_NumberOfTeams = SettingDatabase.getInstance(this).getNumberOfTeams();
        m_NumberOfPlayersPerTeam = SettingDatabase.getInstance(this).getNumberOfPlayersPerTeam();
        m_AttackDefenseRation = SettingDatabase.getInstance(this).getRatioAttackDefense();

        // Create list of teams
        createTeams();

        //assigned players to teams
        assignPlayersToTeams(playersList);

        //print teams
        tv.setText("");
        for (Team team: teams) {
            tv.setText(tv.getText() +  team.name + "\n----------\n");
            for (Player playerInTeam: team.players) {
                tv.setText(tv.getText() +  playerInTeam.getName() +"\n");
            }
            tv.setText(tv.getText() +"Total Score: " + team.totalAttack + "\n");
        }

    }




    //Create teams array
    private void createTeams() {
        for (int i = 0; i< m_NumberOfTeams; i++) {
            teams.add(new Team("Team" + i));
        }
    }

    private void assignPlayersToTeams(final List<Player> playersList) {

//      https://stackoverflow.com/questions/16588669/spread-objects-evenly-over-multiple-collections
//
//      I believe this is a variant of the bin packing problem, and as such it is NP-hard. Your answer is essentially a variant of the first fit decreasing heuristic, which is a pretty good heuristic. That said, I believe that the following will give better results.
//
//      Sort each individual bucket in descending size order, using a balanced binary tree.
//      Calculate average size.
//      Sort the buckets with size less than average (the "too-small buckets") in descending size order, using a balanced binary tree.
//      Sort the buckets with size greater than average (the "too-large buckets") in order of the size of their greatest elements, using a balanced binary tree (so the bucket with {9, 1} would come first and the bucket with {8, 5} would come second).
//      Pass1: Remove the largest element from the bucket with the largest element; if this reduces its size below the average, then replace the removed element and remove the bucket from the balanced binary tree of "too-large buckets"; else place the element in the smallest bucket, and re-index the two modified buckets to reflect the new smallest bucket and the new "too-large bucket" with the largest element. Continue iterating until you've removed all of the "too-large buckets."
//      Pass2: Iterate through the "too-small buckets" from smallest to largest, and select the best-fitting elements from the largest "too-large bucket" without causing it to become a "too-small bucket;" iterate through the remaining "too-large buckets" from largest to smallest, removing the best-fitting elements from them without causing them to become "too-small buckets." Do the same for the remaining "too-small buckets." The results of this variant won't be as good as they are for the more complex variant because it won't shift buckets from the "too-large" to the "too-small" category or vice versa (hence the search space will be smaller), but this also means that it has much simpler halting conditions (simply iterate through all of the "too-small" buckets and then halt), whereas the complex variant might cause an infinite loop if you're not careful.
//      The idea is that by moving the largest elements in Pass1 you make it easier to more precisely match up the buckets' sizes in Pass2. You use balanced binary trees so that you can quickly re-index the buckets or the trees of buckets after removing or adding an element, but you could use linked lists instead (the balanced binary trees would have better worst-case performance but the linked lists might have better average-case performance). By performing a best-fit instead of a first-fit in Pass2 you're less likely to perform useless moves (e.g. moving a size-10 object from a bucket that's 5 greater than average into a bucket that's 5 less than average - first fit would blindly perform the movie, best-fit would either query the next "too-large bucket" for a better-sized object or else would remove the "too-small bucket" from the bucket tree).

        List <Player> tempPlayersList = new ArrayList<>();
        for (Player player: playersList) {
            if (player.isPlayNextMatch())
                tempPlayersList.add(player);
        }


        Collections.sort(tempPlayersList, new PlayerAttackComparator(m_AttackDefenseRation));
        Integer averageTeam = 0;

        int numberOfEntries = m_NumberOfPlayersPerTeam * m_NumberOfTeams;
        for (int i=0; i<numberOfEntries; i++) {
            averageTeam += tempPlayersList.get(i).getAttack();
        }

        averageTeam = averageTeam /3;


        // order list by attack
        for (int i=0; i<numberOfEntries; i++) {
            int teamNumber = -1;
            int teamAttackValue = 999;
            //Get lowest not full team
            for (int j=0; j<teams.size(); j++)
            {
                //If need to add players
                if (teams.get(j).numberOfPlayer <5) {
                    //check if this is the team with lowest available attack - need to choose "ordered player"
                    if (teams.get(j).totalAttack < teamAttackValue)
                    {
                        teamNumber = j;
                        teamAttackValue = teams.get(j).totalAttack;
                    } else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (teamNumber == -1)
            {
                //ERROR
                Log.e(TAG, "Setting teams got error ");

            }
            teams.get(teamNumber).numberOfPlayer ++;
            teams.get(teamNumber).players.add(tempPlayersList.get(i));
            teams.get(teamNumber).totalAttack += tempPlayersList.get(i).getAttack();
            teams.get(teamNumber).totalDefense += tempPlayersList.get(i).getDefense();


        }

        Log.d(TAG, "Setting teams done ");

    }

}