package com.yan.ahtbibleaudio001.views.activities

import NavigationDemoFragment
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.android.uamp.media.MusicService
import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.viewmodels.MainActivityViewModel
import com.yan.ahtbibleaudio001.views.fragments.MediaItemFragment
import com.yan.ahtbibleaudio001.zutil001.utils.InjectorUtils

class NavigationDemoActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_demo)

        Log.d("Blah","NavigationDemoActivity")

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
                    R.id.activity_navigation_demo_content_id, fragmentRequest.fragment, fragmentRequest.tag
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
                Log.d("Blah","root media id ="+rootMediaId)

                rootMediaId?.let { navigateToMediaItem("__ALBUMS__") }
            })

        /**
         * Observe [MainActivityViewModel.navigateToMediaItem] for [Event]s indicating
         * the user has requested to browse to a different [MediaItemData].
         */
        viewModel.navigateToMediaItem1.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { mediaId ->
                navigateToMediaItem1(mediaId)
            }
        })

        viewModel.navigateToMediaItem.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { mediaId ->
                navigateToMediaItem(mediaId)
            }
        })
    }

    private fun navigateToMediaItem(mediaId: String) {
        var fragment: NavigationDemoFragment? = getBrowseFragment(mediaId)
        if (fragment == null) {
            Log.d("Blah","Navigate to DemoFragment")

//            fragment = MediaItemFragment.newInstance("__ALBUMS__")
            fragment = NavigationDemoFragment.newInstance(mediaId)
            // If this is not the top level media (root), we add it to the fragment
            // back stack, so that actionbar toggle and Back will work appropriately:
            viewModel.showFragment(fragment, !isRootId(mediaId), mediaId)
        }
    }

    private fun navigateToMediaItem1(mediaId: String) {
        var fragment: MediaItemFragment? = getBrowseFragment1(mediaId)
        if (fragment == null) {
            Log.d("Blah","Navigate to DemoFragment")

//            fragment = MediaItemFragment.newInstance("__ALBUMS__")
            fragment = MediaItemFragment.newInstance(mediaId)
            // If this is not the top level media (root), we add it to the fragment
            // back stack, so that actionbar toggle and Back will work appropriately:
            viewModel.showFragment(fragment, !isRootId(mediaId), mediaId)
        }
    }

//    private fun isRootId(mediaId: String) = mediaId == viewModel.rootMediaId.value
    private fun isRootId(mediaId: String) = mediaId == "__ALBUMS__"

    private fun getBrowseFragment1(mediaId: String): MediaItemFragment? {
        return supportFragmentManager.findFragmentByTag(mediaId) as MediaItemFragment?
    }

    private fun getBrowseFragment(mediaId: String): NavigationDemoFragment? {
        return supportFragmentManager.findFragmentByTag(mediaId) as NavigationDemoFragment?
    }
}