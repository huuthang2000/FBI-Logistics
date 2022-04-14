package com.example.demoapp.view.driver.UISettings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objInviteCode;
import com.example.demoapp.Presenters.Group.pre_Family;
import com.example.demoapp.Presenters.Group.view_Family;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Family;
import com.example.demoapp.Utils.viewUtils;

import java.util.ArrayList;


public abstract class UIJoinOtherFamilies extends AppCompatActivity implements view_Family {
    private final Context context = this;

    private pre_Family preFamily;

    private EditText edtInviteCode;

    private InputMethodManager inputMethodManager;

    private String focusedCode;
    private int keyDELETE = 0;

    private Button btnSubmit;

    private objInviteCode inviteCode;

    private String stringInviteCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_other_families);
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.black,null,R.color.colorThemeStatusBar);

        initView();

        setActionToView();
    }

    private void setActionToView() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringInviteCode = edtInviteCode.getText().toString().toUpperCase();
                if(stringInviteCode.length() == 6){
                    //Get all family of user with Uid
                    ArrayList<objFamily> allFamilyOfUid = tb_Family.getInstance(context).getAllFamilyByUid(objAccount.getCurrentUser().getUid());
                    boolean flag = false;
                    //Check if the invitation code matches the family in the family list
                    for(objFamily family : allFamilyOfUid){
                        if(family.getInviteCode().matches(stringInviteCode)){
                            flag = true;
                            break;
                        }
                    }

                    //If not, then join
                    if(!flag){
                        pre_Family.checkInviteCode(context, stringInviteCode, new pre_Family.onResultInviteCode() {
                            @Override
                            public void onResult(objInviteCode inviteCode, String message) {
                                if(inviteCode != null){
                                    pre_Family.addUidToFamilyID(context, inviteCode.getIdFamily(), new pre_Family.onResultAddUidToFamilyID() {
                                        @Override
                                        public void onResult(boolean isSuccess, String message) {

                                            Toast.makeText(UIJoinOtherFamilies.this, R.string.Successfully_joined, Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }else{

                                    Toast.makeText(context, R.string.No_invitation_code_found, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(String error) {

                                Toast.makeText(context, R.string.No_invitation_code_found, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //Being in this family, can't join
                    else
                        Toast.makeText(context, R.string.You_are_in_this_family, Toast.LENGTH_SHORT).show();
                }
                //Invalid_invitation_code
                else
                    Toast.makeText(context, R.string.Invalid_invitation_code, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void closeKeyboard(){
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    @Override
    public void finish() {
        super.finish();
        closeKeyboard();
    }

    private void initView() {
        edtInviteCode = findViewById(R.id.edtInviteCode);
        btnSubmit = findViewById(R.id.btnSubmit);
        preFamily = new pre_Family(context,this);

    }

    @Override
    public void resultOfActionFamily(boolean isSuccess, String message, String type) {

    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
