package com.sconti.studentcontinent.activity.games.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.details.DetailsActivity;
import com.sconti.studentcontinent.activity.knowledge.fragments.FirstPreferenceViewModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.adapter.GameFirstPrefAdapter;
import com.sconti.studentcontinent.adapter.KnowledgeFirstPrefAdapter;
import com.sconti.studentcontinent.base.BaseFragment;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.SubjectListModel;
import com.sconti.studentcontinent.model.UserDataModel;
import com.sconti.studentcontinent.model.UserResponseModel;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.FilePath;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.GAMES_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.GAMES_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.KNOWLEDGE_FIRST;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_GAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SUBJECT_LIST_GAMES;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_FEEDS;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_RESPONSES;

public class FirstPreferenceFragment extends BaseFragment implements GameFirstPrefAdapter.SelectItem {
    private static final String TAG = "FirstPreferenceFragment";
    private FirstPreferenceViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private KnowledgeFirstPrefAdapter firstPrefAdapter;
    private View mRootView;
    private EditText mEditTextTitle, mEditTextDescription;
    private String mImageURL, mVideoURL;
    private Button mButtonSubmit;
    private ImageView mImageViewAttachment, mImageViewVideoAttachment;
    private DatabaseReference databaseReference;
    private List<FirstPreferenceDataModel> preferenceDataModelList;
    private UserDataModel mUserDataModel;
    private String mSelectedSubject;
    private List<SubjectListModel> mSubjectList;
    private LinearLayout mLinearLayoutMessage;
    private View focusSelection;
    private RadioGroup radioGroup;
    private Button mButtonSelection;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private boolean isVideoSelected;
    private AlertDialog mAlertDialogFocusSelection;
    private RelativeLayout mRelativeLayout;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO,
    };

    private static final int SELECT_FILE = 1100;
    private static final int REQUEST_CAMERA = 1200;
    private static final int SELECT_VIDEO = 1300;
    private static final int REQUEST_CODE_DOC = 1400;
    private static final int RQS_RECORDING = 1500;
    private static final int VIDEO_CAPTURE = 1600;
    private static final int AUDIO_LOCAL = 1700;


    public static FirstPreferenceFragment newInstance() {
        return new FirstPreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.first_preference_fragment, container, false);
        initViews();
        initListener();
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            mSelectedSubject = bundle.getString("SELECTED");
        }
        initData();
        if (mSelectedSubject != null) {
            focusSelection.setVisibility(View.GONE);
            getData();
        } else {
            //focusSelection.setVisibility(View.VISIBLE);
            mLinearLayoutMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            getSubjectList();
        }
        return mRootView;
    }

    private void getSubjectList() {
        databaseReference = ContinentApplication.getFireBaseRef();
        databaseReference = databaseReference.child(SUBJECT_LIST_GAMES);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSubjectList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mSubjectList.add(snapshot.getValue(SubjectListModel.class));
                }
                focusSelectionAlertList(mSubjectList);
                // mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void focusSelectionAlertList(final List<SubjectListModel> mSubjectList)
    {
        if(mActivity!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
            final View view = inflater.inflate(R.layout.focus_select_lyt, null);
            builder.setView(view);
            radioGroup = view.findViewById(R.id.radiogroup);
            for (int i = 0; i < mSubjectList.size(); i++) {
                RadioButton rdbtn = new RadioButton(mActivity);
                rdbtn.setId(View.generateViewId());
               /* if(i==0)
                {
                    rdbtn.setChecked(true);
                    mSelectedSubject = mSubjectList.get(i).getKey();
                    SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT,mSelectedSubject);
                }*/
                rdbtn.setText(mSubjectList.get(i).getName());
                radioGroup.addView(rdbtn);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int idRadioButtonChosen = radioGroup.getCheckedRadioButtonId();
                    if (idRadioButtonChosen > 0) {
                        RadioButton radioButtonChosen = view.findViewById(idRadioButtonChosen);
                        mSelectedSubject = mSubjectList.get(idRadioButtonChosen - 1).getKey();
                        SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_GAME, mSelectedSubject);
                    } else {
                        Toast.makeText(mActivity, "Select Subject", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mButtonSelection = view.findViewById(R.id.buttonSelected);
            mButtonSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // focusSelection.setVisibility(View.GONE);
                    mAlertDialogFocusSelection.dismiss();
                    getData();
                }
            });
            mAlertDialogFocusSelection = builder.create();
            mAlertDialogFocusSelection.show();
        }
    }

    private void initData() {
        mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_GAME, null);
        mUserDataModel = new UserDataModel();
        mUserDataModel.setEmail(SharedPrefsHelper.getInstance().get(EMAIL, ""));
        mUserDataModel.setMobile(SharedPrefsHelper.getInstance().get(MOBILE, ""));
        mUserDataModel.setName(SharedPrefsHelper.getInstance().get(NAME, ""));
    }

    private KnowledgeFirstPrefAdapter.SelectItem selectItemInterface = new KnowledgeFirstPrefAdapter.SelectItem() {
        @Override
        public void selectedPosition(int position) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("DATA", preferenceDataModelList.get(position));
            intent.putExtra("TYPE", GAMES_FIRST);
            intent.putExtra("SUBJECT", mSelectedSubject);
            mActivity.startActivity(intent);
        }


        @Override
        public void openAlert(FirstPreferenceDataModel dataModel) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.more_alert, null);
            TextView report, copyLink, share, mute, onNotification, unfollow;
            report = dialogView.findViewById(R.id.Report);
            copyLink = dialogView.findViewById(R.id.copy_link);
            share = dialogView.findViewById(R.id.share_to);
            mute = dialogView.findViewById(R.id.mute);
            onNotification = dialogView.findViewById(R.id.turn_on_post_noti);
            unfollow = dialogView.findViewById(R.id.unfollow);

            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mActivity, "Report", Toast.LENGTH_LONG).show();
                }
            });

            alertBuilder.setView(dialogView);
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }

        @Override
        public void reachedLastItem() {
            getMoreData();
        }
    };

    private FirstPreferenceDataModel lastItemInQuery;
    private void getData() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        // mLinearLayoutMessage.setVisibility(View.VISIBLE);
        databaseReference = ContinentApplication.getFireBaseRef();
        Query imagesQuery = databaseReference.child(GAMES_FIRST).child(mSelectedSubject).orderByKey().limitToLast(AppUtils.Constants.QUERY_LIMIT);
        databaseReference = databaseReference.child(GAMES_FIRST).child(mSelectedSubject);
        imagesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preferenceDataModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    preferenceDataModelList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                }
                getLikeDetails();

                mRecyclerView.setVisibility(View.VISIBLE);
                Collections.reverse(preferenceDataModelList);
                if(preferenceDataModelList.size()==AppUtils.Constants.QUERY_LIMIT)
                {
                    lastItemInQuery = preferenceDataModelList.remove(preferenceDataModelList.size()-1);
                }

                firstPrefAdapter = new KnowledgeFirstPrefAdapter(mActivity, preferenceDataModelList,selectItemInterface, null, GAMES_FIRST);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setAdapter(firstPrefAdapter);
                mRelativeLayout.setVisibility(View.GONE);

                // mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mRelativeLayout.setVisibility(View.GONE);
            }
        });
    }

    private boolean isLoadingMoreData;
    private List<FirstPreferenceDataModel> moreDataList;
    private void getMoreData() {
        if(!isLoadingMoreData && lastItemInQuery!=null) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            // mLinearLayoutMessage.setVisibility(View.VISIBLE);
            Log.d(TAG, "getMoreData: ");
            databaseReference = ContinentApplication.getFireBaseRef();
            String mLastKey = lastItemInQuery.getKey();
            isLoadingMoreData = true;
            Query imagesQuery = databaseReference.child(GAMES_FIRST).child(mSelectedSubject).orderByKey().endAt(mLastKey).limitToLast(AppUtils.Constants.QUERY_LIMIT);
            //  databaseReference = databaseReference.child(GAMES_FIRST).child(mSelectedSubject);
            imagesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Collections.reverse(preferenceDataModelList);
                    moreDataList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        moreDataList.add(snapshot.getValue(FirstPreferenceDataModel.class));
                        //preferenceDataModelList.add(preferenceDataModelList.size(),snapshot.getValue(FirstPreferenceDataModel.class));
                    }
                    getLikeDetails();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(moreDataList);
                    if (moreDataList.size() == AppUtils.Constants.QUERY_LIMIT) {
                        lastItemInQuery = moreDataList.remove(moreDataList.size() - 1);
                    }
                    else
                    {
                        lastItemInQuery = null;
                    }
                    preferenceDataModelList.addAll(moreDataList);

                    firstPrefAdapter = new KnowledgeFirstPrefAdapter(mActivity, preferenceDataModelList, selectItemInterface, null, GAMES_FIRST);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                    mRecyclerView.setAdapter(firstPrefAdapter);
                    isLoadingMoreData = false;
                    mRelativeLayout.setVisibility(View.GONE);


                    // mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mRelativeLayout.setVisibility(View.GONE);

                }
            });
        }
    }

    private void getLikeDetails(){
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        DatabaseReference databaseReference1 = ContinentApplication.getFireBaseRef().child(GAMES_FIRST).child(USER_RESPONSES).child(USER_FEEDS).child(mSelectedSubject).child(uid);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserResponseModel> userResponseModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userResponseModelList.add(snapshot.getValue(UserResponseModel.class));
                }
                if(userResponseModelList!=null && userResponseModelList.size()>0){
                    for(int i = 0; i<userResponseModelList.size(); i++){
                        for (int j=0; j<preferenceDataModelList.size(); j++){
                            if( userResponseModelList.get(i).getId().equalsIgnoreCase(preferenceDataModelList.get(j).getId()) ){
                                preferenceDataModelList.get(j).setCommented(userResponseModelList.get(i).isCommented());
                                preferenceDataModelList.get(j).setLiked(userResponseModelList.get(i).isLiked());
                                preferenceDataModelList.get(j).setShared(userResponseModelList.get(i).isShared());
                            }
                        }
                    }
                }
                firstPrefAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initListener() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = mEditTextDescription.getText().toString().trim();
                if(desc!=null && desc.length()>2)
                {
                    submitData();
                }
                else{
                    Toast.makeText(getContext(), "Please write description", Toast.LENGTH_LONG).show();
                }
            }
        });
        mImageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = false;
                addPhoto();
            }
        });

        mImageViewVideoAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideoSelected = true;
                addVideo();
            }
        });
       /* mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("DATA", preferenceDataModelList.get(position));
                       // startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
    }

    private FloatingActionButton floatingActionButton;

    private void initViews() {
        focusSelection = mRootView.findViewById(R.id.focus_select_view);
        mLinearLayoutMessage = mRootView.findViewById(R.id.linearlayoutEditMessage);
        mEditTextTitle = mRootView.findViewById(R.id.editTextTitle);
        mEditTextDescription = mRootView.findViewById(R.id.editTextDetails);
        mButtonSubmit = mRootView.findViewById(R.id.submitButton);
        mImageViewAttachment = mRootView.findViewById(R.id.imageViewAttach);
        mImageViewVideoAttachment = mRootView.findViewById(R.id.videoAttach);
        mRecyclerView = mRootView.findViewById(R.id.recycler_view);
        mRelativeLayout = mRootView.findViewById(R.id.progress_bar);
       /* floatingActionButton = mRootView.findViewById(R.id.fabAddPost);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
               // mSelectedSubject = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_GAME, null);
                Intent intent = new Intent(mActivity, PostActivity.class);
                intent.putExtra("TYPE", GAMES_FIRST);
                intent.putExtra("FAV", mSelectedSubject);
                startActivity(intent);
            }
        });*/

    }

    private void submitData() {
        String prfurl =  SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
        FirstPreferenceDataModel firstPreferenceDataModel;
        firstPreferenceDataModel = new FirstPreferenceDataModel();
        firstPreferenceDataModel.setTitle(mEditTextTitle.getText().toString());
        firstPreferenceDataModel.setDescription(mEditTextDescription.getText().toString());
        firstPreferenceDataModel.setDatetime(System.currentTimeMillis());
        firstPreferenceDataModel.setImageURL(mImageURL);
        firstPreferenceDataModel.setVideoURL(mVideoURL);
        String uid = SharedPrefsHelper.getInstance().get(USER_ID, null);
        firstPreferenceDataModel.setPostedBy(uid);
        databaseReference = ContinentApplication.getFireBaseRef().child(GAMES_FIRST).child(mSelectedSubject);
        String key = databaseReference.push().getKey();
        firstPreferenceDataModel.setId(key);
        firstPreferenceDataModel.setKey(key);
        firstPreferenceDataModel.setPosterUrl(prfurl);
        databaseReference.child(key).setValue(firstPreferenceDataModel);
        mEditTextDescription.setText("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FirstPreferenceViewModel.class);
    }

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
            filePath = data.getData();
        // imageViewUploaded.setImageURI(filePath);
        // imageViewUploaded.setVisibility(View.VISIBLE);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            // imageViewUploaded.setImageURI(filePath);
            // imageViewUploaded.setVisibility(View.VISIBLE);
            //   if(filePath!=null)
            //  uploadImage();
        }
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode==PICK_IMAGE_REQUEST_2){
                uploadImage(filePath);
            }
            else if(requestCode==PICK_VIDEO_REQUEST_2) {
                uploadImage(filePath);
            }
            else if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data, "IMAGE");
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == SELECT_VIDEO)
                onSelectFromGalleryResult(data, "VIDEO");
            else if (requestCode == REQUEST_CODE_DOC) {
                onSelectFromGalleryResult(data, "ALL");
            }else if(requestCode==AUDIO_LOCAL){
                onSelectFromGalleryResult(data, "AUDIO");
            }
            else if (requestCode == RQS_RECORDING) {
                String result = data.getStringExtra("result");
                if(result!=null){
                    // uploadImageToAWS(new File(result), "AUDIO");
                }
                else {

                }
            } else if (requestCode == VIDEO_CAPTURE) {
                Log.d(TAG, "onActivityResult: ");
                //onSelectFromGalleryResult(data, "VIDEO");
                //  isVideoCaptured = true;
                uploadImage(Uri.fromFile(mediaFile));
                // uploadImageToAWS(mediaFile, "VIDEO");
            }
        }
    }
    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images").child(GAMES_FIRST).child(mSelectedSubject).child(UUID.randomUUID().toString());
            //UploadTask uploadTask =
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    {
                                        String url = downloadUrl.toString();
                                        if(isVideoSelected)
                                        {
                                            mVideoURL = url;
                                        }
                                        else{
                                            mImageURL = url;
                                        }

                                        /*if(url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png"))
                                        {
                                            mImageURL = url;
                                        }
                                        else if(url.contains(".mp4") ) {
                                            mVideoURL = url;
                                        }*/

                                        //  textViewURL.setText(downloadUrl.toString());
                                    }
                                    //imageViewUploaded.setVisibility(View.VISIBLE);
                                }
                            });

                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }
    }

    private void addPhoto() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void addVideo() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectVideo();
    }

    private void addDocument() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        //   getDocument();
    }

    private void addAudio() {
        if (!ContinentTools.hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL);
        }
        selectAudio();
    }
    private String userChoosenTask;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (camera)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery)
                    {
                        chooseImage();
                        //galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_2);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST_2);
    }

    private void selectVideo() {
        final CharSequence[] items = {"Capture Video", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Capture Video")) {
                    userChoosenTask = "Capture Video";
                    if (hasCamera()) {
                        videoCaptureIntent();
                    } else {
                        ContinentTools.showToast("No Camera Found");
                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (gallery)
                    {
                        chooseVideo();
                        //videoFromGalleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void selectAudio() {
        final CharSequence[] items = {"Record Audio", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(mActivity);
                boolean camera = ContinentTools.checkPermissionCamera(mActivity);
                if (items[item].equals("Record Audio")) {
                    userChoosenTask = "Record Audio";
                    audioIntent();
                    //recordingAlert();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    AudioLocalIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private boolean hasCamera() {
        if (mActivity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void videoFromGalleryIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            //return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra("return-data", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, "Select outdooricon Video "), SELECT_VIDEO);
    }
    private File mediaFile;

    private void videoCaptureIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            // return;
        }
        mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri videoUri = Uri.fromFile(mediaFile);
        /*if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", mediaFile);
        }*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        //intent.putExtra(EXTRA_VIDEO_QUALITY, 0);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivityForResult(intent, VIDEO_CAPTURE);
        }
    }

    private void audioIntent() {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            //  return;
        }
        //Intent intent = new Intent(mActivity, AudioRecordActivity.class);
        //startActivityForResult(intent, RQS_RECORDING);
    }
    private void AudioLocalIntent()
    {
        if (!ContinentTools.checkInternetStatus()) {
            ContinentTools.showToast("No Internet connected");
            // return;
        }
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,AUDIO_LOCAL);
    }

    private void onSelectFromGalleryResult(Intent data, String mediaType) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Uri path = data.getData();
        File original;
        if (path != null) {
            if (path.toString().contains("com.google.android.apps.photos")) {
                Log.d(TAG,"From android photos ");
                String filePath = FilePath.getPathFromInputStreamUri(mActivity, path);

                original = new File(filePath);
                String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                    crop_ImageAndUpload(original,extension_file,mediaType);
                }else {
                    //uploadImageToAWS(new File(filePath), mediaType);
                    uploadImage(Uri.fromFile(original));
                }
                //OustSdkTools.showToast("can't select attachment from google photos app");
                //return;
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Log.d(TAG,"from SDK more than Kitkat");
                String filePath = getRealPathFromUri(mActivity, path);
                if (filePath != null) {
                    original = new File(filePath);
                    uploadImage(Uri.parse(filePath));
                  /*  String extension_file = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf("."));
                    if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                        crop_ImageAndUpload(original,extension_file,mediaType);
                    }else {
                        uploadImage(Uri.parse(filePath));
                        //uploadImageToAWS(new File(filePath), mediaType);
                    }*/
                } else {
                    ContinentTools.showToast("unable to get file");
                }
            } else {

                String[] proj = {MediaStore.Images.Media.DATA};
                String result = null;

                CursorLoader cursorLoader = new CursorLoader(
                        mActivity,
                        path, proj, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    result = cursor.getString(column_index);
                    if (result != null) {
                        uploadImage(Uri.parse(result));
                        //uploadImageToAWS(new File(result), mediaType);
                    }
                }
            }
        }
    }

    private void storeImage(Bitmap image, File pictureFile) {
        //File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public void crop_ImageAndUpload(File original, String extension_file, String mediaType){
        try {
            //change the filepath
            Bitmap d = new BitmapDrawable(mActivity.getResources(), original.getPath()).getBitmap();
            int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
            Bitmap bitmap_new = Bitmap.createScaledBitmap(d, 512, nh, true);
            Log.d(TAG, "original:" + d.getByteCount() + " -- duplicate:" + bitmap_new.getByteCount());
            //Log.d(TAG, "Bitmap width:" + bitmap_new.getWidth() + " -- height:" + bitmap_new.getHeight());

            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "" + extension_file);
            // storeImage(bitmap_new, destination);

            Log.d(TAG, "file size  duplicate:" + destination.length() + " -- Original:" + original.length());
            // uploadImageToAWS(destination, mediaType);
            uploadImage(Uri.fromFile(original));
        }catch (Exception e){
            e.printStackTrace();
            uploadImage(Uri.fromFile(original));
            //  uploadImageToAWS(original, mediaType);
            //Toast.makeText(this,"Couldn't able to load the image. Please try again.",Toast.LENGTH_LONG).show();
        }
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (destination != null) {
            String extension_file = destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("."));
            if(extension_file.equalsIgnoreCase(".jpg") || extension_file.equalsIgnoreCase(".jpeg") || extension_file.equalsIgnoreCase(".png")) {
                crop_ImageAndUpload(destination,extension_file,"IMAGE");
            }else {
                uploadImage(Uri.fromFile(destination));
                // uploadImageToAWS(destination, "IMAGE");
            }

        }
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {


        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    @Override
    public void selectedPosition(int position) {
        Log.d(TAG, "selectedPosition: "+position);
    }

    @Override
    public void openAlert(FirstPreferenceDataModel dataModel) {

    }

    @Override
    public void reachedLastItem() {

    }
}
