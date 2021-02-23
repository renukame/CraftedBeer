package mock.com.craftedbeer.beer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mock.com.craftedbeer.R;
import mock.com.craftedbeer.data.model.Beer;

//Display beer list data into RecyclerView
public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {

    private List<Beer> mBeerList;

    public BeerAdapter(List<Beer> list) {
        setBeerList(list);
    }

    public void replaceData(List<Beer> list) {
        setBeerList(list);
        notifyDataSetChanged();
    }

    public void setBeerList(List<Beer> list) {
        this.mBeerList = list;
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_beer, parent, false);
        return new BeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder holder, int position) {
        holder.mBeerName.setText(mBeerList.get(position).getName());
        holder.mBeerFilter.setText(mBeerList.get(position).getStyle());
        String alcoholContent = "Alcohol Content - ";
        if(mBeerList.get(position).getAbv().isEmpty()){
            alcoholContent = alcoholContent.concat("0.00");
        }else{
            alcoholContent = alcoholContent.concat(mBeerList.get(position).getAbv());
        }
        holder.mAlcoholContent.setText(alcoholContent);
    }

    @Override
    public int getItemCount() {
        return mBeerList.size();
    }

    public static class BeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        TextView mBeerName;
        @BindView(R.id.filter)
        TextView mBeerFilter;
        @BindView(R.id.alcohol_content)
        TextView mAlcoholContent;


        public BeerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
