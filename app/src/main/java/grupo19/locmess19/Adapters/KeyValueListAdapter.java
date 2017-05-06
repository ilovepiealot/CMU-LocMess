package grupo19.locmess19.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.AbstractMap.SimpleEntry;


import java.util.List;

import grupo19.locmess19.R;

public class KeyValueListAdapter extends ArrayAdapter<SimpleEntry<String,String>> {

    Context context;
    private List<SimpleEntry<String,String>> items;

    public KeyValueListAdapter(Context context, int resourceId, List<SimpleEntry<String,String>> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtKey;
        TextView txtValue;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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
                                // TODO: delete from keysFile!!!
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
}