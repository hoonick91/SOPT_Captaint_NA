package org.sopt.captainna.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.model.MakeGroupResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.sopt.captainna.activity.CommonVariable.token;


public class MakeGroupActivity extends AppCompatActivity {

    //네트워킹
    NetworkService service;
    private int group_id;

    //view
    private ImageView ivGroupImg, ivRegisterImg;
    private EditText etGroupName, etGroupIntro, etPwdSetting, etPwdCheck;
    private ImageView btnRegister;

    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    String imgUrl = "";
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri;   //갤러리에서 선택한 파일 경로
    String filename;        //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group);


        //네트워크초기화
        initNetwork();

        //그룹아이디 받기
        getGroupID();

        //뒤로가기
        goBack();

        //findView
        findView();

        //이벤트 사진 추가
        addImg();


        //등록버튼 클릭하면
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerGroup();
                Intent intent = new Intent(MakeGroupActivity.this, HomeActivity.class);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
            }
        });


    }

    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /**********************************그룹 아이디 받기*******************************************/
    private void getGroupID() {
        //해당 프로젝트 id값 가져오기
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("group_id");

    }

    /*************************************그룹 등록************************************************/
    private void registerGroup() {
        if (checkPassword()) {

            //모임명, 모임소개, 비밀번호, 모임장
            RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), etGroupName.getText().toString());
            RequestBody text = RequestBody.create(MediaType.parse("multipart/form-data"), etGroupIntro.getText().toString());
            RequestBody pw = RequestBody.create(MediaType.parse("multipart/form-data"), etPwdCheck.getText().toString());
            RequestBody chairman_name = RequestBody.create(MediaType.parse("multipart/form-data"), "tempName");

            //사진
            MultipartBody.Part photo;

            if (mImageCaptureUri == null) {
                photo = null;
            } else {

                //resizing
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                InputStream in = null; // here, you need to get your context.
                try {
                    in = getContentResolver().openInputStream(mImageCaptureUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                File file_photo = new File(imgUrl); // 가져온 파일의 이름을 알아내려고 사용합니다

                // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                photo = MultipartBody.Part.createFormData("image", file_photo.getName(), photoBody);
            }


            /****************************************서버에 정보 보냄**************************************/
            Call<MakeGroupResult> request = service.registerGroup(token, title, text, pw, chairman_name, null);
            request.enqueue(new Callback<MakeGroupResult>() {
                @Override
                public void onResponse(Call<MakeGroupResult> call, Response<MakeGroupResult> response) {
                    if (response.isSuccessful()) {
                        if (response.body().result.equals("add group success")) {
                            Toast.makeText(MakeGroupActivity.this, "등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                        } else {


                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<MakeGroupResult> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(MakeGroupActivity.this, "패스워드를 다시 확인해주세요", Toast.LENGTH_LONG).show();
        }

    }

    /***************************************뒤로가기***********************************************/
    private void goBack() {
        LinearLayout back = (LinearLayout) findViewById(R.id.cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeGroupActivity.this, HomeActivity.class);
                intent.putExtra("group_id",group_id);
                startActivity(intent);
                finish();
            }
        });
    }

    /****************************************findView**********************************************/
    private void findView() {
        ivGroupImg = (ImageView) findViewById(R.id.ivGroupImg);
        ivRegisterImg = (ImageView) findViewById(R.id.ivRegisterImg);
        etGroupName = (EditText) findViewById(R.id.etGroupName);
        etGroupIntro = (EditText) findViewById(R.id.etGroupIntro);
        etPwdSetting = (EditText) findViewById(R.id.etPwdSetting);
        etPwdCheck = (EditText) findViewById(R.id.etPwdCheck);
        btnRegister = (ImageView) findViewById(R.id.btnRegister);
    }

    /*************************************이벤트 부분 사진 추가**************************************/
    private void addImg() {
        ivGroupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    Crop.of(mImageCaptureUri, destination).asSquare().start(MakeGroupActivity.this);

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



                    File imgFile = new File(cropfilePath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //    ivGroupImg.setImageBitmap(myBitmap);
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

        imgUrl = imgPath;


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

    /***********************************패스워드 확인***************************************/
    private boolean checkPassword() {
        if (etPwdSetting.getText().toString().equals(etPwdCheck.getText().toString())) { //완료
            return true;
        } else {
            return false;
        }
    }
}
