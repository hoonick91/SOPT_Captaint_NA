package org.sopt.captainna.network;


import org.sopt.captainna.model.AddList;
import org.sopt.captainna.model.AddParticipantList;
import org.sopt.captainna.model.ApplyResult;
import org.sopt.captainna.model.ChangeGroupResult;
import org.sopt.captainna.model.DeleteApplyResult;
import org.sopt.captainna.model.DeleteEventResult;
import org.sopt.captainna.model.DeleteGroupResult;
import org.sopt.captainna.model.EditEventResult;
import org.sopt.captainna.model.EditProfileResult;
import org.sopt.captainna.model.EventDetailResult;
import org.sopt.captainna.model.HomeResult;
import org.sopt.captainna.model.JoinGroupResult;
import org.sopt.captainna.model.JoinResult;
import org.sopt.captainna.model.LoginResult;
import org.sopt.captainna.model.MakeEventResult;
import org.sopt.captainna.model.MakeGroupResult;
import org.sopt.captainna.model.MyGroupEventResult;
import org.sopt.captainna.model.MyPageResult;
import org.sopt.captainna.model.ParticipateList;
import org.sopt.captainna.model.PayResult;
import org.sopt.captainna.model.RecentEventResult;
import org.sopt.captainna.model.SearchGroupResult;
import org.sopt.captainna.model.SendPassword;
import org.sopt.captainna.model.UserId;
import org.sopt.captainna.model.loginInfo;
import org.sopt.captainna.model.moveResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by pc on 2017-05-14.
 */

public interface NetworkService {


    //홈 리스트 가져오기
    @GET("/main")
    Call<HomeResult> getHomeList(@Header("token") String token);
    //토큰? 내가 나를증명하는것

    //행사 상세 정보 보기
    @GET("/event/{event_id}")
    Call<EventDetailResult> getEventDetail(@Header("token") String token,
                                           @Path("event_id") int event_id);

    //모임, 행사 정보 보기
    @GET("/group/{group_id}")
    Call<MyGroupEventResult> getMyGroupEvent(@Header("token") String token,
                                             @Path("group_id") int group_id);

    //메인화면 최근행사 더보기
    @GET("/event")
    Call<RecentEventResult> getRecentEventList(@Header("token") String token);


    //모임 만들기
    @Multipart
    @POST("/group")
    Call<MakeGroupResult> registerGroup(@Header("token") String token,
                                        @Part("title") RequestBody title,
                                        @Part("text") RequestBody text,
                                        @Part("pw") RequestBody pw,
                                        @Part("chairman_name") RequestBody chairman_name,
                                        @Part MultipartBody.Part photo);

    //행사 만들기
    @Multipart
    @POST("/event/{group_id}")
    Call<MakeEventResult> registerEvent(@Header("token") String token,
                                        @Path("group_id") int group_id,
                                        @Part("title") RequestBody title,
                                        @Part("text") RequestBody text,
                                        @Part("place") RequestBody place,
                                        @Part("amount") RequestBody amount,
                                        @Part("start_date") RequestBody start_date,
                                        @Part("end_date") RequestBody end_date,
                                        @Part("manager_name") RequestBody manager_name,
                                        @Part("place_1st") RequestBody place_1st,
                                        @Part("amount_1st") RequestBody amount_1st,
                                        @Part("place_2nd") RequestBody place_2nd,
                                        @Part("amount_2nd") RequestBody amount_2nd,
                                        @Part("place_3rd") RequestBody place_3rd,
                                        @Part("amount_3rd") RequestBody amount_3rd,
                                        @Part MultipartBody.Part photo);

    //모임 수정
    @Multipart
    @PUT("/group/{group_id}")
    Call<ChangeGroupResult> changeGroup(@Header("token") String token,
                                        @Path("group_id") int group_id,
                                        @Part("text") RequestBody text,
                                        @Part("pw") RequestBody pw,
                                        @Part MultipartBody.Part photo);

    //모임 삭제
    @DELETE("/group/{group_id}")
    Call<DeleteGroupResult> deleteGroup(@Header("token") String token,
                                        @Path("group_id") int group_id);

    //마이페이지 조회
    @GET("/main/mypage")
    Call<MyPageResult> myPage(@Header("token") String token);

    //모임 검색
    @GET("/group?")
    Call<SearchGroupResult> searchGroup(@Header("token") String token,
                                        @Query("title") String title);

    //모임 참가
//    @FormUrlEncoded
    @POST("/group/join/{group_id}")
    Call<JoinGroupResult> joinGroup(@Header("token") String token,
                                    @Path("group_id") int group_id,
                                    @Body SendPassword pw);

    //참가자 명단 받기
    @GET("/event/{event_id}/participant")
    Call<ParticipateList> getParticipateList(@Header("token") String token,
                                             @Path("event_id") int event_id);

    //비회원 리스트 추가
    @POST("/event/{event_id}/participant")
    Call<AddParticipantList> setParticipateList(@Header("token") String token,
                                                @Path("event_id") int event_id,
                                                @Body AddList not_users);

    //행사 참가하기 버튼
    @POST("/event/{event_id}/apply")
    Call<ApplyResult> setApply(@Header("token") String token,
                               @Path("event_id") int event_id);

    //로그인 기능
    @POST("/member/login")
    Call<LoginResult> login(@Body loginInfo loginlist);

    //회원가입
    @Multipart
    @POST("/member")
    Call<JoinResult> join(@Part("email") RequestBody email,
                          @Part("pw") RequestBody pw,
                          @Part("name") RequestBody name,
                          @Part("ph") RequestBody ph,
                          @Part MultipartBody.Part photo);

    //회원정보 수정
    @Multipart
    @PUT("/member")
    Call<EditProfileResult> setProfile(@Header("token") String token,
                                       @Part("pw") RequestBody pw,
                                       @Part("ph") RequestBody ph,
                                       @Part MultipartBody.Part photo);

    //행사 정보 수정
    @Multipart
    @PUT("/event/{group_id}/{event_id}")
    Call<EditEventResult> getEditEvent(@Header("token") String token,
                                       @Path("group_id") int group_id,
                                       @Path("event_id") int event_id,

                                       @Part("title") RequestBody title,
                                       @Part("text") RequestBody text,
                                       @Part("place") RequestBody place,
                                       @Part("amount") RequestBody amount,
                                       @Part("start_date") RequestBody start_date,
                                       @Part("end_date") RequestBody end_date,
                                       @Part("place_1st") RequestBody place_1st,
                                       @Part("amount_1st") RequestBody amount_1st,
                                       @Part("place_2nd") RequestBody place_2nd,
                                       @Part("amount_2nd") RequestBody amount_2nd,
                                       @Part("place_3rd") RequestBody place_3rd,
                                       @Part("amount_3rd") RequestBody amount_3rd,
                                       @Part MultipartBody.Part photo);

    //행사 삭제
    @DELETE("/event/{event_id}")
    Call<DeleteEventResult> deleteEvent(@Header("token") String token,
                                        @Path("event_id") int event_id);

    //결제하기
    @PUT("/event/{event_id}")
    Call<PayResult> paid(@Header("token") String token,
                         @Path("event_id") int event_id);

    //참가신청 취소하기
    @DELETE("/event/{event_id}/apply")
    Call<DeleteApplyResult> deleteapply (@Header("token") String token,
                                         @Path("event_id") int event_id);

    //입금자-미입금자 보내기
    @PUT("/event/{event_id}/participant/move")
    Call<moveResult> move (@Header("token") String token,
                           @Path("event_id") int event_id,
                           @Body UserId userId );


}