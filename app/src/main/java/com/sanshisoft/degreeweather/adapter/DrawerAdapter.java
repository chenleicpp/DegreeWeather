package com.sanshisoft.degreeweather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.model.Category;

import java.util.List;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class DrawerAdapter extends BaseAdapter{

    private Context mContext;
    private List<Category> mList;
    private int curPos;

    public DrawerAdapter(Context ctx,List<Category> list){
        this.mContext = ctx;
        this.mList = list;
    }

    public void setPos(int pos){
        curPos = pos;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.drawer_list_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        textView.setText(((Category) getItem(position)).getName());
        if (position + 1 == curPos){
            textView.setTextColor(App.getContext().getResources().getColorStateList(R.color.primary_color));
        }else{
            textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}
