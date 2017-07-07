package org.sopt.captainna.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.sopt.captainna.R;
import org.sopt.captainna.model.ApplyResult;
import org.sopt.captainna.model.DeleteApplyResult;
import org.sopt.captainna.model.EventDetailResult;
import org.sopt.captainna.model.GroupDetail;
import org.sopt.captainna.model.MyPageResult;
import org.sopt.captainna.model.PayResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.pushEditDetail;
import static org.sopt.captainna.activity.CommonVariable.subEditDetail;
import static org.sopt.captainna.activity.CommonVariable.token;

public class EventDetailActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;

    private SlidingUpPanelLayout slidingLayout;
    private RecyclerView rvEventOption;
    private LinearLayoutManager layoutManager;
    private optRecyclerAdapter optrecyclerAdapter;
    private TextView optionContent_title;
    private TextView price_title;
    private RelativeLayout dragView;

    private int already_participate;
    private int already_paid;

    private ImageView ivEventImg, ivGroupImg;
    private TextView tvGroupName, tvEventName, tvEventMaker, tvEventIntro, tvEventPlace, tvEventDate, tvEventMoney, otsum;
    ImageButton ibApply;
    private ImageButton checkPay, cancelPar;
    private LinearLayout editDetail;

    private GroupDetail detailGroup;
    private ArrayList<EventDetailResult.SUb_event> optionGroups;

    private boolean click = false;
    private boolean setting = false;
    private boolean is_manager = false;


    private int event_id;
    private int group_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        optionGroups = new ArrayList<>();
        //temp(명단보는버튼 temp)

        editDetail = (LinearLayout) findViewById(R.id.editDetail);
        editDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this, EditEventActivity.class);
                intent.putExtra("event_id",event_id);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
                finish();
            }
        });


        //네트워크초기화
        initNetwork();

        //home 화면에서 해당 이벤트 아이디 받기
        getEventID();

        //그룹 아이디 받기
        getGroupID();

        //뒤로가기
        goBack();

        //공유하기 버튼클릭
        gokakao();

        //findView
        findView();

        //슬라이딩 리스너 설정
        setSlidingListener();


        //참가하기 눌렀을 경우
        applyEvent();

        //이벤트 상세 정보 가져오기
        getEventDetail();




    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /***********************************이벤트 상세 정보 가져오기*********************************/
    private void getEventDetail() {
        Call<EventDetailResult> request = service.getEventDetail(token, event_id);
        request.enqueue(new Callback<EventDetailResult>() {
            @Override
            public void onResponse(Call<EventDetailResult> call, Response<EventDetailResult> response) {
                if (response.isSuccessful()) {

                    pushEditDetail = response.body().event;
                    subEditDetail = response.body().sub_event;



                    group_id = Integer.parseInt(response.body().event.group_id);
                    //화면 배치
                    Glide.with(EventDetailActivity.this)
                            .load(response.body().event.photo)
                            .into(ivEventImg);

                    Glide.with(EventDetailActivity.this)
                            .load(response.body().event.group_photo)
                            .into(ivGroupImg);

                    tvGroupName.setText(response.body().event.group_title);
                    tvEventName.setText(response.body().event.title);
                    tvEventMaker.setText(response.body().event.manager_name);
                    tvEventIntro.setText(response.body().event.text);
                    tvEventDate.setText(response.body().event.start_date);
                    tvEventPlace.setText(response.body().event.place);
                    tvEventMoney.setText(Integer.toString(response.body().event.amount));
                    optionContent_title.setText(response.body().event.place);
                    price_title.setText(Integer.toString(response.body().event.amount));
                    already_participate = response.body().event.is_participated;

                    already_paid = response.body().event.is_paid;

                    otsum.setText(price_title.getText().toString());


                    //선택한 옵션 제이슨으로 받아야함. 옵션과 already_participate 변수에 따라 view를 조정해야함!

                    if(response.body().event.is_manager == 1){//총무인 경우
                        editDetail.setVisibility(View.VISIBLE);
                        is_manager = true;
                        ibApply.setImageResource(R.drawable.participant_list_button);
                    }
                    else{
                        editDetail.setVisibility(View.GONE);
                        is_manager = false;
                        if(already_participate == 0){//아직 참가신청자가 아닐 경우

                            //그냥 원래대로 냅둠
                        }else if(already_participate == 1){
                            if(already_paid == 0){ //참가신청은 햇으나 아직 결제를 안한 경우
                                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                checkPay.setVisibility(View.VISIBLE);
                                cancelPar.setVisibility(View.VISIBLE);
                                ibApply.setVisibility(View.INVISIBLE);
                                setting = true;
                                click = false;
                            }
                            else{ //참가신청도 하고 결제도 완료한 사람
                                checkPay.setVisibility(View.INVISIBLE);
                                cancelPar.setVisibility(View.INVISIBLE);
                                ibApply.setVisibility(View.VISIBLE);
                                ibApply.setImageResource(R.drawable.payment_finish_button);
                                setting = true;
                            }
                        }
                    }

                    optionGroups = response.body().sub_event;

                    rvEventOption = (RecyclerView) findViewById(R.id.rvOptions);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvEventOption.setLayoutManager(layoutManager);



                    //어뎁터 생성, 리사이클러뷰에 붙임
                    optrecyclerAdapter = new optRecyclerAdapter(optionGroups);
                    rvEventOption.setAdapter(optrecyclerAdapter);

                    if(optrecyclerAdapter.getItemCount() == 0){
                        dragView.setBackgroundResource(R.drawable.payment1);
                    }
                    else if(optrecyclerAdapter.getItemCount() == 1){
                        dragView.setBackgroundResource(R.drawable.payment2);
                    }
                    else if(optrecyclerAdapter.getItemCount() == 2){
                        dragView.setBackgroundResource(R.drawable.payment3);
                    }
                    else{
                        dragView.setBackgroundResource(R.drawable.payment4);
                    }
                }
            }

            @Override
            public void onFailure(Call<EventDetailResult> call, Throwable t) {
            }
        });

    }

    /**********************************이벤트 아이디 받기*******************************************/
    private void getEventID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        event_id = intent.getExtras().getInt("event_id");

    }

    /**********************************그룹 아이디 받기*******************************************/
    private void getGroupID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("group_id");

    }

    private void goBack() {

        LinearLayout back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /********************************* 공유하기 버튼 *****************************************/
    private void gokakao() {
        LinearLayout share = (LinearLayout) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventShare();
            }
        });
    }

    /******************************카카오톡 공유하기******************************************/
    private void eventShare() {
        try {

            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            //메세지 추가
            kakaoBuilder.addText("[ 나총무 금주의 추천 행사 ]");

            //이미지 가로세로 80px보다 커야하며 이미지용량 500kb제한
            //이미지 url
            String url = "http://cafefiles.naver.net/20140729_251/yourastar_1406641095345U8amz_JPEG/1406513705318.jpeg";
            kakaoBuilder.addImage(url, 200, 140);

            //앱실행버튼 추가
            kakaoBuilder.addAppButton("나총무 앱으로 가기");

            //메세지 발송
            kakaoLink.sendMessage(kakaoBuilder, this);


        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }


    /***************************************참가하기 누르면***************************************/
    private void applyEvent() {

        ibApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_manager) {
                    if (!setting) {
                        if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            click = false;
                        }

                        if (click == false) { //슬라이딩바 올라오기
                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            click = true;


                        } else if (click == true) { //참가 신청하는 부분
                            Call<ApplyResult> request = service.setApply(token, event_id);
                            request.enqueue(new Callback<ApplyResult>() {
                                @Override
                                public void onResponse(Call<ApplyResult> call, Response<ApplyResult> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().result.equals("apply event success")) {
                                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                            checkPay.setVisibility(View.VISIBLE);
                                            cancelPar.setVisibility(View.VISIBLE);
                                            ibApply.setVisibility(View.INVISIBLE);
                                            setting = true;
                                            click = false;

                                        }
                                    } else {
                                        if (response.code() == 406) {
                                            Toast.makeText(EventDetailActivity.this, "이미 참여한 행사입니다!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<ApplyResult> call, Throwable t) {

                                }
                            });

                        }
                    }
                }
                else{ //총무일때
                    Intent intent = new Intent(EventDetailActivity.this, EventJoinerActivity.class);
                    intent.putExtra("event_id",event_id);
                    startActivity(intent);
                    finish();
                }
            }
        });

        checkPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                    click = false;

                if (click == false) { //슬라이딩바 올라오기
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                    click = true;
                } else if (click == true) { //결제완료 다이얼로그 출력해야 함
                    /*****************************결제하기 서버****************************************/
                    Call<PayResult> request = service.paid(token,event_id);
                    request.enqueue(new Callback<PayResult>() {
                        @Override
                        public void onResponse(Call<PayResult> call, Response<PayResult> response) {
                            if (response.isSuccessful()) {
                                if(response.body().result.equals("update payment success")){
                                    Toast.makeText(EventDetailActivity.this,"결제 완료",Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        @Override
                        public void onFailure(Call<PayResult> call, Throwable t) {
                        }

                    });
                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    checkPay.setVisibility(View.INVISIBLE);
                    cancelPar.setVisibility(View.INVISIBLE);
                    ibApply.setVisibility(View.VISIBLE);
                    ibApply.setImageResource(R.drawable.payment_finish_button);
                }
            }
        });

        cancelPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DeleteApplyResult> request = service.deleteapply(token,event_id);
                request.enqueue(new Callback<DeleteApplyResult>() {
                    @Override
                    public void onResponse(Call<DeleteApplyResult> call, Response<DeleteApplyResult> response) {
                        if (response.isSuccessful()) {
                            if(response.body().result.equals("delete apply success")){
                                Toast.makeText(EventDetailActivity.this,"참가 신청을 취소하셨습니다.",Toast.LENGTH_LONG).show();
                                checkPay.setVisibility(View.INVISIBLE);
                                cancelPar.setVisibility(View.INVISIBLE);
                                ibApply.setVisibility(View.VISIBLE);
                                setting = false;
                                click = false;

                            }
                        }else {
                           //에러코드 수정
                        }

                    }

                    @Override
                    public void onFailure(Call<DeleteApplyResult> call, Throwable t) {

                    }

                });
            }
        });


    }



    /************************************find View************************************************/
    private void findView() {
        ivEventImg = (ImageView) findViewById(R.id.ivEventImg);
        ivGroupImg = (ImageView) findViewById(R.id.ivGroupImg);
        tvGroupName = (TextView) findViewById(R.id.tvGroupName);
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvEventMaker = (TextView) findViewById(R.id.tvEventMaker);
        tvEventIntro = (TextView) findViewById(R.id.tvEventIntro);
        tvEventDate = (TextView) findViewById(R.id.tvEventDate);
        tvEventPlace = (TextView) findViewById(R.id.tvEventPlace);
        tvEventMoney = (TextView) findViewById(R.id.tvEventMoney);
        optionContent_title = (TextView)findViewById(R.id.optionContent_title);
        price_title = (TextView)findViewById(R.id.price_title);
        ibApply = (ImageButton) findViewById(R.id.ibApply);
        otsum = (TextView) findViewById(R.id.priceSum);

        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        dragView = (RelativeLayout)findViewById(R.id.dragView);
        checkPay = (ImageButton)findViewById(R.id.checkPay);
        cancelPar = (ImageButton)findViewById(R.id.cancelPar);



    }

    /*******************************슬라이딩 리스너************************************************/
    private void setSlidingListener() {
        slidingLayout.setPanelSlideListener(onSlideListener());
    }

    /*******************************슬라이딩 모션별 작업*******************************************/
    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                //  textView.setText("panel is sliding"); //메뉴바 움직이는 중
                //    slidingLayout.setCoveredFadeColor(00000000);
            }

            @Override
            public void onPanelCollapsed(View view) {
                //  textView.setText("panel Collapse"); //다시한번 눌렀을 때
            }

            @Override
            public void onPanelExpanded(View view) {
                //  textView.setText("panel expand");//참가하기 눌럿을 때
                //     slidingLayout.setCoveredFadeColor(00000000);
            }

            @Override
            public void onPanelAnchored(View view) {
                //  textView.setText("panel anchored");
            }

            @Override
            public void onPanelHidden(View view) {
                //  textView.setText("panel is Hidden");
            }
        };
    }



    /***********************************Adapter**********************************/
    class optRecyclerAdapter extends RecyclerView.Adapter<EventDetailActivity.OptViewHolder> {

        ArrayList<EventDetailResult.SUb_event> optionGroups;
        // 혜주 부분  ArrayList<EventDetailActivity.OptViewHolder> holders;

        public optRecyclerAdapter(ArrayList<EventDetailResult.SUb_event> optionGroups) {
            this.optionGroups = optionGroups;
            // 혜주 부분  holders = new ArrayList<EventDetailActivity.OptViewHolder>();
        }

        public void setAdapter(ArrayList<EventDetailResult.SUb_event> optionGroups) {
            this.optionGroups = optionGroups;
            notifyDataSetChanged();
        }

        @Override
        public EventDetailActivity.OptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_option, parent, false);
            final EventDetailActivity.OptViewHolder viewHolder = new EventDetailActivity.OptViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /* 혜주 부분 - 한번 클릭시 모두선택 기능되도록 하는 부분
                    for(int i=0; i<optrecyclerAdapter.getItemCount();i++){
                        holders.get(i).optionCheck.setVisibility(View.VISIBLE);
                    }
                    */
                if(!setting) {
                    Drawable temp = viewHolder.optionCheck.getDrawable();
                    Drawable temp1 = getResources().getDrawable(R.drawable.event_check1);
                    Bitmap tmpBitmap = ((BitmapDrawable) temp).getBitmap();
                    Bitmap tmpBitmap1 = ((BitmapDrawable) temp1).getBitmap();

                    if (tmpBitmap.equals(tmpBitmap1)) {

                        int sum = Integer.parseInt(otsum.getText().toString());
                        sum -= Integer.parseInt(viewHolder.otPrice.getText().toString());
                        otsum.setText(sum + "");
                        viewHolder.optionCheck.setImageResource(R.drawable.event_check2);
                        viewHolder.otPrice.setTextColor(getResources().getColor(R.color.colorNonCheck));
                        viewHolder.otContent.setTextColor(getResources().getColor(R.color.colorNonCheck));
                        viewHolder.otNum.setTextColor(getResources().getColor(R.color.colorNonCheck));
                        viewHolder.won.setTextColor(getResources().getColor(R.color.colorNonCheck));
                    } else {

                        int sum = Integer.parseInt(otsum.getText().toString());
                        sum += Integer.parseInt(viewHolder.otPrice.getText().toString());
                        otsum.setText(sum + "");
                        viewHolder.optionCheck.setImageResource(R.drawable.event_check1);
                        viewHolder.otPrice.setTextColor(getResources().getColor(R.color.colorMain));
                        viewHolder.otContent.setTextColor(getResources().getColor(R.color.colorMain));
                        viewHolder.otNum.setTextColor(getResources().getColor(R.color.colorMain));
                        viewHolder.won.setTextColor(getResources().getColor(R.color.colorMain));
                    }
                }

                }
            });

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final EventDetailActivity.OptViewHolder holder, int position) {

            holder.otNum.setText(optionGroups.get(position).event_count + "");
            holder.otContent.setText(optionGroups.get(position).place);
            holder.otPrice.setText(optionGroups.get(position).price + "");
            if(optionGroups.get(position).is_participated.equals("0")) { //옵션 선택 안했을때
                holder.optionCheck.setImageResource(R.drawable.event_check2);
            }
            else {
                holder.optionCheck.setImageResource(R.drawable.event_check1);
                int sum = Integer.parseInt(otsum.getText().toString());
                sum += Integer.parseInt(holder.otPrice.getText().toString());
                otsum.setText(sum + "");
                holder.otPrice.setTextColor(getResources().getColor(R.color.colorMain));
                holder.otContent.setTextColor(getResources().getColor(R.color.colorMain));
                holder.otNum.setTextColor(getResources().getColor(R.color.colorMain));
                holder.won.setTextColor(getResources().getColor(R.color.colorMain));
            }

            //혜주 부분  holders.add(holder);


        }

        @Override
        public int getItemCount() {
            return optionGroups.size();
        }
    }

    /**********************************ViewHolder********************************/

    class OptViewHolder extends RecyclerView.ViewHolder {


        TextView otNum;
        TextView otContent;
        TextView otPrice;
        TextView won;
        ImageView optionCheck;

        public OptViewHolder(View itemView) {
            super(itemView);
            otNum = (TextView) itemView.findViewById(R.id.optionNum);
            otContent = (TextView) itemView.findViewById(R.id.optionContent);
            otPrice = (TextView) itemView.findViewById(R.id.price);
            won = (TextView) itemView.findViewById(R.id.won);
            optionCheck = (ImageView) itemView.findViewById(R.id.optionCheck);
        }
    }

}
