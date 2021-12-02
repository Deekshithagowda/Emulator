package com.sconti.studentcontinent.activity.profile.editProfile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sconti.studentcontinent.R;
import com.sconti.studentcontinent.activity.alltabs.SecondMainActivity;
import com.sconti.studentcontinent.activity.outdoor.model.OutDoorPostModel;
import com.sconti.studentcontinent.activity.post.PostActivity;
import com.sconti.studentcontinent.activity.profile.FollowersListShowActivity;
import com.sconti.studentcontinent.activity.profile.ProfileFragment;
import com.sconti.studentcontinent.activity.starterscreen.DegreeListAdapter;
import com.sconti.studentcontinent.base.BaseActivity;
import com.sconti.studentcontinent.model.APIResponseModel;
import com.sconti.studentcontinent.model.DegreeAspirationModel;
import com.sconti.studentcontinent.model.FirstPreferenceDataModel;
import com.sconti.studentcontinent.model.NewAPIResponseModel;
import com.sconti.studentcontinent.model.UserDetails;
import com.sconti.studentcontinent.network.ApiInterface;
import com.sconti.studentcontinent.network.RetrofitInstance;
import com.sconti.studentcontinent.utils.AppUtils;
import com.sconti.studentcontinent.utils.SharedPrefsHelper;
import com.sconti.studentcontinent.utils.tools.ContinentTools;
import com.sconti.studentcontinent.utils.tools.FilePath;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sconti.studentcontinent.activity.post.PostActivity.getRealPathFromUri;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.BANNER_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.COLLEGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.EMAIL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.MOBILE;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.PROFILE_URL;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_ASPIRATION_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_ID;
import static com.sconti.studentcontinent.utils.AppUtils.Constants.USER_IMAGE;

public class EditProfileActivity extends BaseActivity {
    private CircularProgressButton mUpdateProfileEditText;
    private ImageView mEditProfileImage, mImageBackgroundEditProfile;
    private EditText mUserNameEditProfile, mCollageNameEditProfile, mEMailAddressEditProfile, mPhoneNumberEditProfile;

    private static final int PICK_IMAGE_REQUEST = 777;
    private static final int REQUEST_CAMERA = 1200;
    private final int PICK_IMAGE_REQUEST_2 = 1111;
    private final int PICK_VIDEO_REQUEST_2 = 2222;
    private static final int SELECT_FILE = 1100;
    private static final int SELECT_VIDEO = 1300;
    private static final int REQUEST_CODE_DOC = 1400;
    private static final int RQS_RECORDING = 1500;
    private static final int VIDEO_CAPTURE = 1600;
    private static final int AUDIO_LOCAL = 1700;

    private boolean isProfilePicSelected = true;

