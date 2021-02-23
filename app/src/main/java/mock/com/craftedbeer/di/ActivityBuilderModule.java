package mock.com.craftedbeer.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mock.com.craftedbeer.beer.BeerActivity;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = BeerModule.class)
    abstract BeerActivity beerActivity();

}
