package edu.example.ssf.mma.data;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.model.Lap;

public class SessionListAdapter extends BaseAdapter {


    static class ViewHolder {
        TextView name;
    }

    private ArrayList<String> data;

    private final LayoutInflater mLayoutInflater;

    public SessionListAdapter(Context context, ArrayList<String> data) {
        this.data = data;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.sessionrow_layout, null);

            holder = new ViewHolder();
            holder.name = view.findViewById(R.id.sessionrow_name);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(data.get(i));



        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}
