package mock.com.craftedbeer.beer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.SearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;
import mock.com.craftedbeer.R;

// Display Beer Activity
public class BeerActivity extends DaggerAppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.beer_search)
    SearchView mSearchBeer;

    @Inject
    Lazy<BeerListFragment> mBeerProvider;

    private BeerListFragment mBeerListFragment;

    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer);
        ButterKnife.bind(this);
        mSearchBeer.setOnQueryTextListener(this);
        mBeerListFragment = (BeerListFragment) getSupportFragmentManager().findFragmentById(R.id.beer_container);
        if (mBeerListFragment == null) {
            mBeerListFragment = mBeerProvider.get();
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.beer_container, mBeerListFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mBeerListFragment.searchBeerByName(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
