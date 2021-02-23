package mock.com.craftedbeer.di;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mock.com.craftedbeer.beer.BeerContract;
import mock.com.craftedbeer.beer.BeerListFragment;
import mock.com.craftedbeer.beer.BeerPresenter;

@Module
public abstract class BeerModule {
    @ContributesAndroidInjector
     abstract BeerListFragment beerListFragment();

    @Binds
    abstract BeerContract.Presenter beerPresenter(BeerPresenter presenter);
}
