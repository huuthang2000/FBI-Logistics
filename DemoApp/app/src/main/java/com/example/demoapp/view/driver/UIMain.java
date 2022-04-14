package com.example.demoapp.view.driver;

import static com.example.demoapp.Utils.keyUtils.MAX_FAMILY;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Presenters.Account.pre_Account;
import com.example.demoapp.Presenters.Account.view_Account;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.Presenters.Chat.view_Chat;
import com.example.demoapp.Presenters.Group.pre_Family;
import com.example.demoapp.Presenters.Group.view_Family;
import com.example.demoapp.Presenters.Safety.pre_Safety;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.SQLite.tb_Family;
import com.example.demoapp.SQLite.tb_Message;
import com.example.demoapp.Utils.PowerStatusReceiver;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.suggestionsDialog;
import com.example.demoapp.adapter.driver.aRclvSelectFamily;
import com.example.demoapp.view.driver.UIChat.UIListChat;
import com.example.demoapp.view.driver.UISafety.SafetyFragment;
import com.example.demoapp.view.driver.UISafety.SpeedService;
import com.example.demoapp.view.driver.UISettings.SettingsFragment;
import com.example.demoapp.view.driver.autoStarService.AlarmReceiver;
import com.example.demoapp.view.driver.autoStarService.GPSService;
import com.example.demoapp.view.driver.autoStarService.mAlarm;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;




public class UIMain extends AppCompatActivity implements view_Family, view_Chat, view_Account {
    private final String TAG = UIMain.class.getSimpleName();

    private final Context context = this;

    private AHBottomNavigation bottomNavigation;

    private pre_Family preFamily;


    private FrameLayout frlChat;

    private ImageView imvNewChat;

    private ArrayList<objFamily> families;

    private ImageView imvAnotherMenu;

    public static boolean isFocusActivityCHAT;

    private Button btnSelectFamily;

    private pre_Safety preSafety;

    private pre_Account preAccount;


    private final Fragment fPeople = new PeopleFragment();
    private final Fragment fPlaces = new PlacesFragment();
    private final Fragment fSafety = new SafetyFragment();
    private final Fragment fSettings = new SettingsFragment();

    public static onSelectedChangeFamily impSniperFamilyName;
   // Phát sóng Tiết kiệm pin
    BroadcastReceiver powerStatusReceiver = new PowerStatusReceiver();
    // Bật / tắt internet thay đổi quảng bá
    BroadcastReceiver userOutInternet = new UIUserInternetReceiver ();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        isFocusActivityCHAT = false;

        initView();

        setupMenuBottom();

        setFragment(fPeople);

        setDataToView();

        setActionToView();
        startService(new Intent(UIMain.this, GPSService.class));
        startService(new Intent(UIMain.this, SpeedService.class));

        pre_Family.listenerMemberInFamily(context);

