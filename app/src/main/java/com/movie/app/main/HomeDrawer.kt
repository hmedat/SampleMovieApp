package com.movie.app.main

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.movie.app.R
import com.movie.app.fav.FavouritesActivity

class HomeDrawer internal constructor(activity: MainActivity, toolbar: Toolbar) {
    companion object {
        private const val MY_FAV = 1
    }

    private val mResult: Drawer

    init {
        val headerResult = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem().withName("Mohammed Hmedat")
                    .withEmail("mohammed@gmail.com")
                    .withIcon(R.drawable.ic_person_outline_light_blue_600_24dp)
            )
            .build()
        val itemMyFav = PrimaryDrawerItem()
            .withIdentifier(MY_FAV.toLong())
            .withSetSelected(false)
            .withSelectable(false)
            .withName(R.string.drawer_item_fav)

        mResult = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withAccountHeader(headerResult)
            .addDrawerItems(itemMyFav)
            .withSelectedItemByPosition(RecyclerView.NO_POSITION)
            .withOnDrawerItemClickListener { _, _, drawerItem ->
                when (drawerItem.identifier.toInt()) {
                    MY_FAV -> {
                        FavouritesActivity.startActivity(activity)
                        return@withOnDrawerItemClickListener false
                    }
                }
                true
            }
            .build()
        mResult.currentSelection
    }

    internal fun closeIfOpen(): Boolean {
        if (mResult.isDrawerOpen) {
            mResult.closeDrawer()
            return true
        }
        return false
    }
}
