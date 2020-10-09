import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yan.ahtbibleaudio001.MediaItemAdapter
import com.yan.ahtbibleaudio001.R
import com.yan.ahtbibleaudio001.databinding.FragmentMediaitemListBinding
import com.yan.ahtbibleaudio001.databinding.FragmentNavigationDemoBinding
import com.yan.ahtbibleaudio001.models.drawer.NaviModel
import com.yan.ahtbibleaudio001.viewmodels.MainActivityViewModel
import com.yan.ahtbibleaudio001.viewmodels.MediaItemFragmentViewModel
import com.yan.ahtbibleaudio001.views.adapters.NaviAdapter
import com.yan.ahtbibleaudio001.views.clickEvent.ClickListener
import com.yan.ahtbibleaudio001.views.clickEvent.RecyclerTouchListener
import com.yan.ahtbibleaudio001.views.fragments.AudioCatFragment
import com.yan.ahtbibleaudio001.views.fragments.MediaItemFragment
import com.yan.ahtbibleaudio001.zutil001.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_audio.*

class NavigationDemoFragment : Fragment() {

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel> {
        InjectorUtils.provideMainActivityViewModel(requireContext())
    }
    private val mediaItemFragmentViewModel by viewModels<MediaItemFragmentViewModel> {
        InjectorUtils.provideMediaItemFragmentViewModel(requireContext(), mediaId)
    }

    private lateinit var mediaId: String
    private lateinit var binding: FragmentNavigationDemoBinding

    private val listAdapter = MediaItemAdapter { clickedItem ->
        mainActivityViewModel.mediaItemClicked(clickedItem)
    }

    companion object {
        fun newInstance(mediaId: String): NavigationDemoFragment {

            return NavigationDemoFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDIA_ID_ARG_1, mediaId)
                }
            }
        }
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

    val list1 = arrayListOf<NaviModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Blah","NavigationDemoFragment")

        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Checking for fragment count on back stack
                    if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                        // Go to the previous fragment
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        // Exit the app
                        requireActivity().finish()
                    }
                }

            }
        })


        //

        drawerLayout = view.findViewById(R.id.drawer_layout)

        // Set the toolbar
//        setSupportActionBar(audio_main_toolbar)


        // Setup Recyclerview's Layout
        val itemDecor = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireActivity(), R.drawable.rv_divider)?.let { itemDecor.setDrawable(
            it
        ) }
        navigation_rv.addItemDecoration(itemDecor)
        navigation_rv.layoutManager = LinearLayoutManager(requireActivity())
        navigation_rv.setHasFixedSize(true)

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity(),
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        when (position) {
                            0 -> {
                                // # Home Fragment
//                        supportActionBar?.title = "Home Fragment"
                                (activity as AppCompatActivity).supportActionBar?.title = "Home Fragment"
                                val bundle = Bundle()
                                bundle.putString("fragmentName", "Home Fragment")
                                val homeFragment = AudioCatFragment()
                                homeFragment.arguments = bundle
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_audio_content_id, homeFragment).commit()
                            }
                            1 -> {
//                                activity.supportFragmentManager.beginTransaction().remove().commit()
                                // # Music Fragment
//                        supportActionBar?.title = "Music Fragment"
                                (activity as AppCompatActivity).supportActionBar?.title = "Music Fragment"
                                val bundle = Bundle()
                                bundle.putString("fragmentName", "Music Fragment")
                                val musicFragment = AudioCatFragment()
                                musicFragment.arguments = bundle
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_audio_content_id, musicFragment).commit()
                            }
