package com.sconti.studentcontinent.network;


import com.sconti.studentcontinent.activity.notifications.model.UserNotificationsModel;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorHashTagModel;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.outdoor.model.TagsAndCategoryModel;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.CategoryModel;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.DegreeAspirationModel;
import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.LatestTrendsModel;
import com.sconti.studentcontinent.model.LevelOneModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.LikeModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.NotificationDataModel;
import com.sconti.studentcontinent.model.ShareDataModel;
import com.sconti.studentcontinent.model.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */

    /* //mine AIzaSyD_Zbbwx7aYQaAWnl5O2Dv4-6r2G3dhEUI
     //ind AIzaSyDexSpfSK4WI1XnxQCuusnateV57knMJww

    */
    @GET("getCategory.php")
    Call<List<CategoryModel>> getCategoryModels(@Query("option_id") String option_id);

    @GET("getLeveloneCategory.php")
    Call<List<LevelOneModel>> getLevelOne(@Query("cat_id") String cat_id);

    @POST("InsertUsers")
    Call<APIResponseModel> insertUser(@Body UserDetails csrModel);

    @GET("VerifyUser")
    Call<APIResponseModel> verifyUser(@Query("id") String phone, @Query("pwd") String pwd, @Query("type") int type);


    @GET("UpdateFCM")
    Call<APIResponseModel> updateFcm(@Query("phone") String phone, @Query("fcm_token") String fcm);

    @GET("ProfileUrlUpdate")
    Call<APIResponseModel> updateAvatar(@Query("phone") String phone, @Query("imageURL") String url);

    @GET("UpdateBanner")
    Call<APIResponseModel> updateBanner(@Query("userID") String phone, @Query("bannerURL") String url);

    @GET("getHashTags")
    Call<List<OutDoorHashTagModel>> getHashTags();


    @GET("GetKnowledgePost")
    Call<NewAPIResponseModel> GetKnowledgePost(@Query("category") String category, @Query("type") int type, @Query("FeatureType") int FeatureType, @Query("offset") int offset);

    @GET("v2/GetAllUserPostByUserID")
    Call<NewAPIResponseModel> GetAllUserPostByUserID(@Query("UserId") String UserId,@Query("type") int type,@Query("offset") int offset);


    @POST("AddPost")
    Call<APIResponseModel> AddPost(@Body OutDoorPostModel outDoorPostModel, @Query("type") int type);


    @GET("GetTagsAndCategory.php")
    Call<List<OutDoorHashTagModel>> getTagsAndCategory();

    @GET("DeletePost")
    Call<List<APIResponseModel>> deletePost(@Query("postId") String postId, @Query("type") int type);

    @POST("EditPost")
    Call<APIResponseModel> EditPost(@Body OutDoorPostModel outDoorPostModel, @Query("type") int type);

    @POST("AddOutdoorComment.php")
    Call<APIResponseModel> postOutDoorComment(@Body CommentModel commentModel);

    @POST("AddComment")
    Call<APIResponseModel> AddComment(@Body CommentModel commentModel, @Query("type") int type);



    @POST("AddKnowledgeGeneralComment.php")
    Call<APIResponseModel> AddKnowledgeGeneralComment(@Body CommentModel commentModel);

    //asp
    @POST("AddAspirationComment.php")
    Call<APIResponseModel> postAspirationComment(@Body CommentModel commentModel);

    @POST("AddAspirationGenComment.php")
    Call<APIResponseModel> postAspirationGeneralComment(@Body CommentModel commentModel);


    @GET("GetNotificationsByID")
    Call<NewAPIResponseModel> GetNotificationsByID(@Query("UserID") String uid, @Query("type") int type);

    @GET("UpdateNotificationStatus")
    Call<NewAPIResponseModel> UpdateNotificationStatus(@Query("type") int type, @Query("status") int value, @Query("NotificationID") String ID);

    @POST("MakeNotificationRead")
    Call<NewAPIResponseModel> MakeNotificationRead(@Query("type") int type, @Query("status") int value, @Query("NotificationID") String ID);

    //GetPostDetailsByPostID
    @GET("GetPostDetailsByPostID")
    Call<NewAPIResponseModel> GetPostDetailsByPostID(@Query("postID") String postID, @Query("type") int type);

    @GET("GetCommentsByPostId")
    Call<NewAPIResponseModel> GetCommentsByPostId(@Query("Id") String postId, @Query("type") int type);

    //aspiration
    @GET("GetSubCommentsByPostId")
    Call<NewAPIResponseModel> GetSubCommentsByPostId(@Query("Id") String postId, @Query("type") int type);

    /*@GET("GetSubCommentsByPostId")
    Call<NewAPIResponseModel> GetSubCommentsByPostId(@Query("sl_no") String postId, @Query("type") int type);*/


    @GET("UpdateAspiration")
    Call<APIResponseModel> updateAspiration(@Query("phone") String phone, @Query("interest") String interest);

    @GET("GetLikeListByUserID")
    Call<NewAPIResponseModel> GetLikeListByUserID(@Query("UserID") String userID, @Query("type") int type);

    @GET("UpdateDegree")
    Call<APIResponseModel> updateDegree(@Query("phone") String phone, @Query("degree") String degree);

    @GET("UpdateName")
    Call<APIResponseModel> updateNAme(@Query("uid") String uid, @Query("phone") String phone, @Query("name") String name);

    @GET("UpdateCollege")
    Call<APIResponseModel> updateCollege(@Query("phone") String phone, @Query("college") String degree);


    @GET("GetOthersProfile")
    Call<NewAPIResponseModel> GetOthersProfile(@Query("id") String userId);

    @GET("GetUsers")
    Call<APIResponseModel> GetUser(@Query("name") String userId);

    //getall
    @GET("GetUsers")
    Call<NewAPIResponseModel> GetUserDetail(@Query("name") String userId);

    @GET("GetFollowers")
    Call<NewAPIResponseModel> GetFollowers(@Query("id") String userId, @Query("type") String type);

    @GET("GetLatestTrends.php")
    Call<List<LatestTrendsModel>> GetTrends(@Query("type") String type);

    @GET("GetAspirations")
    Call<APIResponseModel> getAspiration();

    @GET("GetDegree")
    Call<APIResponseModel> getDegree();


    @POST("AddFollower")
    Call<NewAPIResponseModel> AddFollower(@Body FollowModel followModel);

    @POST("UnFollow")
    Call<APIResponseModel> UnFollow(@Query("followee_id") String followee_id, @Query("followers_id") String followers_id);

    @POST("AddLike")
    Call<NewAPIResponseModel> addLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    @POST("DisLike")
    Call<NewAPIResponseModel> DisLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    @GET("GeLikedUsers")
    Call<NewAPIResponseModel> GetLike(@Query("postId") String postId, @Query("type") int type);

    @POST("ProfileUpdate")
    Call<NewAPIResponseModel> ProfileUpdate(@Body UserDetails userDetails);


    @POST("AddSubComment")
    Call<APIResponseModel> AddSubComment(@Body CommentModel commentModel, @Query("type") int type);

    @POST("AddShare")
    Call<APIResponseModel> AddShare(@Body ShareDataModel commentModel, @Query("type") int type);

    @GET("GetSubLikedUsers")
    Call<NewAPIResponseModel> GetSubLikedUsers(@Query("postId") String postId, @Query("type") int type);

    //GetCommentLikedUsers

    @GET("GetCommentLikedUsers")
    Call<NewAPIResponseModel> GetCommentLikedUsers(@Query("postId") String postId, @Query("type") int type);


    // Comment Api

    @POST("AddCommentLike")
    Call<NewAPIResponseModel> AddCommentLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    @POST("CommentDisLike")
    Call<NewAPIResponseModel> CommentDisLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    /*@GET("DeleteComment")
    Call<NewAPIResponseModel> DeleteComment(@Query("comment_sl_no") String comment_sl_no, @Query("type") int type);
*/
    @GET("DeleteComment")
    Call<NewAPIResponseModel> DeleteComment(@Query("comment_sl_no") String comment_sl_no,@Query("post_sl_no") String post_sl_no,@Query("commentCount") long commentCount, @Query("type") int type);

    @GET("GetCommentLikeListByUserID")
    Call<NewAPIResponseModel> GetCommentLikeListByUserID(@Query("UserID") String userID, @Query("type") int type);

    //SubComment Api
    @POST("AddSubLike")
    Call<NewAPIResponseModel> AddSubLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    @POST("DisSubLike")
    Call<NewAPIResponseModel> DisSubLike(@Body LikeModel outDoorPostModel, @Query("type") int type);

    @GET("DeleteSubComment")
    Call<NewAPIResponseModel> DeleteSubComment(@Query("comment_sl_no") String comment_sl_no,@Query("post_sl_no") String post_sl_no,@Query("commentCount") long commentCount, @Query("type") int type);

    @GET("GetSubCommentLikeListByUserID")
    Call<NewAPIResponseModel> GetSubCommentLikeListByUserID(@Query("UserID") String userID, @Query("type") int type);


    //Trending

    @GET("v2/GetTrendings")
    Call<NewAPIResponseModel> GetTrendings(@Query("outdoorcategory") String outdoorcategory,@Query("aspirationcategory") String aspirationcategory,@Query("Knowledgecategory") String Knowledgecategory);

//list of comment and shareByUserId
    @GET("GetShareListByUserID")
    Call<NewAPIResponseModel> GetShareListByUserID(@Query("UserID") String UserID,@Query("type") int type);

    @GET("GetCommentListByUserID")
    Call<NewAPIResponseModel> GetCommentListByUserID(@Query("UserID") String UserID,@Query("type") int type);


}
