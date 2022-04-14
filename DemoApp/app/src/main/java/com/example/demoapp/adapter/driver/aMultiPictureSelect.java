package com.example.demoapp.adapter.driver;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.demoapp.R;

import java.util.ArrayList;



public class aMultiPictureSelect extends BaseAdapter {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<String> mImageList;
    private LayoutInflater mInflater;
    private Context mContext;
    private onAction listener;
    private ArrayList<Boolean> booleanArray;

    public aMultiPictureSelect(Context context, ArrayList<String> imageList) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.booleanArray = new ArrayList<>();

        this.mImageList = new ArrayList<>();
        this.mImageList = imageList;

        //initialization boolean list
        for(int i = 0; i< mImageList.size(); i++) {
            booleanArray.add(false);
        }
    }

    public ArrayList<String> getImageCheckedList() {
        ArrayList<String> mTempArray = new ArrayList<>();

        for(int i = 0; i< mImageList.size(); i++) {
            if(booleanArray.get(i)) {
                mTempArray.add(mImageList.get(i));
            }
        }

        return mTempArray;
    }

    public interface onAction{
        void onSelectedImage(int selectedTotal);
    }

    public void setOnClickListener(onAction listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image_from_gallery, null);
        }

        final String pathImage = mImageList.get(position);

        final CheckBox mCheckBox = convertView.findViewById(R.id.smChkSelect);
        final ImageView imageView = convertView.findViewById(R.id.imvImageFromGallery);

        Glide.with(mContext)
                .load(pathImage)
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_image)
                .into(imageView);

        mCheckBox.setChecked(booleanArray.get(position));
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
            public void onCheckedChanged(CheckBox checkBox, boolean isChecked) {
                booleanArray.set(position,isChecked);
                if(listener!=null){
                    int selectedTotal = 0;
                    for(int i = 0; i< booleanArray.size(); i++) {
                        if(booleanArray.get(i)) {
                            selectedTotal++;
                        }
                    }
                    listener.onSelectedImage(selectedTotal);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return convertView;
    }

}

