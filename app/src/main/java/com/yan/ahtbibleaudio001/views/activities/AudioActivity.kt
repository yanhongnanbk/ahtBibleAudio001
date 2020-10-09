package com.yan.ahtbibleaudio001.views.activities

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.uamp.media.MusicService
import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.models.drawer.NaviModel
import com.yan.ahtbibleaudio001.viewmodels.MainActivityViewModel

import com.yan.ahtbibleaudio001.views.adapters.NaviAdapter
import com.yan.ahtbibleaudio001.views.clickEvent.ClickListener
import com.yan.ahtbibleaudio001.views.clickEvent.RecyclerTouchListener
import com.yan.ahtbibleaudio001.views.fragments.AudioCatFragment
import com.yan.ahtbibleaudio001.views.fragments.AudioFragment
import com.yan.ahtbibleaudio001.views.fragments.MediaItemFragment
import com.yan.ahtbibleaudio001.zutil001.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_audio.*

class AudioActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(this)
    }

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NaviAdapter

    private var items = arrayListOf(
        NaviModel("Home"),
        NaviModel("Music"),
        NaviModel("Movies"),
        NaviModel("Books"),
        NaviModel("Profile"),
        NaviModel("Settings"),
        NaviModel("Like us on facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        drawerLayout = findViewById(R.id.drawer_layout)

        // Set the toolbar
        setSupportActionBar(audio_main_toolbar)

        // Setup Recyclerview's Layout
        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this,R.drawable.rv_divider)?.let { itemDecor.setDrawable(it) }
        navigation_rv.addItemDecoration(itemDecor)
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        // # Home Fragment
                        supportActionBar?.title = "Home Fragment"
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Home Fragment")
                        val homeFragment = AudioCatFragment()
                        homeFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, homeFragment).commit()
                    }
                    1 -> {
                        // # Music Fragment
                        supportActionBar?.title = "Music Fragment"
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Music Fragment")
                        val musicFragment = AudioFragment()
                        musicFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, musicFragment).commit()
                    }
                    2 -> {
                        supportActionBar?.title = "Movies Fragment"
                        // # Movies Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Movies Fragment")
                        val moviesFragment = AudioFragment()
                        moviesFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, moviesFragment).commit()
                    }
                    3 -> {
                        supportActionBar?.title = "Books Fragment"
                        // # Books Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Books Fragment")
                        val booksFragment = AudioFragment()
                        booksFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, booksFragment).commit()
                    }
                    4 -> {
                        supportActionBar?.title = "Profile Fragment"
                        // # Books Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Profile Fragment")
                        val profileFragment = AudioFragment()
                        profileFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, profileFragment).commit()
                    }
                    5 -> {
                        // # Settings Fragment
                        val bundle = Bundle()
                        bundle.putString("fragmentName", "Settings Fragment")
                        val settingsFragment = AudioFragment()
                        settingsFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_audio_content_id, settingsFragment).commit()
                    }
                    6 -> {
                        // # Open URL in browser
                        val uri: Uri = Uri.parse("https://johnc.co/fb")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                if (position != 6 && position != 4) {
                    updateAdapter(position)
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    //Your Code
                    drawerLayout.closeDrawer(GravityCompat.START)

                }, 200)
            }
        }))

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
        updateAdapter(0)
        supportActionBar?.title = "Home Fragment"
        // Set 'Home' as the default fragment when the app starts
        val bundle = Bundle()
        bundle.putString("fragmentName", "Home Fragment")
        val homeFragment = AudioCatFragment()
        homeFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_audio_content_id, homeFragment).commit()


        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            audio_main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        // Set background of Drawer
        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        /********/
        // Since UAMP is a music player, the volume controls should adjust the music volume while
        // in the app.
        volumeControlStream = AudioManager.STREAM_MUSIC

        /**
         * Observe [MainActivityViewModel.navigateToFragment] for [Event]s that request a
         * fragment swap.
         */
        viewModel.navigateToFragment.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { fragmentRequest ->
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainer, fragmentRequest.fragment, fragmentRequest.tag
                )
                if (fragmentRequest.backStack) transaction.addToBackStack(null)
                transaction.commit()
            }
        })

        /**
         * Observe changes to the [MainActivityViewModel.rootMediaId]. When the app starts,
         * and the UI connects to [MusicService], this will be updated and the app will show
         * the initial list of media items.
         */
        viewModel.rootMediaId.observe(this,
            Observer<String> { rootMediaId ->
                rootMediaId?.let { navigateToMediaItem(it) }
            })

        /**
         * Observe [MainActivityViewModel.navigateToMediaItem] for [Event]s indicating
         * the user has requested to browse to a different [MediaItemData].
         */
        viewModel.navigateToMediaItem.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { mediaId ->
                navigateToMediaItem(mediaId)
            }
        })

    }

    /*****************************************************/
    private fun navigateToMediaItem(mediaId: String) {
        var fragment: MediaItemFragment? = getBrowseFragment(mediaId)
        if (fragment == null) {
//            fragment = MediaItemFragment.newInstance("__ALBUMS__")
            fragment = MediaItemFragment.newInstance(mediaId)
            // If this is not the top level media (root), we add it to the fragment
            // back stack, so that actionbar toggle and Back will work appropriately:
            viewModel.showFragment(fragment, !isRootId(mediaId), mediaId)
        }
    }

    private fun isRootId(mediaId: String) = mediaId == viewModel.rootMediaId.value

    private fun getBrowseFragment(mediaId: String): MediaItemFragment? {
        return supportFragmentManager.findFragmentByTag(mediaId) as MediaItemFragment?
    }
    /*****************************************************/
    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NaviAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }

}
