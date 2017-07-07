package org.sopt.captainna.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import org.sopt.captainna.R;
import org.sopt.captainna.model.JoinResult;
import org.sopt.captainna.network.ApplicationController;
import org.sopt.captainna.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minjeong on 2017-07-05.
 */

public class JoinActivity extends Activity {

    //네트워킹
    NetworkService service;


    private CircleImageView profileImg;
    private EditText user_name;
    private EditText user_email;
    private EditText user_phone;
    private EditText etPwdSetting;
    private EditText etPwdCheck;
    private ImageButton btnRegister;
    private TextView myprivacy;


    //갤러리 & 크롭기능 사용용
    final int REQ_CODE_SELECT_IMAGE = 1;
    final int CROP_FROM_CAMERA = 2;
    Uri mImageCaptureUri;   //갤러리에서 선택한 파일 경로
    String filename;        //갤러리에서 선택한 파일 이름
    Bitmap selectimage;
    String cropfilePath; // 크롭 파일 저장 경로
    String cropFileName; //크롭 파일 이름
    File imgFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        //뷰 연결
        findview();

        clickListener();

        //네트워크초기화
        initNetwork();



    }


    /****************************************네트워크 초기화*****************************************/
    private void initNetwork() {
        service = ApplicationController.getInstance().getNetworkService();
    }

    /********************************뷰 연결*****************************************************88*/
    private void findview() {
        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        user_name = (EditText) findViewById(R.id.user_name);
        user_email = (EditText) findViewById(R.id.user_email);
        user_phone = (EditText) findViewById(R.id.user_phone);
        etPwdSetting = (EditText) findViewById(R.id.etPwdSetting);
        btnRegister = (ImageButton) findViewById(R.id.btnRegister);
        etPwdCheck = (EditText) findViewById(R.id.etPwdCheck);
        myprivacy = (TextView)findViewById(R.id.myprivacy);

    }

    private void clickListener() {

        myprivacy.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(JoinActivity.this, Myprivacy.class);
                 startActivity(intent);
             }
         });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*if (requestCode == Crop.REQUEST_CROP) {
                    Toast.makeText(getApplicationContext(), "사진을 선택해주세요!", Toast.LENGTH_SHORT).show();
                } else*/ if (user_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (user_email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (user_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (etPwdSetting.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (etPwdCheck.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkPassword()) {
                        //사용자 입력 정보
                        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), user_email.getText().toString());
                        RequestBody pw = RequestBody.create(MediaType.parse("multipart/form-data"), etPwdSetting.getText().toString());
                        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), user_name.getText().toString());
                        RequestBody ph = RequestBody.create(MediaType.parse("multipart/form-data"), user_phone.getText().toString());
                        //현재 사진 들어가지 않음.

                        /*BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageCaptureUri), null, options);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                        MultipartBody.Part profile;
                        profile = MultipartBody.Part.createFormData("image", filename, photoBody);
*/

                        Call<JoinResult> request = service.join(email, pw, name, ph, null);
                        request.enqueue(new Callback<JoinResult>() {
                            @Override
                            public void onResponse(Call<JoinResult> call, Response<JoinResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().result.equals("회원가입 완료")) {
                                        Toast.makeText(JoinActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else if (response.code() == 406) {
                                    Toast.makeText(JoinActivity.this, "이미 가입된 회원입니다", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JoinResult> call, Throwable t) {


                            }
                        });
                    } else {
                        Toast.makeText(JoinActivity.this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
                    }
                }
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
//                    Toast.makeText(getBaseContext(), "name_Str : " + mImageCaptureUri, Toast.LENGTH_SHORT).show();
                    selectimage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    // 디바이스에 captainNa폴더 생성(크롭파일 저장할 공간)
                    String sdPath = getFilesDir().getAbsolutePath() + "/captainNa";
                    File folder = new File(sdPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
//                        Toast.makeText(this, "make folder Success! " + folder, Toast.LENGTH_SHORT).show();
                    }

                    //크롭한 파일 uri
                    cropFileName = "/na" + System.currentTimeMillis() + ".jpg";
                    cropfilePath = sdPath + "" + cropFileName;

                    Uri destination = Uri.fromFile(new File(cropfilePath));
                    Crop.of(mImageCaptureUri, destination).asSquare().start(JoinActivity.this);

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


                    imgFile = new File(cropfilePath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        profileImg.setImageBitmap(myBitmap);
                    }


                    //ivGroupImg.setImageBitmap(photo);
//                    Toast.makeText(getApplicationContext(), "Height : " + photo.getHeight() + ", width : " + photo.getWidth(), Toast.LENGTH_LONG).show();

                }

                File file = new File(mImageCaptureUri.getPath());
                if (file.exists()) {
                    file.delete();
                }


            } else if (requestCode == Crop.REQUEST_CROP) {
                profileImg.setImageURI(Crop.getOutput(data));

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

    /***********************************패스워드 확인***************************************/
    private boolean checkPassword() {
        if (etPwdSetting.getText().toString().equals(etPwdCheck.getText().toString())) { //완료
            return true;
        } else {
            return false;
        }
    }

}
