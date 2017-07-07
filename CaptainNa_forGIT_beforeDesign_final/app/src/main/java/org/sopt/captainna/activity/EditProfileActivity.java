package org.sopt.captainna.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.model.EditProfileResult;
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

/**
 * Created by minjeong on 2017-07-05.
 */

public class EditProfileActivity extends Activity{

    //네트워킹
    NetworkService service;

    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri;   //갤러리에서 선택한 파일 경로
    String filename;        //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로

    //화면
    private ImageView editprofileImg;
    private TextView etGroupName;
    private TextView etUserEmail;
    private EditText etGroupIntro;
    private EditText etPwdSetting;
    private EditText etPwdCheck;// 패스워드 비교용
    private LinearLayout linearLayout;

    //툴바
    private LinearLayout complete;
    private LinearLayout close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        //네트워크초기화
        initNetwork();

        //view 연결
        findView();

        //클릭리스너 연결
        clickListener();

        //패스워드 확인
        checkPassword();

    }
    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    private void findView(){

         editprofileImg = (ImageView)findViewById(R.id.editprofileImg);
        etGroupName = (TextView)findViewById(R.id.etGroupName);
        etUserEmail = (TextView)findViewById(R.id.etUserEmail);
        etGroupIntro = (EditText)findViewById(R.id.etGroupIntro);
        etPwdSetting = (EditText)findViewById(R.id.etPwdSetting);
       etPwdCheck = (EditText)findViewById(R.id.etPwdCheck);// 패스워드 비교용

        complete = (LinearLayout)findViewById(R.id.complete);
        close = (LinearLayout)findViewById(R.id.close);
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        etGroupName.setText(intent.getExtras().getString("name"));
        etUserEmail.setText(intent.getExtras().getString("email"));
        etGroupIntro.setText(intent.getExtras().getString("phone"));

    }


    private void clickListener(){
        editprofileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        //수정확인버튼
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPassword()) { //패스워드가 같으면
                    /****************************통신시작*************************************************/

                    //사용자 입력 정보
                    RequestBody pw = RequestBody.create(MediaType.parse("multipart/form-data"), etPwdSetting.getText().toString());
                    RequestBody ph = RequestBody.create(MediaType.parse("multipart/form-data"), etGroupIntro.getText().toString());
                    //현재 사진 들어가지 않음.

                    Call<EditProfileResult> request = service.setProfile(token, pw, ph,null);
                    request.enqueue(new Callback<EditProfileResult>() {
                        @Override
                        public void onResponse(Call<EditProfileResult> call, Response<EditProfileResult> response) {
                            if (response.isSuccessful()) {
                              if(response.body().result.equals("회원정보 수정 완료")){
                                  Toast.makeText(EditProfileActivity.this,"회원정보 수정 완료",Toast.LENGTH_LONG).show();
                                  Intent intent = new Intent(EditProfileActivity.this,HomeActivity.class);
                                  startActivity(intent);
                                  finish();
                              }
                            }
                        }

                        @Override
                        public void onFailure(Call<EditProfileResult> call, Throwable t) {

                        }
                    });

                }else{ //패스워드가 틀리면
                    Toast.makeText(EditProfileActivity.this,"패스워드를 다시 확인해주세요",Toast.LENGTH_LONG).show();
                }

            }
        });

        //취소(뒤로가기)
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //로그아웃
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                token = "";
                startActivity(intent);
                finish();
            }
        });


    }
    private boolean checkPassword(){
        if(etPwdCheck.getText().toString().equals(etPwdSetting.getText().toString())){ //완료
            return true;
        }
        else{
            return  false;
        }
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
                    Crop.of(mImageCaptureUri, destination).asSquare().start(EditProfileActivity.this);

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
                        editprofileImg.setImageBitmap(myBitmap);
                    }


                    //ivGroupImg.setImageBitmap(photo);
                    Toast.makeText(getApplicationContext(), "Height : " + photo.getHeight() + ", width : " + photo.getWidth(), Toast.LENGTH_LONG).show();

                }

                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


            } else if (requestCode == Crop.REQUEST_CROP) {
                editprofileImg.setImageURI(Crop.getOutput(data));

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
