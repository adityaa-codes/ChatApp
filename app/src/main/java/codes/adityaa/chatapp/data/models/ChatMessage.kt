package codes.adityaa.chatapp.data.models

import codes.adityaa.chatapp.data.enums.MessageType

data class ChatMessage(
val message:String,
val peerID:String,
val timeStamp:String,
val userName:String,
val messageRowType: MessageType
)