package com.example.sih.ui.PROFILE.Agri_Profile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.sih.R;

import java.util.ArrayList;
import java.util.List;

public class AgriProfileViewModel extends ViewModel {


    public static class Item {
        boolean checked;
        String ItemString;

        Item(String t, boolean b) {
            ItemString = t;
            checked = b;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    public static class ViewHolder {
        static CheckBox checkBox;
        public static ArrayList<String> krzcrops = new ArrayList<>();
    }

    public static class ItemsListAdapter extends BaseAdapter {

        private Context context;
        public static List<Item> list;

        ItemsListAdapter(Context c, List<Item> l) {
            this.context = c;
            this.list = l;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            // reuse views
            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(R.layout.checkbox_listview, null);

                ViewHolder.checkBox = convertView.findViewById(R.id.cb);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ViewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            ViewHolder.checkBox.setText(itemStr);

            ViewHolder.checkBox.setTag(position);


            ViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                    if (ViewHolder.checkBox.isChecked()) {
                        ViewHolder.krzcrops.add(ViewHolder.checkBox.getText().toString());
                    }
                    Toast.makeText(context, itemStr + " setOnClickListener\nchecked: " + newState,
                            Toast.LENGTH_LONG).show();

                }
            });

            ViewHolder.checkBox.setChecked(isChecked(position));

            return convertView;
        }
    }

}