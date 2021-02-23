package mock.com.craftedbeer.beer;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import mock.com.craftedbeer.data.api.CraftedBeerService;
import mock.com.craftedbeer.data.api.ErrorUtils;
import mock.com.craftedbeer.data.model.Beer;

// Business logic for Beer Activity : gets data from api and sends it to UI and also listen to UI Actions
public class BeerPresenter implements BeerContract.Presenter {

    private CompositeDisposable mCompositeDisposable;
    private BeerContract.View mBeerView;
    private BeerFilter mCurrentFilter = BeerFilter.ETC;
    private BeerSortOrder mSortOrder = BeerSortOrder.ASC;
    private List<Beer> mList;
    private String searchString;

    @Inject
    CraftedBeerService mCraftedBeerService;


    @Inject
    public BeerPresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void setView(BeerContract.View view) {
        this.mBeerView = view;
    }

    @Override
    public void subscribe() {
        loadBeers();
    }


    @Override
    public void loadBeers() {
        if (mBeerView != null) {
            mBeerView.setProgressBar(true);
        }
        Disposable disposable = mCraftedBeerService.getBeers().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Beer>>() {
                    @Override
                    public void accept(List<Beer> beers) throws Exception {
                        if (mBeerView != null) {
                            mBeerView.setProgressBar(false);
                            mList = beers;
                            showSortedBeer(getSortedOrder());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mBeerView != null) {
                            mBeerView.setProgressBar(false);
                            mBeerView.showError(ErrorUtils.parseThrowable(throwable));
                        }

                    }
                });
        mCompositeDisposable.add(disposable);

    }


    private void showFilteredBeer(final BeerFilter beerFilter) {
        if (mCraftedBeerService.getBeers() != null) {
            Disposable filterDisposable = (mCraftedBeerService.getBeers())
                    .flatMap(new Function<List<Beer>, Observable<Beer>>() {
                        @Override
                        public Observable<Beer> apply(List<Beer> beers) {
                            return Observable.fromIterable(beers);
                        }
                    })
                    .filter(new Predicate<Beer>() {
                        @Override
                        public boolean test(Beer beer) throws Exception {
                            switch (beerFilter) {
                                case LAGER:
                                    return beer.getStyle().contains("Lager");
                                case ALE:
                                    return beer.getStyle().contains("Ale");
                                case IPA:
                                    return beer.getStyle().contains("IPA");
                                default:
                                    return true;
                            }
                        }
                    }).toList().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Beer>>() {
                        @Override
                        public void accept(List<Beer> list) throws Exception {
                            Log.e("Filtered", list.size() + "");
                            mBeerView.loadBeers(list);
                            mList = list;

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("Filtered", throwable.getMessage() + "");
                            mBeerView.showError(ErrorUtils.parseThrowable(throwable));
                        }
                    });
            mCompositeDisposable.add(filterDisposable);
        }
    }

    @Override
    public BeerFilter getFilter() {
        return mCurrentFilter;
    }

    @Override
    public void setFilter(BeerFilter beerFilter) {
        mCurrentFilter = beerFilter;
        showFilteredBeer(beerFilter);
    }


    private void showSortedBeer(BeerSortOrder order) {
        if (mList != null) {
            switch (order) {
                case ASC:
                    Collections.sort(mList);
                    mBeerView.loadBeers(mList);
                    break;
                case DSC:
                    Collections.sort(mList, Collections.reverseOrder());
                    mBeerView.loadBeers(mList);
                    break;
            }
        }
    }

    @Override
    public BeerSortOrder getSortedOrder() {
        return mSortOrder;
    }

    @Override
    public void setSortedOrder(BeerSortOrder order) {
        mSortOrder = order;
        showSortedBeer(order);
    }


    private void searchBeerByName(final String name) {
        if (mCraftedBeerService.getBeers() != null) {
            Disposable searchDisposable = (mCraftedBeerService.getBeers())
                    .flatMap(new Function<List<Beer>, Observable<Beer>>() {
                        @Override
                        public Observable<Beer> apply(List<Beer> beers) {
                            return Observable.fromIterable(beers);
                        }
                    }).filter(new Predicate<Beer>() {
                        @Override
                        public boolean test(Beer beer) throws Exception {
                            return beer.getName().matches("(?i).*" + name + ".*");
                        }
                    }).toList().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Beer>>() {
                        @Override
                        public void accept(List<Beer> list) throws Exception {
                            Log.e("Filtered", list.size() + "");
                            mBeerView.loadBeers(list);
                            mList = list;

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("Filtered", throwable.getMessage() + "");
                            mBeerView.showError(ErrorUtils.parseThrowable(throwable));
                        }
                    });

            mCompositeDisposable.add(searchDisposable);
        }

    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString = searchString;
        searchBeerByName(searchString);
    }

    @Override
    public String getSearchString() {
        return searchString;
    }


    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }


    @Override
    public void deleteView() {
        mBeerView = null;
    }
}
