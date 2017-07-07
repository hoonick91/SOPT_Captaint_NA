package org.sopt.captainna.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.sopt.captainna.R;
import org.sopt.captainna.model.LoginResult;
import org.sopt.captainna.model.loginInfo;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.R.id.etPwdCheck;
import static org.sopt.captainna.R.id.etPwdSetting;
import static org.sopt.captainna.activity.CommonVariable.token;

public class LoginActivity extends AppCompatActivity {
    //네트워킹
    NetworkService service;

    private EditText login_email;
    private EditText login_password;
    private ImageView join_button;
    private  ImageView login_button;
    RelativeLayout login_view;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        login_view = (RelativeLayout)findViewById(R.id.login_view);


        //네트워크초기화
        initNetwork();

        //findview
        findview();

        //리스너 연결하기
        clickListner();

        login_view.setOnClickListener(myClickListener);


    }


    View.OnClickListener myClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            hideKeyboard();
            switch (v.getId())
            {
                case R.id.login_view :
                    break;


            }
        }
    };
    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }
    /****************************************뷰 연결하기*****************************************/
    private void findview(){
        login_email = (EditText)findViewById(R.id.login_email);
        login_password = (EditText)findViewById(R.id.login_password);
        join_button = (ImageView)findViewById(R.id.join_button);
        login_button = (ImageView)findViewById(R.id.login_button);

    }
    /********************************버튼 클릭시 동작 설정*****************************************/
    private void clickListner(){
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /********************************서버 연동*****************************************/
                loginInfo login = new loginInfo(login_email.getText().toString(),login_password.getText().toString());


                Call<LoginResult> request = service.login(login);
                request.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {
                            token = response.body().token;
                            Toast.makeText(LoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(response.code() == 402){
                            Toast.makeText(LoginActivity.this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });

            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(login_email.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
    }


}
