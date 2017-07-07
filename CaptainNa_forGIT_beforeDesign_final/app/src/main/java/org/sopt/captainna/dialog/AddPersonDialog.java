package org.sopt.captainna.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.captainna.R;
import org.sopt.captainna.activity.EventJoinerActivity;
import org.sopt.captainna.model.AddList;
import org.sopt.captainna.model.AddParticipantList;
import org.sopt.captainna.model.AddPerson;
import org.sopt.captainna.model.Contacts;
import org.sopt.captainna.model.list;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.isGiver;
import static org.sopt.captainna.activity.CommonVariable.token;

/**
 * Created by woohyeju on 2017-06-30.
 */

public class AddPersonDialog extends Dialog {

    //네트워킹
    NetworkService service;


    private TextView apCancel;
    private TextView apSubmit;

    //리사이클러뷰 설정
    private RecyclerView recyclerView;
    private AddPersonRecyclerAdapter AddPersonRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<AddPerson> addPersonLists;

    //contacts
    private ArrayList<Contacts> contactes;

    //edittext정보 저장변수
    private ArrayList<String> inputMoney;

    private int event_id;
    private int isgiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.7f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_add);


        //네트워크초기화
        initNetwork();

        //클릭한 사람 정보 가져오기
        getPerson();

        //findView
        findView();

        //버튼 리스너 설정
        clickListener();

        //리사이클러뷰 초기설정
        initRecycler();
    }

    public AddPersonDialog(Context context, ArrayList<Contacts> contactes, int event_id) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.contactes = contactes;
        this.event_id = event_id;
        Log.d("contactes!!", contactes.get(0).name);
    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }



    /****************************************findView**********************************************/
    private void findView() {

        apCancel = (TextView) findViewById(R.id.cancelAdd);
        apSubmit = (TextView) findViewById(R.id.submitAdd);

        if(isGiver) {
            apSubmit.setText("입금자 추가 (" +addPersonLists.size()+ ")");
        }
        else{
            apSubmit.setText("미입금자 추가 (" +addPersonLists.size()+ ")");
        }
    }
    /********************************클릭한 사람 정보 가져오기*************************************/
    private void getPerson() {
        addPersonLists = new ArrayList<>();
        for(int i=0; i<contactes.size();i++){
            if(contactes.get(i).background == 0xFFF1F1F1){ //선택된 경우
                addPersonLists.add(new AddPerson(contactes.get(i).name, contactes.get(i).phone));
            }
        }
    }



    /***************************************리사이클러 설정*****************************************/
    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.addRecyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        //어뎁터 생성, 리사이클러뷰에 붙임
        AddPersonRecyclerAdapter = new AddPersonRecyclerAdapter(addPersonLists);
        recyclerView.setAdapter(AddPersonRecyclerAdapter);
    }

    /***********************************Adapter**********************************/
    class AddPersonRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<AddPerson> addPersonLists;
        private ArrayList<String> Money ;
        private MyViewHolder viewHolder;

        public AddPersonRecyclerAdapter(ArrayList<AddPerson> addPersonLists) {
            this.addPersonLists = addPersonLists;
            Money = new ArrayList<String>();
        }

        public void setAdapter(ArrayList<AddPerson> addPersonLists) {
            this.addPersonLists = addPersonLists;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_add_person, parent, false);
            viewHolder = new MyViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            AddPerson eventList = addPersonLists.get(position);


            if(eventList.phone.contains("-")) {
                String phoneNumber[] =eventList.phone.toString().split("-");
                String phone_split="";
                for(int i=0;i<phoneNumber.length;i++){
                    phone_split += phoneNumber[i];
                }
                addPersonLists.get(position).phone = phone_split;
                eventList.phone = phone_split;
            }


            holder.apName.setText(eventList.name);
            holder.apPhone.setText(eventList.phone);


        }

        @Override
        public int getItemCount() {
            return addPersonLists.size();
        }

        public ArrayList<String> getMoney(){
            for(int i=0; i < AddPersonRecyclerAdapter.getItemCount(); i++) {
                if(recyclerView.findViewHolderForLayoutPosition(i) instanceof MyViewHolder) {
                    MyViewHolder childHolder = (MyViewHolder) recyclerView.findViewHolderForLayoutPosition(i);

                    if (childHolder.apEdit.getText().toString().isEmpty()) {
                        Money.add("0");
                    } else {
                        Money.add(childHolder.apEdit.getText().toString());
                    }
                }
            }
            return Money;
        }
    }

    /**********************************ViewHolder********************************/

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView apName;
        TextView apPhone;
        EditText apEdit;

        public MyViewHolder(View itemView) {
            super(itemView);

            apName = (TextView) itemView.findViewById(R.id.apName);
            apPhone = (TextView) itemView.findViewById(R.id.apPhone);
            apEdit = (EditText) itemView.findViewById(R.id.moneyEdit);
        }
    }

    /***************************************리스너 설정하기****************************************/
    private void clickListener(){
        apSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*************************************서버 연결****************************************/


                inputMoney = AddPersonRecyclerAdapter.getMoney();

                if(isGiver)//입금자에서 온경우
                    isgiver=1;
                else
                    isgiver=0;

                AddList choiceList = new AddList();


                for(int i=0;i<addPersonLists.size();i++){
                    Log.d("name",addPersonLists.get(i).name);
                    Log.d("phone",addPersonLists.get(i).phone);
                    Log.d("money",inputMoney.get(i));
                    choiceList.deposit_status = isgiver;
                    choiceList.not_users.add(new list(addPersonLists.get(i).name,addPersonLists.get(i).phone,
                            Integer.parseInt(inputMoney.get(i))));

                }



                Call<AddParticipantList> request = service.setParticipateList(token, event_id, choiceList);
                request.enqueue(new Callback<AddParticipantList>() {
                    @Override
                    public void onResponse(Call<AddParticipantList> call, Response<AddParticipantList> response) {
                        if (response.isSuccessful()) {
                            if (response.body().result.equals("SYNC SUCCESS")) {
                                Toast.makeText(getContext(), "추가되었습니다", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                               Log.d("error!","error");
                        }
                    }

                    @Override
                    public void onFailure(Call<AddParticipantList> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            /*    for(int i=0; i<addPersonLists.size();i++) {
                    if(isGiver) {
                        addGiverPersons.add(new MoneyPerson(addPersonLists.get(i).name, inputMoney.get(i), addPersonLists.get(i).phone,false,0));
                    }
                    else{
                        addNonGiverPersons.add(new MoneyPerson(addPersonLists.get(i).name, inputMoney.get(i), addPersonLists.get(i).phone,false,0));
                    }
                }*/
                Intent intent = new Intent(getContext(), EventJoinerActivity.class);
                intent.putExtra("event_id", event_id);
                getContext().startActivity(intent);

            }
        });

        apCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
