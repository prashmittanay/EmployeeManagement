package org.learn.employeemanagement;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class EmployeeContentProvider extends ContentProvider {
    // constructing the content URI
    static final String AUTHORITY = "org.learn.employeemanagement.EmployeeContentProvider";
    static final String PATH = "employees";
    static final String URI = "content://" + AUTHORITY + PATH;
    static final Uri CONTENT_URI = Uri.parse(URI);

    // columns
    static final String _ID = "_id";
    static final String NAME = "name";
    static final String DEPARTMENT = "department";

    // result holder
    static HashMap<String, String> EMPLOYEES_PROJECTION_MAP;

    //ids
    static final int EMPLOYEES = 1;
    static final int EMPLOYEE_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, EMPLOYEES);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", EMPLOYEE_ID);
    }

    private SQLiteDatabase mSQLiteDatabase;

    static final String DATABASE_NAME = "Organization";
    static final String EMPLOYEE_TABLE_NAME = "employees";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE = "CREATE TABLE " + EMPLOYEE_TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " name TEXT NOT NULL,"
            + " department TEXT NOT NULL); ";

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EMPLOYEE_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
