package sg.edu.rp.c302.c302l12displayincident;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IncidentAdapter extends ArrayAdapter<Incident> {

    Context context;
    TextView type;
    TextView message;
    ArrayList<Incident> incidents;

    public IncidentAdapter(Context context, int resource, ArrayList<Incident> objects) {
        super(context, resource,objects);
        this.context = context;
        incidents = objects;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.incident_row,parent,false);

        type = view.findViewById(R.id.textViewType);
        message = view.findViewById(R.id.textViewMessage);

        Incident currIncident = incidents.get(position);
        type.setText(currIncident.getType());
        message.setText(currIncident.getMessage());

        return view;


    }
}
