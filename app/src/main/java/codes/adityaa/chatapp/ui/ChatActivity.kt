package codes.adityaa.chatapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import codes.adityaa.chatapp.R
import codes.adityaa.chatapp.core.ChatConfigImpl
import codes.adityaa.chatapp.core.Config
import codes.adityaa.chatapp.core.NetworkManager
import codes.adityaa.chatapp.core.SkylinkService
import codes.adityaa.chatapp.data.enums.MessageType
import codes.adityaa.chatapp.data.models.ChatMessage
import codes.adityaa.chatapp.databinding.ActivityChatBinding
import codes.adityaa.chatapp.ui.adapters.ChatAdapter
import codes.adityaa.chatapp.ui.viewmodels.ChatViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    // override val kodein by kodein()
    //private val factory: ViewModelFactory by instance()
    lateinit var viewModel: ChatViewModel
    lateinit var chatAdapter: ChatAdapter
    lateinit var skylinkService: SkylinkService
    lateinit var chatMessageArrayList: ArrayList<ChatMessage>
    lateinit var USER_NAME_CHAT_DEFAULT: String
    lateinit var networkAware: NetworkManager
    lateinit var chatConfig: ChatConfigImpl
    lateinit var config: Config
    var roomName: String = "defaultRoom"
    var encryptionKey: String = "defaultKey"
    var encryptionValue: String = "defaultValue"

    val TAG: String = ChatActivity::class.java.name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        initComponents()


    }

    private fun initComponents() {
        showProgressBar(true)
        networkAware = NetworkManager(this)
        chatConfig = ChatConfigImpl()
        config = Config(this)
        USER_NAME_CHAT_DEFAULT = "adityaa"
        skylinkService = SkylinkService(networkAware, chatConfig, config, this)
        val bundle = Bundle()
        val intentExtras = intent.extras
        if (intentExtras != null) {
            roomName = intentExtras.getString("roomName").toString()
            encryptionKey = intentExtras.getString("encryptionKey").toString()
            encryptionValue = intentExtras.getString("encryptionValue").toString()
        }
        title = roomName
        skylinkService.initSkylinkConnection()
        skylinkService.setEncryptedMap(encryptionKey,encryptionValue)
        skylinkService.connectToRoom(roomName)

        chatMessageArrayList = ArrayList()
        chatMessageArrayList.clear()
        //Defining the ArrayAdapter to set items to ListView
        chatAdapter = ChatAdapter(
            //processGetChatCollection()
            chatMessageArrayList
        )
        binding.messagesRV.layoutManager = LinearLayoutManager(this)
        binding.messagesRV.adapter = chatAdapter
        showProgressBar(false)
        binding.sendMessageBtn.setOnClickListener {
            val message = binding.messagesTIET.text.toString()
            processSendServerMessage(message)
            binding.messagesTIET.text?.clear()
        }


    }

    private fun processSendServerMessage(message: String) {


        var messageRowType: MessageType? = null
        //add message to list view for displaying

        skylinkService.sendServerMessage(null, message)
        messageRowType = MessageType.CHAT_LOCAL_GRP_SIG

        Log.d(TAG, "processSendServerMessage: Peer ID" + skylinkService.getPeerId())
        addLocalMessage(
            skylinkService.getPeerId().toString(), USER_NAME_CHAT_DEFAULT,
            message, Date().time, messageRowType
        )
    }

    private fun addLocalMessage(
        localPeerId: String, userName: String, message: String,
        timeStamp: Long, messageRowType: MessageType
    ) {
        val messageModel = ChatMessage(
            message,
            localPeerId,
            date2DayTime(Date(timeStamp)),
            //getDefaultShortTimeStamp(),
            userName,
            messageRowType
        )

        chatMessageArrayList.add(messageModel)
        chatAdapter.notifyDataSetChanged()
    }

    fun addRemoteMessage(
        remotePeerId: String, userName: String, message: String,
        timeStamp: String?, messageRowType: MessageType
    ) {
        val messageModel = ChatMessage(
            message,
            remotePeerId,
            timeStamp.toString(),
            userName,
            messageRowType
        )

        chatMessageArrayList.add(messageModel)
        chatAdapter.notifyDataSetChanged()
    }

    fun getDefaultShortTimeStamp(date: Date): String {

        val SHORT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

        val tz = TimeZone.getDefault()
        val df: DateFormat =
            SimpleDateFormat(SHORT_TIME_FORMAT, Locale.ENGLISH)
        df.timeZone = tz
        return df.format(date)
    }

    private val yyyy_MM_dd_HH_mm = SimpleDateFormat(
        "yyyy-MM-dd HH:mm", Locale.getDefault()
    )
    private val HHmm = SimpleDateFormat(
        "HH:mm",
        Locale.getDefault()
    ) /*from w  w  w .  j a  v  a  2s .  co m*/
    private val MM_dd_HHmm = SimpleDateFormat(
        "MM-dd HH:mm", Locale.getDefault()
    )

    private fun date2DayTime(oldTime: Date): String {
        val newTime = Date()
        try {
            val cal = Calendar.getInstance()
            cal.time = newTime
            val oldCal = Calendar.getInstance()
            oldCal.time = oldTime
            val oldYear = oldCal[Calendar.YEAR]
            val year = cal[Calendar.YEAR]
            val oldDay = oldCal[Calendar.DAY_OF_YEAR]
            val day = cal[Calendar.DAY_OF_YEAR]
            if (oldYear == year) {
                val value = oldDay - day
                return if (value == -1) {
                    "Yesterday " //+ HHmm.format(oldTime)
                } else if (value == 0) {
                    "Today " //+ HHmm.format(oldTime)
                } else if (value == 1) {
                    "Tomorrow "// + HHmm.format(oldTime)
                } else {
                    MM_dd_HHmm.format(oldTime)
                }
            }
        } catch (e: Exception) {
        }
        return yyyy_MM_dd_HH_mm.format(oldTime)
    }

    private fun getDeviceModel(): String {
        return Build.MANUFACTURER + "_" + Build.MODEL + "_"
    }


    public fun processServerMessageReceived(
        remotePeerId: String,
        message: Any?,
        isPrivate: Boolean,
        timeStamp: Long
    ) {
        showProgressBar(true)
        if (message == null) return
        var userName: String
        var messageContent: String?


        if (message is String) {
            userName = skylinkService.getPeerId().toString()
            messageContent = message

            addRemoteMessage(
                remotePeerId,
                userName,
                messageContent,
                getDefaultShortTimeStamp(Date(timeStamp)),
                MessageType.CHAT_REMOTE_GRP_SIG
            )
        }

        showProgressBar(false)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                getStoredMessages()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getStoredMessages() {
        showProgressBar(true)
        skylinkService.getStoredMessagesLocal()
        showProgressBar(false)
    }

    private fun showProgressBar(status:Boolean){
        binding.progressBar.isVisible = status
        binding.messagesRV.isVisible = !status
    }


}