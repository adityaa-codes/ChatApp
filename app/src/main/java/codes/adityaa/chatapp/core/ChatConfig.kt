package codes.adityaa.chatapp.core

import sg.com.temasys.skylink.sdk.rtc.SkylinkConfig

interface ChatConfig {
    fun getSkylinkConfig(): SkylinkConfig
}