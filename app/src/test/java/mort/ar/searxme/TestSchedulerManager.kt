package mort.ar.searxme

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class TestSchedulerManager {

  companion object {
    fun throwEverythingOnTrampoline() {
      RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
      RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
      RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }
  }

}