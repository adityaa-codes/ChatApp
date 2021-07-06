package codes.adityaa.chatapp.core

import sg.com.temasys.skylink.sdk.rtc.SkylinkConfig

class ChatConfigImpl : ChatConfig {
    override fun getSkylinkConfig(): SkylinkConfig {
        val skylinkConfig = SkylinkConfig()

        skylinkConfig.skylinkRoomSize = SkylinkConfig.SkylinkRoomSize.SMALL
        skylinkConfig.preferredSocketTransport = SkylinkConfig.SocketTransport.WEBSOCKET
        skylinkConfig.networkTransport = SkylinkConfig.NetworkTransport.UDP
        skylinkConfig.setP2PMessaging(false)
        skylinkConfig.setFileTransfer(false)
        skylinkConfig.setDataTransfer(false)
        skylinkConfig.isEnableVideoHwAcceleration = false
        skylinkConfig.isEnableH264HighProfile = false
        skylinkConfig.isEnableIntelVp8Encoder = false
        skylinkConfig.setAudioStereo(false)
        skylinkConfig.setAudioAutoGainControl(false)
        skylinkConfig.setAudioEchoCancellation(false)
        skylinkConfig.setAudioHighPassFilter(false)
        skylinkConfig.setAudioNoiseSuppression(false)
        skylinkConfig.isMirrorLocalFrontCameraView = false
        skylinkConfig.isReportVideoResolutionOnVideoChange = false
        skylinkConfig.isReportVideoResolutionUntilStable = false
        skylinkConfig.videoResNumCheckStable = 5
        skylinkConfig.videoResNumWaitMs = 275
        skylinkConfig.maxAudioBitrate = -1
        skylinkConfig.maxVideoBitrate = -1
        skylinkConfig.maxDataBitrate = -1
        skylinkConfig.isAllowTurn = true
        skylinkConfig.isAllowStun = true
        skylinkConfig.isAllowHost = true
        skylinkConfig.isAllowIceRestart = true
        skylinkConfig.setMultitrackCreateEnable(true);
        skylinkConfig.reconnectAttempts = -1
        skylinkConfig.reconnectionDelay = 1000




        skylinkConfig.setAudioVideoSendConfig(SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO)
        skylinkConfig.setAudioVideoReceiveConfig(SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO)

        skylinkConfig.skylinkRoomSize = SkylinkConfig.SkylinkRoomSize.LARGE

        val maxRemotePeer = 10
        skylinkConfig.setMaxRemotePeersConnected(
            maxRemotePeer,
            SkylinkConfig.AudioVideoConfig.NO_AUDIO_NO_VIDEO
        )

        skylinkConfig.setTimeout(
            SkylinkConfig.SkylinkAction.GET_MESSAGE_STORED,
            30 * 1000
        )

        return skylinkConfig
    }
}