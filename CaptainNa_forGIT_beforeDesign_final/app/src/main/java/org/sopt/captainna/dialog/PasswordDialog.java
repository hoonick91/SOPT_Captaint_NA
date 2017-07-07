package org.sopt.captainna.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.captainna.R;
import org.sopt.captainna.model.JoinGroupResult;
import org.sopt.captainna.model.SendPassword;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.token;

/**
 * Created by minjeong on 2017-06-29.
 */

public class PasswordDialog extends Dialog {

    private EditText inputPassword;
    private LinearLayout cancel;
    private LinearLayout submit;
    private ImageView lockImage ;
    private TextView ok;

    //서버에서 받아온 패스워드 저장용
    private String password;
    private int group_id;

    //네트워킹
    NetworkService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.7f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_password);

        findView(); // id연결


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lockImage.setImageResource(R.drawable.group_password_popup_lock2);
                ok.setTextColor(0xFF638CA5);
            }
        };


        //네트워크초기화
        initNetwork();

        clickListener();//클릭리스너 연결

        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());


    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }


    public PasswordDialog(Context context,String tag) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        group_id = Integer.parseInt(tag);
        Log.d("tag",tag);
    }

    private void pushPassword(){
    //    RequestBody pw =  RequestBody.create(MediaType.parse("multipart/x-www-form-urlencoded"), inputPassword.getText().toString());
        //****************************************서버에 정보 보냄**************************************//*
        SendPassword sendPassword = new SendPassword(inputPassword.getText().toString());
      //  sendPassword.password = inputPassword.getText().toString();
      //  String pw = inputPassword.getText().toString();

        Call<JoinGroupResult> request = service.joinGroup(token, group_id, sendPassword);
        request.enqueue(new Callback<JoinGroupResult>() {
            @Override
            public void onResponse(Call<JoinGroupResult> call, Response<JoinGroupResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.equals("add group success")) {
                        Toast.makeText(getContext(), "모임 가입 완료", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if(response.code()==406) {
                        Toast.makeText(getContext(), "이미 참여한 모임입니다.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    else if (response.code()==401){
                        Toast.makeText(getContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "존재하지 않는 그룹입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinGroupResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });

    }

    private void clickListener(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushPassword(); //서버에 패스워드 보내기
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    private void findView() {
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        cancel = (LinearLayout) findViewById(R.id.cancel);
        submit = (LinearLayout) findViewById(R.id.submit);
        lockImage = (ImageView) findViewById(R.id.lockImage);
        ok = (TextView) findViewById(R.id.ok);
    }

}
