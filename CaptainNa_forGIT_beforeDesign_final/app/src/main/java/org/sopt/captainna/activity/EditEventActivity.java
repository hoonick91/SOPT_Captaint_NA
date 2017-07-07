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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.dialog.DateDialog;
import org.sopt.captainna.model.DeleteEventResult;
import org.sopt.captainna.model.EditEventResult;
import org.sopt.captainna.model.EventDetailResult;
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
import static org.sopt.captainna.activity.CommonVariable.isFrombutton;
import static org.sopt.captainna.activity.CommonVariable.pushEditDetail;
import static org.sopt.captainna.activity.CommonVariable.subEditDetail;
import static org.sopt.captainna.activity.CommonVariable.token;

public class EditEventActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;

    //view
    private ImageView ivAddImg, ivAddParty, fromWhen, toWhen;
    private EditText etEventName, etEventContents, etEventPlace, etEventMoney,
            etFirstPartyPlace, etFirstPartyMoney, etSecondPartyPlace, etSecondPartyMoney,
            etThirdPartyPlace, etThirdPartyMoney;
    //private ImageButton btnRegister;
    private LinearLayout llsecondParty, llthirdParty;
    private TextView choosefromday,choosetoday,choosefromtime,choosetotime;
    private   LinearLayout delete;
    private  LinearLayout ok;
    private  LinearLayout llGroupDelete_edit;

    //뒤풀이 추가 카운트
    private int partyCnt;

    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri; //갤러리에서 선택한 파일 경로
    String filename; //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로

    //dialog
    public DateDialog dialog;

    //정보가져오기
    private  EventDetailResult.Event getEditDetail;
    private int event_id;

    private int group_id;
    private String start_day="";
    private String start_time="";
    private String end_day="";
    private String end_time="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_event);


        //네트워크초기화
        initNetwork();

        //id값 intent에서 가져오기
        getEventID();
        getGroupID();

        //뒤로가기
        goBack();

        //findView
        findView();

        //init 정보가져오기
        initView();

        //뒤풀이 차수 추가
        addParty();

        //이벤트 사진 추가
        addImg();

        //행사날짜 추가
        choiceDate();

        //행사 등록하기
        regsiterEvent();

        //행사 삭제하기
        deleteEvent();

        //창끄기
        deleteEdit();
    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /*************************************정보 가져오기*****************************************/
    private void initView(){
        getEditDetail = pushEditDetail;

        Glide.with(EditEventActivity.this)
                .load(getEditDetail.photo)
                .into(ivAddImg);

        etEventName.setText(getEditDetail.title);//행사이름
        etEventContents.setText(getEditDetail.text);//행사내용

/*        //실제 맞는 코드! 아래코드는 더미데이터 형식에 맞춰쓴것 실험용(2017-10-4 17:00 같은형식)
        String start = getEditDetail.start_date;
        String date[] = start.split(" ");
        choosefromday.setText(date[0]+" "+date[1]+" "+date[2]);
        choosefromtime.setText(date[3]+" "+date[4]+" "+date[5]);

        String start2 = getEditDetail.end_date;
        String date2[] = start2.split(" ");
        choosefromday.setText(date2[0]+" "+date2[1]+" "+date2[2]);
        choosefromtime.setText(date2[3]+" "+date2[4]+" "+date2[5]);*/

        String start = getEditDetail.start_date;
        String date[] = start.split("-");
        String temp[] = date[2].split(" ");
        choosefromday.setText(date[0]+"년 "+date[1]+"월 "+temp[0]+"일");


        String time[] = temp[1].split(":");
        if(Integer.parseInt(time[0])>12){
        choosefromtime.setText("오후 "+(Integer.parseInt(time[0])-12)+"시 "+time[1]+"분");
        }
        else if(Integer.parseInt(time[0])== 12){
            choosefromtime.setText("오후 "+time[0]+"시 "+time[1]+"분");
        }else {
            choosefromtime.setText("오전 "+time[0]+"시 "+time[1]+"분");
        }

        String start2 = getEditDetail.end_date;
        String date2[] = start.split("-");
        String temp2[] = date2[2].split(" ");
        choosetoday.setText(date2[0]+"년 "+date2[1]+"월 "+temp2[0]+"일");


        String time2[] = temp2[1].split(":");
        if(Integer.parseInt(time2[0])>12){
            choosetotime.setText("오후 "+(Integer.parseInt(time2[0])-12)+"시 "+time2[1]+"분");
        }
        else if(Integer.parseInt(time[0])== 12){
            choosetotime.setText("오후 "+time2[0]+"시 "+time2[1]+"분");
        }else {
            choosetotime.setText("오전 "+time2[0]+"시 "+time2[1]+"분");
        }


        etEventPlace.setText(getEditDetail.place); // 행사장소
        etEventMoney.setText(Integer.toString(getEditDetail.amount));//행사금액
        partyCnt = subEditDetail.size();

        if(partyCnt >= 1){
        etFirstPartyPlace.setText(subEditDetail.get(0).place);//1차
        etFirstPartyMoney.setText(Integer.toString(subEditDetail.get(0).price));
        }

        if(partyCnt>=2){
            llsecondParty.setVisibility(View.VISIBLE);
        etSecondPartyPlace.setText(subEditDetail.get(1).place);;//2차
        etSecondPartyMoney.setText(Integer.toString(subEditDetail.get(1).price));
        }

        if(partyCnt == 3){
            llthirdParty.setVisibility(View.VISIBLE);
        etThirdPartyPlace.setText(subEditDetail.get(2).place);;//3차
        etThirdPartyMoney.setText(Integer.toString(subEditDetail.get(2).price));
        }



    }

    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.cancelMakeEvent);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEventActivity.this, EventDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*****************************************완료 버튼*****************************************/
    private void regsiterEvent() {

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //차후 행사
                RequestBody place_1st = RequestBody.create(MediaType.parse("multipart/form-data"), etFirstPartyPlace.getText().toString());
                RequestBody amount_1st = RequestBody.create(MediaType.parse("multipart/form-data"), etFirstPartyMoney.getText().toString());

                RequestBody place_2nd = RequestBody.create(MediaType.parse("multipart/form-data"), etSecondPartyPlace.getText().toString());
                RequestBody amount_2nd = RequestBody.create(MediaType.parse("multipart/form-data"), etSecondPartyMoney.getText().toString());

                RequestBody place_3rd = RequestBody.create(MediaType.parse("multipart/form-data"), etThirdPartyPlace.getText().toString());
                RequestBody amount_3rd = RequestBody.create(MediaType.parse("multipart/form-data"), etThirdPartyMoney.getText().toString());


                /****************************************서버에 정보 보냄**************************************/
                ///group_id 가라 수정해야함!!!! 1로 그냥 박음
                Call<EditEventResult> request = service.getEditEvent(token, group_id, event_id, title, text, place, amount, start_date, end_date,
                        place_1st, amount_1st, place_2nd, amount_2nd, place_3rd, amount_3rd, null);
                request.enqueue(new Callback<EditEventResult>() {
                    @Override
                    public void onResponse(Call<EditEventResult> call, Response<EditEventResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().result.equals("update event success")) {
                                Toast.makeText(EditEventActivity.this, "행사 수정 완료", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EditEventResult> call, Throwable t) {

                    }
                });
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
    /***************************************창 끄기 버튼*****************************************/
    private void deleteEdit() {

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEventActivity.this, EventDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /***************************************행사 삭제 버튼*****************************************/
    private void deleteEvent() {

        llGroupDelete_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /***********************************서버 통신****************************************/
                Call<DeleteEventResult> request = service.deleteEvent(token, event_id);
                request.enqueue(new Callback<DeleteEventResult>() {
                    @Override
                    public void onResponse(Call<DeleteEventResult> call, Response<DeleteEventResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().result.equals("delete success")) {
                                Toast.makeText(EditEventActivity.this, "행사 삭제 완료", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditEventActivity.this, ListActivity.class);
                                intent.putExtra("group_id",group_id);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteEventResult> call, Throwable t) {

                    }
                });

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
                    Toast.makeText(EditEventActivity.this, "3차까지 가능합니다", Toast.LENGTH_SHORT).show();
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

        String afternoon = "오전 ";
        int hour= 0;

        if (cal.get(Calendar.HOUR_OF_DAY)>=12) { //pm일때
            afternoon = "오후 ";
            if(cal.get(Calendar.HOUR_OF_DAY) > 12) //13시~23시
                hour = cal.get(Calendar.HOUR_OF_DAY)-12;
        }
        else{
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }



        dialog = new DateDialog(EditEventActivity.this);
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
                    String year[] = choosefromday.getText().toString().split("년 ");
                    String  month[] = year[1].split("월 ");
                    String day[] = month[1].split("일");
                    String afternoon[] = choosefromtime.getText().toString().split(" ");
                    String hour[] = afternoon[1].split("시");
                    String minute[] = afternoon[2].split("분");

                    dialog.datePickerTheme.updateDate(Integer.parseInt(year[0]), (Integer.parseInt(month[0]))-1, Integer.parseInt(day[0]));
                    if(afternoon.equals("오전"))
                        dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0]));
                    else {
                        if (hour[0].equals("12"))
                            dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0]));
                        else
                            dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0])-12);
                    }
                    dialog.timePickerTheme.setCurrentMinute(Integer.parseInt(minute[0]));

                } else {
                    String year[] = choosetoday.getText().toString().split("년 ");
                    String  month[] = year[1].split("월 ");
                    String day[] = month[1].split("일");
                    String afternoon[] = choosetotime.getText().toString().split(" ");
                    String hour[] = afternoon[1].split("시");
                    String minute[] = afternoon[2].split("분");

                    dialog.datePickerTheme.updateDate(Integer.parseInt(year[0]), (Integer.parseInt(month[0]))-1, Integer.parseInt(day[0]));
                    if(afternoon.equals("오전"))
                        dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0]));
                    else {
                        if (hour[0].equals("12"))
                            dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0]));
                        else
                            dialog.timePickerTheme.setCurrentHour(Integer.parseInt(hour[0])-12);
                    }
                    dialog.timePickerTheme.setCurrentMinute(Integer.parseInt(minute[0]));
                }
            }
        });

    }

    /*****************************************findView*****************************************/
    private void findView() {
        ivAddImg = (ImageView) findViewById(R.id.ivAddImg_edit);
        ivAddParty = (ImageView) findViewById(R.id.ivAddParty_edit);
        fromWhen = (ImageView) findViewById(R.id.fromWhen);
        toWhen = (ImageView) findViewById(R.id.toWhen);
        etEventName = (EditText) findViewById(R.id.etEventName_edit);
        etEventContents = (EditText) findViewById(R.id.etEventContents_edit);
        etEventPlace = (EditText) findViewById(R.id.etEventPlace_edit);
        etEventMoney = (EditText) findViewById(R.id.etEventMoney_edit);
        etFirstPartyPlace = (EditText) findViewById(R.id.etFirstPartyPlace_edit);
        etFirstPartyMoney = (EditText) findViewById(R.id.etFirstPartyMoney_edit);
        etSecondPartyPlace = (EditText) findViewById(R.id.etSecondPartyPlace_edit);
        etSecondPartyMoney = (EditText) findViewById(R.id.etSecondPartyMoney_edit);
        etThirdPartyPlace = (EditText) findViewById(R.id.etThirdPartyPlace_edit);
        etThirdPartyMoney = (EditText) findViewById(R.id.etThirdPartyMoney_edit);
        etEventName = (EditText) findViewById(R.id.etEventName_edit);
       // btnRegister = (ImageButton) findViewById(R.id.btnRegister);
        llsecondParty = (LinearLayout) findViewById(R.id.llsecondParty_edit);
        llthirdParty = (LinearLayout) findViewById(R.id.llthirdParty_edit);
        choosefromday = (TextView)findViewById(R.id.choosefromday_edit);
        choosefromtime = (TextView)findViewById(R.id.choosefromtime_edit);
        choosetoday = (TextView)findViewById(R.id.choosetoday_edit);
        choosetotime = (TextView)findViewById(R.id.choosetotime_edit);
        delete = (LinearLayout) findViewById(R.id.cancelMakeEvent) ;
        ok= (LinearLayout) findViewById(R.id.complete) ;
       llGroupDelete_edit = (LinearLayout)findViewById(R.id.llGroupDelete_edit);


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
                    Crop.of(mImageCaptureUri, destination).withAspect(2, 1).start(EditEventActivity.this);

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
