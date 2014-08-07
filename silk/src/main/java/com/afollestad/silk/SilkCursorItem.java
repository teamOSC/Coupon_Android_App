package com.afollestad.silk;

import android.content.ContentValues;
import android.database.Cursor;

public interface SilkCursorItem<Type> extends SilkComparable {

    public ContentValues getContentValues();

    public Type convert(Cursor cursor);

    public String getColumns();
}
