package grupo19.locmess19;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by super on 06/04/2017.
 */

public class Locations extends Fragment {

    public static Locations newInstance() {
        Locations fragment = new Locations();
        return fragment;
    }

    public Locations() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
        return rootView;
    }

}
