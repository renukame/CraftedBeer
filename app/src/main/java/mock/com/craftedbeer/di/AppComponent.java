package mock.com.craftedbeer.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import mock.com.craftedbeer.CraftedBeerApplication;
import mock.com.craftedbeer.data.api.CraftedBeerClient;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBuilderModule.class, CraftedBeerClient.class})
public interface AppComponent extends AndroidInjector<CraftedBeerApplication> {
    @Override
    void inject(CraftedBeerApplication instance);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
