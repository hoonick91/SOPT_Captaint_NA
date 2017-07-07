package org.sopt.captainna.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sopt.captainna.R;
import org.sopt.captainna.activity.HomeActivity;
import org.sopt.captainna.activity.ListActivity;
import org.sopt.captainna.dialog.GroupDialog;
import org.sopt.captainna.model.Group;

import java.util.ArrayList;

import static android.R.attr.x;
import static org.sopt.captainna.R.id.ivGroupImg01;
import static org.sopt.captainna.R.id.ivGroupImg011;
import static org.sopt.captainna.R.id.tvGroupName011;
import static org.sopt.captainna.R.id.tvGroupName04;

/**
 * Created by KoJunHee on 201117-0666-29.
 */

public class ThirdMyGroupsFragment extends Fragment {

    private ImageView ivGroupImg0111, ivGroupImg0222, ivGroupImg0333, ivGroupImg0444, ivGroupImg0555, ivGroupImg0666;
    private TextView tvGroupName0111, tvGroupName0222, tvGroupName0333, tvGroupName0444, tvGroupName0555, tvGroupName0666;
    private ImageView ivRed0111,ivRed0222, ivRed0333, ivRed0444, ivRed0555, ivRed0666;
    private RelativeLayout item0111, item0222, item0333, item0444, item0555, item0666;
    private RelativeLayout rlPlus0111, rlPlus0222, rlPlus0333, rlPlus0444, rlPlus0555, rlPlus0666;
    private View view;
    private static ArrayList<Group> groups;

    //dialog
    private GroupDialog dialog;
    private int group_id;


    /******************************************빈 생성자***************************************/
    public ThirdMyGroupsFragment() {
    }

