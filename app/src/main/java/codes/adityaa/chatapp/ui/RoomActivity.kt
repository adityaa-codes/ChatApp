package codes.adityaa.chatapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import codes.adityaa.chatapp.core.ChatConfigImpl
import codes.adityaa.chatapp.core.Config
import codes.adityaa.chatapp.core.NetworkManager
import codes.adityaa.chatapp.core.SkylinkService
import codes.adityaa.chatapp.data.models.Room
import codes.adityaa.chatapp.databinding.ActivityRoomBinding
import codes.adityaa.chatapp.ui.adapters.RoomAdapter

class RoomActivity : AppCompatActivity(), RoomAdapter.OnItemClickListener {
    lateinit var binding: ActivityRoomBinding
    lateinit var mRoomList: ArrayList<Room>
    lateinit var roomAdapter: RoomAdapter
//    lateinit var skylinkService: SkylinkService
//    lateinit var networkAware: NetworkManager
//    lateinit var chatConfig: ChatConfigImpl
//    lateinit var config: Config
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    private fun initComponents() {
        //networkAware = NetworkManager(this)
        //chatConfig = ChatConfigImpl()
        //config = Config(this)
        //skylinkService = SkylinkService(networkAware, chatConfig, config, this)
        //skylinkService.initSkylinkConnection()
        mRoomList = ArrayList()
        mRoomList.clear()
        //Please Add Rooms Here
        //Eg
        //mRoomList.add(Room("Room Name","Encryption Key","Encryption Value"))

        mRoomList.add(Room("Room One","room_one","bangalore"))
        mRoomList.add(Room("Room Two","room_two","important"))
        mRoomList.add(Room("Room Three","room_three","taskfirst"))
        mRoomList.add(Room("Room Four","room_four","doesNotMatter"))
        mRoomList.add(Room("Room Five","room_five","itDoesMatter"))
        mRoomList.add(Room("Room Six","room_six","itDefDoesMatter"))
        mRoomList.add(Room("Room Seven","room_seven","whyItsNotWorking"))
        mRoomList.add(Room("Room Eight","room_eight","pleaseWorkPlease"))
        mRoomList.add(Room("Room Nine","room_nine","deleteTheMessagesPlease"))
        val layoutManager = LinearLayoutManager(this)
        roomAdapter = RoomAdapter(mRoomList, this)
        binding.roomRV.setHasFixedSize(true)
        binding.roomRV.layoutManager = layoutManager
        binding.roomRV.adapter = roomAdapter

    }

    override fun onItemClick(position: Int) {
        val currentItem = mRoomList[position]
        val chatIntent: Intent = Intent(this, ChatActivity::class.java)
        chatIntent.putExtra("roomName", currentItem.name)
        chatIntent.putExtra("encryptionKey", currentItem.encryptionKey)
        chatIntent.putExtra("encryptionValue", currentItem.encryptionValue)

        startActivity(chatIntent)
    }
}