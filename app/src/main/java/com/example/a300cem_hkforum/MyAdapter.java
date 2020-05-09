package com.example.a300cem_hkforum;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<Post> listData;
    private Context context;

    String username;
    public MyAdapter(List<Post> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.posts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Post ld=listData.get(position);
        long date = Long.parseLong(ld.getTimestamp());
       holder.user.setText(ld.getUser());
        holder.title.setText(ld.getTitle());
        holder.date.setText(getDate(date));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,user,date;
        public ViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            user =(TextView)itemView.findViewById(R.id.username);
            date = (TextView) itemView.findViewById(R.id.date);

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
            replyDate =  context.getString(R.string.today)+ DateFormat.format(" HH:MM", cal).toString();
        } else {
            replyDate =  DateFormat.format("dd-MM-yyyy", cal).toString();
        }
        return replyDate;
    }

}
