package codes.adityaa.chatapp.core

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import codes.adityaa.chatapp.data.enums.MessageType
import codes.adityaa.chatapp.ui.ChatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sg.com.temasys.skylink.sdk.listener.LifeCycleListener
import sg.com.temasys.skylink.sdk.listener.MessagesListener
import sg.com.temasys.skylink.sdk.listener.OsListener
import sg.com.temasys.skylink.sdk.listener.RemotePeerListener
import sg.com.temasys.skylink.sdk.rtc.*
import sg.com.temasys.skylink.sdk.rtc.SkylinkEvent.CONTEXT_DESCRIPTION
import java.util.*


class SkylinkService(
    private val networkAware: NetworkAware,
    private val chatConfig: ChatConfig,
    private val config: Config,
    private val context: Context
) : LifeCycleListener, MessagesListener, RemotePeerListener, OsListener {

    val TAG: String = SkylinkService::class.java.name
    var remotePeerId = " "
    var localPeerID: String? = null
    private val PEER_USER_ID = "senderId"
    private val PEER_USERNAME = "senderName"
    private val MESSAGE_CONTENT = "data"
    private val TIMESTAMP = "timeStamp"


    var skylinkConnection: SkylinkConnection? = null


    override fun onConnectToRoomSucessful() {
        Log.d(TAG, "onConnectToRoomSuccessful");
        if (skylinkConnection != null) {
            localPeerID = skylinkConnection?.localPeerId
        } else {
            Log.d(TAG, "onConnectToRoomSucessful:Skylink Null ")
        }
        Log.d(TAG, "initSkylinkConnection:LocalPeerIDAfterRoomConnect $localPeerID")
        Log.d(TAG, "initSkylinkConnection:RoomName ${skylinkConnection?.roomId}")
        //setEncryptedMap()
        Log.d(TAG, "onConnectToRoomSuccessful: ${skylinkConnection?.selectedSecretId}")

        //getStoredMessagesLocal()
    }

    override fun onConnectToRoomFailed(p0: String?) {
        Log.d(TAG, "onConnectToRoomFailed($p0)");
    }

    override fun onDisconnectFromRoom(p0: SkylinkEvent?, p1: String?) {
        Log.d(TAG, "onDisconnectFromRoom($p0, message: $p1)");
    }

    override fun onChangeRoomLockStatus(p0: Boolean, p1: String?) {

    }

    override fun onReceiveInfo(p0: SkylinkInfo?, p1: HashMap<String, Any>) {
        Log.d(
            TAG,
            "onReceiveInfo(skylinkInfo: " + p0.toString()
                    + ", details: " + p1[CONTEXT_DESCRIPTION]
        )
    }

    override fun onReceiveWarning(p0: SkylinkError?, p1: HashMap<String, Any>) {
        val contextDescriptionString = p1[CONTEXT_DESCRIPTION]
        Log.d(
            TAG,
            "onReceiveWarning(skylinkInfo: " + p0.toString() + ", details: " + contextDescriptionString
        )
    }

    override fun onReceiveError(p0: SkylinkError?, p1: HashMap<String, Any>) {
        val contextDescriptionString = p1[CONTEXT_DESCRIPTION]
        Log.d(
            TAG,
            "onReceiveError(skylinkInfo: " + p0.toString() + ", details: " + contextDescriptionString
        )

    }

    override fun onReceiveServerMessage(
        message: Any?,
        isPublic: Boolean,
        timeStamp: Long?,
        remotePeerId: String?
    ) {
        Log.d(TAG, "onReceiveServerMessage: $message")
        (context as ChatActivity).processServerMessageReceived(
            remotePeerId!!,
            message,
            isPublic,
            timeStamp!!
        )
    }

    override fun onReceiveP2PMessage(
        message: Any?,
        isPublic: Boolean,
        timeStamp: Long?,
        remotePeerId: String?
    ) {
        Log.d(TAG, "onReceiveP2PMessage: $message")
        (context as ChatActivity).processServerMessageReceived(
            remotePeerId!!,
            message,
            isPublic,
            timeStamp!!
        )

    }

    override fun onReceiveRemotePeerJoinRoom(peerId: String?, p1: UserInfo?) {
        remotePeerId = peerId!!
        Log.d(TAG, "onReceiveRemotePeerJoinRoom: $peerId")

    }

    override fun onConnectWithRemotePeer(p0: String?, p1: UserInfo?, p2: Boolean) {
        Log.d(TAG, "onConnectWithRemotePeer: $p0")

    }

    override fun onRefreshRemotePeerConnection(
        p0: String?,
        p1: UserInfo?,
        p2: Boolean,
        p3: Boolean
    ) {

    }

    override fun onReceiveRemotePeerUserData(p0: Any?, p1: String?) {

    }

    override fun onOpenRemotePeerDataConnection(p0: String?) {

    }

    override fun onDisconnectWithRemotePeer(p0: String?, p1: UserInfo?, p2: Boolean) {

    }

    override fun onReceiveRemotePeerLeaveRoom(p0: String?, p1: SkylinkInfo?, p2: UserInfo?) {

    }

    override fun onErrorForRemotePeerConnection(p0: SkylinkError?, p1: HashMap<String, Any>?) {

    }

    override fun onRequirePermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onRequirePermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onGrantPermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onGrantPermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onDenyPermission(p0: Intent?, p1: Int, p2: SkylinkInfo?) {

    }

    override fun onDenyPermission(p0: Array<out String>?, p1: Int, p2: SkylinkInfo?) {

    }


    fun initSkylinkConnection() {

        //Get the user config for connection
        val skylinkConfig: SkylinkConfig = chatConfig.getSkylinkConfig()

        skylinkConnection = SkylinkConnection.getInstance()

        skylinkConnection?.init(skylinkConfig, context, object : SkylinkCallback {
            override fun onError(p0: SkylinkError?, p1: HashMap<String, Any>?) {
                val contextDescription = p1?.get(SkylinkEvent.CONTEXT_DESCRIPTION) as String
                Log.e("SkylinkCallback", contextDescription)
                onConnectToRoomFailed(contextDescription)
            }
        })
        Log.d(TAG, "initSkylinkConnection:${skylinkConnection?.selectedSecretId} ")
        skylinkConnection?.lifeCycleListener = this
        skylinkConnection?.remotePeerListener = this
        skylinkConnection?.osListener = this
        skylinkConnection?.messagesListener = this
        skylinkConnection?.isEnableLogs = true


    }

    fun connectToRoom(ROOM_NAME: String,encryptionKey:String,encryptionValue: String) {
        val MY_USER_NAME = "adityaa"

        skylinkConnection?.connectToRoom(
            config.appKey, config.appKeySecret, ROOM_NAME, MY_USER_NAME,
            object : SkylinkCallback {
                override fun onError(error: SkylinkError?, details: HashMap<String?, Any?>) {
                    val contextDescription = details[SkylinkEvent.CONTEXT_DESCRIPTION] as String?
                    Log.e("SkylinkCallback", contextDescription!!)

                }
            })
        setEncryptedMap(encryptionKey,encryptionValue)
        Log.d(TAG, "connectToRoom:${skylinkConnection?.selectedSecretId} ")

        //Log.d(TAG, "connectToRoom: " + skylinkConnection?.localPeerId)


    }


    fun sendServerMessage(remotePeerId: String?, message: Any?) {
        Log.d(TAG, "sendServerMessage:$remotePeerId $message")
        if (skylinkConnection != null) {
            Log.d(TAG, "sendServerMessage Not Null:$remotePeerId $message")



            skylinkConnection?.sendServerMessage(message, remotePeerId, object : SkylinkCallback {
                override fun onError(error: SkylinkError, details: HashMap<String, Any>) {
                    // processMessageSendFailed()
                    val contextDescription = details[SkylinkEvent.CONTEXT_DESCRIPTION] as String?
                    Log.e("SkylinkCallback", contextDescription!!)
                }
            })
        }
    }

    fun getPeerId(): String? {
        Log.d(TAG, "getPeerId:${localPeerID} ")
        return localPeerID
    }

//    fun getStoredMessagesLocal() {
//        if (skylinkConnection != null) {
//            skylinkConnection?.getStoredMessages(object : SkylinkCallback.StoredMessages {
//                override fun onObtainStoredMessages(
//                    messages: JSONArray?,
//                    p1: MutableMap<SkylinkError, JSONArray>?
//                ) {
//                    Log.d(TAG, "onObtainStoredMessages:$messages ")
//                    Log.d(TAG, "onObtainStoredMessages:$p1 ")
//                }
//            })
//        }
//    }
    fun getStoredMessagesLocal() {
        if (skylinkConnection != null) {
            skylinkConnection?.getStoredMessages(object : SkylinkCallback.StoredMessages {
                override fun onObtainStoredMessages(
                    messages: JSONArray?,
                    p1: MutableMap<SkylinkError?, JSONArray?>?
                ) {
                    Log.d(TAG, "onObtainStoredMessages:$messages ")
                    Log.d(TAG, "onObtainStoredMessages:$p1 ")
                    Toast.makeText(context, messages.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, p1.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun processStoredMessagesResult(messages: JSONArray?) {
        // remove the UI for getting message history in the dataset
        if (messages == null || messages.length() == 0) {
            return
        }
        var message: Any?
        Log.d(TAG, "processStoredMessagesResult:$messages")
        // process stored messages
        for (i in 0 until messages.length()) {
            var userName: String
            var messageContent: String
            var timeStamp: String?
            var messageJson: JSONObject? = null
            var messageArray: JSONArray? = null
            message = try {
                messages[i]
            } catch (e: JSONException) {
                e.printStackTrace()
                continue
            }
            if (message is JSONArray) {
                messageArray = message
                try {
                    messageJson = messageArray.getJSONObject(0)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (message is JSONObject) {
                messageJson = message
            }
            try {
                userName =
                    messageJson!!.getString(PEER_USERNAME)
                Log.d(TAG, "processStoredMessagesResult:Username $userName")
            } catch (e: JSONException) {
                // use peerId as senderName if userName is not present
                try {
                    userName =
                        messageJson!!.getString(PEER_USER_ID)
                    Log.d(TAG, "processStoredMessagesResult:UserId $userName")
                } catch (e1: JSONException) {
                    userName = "N/A"
                }
            }
            messageContent = try {
                messageJson!!.getString(MESSAGE_CONTENT)
            } catch (e: JSONException) {
                "N/A"
            }
            timeStamp = try {
                val timeStampLong =
                    messageJson!!.getLong(TIMESTAMP)
                (context as ChatActivity).getDefaultShortTimeStamp(Date(timeStampLong))
            } catch (e: JSONException) {
                "Not correct format as Long or N/A"
            }
            (context as ChatActivity).addRemoteMessage(
                userName,
                userName,
                messageContent,
                timeStamp,
                MessageType.CHAT_REMOTE_GRP_SIG
            )
        }
    }

    private fun setEncryptedMap(encryptionKey:String, encryptionValue: String) {
        val encryptionMap: MutableMap<String?, String?> = HashMap()
        encryptionMap[encryptionKey] = encryptionValue
        //Log.d(TAG, "setEncryptedMap: $encryptionKey - $encryptionValue")
        skylinkConnection?.isMessagePersist = true
        skylinkConnection?.setEncryptSecretsMap(encryptionMap)
        skylinkConnection?.selectedSecretId = encryptionMap[encryptionKey]

    }


}


