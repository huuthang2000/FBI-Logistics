//package com.example.demoapp.view.driver;
//
//import static com.example.demoapp.Models.objApplication.objAccount.getCurrentUser;
//import static com.example.demoapp.Utils.keyUtils.NETWORK;
//import static com.example.demoapp.Utils.keyUtils.accountList;
//import static com.firebase.ui.auth.AuthUI.getApplicationContext;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.work.WorkerParameters;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import javax.xml.transform.Result;
//
//import kotlinx.coroutines.scheduling.CoroutineScheduler;
//
//
//public class UIAppWorker extends CoroutineScheduler.Worker {
//
//    private DatabaseReference mFirebaseDatabase;
//    private FirebaseDatabase mFirebaseInstance;
//    public UIAppWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        if(getCurrentUser()!=null){
//            Log.d("UIAppWorker", "doWork()");
//            Context context = getApplicationContext();
//            mFirebaseInstance = FirebaseDatabase.getInstance();
//            //Called each time when 30 minutes (1 second) (the period parameter)
//            Log.d("UIAppWorker", "timer sent");
//            mFirebaseDatabase = mFirebaseInstance.getReference(accountList);
//            mFirebaseDatabase.child(getCurrentUser().getUid()).child(NETWORK).setValue(System.currentTimeMillis()+"");
//            return Result.success();
//        }
//        return Result.failure();
//    }
//
//}
