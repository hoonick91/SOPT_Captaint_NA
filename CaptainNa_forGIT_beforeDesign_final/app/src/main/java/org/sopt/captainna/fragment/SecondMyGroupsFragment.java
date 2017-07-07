package org.sopt.captainna.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by KoJunHee on 20117-066-29.
 */

public class SecondMyGroupsFragment extends Fragment {

    private ImageView ivGroupImg011, ivGroupImg022, ivGroupImg033, ivGroupImg044, ivGroupImg055, ivGroupImg066;
    private TextView tvGroupName011, tvGroupName022, tvGroupName033, tvGroupName044, tvGroupName055, tvGroupName066;
    private ImageView ivRed011,ivRed022, ivRed033, ivRed044, ivRed055, ivRed066;
    private RelativeLayout item011, item022, item033, item044, item055, item066;
    private RelativeLayout rlPlus011, rlPlus022, rlPlus033, rlPlus044, rlPlus055, rlPlus066;
    private View view;
    private static ArrayList<Group> groups;

    //dialog
    private GroupDialog dialog;
    private int group_id;


    /******************************************빈 생성자***************************************/
    public SecondMyGroupsFragment() {
    }

    /**************************************onnCreateView***************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_second_my_groups, container, false);


        //findView
        findView();

        //화면 생성
        makeDisplay();

        return view;
    }


    /************************************화면에 뿌려주기********************************************/
    private void makeDisplay() {
        if(groups.size()==6){

            //플러스 버튼
            rlPlus011.setVisibility(View.VISIBLE);
            rlPlus011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==7){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus022.setVisibility(View.VISIBLE);
            rlPlus022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==8){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            item022.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(7).photo)
                    .into(ivGroupImg022);
            tvGroupName022.setText(groups.get(7).title);
            item022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(7).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus033.setVisibility(View.VISIBLE);
            rlPlus033.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==9){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            item022.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(7).photo)
                    .into(ivGroupImg022);
            tvGroupName022.setText(groups.get(7).title);
            item022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(7).id);
                    startActivity(intent);
                }
            });

            item033.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(8).photo)
                    .into(ivGroupImg033);
            tvGroupName033.setText(groups.get(8).title);
            item033.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(8).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus044.setVisibility(View.VISIBLE);
            rlPlus044.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==10){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            item022.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(7).photo)
                    .into(ivGroupImg022);
            tvGroupName022.setText(groups.get(7).title);
            item022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(7).id);
                    startActivity(intent);
                }
            });

            item033.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(8).photo)
                    .into(ivGroupImg033);
            tvGroupName033.setText(groups.get(8).title);
            item033.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(8).id);
                    startActivity(intent);
                }
            });

            item044.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(9).photo)
                    .into(ivGroupImg044);
            tvGroupName044.setText(groups.get(9).title);
            item044.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(9).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus055.setVisibility(View.VISIBLE);
            rlPlus055.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==11){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            item022.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(7).photo)
                    .into(ivGroupImg022);
            tvGroupName022.setText(groups.get(7).title);
            item022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(7).id);
                    startActivity(intent);
                }
            });

            item033.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(8).photo)
                    .into(ivGroupImg033);
            tvGroupName033.setText(groups.get(8).title);
            item033.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(8).id);
                    startActivity(intent);
                }
            });

            item044.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(9).photo)
                    .into(ivGroupImg044);
            tvGroupName044.setText(groups.get(9).title);
            item044.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(9).id);
                    startActivity(intent);
                }
            });


            item055.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(10).photo)
                    .into(ivGroupImg044);
            tvGroupName055.setText(groups.get(10).title);
            item055.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(10).id);
                    startActivity(intent);
                }
            });

            rlPlus066.setVisibility(View.VISIBLE);
            rlPlus066.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });
        }if(groups.size()>=12){

            //아이템들
            item011.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(6).photo)
                    .into(ivGroupImg011);
            tvGroupName011.setText(groups.get(6).title);
            item011.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(6).id);
                    startActivity(intent);
                }
            });

            item022.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(7).photo)
                    .into(ivGroupImg022);
            tvGroupName022.setText(groups.get(7).title);
            item022.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(7).id);
                    startActivity(intent);
                }
            });

            item033.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(8).photo)
                    .into(ivGroupImg033);
            tvGroupName033.setText(groups.get(8).title);
            item033.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(8).id);
                    startActivity(intent);
                }
            });

            item044.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(9).photo)
                    .into(ivGroupImg044);
            tvGroupName044.setText(groups.get(9).title);
            item044.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(9).id);
                    startActivity(intent);
                }
            });


            item055.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(10).photo)
                    .into(ivGroupImg044);
            tvGroupName055.setText(groups.get(10).title);
            item055.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(10).id);
                    startActivity(intent);
                }
            });

            item066.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(11).photo)
                    .into(ivGroupImg044);
            tvGroupName066.setText(groups.get(11).title);
            item066.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(11).id);
                    startActivity(intent);
                }
            });

        }
    }


    /********************************내 그룹 리스트 받기*******************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( getActivity() != null && getActivity() instanceof HomeActivity){
            groups = ((HomeActivity)getActivity()).getData();
        }
    }

    /***************************************8findView*********************************************/
    private void findView() {

        //그룹 이미지
        ivGroupImg011 = (ImageView) view.findViewById(R.id.ivGroupImg011);
        ivGroupImg022 = (ImageView) view.findViewById(R.id.ivGroupImg022);
        ivGroupImg033 = (ImageView) view.findViewById(R.id.ivGroupImg033);
        ivGroupImg044 = (ImageView) view.findViewById(R.id.ivGroupImg044);
        ivGroupImg055 = (ImageView) view.findViewById(R.id.ivGroupImg055);
        ivGroupImg066 = (ImageView) view.findViewById(R.id.ivGroupImg066);

        //그룹명
        tvGroupName011 = (TextView) view.findViewById(R.id.tvGroupName011);
        tvGroupName022 = (TextView) view.findViewById(R.id.tvGroupName022);
        tvGroupName033 = (TextView) view.findViewById(R.id.tvGroupName033);
        tvGroupName044 = (TextView) view.findViewById(R.id.tvGroupName044);
        tvGroupName055 = (TextView) view.findViewById(R.id.tvGroupName055);
        tvGroupName066 = (TextView) view.findViewById(R.id.tvGroupName066);

        //빨간점
        ivRed011 = (ImageView) view.findViewById(R.id.ivRed011);
        ivRed022 = (ImageView) view.findViewById(R.id.ivRed022);
        ivRed033 = (ImageView) view.findViewById(R.id.ivRed033);
        ivRed044 = (ImageView) view.findViewById(R.id.ivRed044);
        ivRed055 = (ImageView) view.findViewById(R.id.ivRed055);
        ivRed066 = (ImageView) view.findViewById(R.id.ivRed066);

        //아이템
        item011 = (RelativeLayout) view.findViewById(R.id.item011);
        item022 = (RelativeLayout) view.findViewById(R.id.item022);
        item033 = (RelativeLayout) view.findViewById(R.id.item033);
        item044 = (RelativeLayout) view.findViewById(R.id.item044);
        item055 = (RelativeLayout) view.findViewById(R.id.item055);
        item066 = (RelativeLayout) view.findViewById(R.id.item066);

        //추가 버튼
        rlPlus011 = (RelativeLayout) view.findViewById(R.id.rlPlus011);
        rlPlus022 = (RelativeLayout) view.findViewById(R.id.rlPlus022);
        rlPlus033 = (RelativeLayout) view.findViewById(R.id.rlPlus033);
        rlPlus044 = (RelativeLayout) view.findViewById(R.id.rlPlus044);
        rlPlus055 = (RelativeLayout) view.findViewById(R.id.rlPlus055);
        rlPlus066 = (RelativeLayout) view.findViewById(R.id.rlPlus066);
    }

}
