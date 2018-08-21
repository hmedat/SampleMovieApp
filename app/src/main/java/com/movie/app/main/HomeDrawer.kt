package com.movie.app.main

import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.movie.app.R

class HomeDrawer internal constructor(activity: MainActivity, toolbar: Toolbar) {
    companion object {
        private const val MY_FAV = 1
    }

    private val mResult: Drawer

    init {
        val itemMyFav = PrimaryDrawerItem()
            .withIdentifier(MY_FAV.toLong())
            .withName(R.string.drawer_item_fav)

        mResult = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .addDrawerItems(itemMyFav)
            .withOnDrawerItemClickListener { _, _, drawerItem ->
                when (drawerItem.identifier.toInt()) {
                    MY_FAV -> {
                    }
                }//TODO
                true
            }
            .build()
    }

    internal fun closeIfOpen(): Boolean {
        if (mResult.isDrawerOpen) {
            mResult.closeDrawer()
            return true
        }
        return false
    }
}
