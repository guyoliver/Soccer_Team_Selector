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
        TextView textViewDefense = view.findViewById(R.id.textViewPlayerDefense);
        TextView textViewAttack = view.findViewById(R.id.textViewPlayerAttack);

        //adding data to views
        textViewName.setText(player.getName());
        textViewDefense.setText(player.getDefense().toString());
        textViewAttack.setText(player.getAttack().toString());

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
        final Spinner spinnerDefense = view.findViewById(R.id.spinnerPlayerDefense);
        final Spinner spinnerAttack = view.findViewById(R.id.spinnerPlayerAttack);

        //update the player info into dialog
        editTextName.setText(player.getName());
        spinnerDefense.setSelection(((ArrayAdapter)spinnerDefense.getAdapter()).getPosition(player.getDefense().toString()));
        spinnerAttack.setSelection(((ArrayAdapter)spinnerAttack.getAdapter()).getPosition(player.getAttack().toString()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdatePlayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                int defense = Integer.parseInt(spinnerDefense.getSelectedItem().toString());
                int attack = Integer.parseInt(spinnerAttack.getSelectedItem().toString());

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                PlayersDatabase.getInstance(view.getContext()).updatePlayer(String.valueOf(player.getId()),name,defense,attack);
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