    /**************************************onnCreateView***************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_third_my_groups, container, false);


        //findView
        findView();

        //화면 생성
        makeDisplay();

        return view;
    }


    /************************************화면에 뿌려주기********************************************/
    private void makeDisplay() {
        if(groups.size()==12){

            //플러스 버튼
            rlPlus0111.setVisibility(View.VISIBLE);
            rlPlus0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });


        }if(groups.size()==13){

            //아이템들
            item0111.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(12).photo)
                    .into(ivGroupImg0111);
            tvGroupName0111.setText(groups.get(12).title);
            item0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(12).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus0222.setVisibility(View.VISIBLE);
            rlPlus0222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==14){

            //아이템들
            item0111.setVisibility(View.VISIBLE);
            item0111.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(12).photo)
                    .into(ivGroupImg0111);
            tvGroupName0111.setText(groups.get(12).title);
            item0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(12).id);
                    startActivity(intent);
                }
            });

            item0222.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(13).photo)
                    .into(ivGroupImg0222);
            tvGroupName0222.setText(groups.get(13).title);
            item0222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(13).id);
                    startActivity(intent);
                }
            });

            //플러스버튼
            rlPlus0333.setVisibility(View.VISIBLE);
            rlPlus0333.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==15){

            //아이템들
            item0111.setVisibility(View.VISIBLE);
            item0111.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(12).photo)
                    .into(ivGroupImg0111);
            tvGroupName0111.setText(groups.get(12).title);
            item0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(12).id);
                    startActivity(intent);
                }
            });

            item0222.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(13).photo)
                    .into(ivGroupImg0222);
            tvGroupName0222.setText(groups.get(13).title);
            item0222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(13).id);
                    startActivity(intent);
                }
            });

            item0333.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(14).photo)
                    .into(ivGroupImg0333);
            tvGroupName0333.setText(groups.get(14).title);
            item0333.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(14).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus0444.setVisibility(View.VISIBLE);
            rlPlus0444.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });
        }if(groups.size()==16){

            //아이템들
            item0111.setVisibility(View.VISIBLE);
            item0111.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(12).photo)
                    .into(ivGroupImg0111);
            tvGroupName0111.setText(groups.get(12).title);
            item0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(12).id);
                    startActivity(intent);
                }
            });

            item0222.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(13).photo)
                    .into(ivGroupImg0222);
            tvGroupName0222.setText(groups.get(13).title);
            item0222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(13).id);
                    startActivity(intent);
                }
            });

            item0333.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(14).photo)
                    .into(ivGroupImg0333);
            tvGroupName0333.setText(groups.get(14).title);
            item0333.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(14).id);
                    startActivity(intent);
                }
            });

            item0444.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(15).photo)
                    .into(ivGroupImg0444);
            tvGroupName0444.setText(groups.get(15).title);
            item0444.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(15).id);
                    startActivity(intent);
                }
            });

            //플러스 버튼
            rlPlus0555.setVisibility(View.VISIBLE);
            rlPlus0555.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
                }
            });

        }if(groups.size()==17){

            //아이템들
            item0111.setVisibility(View.VISIBLE);
            item0111.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(12).photo)
                    .into(ivGroupImg0111);
            tvGroupName0111.setText(groups.get(12).title);
            item0111.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(12).id);
                    startActivity(intent);
                }
            });

            item0222.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(13).photo)
                    .into(ivGroupImg0222);
            tvGroupName0222.setText(groups.get(13).title);
            item0222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(13).id);
                    startActivity(intent);
                }
            });

            item0333.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(14).photo)
                    .into(ivGroupImg0333);
            tvGroupName0333.setText(groups.get(14).title);
            item0333.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(14).id);
                    startActivity(intent);
                }
            });

            item0444.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(15).photo)
                    .into(ivGroupImg0444);
            tvGroupName0444.setText(groups.get(15).title);
            item0444.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(15).id);
                    startActivity(intent);
                }
            });

            item0555.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(groups.get(16).photo)
                    .into(ivGroupImg0555);
            tvGroupName0555.setText(groups.get(16).title);
            item0555.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("group_id",groups.get(16).id);
                    startActivity(intent);
                }
            });
            //플러스 버튼
            rlPlus0666.setVisibility(View.VISIBLE);
            rlPlus0666.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new GroupDialog(getContext());
                    dialog.show();
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
        ivGroupImg0111 = (ImageView) view.findViewById(R.id.ivGroupImg0111);
        ivGroupImg0222 = (ImageView) view.findViewById(R.id.ivGroupImg0222);
        ivGroupImg0333 = (ImageView) view.findViewById(R.id.ivGroupImg0333);
        ivGroupImg0444 = (ImageView) view.findViewById(R.id.ivGroupImg0444);
        ivGroupImg0555 = (ImageView) view.findViewById(R.id.ivGroupImg0555);
        ivGroupImg0666 = (ImageView) view.findViewById(R.id.ivGroupImg0666);

        //그룹명
        tvGroupName0111 = (TextView) view.findViewById(R.id.tvGroupName0111);
        tvGroupName0222 = (TextView) view.findViewById(R.id.tvGroupName0222);
        tvGroupName0333 = (TextView) view.findViewById(R.id.tvGroupName0333);
        tvGroupName0444 = (TextView) view.findViewById(R.id.tvGroupName0444);
        tvGroupName0555 = (TextView) view.findViewById(R.id.tvGroupName0555);
        tvGroupName0666 = (TextView) view.findViewById(R.id.tvGroupName0666);

        //빨간점
        ivRed0111 = (ImageView) view.findViewById(R.id.ivRed0111);
        ivRed0222 = (ImageView) view.findViewById(R.id.ivRed0222);
        ivRed0333 = (ImageView) view.findViewById(R.id.ivRed0333);
        ivRed0444 = (ImageView) view.findViewById(R.id.ivRed0444);
        ivRed0555 = (ImageView) view.findViewById(R.id.ivRed0555);
        ivRed0666 = (ImageView) view.findViewById(R.id.ivRed0666);

        //아이템
        item0111 = (RelativeLayout) view.findViewById(R.id.item0111);
        item0222 = (RelativeLayout) view.findViewById(R.id.item0222);
        item0333 = (RelativeLayout) view.findViewById(R.id.item0333);
        item0444 = (RelativeLayout) view.findViewById(R.id.item0444);
        item0555 = (RelativeLayout) view.findViewById(R.id.item0555);
        item0666 = (RelativeLayout) view.findViewById(R.id.item0666);

        //추가 버튼
        rlPlus0111 = (RelativeLayout) view.findViewById(R.id.rlPlus0111);
        rlPlus0222 = (RelativeLayout) view.findViewById(R.id.rlPlus0222);
        rlPlus0333 = (RelativeLayout) view.findViewById(R.id.rlPlus0333);
        rlPlus0444 = (RelativeLayout) view.findViewById(R.id.rlPlus0444);
        rlPlus0555 = (RelativeLayout) view.findViewById(R.id.rlPlus0555);
        rlPlus0666 = (RelativeLayout) view.findViewById(R.id.rlPlus0666);
    }

}
