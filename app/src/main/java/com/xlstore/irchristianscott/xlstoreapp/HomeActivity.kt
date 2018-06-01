package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.NavigationView
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SearchViewCompat.setOnQueryTextListener
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.arlib.floatingsearchview.FloatingSearchView
import com.squareup.picasso.Picasso
import com.xlstore.irchristianscott.xlstoreapp.models.UserModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.menu_header.*
import kotlinx.android.synthetic.main.menu_text_likes.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var menuFollowers: TextView
    private lateinit var menuFollowing: TextView
    private lateinit var menuName: TextView
    private lateinit var menuProfileImage: CircleImageView
    private lateinit var searchBox: FloatingSearchView
    private val userDataManager = UserDataManager()


    private val mOnNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(ProductsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_posts -> {
                changeFragment(PostsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(NotificationsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile ->{
                changeFragment(ProfileMainFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val helpers = Helpers()

        val connectionStatus: Boolean = helpers.checkConnection(this)

        if (connectionStatus){
            changeFragment(ProductsFragment())
        } else {
            networkError.visibility = View.VISIBLE
        }

        drawerLayout = findViewById<DrawerLayout>(R.id.container) as DrawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val navigationView: NavigationView = findViewById<NavigationView>(R.id.navigation_view) as NavigationView
        val header: View = navigationView.getHeaderView(0)

        getSetUserProfileData(header, navigationView)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Search Layout

        searchBox = findViewById<FloatingSearchView>(R.id.search_box) as FloatingSearchView
        searchBox.focusedChild

        searchBox.setOnQueryChangeListener {
            oldQuery, newQuery ->

        }

        searchBox.setOnLeftMenuClickListener(object: FloatingSearchView.OnLeftMenuClickListener{

            override fun onMenuClosed() {

            }

            override fun onMenuOpened() {

            }
        })

        searchBox.setOnBindSuggestionCallback {
            suggestionView, leftIcon, textView, item, itemPosition ->

            for (x in 0..20){
                textView.text = "Product $x"
                leftIcon.resources.getDrawable(R.drawable.ic_history_black_24dp)
            }
        }

        searchBox.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.search_close -> {

                    findViewById<FrameLayout>(R.id.fragment_container).visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.search_layout).visibility = View.INVISIBLE
                    findViewById<BottomNavigationView>(R.id.navigation).visibility = View.VISIBLE
                    searchBox.setSearchFocused(false)
                    supportActionBar!!.show()

                }
                R.id.search_settings -> {

                }
                R.id.search_voice -> {

                }
            }

        }


    }

    private fun changeFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("SetTextI18n")
    private fun getSetUserProfileData(header: View, navigationView: NavigationView){

        val user: UserModel? = userDataManager.getLoggedInUserObject(this@HomeActivity)

        menuFollowers = header.findViewById<TextView>(R.id.menu_followers) as TextView
        menuFollowing = header.findViewById<TextView>(R.id.menu_following) as TextView
        menuProfileImage = header.findViewById<CircleImageView>(R.id.menu_profile) as CircleImageView
        menuName = header.findViewById<TextView>(R.id.menu_name) as TextView

        menuFollowers.text = "${user?.followers.toString()} ${menuFollowers.text}"
        menuFollowing.text = "${user?.following.toString()} ${menuFollowing.text}"
        menuName.text = user?.name

        Picasso.get().load(user?.getUserProfileImage()).into(menuProfileImage)

        if (navigationView.menu.getItem(3).hasSubMenu()) {

            val menu: Menu = navigationView.menu.getItem(3).subMenu

            val likeView: View = menu.getItem(0).actionView
            likeView.findViewById<TextView>(R.id.menu_sum_likes).text = user?.likes.toString()

            val dislikeView: View = menu.getItem(1).actionView
            dislikeView.findViewById<TextView>(R.id.menu_sum_dislikes).text = user?.dislikes.toString()

            val companyView: View = menu.getItem(2).actionView
            companyView.findViewById<TextView>(R.id.menu_sum_companies).text = user?.companies.toString()

            val commentView: View = menu.getItem(3).actionView
            commentView.findViewById<TextView>(R.id.menu_sum_comments).text = user?.comments.toString()

            val repliesView: View = menu.getItem(4).actionView
            repliesView.findViewById<TextView>(R.id.menu_sum_replies).text = user?.replies.toString()

            val categoriesView: View = menu.getItem(5).actionView
            categoriesView.findViewById<TextView>(R.id.menu_sum_categories).text = user?.categories.toString()

            val tradeView: View = menu.getItem(6).actionView
            tradeView.findViewById<TextView>(R.id.menu_sum_trades).text = user?.trades.toString()

            val billsView: View = menu.getItem(7).actionView
            billsView.findViewById<TextView>(R.id.menu_sum_bills).text = user?.bills_carts.toString()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId){
            R.id.menu_search -> {

                findViewById<FrameLayout>(R.id.fragment_container).visibility = View.INVISIBLE
                findViewById<LinearLayout>(R.id.search_layout).visibility = View.VISIBLE
                findViewById<BottomNavigationView>(R.id.navigation).visibility = View.INVISIBLE
                searchBox.setSearchFocused(true)
                supportActionBar!!.hide()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}