//                            2 -> {
////                        supportActionBar?.title = "Movies Fragment"
//                                // # Movies Fragment
//                                val bundle = Bundle()
//                                bundle.putString("fragmentName", "Movies Fragment")
//                                val moviesFragment = AudioCatFragment()
//                                moviesFragment.arguments = bundle
//                                requireActivity().supportFragmentManager.beginTransaction()
//                                    .replace(R.id.activity_audio_content_id, moviesFragment)
//                                    .commit()
//                            }
//                            3 -> {
////                        supportActionBar?.title = "Books Fragment"
//                                // # Books Fragment
//                                val bundle = Bundle()
//                                bundle.putString("fragmentName", "Books Fragment")
//                                val booksFragment = AudioCatFragment()
//                                booksFragment.arguments = bundle
//                                requireActivity().supportFragmentManager.beginTransaction()
//                                    .replace(R.id.activity_audio_content_id, booksFragment).commit()
//                            }
//                            4 -> {
////                        supportActionBar?.title = "Profile Fragment"
//                                // # Books Fragment
//                                val bundle = Bundle()
//                                bundle.putString("fragmentName", "Profile Fragment")
//                                val profileFragment = AudioCatFragment()
//                                profileFragment.arguments = bundle
//                                requireActivity().supportFragmentManager.beginTransaction()
//                                    .replace(R.id.activity_audio_content_id, profileFragment)
//                                    .commit()
//                            }
//                            5 -> {
//                                // # Settings Fragment
//                                val bundle = Bundle()
//                                bundle.putString("fragmentName", "Settings Fragment")
//                                val settingsFragment = AudioCatFragment()
//                                settingsFragment.arguments = bundle
//                                requireActivity().supportFragmentManager.beginTransaction()
//                                    .replace(R.id.activity_audio_content_id, settingsFragment)
//                                    .commit()
//                            }
//                            6 -> {
//                                // # Open URL in browser
//                                val uri: Uri = Uri.parse("https://johnc.co/fb")
//                                val intent = Intent(Intent.ACTION_VIEW, uri)
//                                startActivity(intent)
//                            }
                        }
//                        // Don't highlight the 'Profile' and 'Like us on Facebook' item row
//                        if (position != 6 && position != 4) {
//                            updateAdapter(position)
//                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            //Your Code
                            drawerLayout.closeDrawer(GravityCompat.START)

                        }, 200)
                    }
                })
        )

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
//        updateAdapter(0)

        (activity as AppCompatActivity).supportActionBar?.title = "Home Fragment"
//        supportActionBar?.title = "Home Fragment"
        // Set 'Home' as the default fragment when the app starts
        val bundle = Bundle()
        bundle.putString("fragmentName", "Home Fragment")
        val homeFragment = AudioCatFragment()
        homeFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.activity_audio_content_id, homeFragment).commit()


        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            requireActivity(),
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
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                        requireActivity().currentFocus?.windowToken,
                        0
                    )
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                        requireActivity().currentFocus!!.windowToken,
                        0
                    )
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        // Set background of Drawer
        navigation_layout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorPrimary
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val rootView = inflater.inflate(R.layout.fragment_navigation_demo, container, false)
//        return rootView
        Log.d("Blah","NavigationDemoFragment OnCreateView")

        binding = FragmentNavigationDemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Always true, but lets lint know that as well.
//        mediaId = arguments?.getString(MEDIA_ID_ARG_1) ?: return
        mediaId = "__ALBUMS__"

        Log.d("MEDIAID", mediaId)

        mediaItemFragmentViewModel.mediaItems.observe(viewLifecycleOwner,
            Observer { list ->
                binding.loadingSpinner.visibility =
                    if (list?.isNotEmpty() == true) View.GONE else View.VISIBLE
                Log.d("Blah","fdsfafdsf")
                listAdapter.submitList(list)

            })
        mediaItemFragmentViewModel.networkError.observe(viewLifecycleOwner,
            Observer { error ->
                binding.networkError.visibility = if (error) View.VISIBLE else View.GONE
            })

        // Set the adapter
        binding.navigationRv.adapter = listAdapter

    }

//    /*****************************************************/
    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NaviAdapter(list1, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

//        fun onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            // Checking for fragment count on back stack
//            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
//                // Go to the previous fragment
//                requireActivity().supportFragmentManager.popBackStack()
//            } else {
//                // Exit the app
//
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }

}

private const val MEDIA_ID_ARG_1 = "com.example.android.uamp.fragments.MediaItemFragment.MEDIA_ID_1"