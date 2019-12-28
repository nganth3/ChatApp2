package com.example.chatapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DataCallAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<DataCall> DataCallList;

    public DataCallAdapter(Context context, int layout, List<DataCall> dataCallList) {
        this.context = context;
        this.layout = layout;
        DataCallList = dataCallList;
    }

    @Override
    public int getCount() {
        return DataCallList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtID,txtSoDienThoai,txtNoidung;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
           viewHolder = new ViewHolder();
           LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = inflater.inflate(layout,null);
           viewHolder.txtID = convertView.findViewById(R.id.textView_ID);
           viewHolder.txtNoidung = convertView.findViewById(R.id.textView_Noidung);
           viewHolder.txtSoDienThoai = convertView.findViewById(R.id.textView_Sodienthoai);
           convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataCall dataCall = DataCallList.get(position);
        viewHolder.txtSoDienThoai.setText(dataCall.getSodienthoai());
        viewHolder.txtID.setText("ID: " + dataCall.getId()+" ___  ");
        viewHolder.txtNoidung.setText(dataCall.getNoidung());

      return convertView;
    }
}
