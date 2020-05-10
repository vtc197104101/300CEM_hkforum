package com.example.a300cem_hkforum;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyRecordAdapter extends RecyclerView.Adapter<MyRecordAdapter.ViewHolder>{
    private List<Contact> listData;
    private Context context;

    public MyRecordAdapter(List<Contact> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact ld= listData.get(position);
        long date = Long.parseLong(ld.getDate());
        String sid = Integer.toString(ld.getId());
        long id = Long.parseLong(sid);
        holder.user.setText(ld.getUser());
        holder.title.setText(ld.getTitle());
        holder.time.setText(getDate(date));
        holder.id.setText(getDate(id));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id, user, title, time;
        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView)itemView.findViewById(R.id.recordDate);
            user = (TextView)itemView.findViewById(R.id.recordUser);
            time =(TextView)itemView.findViewById(R.id.recordTime);
            title = (TextView)itemView.findViewById(R.id.recordTitle);
        }
    }

    private String getDate(long time) {
        String sDate;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        Long tsLong = System.currentTimeMillis();
        String today = DateFormat.format("dd-MM-yyyy", tsLong).toString();
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        if (date.equals(today)){
            sDate = context.getString(R.string.today) + DateFormat.format("HH:mm", cal).toString();
        } else {
            sDate =  DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
        }
        return sDate;
    }
}
