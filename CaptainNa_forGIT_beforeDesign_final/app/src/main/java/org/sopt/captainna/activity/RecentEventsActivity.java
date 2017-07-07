package org.sopt.captainna.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sopt.captainna.R;
import org.sopt.captainna.model.RecentEventResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.token;

public class RecentEventsActivity extends AppCompatActivity {

    //리사이클러뷰 설정
    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<RecentEventResult.EVent> recentEvents;

    //네트워킹
    NetworkService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_events);


        //네트워크초기화
        initNetwork();

        //뒤로가기
        goBack();

        //findView
        findView();

        //리사이클러뷰 새팅
        initRecycler();

        //최근행사 리스트 가져오기
        getRecentEventList();
    }

    /****************************************최근행사 리스트 가져오기*****************************/
    private void getRecentEventList() {
        Call<RecentEventResult> request = service.getRecentEventList(token);
        request.enqueue(new Callback<RecentEventResult>() {
            @Override
            public void onResponse(Call<RecentEventResult> call, Response<RecentEventResult> response) {
                if (response.isSuccessful()) {
                    recentEvents = response.body().event;
                    myRecyclerAdapter.setAdapter(recentEvents);
                    myRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RecentEventResult> call, Throwable t) {

            }
        });
    }


    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }



    /****************************************findView**********************************************/
    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvRecentEvents);
    }


    /***************************************리사이클러 설정*****************************************/
    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.rvRecentEvents);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        recentEvents = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter(recentEvents);
        recyclerView.setAdapter(myRecyclerAdapter);
    }





    /***********************************Adapter**********************************/
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<RecentEventResult.EVent> eVents;

        public MyRecyclerAdapter(ArrayList<RecentEventResult.EVent> eVents) {
            this.eVents = eVents;
        }

        public void setAdapter(ArrayList<RecentEventResult.EVent> eVents) {
            this.eVents = eVents;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recent_events, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            RecentEventResult.EVent recentEvent = eVents.get(position);

            //화면 배치
            Glide.with(RecentEventsActivity.this)
                    .load(recentEvent.photo)
                    .into(holder.ivEvent);
            holder.tvEventName.setText(recentEvent.event_title);
            holder.tvGroupName.setText(recentEvent.group_title);
            holder.tvEventPlace.setText(recentEvent.place);
            holder.tvEventDate.setText(recentEvent.start_date);
            holder.tvEventMoney.setText(Integer.toString(recentEvent.amount));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecentEventsActivity.this, EventDetailActivity.class);
                    intent.putExtra("event_id", 1);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recentEvents.size();
        }
    }

    /**********************************ViewHolder********************************/

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEvent;
        TextView tvEventName;
        TextView tvGroupName;
        TextView tvEventPlace;
        TextView tvEventDate;
        TextView tvEventMoney;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvGroupName = (TextView) itemView.findViewById(R.id.tvGroupName);
            tvEventPlace = (TextView) itemView.findViewById(R.id.tvEventPlace);
            tvEventDate = (TextView) itemView.findViewById(R.id.tvEventDate);
            tvEventMoney = (TextView) itemView.findViewById(R.id.tvEventMoney);
        }
    }


    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.cancelRecentEvents);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecentEventsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
