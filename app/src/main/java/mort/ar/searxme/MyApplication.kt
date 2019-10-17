package mort.ar.searxme

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import mort.ar.searxme.di.DaggerAppComponent
import javax.inject.Inject

class MyApplication : Application(), HasAndroidInjector {

  @Inject
  lateinit var activityInjector: DispatchingAndroidInjector<Any>

  override fun onCreate() {
    super.onCreate()

    DaggerAppComponent
      .builder()
      .application(this)
      .build()
      .inject(this)
  }

  override fun androidInjector(): AndroidInjector<Any> = activityInjector

}