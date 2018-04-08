package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayersDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "myplayerdatabase";
    public static final String TABLE_NAME = "PLAYERS";
    public static final String COLUMN_STRING_ID = "ID";
    public static final int COLUMN_NUMBER_ID = 0;
    public static final String COLUMN_STRING_NAME = "NAME";
    public static final int COLUMN_NUMBER_NAME = 1;
    public static final String COLUMN_STRING_DEFENSE = "DEFENSE";
    public static final int COLUMN_NUMBER_DEFENSE = 2;
    public static final String COLUMN_STRING_ATTACK = "ATTACK";
    public static final int COLUMN_NUMBER_ATTACK = 3;
    public static final String COLUMN_STRING_IS_PLAY_NEXT_MATCH = "IS_PLAY_NEXT_MATCH";
    public static final int COLUMN_NUMBER_IS_PLAY_NEXT_MATCH = 4;

    private static final String TAG = PlayersDatabase.class.getName();

    private static PlayersDatabase mInstance = null;
    private final Context context;

    SQLiteDatabase mDatabase;

    TextView textViewViewPlayers;
    EditText editTextName;
    //Spinner spinnerDefensePlayerLevel, spinnerAttackPlayerLevel;

    public PlayersDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        mDatabase = getWritableDatabase();
        //onCreate(mDatabase);
        //openCreateDatabase(context);
    }

    public static synchronized PlayersDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new PlayersDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }
/*
    //creating a database
    private void openCreateDatabase(Context context) {
        mDatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        //createPlayerTable();
        onCreate(mDatabase);

    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(BookEntry.SQL_CREATE_BOOK_ENTRY_TABLE);
        // The rest of your create scripts go here.
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n " +
                            COLUMN_STRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                            COLUMN_STRING_NAME + " varchar(200) NOT NULL,\n " +
                            COLUMN_STRING_DEFENSE + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_ATTACK + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_IS_PLAY_NEXT_MATCH + " INTEGER DEFAULT 0\n " +
                            ");"
            );
        }
        catch (Exception e) {
            Log.e(TAG, "ExecSql got exception " + e.toString());
        }
        mDatabase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);

        /*
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d(TAG, "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(db, context, migrationName);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }
*/
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    //db.execSQL("ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + COLUMN_STRING_IS_PLAY_NEXT_MATCH + " INTEGER");
                    //db.execSQL("UPDATE " + TABLE_NAME + " SET " +COLUMN_STRING_IS_PLAY_NEXT_MATCH + " = 0 ");
                    break;
                    /*
                case 5:
                    db.execSQL("SOME ALTER COMMAND FOR VERSION 5");
                    db.execSQL("SOME ALTER COMMAND FOR VERSION 5");
                    break;
                case 6:
                    db.execSQL("SOME ALTER COMMAND FOR VERSION 6");
                    break;
                case 7:
                    db.execSQL("SOME ALTER COMMANDS FOR VERSION 7");
                    break;
                    */
                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown oldVersion " + oldVersion);
            }
            upgradeTo++;
        }
    }
    /*
    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created
    private void createPlayerTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n " +
                        COLUMN_STRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                        COLUMN_STRING_NAME + " varchar(200) NOT NULL,\n " +
                        COLUMN_STRING_DEFENSE + " INTEGER NOT NULL,\n " +
                        COLUMN_STRING_ATTACK + " INTEGER NOT NULL,\n " +
                        COLUMN_STRING_IS_PLAY_NEXT_MATCH + " INTEGER DEFAULT 0\n " +
                        ");"
        );
    }
     @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty");
            return;
        }

        Log.d(TAG, "Script found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }
*/


    //public function to add player
    public boolean addPlayerToDb(String name, int defense, int attack) {
        if (addPlayer(name, defense, attack)) {
            return true;
        } else
            return false;
    }

    //private function to add player
    private boolean addPlayer(String name, int defense, int attack) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STRING_NAME, name);
        contentValues.put(COLUMN_STRING_DEFENSE, defense);
        contentValues.put(COLUMN_STRING_ATTACK, attack);
        //contentValues.put(COLUMN_STRING_IS_PLAY_NEXT_MATCH, 0); //false

        long rt =  mDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "inserting entry to table returned " + rt);

        if (-1 == rt)
        {
            return false;
        }
        //mDatabase.close();
        return true;
    }

    //public function to add player
    public boolean updatePlayer(String id, String name, int defense, int attack) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STRING_NAME, name);
        contentValues.put(COLUMN_STRING_DEFENSE, defense);
        contentValues.put(COLUMN_STRING_ATTACK, attack);
        long rt = mDatabase.update(TABLE_NAME, contentValues, COLUMN_STRING_ID + '=' + id, null);
        Log.d(TAG, "inserting entry to table returned " + rt);

        if (-1 == rt)
        {
            return false;
        }
        //mDatabase.close();
        return true;
    }

    //public function to add player
    public boolean updatePlayerIsPlayNextMatch(String id, boolean isPlayNextMatch) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STRING_IS_PLAY_NEXT_MATCH, isPlayNextMatch);
        mDatabase.update(TABLE_NAME, contentValues, COLUMN_STRING_ID + '=' + id, null);
        //mDatabase.close();
        return true;
    }

    public boolean deletePlayerFromDatabase(String id) {

        return mDatabase.delete(TABLE_NAME, COLUMN_STRING_ID + "=" + id, null) > 0;
    }

    public List<Player> getPlayersFromDatabase() {

        List<Player> playersList = new ArrayList<>();
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorPlayer = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //if the cursor has some data
        if (cursorPlayer.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the players list
                playersList.add(new Player(
                        cursorPlayer.getInt(COLUMN_NUMBER_ID),
                        cursorPlayer.getString(COLUMN_NUMBER_NAME),
                        cursorPlayer.getInt(COLUMN_NUMBER_DEFENSE),
                        cursorPlayer.getInt(COLUMN_NUMBER_ATTACK),
                        cursorPlayer.getInt(COLUMN_NUMBER_IS_PLAY_NEXT_MATCH)> 0 ? true: false
                        ));
            } while (cursorPlayer.moveToNext());
        }
        //closing the cursor
        cursorPlayer.close();
        return playersList;
    }

    public List<Player> setAllPlayersIsPlayNextMatchToDatabase(boolean isPlayNextMatch) {

        List<Player> playersList = new ArrayList<>();
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorPlayer = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //if the cursor has some data
        if (cursorPlayer.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the players list
                updatePlayerIsPlayNextMatch(((Integer)cursorPlayer.getInt(0)).toString(), isPlayNextMatch);
            } while (cursorPlayer.moveToNext());
        }
        //closing the cursor
        cursorPlayer.close();
        return getPlayersFromDatabase();
    }

}