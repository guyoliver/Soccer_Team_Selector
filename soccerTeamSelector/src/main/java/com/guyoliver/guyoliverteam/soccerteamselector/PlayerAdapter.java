package com.guyoliver.guyoliverteam.soccerteamselector;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class PlayerAdapter extends ArrayAdapter<Player> {

    Context mCtx;
    int listLayoutRes;
    List<Player> playersList;
    //SQLiteDatabase mDatabase;

    public PlayerAdapter(Context mCtx, int listLayoutRes, List<Player> playersList/*, SQLiteDatabase mDatabase*/) {
        super(mCtx, listLayoutRes, playersList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.playersList = playersList;
        //this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting employee of the specified position
        final Player player = playersList.get(position);


        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewAttack = view.findViewById(R.id.textViewPlayerAttack);
        TextView textViewDefense = view.findViewById(R.id.textViewPlayerDefense);
        TextView textViewPlayMaker = view.findViewById(R.id.textViewPlayerPlayMaker);
        TextView textViewFitness = view.findViewById(R.id.textViewPlayerFitness);
        TextView textViewPlayerPermanent = view.findViewById(R.id.textViewPlayerPermanent);

        //adding data to views
        textViewName.setText(player.getName());
        textViewAttack.setText(player.getAttack().toString());
        textViewDefense.setText(player.getDefense().toString());
        textViewPlayMaker.setText(player.getPlayMaker().toString());
        textViewFitness.setText(player.getFitness().toString());
        textViewPlayerPermanent.setText(player.isPlayerPermanent()?"True":"False");

        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeletePlayer);
        Button buttonEdit = view.findViewById(R.id.buttonEditPlayer);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlayer(player);
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PlayersDatabase.getInstance(convertView.getContext()).deletePlayerFromDatabase(String.valueOf(player.getId()));
                        reloadPlayersFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updatePlayer(final Player player) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_player, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final Spinner spinnerAttack = view.findViewById(R.id.spinnerPlayerAttack);
        final Spinner spinnerDefense = view.findViewById(R.id.spinnerPlayerDefense);
        final Spinner spinnerPlayMaker = view.findViewById(R.id.spinnerPlayerPlayMaker);
        final Spinner spinnerFitness = view.findViewById(R.id.spinnerPlayerFitness);
        final Spinner spinnerPlayerPermanent = view.findViewById(R.id.spinnerPlayerPermanent);

        //update the player info into dialog
        editTextName.setText(player.getName());
        spinnerAttack.setSelection(((ArrayAdapter)spinnerAttack.getAdapter()).getPosition(player.getAttack().toString()));
        spinnerDefense.setSelection(((ArrayAdapter)spinnerDefense.getAdapter()).getPosition(player.getDefense().toString()));
        spinnerPlayMaker.setSelection(((ArrayAdapter)spinnerPlayMaker.getAdapter()).getPosition(player.getPlayMaker().toString()));
        spinnerFitness.setSelection(((ArrayAdapter)spinnerFitness.getAdapter()).getPosition(player.getFitness().toString()));
        spinnerPlayerPermanent.setSelection(((ArrayAdapter)spinnerPlayerPermanent.getAdapter()).getPosition(Boolean.toString(player.isPlayerPermanent())));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdatePlayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                int defense = Integer.parseInt(spinnerDefense.getSelectedItem().toString());
                int attack = Integer.parseInt(spinnerAttack.getSelectedItem().toString());
                int playMaker = Integer.parseInt(spinnerPlayMaker.getSelectedItem().toString());
                int fitness = Integer.parseInt(spinnerFitness.getSelectedItem().toString());
                String stringPlayerPermanent = spinnerPlayerPermanent.getSelectedItem().toString();
                int playerPermanent = 0;
                if (stringPlayerPermanent.equals("true"))
                    playerPermanent = 1;

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }
                PlayersDatabase.getInstance(view.getContext()).updatePlayer(String.valueOf(player.getId()),
                        name, attack, defense, playMaker,fitness, playerPermanent);
                //run reload operation on Ui thread
                ((Activity) mCtx).runOnUiThread (new Runnable() {
                    @Override
                    public void run() {
                        reloadPlayersFromDatabase();
                    }
                    });

                Toast.makeText(mCtx, "Player Updated", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });


    }

    private void reloadPlayersFromDatabase() {
        playersList.clear();
        playersList.addAll(PlayersDatabase.getInstance(mCtx).getPlayersFromDatabase());
        notifyDataSetChanged();

    }

}
