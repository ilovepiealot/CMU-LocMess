package grupo19.locmess19.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractMap.SimpleEntry;


import java.util.List;

import grupo19.locmess19.Communications.ServerCommunication;
import grupo19.locmess19.R;

public class KeyValueListAdapter extends ArrayAdapter<SimpleEntry<String,String>> {

    private Context context;
    private List<SimpleEntry<String,String>> items;
    private ServerCommunication server;
    private String username;

    public KeyValueListAdapter(Context context, int resourceId, List<SimpleEntry<String,String>> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
        server = new ServerCommunication("10.0.2.2", 11113);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString("loggedUser", "");

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtKey;
        TextView txtValue;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final SimpleEntry<String,String> kv = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_key, parent, false);
            holder = new ViewHolder();
            holder.txtKey = (TextView) convertView.findViewById(R.id.keyText);
            holder.txtValue = (TextView) convertView.findViewById(R.id.valueText);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtKey.setText(kv.getKey());
        holder.txtValue.setText(kv.getValue());

        Button deleteKeyBtn = (Button) convertView.findViewById(R.id.delete_key_btn);
        deleteKeyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you really want to delete key \"" + kv.getKey() + "\" with value \"" + kv.getValue() + "\"?");
                alertbox.setTitle("Delete Key");
                alertbox.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                new DeleteKeyOperation().execute(username, items.get(position).getKey(), items.get(position).getValue());
                                items.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                alertbox.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                // do nothing
                            }
                        });
                alertbox.show();
                //ask for confirmation and remove from keysFile!!

            }
        });

        return convertView;
    }

    private class DeleteKeyOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (server.deleteKey(params[0],params[1], params[2])) {
                return "Success";
            } else {
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success")){
               Toast.makeText(context, "Key deleted with success!", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Failed")){
               Toast.makeText(context, "Key failed to delete!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}