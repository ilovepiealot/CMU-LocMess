package grupo19.locmess19.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import grupo19.locmess19.DataTypes.KeyValuePair;
import grupo19.locmess19.R;

public class KeyValueListAdapter extends ArrayAdapter<KeyValuePair> {

    Context context;

    public KeyValueListAdapter(Context context, int resourceId, List<KeyValuePair> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtKey;
        TextView txtValue;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        KeyValuePair kv = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_key, null);
            holder = new ViewHolder();
            holder.txtKey = (TextView) convertView.findViewById(R.id.keyText);
            holder.txtValue = (TextView) convertView.findViewById(R.id.valueText);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtKey.setText(kv.getKey());
        holder.txtValue.setText(kv.getValue());

        return convertView;
    }
}