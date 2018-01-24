package map.android.com.wcstravel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apprenti on 24/01/18.
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {
	private List<TravelModel> mTravelList;

	public TravelAdapter( List<TravelModel> list) {
		this.mTravelList = list;
	}

	@Override
	public TravelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_travel_item, parent, false);
		return new TravelViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TravelViewHolder holder, int position) {
		final TravelModel travelModel = mTravelList.get(position);
		double dollar = Double.valueOf(travelModel.getPrice());
		double priceDollardollar = dollar / 1.24;
		DecimalFormat df2 = new DecimalFormat(".##");
		df2.setRoundingMode(RoundingMode.UP);
		df2.format(priceDollardollar);
		holder.airline.setText(travelModel.getAirline());
		holder.travel.setText(travelModel.getTravel());
		holder.price.setText(travelModel.getPrice() + "€" + " " + priceDollardollar + "$");
		holder.dateGo.setText(travelModel.getDeparture_date());
		holder.dateBack.setText(travelModel.getReturn_date());
	}

	@Override
	public int getItemCount() {
		return mTravelList.size();
	}

	void updateAdapter(List<TravelModel> travelList) {
		mTravelList = travelList;
		notifyDataSetChanged();
	}



	class TravelViewHolder extends RecyclerView.ViewHolder {
		TextView travel,price,dateGo,dateBack,airline;

		TravelViewHolder(View view) {
			super(view);
			travel = view.findViewById(R.id.travel_item);
			price = view.findViewById(R.id.travel_price_item);
			dateGo = view.findViewById(R.id.travel_date_go_item);
			dateBack = view.findViewById(R.id.travel_date_return_item);
			airline = view.findViewById(R.id.travel_airline_item);
		}
	}
}
