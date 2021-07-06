package codes.adityaa.chatapp

import android.app.Application
import codes.adityaa.chatapp.core.NetworkManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ChatAppApplication : Application(),KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@ChatAppApplication))

    }
}