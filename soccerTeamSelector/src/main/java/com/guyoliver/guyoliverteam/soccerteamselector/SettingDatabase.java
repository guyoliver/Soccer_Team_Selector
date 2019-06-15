package com.guyoliver.guyoliverteam.soccerteamselector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.BoringLayout;
import android.util.Log;


public class SettingDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "myplayersettingdatabase";
    private static final String TABLE_NAME = "SETTINGS";
    private static final String COLUMN_STRING_ID = "ID";
    public static final int COLUMN_NUMBER_ID = 0;
    private static final String COLUMN_STRING_NUMBER_OF_TEAMS = "NUMBER_OF_TEAMS";
    private static final int COLUMN_NUMBER_NUMBER_OF_TEAMS = 1;
    private static final String COLUMN_STRING_NUMBER_OF_PLAYERS_PER_TEAM = "NUMBER_OF_PLAYERS_PER_TEAM";
    private static final int COLUMN_NUMBER_NUMBER_OF_PLAYERS_PER_TEAM = 2;
    private static final String COLUMN_STRING_ATTACK_FACTOR = "ATTACK_FACTOR";
    private static final int COLUMN_NUMBER_ATTACK_FACTOR = 3;
    private static final String COLUMN_STRING_DEFENSE_FACTOR = "DEFENSE_FACTOR";
    private static final int COLUMN_NUMBER_DEFENSE_FACTOR = 4;
    private static final String COLUMN_STRING_PLAYMAKER_FACTOR = "PLAYMAKER_FACTOR";
    private static final int COLUMN_NUMBER_PLAYMAKER_FACTOR = 5;
    private static final String COLUMN_STRING_FITNESS_FACTOR = "FITNESS_FACTOR";
    private static final int COLUMN_NUMBER_FITNESS_FACTOR = 6;
    private static final String COLUMN_STRING_ROUND_VALUE = "ROUND_VALUES";
    private static final int COLUMN_NUMBER_ROUND_VALUE = 7;
    private static final String COLUMN_STRING_MAX_VALUE_FOR_PLAYER = "MAX_VALUE_FOR_PLAYER";
    private static final int COLUMN_NUMBER_MAX_VALUE_FOR_PLAYER = 8;

    private static final String TAG = PlayersDatabase.class.getName();

    private static SettingDatabase mInstance = null;
    private final Context context;

    SQLiteDatabase mDatabase;

    //Spinner spinnerDefensePlayerLevel, spinnerAttackPlayerLevel;

    public SettingDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        mDatabase = getWritableDatabase();
        createDefaultEntryIfNeeded();
    }

    public static synchronized SettingDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new SettingDatabase(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n " +
                            COLUMN_STRING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                            COLUMN_STRING_NUMBER_OF_TEAMS + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_NUMBER_OF_PLAYERS_PER_TEAM + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_ATTACK_FACTOR + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_DEFENSE_FACTOR + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_PLAYMAKER_FACTOR + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_FITNESS_FACTOR + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_ROUND_VALUE + " INTEGER NOT NULL,\n " +
                            COLUMN_STRING_MAX_VALUE_FOR_PLAYER + " INTEGER NOT NULL\n " +
                            ");"
            );
        }
        catch (Exception e) {
            Log.e(TAG, "ExecSql got exception " + e.toString());
        }
        mDatabase = db;
        //if default entry not exist, create with default values
        createDefaultEntryIfNeeded();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Updating " + TABLE_NAME + "table from " + oldVersion + " to " + newVersion);

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
                case 1:
                    break;
                case 2:
                    db.execSQL("ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + COLUMN_STRING_ROUND_VALUE + " INTEGER");
                    db.execSQL("UPDATE " + TABLE_NAME + " SET " +COLUMN_STRING_ROUND_VALUE + " = 1 ");
                    db.execSQL("ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + COLUMN_STRING_MAX_VALUE_FOR_PLAYER + " INTEGER");
                    db.execSQL("UPDATE " + TABLE_NAME + " SET " +COLUMN_STRING_MAX_VALUE_FOR_PLAYER + " = 100 ");
                    break;
                case 3:
                case 4:
                    db.delete(DATABASE_NAME, null, null);
                    //db.execSQL("ALTER TABLE "+ TABLE_NAME + " ADD COLUMN " + COLUMN_STRING_IS_PLAY_NEXT_MATCH + " INTEGER");
                    //db.execSQL("UPDATE " + TABLE_NAME + " SET " +COLUMN_STRING_IS_PLAY_NEXT_MATCH + " = 0 ");
                    break;
                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown oldVersion " + oldVersion);
            }
            upgradeTo++;
        }
    }
     @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //if default entry not exist, create with default values
    private void createDefaultEntryIfNeeded() {
        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorSetting = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //if the cursor has some data
        if (false == cursorSetting.moveToFirst()) {
            saveSettingsToDb(true, 3, 5, 40,
                    30, 0, 30, true, 100);
        }
        //closing the cursor
        cursorSetting.close();
    }


    //public function to save setting
    public boolean saveSettingsToDb(Integer numberOfTeams, Integer numberOfPlayers, Integer attackFactor,
                                    Integer defenseFactor, Integer playMakerFactor,
                                    Integer fitnessFactor, Boolean isRoundValue, Integer maxValueForPlayer) {
        return saveSettingsToDb(false, numberOfTeams, numberOfPlayers, attackFactor,
                defenseFactor, playMakerFactor, fitnessFactor, isRoundValue, maxValueForPlayer);
    }

        //public function to add player
    private boolean saveSettingsToDb(Boolean isCreate, Integer numberOfTeams, Integer numberOfPlayers,
                                     Integer attackFactor, Integer defenseFactor, Integer playMakerFactor,
                                     Integer fitnessFactor, Boolean isRoundValue, Integer maxValueForPlayer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STRING_NUMBER_OF_TEAMS, numberOfTeams);
        contentValues.put(COLUMN_STRING_NUMBER_OF_PLAYERS_PER_TEAM, numberOfPlayers);
        contentValues.put(COLUMN_STRING_ATTACK_FACTOR, attackFactor);
        contentValues.put(COLUMN_STRING_DEFENSE_FACTOR, defenseFactor);
        contentValues.put(COLUMN_STRING_PLAYMAKER_FACTOR, playMakerFactor);
        contentValues.put(COLUMN_STRING_FITNESS_FACTOR, fitnessFactor);
        contentValues.put(COLUMN_STRING_ROUND_VALUE, isRoundValue ? 1 : 0);
        contentValues.put(COLUMN_STRING_MAX_VALUE_FOR_PLAYER, maxValueForPlayer);

        if (isCreate)
            mDatabase.insert(TABLE_NAME, COLUMN_STRING_ID + '=' + 1, contentValues);
        else
            mDatabase.update(TABLE_NAME, contentValues, COLUMN_STRING_ID + '=' + 1, null);

        //mDatabase.close();
        return true;
    }

    private Integer getContentFromDb(Integer columnId) {
        Integer value = 0;

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorSetting = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //if the cursor has some data
        if (cursorSetting.moveToFirst()) {
            switch (columnId) {
                case COLUMN_NUMBER_NUMBER_OF_TEAMS:
                case COLUMN_NUMBER_NUMBER_OF_PLAYERS_PER_TEAM:
                case COLUMN_NUMBER_ATTACK_FACTOR:
                case COLUMN_NUMBER_DEFENSE_FACTOR:
                case COLUMN_NUMBER_PLAYMAKER_FACTOR:
                case COLUMN_NUMBER_FITNESS_FACTOR:
                case COLUMN_NUMBER_MAX_VALUE_FOR_PLAYER:
                case COLUMN_NUMBER_ROUND_VALUE:
                    value = cursorSetting.getInt(columnId);
                    break;
                default:
                    Log.e(TAG, "unknown request columnId = " + columnId);
                    value = 0;
            }
        }
        //closing the cursor
        cursorSetting.close();
        return value;
    }

    public Integer getNumberOfTeams() {
        return getContentFromDb(COLUMN_NUMBER_NUMBER_OF_TEAMS);
    }

    public Boolean getIsToRoundValues() {
        //GUYO to update
        return true; //getContentFromDb(COLUMN_NUMBER_NUMBER_OF_TEAMS);
    }

    public Integer getNumberOfPlayersPerTeam() {
        return getContentFromDb(COLUMN_NUMBER_NUMBER_OF_PLAYERS_PER_TEAM);
    }

    public Integer getAttackFactor() {
        return getContentFromDb(COLUMN_NUMBER_ATTACK_FACTOR);
    }

    public Integer getDefenseFactor() {
        return getContentFromDb(COLUMN_NUMBER_DEFENSE_FACTOR);
    }

    public Integer getPlayMakerFactor() {
        return getContentFromDb(COLUMN_NUMBER_PLAYMAKER_FACTOR);
    }

    public Integer getFitnessFactor() {
        return getContentFromDb(COLUMN_NUMBER_FITNESS_FACTOR);
    }

    public Boolean getIsRoundValues() {
        return getContentFromDb(COLUMN_NUMBER_ROUND_VALUE) > 0 ? true : false;
    }
    public Integer getMaxValueForPlayer() {
        return getContentFromDb(COLUMN_NUMBER_MAX_VALUE_FOR_PLAYER);
    }


}