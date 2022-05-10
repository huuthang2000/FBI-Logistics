package com.example.demoapp.view.dialog.log;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.adapter.log.ImageAdapter;
import com.example.demoapp.databinding.FragmentInsertLogBinding;
import com.example.demoapp.model.ImageModel;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.activity.log.ImagesActivity;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.LogViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import gun0912.tedbottompicker.util.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InsertLogFragment extends DialogFragment implements View.OnClickListener{

    private static final int MY_REQUEST_CODE = 10;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGES = 2;
    public static final int STORAGE_PERMISSION = 100;

    ArrayList<ImageModel> imageList;
    ArrayList<String> selectedImageList;
    RecyclerView imageRecyclerView, selectedImageRecyclerView;
    private final String[] itemsImportOrExport = {"Nhập Khẩu", "Xuất Khẩu"};
    ImageAdapter imageAdapter;
    String[] projection = {MediaStore.MediaColumns.DATA};
    File image;
    private final String[] listStr = new String[3];
    private CardView cardView;
    private ImageView imageView;
    private Button button;
    private CharSequence[] options= {"Camera","Gallery","Cancel"};
    private String type = "";

    public static InsertLogFragment insertDiaLogLog(){
        return new InsertLogFragment();

    }
    private ArrayAdapter<String> adapterItemsMonth, adapterItemsImportAndExport, adapterItemsType;

    private FragmentInsertLogBinding logBinding;

    private LogViewModel mLogViewModel;

    private Bitmap bitmap;

    private CommunicateViewModel mCommunicateViewModel;

    public static final String TAG = InsertLogFragment.class.getName();

    private Uri mUri;
    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    android.util.Log.e(TAG, "onActivityResult");
                    if(result.getResultCode() == getActivity().RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            logBinding.imageview.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logBinding = FragmentInsertLogBinding.inflate(inflater,container, false);
        View view = logBinding.getRoot();


        selectedImageList = new ArrayList<>();
        imageList = new ArrayList<>();
        mLogViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        initView();
        return view;
    }

    private void initView() {
        adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsImportAndExport = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, itemsImportOrExport);
        adapterItemsType = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_TYPE);
        selectedImageList = new ArrayList<>();
        imageList = new ArrayList<>();
        logBinding.insertAutoMonth.setAdapter(adapterItemsMonth);
        logBinding.insertAutoShippingType.setAdapter(adapterItemsImportAndExport);
        logBinding.insertAutoLoaihinh.setAdapter(adapterItemsType);
        logBinding.imageview.setOnClickListener(this);
        logBinding.btnFunctionAdd.setOnClickListener(this);
        logBinding.btnFunctionCancel.setOnClickListener(this);


        logBinding.insertAutoMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[0] = adapterView.getItemAtPosition(i).toString();
            }
        });

        logBinding.insertAutoShippingType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[1] = adapterView.getItemAtPosition(i).toString();
            }
        });

        logBinding.insertAutoLoaihinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listStr[2] = adapterView.getItemAtPosition(i).toString();
            }
        });

        setCancelable(false);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_function_add:
                if(isFilled()) {
                    insertLog();
                    dismiss();
                }else{
                    Toast.makeText(getContext(), Constants.INSERT_FAILED, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_function_cancel:
                dismiss();
                break;
            case R.id.imageview:
                Intent gallery = new Intent(getActivity(), ImagesActivity.class);
                gallery.setType("image/*"); //allow any image file type.
               startActivity(gallery);
//                onClickRequestPermission();
        }
    }

//    private void onClickRequestPermission() {
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
//            openGallery();
//            return;
//        }
//        if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            openGallery();
//        } else{
//            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//            requestPermissions(permission, MY_REQUEST_CODE);
//        }
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == MY_REQUEST_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openGallery();
//            }
//        }
//    }

//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(intent.ACTION_GET_CONTENT);
//        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
//    }


    private String getCreatedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public boolean isFilled() {
        boolean result = true;


        if (TextUtils.isEmpty(logBinding.insertAutoShippingType.getText())) {
            result = false;
            logBinding.insertAutoShippingType.setError(Constants.ERROR_AUTO_COMPLETE_SHIPPING_TYPE);
        }
        if (TextUtils.isEmpty(logBinding.insertAutoLoaihinh.getText())) {
            result = false;
            logBinding.insertAutoLoaihinh.setError(Constants.ERROR_AUTO_COMPLETE_TYPE_LOG);
        }
        if (TextUtils.isEmpty(logBinding.insertAutoMonth.getText())) {
            result = false;
            logBinding.insertAutoMonth.setError(Constants.ERROR_AUTO_COMPLETE_MONTH);
        }

        return result;
    }


    private void insertLog() {
        String strTenHang = logBinding.tfTenhang.getEditText().getText().toString();
        String strhscode = logBinding.tfHscode.getEditText().getText().toString();
        String strcongdung = logBinding.tfCongdung.getEditText().getText().toString();

        String strcangdi = logBinding.tfCangdi.getEditText().getText().toString();
        String strcangden = logBinding.tfCangden.getEditText().getText().toString();
        String strloaihang = logBinding.tfLoaihang.getEditText().getText().toString();
        String strsoluongcuthe = logBinding.tfSoluongcuthe.getEditText().getText().toString();
        String stryeucaudacbiet = logBinding.tfYeucaudacbiet.getEditText().getText().toString();
        String strPrice = logBinding.tfPrice.getEditText().getText().toString();

        mCommunicateViewModel.makeChanges();
        String strRealPath = RealPathUtil.getRealPath(getContext(),mUri);
        File file = new File(strRealPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part multPartbodyimage = MultipartBody.Part.createFormData(Constants.KEY_IMG,file.getName(),requestBody);
//        Call<Log> call = mLogViewModel.insertLog(strTenHang, strhscode, strcongdung, String.valueOf(multPartbodyimage),
//                strcangdi, strcangden, strloaihang, strsoluongcuthe, stryeucaudacbiet, strPrice,
//                listStr[0], listStr[1], listStr[2], getCreatedDate());
//        call.enqueue(new Callback<Log>() {
//            @Override
//            public void onResponse(Call<Log> call, Response<Log> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(getContext(), "Created Successful!!", Toast.LENGTH_LONG).show();
//                }
//
//            }

//            @Override
//            public void onFailure(Call<Log> call, Throwable t) {
//
//            }
//        });

    }
//    public void resetEditText(){
//        Objects.requireNonNull(logBinding.tfTenhang.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfHscode.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfCongdung.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfHinhanh.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfCangdi.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfCangden.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfLoaihang.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfSoluongcuthe.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfYeucaudacbiet.getEditText()).setText("");
//        Objects.requireNonNull(logBinding.tfValid.getEditText()).setText("");
//
//    }
    private void uploadImage(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);

        byte [] imageInByte = byteArrayOutputStream.toByteArray();

        String encodeImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        Toast.makeText(getContext(), encodeImage,Toast.LENGTH_LONG).show();

    }
}