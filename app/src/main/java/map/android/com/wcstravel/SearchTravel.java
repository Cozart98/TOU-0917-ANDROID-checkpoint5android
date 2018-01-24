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


	private EditText etGO, etBack, etDateGo, etDateBack;
	TextView mAirline, mDepartureDate,mPrice, mRetunDate, mTravel;
	Button search;
	RecyclerView mRecycler;
	String compare;
	String etTravel;
	private TravelAdapter mAdapter = null;
	private List<TravelModel> mTravelList = new ArrayList<>();
	FirebaseDatabase database = FirebaseDatabase.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_travel);

		etGO = (EditText) findViewById(R.id.et_go) ;
		etBack = (EditText) findViewById(R.id.et_return);
		etDateGo = (EditText) findViewById(R.id.et_date_go);
		etDateBack = (EditText) findViewById(R.id.et_date_back);
		mAirline = (TextView) findViewById(R.id.travel_airline_item);
		mDepartureDate = (TextView) findViewById(R.id.travel_date_go_item);
		mPrice = (TextView) findViewById(R.id.travel_price_item);
		mRetunDate = (TextView) findViewById(R.id.travel_date_return_item);
		mTravel = (TextView) findViewById(R.id.travel_item);
		search = (Button) findViewById(R.id.search);
		etDateGo.setFocusable(false);
		etDateGo.setFocusable(false);



		final Calendar myCalendarGo = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener datePickerListenerGo =
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						myCalendarGo.set(Calendar.YEAR, year);
						myCalendarGo.set(Calendar.MONTH, monthOfYear);
						myCalendarGo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						etDateGo.setText(sdf.format(myCalendarGo.getTime()));
					}
				};
		etDateGo.setOnClickListener(new View.OnClickListener() {
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
						etDateBack.setText(sdf.format(myCalendarBack.getTime()));
					}
				};
		etDateBack.setOnClickListener(new View.OnClickListener() {
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

		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				compare = etGO.getText().toString() + etBack.getText().toString();
				etTravel = etGO.getText().toString() + "_" + etBack.getText().toString();
				if (etGO.getText().toString().isEmpty()
						||etBack.getText().toString().isEmpty()
						||etDateGo.getText().toString().isEmpty()
						||etDateBack.getText().toString().isEmpty()){
					Toast.makeText(SearchTravel.this, "T'es une merde", Toast.LENGTH_SHORT).show();
				}else {
					mTravelList.clear();
					final DatabaseReference refTravels = database.getReference("checkpoint5/travels");
					refTravels.orderByChild("travel").equalTo(etTravel).addValueEventListener(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot dataSnapshot) {

									for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
										TravelModel travelModel =
												usersSnapshot.getValue(TravelModel.class);
										String departureDate = travelModel.getDeparture_date();
										String departureReturn = travelModel.getReturn_date();
											if (departureDate.equals(etDateGo.getText().toString())
													&& departureReturn.equals(etDateBack.getText().toString())){
												Toast.makeText(SearchTravel.this, "YEAH", Toast.LENGTH_SHORT).show();
												mTravelList.add(travelModel);
												mAdapter.notifyDataSetChanged();
											}else{

											}
									}
								}
								@Override
								public void onCancelled(DatabaseError databaseError) {
									Toast.makeText(SearchTravel.this, "RIEN VU FDP", Toast.LENGTH_SHORT).show();
								}
							});
					if (compare.equals("NYCLAX")
							|| compare.equals("NYCBOS")
							|| compare.equals("NYCMIA")) {
						Toast.makeText(SearchTravel.this, compare, Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(SearchTravel.this, "Espece de merde", Toast.LENGTH_SHORT).show();
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
		DatabaseReference refTravels = database.getReference("checkpoint5/travels");
		refTravels.child("checkpoint5/travels").orderByChild("travel").equalTo(etTravel)
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