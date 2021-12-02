package com.sconti.studentcontinent;

import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.User;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser {
   static List<UserDetails> userDetails=new ArrayList<>();

    public static List<UserDetails> getallUser(String name) {
        ApiInterface apiInterface;

        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);

        Call<NewAPIResponseModel> call = apiInterface.GetUserDetail(name);
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                userDetails=response.body().getUsertable();
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

            }
        });

        /*List<User> u1 = Arrays.asList(
                new User(20, "Akash", "https://cdn.vox-cdn.com/thumbor/EX9U2X34axDOUD9Jo2bokrM_Lvg=/0x0:3000x2000/920x613/filters:focal(1260x760:1740x1240):format(webp)/cdn.vox-cdn.com/uploads/chorus_image/image/64062111/1_LGFTlvUlC8iJ5mi-UPF7Uw.0.0.0.jpeg"),
                new User(21, "Anush", "https://cdn.shortpixel.ai/client/to_webp,q_glossy,ret_img,w_225/https://www.mystudy.icu/wp-content/uploads/2019/12/images-1.jpg"),
                new User(21, "Ajith", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTJlEMVNi9PXtUuGqfgH9ckmdi8MWAEAfe2GQ&usqp=CAU"),
                new User(21, "Anusha", "https://www.adobe.com/content/dam/cc/us/en/products/creativecloud/stock/stock-riverflow1-720x522.jpg.img.jpg"),
                new User(21, "Ankith", "https://media3.s-nbcnews.com/j/newscms/2019_41/3047866/191010-japan-stalker-mc-1121_06b4c20bbf96a51dc8663f334404a899.fit-760w.JPG"),
                new User(22, "Mohan", "https://www.untumble.com/blog/wp-content/uploads/2017/04/Krishna.jpg"));
        */


        return userDetails;
    }
}