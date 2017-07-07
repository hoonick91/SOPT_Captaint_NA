package org.sopt.captainna.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sopt.captainna.R;
import org.sopt.captainna.activity.HomeActivity;
import org.sopt.captainna.activity.ListActivity;
import org.sopt.captainna.activity.MakeGroupActivity;
import org.sopt.captainna.dialog.GroupDialog;
import org.sopt.captainna.model.Group;
import org.sopt.captainna.model.MyGroup_Home;

import java.util.ArrayList;


/**
 * Created by KoJunHee on 2017-06-29.
 */

public class FirstMyGroupsFragment extends Fragment {

    private ImageView ivGroupImg01, ivGroupImg02, ivGroupImg03, ivGroupImg04, ivGroupImg05, ivGroupImg06;
    private TextView tvGroupName01, tvGroupName02, tvGroupName03, tvGroupName04, tvGroupName05, tvGroupName06;
    private ImageView ivRed01, ivRed02, ivRed03, ivRed04, ivRed05, ivRed06;
    private RelativeLayout item01, item02, item03, item04, item05, item06;
    private RelativeLayout rlPlus01, rlPlus02, rlPlus03, rlPlus04, rlPlus05, rlPlus06;
    private View view;
    private static ArrayList<Group> groups;
    private GroupDialog dialog;

    /******************************************빈 생성자***************************************/
    public FirstMyGroupsFragment() {
    }


    /**************************************프레그먼트 초기화***************************************/
    public static Fragment newInstance() {
        Fragment FirstMyGroupsFragment = new FirstMyGroupsFragment();
        Bundle args = new Bundle();
        FirstMyGroupsFragment.setArguments(args);
        return FirstMyGroupsFragment;
    }

