package org.sopt.captainna.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.sopt.captainna.R;
import org.sopt.captainna.fragment.GiverFragment;
import org.sopt.captainna.fragment.NonGiverFragment;
import org.sopt.captainna.model.ParticipateList;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.givercount;
import static org.sopt.captainna.activity.CommonVariable.nongivercount;
import static org.sopt.captainna.activity.CommonVariable.token;



public class EventJoinerActivity extends AppCompatActivity {


    private int event_id;

    //네트워킹
    NetworkService service;

    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private Fragment selectedFragment;
    private Fragment nonSelectedFragment;
    private int givercnt = 0;
    private int nongivercnt = 0;

    //혜주
    private LinearLayout canclebtn;
    private LinearLayout backbtn;
    private LinearLayout deletebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_joiner);

        //event_id값 받아오기
        getEventID();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //버전 체크

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                //권한 없음 테드 퍼미션으로 권한 부여

                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(getApplicationContext(), "권한 허가", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                new TedPermission(this)
                        .setPermissionListener(permissionListener)
          //              .setRationaleMessage("주소록 접근 권한이 필요해요")
                        .setDeniedMessage("사용을 위해선 권한 허용이 필요합니다.\n [설정] > [권한] 에서 권한을 허용할 수 있어요!")
                        .setPermissions(Manifest.permission.READ_CONTACTS)
                        .check();

            } else {
                //권한 있음

            }
        }

        initNetwork();


       Call<ParticipateList> request = service.getParticipateList(token,event_id);
        request.enqueue(new Callback<ParticipateList>() {
            @Override
            public void onResponse(Call<ParticipateList> call, Response<ParticipateList> response) {
                if (response.isSuccessful()) {
                    givercount = response.body().paid_count;
                    nongivercount = response.body().unpaid_count;

                    if(tabLayout.getTabCount() != 0) {
                        tabLayout.getTabAt(0).setText("입금자 " + givercount);
                        tabLayout.getTabAt(1).setText("미입금자 " + nongivercount);
                    }

                }

            }

            @Override
            public void onFailure(Call<ParticipateList> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });

        //탭 레이아웃 설정
        //first page 처음에 보이도록
        if (savedInstanceState == null) {
            Log.d("qqqqq", "0000");
            selectedFragment = GiverFragment.newInstance(event_id);
            nonSelectedFragment = NonGiverFragment.newInstance(event_id);
            getSupportFragmentManager().beginTransaction().replace(R.id.flcontainter, selectedFragment).commit();

        }

        //텝 레이아웃 설정
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("입금자 " + givercount));
        tabLayout.addTab(tabLayout.newTab().setText("미입금자 " + nongivercount));

        //페이지 어뎁터 설정, 뷰페이저와 연동
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), 2);


        //탭 클릭 시에 이벤트 부여
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        selectedFragment = GiverFragment.newInstance(event_id);
                //        givercnt = selectedFragment.getArguments().getInt("giverCNT");

                        tabLayout.getTabAt(0).setText("입금자 " + givercount);
                        break;
                    case 1:
                        selectedFragment = NonGiverFragment.newInstance(event_id);
                     //   nongivercnt = selectedFragment.getArguments().getInt("nongiverCNT");
                        tabLayout.getTabAt(1).setText("미입금자 " + nongivercount);
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                selectedFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flcontainter, selectedFragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        //혜주주
       canclebtn = (LinearLayout) findViewById(R.id.cancelbtn);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventJoinerActivity.this, EventDetailActivity.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
                finish();
            }
        });

        backbtn = (LinearLayout) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventJoinerActivity.this, EventJoinerActivity.class);
                intent.putExtra("event_id", event_id);
                startActivity(intent);
                finish(); //뒤로가기 버튼 누르면 원래의 EventJoinerActivity 띄우기..?
            }
        });

        deletebtn = (LinearLayout) findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "구현예정", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /**********************************이벤트 아이디 받기*******************************************/
    private void getEventID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        event_id = intent.getExtras().getInt("event_id");

    }


    /***********************************페이지 어뎁터****************************************/
    public class PageAdapter extends FragmentStatePagerAdapter {
        int tabcount;

        public PageAdapter(FragmentManager fm, int tabcount) {
            super(fm);
            this.tabcount = tabcount;

        }

        //각 페이지의 포지션마다 어떠한 프레그먼트를 보여줄 것 인지
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new GiverFragment(event_id);
                case 1:
                    return new NonGiverFragment(event_id);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }           //보여질 페이지의 수 tab사용시 tabcount 리턴
    }


}
