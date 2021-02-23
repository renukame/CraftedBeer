package mock.com.craftedbeer;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import mock.com.craftedbeer.di.DaggerAppComponent;

public class CraftedBeerApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

}
