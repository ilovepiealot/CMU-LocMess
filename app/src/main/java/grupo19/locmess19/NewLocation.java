package grupo19.locmess19;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class NewLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlocation);

    }

    public void saveLocation(View v){

        EditText editLocationName = (EditText)findViewById(R.id.location_name);
        String locationName = editLocationName.getText().toString();

        Intent intent = new Intent(getBaseContext(), Messages.class);
        intent.putExtra("key",locationName);
        startActivity(intent);

    }

}
