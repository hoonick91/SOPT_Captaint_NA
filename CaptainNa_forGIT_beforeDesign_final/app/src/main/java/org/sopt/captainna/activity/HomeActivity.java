package org.sopt.captainna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.sopt.captainna.R;
import org.sopt.captainna.fragment.FirstMyGroupsFragment;
import org.sopt.captainna.fragment.SecondMyGroupsFragment;
import org.sopt.captainna.fragment.ThirdMyGroupsFragment;
import org.sopt.captainna.model.Event;
import org.sopt.captainna.model.Group;
import org.sopt.captainna.model.HomeResult;
import org.sopt.captainna.model.ImCaptian;
import org.sopt.captainna.model.ImLeader;
import org.sopt.captainna.model.MyPageResult;
import org.sopt.captainna.model.Participate_in;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.id_name;
import static org.sopt.captainna.activity.CommonVariable.token;


public class HomeActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;


    //상단부분
    private PagerContainer myPagerContainer;
    private ViewPager vpRecentEvents;
    private PagerAdapter paRecentEvents;
    private ArrayList<Event> events;


    //하단 부분
    private ViewPager vpMyGroups;
    private PageAdapterMyGroups paMyGroups;
    private ArrayList<Group> groups;
    private Fragment selectedFragment;

    //view
    private LinearLayout pageId;
    private ImageView ivEvent;
    private TextView tvGroupName;
    private TextView tvEventName;
    private TextView tvEventIntro;
    private TextView tvEventPlace;

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //네비게이션바
    private DrawerLayout drawerLayout;
    private View drawerView;
    private LinearLayout menu;
    private DrawerLayout.DrawerListener myDrawerListener;
    private LinearLayout lldelete;

    //마이페이지
    private CircleImageView profileImage ;
    private TextView profileName;
    private TextView profileEmail;
    private ImageView editProfile;
    private String phone;

    //내 행사(네비)
    private RecyclerView recyclerView_myEvent;
    private MyEventRecyclerAdapter RecyclerAdapter_myEvent;
    private LinearLayoutManager layoutManager_myEvent;
    private ArrayList<Participate_in> list_participatein;

    //나 총무(네비)
    private RecyclerView recyclerView_ImCap;
    private ImCaptainRecyclerAdapter RecyclerAdapter_ImCap;
    private LinearLayoutManager layoutManager_ImCap;
    private ArrayList<ImCaptian> List_ImCap;

    //나 모임장(네비)
    private RecyclerView recyclerView_ImLeader;
    private ImLeaderRecyclerAdapter RecyclerAdapter_ImLeader;
    private LinearLayoutManager layoutManager_ImLeader;
    private ArrayList<ImLeader> List_ImLeader;

    private int event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //네트워크초기화
        initNetwork();

        //더미데이터 생성
        makeDummy();

        //home 화면 networking
        getHomeInfo();

        //마이페이지 networking
        getMypage();

        //네비게이션 초기화
        initNav();

        //최근행사 초기 설정
        initRecentEvents(savedInstanceState);

        //내 모임 리스트 프레그먼트에 보내기
        getData();

        //더보기
        viewMoreEvents();

    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /************************************네트워킹*****************************************************/
    private void getHomeInfo() {
        Call<HomeResult> request = service.getHomeList(token);
        request.enqueue(new Callback<HomeResult>() {
            @Override
            public void onResponse(Call<HomeResult> call, Response<HomeResult> response) {
                if (response.isSuccessful()) {

                    //가져온 데이터
                    events = response.body().events;
                    groups = response.body().groups;

                    //최근행사
                    myPagerContainer = (PagerContainer) findViewById(R.id.pc);
                    vpRecentEvents = myPagerContainer.getViewPager();
                    paRecentEvents = new PageAdapterRecentEvents(events);
                    vpRecentEvents.setAdapter(paRecentEvents);
                    vpRecentEvents.setOffscreenPageLimit(paRecentEvents.getCount());
                    vpRecentEvents.setPageMargin(12);
                    vpRecentEvents.setClipChildren(false);
                    vpRecentEvents.setCurrentItem(1);


                    //내 모임 뷰페이저
                    paMyGroups = new PageAdapterMyGroups(getSupportFragmentManager(), groups);
                    vpMyGroups = (ViewPager) findViewById(R.id.vpMyGroups);
                    vpMyGroups.setAdapter(paMyGroups);
                    vpMyGroups.setCurrentItem(0);
                }
            }

            @Override
            public void onFailure(Call<HomeResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });

    }

    /************************************마이페이지 네트워킹*****************************************************/
    private void getMypage() {
        Call<MyPageResult> request2 = service.myPage(token);
        request2.enqueue(new Callback<MyPageResult>() {
            @Override
            public void onResponse(Call<MyPageResult> call, Response<MyPageResult> response) {
                if (response.isSuccessful()) {

                    profileImage = (CircleImageView)findViewById(R.id.profileImage);
                    profileName = (TextView)findViewById(R.id.profileName);
                    profileEmail= (TextView)findViewById(R.id.profileEmail);


                    //가져온 데이터
                    profileName.setText(response.body().my_profile.name) ;
                    id_name = response.body().my_profile.name;
                    profileEmail.setText(response.body().my_profile.email);
                    profileName.setText(response.body().my_profile.name);
                    phone = response.body().my_profile.ph;
                    list_participatein = response.body().participate_in;
                    List_ImCap =  response.body().my_event;
                    List_ImLeader = response.body().my_group;

                    Log.d("phone_data","asdf "+response.body().my_profile.ph);
                    initDrawer();

                }

            }

            @Override
            public void onFailure(Call<MyPageResult> call, Throwable t) {

            }

        });


    }
    /***********************************네비게이션 초기화******************************************/
    private void initNav() {

        //findView
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        menu = (LinearLayout) findViewById(R.id.menu);
        lldelete = (LinearLayout) findViewById(R.id.lldelete);

        //햄버거 클릭시
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(myDrawerListener);

        drawerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });


        myDrawerListener = new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerClosed(View drawerView) {
                //네비게이션 바가 닫힌 상태
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //네비게이션 바가 열린 상태
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //네비게이션바 슬라이딩중
                //String.format("%.2f", slideOffset)은 얼마나 열리고 닫혓는지(0:시작~100:끝)
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                String state;
                switch (newState) {
                    case DrawerLayout.STATE_IDLE: //슬라이딩 정지
                        state = "STATE_IDLE";
                        break;
                    case DrawerLayout.STATE_DRAGGING: //드래그중
                        state = "STATE_DRAGGING";
                        break;
                    case DrawerLayout.STATE_SETTLING://슬라이딩중
                        state = "STATE_SETTLING";
                        break;
                    default:
                        state = "unknown!";
                }

            }
        };
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        lldelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

    }

    /***************************************리사이클러뷰 초기화************************************/
    private void initDrawer() {

        //내 행사
        recyclerView_myEvent = (RecyclerView) findViewById(R.id.rvMyevent);
        layoutManager_myEvent = new LinearLayoutManager(this);
        layoutManager_myEvent.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_myEvent.setLayoutManager(layoutManager_myEvent);
        //어뎁터 생성, 리사이클러뷰에 붙임
        RecyclerAdapter_myEvent = new MyEventRecyclerAdapter(list_participatein);
        recyclerView_myEvent.setAdapter(RecyclerAdapter_myEvent);


        //나 총무
        recyclerView_ImCap = (RecyclerView) findViewById(R.id.rvMyCaptain);
        layoutManager_ImCap = new LinearLayoutManager(this);
        layoutManager_ImCap.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_ImCap.setLayoutManager(layoutManager_ImCap);
        //어뎁터 생성, 리사이클러뷰에 붙임
        RecyclerAdapter_ImCap = new ImCaptainRecyclerAdapter(List_ImCap);
        recyclerView_ImCap.setAdapter(RecyclerAdapter_ImCap);


        //나 모임장
        recyclerView_ImLeader = (RecyclerView) findViewById(R.id.rvReader);
        layoutManager_ImLeader = new LinearLayoutManager(this);
        layoutManager_ImLeader.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_ImLeader.setLayoutManager(layoutManager_ImLeader);
        //어뎁터 생성, 리사이클러뷰에 붙임
        RecyclerAdapter_ImLeader = new ImLeaderRecyclerAdapter(List_ImLeader);
        recyclerView_ImLeader.setAdapter(RecyclerAdapter_ImLeader);
    }

    /***********************************최근행사 초기설정****************************************/
    private void initRecentEvents(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            selectedFragment = FirstMyGroupsFragment.newInstance();
            Bundle bundle = new Bundle();
            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.vpMyGroups, selectedFragment).commit();
        }
    }



    /************************************더미 데이터 생성*****************************************/
    private void makeDummy() {
        editProfile = (ImageView)findViewById(R.id.editProfile) ;

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,EditProfileActivity.class);
                intent.putExtra("name",profileName.getText() );
                Log.d("phone_before",""+phone);
                intent.putExtra("phone",phone);
                intent.putExtra("email",profileEmail.getText() );
                //사진도 보내야함!
                startActivity(intent);
                finish();
            }
        });

        //최근행사, 내 모임 생성
        events = new ArrayList<>();
        groups = new ArrayList<>();
        list_participatein = new ArrayList<>();
        List_ImCap = new ArrayList<>();
        List_ImLeader = new ArrayList<>();
    }

    public ArrayList<Group> getData() {
        return groups;
    }


    /******************************************최근행사 어뎁터*****************************************/
    public class PageAdapterRecentEvents extends PagerAdapter {

        ArrayList<Event> events;

        public PageAdapterRecentEvents(ArrayList<Event> events) {
            this.events = events;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_recent_event_home, container, false);

            Event event = events.get(position);

            //findView
            pageId = (LinearLayout)view.findViewById(R.id.pageId);
            ivEvent = (ImageView) view.findViewById(R.id.ivEvent);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            tvEventName = (TextView) view.findViewById(R.id.tvEventName);
            tvEventIntro = (TextView) view.findViewById(R.id.tvEventIntro);
            tvEventPlace = (TextView) view.findViewById(R.id.tvEventPlace);


            //화면 배치
            Glide.with(HomeActivity.this)
                    .load(event.photo)
                    .into(ivEvent);
            Log.d("eventid",""+event.id);
            view.setTag(event.id);
            tvGroupName.setText(event.group_title);
            tvEventName.setText(event.event_title);
            tvEventIntro.setText(event.text);

            //클릭하면 상세로 이동
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HomeActivity.this, EventDetailActivity.class);

                    intent.putExtra("event_id", Integer.parseInt(view.getTag().toString()));
                    startActivity(intent);
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return events.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }


    /***********************************내 모임 Adapter**********************************/

    public class PageAdapterMyGroups extends FragmentStatePagerAdapter implements Serializable {

        ArrayList<Group> groups;

        public PageAdapterMyGroups(FragmentManager fm, ArrayList<Group> groups) {
            super(fm);
            this.groups = groups;
        }


        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FirstMyGroupsFragment firstMyGroupsFragment = new FirstMyGroupsFragment();
                    return firstMyGroupsFragment;
                case 1:
                    SecondMyGroupsFragment secondMyGroupsFragment = new SecondMyGroupsFragment();
                    return secondMyGroupsFragment;
                case 2:
                    ThirdMyGroupsFragment thirdMyGroupsFragment = new ThirdMyGroupsFragment();
                    return thirdMyGroupsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return (groups.size() / 6) + 1;
        }
    }


    /*****************************************더보기*********************************************/
    private void viewMoreEvents() {
        LinearLayout llviewMoreTxt = (LinearLayout) findViewById(R.id.llviewMoreTxt);

        llviewMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecentEventsActivity.class);
                startActivity(intent);
            }
        });
    }


    /*********************************back 두번 누렀을 경우********************************/
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;


        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /***********************************내 행사 Adapter**********************************/
    class MyEventRecyclerAdapter extends RecyclerView.Adapter<MyEventViewHolder> {

        ArrayList<Participate_in> eventLists;

        public MyEventRecyclerAdapter(ArrayList<Participate_in> eventLists) {
            this.eventLists = eventLists;
        }

        public void setAdapter(ArrayList<Participate_in> eventLists) {
            this.eventLists = eventLists;
            notifyDataSetChanged();
        }

        @Override
        public MyEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_event, parent, false);

            MyEventViewHolder viewHolder = new MyEventViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final MyEventViewHolder holder, int position) {
            Participate_in eventList = eventLists.get(position);

            holder.itemView.setTag(eventList.id);

            holder.tvEventName.setText(eventList.group_title);
            holder.tvEventIntro.setText(eventList.event_title);
            holder.tvDday.setText(eventList.start_date);
            if(eventList.deposit_status==0) {
                holder.tvMoneyCheck.setText("미입금");
            }
            else{
                holder.tvMoneyCheck.setText("입금");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, EventDetailActivity.class);
                    intent.putExtra("event_id",Integer.parseInt(holder.itemView.getTag().toString()));
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventLists.size();
        }
    }


    /***********************************나 총무 Adapter**********************************/
    class ImCaptainRecyclerAdapter extends RecyclerView.Adapter<ImCapViewHolder> {

        ArrayList<ImCaptian> eventLists;

        public ImCaptainRecyclerAdapter(ArrayList<ImCaptian> eventLists) {
            this.eventLists = eventLists;
        }

        public void setAdapter(ArrayList<ImCaptian> eventLists) {
            this.eventLists = eventLists;
            notifyDataSetChanged();
        }

        @Override
        public ImCapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_im_captain, parent, false);
            ImCapViewHolder viewHolder = new ImCapViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ImCapViewHolder holder, int position) {
            ImCaptian eventList = eventLists.get(position);

            holder.itemView.setTag(eventList.id);

            holder.tvEventName.setText(eventList.group_title);
            holder.tvEventIntro.setText(eventList.event_title);
            holder.tvDday.setText(eventList.start_date);
            holder.tvMoneyGiverNumber.setText(Integer.toString(eventList.count));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, EventDetailActivity.class);
                    intent.putExtra("event_id",Integer.parseInt(holder.itemView.getTag().toString()));
                    startActivity(intent);
                    finish();
                }
            });
        }


        @Override
        public int getItemCount() {
            return eventLists.size();
        }
    }


    /***********************************나 모임장 Adapter**********************************/
    class ImLeaderRecyclerAdapter extends RecyclerView.Adapter<ImLeaderViewHolder> {

        ArrayList<ImLeader> eventLists;

        public ImLeaderRecyclerAdapter(ArrayList<ImLeader> eventLists) {
            this.eventLists = eventLists;
        }

        public void setAdapter(ArrayList<ImLeader> eventLists) {
            this.eventLists = eventLists;
            notifyDataSetChanged();
        }

        @Override
        public ImLeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_im_leader, parent, false);
            ImLeaderViewHolder viewHolder = new ImLeaderViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ImLeaderViewHolder holder, int position) {
            ImLeader eventList = eventLists.get(position);

            holder.itemView.setTag(eventList);


            Glide.with(HomeActivity.this)
                    .load(eventList.photo)
                    .into(holder.ivImg);
            holder.tvName.setText(eventList.title);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return eventLists.size();
        }
    }


    /**********************************내 행사 ViewHolder********************************/
    class MyEventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName;
        TextView tvEventIntro;
        TextView tvDday;
        TextView tvMoneyCheck;


        public MyEventViewHolder(View itemView) {
            super(itemView);

            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventIntro = (TextView) itemView.findViewById(R.id.tvEventIntro);
            tvDday = (TextView) itemView.findViewById(R.id.tvDday);
            tvMoneyCheck = (TextView) itemView.findViewById(R.id.tvMoneyCheck);
        }


    }

    /**********************************나 총무 ViewHolder********************************/
    class ImCapViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName;
        TextView tvEventIntro;
        TextView tvDday;
        TextView tvMoneyGiverNumber;


        public ImCapViewHolder(View itemView) {
            super(itemView);

            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventIntro = (TextView) itemView.findViewById(R.id.tvEventIntro);
            tvDday = (TextView) itemView.findViewById(R.id.tvDday);
            tvMoneyGiverNumber = (TextView) itemView.findViewById(R.id.tvNonGiverNumber);
        }

    }

    /**********************************나 모임장 ViewHolder********************************/
    class ImLeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        TextView tvName;


        public ImLeaderViewHolder(View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }


}

