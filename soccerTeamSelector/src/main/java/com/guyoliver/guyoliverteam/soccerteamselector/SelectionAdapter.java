package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SelectionAdapter  extends BaseAdapter {

    private Context context;
    public static List<Player> playersList;


    public SelectionAdapter(Context context, List<Player> playersArrayList) {

        this.context = context;
        this.playersList = playersArrayList;

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return playersList.size();
    }

    @Override
    public Object getItem(int position) {
        return playersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_select_players_item, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.tvPlayerName = (TextView) convertView.findViewById(R.id.textViewName);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }


        holder.checkBox.setText("Playing? ");
        holder.tvPlayerName.setText(playersList.get(position).getName());

        holder.checkBox.setChecked(playersList.get(position).isPlayNextMatch());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag( position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer)  holder.checkBox.getTag();

                if(playersList.get(pos).isPlayNextMatch()){
                    playersList.get(pos).setPlayNextMatch(false);
                }else {
                    playersList.get(pos).setPlayNextMatch(true);
                }
                int counter = 0;
                //save all selected players in DB and count number pf players
                for (Player player: playersList) {
                    PlayersDatabase.getInstance(v.getContext()).updatePlayerIsPlayNextMatch(((Integer)player.getId()).toString(), player.isPlayNextMatch());
                    if (player.isPlayNextMatch())
                        counter++;
                }
                Toast.makeText(context, counter + " players selected", Toast.LENGTH_SHORT).show();


            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected CheckBox checkBox;
        private TextView tvPlayerName;

    }

}