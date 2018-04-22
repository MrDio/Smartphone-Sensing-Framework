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
import edu.example.ssf.mma.model.Section;

public class SectionListAdapter extends BaseAdapter {


    static class ViewHolder {
        TextView type;
        TextView optimizationTip;
        ImageView performanceIndicator;
    }
    private ArrayList<Section> data;

    private final LayoutInflater layoutInflater;

    public SectionListAdapter(Context context, ArrayList<Section> data){
        this.data = data;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if(view == null){
            view = layoutInflater.inflate(R.layout.sectionrow_layout, null);

            holder = new ViewHolder();
            holder.type = view.findViewById(R.id.sectionrow_type);
            holder.optimizationTip = view.findViewById(R.id.sectionrow_hint);
            holder.performanceIndicator = view.findViewById(R.id.sectionrow_pi);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.type.setText(String.valueOf(data.get(i).getType()));
        holder.optimizationTip.setText(data.get(i).getOptimizationTip());
        int pi = data.get(i).getPerformanceIndicator();

        switch(pi){
            case 0:
                holder.performanceIndicator.setImageResource(R.mipmap.lightred);
                break;
            case 1:
                holder.performanceIndicator.setImageResource(R.mipmap.lightyellow);
                break;
            case 2:
                holder.performanceIndicator.setImageResource(R.mipmap.lightgreen);
                break;
        }

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
