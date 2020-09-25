package org.learn.employeemanagement;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class EmployeeContentProvider extends ContentProvider {
    // constructing the content URI
    static final String AUTHORITY = "org.learn.employeemanagement.EmployeeContentProvider";
    static final String PATH = "employees";
    static final String URI = "content://" + AUTHORITY + "/" +PATH;
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

        public DatabaseHelper(@Nullable Context context) {
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
        Context context = getContext();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        mSQLiteDatabase = databaseHelper.getWritableDatabase();

        return !(mSQLiteDatabase == null);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(EMPLOYEE_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case EMPLOYEES:
                sqLiteQueryBuilder.setProjectionMap(EMPLOYEES_PROJECTION_MAP);
                break;
            case EMPLOYEE_ID:
                sqLiteQueryBuilder.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = _ID;
        }

        Cursor cursor = sqLiteQueryBuilder.query(mSQLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EMPLOYEES:
                return "vnd.android.cursor.dir/vnd.example.employees";
            case EMPLOYEE_ID:
                return "vnd.android.cursor.item/vnd.example.employees";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowId = mSQLiteDatabase.insert(EMPLOYEE_TABLE_NAME, "", contentValues);


        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case EMPLOYEES:
                count = mSQLiteDatabase.delete(EMPLOYEE_TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                String id = uri.getPathSegments().get(1);
                String selectionQuery = "";
                if (!TextUtils.isEmpty(selection)) {
                    selectionQuery += " AND (" + selectionQuery + ")";
                }

                count = mSQLiteDatabase.delete(EMPLOYEE_TABLE_NAME, _ID + " = " + id + selectionQuery, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case EMPLOYEES:
                count = mSQLiteDatabase.update(EMPLOYEE_TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                String id = uri.getPathSegments().get(1);
                String selectionQuery = "";
                if (!TextUtils.isEmpty(selection)) {
                    selectionQuery += " AND (" + selectionQuery + ")";
                }

                count = mSQLiteDatabase.update(EMPLOYEE_TABLE_NAME, contentValues, _ID + " = " + id + selectionQuery, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
