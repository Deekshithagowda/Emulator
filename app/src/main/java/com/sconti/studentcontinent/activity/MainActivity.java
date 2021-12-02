package com.sconti.studentcontinent.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.ContinentApplication;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.knowledge.KnowledgeTabActivity;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.FIRST_TIME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private TextView mTextViewName, mTextViewEmail;
    private String mEmail, mUserName;
    private String mAvatar;
    private CircleImageView mImageViewAvatar;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private boolean isVideoSelected;
    private AlertDialog mAlertDialogFocusSelection;
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


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        if(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE, null)!=null)
        {
            startActivity(new Intent(this, KnowledgeTabActivity.class));
        }
       // else
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mTextViewName = headerView.findViewById(R.id.textViewName);
        mTextViewEmail = headerView.findViewById(R.id.textViewEmail);
        mImageViewAvatar = headerView.findViewById(R.id.imageViewAvatar);
        mUserName = SharedPrefsHelper.getInstance().get(NAME);
        mEmail = SharedPrefsHelper.getInstance().get(EMAIL);
        mTextViewName.setText(mUserName);
        mTextViewEmail.setText(mEmail);
        // Passing each menu ID as outdooricon set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void initData() {
       String prfurl =  SharedPrefsHelper.getInstance().get(PROFILE_URL, null);
       if(prfurl!=null && prfurl.length()>10)
       {
           Picasso.with(MainActivity.this).load(prfurl).into(mImageViewAvatar);
       }
       else
       {
           mImageViewAvatar.setImageResource(R.mipmap.ic_launcher);
       }
    }

    @Override
    protected void initListener() {
        mImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                SharedPrefsHelper.getInstance().clearAllData();
                LoginManager.getInstance().logOut();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                //startActivity(new Intent(this, SplashActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void addPhoto() {
        if (!ContinentTools.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }
    String userChoosenTask;
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(MainActivity.this);
                boolean camera = ContinentTools.checkPermissionCamera(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (camera) {
                        //cameraIntent();
                        chooseImage();
                    }
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

    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
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
           /* else if(requestCode==PICK_VIDEO_REQUEST_2) {
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
            }*/
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
               // uploadImage(Uri.fromFile(mediaFile));
                // uploadImageToAWS(mediaFile, "VIDEO");
            }
        }
    }
    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String userId = SharedPrefsHelper.getInstance().get(USER_ID);
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images").child(PROFILE).child(userId);
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

                                        updateProfile(url);

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

                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateProfile(String url) {
        SharedPrefsHelper.getInstance().save(PROFILE_URL, url);
        Picasso.with(MainActivity.this).load(url).into(mImageViewAvatar);
        String uid = SharedPrefsHelper.getInstance().get(USER_ID);
        DatabaseReference databaseReference = ContinentApplication.getFireBaseRef().child("users").child("profile").child(uid).child("profileURL");
        databaseReference.setValue(url);

    }

}
