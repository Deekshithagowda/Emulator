package com.sconti.studentcontinent.activity.othersprofile;

import com.sconti.studentcontinent.model.FollowModel;
import com.sconti.studentcontinent.model.UserDetails;

import java.util.List;

public interface OthersProfileView {
    void updateProfileData(UserDetails userDetails);
    void showProgressbar();
    void hideProgressbar();
    void updateFollowers(int IamFollowing, int IhaveFollowers, List<FollowModel> followingModel, List<FollowModel> body);
    void updateAddedFollowers(boolean val);
}
