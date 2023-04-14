package com.example.testdisasterevent.ui.report;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.algorithms.TokenBucketAlgorithm;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.google.android.gms.maps.model.LatLng;
import com.example.testdisasterevent.data.model.ReportFromCitizen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;
    public ReportFromCitizen reportData = new ReportFromCitizen();
    public int AccountType;
    public String AccountUID;
    private ImageView cameraIcon;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ImageView mapIcon;

    private Uri mImageUri;
    private StorageReference mStroageRef;
    private DatabaseReference mDatabaseRef;

    public TokenBucketAlgorithm tokenBucketAlgorithm;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /**
         * AccountType
         * 0 Citizen
         * 1 Garda
         * 2 Doctor
         * 3 Fireman
         *
         *
         * */
        MainActivity mainActivity = (MainActivity) getActivity();
        AccountUserInfo accountUserInfoData = mainActivity.getAccountUserInfo();
        if (accountUserInfoData != null) {
            AccountType=accountUserInfoData.getUserTypeID();
            AccountUID=Long.toString(accountUserInfoData.getUid());
        }

        reportData.setDisasterType("0");

    }


    public View onCreateView( LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Boolean isChosen=false;
        View rootView = inflater.inflate(R.layout.fragment_report_garda, container, false);

        if(AccountType!=1)
            rootView = inflater.inflate(R.layout.fragment_report, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            isChosen=bundle.getBoolean("isChosen");

            LatLng location = new LatLng(bundle.getDouble("Latitude"), bundle.getDouble("Longitude"));
            //int reportType = bundle.getInt("Type");
            int radius = bundle.getInt("Radius");
            String locName=bundle.getString("locName");

            Log.d("Longitude",Double.toString(location.longitude));
            Log.d("Latitude", Double.toString(location.latitude));
            Log.d("Location", locName);

            Log.d("Radius", Integer.toString(radius));
            reportData.setLatitude((float)location.latitude);
            reportData.setLongitude((float)location.longitude);
            reportData.setRadius(radius);
            reportData.setLocation(locName);
            //mapIcon.setImageResource(R.drawable.firstaid);
        }

//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST_CODE);
//        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain why the permission is needed
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("This permission is required to access your photos");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Request the permission again
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE);
                    }
                });
                builder.create().show();
            } else {
                // No explanation needed, request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }

        /**
         * init the token bukect
         */
        tokenBucketAlgorithm = new TokenBucketAlgorithm(0);

        /**
         * disaster single choice click
         * */
        RadioGroup disasterChosen=rootView.findViewById(R.id.report_radioGroup);
        disasterChosen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup disasterChosen, int checkedId) {
                int selectedId = disasterChosen.getCheckedRadioButtonId();
                if (selectedId == R.id.report_fire) {
                    reportData.setDisasterType("fire");
                    Log.d("Button click", "fire clicked!");
                } else if (selectedId == R.id.report_water) {
                    reportData.setDisasterType("water");
                    Log.d("Button click", "water clicked!");
                } else if (selectedId == R.id.report_otherevent) {
                    reportData.setDisasterType("other");
                    Log.d("Button click", "other clicked!");
                }
            }
        });


        /**
         * map Button click
         * default mock geo info and radius
         * ----------
         * for yilun to combine interface
         * */



        mapIcon = rootView.findViewById(R.id.report_mark_map_icon);
        if(isChosen){
            mapIcon.setBackgroundResource(R.drawable.report_location_check);
        }
        mapIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){

                Log.d("Button click", "map clicked!");
                int index = rootView.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("data_key", index);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment reportMapFragment = new ReportMapFragment();
                reportMapFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.report_container, reportMapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });

        if(AccountType!=1){
            cameraIcon = rootView.findViewById(R.id.report_camera);
            cameraIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View rootView){
                    Log.d("Button click", "camera clicked!");



                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);



                }
            });
        }
        /**
         * camera Button click
         * ---------
         * to do
         * get the picture, related to image compression
         * */



        /**
         * find injuredNum string
         * */
        EditText injuredNumEditText = rootView.findViewById(R.id.report_injured);


        /**
         * find other info
         * */
        EditText otherInfoEditText = rootView.findViewById(R.id.report_otherinfo);

        /**
         * cnacel Button click
         * clear input string and button
         *
         * */
        ImageView cancelButton = rootView.findViewById(R.id.report_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView) {

                Log.d("Button click", "cancel clicked!");
                injuredNumEditText.setText("");
                otherInfoEditText.setText("");
                disasterChosen.clearCheck();
                //set disasterType as 0 to show not choose state
                reportData.setDisasterType("0");
                reportData.setLatitude(0);
                reportData.setLongitude(0);



            }


        });







        /**
         * submit Button click
         * -------------
         * decide whether the form is completed
         * if not
         * get alert message
         * if so
         * transfer to repoertData source and send to database
         * and navigate to report submit successfully page
         * */
        ImageView submitButton = rootView.findViewById(R.id.report_submit);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){
                Log.d("Button click", "submit clicked!");
                String notification="";
                boolean submitCompleted=true;
                boolean informationCompleted=true;









                if(reportData.getDisasterType()=="0"){
                    notification=notification+"DISASTER TYPE ";
                    informationCompleted=false;
                }

                if(reportData.getLatitude()==0||reportData.getLongitude()==0){
                    notification=notification+"LOCATION ";
                    informationCompleted=false;
                }


                if((injuredNumEditText.getText().toString().trim().length() == 0)){
                    Log.d("Button click", "injure problem!");
                    notification=notification+"INJURIED NUMBER ";
                    informationCompleted=false;
                }


                if(otherInfoEditText.getText().toString().trim().length() == 0){
                    notification=notification+"OTHER INFORMATION ";
                    informationCompleted=false;

                }

                if(!informationCompleted){
                    ConfirmationDialogFragment dialog=ConfirmationDialogFragment.newInstance(notification);
                    dialog.show(getFragmentManager(), "checkDialog");
                }

                if(submitCompleted && informationCompleted){
                    reportData.setInjuredNum(Integer.parseInt((injuredNumEditText.getText().toString())));
                    reportData.setOtherInfo(otherInfoEditText.getText().toString());
                    reportData.setAccountUID(AccountUID);
                    reportData.setReportState(0);
                    String timeStamp=getCurrentTime();
                    reportData.setTimestamp(timeStamp);
                    if(AccountType!=1){


                        /**
                         * check the whether the request tokens available
                         */
                        if (!tokenBucketAlgorithm.allowRequest()) {
                            ConfirmationDialogFragment dialog=ConfirmationDialogFragment.newInstance("Request reach limit!!!! Try again later");
                            dialog.show(getFragmentManager(), "");
                            return;
                        }


                        reportViewModel.CitizenSubmit(reportData);

                        /**
                         * here to upload image with timestamp as its name
                         * */
                        uploadImage(timeStamp);
                        replaceFragment(new SubmitSucessFragment());
                    }
                    else{
                        reportViewModel.GardaSubmit(reportData);

                        replaceFragment(new GardaSubmitSucessFragment());
                    }
                }
            }
        });

        return rootView;
    }



    private void uploadImage(String timeStamp) {
        mStroageRef = FirebaseStorage.getInstance().getReference().child(timeStamp);
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("imageUpload");
        Context context = getContext();





        if (mImageUri != null) {
//            InputStream inputStream = getActivity().getContentResolver().openInputStream(mImageUri);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
//            byte[] bytesArray = byteArrayOutputStream.toByteArray();
//            Bitmap bitmapCompressedImage = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length);



            // Upload the image file to Firebase Storage

// Upload the image file to Firebase Storage

            UploadTask mUploadTask = mStroageRef.putFile(mImageUri);
            // Register observers to listen for when the upload is done or if it fails
            mUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads
                    Log.d("image", "Image upload successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("image", "Image upload failed: " + exception.getMessage());
                }
            });

        }

        else {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.view_toast_custom,
                    getView().findViewById(R.id.lly_toast));
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            tv_msg.setText("No file selected");
            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.CENTER, 0, 400);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();

        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);



    }

// navigate to submitSuccess Fragment
    public void replaceFragment(Fragment fragment){

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.report_container, fragment);

        ft.addToBackStack(null);
        ft.commit();


    }
    public String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        Timestamp timestamp = new Timestamp(currentDate.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestampString = dateFormat.format(timestamp);
        return timestampString;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            mImageUri = data.getData();
//            try {
//                mImageUri = compressImage(uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(mImageUri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);




            cameraIcon.setImageBitmap(bitmap);
        }
    }




}
