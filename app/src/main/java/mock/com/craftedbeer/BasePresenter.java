package mock.com.craftedbeer;

public interface BasePresenter<T> {

    void subscribe();

    void unSubscribe();

    void setView(T view);

    void deleteView();

}
