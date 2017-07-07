package org.sopt.captainna.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.sopt.captainna.R;
import org.sopt.captainna.activity.ContentProviderActivity;
import org.sopt.captainna.activity.EventJoinerActivity;
import org.sopt.captainna.model.MoneyList;
import org.sopt.captainna.model.MoneyPerson;
import org.sopt.captainna.model.ParticipateList;
import org.sopt.captainna.model.UserId;
import org.sopt.captainna.model.moveResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.addGiverPersons;
import static org.sopt.captainna.activity.CommonVariable.dataList;
import static org.sopt.captainna.activity.CommonVariable.givercount;
import static org.sopt.captainna.activity.CommonVariable.isGiver;
import static org.sopt.captainna.activity.CommonVariable.token;

public class GiverFragment extends Fragment {

    //네트워킹
    NetworkService service;
    private int event_id;
    private ArrayList<MoneyList> giverList;

    private FloatingActionButton fab;
    private EditText searchBox;

    private RecyclerView recyclerView;
    private ArrayList<MoneyPerson> itemdata;
    private RecyclerAdapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private static int giverCNT;

    private UserId userId = new UserId();

    //프로그레스 다이얼로그
    private ProgressDialog progDialog;
    private Handler confirmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //완료 후 실행할 처리 삽입
        }
    };

    public GiverFragment() {

    }
    @SuppressLint("ValidFragment")
    public GiverFragment(int event_id) {
        this.event_id = event_id;
        giverList = new ArrayList<>();
        itemdata = new ArrayList<MoneyPerson>();
    }


    public static Fragment newInstance(int event_id) {
        Fragment firstPage = new GiverFragment(event_id);
        Bundle args = new Bundle();
        args.putInt("giverCNT", giverCNT);
        firstPage.setArguments(args);
        return firstPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_giver, container, false);


        //네트워크초기화
        initNetwork();

        //입금자 미입금자 가져오기
        getMoneyList();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);


