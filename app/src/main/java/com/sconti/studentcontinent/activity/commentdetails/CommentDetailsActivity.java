package com.sconti.studentcontinent.activity.commentdetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.commentdetails.adapter.CommentsListAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.CommentModel;
import com.sconti.studentcontinent.model.LikeDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.OutdoorCommentModel;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.COMMENT_NODE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_COMMENTS;

public class CommentDetailsActivity extends BaseActivity {
    private static final String TAG = "CommentDetailsActivity";
    private RecyclerView recyclerViewCommentor;
    private TextView mTextViewTitle;
    private CommentModel mCommentModel;
    private String type, mSubjectSelected;
    private DatabaseReference databaseReference;
    private List<CommentModel> commentModelList;
    private List<OutdoorCommentModel> OutdoorCommentModelList;
    private List<LikeDataModel> likeDatalist;

    private CommentsListAdapter mCommentsListAdapter;
    private ActionBar mActionBar;
    private boolean isOutdoorComment, isKnowledgeComment, isKnowledgeGeneralComment, isAspirationComment, isAspirationGeneralComment;
    private String postId;

    @Override
    protected int getContentView() {
        return R.layout.activity_comment_details;
    }

    @Override
    protected void initView() {
        mActionBar = getSupportActionBar();
        mTextViewTitle = findViewById(R.id.textViewTitle);
        recyclerViewCommentor = findViewById(R.id.recycler_viewComments);
    }

    @Override
    protected void initData() {
        mCommentModel = getIntent().getParcelableExtra("DATA");
        type = getIntent().getStringExtra("TYPE");

        mSubjectSelected = getIntent().getStringExtra("SUBJECT");
        isOutdoorComment = getIntent().getBooleanExtra("IS_OUTDOOR", false);
        isKnowledgeComment = getIntent().getBooleanExtra("KNOWLEDGE_COMMENT", false);
        isKnowledgeGeneralComment = getIntent().getBooleanExtra("KNOWLEDGE_COMMENT_GENERAL", false);

        isAspirationComment = getIntent().getBooleanExtra("ASPIRATION_COMMENT", false);
        isAspirationGeneralComment = getIntent().getBooleanExtra("ASPIRATION_COMMENT_GENERAL", false);


        postId = getIntent().getStringExtra("POST_ID");
        if(mCommentModel!=null)
        {
            mTextViewTitle.setText(mCommentModel.getDescription());
        }
        if(mActionBar!=null)
        {
            mActionBar.setTitle(mCommentModel.getDescription());
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(isKnowledgeComment)
        {
            getOutdoorComments();
        }
        else{
            getOutdoorComments();
        }

    }

    private void getOutdoorComments() {
        setUpNetwork();
        Call<NewAPIResponseModel> call = null;
        if(isKnowledgeComment)
        {
            call = apiInterface.GetCommentsByPostId(postId, 1);
        }
        else if(isKnowledgeGeneralComment)
        {
            call = apiInterface.GetCommentsByPostId(postId, 2);
        }
        else if(isAspirationComment)
        {
            call = apiInterface.GetCommentsByPostId(postId, 3);
        }
        else if(isAspirationGeneralComment)
        {
            call = apiInterface.GetCommentsByPostId(postId, 4);
        }
        else if(isOutdoorComment) {
            call = apiInterface.GetCommentsByPostId(postId, 5);
        }
        call.enqueue(new Callback<NewAPIResponseModel>() {
            @Override
            public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                commentModelList = response.body().getCommentModel();
                Collections.reverse(commentModelList);
                mCommentsListAdapter = new CommentsListAdapter(CommentDetailsActivity.this, commentModelList,likeDatalist ,selectItemInterface, type);
                recyclerViewCommentor.setLayoutManager(new LinearLayoutManager(CommentDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerViewCommentor.setAdapter(mCommentsListAdapter);
            }

            @Override
            public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });

    }
    protected ApiInterface apiInterface;
    private void setUpNetwork()
    {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }
    @Override
    protected void initListener() {

    }

    private CommentsListAdapter.SelectItem selectItemInterface = new CommentsListAdapter.SelectItem() {
        @Override
        public void selectedPosition(CommentModel commentModel) {
            //updateCommentCount(commentModel);
           // deleteComment(commentModel);
        }

        @Override
        public void selectedItemPosition(String position, String desc, String numberOfComments, String postBy, CommentModel commentModel) {

        }

        @Override
        public void updateCommentLikeCount(CommentModel dataModel, boolean isLiked) {

        }

       /* @Override
        public void deleteItem(CommentModel commentModel) {

        }*/
    };

    private void deleteComment(CommentModel commentModel) {
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(type).child(USER_COMMENTS).child(mSubjectSelected).child(commentModel.getId()).child(commentModel.getCommentID());
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d(TAG, "onComplete: deleted comment");
            }
        });
    }

    private void getCommentsDetails(){
        databaseReference = ContinentApplication.getFireBaseRef().child(type).child(USER_COMMENTS).child(mSubjectSelected).child(mCommentModel.getKey());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    commentModelList.add(snapshot.getValue(CommentModel.class));
                }
                Collections.reverse(commentModelList);
                mCommentsListAdapter = new CommentsListAdapter(CommentDetailsActivity.this, commentModelList, likeDatalist,selectItemInterface, type);
                recyclerViewCommentor.setLayoutManager(new LinearLayoutManager(CommentDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerViewCommentor.setAdapter(mCommentsListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void updateCommentCount(CommentModel commentModel)
    {
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child(type).child(mSubjectSelected);
        databaseReference = databaseReference.child(commentModel.getKey()).child(COMMENT_NODE);
        databaseReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() +-1);
                    long val = (Long) currentData.getValue();
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
}
