package mock.com.craftedbeer.data.api;

import java.util.List;

import io.reactivex.Observable;
import mock.com.craftedbeer.data.model.Beer;
import retrofit2.http.GET;

// API calls to server
public interface CraftedBeerService {

    @GET("beercraft")
    Observable<List<Beer>> getBeers();

}
