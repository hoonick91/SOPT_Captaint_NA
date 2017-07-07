package org.sopt.captainna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.sopt.captainna.R;
import org.sopt.captainna.dialog.PasswordDialog;
import org.sopt.captainna.model.SearchGroupResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static org.sopt.captainna.R.id.ivEvent;
import static org.sopt.captainna.activity.CommonVariable.token;

public class SearchGroupActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;


    private EditText etSearchGroup;
    private ArrayList<SearchGroupResult.SearchResult> myGroup_lists;

    //리사이클러뷰 설정
    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    private LinearLayoutManager layoutManager;

    //다이어롤그
    private PasswordDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        //네트워크초기화
        initNetwork();

        //뒤로가기
        goBack();

        //findView
        findView();

        //더메데이터 세팅
        makeDummy();

        //검색기능
        search();

    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }
    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchGroupActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /***************************************검색기능***********************************************/
    private void search(){
  /*      etSearchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 변경 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 변경 되는 순간
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력이 끝난 후 처리를 담당
                String text = etSearchGroup.getText().toString().toLowerCase();
                myRecyclerAdapter.filter(text);
            }
        });*/
        etSearchGroup.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchServer();
                        break;
                    default:
                    //    searchServer();
                        return false;
                }
                return true;

            }
        });

    }
    /*************************************서버에서 검색*****************************************/
    private void searchServer(){
        Call<SearchGroupResult> request = service.searchGroup(token,etSearchGroup.getText().toString());
        request.enqueue(new Callback<SearchGroupResult>() {
            @Override
            public void onResponse(Call<SearchGroupResult> call, Response<SearchGroupResult> response) {
                if (response.isSuccessful()) {

                    //가져온 데이터
                    myGroup_lists = response.body().groups;


                    //리사이클러뷰 세팅
                    initRecycler();
                }
            }

            @Override
            public void onFailure(Call<SearchGroupResult> call, Throwable t) {

            }
        });

    }

    /****************************************findView**********************************************/
    private void findView() {
        etSearchGroup = (EditText) findViewById(R.id.etSearchGroup);


    }

    /***************************************더미 설정*****************************************/
    private void makeDummy() {

        myGroup_lists = new ArrayList<>();

    }


    /***************************************리사이클러 설정*****************************************/
    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.rvSearchGroup);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //어뎁터 생성, 리사이클러뷰에 붙임
        myRecyclerAdapter = new MyRecyclerAdapter(myGroup_lists);
        recyclerView.setAdapter(myRecyclerAdapter);
    }


    /***********************************Adapter**********************************/
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

        ArrayList<SearchGroupResult.SearchResult> myGroup_lists;
        ArrayList<SearchGroupResult.SearchResult> filterdatas;

        public MyRecyclerAdapter(ArrayList<SearchGroupResult.SearchResult> myGroup_lists) {
            this.myGroup_lists = myGroup_lists;
            this.filterdatas = new ArrayList<SearchGroupResult.SearchResult>();
            this.filterdatas.addAll(myGroup_lists);
        }

        public void setAdapter(ArrayList<SearchGroupResult.SearchResult> myGroup_lists) {
            this.myGroup_lists = myGroup_lists;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_group, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            SearchGroupResult.SearchResult myGroup_list = myGroup_lists.get(position);

            holder.itemView.setTag(myGroup_list.id);
            Glide.with(SearchGroupActivity.this)
                    .load(myGroup_list.photo)
                    .into(holder.ivGroupImg);
            holder.tvGroupName.setText(myGroup_list.title);
            holder.tvGroupIntro.setText(myGroup_list.text);
            holder.tvGroupCaptain.setText(myGroup_list.chairman_name);
            holder.tvJoinNumber.setText(Integer.toString(myGroup_list.count));


            //item 하나 클릭시
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new PasswordDialog(SearchGroupActivity.this,holder.itemView.getTag().toString());
                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return myGroup_lists.size();
        }

    }


    /**********************************ViewHolder********************************/
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGroupImg;
        TextView tvGroupName;
        TextView tvGroupIntro;
        TextView tvGroupCaptain;
        TextView tvJoinNumber;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivGroupImg = (ImageView) itemView.findViewById(R.id.ivGroupImg);
            tvGroupName = (TextView) itemView.findViewById(R.id.tvGroupName);
            tvGroupIntro = (TextView) itemView.findViewById(R.id.tvGroupIntro);
            tvGroupCaptain = (TextView) itemView.findViewById(R.id.tvGroupCaptain);
            tvJoinNumber = (TextView) itemView.findViewById(R.id.tvJoinNumber);
        }
    }



}


