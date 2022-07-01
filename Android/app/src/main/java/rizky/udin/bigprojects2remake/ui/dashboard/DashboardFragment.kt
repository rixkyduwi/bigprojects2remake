package rizky.udin.bigprojects2remake.ui.dashboard

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chatbot.*
import kotlinx.android.synthetic.main.activity_dasboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.*
import rizky.udin.bigprojects2remake.data.Message
import rizky.udin.bigprojects2remake.databinding.FragmentDashboardBinding
import rizky.udin.bigprojects2remake.utils.BotResponse
import rizky.udin.bigprojects2remake.utils.Constants
import rizky.udin.bigprojects2remake.utils.Time


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val TAG = "MainActivity"

    //You can ignore this messageList if you're coming from the tutorial,
    // it was used only for my personal debugging
    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter = MessagingAdapter()

        binding.webView.webViewClient = WebViewClient()
        // this will load the url of the website
        binding.webView.loadUrl("http://192.168.100.63:5000/chatbotandroid")

        // this will enable the javascript settings
        binding.webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}