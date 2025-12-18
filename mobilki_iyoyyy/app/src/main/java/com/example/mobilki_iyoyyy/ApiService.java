package com.example.mobilki_iyoyyy;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("register")
    Call<ResponseUser> register(@Body RequestRegister body);

    @POST("login")
    Call<ResponseUser> login(@Body RequestLogin body);

//    @POST("logout")
//    Call<ResponseUser> logout();

    @POST("logout")
    Call<LogoutResponse> logout();

    @GET("user_photo")
    Call<UserPhotoResponse> getUserPhoto();

    @GET("/user_info")
    Call<UserProfileResponse> getUserInfo();


    @GET("friends")
    Call<List<Friend>> getFriends();

    @FormUrlEncoded
    @POST("/set_secret_kode")
    Call<Void> setSecretKode(
            @Field("secret_kod") String secretKode
    );


    @GET("/user_info?full=true")
    Call<UserInfoResponse> getUserInfoRow();

    @GET("/rating")
    Call<RatingResponse> getRating();

    @POST("/login_by_secret")
    Call<UserInfoResponse> loginBySecretKode(@Body SecretLoginRequest body);

    @GET("/forum/topics")
    Call<ForumTopicsResponse> getForumTopics();




    @GET("/forum/messages")
    Call<List<ChatMessage>> getForumMessages(@Query("topic_id") int topicId);

    @GET("forum/message/{message_id}/comments")
    Call<List<CommentModel>> getForumComments(@Path("message_id") int messageId);

    @FormUrlEncoded
    @POST("/forum/add_message")
    Call<ResponseBody> addForumMessage(
            @Field("content") String content,
            @Field("topic_id") int topicId
    );

    @FormUrlEncoded
    @POST("/forum/add_comment")
    Call<Void> addForumComment(
            @Field("content") String content,
            @Field("message_id") int messageId
    );

    @GET("theory/{id}")
    Call<Theory> getTheory(@Path("id") int id);


    @FormUrlEncoded
    @POST("check_invites")
    Call<List<InviteModel>> checkInvites(
            @Field("user_id") int userId
    );
    @GET("/team")
    Call<TeamResponse> getTeamMembers();

    @POST("update_user_project")
    Call<Void> updateUserProject(@Body RequestUserProject request);

    @POST("/update_user_role")
    Call<ResponseBody> updateUserRole(@Body Map<String, String> body);

    @POST("/invite_answer")
    Call<ResponseBody> sendInviteAnswer(@Body Map<String, String> body);

    @FormUrlEncoded
    @POST("/add_friend_to_project")
    Call<ResponseBody> addFriendToProject(
            @FieldMap Map<String, String> data
    );


    @GET("/check_invites")
    Call<java.util.List<InviteModel>> getInvites();

    @POST("/invite_answer")
    Call<ResponseBody> inviteAnswer(@Body Map<String, Object> data);


    @GET("project/content")
    Call<ProjectContentResponse> getProjectContent(@Query("project_id") int projectId);



    @POST("/request_delete_teammate")
    Call<ResponseBody> requestDeleteTeammate(@Body Map<String, Object> body);

    @POST("/accept_delete_teammate")
    Call<ResponseBody> acceptDeleteTeammate(@Body Map<String, Object> body);

    @POST("/decline_delete_teammate")
    Call<ResponseBody> declineDeleteTeammate(@Body Map<String, Object> body);




    @GET("get_tasks")
    Call<List<Task>> getTasks();

    @POST("send_answer")
    Call<AnswerResponse> sendAnswer(@Body AnswerRequest request);

    @GET("/tasks_by_razdel")
    Call<List<Task>> getTasksByRazdel(@Query("razdel_id") int razdelId);

    @POST("/login_guest")
    Call<GuestResponse> loginGuest();

    @GET("friend/{id}")
    Call<Friend> getFriendProfile(@Path("id") int friendId);

    @Multipart
    @POST("/upload_user_photo")
    Call<ResponseBody> uploadUserPhoto(@Part MultipartBody.Part photo);

    @GET("/search_user")
    Call<List<Friend>> searchUsers(@Query("q") String query);

    @FormUrlEncoded
    @POST("/add_friend")
    Call<ResponseBody> addFriend(@Field("friend_id") int friendId);

    @FormUrlEncoded
    @POST("/friend/request")
    Call<ResponseBody> requestFriend(@Field("friend_id") int friendId);

    @FormUrlEncoded
    @POST("friend/decline")
    Call<ResponseBody> declineFriend(
            @Field("friend_id") int friendId
    );

    @GET("friend/requests")
    Call<List<Friend>> getFriendRequests();

    @FormUrlEncoded
    @POST("friend/accept")
    Call<ResponseBody> acceptFriend(
            @Field("friend_id") int friendId);

    @POST("login/google")
    Call<ResponseGoogleLogin> loginGoogle(@Body MainActivity2.GoogleLoginRequest request);

    @POST("/user/photo")
    Call<Response<Void>> saveUserPhoto(@Body Map<String, String> body);



}
