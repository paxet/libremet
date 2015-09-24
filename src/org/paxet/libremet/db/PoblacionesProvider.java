package org.paxet.libremet.db;



import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PoblacionesProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "org.paxet.libremet.providers.poblacionesprovider";
	 
    /** A uri to do operations on cust_master table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/poblaciones" );
 
    /** Constants to identify the requested operation */
    private static final int POBLACIONES = 1;
    private static final int POBLACIONES_ID = 2;
 
    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "poblaciones", POBLACIONES);
        uriMatcher.addURI(PROVIDER_NAME, "poblaciones/#", POBLACIONES_ID);
    }
 
    /** This content provider does the database operations by this object */
    PoblacionDB mPoblacionBD;
 
    /** A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
        mPoblacionBD = new PoblacionDB(getContext());
        return true;
    }
 
    @Override
    public String getType(Uri uri) {
        return null;
    }
 
    /** A callback method which is by the default content uri */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == POBLACIONES_ID){
        	where = "_id=" + uri.getLastPathSegment();
        	SQLiteDatabase db = mPoblacionBD.getWritableDatabase();
            c = db.query(PoblacionDB.DATABASE_TABLE, projection, where,
                            selectionArgs, null, null, sortOrder);
        } else {
        	if(uriMatcher.match(uri) == POBLACIONES){
                c = mPoblacionBD.getAllPoblaciones();
            }else{
                c = null;
            }
        }
        
        return c;
    }
 
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	int cont;
    	 
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if(uriMatcher.match(uri) == POBLACIONES_ID){
        	where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = mPoblacionBD.getWritableDatabase();
        cont = db.delete(PoblacionDB.DATABASE_TABLE, where, selectionArgs);
        return cont;
    }
 
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mPoblacionBD.getWritableDatabase();
        long regId = db.insert(PoblacionDB.DATABASE_TABLE, null, values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);
        return newUri;
    }
 
    /** No se realizan actualizaciones en las entradas, sólo se pueden borrar */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
        String[] selectionArgs) {
        return 0;
    }
	
}
