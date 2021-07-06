package codes.adityaa.chatapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codes.adityaa.chatapp.R
import codes.adityaa.chatapp.data.models.Room

class RoomAdapter(roomList: ArrayList<Room>,val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    var roomMutableList = roomList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(v)

    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentItem = roomMutableList[position]
        holder.roomName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return roomMutableList.size
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var roomName: TextView = itemView.findViewById(R.id.roomName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}