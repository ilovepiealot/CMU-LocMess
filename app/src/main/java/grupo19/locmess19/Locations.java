package grupo19.locmess19;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by super on 06/04/2017.
 */

public class Locations extends Fragment {

    private View rootView;

    public static Locations newInstance() {
        Locations fragment = new Locations();
        return fragment;
    }

    public Locations() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        Button newLocationBtn = (Button) rootView.findViewById(R.id.newLocationBtn);
        newLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewLocation.class));
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = this.getArguments();

        ArrayList<String> locations = new ArrayList<>();
        locations.add("Arco do Cego");
        locations.add("Instituto Superior Técnico");
        locations.add("Campo Pequeno");

        if (b != null) {
            locations.add(this.getArguments().getString("key"));
            Toast.makeText(getActivity(),"Text Saved!",Toast.LENGTH_LONG).show();
        }


        // Create the adapter to convert the array to views
        CustomLocationAdapter adapter = new CustomLocationAdapter(rootView.getContext(), locations);
        // Attach the adapter to a ListView
        ListView listView1 = (ListView) rootView.findViewById(R.id.locations_list);
        listView1.setAdapter(adapter);

    }

}
