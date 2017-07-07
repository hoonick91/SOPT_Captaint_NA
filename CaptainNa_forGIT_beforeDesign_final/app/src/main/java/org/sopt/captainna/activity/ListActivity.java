package org.sopt.captainna.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import org.sopt.captainna.model.MyGroupEventResult;
import org.sopt.captainna.model.MyGroup_List;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;
import static org.sopt.captainna.activity.CommonVariable.token;

public class ListActivity extends AppCompatActivity {


    //네트워킹
    NetworkService service;
    private int group_id;

    private ImageView ivTopGroup;
    private TextView tvGroupName;
    private TextView tvGroupIntro;
    private TextView tvJoinNumber;
    private MyGroup_List myGroupList;
    private TextView chairman_name;

    //리사이클러뷰 설정
    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<MyGroupEventResult.EVent> eventLists;

    private  LinearLayout llchangeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //네트워크초기화
        initNetwork();

        //그룹아이디 받기
        getGroupID();

        //뒤로가기
        goBack();

        //findView
        findView();

        //모임수정 리스너 설정
        changeGroupInfo();

        //리사이클러뷰 새팅
        initRecycler();

        //행사등록하기로 이동
        registerEvent();



        //내모임, 이벤트 정보 가져오기
        getMyGroupEventInfo();


    }
    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /************************************네트워킹*****************************************************/
    private void getMyGroupEventInfo() {
        Call<MyGroupEventResult> request = service.getMyGroupEvent(token, group_id);
        request.enqueue(new Callback<MyGroupEventResult>() {
            @Override
            public void onResponse(Call<MyGroupEventResult> call, Response<MyGroupEventResult> response) {
                if (response.isSuccessful()) {

                    //상단부분
                    Glide.with(ListActivity.this)
                            .load(response.body().group.get(0).photo)
                            .into(ivTopGroup);
                    tvGroupName.setText(response.body().group.get(0).title);
                    tvGroupIntro.setText(response.body().group.get(0).text);
                    chairman_name.setText(response.body().group.get(0).chairman_name);
                    tvJoinNumber.setText(Integer.toString(response.body().group.get(0).member_count));

                    if(response.body().group.get(0).is_chairman==1){//내가 모임장인 경우
                        llchangeButton.setVisibility(View.VISIBLE);
                    }
                    else{
                        llchangeButton.setVisibility(View.INVISIBLE);
                    }


                    //하단 부분
                    eventLists = response.body().events;
                    myRecyclerAdapter.setAdapter(eventLists);
                    myRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MyGroupEventResult> call, Throwable t) {

            }
        });
    }

    /**********************************그룹 아이디 받기*******************************************/
    private void getGroupID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("group_id");

    }

    /******************************************모임 수정하기****************************************/
    private void changeGroupInfo() {
        llchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, ChangeGroupActivity.class);
                startActivity(intent);
            }
        });
    }
    /**********************************행사 등록으로 이동******************************************/
    private void registerEvent() {
        LinearLayout llregisterButton = (LinearLayout) findViewById(R.id.llregisterButton);
        llregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListActivity.this ,MakeEventActivity.class);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
            }
        });
    }

    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /***************************************리사이클러 설정*****************************************/
    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.rvEvents);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //어뎁터 생성, 리사이클러뷰에 붙임
        eventLists = new ArrayList<>();
        myRecyclerAdapter = new MyRecyclerAdapter(eventLists);
        recyclerView.setAdapter(myRecyclerAdapter);
    }


    /****************************************findView**********************************************/
    private void findView() {
        //상단 부분
        ivTopGroup = (ImageView) findViewById(R.id.ivTopGroup);
        tvGroupName = (TextView) findViewById(R.id.tvGroupName);
        tvGroupIntro = (TextView) findViewById(R.id.tvGroupIntro);
        tvJoinNumber = (TextView) findViewById(R.id.tvJoinNumber);
        chairman_name = (TextView)findViewById(R.id.chairman_name);
        llchangeButton = (LinearLayout) findViewById(R.id.llchangeButton);
    }


    /***********************************Adapter**********************************/
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<MyGroupEventResult.EVent> eventLists;

        public MyRecyclerAdapter(ArrayList<MyGroupEventResult.EVent> eventLists) {
            this.eventLists = eventLists;
        }

        public void setAdapter(ArrayList<MyGroupEventResult.EVent> eventLists) {
            this.eventLists = eventLists;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_list, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            MyGroupEventResult.EVent eventList = eventLists.get(position);

            Glide.with(ListActivity.this)
                    .load(eventList.photo)
                    .into(holder.ivEvent);
            holder.itemView.setTag(eventList.id);
            holder.tvEventName.setText(eventList.title);
            holder.tvEventPlace.setText(eventList.place);
            holder.tvEventDate.setText(eventList.start_date);
            holder.tvEventMoney.setText(Integer.toString(eventList.amount));
            if(eventList.is_manager==0){
                holder.isManager.setVisibility(View.INVISIBLE);
            }else{
                holder.isManager.setVisibility(View.VISIBLE);
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListActivity.this, EventDetailActivity.class);
                    intent.putExtra("event_id", Integer.parseInt(holder.itemView.getTag().toString()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventLists.size();
        }
    }

    /**********************************ViewHolder********************************/

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEvent;
        TextView tvEventName;
        TextView tvEventDate;
        TextView tvEventPlace;
        TextView tvEventMoney;
        ImageView isManager;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventPlace = (TextView) itemView.findViewById(R.id.tvEventPlace);
            tvEventDate = (TextView) itemView.findViewById(R.id.tvEventDate);
            tvEventMoney = (TextView) itemView.findViewById(R.id.tvEventMoney);
            isManager = (ImageView) itemView.findViewById(R.id.isManager);
        }
    }
}
