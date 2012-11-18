package com.example.question;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionDB extends SQLiteOpenHelper{


//campos de la base de datos que se creararan en la tabla
    private static final String DATABASE_NAME = "qdb.db";
    public static final String TABLE = "tabla";
    public static final String NAME = "name";
    public static final String SCORE = "score";
     
    public QuestionDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        }
    @Override
    public void onCreate(SQLiteDatabase db) {
    //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL("CREATE TABLE " + TABLE + " (_id  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, score INTEGER);");
    }    
    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {
    /* NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de eliminar la tabla anterior y crearla
     * de nuevo vacía con el nuevo formato. Sin embargo lo normal será que haya que migrar datos de la tabla antigua
     * a la nueva, por lo que este método debería ser más elaborado.*/
        android.util.Log.v("tabla","Upgrading database, which will destroy all old data");        
    //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
    //Se utiliza para poder crear la nueva base de datos
        onCreate(db);
    }
}