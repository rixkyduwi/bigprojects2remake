package rizky.udin.bigprojects2remake

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.*
import rizky.udin.bigprojects2remake.R
import rizky.udin.bigprojects2remake.data.Message
import rizky.udin.bigprojects2remake.databinding.ActivityDasboardBinding
import rizky.udin.bigprojects2remake.ui.dashboard.MessagingAdapter
import rizky.udin.bigprojects2remake.utils.BotResponse
import rizky.udin.bigprojects2remake.utils.Constants.OPEN_GOOGLE
import rizky.udin.bigprojects2remake.utils.Constants.OPEN_SEARCH
import rizky.udin.bigprojects2remake.utils.Constants.RECEIVE_ID
import rizky.udin.bigprojects2remake.utils.Constants.SEND_ID
import rizky.udin.bigprojects2remake.utils.Time

class BottomNav : AppCompatActivity() {

    private lateinit var binding: ActivityDasboardBinding
    private val TAG = "BottomNav"

    //You can ignore this messageList if you're coming from the tutorial,
    // it was used only for my personal debugging
    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDasboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        navView.setupWithNavController(navController)
    }
}