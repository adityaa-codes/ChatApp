package codes.adityaa.chatapp.ui.adapters

import android.icu.text.RelativeDateTimeFormatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codes.adityaa.chatapp.R
import codes.adityaa.chatapp.data.enums.MessageType
import codes.adityaa.chatapp.data.models.ChatMessage

class ChatAdapter(
    private val messageArrayList: ArrayList<ChatMessage>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == MessageType.CHAT_LOCAL_GRP_SIG.ordinal) {
            LocalMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_local, parent, false)
            )
        } else {
            RemoteMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_remote, parent, false)
            )

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = messageArrayList[position]
        if(currentItem.messageRowType.ordinal == MessageType.CHAT_LOCAL_GRP_SIG.ordinal){
            (holder as LocalMessageViewHolder).bind(position)
        }else{
            (holder as RemoteMessageViewHolder).bind(position)
        }

    }

    override fun getItemCount(): Int {
        return messageArrayList.size

    }

    inner class LocalMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var messageContent: TextView = itemView.findViewById(R.id.localMessageTV)
        var peerID: TextView = itemView.findViewById(R.id.localPeerIDTV)
        var timeStamp: TextView = itemView.findViewById(R.id.localTimestampTV)

        fun bind(position: Int){
            val currentItem = messageArrayList[position]
            messageContent.text = currentItem.message
            peerID.text = currentItem.peerID
            timeStamp.text = currentItem.timeStamp
        }




    }

    inner class RemoteMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageContent: TextView = itemView.findViewById(R.id.remoteMessageTV)
        var peerID: TextView = itemView.findViewById(R.id.remotePeerIDTV)
        var timeStamp: TextView = itemView.findViewById(R.id.remoteTimestampTV)

        fun bind(position: Int){

            val currentItem = messageArrayList[position]
            messageContent.text = currentItem.message
            peerID.text = currentItem.peerID
            timeStamp.text = currentItem.timeStamp
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = messageArrayList[position]
        return currentItem.messageRowType.ordinal
    }
}