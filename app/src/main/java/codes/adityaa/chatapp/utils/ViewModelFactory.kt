package codes.adityaa.chatapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import codes.adityaa.chatapp.ui.viewmodels.ChatViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}