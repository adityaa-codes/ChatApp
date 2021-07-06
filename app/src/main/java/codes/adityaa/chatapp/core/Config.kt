package codes.adityaa.chatapp.core

import android.app.Activity
import codes.adityaa.chatapp.R

class Config(activity: Activity) {
        val appKey: String = activity.getString(R.string.app_key_no_smr)
        val appKeySecret: String = activity.getString(R.string.app_key_secret_no_smr)
        val appKeyDesc: String = activity.getString(R.string.app_key_desc_no_smr)
        val appKeySmr: Boolean = activity.resources.getBoolean(R.bool.is_app_key_smr)



}