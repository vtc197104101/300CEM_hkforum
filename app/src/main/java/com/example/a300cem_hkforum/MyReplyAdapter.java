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

public class MyReplyAdapter extends RecyclerView.Adapter<MyReplyAdapter.ViewHolder>{
    private List<Reply> listData;
    private Context context;

    public MyReplyAdapter(List<Reply> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_reply,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reply ld=listData.get(position);
        long date = Long.parseLong(ld.getReplyTimestamp());
        holder.user.setText(ld.getReplyUser());
        holder.content.setText(ld.getReplyContent());
        holder.time.setText(getDate(date));
        holder.id.setText("#"+ld.getReplyID());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView id, user, content, time;
        public ViewHolder(View itemView) {
            super(itemView);
            id=(TextView)itemView.findViewById(R.id.reply_ID);
            user=(TextView)itemView.findViewById(R.id.reply_Username);
           content=(TextView)itemView.findViewById(R.id.reply_Content);
           time = (TextView)itemView.findViewById(R.id.reply_Time);
        }
    }
    private String getDate(long time) {
        String replyDate;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        Long tsLong = System.currentTimeMillis();
        String today = DateFormat.format("dd-MM-yyyy", tsLong).toString();
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        if (date.equals(today)){
            replyDate = context.getString(R.string.today) + DateFormat.format(" HH:mm", cal).toString();
        } else {
            replyDate =  DateFormat.format("dd-MM-yyyy HH:mm", cal).toString();
        }
        return replyDate;
    }
}
