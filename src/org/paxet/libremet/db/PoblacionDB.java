package org.paxet.libremet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PoblacionDB extends SQLiteOpenHelper {

	/** Versión de la base de datos */
    private static int VERSION = 1;
    
	/** Nombre de la base de datos */
    public static String DBNAME = "libremet";
    /** Nombre de la tabla */
    public static final String DATABASE_TABLE = "poblaciones";

    /** Columnas de la base de datos */
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_PAIS = "pais";

    private SQLiteDatabase mDB;
 
    /** Constructor */
    public PoblacionDB(Context context) {
        super(context, DBNAME, null, VERSION);
        this.mDB = getWritableDatabase();
    }
 
    /** Método que se llama cuando la base de datos no existe todavía
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table "+ DATABASE_TABLE + " ( "
                        + KEY_ROW_ID + " integer primary key, "
                        + KEY_NOMBRE + " text, "
                        + KEY_PAIS + " text ) " ;
 
        db.execSQL(sql);
 
        //Introduzco unos valores de ejemplo
        sql = "insert into " + DATABASE_TABLE + " ( " + KEY_ROW_ID + "," + KEY_NOMBRE + "," + KEY_PAIS + " ) "
                + " values ( '2509954', 'Valencia', 'ES' )";
        db.execSQL(sql);
 
        sql = "insert into " + DATABASE_TABLE + " ( " + KEY_ROW_ID + "," + KEY_NOMBRE + "," + KEY_PAIS + " ) "
                + " values ( '2643743', 'London', 'UK' )";
        db.execSQL(sql);
 
        sql = "insert into " + DATABASE_TABLE + " ( " + KEY_ROW_ID + "," + KEY_NOMBRE + "," + KEY_PAIS + " ) "
                + " values ( '3352136', 'Windhoek', 'NA' )";
        db.execSQL(sql);
 
        sql = "insert into " + DATABASE_TABLE + " ( " + KEY_ROW_ID + "," + KEY_NOMBRE + "," + KEY_PAIS + " ) "
                + " values ( '1283240', 'Kathmandu', 'NP' )";
        db.execSQL(sql);
 
    }
 
    /** Devuelve todas las poblaciones de la tabla */
    public Cursor getAllPoblaciones(){
        return mDB.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_NOMBRE, KEY_PAIS } ,
                            null, null, null, null,
                            KEY_NOMBRE + " asc ");
    }
    
    public int deletePoblacion(long id) {
        return mDB.delete(DATABASE_TABLE, KEY_ROW_ID + " = " + id, null);
    }
    
    public void insertPoblacion(Poblacion pobl) {
    	ContentValues valores = new ContentValues();
    	valores.put(KEY_ROW_ID, pobl.getID());
    	valores.put(KEY_NOMBRE, pobl.getNombre());
    	valores.put(KEY_PAIS, pobl.getPais());
    	
		mDB.insert(DATABASE_TABLE, null, valores);
	}
 
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
}