        suggestionsDialog.showSuggestions(context);

    }


    private void setActionToView() {
        imvNewChat.setVisibility(View.INVISIBLE);

        frlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UIListChat.class));
            }
        });



        btnSelectFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectFamilyDialog(families);
            }
        });

        imvAnotherMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_open_menu));
                showDialogAnotherMenu();
            }
        });
    }


    private void showDialogAnotherMenu() {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_another_menu);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView imvClose = dialog.findViewById(R.id.imvClose);
        LinearLayout lnlAddPlace = dialog.findViewById(R.id.lnlAddPlace);
        LinearLayout lnlAddFamily = dialog.findViewById(R.id.lnlAddFamily);
        LinearLayout lnlJoinOtherFamilies = dialog.findViewById(R.id.lnlJoinOtherFamilies);
        LinearLayout lnlEditProfile = dialog.findViewById(R.id.lnlEditProfile);
        LinearLayout lnlInviteFriends = dialog.findViewById(R.id.lnlInviteFriends);


        imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
                dialog.dismiss();
            }
        });

        lnlAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIPlaceAlertsDetailAdd(context);
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
                dialog.dismiss();
            }
        });



        lnlAddFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show((AppCompatActivity) context,
                        getResources().getString(R.string.Create_family),
                        null, getResources().getString(R.string.ok),
                        getResources().getString(R.string.Cancel))
                        .setHintText(getResources().getString(R.string.family_name))
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                if (!inputStr.matches("")) {

                                    pre_Family.getCountFamilyOfUid(new pre_Family.onResultCountFamily() {
                                        @Override
                                        public void onSuccess(long count) {
                                            if (count < MAX_FAMILY) {
                                                pre_Family.createFamily(context, inputStr, new pre_Family.onResultCreateFamily() {
                                                    @Override
                                                    public void onResult(boolean isSuccess, String message) {


                                                        if (isSuccess) {
                                                            Toast.makeText(context, R.string.Create_a_successful_family, Toast.LENGTH_SHORT).show();
                                                            baseDialog.doDismiss();
                                                            imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
                                                            dialog.dismiss();
                                                        } else {
                                                            Toast.makeText(context, R.string.Failed_family_creation, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            //More than the specified number (Up to 5 families)
                                            else {

                                                Toast.makeText(context, R.string.You_have_created_more_than_the_specified_number, Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailed(String message) {
                                            Toast.makeText(context, R.string.Failed_family_creation, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return true;
                                }
                                //Invalid family name
                                else {
                                    Toast.makeText(context, R.string.Invalid_family_name, Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            }
                        });

            }
        });

        lnlJoinOtherFamilies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIJoinOtherFamilies(context);
                dialog.dismiss();
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
            }
        });

        lnlEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIEditProfile(context);
                dialog.dismiss();
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
            }
        });

        lnlInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUIInviteFriends(context);
                dialog.dismiss();
                imvAnotherMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_button_close_menu));
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
        lnlJoinOtherFamilies.startAnimation(animation);
        lnlInviteFriends.startAnimation(animation);
        lnlEditProfile.startAnimation(animation);
        lnlAddPlace.startAnimation(animation);
        lnlAddFamily.startAnimation(animation);

        dialog.show();

    }

    private void setDataToView() {
        //Set family name to button select family
        //If the id of the family is empty, the default slot is position 0
        if (tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID().matches("")) {
            if (families.size() > 0) {
                tb_CurrentFamilyID.getInstance(context).addOrUpdateID(families.get(0).getId());
            }

        } else {

            //Get position by saved id
            for (int i = 0; i < families.size(); i++) {
                if (families.get(i).getId().matches(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID())) {
                    btnSelectFamily.setText(objFamily.getMyFamily(context).getCommonName());
                    break;
                }
            }
        }

        imvNewChat.setVisibility(pre_Chat.isNewMessage(context) ? View.VISIBLE : View.INVISIBLE);
        tb_Message.getInstance(context).setOnAddMessageListener(new tb_Message.onAddMessageListener() {
            @Override
            public void onAddMessage(objMessage message) {
                imvNewChat.setVisibility(pre_Chat.isNewMessage(context) ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preFamily.getAllIDFamilyByUserID();

        if (mAlarm.checkDevice())
            mAlarm.startAlarm(context);
        else {
            if (!AlarmReceiver.isMyServiceRunning(context, GPSService.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, GPSService.class));
                } else {
                    context.startService(new Intent(context, GPSService.class));
                }
            }
        }

        setDataToView();



//
    }


    private void setupMenuBottom() {
        // Create items
        AHBottomNavigationItem iPeople = new AHBottomNavigationItem(getResources().getString(R.string.People), R.drawable.ic_people_purple_28dp);
        AHBottomNavigationItem iPlaces = new AHBottomNavigationItem(getResources().getString(R.string.Places), R.drawable.ic_place_black_24dp);
        AHBottomNavigationItem iMore = new AHBottomNavigationItem(null, R.drawable.ic_add_circle_green_24dp);
        AHBottomNavigationItem iSafety = new AHBottomNavigationItem(getResources().getString(R.string.Safety), R.drawable.ic_safety_24dp);
        AHBottomNavigationItem iSettings = new AHBottomNavigationItem(getResources().getString(R.string.Settings), R.drawable.ic_settings_black_24dp);

        // Add items
        bottomNavigation.addItem(iPeople);
        bottomNavigation.addItem(iPlaces);
        bottomNavigation.addItem(iMore);
        bottomNavigation.addItem(iSafety);
        bottomNavigation.addItem(iSettings);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#4876d1"));

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        setFragment(fPeople);
                        //Show ad

                        return true;
                    case 1:
                        setFragment(fPlaces);

                        return true;
                    case 3:
                        setFragment(fSafety);

                        return true;
                    case 4:
                        setFragment(fSettings);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }



    private void initView() {

        bottomNavigation = findViewById(R.id.bottomNavigation);

        frlChat = findViewById(R.id.frlChat);

        imvNewChat = findViewById(R.id.imvNewChat);


        imvAnotherMenu = findViewById(R.id.imvAnotherMenu);

        btnSelectFamily = findViewById(R.id.btnSelectFamily);

        preFamily = new pre_Family(context, this);
        preAccount = new pre_Account(context, this);

        pre_Chat preChat = new pre_Chat(context, this);
        preChat.getAllListChatOfUid();

        families = tb_Family.getInstance(context).getAllFamilyByUid(objAccount.getCurrentUser().getUid());
    }


    /**
     * Set fragment to view
     *
     * @param fragment fragment
     */
    private void setFragment(Fragment fragment) {

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();

            ft.replace(R.id.frameMain, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();

        }
    }

    @Override
    public void onBackPressed() {
        MessageDialog.build(this)
                .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
                .setTheme(DialogSettings.THEME.LIGHT)
                .setTitle(R.string.app_name)
                .setMessage(R.string.you_want_exit_app)
                .setOkButton(R.string.YES, new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        return false;
                    }
                })
                .setCancelButton(R.string.NO, new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        baseDialog.doDismiss();
                        return false;
                    }
                })
                .show();
    }

    @Override
    public void resultOfActionFamily(boolean isSuccess, String message, String type) {
        if (type.matches(keyUtils.getAllIDFamilyByUserID)) {
            preFamily.getAllFamilyByUid();
        }

//        if(type.matches(keyUtils.getAllAccountInformationInFamily)){
//
//        }
//
//        if(type.matches(keyUtils.getFamilyInformation)){
//
//        }
    }


    @Override
    public void resultFamilyList(ArrayList<objFamily> families) {
        this.families = families;
    }

    private void showSelectFamilyDialog(ArrayList<objFamily> families) {
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setExpanded(false)
                .setCancelable(true)
                .setGravity(Gravity.TOP)
                .setContentHolder(new ViewHolder(R.layout.dialog_select_family))
                .create();

        RecyclerView rclvFamily = dialog.getHolderView().findViewById(R.id.rclvFamily);

        rclvFamily.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        aRclvSelectFamily adapter = new aRclvSelectFamily(families, context);
        rclvFamily.setAdapter(adapter);

        adapter.setOnClickListener(new aRclvSelectFamily.actionClick() {
            @Override
            public void onClick(int position) {

                if (impSniperFamilyName != null && !families.get(position).getId().matches(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID())) {
                    preFamily.getAllAccountInformationInFamily(families.get(position).getId());

                    //Save current family id
                    tb_CurrentFamilyID.getInstance(context).addOrUpdateID(families.get(position).getId());

                    impSniperFamilyName.onChange(families.get(position).getId());

                    btnSelectFamily.setText(objFamily.getMyFamily(context).getCommonName());

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void setListener(onSelectedChangeFamily listener) {
        impSniperFamilyName = listener;
    }

    @Override
    public void resultListChat(ArrayList<objChat> chatList) {

    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {

    }





    @Override
    public void resultChatDetail(fb_Chat chats) {

    }

    @Override
    public void resultMessage(objMessage newMessage) {

    }

    @Override
    public void resultAllImage(ArrayList<objDetailImage> detailImageList) {

    }

    @Override
    public void errorEmailOrPassword(String errorEmail, String errorPassword) {

    }

    @Override
    public void resultOfActionAccount(boolean isSuccess, String message, String type) {

    }

    @Override
    public void errorInputRegister(String email, String password, String confirmPassword) {

    }


    @Override
    public void errorInputSettingProfile(String errorUsername, String errorPhoneNumber) {

    }

    @Override
    public void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName) {

    }

    @Override
    public void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm) {

    }




    public void showUpgradeDialog() {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialog.findViewById(R.id.imvClose)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.show((AppCompatActivity) context,
                                getResources().getString(R.string.Confirm_close_dialog),
                                getResources().getString(R.string.You_can_still_upgrade_in_settings),
                                getResources().getString(R.string.ok), getResources().getString(R.string.Cancel))
                                .setOkButton(new OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        dialog.dismiss();
                                        return false;
                                    }
                                });
                    }
                });




        dialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public interface onSelectedChangeFamily {
        void onChange(String familyID);
    }






}
