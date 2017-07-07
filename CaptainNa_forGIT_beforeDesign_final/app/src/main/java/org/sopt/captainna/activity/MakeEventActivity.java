package org.sopt.captainna.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.dialog.DateDialog;
import org.sopt.captainna.model.MakeEventResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.chooseTime;
import static org.sopt.captainna.activity.CommonVariable.chooseTime2;
import static org.sopt.captainna.activity.CommonVariable.chooseday;
import static org.sopt.captainna.activity.CommonVariable.chooseday2;
import static org.sopt.captainna.activity.CommonVariable.id_name;
import static org.sopt.captainna.activity.CommonVariable.isFrombutton;
import static org.sopt.captainna.activity.CommonVariable.token;

public class
MakeEventActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;


    //view
    private ImageView ivAddImg, ivAddParty, fromWhen, toWhen;
    private EditText etEventName, etEventContents, etEventPlace, etEventMoney,
            etFirstPartyPlace, etFirstPartyMoney, etSecondPartyPlace, etSecondPartyMoney,
            etThirdPartyPlace, etThirdPartyMoney;
    private ImageButton btnRegister;
    private LinearLayout llsecondParty, llthirdParty;
    private TextView choosefromday,choosetoday,choosefromtime,choosetotime;

    //뒤풀이 추가 카운트
    private int partyCnt = 1;

    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri; //갤러리에서 선택한 파일 경로
    String filename; //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로

    //dialog
    public DateDialog dialog;

    private int group_id;
    private String start_day="";
    private String start_time="";
    private String end_day="";
    private String end_time="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        //네트워크초기화
        initNetwork();

        //그룹아이디 얻어오기
        getGroupID();

        //뒤로가기
        goBack();

        //findView
        findView();

        //이벤트 사진 추가
        addImg();

        //행사날짜 추가
        choiceDate();

        //뒤풀이 차수 추가
        addParty();

        //행사 등록하기
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regsiterEvent();
                Intent intent = new Intent(MakeEventActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /*****************************************행사 등록*****************************************/
    private void regsiterEvent() {

        //행사 이름, 행사내용, 행사장소, 행사 금액
        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), etEventName.getText().toString());
        RequestBody text = RequestBody.create(MediaType.parse("multipart/form-data"), etEventContents.getText().toString());
        RequestBody place = RequestBody.create(MediaType.parse("multipart/form-data"), etEventPlace.getText().toString());
        RequestBody amount = RequestBody.create(MediaType.parse("multipart/form-data"), etEventPlace.getText().toString());

        //행사 일시
        String temp[] = choosefromday.getText().toString().split(" ");
        String year[] = temp[0].split("년");
        String month[] = temp[1].split("월");
        String day[] = temp[2].split("일");

        start_day=year[0]+"-"+month[0]+"-"+day[0];

        String temp1[] = choosefromtime.getText().toString().split(" ");
        String hour[] = temp1[1].split("시");
        String minute[] = temp1[2].split("분");


        if(temp1[0].equals("오전")){
            if(hour[0].equals("12")){
                hour[0] = "00";
            }
        }
        else if(temp1[0].equals("오후")){
            if(hour[0].equals("12")){
                //넘기기
            }
            else{
                int changeTime = Integer.parseInt(hour[0])+12;
                hour[0] = Integer.toString(changeTime);
            }
        }
        start_time=hour[0]+":"+minute[0];

        RequestBody start_date = RequestBody.create(MediaType.parse("multipart/form-data"),  start_day+" "+ start_time);

        String temp2[] = choosefromday.getText().toString().split(" ");
        String year2[] = temp2[0].split("년");
        String month2[] = temp2[1].split("월");
        String day2[] = temp2[2].split("일");

        end_day=year2[0]+"-"+month2[0]+"-"+day2[0];

        String temp3[] = choosefromtime.getText().toString().split(" ");
        String hour2[] = temp3[1].split("시");
        String minute2[] = temp3[2].split("분");
        if(temp3[0].equals("오전")){
            if(hour2[0].equals("12")){
                hour2[0] = "00";
            }
        }
        else if(temp1[0].equals("오후")){
            if(hour2[0].equals("12")){
                //넘기기
            }
            else{
                hour2[0] = Integer.toString(Integer.parseInt(hour[0])+12);
            }
        }
        end_time=hour[0]+":"+minute[0];


        RequestBody end_date = RequestBody.create(MediaType.parse("multipart/form-data"), end_day+" "+ end_time);

        RequestBody manager_name = RequestBody.create(MediaType.parse("multipart/form-data"), id_name);
        //차후 행사
        RequestBody place_1st = RequestBody.create(MediaType.parse("multipart/form-data"), etFirstPartyPlace.getText().toString());
        RequestBody amount_1st = RequestBody.create(MediaType.parse("multipart/form-data"), etFirstPartyMoney.getText().toString());

        RequestBody place_2nd = RequestBody.create(MediaType.parse("multipart/form-data"), etSecondPartyPlace.getText().toString());
        RequestBody amount_2nd = RequestBody.create(MediaType.parse("multipart/form-data"), etSecondPartyMoney.getText().toString());

        RequestBody place_3rd = RequestBody.create(MediaType.parse("multipart/form-data"), etThirdPartyPlace.getText().toString());
        RequestBody amount_3rd = RequestBody.create(MediaType.parse("multipart/form-data"), etThirdPartyMoney.getText().toString());


        /****************************************서버에 정보 보냄**************************************/

        Call<MakeEventResult> request = service.registerEvent(token, group_id, title, text, place, amount, start_date, end_date,manager_name,
                place_1st, amount_1st, place_2nd, amount_2nd, place_3rd, amount_3rd, null);
        request.enqueue(new Callback<MakeEventResult>() {
            @Override
            public void onResponse(Call<MakeEventResult> call, Response<MakeEventResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.equals("add group success")) {
                        Toast.makeText(MakeEventActivity.this, "행사 등록 완료", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MakeEventResult> call, Throwable t) {

            }
        });
    }
    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.cancelMakeEvent);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeEventActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /*************************************뒤풀이 차수 추가*************************************/
    private void addParty() {
        ivAddParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partyCnt == 1) {
                    llsecondParty.setVisibility(View.VISIBLE);
                    partyCnt++;
                } else if (partyCnt == 2) {
                    llthirdParty.setVisibility(View.VISIBLE);
                    partyCnt++;
                } else {
                    Toast.makeText(MakeEventActivity.this, "3차까지 가능합니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*************************************이벤트 부분 사진 추가**************************************/
    private void addImg() {
        ivAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);


            }
        });

    }

    /********************************** 행사 날짜 선택****************************************/
    private void choiceDate() {

        Calendar cal = Calendar.getInstance();

        String afternoon = "오전";
        int hour= 0;

        if (cal.get(Calendar.HOUR_OF_DAY)>=12) { //pm일때
            afternoon = "오후";
            if(cal.get(Calendar.HOUR_OF_DAY) > 12) //13시~23시
                hour = cal.get(Calendar.HOUR_OF_DAY)-12;
        }
        else{
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }

        choosefromday.setText(cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DAY_OF_MONTH)+"일");
        choosefromtime.setText(afternoon +" " + hour+"시 "+cal.get(Calendar.MINUTE)+"분");
        choosetoday.setText(cal.get(Calendar.YEAR)+"년 "+(cal.get(Calendar.MONTH)+1)+"월 "+cal.get(Calendar.DAY_OF_MONTH)+"일");
        choosetotime.setText(afternoon +" " + hour+"시 "+cal.get(Calendar.MINUTE)+"분");


        dialog = new DateDialog(MakeEventActivity.this);
        fromWhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseday = choosefromday.getText().toString();
                chooseTime = choosefromtime.getText().toString();
                chooseday2 = choosetoday.getText().toString();
                chooseTime2 = choosetotime.getText().toString();
                isFrombutton = true;
                dialog.show();


            }
        });

        toWhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFrombutton = false;
                chooseday = choosefromday.getText().toString();
                chooseTime = choosefromtime.getText().toString();
                chooseday2 = choosetoday.getText().toString();
                chooseTime2 = choosetotime.getText().toString();
                dialog.show();
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(isFrombutton){
                choosefromday.setText(chooseday);
                choosefromtime.setText(chooseTime);
                }
                else{
                    choosetoday.setText(chooseday2);
                    choosetotime.setText(chooseTime2);
                }
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog2) {

                if (isFrombutton) {
                    dialog.datePickerTheme.updateDate(dialog.year_from, dialog.month_from, dialog.day_from);
                    dialog.timePickerTheme.setCurrentHour(dialog.hour_from);
                    dialog.timePickerTheme.setCurrentMinute(dialog.min_from);
                } else {
                    dialog.datePickerTheme.updateDate(dialog.year_to, dialog.month_to, dialog.day_to);
                    dialog.timePickerTheme.setCurrentHour(dialog.hour_to);
                    dialog.timePickerTheme.setCurrentMinute(dialog.min_to);
                }
            }
        });

    }

    /*****************************************findView*****************************************/
    private void findView() {
        ivAddImg = (ImageView) findViewById(R.id.ivAddImg);
        ivAddParty = (ImageView) findViewById(R.id.ivAddParty);
        fromWhen = (ImageView) findViewById(R.id.fromWhen);
        toWhen = (ImageView) findViewById(R.id.toWhen);
        etEventName = (EditText) findViewById(R.id.etEventName);
        etEventContents = (EditText) findViewById(R.id.etEventContents);
        etEventPlace = (EditText) findViewById(R.id.etEventPlace);
        etEventMoney = (EditText) findViewById(R.id.etEventMoney);
        etFirstPartyPlace = (EditText) findViewById(R.id.etFirstPartyPlace);
        etFirstPartyMoney = (EditText) findViewById(R.id.etFirstPartyMoney);
        etSecondPartyPlace = (EditText) findViewById(R.id.etSecondPartyPlace);
        etSecondPartyMoney = (EditText) findViewById(R.id.etSecondPartyMoney);
        etThirdPartyPlace = (EditText) findViewById(R.id.etThirdPartyPlace);
        etThirdPartyMoney = (EditText) findViewById(R.id.etThirdPartyMoney);
        etEventName = (EditText) findViewById(R.id.etEventName);
        btnRegister = (ImageButton) findViewById(R.id.btnRegister);
        llsecondParty = (LinearLayout) findViewById(R.id.llsecondParty);
        llthirdParty = (LinearLayout) findViewById(R.id.llthirdParty);
        choosefromday = (TextView)findViewById(R.id.choosefromday);
        choosefromtime = (TextView)findViewById(R.id.choosefromtime);
        choosetoday = (TextView)findViewById(R.id.choosetoday);
        choosetotime = (TextView)findViewById(R.id.choosetotime);


    }

    /*****************************************갤러리에서 선택기능**********************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_IMAGE) {

                try {
                    //선택한 파일 uri
                    mImageCaptureUri = data.getData();
                    //선택한 파일이름
                    filename = getImageNameToUri(mImageCaptureUri);
                    Toast.makeText(getBaseContext(), "name_Str : " + mImageCaptureUri, Toast.LENGTH_SHORT).show();
                    selectimage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // 디바이스에 captainNa폴더 생성(크롭파일 저장할 공간)
                    String sdPath = getFilesDir().getAbsolutePath() + "/captainNa";
                    File folder = new File(sdPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                        Toast.makeText(this, "make folder Success! " + folder, Toast.LENGTH_SHORT).show();
                    }

                    //크롭한 파일 uri
                    cropfilePath = sdPath + "/na" + System.currentTimeMillis() + ".jpg";


                    Uri destination = Uri.fromFile(new File(cropfilePath));
                    Crop.of(mImageCaptureUri, destination).withAspect(2, 1).start(MakeEventActivity.this);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CROP_FROM_CAMERA) {

                final Bundle extras = data.getExtras();

                if (extras != null) {
                    // crop된 bitmap
                    Bitmap photo = extras.getParcelable("data");

                    storeCropImage(photo, cropfilePath);
                    Toast.makeText(getBaseContext(), "cropfilePath : " + cropfilePath, Toast.LENGTH_SHORT).show();


                    File imgFile = new File(cropfilePath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ivAddImg.setImageBitmap(myBitmap);
                    }


                    //ivAddImg.setImageBitmap(photo);
                    Toast.makeText(getApplicationContext(), "Height : " + photo.getHeight() + ", width : " + photo.getWidth(), Toast.LENGTH_LONG).show();

                }

                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


            } else if (requestCode == Crop.REQUEST_CROP) {
                ivAddImg.setImageURI(Crop.getOutput(data));

            }
        }
    }
    /**********************************그룹 아이디 받기*******************************************/
    private void getGroupID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("group_id");

    }
    /**************************이미지 파일 이름 가져오기******************************************/
    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    //*********************** crop된 이미지를 저장하기*********************
    private void storeCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        //  BufferedOutputStream out = null;


        try {
            copyFile.createNewFile();
            FileOutputStream out = new FileOutputStream(copyFile);
            // 넘겨 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();


        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "이미지 저장 에러 : " + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

    }


}