    private Uri filePath;
    private String mImageURL ;
    private String name, imagePath ;
    private String type, mSelectedSubject;
    private boolean isVideoSelected;
    private List<UserDetails> userDetailsList;
    private List<String> degrees, aspirations;
    private DegreeListAdapter mDegreeListAdapter;
    private List<DegreeAspirationModel> degreeList, aspirationModelList;
    private Spinner mSpinnerEducation, mSpinnerAspiration;
    private UserDetails mUserDetails;
    private String selectedDegree, selectedAspiration, selectedAspirationName, selectedDegreeName;
    private String userChoosenTask , BannerUrl;
    private OutDoorPostModel mOutDoorPostModelData;


    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO,
    };


    @Override
    protected int getContentView() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void initView() {
        mImageBackgroundEditProfile = findViewById(R.id.ImageBackgroundEditProfile);
        mEditProfileImage = findViewById(R.id.EditProfileImage);
        mUserNameEditProfile = findViewById(R.id.UserNameEditProfile);
        mUpdateProfileEditText = findViewById(R.id.UpdateProfileEditText);
        mCollageNameEditProfile = findViewById(R.id.CollageNameEditProfile);
        mEMailAddressEditProfile = findViewById(R.id.EMailAddressEditProfile);
        mPhoneNumberEditProfile = findViewById(R.id.PhoneNumberEditProfile);
        mSpinnerEducation = findViewById(R.id.spinnerEducation);
        mSpinnerAspiration = findViewById(R.id.spinnerAspiration);
        setUpNetwork();
        getDegree();
        getAspiration();
    }

    @Override
    protected void initData() {
        setUpNetwork();
        name = SharedPrefsHelper.getInstance().get(NAME);
        BannerUrl = SharedPrefsHelper.getInstance().get(BANNER_URL);
        if (name != null)
            mUserNameEditProfile.setText(capitalizeWord(name));
        if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME) != null)
            //     mEductionEditProfile.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME).toString());
            if (SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME) != null)
                //  mAspirationEditProfile.setText(SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME).toString());
                if (SharedPrefsHelper.getInstance().get(COLLEGE_NAME) != null)
                    mCollageNameEditProfile.setText(capitalizeWord(SharedPrefsHelper.getInstance().get(COLLEGE_NAME).toString()));
        if (SharedPrefsHelper.getInstance().get(EMAIL) != null && !SharedPrefsHelper.getInstance().get(EMAIL).toString().isEmpty())
            mEMailAddressEditProfile.setText(SharedPrefsHelper.getInstance().get(EMAIL).toString());
        if (SharedPrefsHelper.getInstance().get(MOBILE) != null)
            mPhoneNumberEditProfile.setText(SharedPrefsHelper.getInstance().get(MOBILE).toString());
        if(SharedPrefsHelper.getInstance().get(PROFILE_URL)!=null)
        {
            Glide.with(context).load(SharedPrefsHelper.getInstance().get(PROFILE_URL).toString()).placeholder(R.drawable.ic_user_profile).into(mEditProfileImage);
//            Picasso.with(this).load(SharedPrefsHelper.getInstance().get(PROFILE_URL).toString()).into(mEditProfileImage);
        }
        if(BannerUrl!=null && !BannerUrl.isEmpty())
            Glide.with(EditProfileActivity.this).load(BannerUrl).into(mImageBackgroundEditProfile);

    }

    @Override
    protected void initListener() {
        mEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // addPhoto();
                isProfilePicSelected = true;
                chooseImage();
            }
        });




        mUpdateProfileEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        mImageBackgroundEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfilePicSelected = false;
                chooseImage();
            }
        });

        mSpinnerEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDegree = degreeList.get(position).getId();
                selectedDegreeName = degreeList.get(position).getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerAspiration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAspiration = aspirationModelList.get(position).getId();
                selectedAspirationName = aspirationModelList.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    FirstPreferenceDataModel firstPreferenceDataModel ;
    UserDetails userDetails;
    private void submit() {
            userDetails = new UserDetails();
            userDetails.setPhone(SharedPrefsHelper.getInstance().get(MOBILE).toString());
            userDetails.setDegree(selectedDegree);
            userDetails.setInterest(selectedAspiration);
            firstPreferenceDataModel = new FirstPreferenceDataModel();

            if (mEMailAddressEditProfile.getText() != null && !mEMailAddressEditProfile.getText().toString().isEmpty()) {
                userDetails.setEmail(mEMailAddressEditProfile.getText().toString());
            } else {
                AppUtils.showToastMessage(EditProfileActivity.this, getString(R.string.email_correct));
                return;
            }
            if (mUserNameEditProfile.getText() != null && !mUserNameEditProfile.getText().toString().isEmpty()) {
                userDetails.setName(mUserNameEditProfile.getText().toString());
            } else {
                AppUtils.showToastMessage(EditProfileActivity.this, getString(R.string.name_required));
                return;
            }

            if (mCollageNameEditProfile.getText() != null && !mCollageNameEditProfile.getText().toString().isEmpty()) {
                userDetails.setVillage(mCollageNameEditProfile.getText().toString());
            } else {
                AppUtils.showToastMessage(EditProfileActivity.this, getString(R.string.college_name));
                return;
            }

            userDetails.setUserId(SharedPrefsHelper.getInstance().get(USER_ID).toString());

            Call<NewAPIResponseModel> call = apiInterface.ProfileUpdate(userDetails);
            call.enqueue(new Callback<NewAPIResponseModel>() {
                @Override
                public void onResponse(Call<NewAPIResponseModel> call, Response<NewAPIResponseModel> response) {
                    if (response.body().isStatus()) {
                        try {
                            SharedPrefsHelper.getInstance().save(NAME, userDetails.getName());
                            SharedPrefsHelper.getInstance().save(EMAIL, userDetails.getEmail());
                            int pos = degrees.indexOf(selectedDegreeName);
                            String deg = degreeList.get(pos).getId();
                            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE, deg);


                            String degName = degreeList.get(degrees.indexOf(selectedDegreeName)).getName();
                            SharedPrefsHelper.getInstance().save(AppUtils.Constants.SELECTED_SUBJECT_KNOWLEDGE_NAME, degName);

                            String asp = aspirationModelList.get(aspirations.indexOf(selectedAspirationName)).getId();
                            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION, asp);

                            String aspName = aspirationModelList.get(aspirations.indexOf(selectedAspirationName)).getName();
                            SharedPrefsHelper.getInstance().save(SELECTED_SUBJECT_ASPIRATION_NAME, aspName);
                            SharedPrefsHelper.getInstance().save(COLLEGE_NAME, userDetails.getVillage());
                            Toast.makeText(EditProfileActivity.this, "Update", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<NewAPIResponseModel> call, Throwable t) {

                }
            });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


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
            if(requestCode==PICK_IMAGE_REQUEST){
                uploadImage(filePath);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    if(isProfilePicSelected){
                        mEditProfileImage.setImageBitmap(bitmap);
                    }else {
                        mImageBackgroundEditProfile.setImageBitmap(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


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
              //  uploadImage(Uri.fromFile(mediaFile));
                // uploadImageToAWS(mediaFile, "VIDEO");
            }
        }
    }


    private void onSelectFromGalleryResult(Intent data, String mediaType) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Uri path = data.getData();
        File original;
        if (path != null) {
            if (path.toString().contains("com.google.android.apps.photos")) {
                Log.d(TAG,"From android photos ");
                String filePath = FilePath.getPathFromInputStreamUri(EditProfileActivity.this, path);

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
                String filePath = getRealPathFromUri(EditProfileActivity.this, path);
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
                        EditProfileActivity.this,
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

    protected ApiInterface apiInterface;

    private void setUpNetwork() {
        RetrofitInstance retrofitInstance = new RetrofitInstance();
        retrofitInstance.setClient();
        apiInterface = retrofitInstance.getClient().create(ApiInterface.class);
    }

    private void getDegree() {
        //  mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> modelCall = apiInterface.getDegree();
        modelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                // mLinearLayoutProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getDegreeAspirationModelList().size() > 0) {
                    degrees = new ArrayList<>();
                    degreeList = new ArrayList<>();
                    degreeList = response.body().getDegreeAspirationModelList();
                    for (int i = 0; i < response.body().getDegreeAspirationModelList().size(); i++) {
                        degrees.add(response.body().getDegreeAspirationModelList().get(i).getName());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_item, degrees);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSpinnerEducation.setAdapter(dataAdapter);
                    String selectedEducation = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_KNOWLEDGE_NAME);
                    if (selectedEducation != null) {
                        mSpinnerEducation.setSelection(degrees.indexOf(selectedEducation));
                    }
                }

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                //  mLinearLayoutProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(EditProfileActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }

    private void getAspiration() {
        //  mLinearLayoutProgressBar.setVisibility(View.VISIBLE);
        Call<APIResponseModel> modelCall = apiInterface.getAspiration();
        modelCall.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                // mLinearLayoutProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getDegreeAspirationModelList().size() > 0) {
                    aspirations = new ArrayList<>();
                    aspirationModelList = new ArrayList<>();
                    aspirationModelList = response.body().getDegreeAspirationModelList();
                    for (int i = 0; i < response.body().getDegreeAspirationModelList().size(); i++) {
                        aspirations.add(response.body().getDegreeAspirationModelList().get(i).getName());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_item, aspirations);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSpinnerAspiration.setAdapter(dataAdapter);
                    String selectedEducation = SharedPrefsHelper.getInstance().get(SELECTED_SUBJECT_ASPIRATION_NAME);
                    if (selectedEducation != null) {
                        mSpinnerAspiration.setSelection(aspirations.indexOf(selectedEducation));
                    }
                }

            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {
                //  mLinearLayoutProgressBar.setVisibility(View.GONE);
                AppUtils.showToastMessage(EditProfileActivity.this, getString(R.string.some_thing_wrong));
            }
        });
    }

    public void setItemSelected(View view) {
        try {
            View rowView = view;
            TextView tv = rowView.findViewById(R.id.textViewLanguage);
            tv.setTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPhoto() {
        if (!ContinentTools.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Attachment!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean gallery = ContinentTools.checkPermission(EditProfileActivity.this);
                boolean camera = ContinentTools.checkPermissionCamera(EditProfileActivity.this);
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

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    public void crop_ImageAndUpload(File original, String extension_file, String mediaType){
        try {
            //change the filepath
            Bitmap d = new BitmapDrawable(getResources(), original.getPath()).getBitmap();
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

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private void uploadImage(Uri filePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = null;
            if(isProfilePicSelected){
                ref = storageReference.child("images").child(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            }else {
                ref = storageReference.child("images").child("ProfileBanner").child(SharedPrefsHelper.getInstance().get(USER_ID).toString());
            }

            //UploadTask uploadTask =
            final StorageReference finalRef = ref;
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            finalRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    {
                                        String url = downloadUrl.toString();
                                        if(isVideoSelected)
                                        {
                                       //     mVideoURL = url;
                                        }
                                        else{
                                            mImageURL = url;
                                            updateProfileURL(url);
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

                            Toast.makeText(EditProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateProfileURL(final String url) {
        String userId = SharedPrefsHelper.getInstance().get(USER_ID);
        Call<APIResponseModel> call = null;
        if(isProfilePicSelected){
            call =  apiInterface.updateAvatar(userId, url);
        }else {
            call =  apiInterface.updateBanner(userId, url);
        }
        call.enqueue(new Callback<APIResponseModel>() {
            @Override
            public void onResponse(Call<APIResponseModel> call, Response<APIResponseModel> response) {
                if(response.body().isStatus())
                {
                    if (isProfilePicSelected){
                        SharedPrefsHelper.getInstance().save(PROFILE_URL, url);
                    }else {
                        SharedPrefsHelper.getInstance().save(BANNER_URL, url);
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<APIResponseModel> call, Throwable t) {

            }
        });
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


}