package com.sconti.studentcontinent.activity.othersprofile;

import com.sconti.studentcontinent.model.FollowModel;

public interface OthersProfilePresenter {
    void getProfileData(String userdId);
    void addFollower(FollowModel followModel);
    void UnFollower(FollowModel followModel);
    void getFollowers(String id);

}
