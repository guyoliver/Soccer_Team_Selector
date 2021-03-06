package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelectionNextMatchResults extends AppCompatActivity {

    private static final String TAG = SelectionNextMatchResults.class.getName();

    private TextView tv;

    //need to get from setting
    Integer m_NumberOfPlayersPerTeam = 5;
    Integer m_NumberOfTeams = 3;
    Integer m_attackFactor = 40;
    Integer m_defenseFactor = 30;
    Integer m_playMakerFactor = 0;
    Integer m_fitnessFactor = 30;
    List <Team > teams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btCopyToClipboardRandom, btCopyToClipboardWithScore, btShareAllTo ;
        setContentView(R.layout.activity_selection_next_match_results);

        tv = (TextView) findViewById(R.id.textviewResults);
        List<Player> playersList = PlayersDatabase.getInstance(this.getApplicationContext()).getPlayersFromDatabase();

        //load setting data
        m_NumberOfTeams = SettingDatabase.getInstance(this).getNumberOfTeams();
        m_NumberOfPlayersPerTeam = SettingDatabase.getInstance(this).getNumberOfPlayersPerTeam();
        m_attackFactor = SettingDatabase.getInstance(this).getAttackFactor();
        m_defenseFactor = SettingDatabase.getInstance(this).getDefenseFactor();
        m_playMakerFactor = SettingDatabase.getInstance(this).getPlayMakerFactor();
        m_fitnessFactor = SettingDatabase.getInstance(this).getFitnessFactor();


        btCopyToClipboardRandom = (Button) findViewById(R.id.copyToClipboard);
        btCopyToClipboardWithScore = (Button) findViewById(R.id.copyWithScore);
        btShareAllTo = (Button) findViewById(R.id.shareAllTo);

        // Create list of teams
        createTeams();

        final int rand_val = new Random().nextInt(3)+1;

        //assigned players to teams
        assignPlayersToTeams(playersList, rand_val);

        //print teams
        tv.setText("");
        for (Team team: teams) {
            tv.setText(tv.getText() +  team.getName() + "\n----------\n");
            for (int i=0; i<team.getNumberOfPlayer(); i++) {
                //GUY_TO_REMOVE //tv.setText(tv.getText() +  team.getPlayer(i).getName() +"\n");
                tv.setText(tv.getText() +  team.getPlayer(i).getName() + "     " + team.getPlayer(i).getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor,m_fitnessFactor) +"\n");
            }
            tv.setText(tv.getText() +"Total Score: " + team.getTotalFactor() + "\n");
            String string_rand_val = "";
            switch (rand_val)
            {
                case 1:
                    string_rand_val = "Attack";
                    break;
                case 2:
                    string_rand_val = "Playmaker";
                    break;
                case 3:
                    string_rand_val = "Defence";
                    break;
            }
            tv.setText(tv.getText() +"Ordered by: " + string_rand_val + "\n");
        }

        //move to next step
        btCopyToClipboardRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theString = printToLog(true, false, rand_val);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(v.getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("teamContent", theString);
                clipboard.setPrimaryClip(clip);

            }
        });


        //move to next step
        btCopyToClipboardWithScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theString = printToLog(false, true, rand_val);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(v.getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("teamContent", theString);
                clipboard.setPrimaryClip(clip);

            }
        });

        //move to next step
        btShareAllTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theString = printToLog(true, true, rand_val);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, theString);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

    }

    private String printToLog(boolean isRandom, boolean isWithScore, Integer rand_val) {

        String theString = "";

        for (Team team: teams) {
            theString += team.getName() + "\n----------\n";
            List<Player> players = team.copyListAndShuffleTeam(isRandom);

            for (int i=0; i<players.size(); i++) {
                theString += team.getPlayer(i).getName() + "\n"; //+ "     " + team.getPlayer(i).getTotalFactor(m_attackFactor, m_defenseFactor, m_playMakerFactor,m_fitnessFactor) +"\n";
            }
            theString += "\n";
        }

        if (isWithScore) {
            theString += "\n\n\n";
            for (Team team : teams) {
                theString += team.getName() + "  Total Score: " + team.getTotalFactor() + "\n";
            }

            String string_rand_val = "";
            switch (rand_val)
            {
                case 1:
                    string_rand_val = "Attack";
                    break;
                case 2:
                    string_rand_val = "Playmaker";
                    break;
                case 3:
                    string_rand_val = "Defence";
                    break;
            }
            theString += "Ordered by: " + string_rand_val + "\n";
        }

        return theString;
    }

    //Create teams array
    private void createTeams() {
        for (int i = 0; i< m_NumberOfTeams; i++) {
            teams.add(new Team("Team" + Integer.toString(i+1), m_attackFactor, m_defenseFactor,
                    m_playMakerFactor, m_fitnessFactor));
        }
    }

    private void assignPlayersToTeams(final List<Player> playersList, Integer order) {

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

        //Update to below:
        // Create the list by the given order
        // 1 - Attack, PlayMaker, Defence
        // 2 - PlayMaker, Attack, Defence
        // 3 - Defence, Attack, PlayMaker

        //Algo for distribution between teams:
        // BEST == left in the playing list (removed after selected)
        //0. Create list of playing player from total list
        //1. Get best attack player to team1, 2nd to team2 and 3rd to team3
        //2. Get best playMaker player to team2, 2nd to team3 and 3rd to team1
        //3. Get best defense player to team3, 2nd to team1 and 3rd to team2
        //4. Rest of team distribute by playerComparator between teams (Attack=40, Defense = 30 and
        //    fitness = 30

        if (order < 1 || order > 3 )
        {
            //ERROR! out our bound
            tv.setText("ERROR with order type");
            return;
        }

        //section 0
        //Create list with all playing list
        List <Player> tempPlayersList = new ArrayList<>();
        for (Player player: playersList) {
            if (player.isPlayNextMatch())
                tempPlayersList.add(player);
        }

        //for dummy calculate average team by attack, can be removed
        Integer averageTeam = 0;
        int numberOfEntries = m_NumberOfPlayersPerTeam * m_NumberOfTeams;
        for (int i=0; i<numberOfEntries; i++) {
            averageTeam += tempPlayersList.get(i).getAttack();
        }
        averageTeam = averageTeam /3;



        //section 1\//1. Get best attack player to team1, 2nd to team2 and 3rd to team3
        //Sort list by attack
        switch (order)
        {
            case 1:
                Collections.sort(tempPlayersList, new PlayerComparatorByAttack());
                break;
            case 2:
                Collections.sort(tempPlayersList, new PlayerComparatorByPlayMaker());
                break;
            case 3:
                Collections.sort(tempPlayersList, new PlayerComparatorByDefense());
                break;
        }
        for (int i=0; i<m_NumberOfTeams; i++) {
            //1. Get "i" best attack player to team1
            teams.get(i).addPlayer(tempPlayersList.get(0));
            //remove player
            tempPlayersList.remove(0);
            //1. Get "i" best attack player to team1 and remove him
            //1. Get 2nd best to team2 and remove him
            //1. Get 3rd best to team3 and remove him
        }


        //section 2\//2. Get best playMaker player to team2, 2nd to team3 and 3rd to team1
        //Sort list by playMaker
        switch (order)
        {
            case 1:
                Collections.sort(tempPlayersList, new PlayerComparatorByPlayMaker());
                break;
            case 2:
                Collections.sort(tempPlayersList, new PlayerComparatorByAttack());
                break;
            case 3:
                Collections.sort(tempPlayersList, new PlayerComparatorByAttack());
                break;
        }
        for (int i=m_NumberOfTeams-1; i>=0; i--) {
            //1. Get best playMaker player to team2
            teams.get(i).addPlayer(tempPlayersList.get(0));
            //remove player
            tempPlayersList.remove(0);
            //1. Get best playMaker player to team2
            //1. Get 2nd best to team3
            //1. Get 3rd best to team1
        }

        //section 3\//3. Get best defense player to team3, 2nd to team1 and 3rd to team2
        //Sort list by defense
        switch (order)
        {
            case 1:
                Collections.sort(tempPlayersList, new PlayerComparatorByDefense());
                break;
            case 2:
                Collections.sort(tempPlayersList, new PlayerComparatorByDefense());
                break;
            case 3:
                Collections.sort(tempPlayersList, new PlayerComparatorByPlayMaker());
                break;
        }
        for (int i=m_NumberOfTeams/2; i<m_NumberOfTeams; i++) {
            //1. Get best defense player to team3
            teams.get(i).addPlayer(tempPlayersList.get(0));
            //remove player
            tempPlayersList.remove(0);
            //1. Get best defense player to team3 and remove him
            //1. Get 2nd best to team1 and remove him
            //1. Get 3rd best to team2 and remove him
        }


        for (int i=(m_NumberOfTeams/2)-1; i>=0; i--) {
            //1. Get best defense player to team3
            teams.get(i).addPlayer(tempPlayersList.get(0));
            //remove player
            tempPlayersList.remove(0);
            //1. Get best defense player to team3 and remove him
            //1. Get 2nd best to team1 and remove him
            //1. Get 3rd best to team2 and remove him
        }


        //section 3\//4. Rest of team distribute by playerComparator between teams (Attack=40, Defense = 30 and
        //        //    fitness = 30
        Collections.sort(tempPlayersList, new PlayerComparator(m_attackFactor, m_defenseFactor, m_playMakerFactor, m_fitnessFactor));
        numberOfEntries = tempPlayersList.size();
        // order list by attack
        for (int i=0; i<numberOfEntries; i++) {
            int teamNumber = -1;
            Double teamCalculatedFactors = 9999.99;
            //Get lowest not full team
            for (int j=0; j<teams.size(); j++)
            {
                //If need to add players
                if (teams.get(j).getNumberOfPlayer() < m_NumberOfPlayersPerTeam) {
                    //check if this is the team with lowest available attack - need to choose "ordered player"
                    if (teams.get(j).getTotalFactor() < teamCalculatedFactors)
                    {
                        teamNumber = j;
                        teamCalculatedFactors = teams.get(j).getTotalFactor();
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
            teams.get(teamNumber).addPlayer(tempPlayersList.get(i));
        }

        Log.d(TAG, "Setting teams done ");

    }
}