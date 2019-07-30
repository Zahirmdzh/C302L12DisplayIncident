package sg.edu.rp.c302.c302l12displayincident;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    private FirebaseFirestore db;
    ArrayList<Incident> incidents;
    ArrayAdapter aaIncident;
    CollectionReference colRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv = findViewById(R.id.lv);
        incidents = new ArrayList<>();
        aaIncident = new IncidentAdapter(MainActivity.this,R.layout.incident_row,incidents);
        lv.setAdapter(aaIncident);

        db = FirebaseFirestore.getInstance();
        colRef = db.collection("incidents");

        String url = "http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.addHeader("AccountKey","cYsiznKuReChgmNVjkun9Q==");
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    Log.i("JSON Results", response.toString());
                    JSONArray jsonArray = response.getJSONArray("value");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String type = obj.getString("Type");
                        double latitude = obj.getDouble("Latitude");
                        double longitude = obj.getDouble("Longitude");
                        String message = obj.getString("Message");
                        Date date = Calendar.getInstance().getTime();


                        Incident incident = new Incident(type, latitude, longitude, message);
                        incident.setDate(date);
                        incidents.add(incident);
                    }
                    aaIncident.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Incident incidentSelected = incidents.get(position);

                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("incident",incidentSelected);
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.map) {
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
        }

        if(id == R.id.upload) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String url = "http://datamall2.mytransport.sg/ltaodataservice/TrafficIncidents";
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    client.addHeader("AccountKey","cYsiznKuReChgmNVjkun9Q==");
                    client.get(url, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            try {
                                Log.i("JSON Results", response.toString());
                                JSONArray jsonArray = response.getJSONArray("value");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String type = obj.getString("Type");
                                    double latitude = obj.getDouble("Latitude");
                                    double longitude = obj.getDouble("Longitude");
                                    String message = obj.getString("Message");
                                    Date date = Calendar.getInstance().getTime();

                                    Incident incident = new Incident(type, latitude, longitude, message);
                                    incident.setDate(date);
                                    incidents.add(incident);

                                    colRef.add(incident);
                                }
                                aaIncident.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        /*            final CollectionReference colRef = db.collection("students");
                    colRef
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w("LISTENER", "Listen failed.", e);
                                        return;
                                    }
                                    // Read from Snapshot and add into ArrayAdapter for ListView
                                    incidents = new ArrayList<>();

                                  Date d = Calendar.getInstance().getTime();
                                    for (QueryDocumentSnapshot doc : value) {
                                        if (doc.get("name") != null) {
                                            Incident incident = doc.toObject(Incident.class);//use POJO
                                            incident.setDate(d);//set the docment id as the student id
                                            incidents.add(incident);
                                        }
                                    }
                                    aaIncident = new IncidentAdapter(MainActivity.this,R.layout.incident_row,incidents);
                                    lv.setAdapter(aaIncident);


                                    Log.d("ARRAYLIST", "Current cites in CA: " + incidents);
                                }
                            });
*/

                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog myDialog = builder.create();

            myDialog.setMessage("Proceed to upload to Firestore?");
            myDialog.setTitle("Upload to Firestore");
            myDialog.setIcon(R.drawable.ic_cloud_queue_black_24dp);

            myDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }


}
