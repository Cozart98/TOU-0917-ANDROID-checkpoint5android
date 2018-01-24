package map.android.com.wcstravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

	private String TAG = "Value";
	DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ref.child("checkpoint5/students/cozart98").child("hasContent").addValueEventListener
				(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Log.d(TAG, "onDataChange: " + dataSnapshot.getValue(Boolean.class));
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}
}
