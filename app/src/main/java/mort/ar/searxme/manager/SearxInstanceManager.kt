package mort.ar.searxme.manager

import android.content.Context
import android.content.SharedPreferences
import mort.ar.searxme.access.SearxDatabase

val backupInstance = "https://anonyk.com/"

class SearxInstanceManager(context: Context) {

    private val mInstances: SharedPreferences = context.getSharedPreferences("Instances", Context.MODE_PRIVATE)
    private val mDatabase: SearxDatabase = SearxDatabase.getInstance(context)!!

    private val instances = mutableMapOf<String, String>()

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
