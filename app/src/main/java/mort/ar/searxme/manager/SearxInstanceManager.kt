package mort.ar.searxme.manager

import android.content.Context
import android.content.SharedPreferences

val backupInstance = "https://anonyk.com/"

class SearxInstanceManager(mContext: Context) {

    private val mInstances: SharedPreferences =
        mContext.getSharedPreferences("Instances", Context.MODE_PRIVATE)

    val instances = mutableMapOf<String, String>()

    init {
        val allInstances = mInstances.all
        when {
            allInstances.isNotEmpty() -> allInstances.keys.forEach { instances[it] = it }
            else -> addInstance(backupInstance)
        }
    }

    fun addInstance(instance: String) {
        instances[instance] = instance
        mInstances
            .edit()
            .putString(instance, instance)
            .apply()
    }

    fun removeInstance(instance: String) {
        instances.remove(instance)
    }

    fun getFirstInstance() = instances.keys.first()

}