/*******************************************검색관련******************************************/

        searchBox = (EditText) view.findViewById(R.id.editText);
        searchBox.addTextChangedListener(new TextWatcher() {
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
                String text = searchBox.getText().toString().toLowerCase();
                recyclerAdapter.filter(text);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.attachToRecyclerView(recyclerView);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    fab.hide();
                } else {
                    fab.show();
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
                progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progDialog.setMessage("연락처를 가지고 오는 중입니다");
                progDialog.show();

                //Thread 사용은 선택이 아니라 필수
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //TODO : 시간이 걸리는 처리 삽입
                    /*  try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        //전화번호부 가져오기
                        getContacts();

                        //Handler를 호출
                        confirmHandler.sendEmptyMessage(0);

                        //dismiss(다이알로그종료)는 반드시 새로운 쓰레드 안에서 실행되어야한다
                        progDialog.dismiss();
                        isGiver = true;
                        Intent intent = new Intent(getContext(), ContentProviderActivity.class);
                        intent.putExtra("event_id", event_id);
                        startActivity(intent);

                    }
                }).start();


            }
        });

        return view;
    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }


    /****************************************참가자 명단 가져오기*****************************************/
    private void getMoneyList() {

        Log.d("event_id : ", "" + event_id);
        Call<ParticipateList> request = service.getParticipateList(token, event_id);
        request.enqueue(new Callback<ParticipateList>() {
            @Override
            public void onResponse(Call<ParticipateList> call, Response<ParticipateList> response) {
                if (response.isSuccessful()) {
                    Log.d("response : ", "success");
                    giverList = response.body().paid_list;
                    Log.d("giverListsize1 : ", "" + giverList.size());

                    for (int i = 0; i < giverList.size(); i++) {
                        itemdata.add(new MoneyPerson(giverList.get(i).name, Integer.toString(giverList.get(i).amount), giverList.get(i).ph, false, 0));
                    }


                    addGiverList();

                    giverCNT = itemdata.size();
                    givercount = response.body().paid_count;

                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);

                    recyclerAdapter = new RecyclerAdapter(itemdata, clickEvent);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<ParticipateList> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });

    }


    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {


        }
    };

    /*******************************************입금자 어뎁터******************************************/

    public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<MoneyPerson> itemdatas;
        ArrayList<MoneyPerson> filterdatas;
        View.OnClickListener clickListener;


        public RecyclerAdapter(ArrayList<MoneyPerson> itemdatas, View.OnClickListener clickListener) {
            this.itemdatas = itemdatas;
            this.filterdatas = new ArrayList<MoneyPerson>();
            this.filterdatas.addAll(itemdatas);
            this.clickListener = clickListener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_joiner, parent, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            view.setOnClickListener(clickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            //혜주
            EventJoinerActivity eventJoinerActivity = (EventJoinerActivity) getActivity();

            RelativeLayout defaultTB = (RelativeLayout) eventJoinerActivity.findViewById(R.id.defaultTB);
            RelativeLayout selectTB = (RelativeLayout) eventJoinerActivity.findViewById(R.id.selectTB);
            LinearLayout choicebtn = (LinearLayout) eventJoinerActivity.findViewById(R.id.choicebtn);
            TabLayout tabLayout = (TabLayout) eventJoinerActivity.findViewById(R.id.tablayout);
            final ImageView choiceimg = (ImageView) eventJoinerActivity.findViewById(R.id.choiceimg);
            TextView choicetxt = (TextView) eventJoinerActivity.findViewById(R.id.choicetxt);
            TextView sendbtn = (TextView) eventJoinerActivity.findViewById(R.id.sendbtn);


            holder.custom_item_name.setText(itemdatas.get(position).name);
            holder.custom_item_money.setText(itemdatas.get(position).money);
            holder.custom_item_phone.setText(itemdatas.get(position).phone);

            if (itemdata.get(position).visibleBox == false) { //비활성화상태
                holder.checkBox.setVisibility(View.GONE);
            } else {//활성화상태
                holder.checkBox.setVisibility(View.VISIBLE);
                defaultTB.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                selectTB.setVisibility(View.VISIBLE);
                sendbtn.setText("미입금자로 보내기");
            }

            if (itemdata.get(position).checkbox == 0) {//체크박스 체크x
                holder.checkBox.setImageResource(R.drawable.participant_movement1);
            } else {
                holder.checkBox.setImageResource(R.drawable.participant_movement2);
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    for (int i = 0; i < itemdata.size(); i++) {
                        if (itemdata.get(i).visibleBox == false) { //버튼 비활성화 상태
                            itemdatas.get(i).visibleBox = true;
                        }
                    }
                    notifyDataSetChanged();
                    return false;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemdata.get(position).checkbox == 0) { //버튼 안눌린상태
                        itemdatas.get(position).checkbox = 1;
                    } else {// 버튼눌린상태
                        itemdatas.get(position).checkbox = 0;
                    }
                    notifyDataSetChanged();
                }
            });

            //혜주
            sendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userId.target_deposit_status = 0;
                    userId.user_list = new ArrayList<>();

                    for (int i = 0; i < itemdata.size(); i++) {
                        if (itemdata.get(i).checkbox == 1) { //체크되어있는 상태인경우
                            userId.user_list.add(new UserId.User_pk(giverList.get(i).user_pk));
                        }
                    }

                    Call<moveResult> request = service.move(token, event_id, userId);
                    request.enqueue(new Callback<moveResult>() {
                        @Override
                        public void onResponse(Call<moveResult> call, Response<moveResult> response) {
                            if (response.isSuccessful()) {
                                if (response.body().result.equals("SYNC SUCCESS")) {
                                    Toast.makeText(getContext(), "이동을 완료하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<moveResult> call, Throwable t) {
                            Log.i("err", t.getMessage());
                        }
                    });

                    Intent intent = new Intent(getContext(), EventJoinerActivity.class);
                    intent.putExtra("event_id", event_id);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            choicebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < itemdata.size(); i++) {
                        if (itemdata.get(i).checkbox == 0) {//체크박스 체크x
                            itemdata.get(i).checkbox = 1;
                            choiceimg.setImageResource(R.drawable.participant_all_check2);
                        } else {
                            itemdata.get(i).checkbox = 0;
                            choiceimg.setImageResource(R.drawable.participant_all_check1);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return itemdatas != null ? itemdatas.size() : 0;
        }


        /*************************************검색***************************************/

        public void filter(String text) {
            String text_t = text.toLowerCase();
            String text_T = text.toUpperCase();
            itemdata.clear();
            if (text.length() == 0) {
                itemdata.addAll(filterdatas);
            } else {
                for (MoneyPerson item : filterdatas) {
                    if (item.getItem_text().contains(text_T) || item.getItem_text().contains(text_t)) {
                        itemdata.add(item);
                    }
                }
            }
            notifyDataSetChanged();

        }
    }


    /*******************************************입금자 뷰홀더******************************************/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView custom_item_name;
        TextView custom_item_money;
        TextView custom_item_phone;
        ImageView checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);

            custom_item_name = (TextView) itemView.findViewById(R.id.event_joiner_name);
            custom_item_money = (TextView) itemView.findViewById(R.id.event_joiner_money);
            custom_item_phone = (TextView) itemView.findViewById(R.id.event_joiner_phone);
            checkBox = (ImageView) itemView.findViewById(R.id.event_joiner_checkbox);

        }
    }


    /********************************전화번호부 가져오기******************************************/
    //엑티비티 띄우기 전에 미리 가져오는것! 프로그레스바 때문! 한번만 가져오게 할 수 있고(ex스플래쉬때, 프로그레스바 없이)
    //누를때마다 가져오게 할 수 있음. 현재는 누를때마다 가져옴. 한번만 가져오면 연락처 수정시 로딩x
    private void getContacts() {

        dataList = new ArrayList<Map<String, String>>();


        Cursor c = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

        while (c.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String name = "";
            String number = "";
            // 연락처 id 값, 등록순서?
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));


            // 연락처 대표 이름
            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            map.put("name", name);


            // ID로 전화 정보 조회
            Cursor phoneCursor = getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

            // 데이터가 있는 경우
            if (phoneCursor.moveToFirst()) {
                number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("phone", number);
            }
            phoneCursor.close();
            if (!number.equals("")) { //전화번호가 없는 경우 넣지 않음
                if (dataList.size() > 0) {
                    //맨처음이 아닐때(비교값 존재할때)
                    if (dataList.get(dataList.size() - 1).get("name").equals(name) && dataList.get(dataList.size() - 1).get("phone").equals(number)) {
                        //중복되는 값이니 add하지 않음
                    } else {
                        //중복되지 않는 값이니 넣음
                        dataList.add(map);
                    }
                } else {
                    //맨처음일때
                    dataList.add(map);
                }
            }

        }// end while
        c.close();
    }

    private void addGiverList() {

        if (addGiverPersons.size() > 0) {
            for (int i = addGiverPersons.size() - 1; i >= 0; i--) {
                itemdata.add(addGiverPersons.get(i));
                //  addGiverPersons.remove(i);
            }
        }

    }

}