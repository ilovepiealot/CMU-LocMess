package grupo19.locmess19.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import grupo19.locmess19.Activities.MessagesActivity;
import grupo19.locmess19.R;

public class NewLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlocation);

    }

    public void saveLocation(View v){

        EditText editLocationName = (EditText)findViewById(R.id.location_name);
        String locationName = editLocationName.getText().toString();

        Intent intent = new Intent(getBaseContext(), MessagesActivity.class);
        intent.putExtra("key",locationName);
        startActivity(intent);

    }

}
