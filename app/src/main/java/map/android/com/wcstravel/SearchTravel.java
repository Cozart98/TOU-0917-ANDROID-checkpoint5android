package map.android.com.wcstravel;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchTravel extends AppCompatActivity {


	private EditText mEtGO, mEtBack, mEtDateGo, mEtDateBack;
	TextView mAirline, mDepartureDate,mPrice, mRetunDate, mTravel;
	Button mSearch;
	RecyclerView mRecycler;
	String mCompare;
	String mEtTravel;
	private TravelAdapter mAdapter = null;
	private List<TravelModel> mTravelList = new ArrayList<>();
	FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_travel);

		mEtGO = (EditText) findViewById(R.id.et_go) ;
		mEtBack = (EditText) findViewById(R.id.et_return);
		mEtDateGo = (EditText) findViewById(R.id.et_date_go);
		mEtDateBack = (EditText) findViewById(R.id.et_date_back);
		mAirline = (TextView) findViewById(R.id.travel_airline_item);
		mDepartureDate = (TextView) findViewById(R.id.travel_date_go_item);
		mPrice = (TextView) findViewById(R.id.travel_price_item);
		mRetunDate = (TextView) findViewById(R.id.travel_date_return_item);
		mTravel = (TextView) findViewById(R.id.travel_item);
		mSearch = (Button) findViewById(R.id.search);
		mEtDateGo.setFocusable(false);
		mEtDateGo.setFocusable(false);



		final Calendar myCalendarGo = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener datePickerListenerGo =
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						myCalendarGo.set(Calendar.YEAR, year);
						myCalendarGo.set(Calendar.MONTH, monthOfYear);
						myCalendarGo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						mEtDateGo.setText(sdf.format(myCalendarGo.getTime()));
					}
				};
		mEtDateGo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog date =
						new DatePickerDialog(SearchTravel.this, datePickerListenerGo,
								myCalendarGo.get(Calendar.YEAR),
								myCalendarGo.get(Calendar.MONTH),
								myCalendarGo.get(Calendar.DAY_OF_MONTH));
				myCalendarGo.add(Calendar.DAY_OF_MONTH,7);
				long afterMonth = myCalendarGo.getTimeInMillis();
				date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
				date.getDatePicker().setMaxDate(afterMonth);
				date.show();
			}
		});

		final Calendar myCalendarBack = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener datePickerListenerBack =
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						myCalendarBack.set(Calendar.YEAR, year);
						myCalendarBack.set(Calendar.MONTH, monthOfYear);
						myCalendarBack.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						mEtDateBack.setText(sdf.format(myCalendarBack.getTime()));
					}
				};
		mEtDateBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				DatePickerDialog date =
						new DatePickerDialog(SearchTravel.this, datePickerListenerBack,
								myCalendarBack.get(Calendar.YEAR),
								myCalendarBack.get(Calendar.MONTH),
								myCalendarBack.get(Calendar.DAY_OF_MONTH));
				myCalendarBack.add(Calendar.DAY_OF_MONTH,7);
				long afterMonth = myCalendarBack.getTimeInMillis();
				date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
				date.getDatePicker().setMaxDate(afterMonth);
				date.show();
			}
		});

	mSearch.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			mCompare = mEtGO.getText().toString() + mEtBack.getText().toString();
			mEtTravel = mEtGO.getText().toString() + "_" + mEtBack.getText().toString();
			if (mEtGO.getText().toString().isEmpty()
					|| mEtBack.getText().toString().isEmpty()
					|| mEtDateGo.getText().toString().isEmpty()
					|| mEtDateBack.getText().toString().isEmpty()){
				Toast.makeText(SearchTravel.this, R.string.champ_vide,
						Toast.LENGTH_SHORT).show();
			}else {
				mTravelList.clear();
				final DatabaseReference refTravels = mDatabase.getReference("checkpoint5/travels");
				refTravels.orderByChild("travel").equalTo(mEtTravel).addValueEventListener
						(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {

								for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
									TravelModel travelModel =
											usersSnapshot.getValue(TravelModel.class);
									String departureDate = travelModel.getDeparture_date();
									String departureReturn = travelModel.getReturn_date();
										if (departureDate.equals(mEtDateGo.getText().toString())
												&& departureReturn.equals(mEtDateBack.getText()
												.toString())){
											mTravelList.add(travelModel);
											mAdapter.notifyDataSetChanged();
										}else{

										}
								}
							}
							@Override
							public void onCancelled(DatabaseError databaseError) {
							}
						});
				if (mCompare.equals("NYCLAX")
						|| mCompare.equals("NYCBOS")
						|| mCompare.equals("NYCMIA")) {
					Toast.makeText(SearchTravel.this, mCompare, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(SearchTravel.this, R.string.aucun_resultat,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	});

		mAdapter = new TravelAdapter(mTravelList);
		mRecycler = findViewById(R.id.travel_list);
		mRecycler.setHasFixedSize(true);
		mRecycler.setLayoutManager(new LinearLayoutManager(this));
		addRdvFirebaseListener();
	}

	private void addRdvFirebaseListener() {
		DatabaseReference refTravels = mDatabase.getReference("checkpoint5/travels");
		refTravels.child("checkpoint5/travels").orderByChild("travel").equalTo(mEtTravel)
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if (mTravelList.size() > 0){
							mTravelList.clear();
						}

						for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
							TravelModel rdvModel = postSnapshot.getValue(TravelModel.class);
							mTravelList.add(rdvModel);
						}
						mRecycler.setAdapter(mAdapter);
						mRecycler.setVisibility(View.VISIBLE);
					}
					@Override
					public void onCancelled(DatabaseError databaseError) {}
				});
	}
}