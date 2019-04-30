package com.example.paises.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.paises.Model.Countries;

import java.util.ArrayList;
import java.util.List;



public class Repositorio {

    private SQLHelper helper;
    private SQLiteDatabase db;

    public Repositorio(Context ctx){
        helper = new SQLHelper(ctx);
    }

    public long inserir(Countries country){
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SQLHelper.NAME_COLUMN, country.name);
        cv.put(SQLHelper.REGION_COLUMN, country.region);
        cv.put(SQLHelper.POPULATION_COLUMN, country.population);
        cv.put(SQLHelper.FLAG_COLUMN, country.flag);

        long id = db.insert(SQLHelper.COUNTRY_TABLE, null, cv);

        if(id != -1){
            country.id = id;
        }
        db.close();
        return id;
    }

    public void excluirAll(){
        db = helper.getWritableDatabase();
        db.delete(SQLHelper.COUNTRY_TABLE, null, null);
        db.close();
    }

    public List<Countries> listarPaises() {
        String sql = "SELECT * FROM " + SQLHelper.COUNTRY_TABLE;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Countries> list = new ArrayList();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(
                    cursor.getColumnIndex(SQLHelper.COLUNA_ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.NAME_COLUMN)
            );
            String region = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.REGION_COLUMN)
            );
            String population = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.POPULATION_COLUMN)
            );
            String flag = cursor.getString(
                    cursor.getColumnIndex(SQLHelper.FLAG_COLUMN)
            );

            Countries ubs = new Countries(name, region, population, flag);
            list.add(ubs);
        }
        cursor.close();
        return list;
    }

}