    /**************************************onnCreateView***************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_first_my_groups, container, false);

        //findView
        findView();

        //화면 생성
        makeDisplay();

        return view;
    }


    /************************************화면에 뿌려주기********************************************/
    private void makeDisplay() {
        if(groups.size()==0){

            //플러스 버튼
            rlPlus01.setVisibility(View.VISIBLE);
            rlPlus01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==1){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus02.setVisibility(View.VISIBLE);
            rlPlus02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==2){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            item02.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(1).photo)
                    .into(ivGroupImg02);
            tvGroupName02.setText(groups.get(1).title);
            item02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(1).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus03.setVisibility(View.VISIBLE);
            rlPlus03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==3){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            item02.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(1).photo)
                    .into(ivGroupImg02);
            tvGroupName02.setText(groups.get(1).title);
            item02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(1).id);
                    startActivity(intent);
                }
            });

            item03.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(2).photo)
                    .into(ivGroupImg03);
            tvGroupName03.setText(groups.get(2).title);
            item03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(2).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus04.setVisibility(View.VISIBLE);
            rlPlus04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==4){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            item02.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(1).photo)
                    .into(ivGroupImg02);
            tvGroupName02.setText(groups.get(1).title);
            item02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(1).id);
                    startActivity(intent);
                }
            });

            item03.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(2).photo)
                    .into(ivGroupImg03);
            tvGroupName03.setText(groups.get(2).title);
            item03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(2).id);
                    startActivity(intent);
                }
            });

            item04.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(3).photo)
                    .into(ivGroupImg04);
            tvGroupName04.setText(groups.get(3).title);
            item04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(3).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus05.setVisibility(View.VISIBLE);
            rlPlus05.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==5){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            item02.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(1).photo)
                    .into(ivGroupImg02);
            tvGroupName02.setText(groups.get(1).title);
            item02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(1).id);
                    startActivity(intent);
                }
            });

            item03.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(2).photo)
                    .into(ivGroupImg03);
            tvGroupName03.setText(groups.get(2).title);
            item03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(2).id);
                    startActivity(intent);
                }
            });

            item04.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(3).photo)
                    .into(ivGroupImg04);
            tvGroupName04.setText(groups.get(3).title);
            item04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(3).id);
                    startActivity(intent);
                }
            });


            item05.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(4).photo)
                    .into(ivGroupImg04);
            tvGroupName05.setText(groups.get(4).title);
            item05.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(4).id);
                    startActivity(intent);
                }
            });

            rlPlus06.setVisibility(View.VISIBLE);
            rlPlus06.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });
        }if(groups.size()>=6){

            //아이템들
            item01.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(0).photo)
                    .into(ivGroupImg01);
            tvGroupName01.setText(groups.get(0).title);
            item01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(0).id);
                    startActivity(intent);
                }
            });

            item02.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(1).photo)
                    .into(ivGroupImg02);
            tvGroupName02.setText(groups.get(1).title);
            item02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(1).id);
                    startActivity(intent);
                }
            });

            item03.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(2).photo)
                    .into(ivGroupImg03);
            tvGroupName03.setText(groups.get(2).title);
            item03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(2).id);
                    startActivity(intent);
                }
            });

            item04.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(3).photo)
                    .into(ivGroupImg04);
            tvGroupName04.setText(groups.get(3).title);
            item04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(3).id);
                    startActivity(intent);
                }
            });


            item05.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(4).photo)
                    .into(ivGroupImg05);
            tvGroupName05.setText(groups.get(4).title);
            item05.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(4).id);
                    startActivity(intent);
                }
            });

            item06.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(5).photo)
                    .into(ivGroupImg06);
            tvGroupName06.setText(groups.get(5).title);
            item06.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(5).id);
                    startActivity(intent);
                }
            });

        }
    }

    /*******************************내 그룹 리스트 받기******************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof HomeActivity) {
            groups = ((HomeActivity) getActivity()).getData();
        }
    }



    /***************************************8findView*********************************************/
    private void findView() {

        //그룹 이미지
        ivGroupImg01 = (ImageView) view.findViewById(R.id.ivGroupImg01);
        ivGroupImg02 = (ImageView) view.findViewById(R.id.ivGroupImg02);
        ivGroupImg03 = (ImageView) view.findViewById(R.id.ivGroupImg03);
        ivGroupImg04 = (ImageView) view.findViewById(R.id.ivGroupImg04);
        ivGroupImg05 = (ImageView) view.findViewById(R.id.ivGroupImg05);
        ivGroupImg06 = (ImageView) view.findViewById(R.id.ivGroupImg06);

        //그룹명
        tvGroupName01 = (TextView) view.findViewById(R.id.tvGroupName01);
        tvGroupName02 = (TextView) view.findViewById(R.id.tvGroupName02);
        tvGroupName03 = (TextView) view.findViewById(R.id.tvGroupName03);
        tvGroupName04 = (TextView) view.findViewById(R.id.tvGroupName04);
        tvGroupName05 = (TextView) view.findViewById(R.id.tvGroupName05);
        tvGroupName06 = (TextView) view.findViewById(R.id.tvGroupName06);

        //아이템
        item01 = (RelativeLayout) view.findViewById(R.id.item01);
        item02 = (RelativeLayout) view.findViewById(R.id.item02);
        item03 = (RelativeLayout) view.findViewById(R.id.item03);
        item04 = (RelativeLayout) view.findViewById(R.id.item04);
        item05 = (RelativeLayout) view.findViewById(R.id.item05);
        item06 = (RelativeLayout) view.findViewById(R.id.item06);

        //추가 버튼
        rlPlus01 = (RelativeLayout) view.findViewById(R.id.rlPlus01);
        rlPlus02 = (RelativeLayout) view.findViewById(R.id.rlPlus02);
        rlPlus03 = (RelativeLayout) view.findViewById(R.id.rlPlus03);
        rlPlus04 = (RelativeLayout) view.findViewById(R.id.rlPlus04);
        rlPlus05 = (RelativeLayout) view.findViewById(R.id.rlPlus05);
        rlPlus06 = (RelativeLayout) view.findViewById(R.id.rlPlus06);
    }

}
