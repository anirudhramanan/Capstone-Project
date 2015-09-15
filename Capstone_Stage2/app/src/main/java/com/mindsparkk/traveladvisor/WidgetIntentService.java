package com.mindsparkk.traveladvisor;

import android.app.IntentService;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.mindsparkk.traveladvisor.Utils.DatabaseSave;

/**
 * Created by Anirudh on 15/09/15.
 */
public class WidgetIntentService extends IntentService implements LoaderManager.LoaderCallbacks<Cursor> {

    DatabaseSave db;

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, MyAppWidgetProvider.class));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
