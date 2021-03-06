package br.uvv.carona.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.StudentRide;

public class RideMembersRecyclerAdapter extends RecyclerView.Adapter {
    List<StudentRide> mMembers;

    public RideMembersRecyclerAdapter(List<StudentRide> members){
        this.mMembers = members;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, null);
        UserMemberViewHolder holder = new UserMemberViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserMemberViewHolder userHolder = (UserMemberViewHolder)holder;
        StudentRide student = this.mMembers.get(position);
        if(!TextUtils.isEmpty(student.student.photo)){
            userHolder.userPhoto.setImageURI(Uri.parse(student.student.photo));
        }
        userHolder.userName.setText(student.student.name);
    }

    @Override
    public int getItemCount() {
        return this.mMembers.size();
    }

    public void replaceContent(List<StudentRide> studentRides){
        this.mMembers.clear();
        this.mMembers.addAll(studentRides);
        this.notifyDataSetChanged();
    }

    public class UserMemberViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView userPhoto;
        public TextView userName;


        public UserMemberViewHolder(View itemView) {
            super(itemView);

            this.userPhoto = (SimpleDraweeView)itemView.findViewById(R.id.user_photo);
            this.userName = (TextView)itemView.findViewById(R.id.user_name);
        }
    }
}
