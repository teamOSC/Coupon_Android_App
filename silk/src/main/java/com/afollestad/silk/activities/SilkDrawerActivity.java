package com.afollestad.silk.activities;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;

/**
 * An activity that makes interacting with a DrawerLayout quick and easy. All that you have to do is create a layout
 * modelled off the layouts used on the Android Developer website for drawer layouts.
 *
 * @author Aidan Follestad (afollestad)
 */
public abstract class SilkDrawerActivity extends FragmentActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        super.setTitle(title);
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void setTitle(int titleId) {
        mTitle = getString(titleId);
        super.setTitle(titleId);
        getActionBar().setTitle(titleId);
    }

    /**
     * Checks whether or not any drawer is open in the DrawerActivity.
     */
    public final boolean isDrawerOpen() {
        DrawerLayout drawer = getDrawerLayout();
        return drawer != null && (drawer.isDrawerOpen(Gravity.START) || drawer.isDrawerOpen(Gravity.LEFT) || drawer.isDrawerOpen(Gravity.RIGHT));
    }

    /**
     * Gets the drawer indicator drawable resource that will be displayed next to the the home up button in the activity. This is usually
     * an icon consisting of 3 vertically orientated lines.
     */
    protected abstract int getDrawerIndicatorRes();

    /**
     * Gets the shadow drawable resource that will be used for the drawer.
     */
    protected abstract int getDrawerShadowRes();

    /**
     * Gets the layout used for the activity.
     */
    protected abstract int getLayout();

    /**
     * Gets the drawer layout, you should return the View from the layout.
     */
    public abstract DrawerLayout getDrawerLayout();

    /**
     * Gets the action bar title displayed when the drawer is open.
     */
    protected abstract int getOpenedTextRes();

    /**
     * Can be overridden.
     */
    protected void onDrawerOpened() {
    }

    /**
     * Can be overridden.
     */
    protected void onDrawerClosed() {
    }

    private void invalidateOpenClosed() {
        ActionBar ab = getActionBar();
        if (isDrawerOpen()) {
            if (getOpenedTextRes() != 0)
                ab.setTitle(getOpenedTextRes());
            onDrawerOpened();
        } else {
            if (mTitle != null)
                ab.setTitle(mTitle);
            onDrawerClosed();
        }
        invalidateOptionsMenu();
    }

    protected void setupDrawer() {
        mTitle = getTitle();
        DrawerLayout mDrawerLayout = getDrawerLayout();
        if (mDrawerLayout == null) {
            Log.w("SilkDrawerActivtiy", "You must return a drawer layout in getDrawerLayout() of your Activity in order for the drawer to be displayed.");
            return;
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getDrawerIndicatorRes(), getOpenedTextRes(), getOpenedTextRes()) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOpenClosed();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOpenClosed();

            }
        };
        mDrawerLayout.setDrawerShadow(getDrawerShadowRes(), Gravity.START);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        try {
            Field mDragger = mDrawerLayout.getClass().getDeclaredField(
                    "mLeftDragger");//mRightDragger for right obviously
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger
                    .get(mDrawerLayout);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField(
                    "mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            mEdgeSize.setInt(draggerObj, edge * 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        if (getDrawerLayout() != null) {
            ActionBar ab = getActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setupDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerToggle != null)
                    return mDrawerToggle.onOptionsItemSelected(item);
                else break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }
}