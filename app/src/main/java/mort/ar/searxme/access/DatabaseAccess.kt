package mort.ar.searxme.access

import android.content.Context
import io.reactivex.Observable
import mort.ar.searxme.model.SearxInstance

class DatabaseAccess {

    private val mContext: Context
    private val mDatabase: SearxDatabase
    private val mSearxInstanceAccess: SearxInstanceDao

    constructor(context: Context) {
        mContext = context
        mDatabase = SearxDatabase.getInstance(context)!!
        mSearxInstanceAccess = mDatabase.searxInstanceDao()
    }

    fun getAllSearxInstances(): Observable<List<SearxInstance>> {
        return mSearxInstanceAccess.getAll()
    }

    fun getSearxInstance(name: String): Observable<SearxInstance> {
        return mSearxInstanceAccess.getSearxInstance(name)
    }

    fun getSearxInstances(names: List<String>): Observable<List<SearxInstance>> {
        return mSearxInstanceAccess.getSearxInstances(names)
    }
}