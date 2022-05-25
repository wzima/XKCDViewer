package com.zima.myxkcdviewer.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.zima.myxkcdviewer.R
import com.zima.myxkcdviewer.databinding.ActivityMainBinding
import com.zima.myxkcdviewer.ui.fragments.TipOfTheDayDialogFragment
import com.zima.myxkcdviewer.ui.utils.MySnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

/**

xkcd-viewer with a database of favorites
Author: Wolfgang Zima

Following third-party libraries were used:
Room:  storing the favorites in a sql-database (https://developer.android.com/jetpack/androidx/releases/room)
Volley: dowloading the JSON from the web (https://github.com/google/volley)
PhotoView: enable zooming of the comic
Joda: useful date library

There are three fragments. Navigation between the fragments is controlled by the NavController of the NavHostFragment (navigation.xml)
- ComicNavigationViewFragment: This is the main fragment for navigating through the comics.
- FavoriteComicListFragment: This shows the list of favorites using the ComicListAdapter. Data is loaded from the RoomDatabase in the class ComicDatabase
- ComicViewFragment: When selecting a comic in the list, it is shown through this fragment.

 **/


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope()  {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // using toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        TipOfTheDayDialogFragment.newInstance(R.string.IntroHelpTitle, R.string.IntroHelp, TipOfTheDayDialogFragment.INTRO_TIP).isShow(
            supportFragmentManager, TipOfTheDayDialogFragment.INTRO_TIP,
            this, TipOfTheDayDialogFragment.INTRO_TIP
        )

        //getCurrentData()
    }

//    fun getCurrentData() {
//        val api = Retrofit.Builder()
//            .baseUrl(ComicURLBuilder.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiRequests::class.java)
//
//        launch(Dispatchers.IO) {
//            try {
//                val response=api.getTodaysComic().awaitResponse()
//                if (response.isSuccessful) {
//                    val data = response.body()!!
//                    Log.d("Retrofit",data.toString())
//                }
//            } catch (e: Throwable) {
//                Log.d("HttpException",e.toString())
//            }
//        }
//    }

    //implement exit on double-tap on back button. this can prevent accidental exiting of the app.
    private var exitMillis: Long = 0
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        //in navigation.xml the top fragment has the label R.string.xkcd. Must be modified if changed here.
        val topFragmentLabel = getString(R.string.xkcd)
        if (navHostFragment.navController.currentDestination?.label == topFragmentLabel) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (System.currentTimeMillis() - exitMillis < 3000) {
                        finish()
                    } else {
                        exitMillis = System.currentTimeMillis()
                        MySnackbar.showShort(this, binding.root, R.string.PressBackOnceMoreToExit)
                    }
                    return true
                }
            }
        }
        return super.onKeyUp(keyCode, event)

    }

}