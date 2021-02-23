package mock.com.craftedbeer.beer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;
import mock.com.craftedbeer.R;
import mock.com.craftedbeer.data.model.Beer;

// UI for Beer List
public class BeerListFragment extends DaggerFragment implements BeerContract.View {

    private static final String CURRENT_FILTER = "Filter";
    private static final String SORT_ORDER = "Order";
    private static final String SEARCH_STRING = "Search";
    @BindView(R.id.beer_rv)
    RecyclerView mBeerRecyclerView;
    @BindView(R.id.beer_pb)
    ProgressBar mBeerPb;
    @BindView(R.id.beer_fab)
    FloatingActionButton mBeerFab;
    @BindView(R.id.larger)
    Button mLargeBtn;
    @BindView(R.id.ale)
    Button mAleBtn;
    @BindView(R.id.ipa)
    Button mIpaBtn;
    @BindView(R.id.etc)
    Button mEtcBtn;
    @Inject
    BeerPresenter mBeerPresenter;
    private BeerAdapter mBeerAdapter;
    private BeerSortOrder mSortOrder = BeerSortOrder.DSC;
    private BeerFilter filter;
    private BeerSortOrder beerSortOrder;
    private String searchString;

    @Inject
    public BeerListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeerAdapter = new BeerAdapter(new ArrayList<Beer>(0));
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beerlist, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBeerRecyclerView.setLayoutManager(layoutManager);
        mBeerRecyclerView.setAdapter(mBeerAdapter);

        if (savedInstanceState != null) {
            filter =
                    (BeerFilter) savedInstanceState.getSerializable(CURRENT_FILTER);
            beerSortOrder = (BeerSortOrder) savedInstanceState.getSerializable(SORT_ORDER);
            searchString = savedInstanceState.getString(SEARCH_STRING);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBeerPresenter.setView(this);
        mBeerPresenter.subscribe();
        if (filter != null) {
            currentFilter(filter);
        }
        if (beerSortOrder != null) {
            sortOrder(beerSortOrder);
        }
        if (searchString != null) {
            searchBeerByName(searchString);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTER, mBeerPresenter.getFilter());
        outState.putSerializable(SORT_ORDER, mBeerPresenter.getSortedOrder());
        outState.putString(SEARCH_STRING, mBeerPresenter.getSearchString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setProgressBar(boolean state) {
        if (state) {
            mBeerPb.setVisibility(View.VISIBLE);
        } else {
            mBeerPb.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadBeers(List<Beer> list) {
        mBeerAdapter.replaceData(list);
    }

    @Override
    public void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.error));
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @OnClick({R.id.ale, R.id.larger, R.id.ipa, R.id.etc})
    public void onFilterClicked(View view) {
        int id = view.getId();
        BeerFilter beerFilter = BeerFilter.ETC;
        switch (id) {
            case R.id.ale:
                beerFilter = BeerFilter.ALE;
                break;
            case R.id.larger:
                beerFilter = BeerFilter.LAGER;
                break;
            case R.id.ipa:
                beerFilter = BeerFilter.IPA;
                break;
            case R.id.etc:
                beerFilter = BeerFilter.ETC;
                break;

        }
        currentFilter(beerFilter);
    }

    @Override
    public void currentFilter(BeerFilter beerFilter) {
        setButtonColor(beerFilter);
        mBeerPresenter.setFilter(beerFilter);
    }

    private void setButtonColor(BeerFilter beerFilter) {
        switch (beerFilter) {
            case LAGER:
                mLargeBtn.setBackground(getActivity().getDrawable(R.color.colorYellow));
                mAleBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mIpaBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mEtcBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                break;
            case ALE:
                mAleBtn.setBackground(getActivity().getDrawable(R.color.colorYellow));
                mLargeBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mIpaBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mEtcBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                break;
            case IPA:
                mIpaBtn.setBackground(getActivity().getDrawable(R.color.colorYellow));
                mLargeBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mAleBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mEtcBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                break;
            case ETC:
                mEtcBtn.setBackground(getActivity().getDrawable(R.color.colorYellow));
                mLargeBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mAleBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                mIpaBtn.setBackground(getActivity().getDrawable(R.drawable.filter));
                break;
        }
    }

    @OnClick(R.id.beer_fab)
    public void sortList() {
        sortOrder(mSortOrder);
    }

    @Override
    public void sortOrder(BeerSortOrder beerSortOrder) {
        switch (beerSortOrder) {
            case ASC:
                mBeerFab.setImageResource(R.drawable.asc);
                mBeerPresenter.setSortedOrder(beerSortOrder);
                mSortOrder = BeerSortOrder.DSC;
                break;
            case DSC:
                mBeerFab.setImageResource(R.drawable.dsc);
                mBeerPresenter.setSortedOrder(beerSortOrder);
                mSortOrder = BeerSortOrder.ASC;
                break;
        }

    }

    @Override
    public void searchBeerByName(String name) {
        mBeerPresenter.setSearchString(name);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBeerPresenter.deleteView();
        mBeerPresenter.unSubscribe();
    }
}
