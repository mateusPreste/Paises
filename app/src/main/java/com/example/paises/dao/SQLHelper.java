package com.example.paises.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "dbCountry";
    private static final int VERSAO_BANCO = 1;
    public static final String COLUNA_ID = "_id";
    public static final String COUNTRY_TABLE = "country_table";
    public static final String NAME_COLUMN = "name";
    public static final String REGION_COLUMN = "region";
    public static final String POPULATION_COLUMN = "population";
    public static final String FLAG_COLUMN = "flag";

    public SQLHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + COUNTRY_TABLE + " ( " +
                        COLUNA_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        NAME_COLUMN + " TEXT, " +
                        REGION_COLUMN + " TEXT, " +
                        POPULATION_COLUMN + " TEXT, " +
                        FLAG_COLUMN + " TEXT)"
        );

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // para as próximas versões
    }

}
