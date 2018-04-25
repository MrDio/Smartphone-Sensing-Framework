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

public class LapListAdapter extends BaseAdapter {


    static class ViewHolder {
        TextView number;
        TextView roundTime;
        ImageView performanceIndicator;
    }

    private ArrayList<Lap> data;

    private final LayoutInflater mLayoutInflater;

    public LapListAdapter(Context context, ArrayList<Lap> data) {
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
            view = mLayoutInflater.inflate(R.layout.laprow_layout, null);

            holder = new ViewHolder();
            holder.number = view.findViewById(R.id.laprow_number);
            holder.roundTime = view.findViewById(R.id.laprow_roundTime);
            holder.performanceIndicator = view.findViewById(R.id.laprow_pi);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.number.setText(String.valueOf(data.get(i).getNumberAsString()));
        holder.roundTime.setText(String.valueOf(data.get(i).getRoundTimeAsString()));
        boolean valid = data.get(i).isValid();
        boolean fastest = data.get(i).isFastestLap();

        if (valid) {
            if (fastest) {
                holder.performanceIndicator.setImageResource(R.mipmap.lightgreen);
            } else {
                holder.performanceIndicator.setImageResource(R.mipmap.lightyellow);
            }

        } else {
            holder.performanceIndicator.setImageResource(R.mipmap.lightred);
        }


        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public void refreshData(ArrayList<Lap> laps) {
        this.data.clear();
        this.data.addAll(laps);
        notifyDataSetChanged();
    }
}
