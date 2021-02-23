package mock.com.craftedbeer.beer;

import java.util.List;

import mock.com.craftedbeer.BasePresenter;
import mock.com.craftedbeer.BaseView;
import mock.com.craftedbeer.data.model.Beer;

// Contract between Presenter and View
public class BeerContract {
    public interface View extends BaseView {

        //Display and hide Progress bar
        void setProgressBar(boolean state);

        //set the beer data to recyclerview
        void loadBeers(List<Beer> list);

        //show error while loading
        void showError(String errorMessage);


        void currentFilter(BeerFilter beerFilter);

        void sortOrder(BeerSortOrder beerSortOrder);

        void searchBeerByName(String name);

    }

    public interface Presenter extends BasePresenter<View> {

        //load beer by making call to server
        void loadBeers();

        //get selected filter
        BeerFilter getFilter();

        //set selected filter
        void setFilter(BeerFilter beerFilter);

        //get current sort order
        BeerSortOrder getSortedOrder();

        //set current sort order
        void setSortedOrder(BeerSortOrder order);

        //set entered search query
        void setSearchString(String searchString);

        //get search query
        String getSearchString();
    }
}
