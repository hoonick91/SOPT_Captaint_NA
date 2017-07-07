package org.sopt.captainna.activity;

import android.app.Activity;
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

import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.model.ChangeGroupResult;
import org.sopt.captainna.model.DeleteGroupResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.token;


public class ChangeGroupActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;

    //view
    private ImageView ivGroupImg, ivRegisterImg;
    private EditText etGroupIntro, etPwdSetting, etPwdCheck;
    private TextView tvGroupName;

    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri;   //갤러리에서 선택한 파일 경로
    String filename;        //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group);


        //네트워크초기화
        initNetwork();

        //뒤로가기
        goBack();

        //수정 완료 누르면
        goComplete();

        //삭제 하기 누르면
        goDelete();

        //findView
        findView();

        //이벤트 사진 추가
        addImg();

        //기존 정보 가져오기 ( yet 서버 api 준비 no )
        getPrevInfo();
    }


    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /***************************************모임 삭제하기*******************************************/
    private void deleteGroup() {
        Call<DeleteGroupResult> request = service.deleteGroup(token, 20);
        request.enqueue(new Callback<DeleteGroupResult>() {
            @Override
            public void onResponse(Call<DeleteGroupResult> call, Response<DeleteGroupResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("delete success")) {

                        Toast.makeText(ChangeGroupActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteGroupResult> call, Throwable t) {

            }
        });
    }



    /***************************************기존 정보 가져오기************************************/
    private void getPrevInfo(){

        //가라
        tvGroupName.setText("수정전 모임명");
    }



    /*************************************수정 완료 누르면************************************************/
    private void completeChange() {
        //모임소개, 비밀번호
        RequestBody text = RequestBody.create(MediaType.parse("multipart/form-data"), etGroupIntro.getText().toString());
        RequestBody pw = RequestBody.create(MediaType.parse("multipart/form-data"), etPwdCheck.getText().toString());

        //서버 전송
        Call<ChangeGroupResult> request = service.changeGroup(token, 1, text, pw, null);
        request.enqueue(new Callback<ChangeGroupResult>() {
            @Override
            public void onResponse(Call<ChangeGroupResult> call, Response<ChangeGroupResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.equals("update group success")) {
                        Toast.makeText(ChangeGroupActivity.this, "수정하기 완료", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangeGroupResult> call, Throwable t) {
            }
        });


    }

    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeGroupActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*****************************************완료 누르면******************************************/
    private void goComplete() {
        LinearLayout complete = (LinearLayout) findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeChange();
            }
        });
    }

    /*****************************************삭제 누르면******************************************/
    private void goDelete() {
        LinearLayout llGroupDelete = (LinearLayout) findViewById(R.id.llGroupDelete);
        llGroupDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGroup();
            }
        });
    }

    /****************************************findView**********************************************/
    private void findView() {
        ivGroupImg = (ImageView) findViewById(R.id.ivGroupImg);
        ivRegisterImg = (ImageView) findViewById(R.id.ivRegisterImg);
        tvGroupName = (TextView) findViewById(R.id.tvGroupName);
        etGroupIntro = (EditText) findViewById(R.id.etGroupIntro);
        etPwdSetting = (EditText) findViewById(R.id.etPwdSetting);
        etPwdCheck = (EditText) findViewById(R.id.etPwdCheck);
    }

    /*************************************이벤트 부분 사진 추가**************************************/
    private void addImg() {
        ivGroupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
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
                    Crop.of(mImageCaptureUri, destination).asSquare().start(ChangeGroupActivity.this);

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
                        ivGroupImg.setImageBitmap(myBitmap);
                    }


                    //ivGroupImg.setImageBitmap(photo);
                    Toast.makeText(getApplicationContext(), "Height : " + photo.getHeight() + ", width : " + photo.getWidth(), Toast.LENGTH_LONG).show();

                }

                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


            } else if (requestCode == Crop.REQUEST_CROP) {
                ivGroupImg.setImageURI(Crop.getOutput(data));

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

    /*********************************** crop된 이미지를 저장하기**********************************/
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
