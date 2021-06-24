package com.huawei.shopmate.ui.LogIn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.shopmate.R;
import com.huawei.shopmate.ui.DashBoard.List.ListMenu;


public class LogInScreen extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG ="HMSAccountKit" ;
    private HuaweiIdAuthService service;
    private HuaweiIdAuthParams authParams;
    AccountAuthParams authParams1;
    AccountAuthService service1;
    private TextView displayNameTextView;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen_layout);
        //findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.token_sign_in).setOnClickListener(this);
        displayNameTextView=findViewById(R.id.textView3);
        imgView=findViewById(R.id.imageView2);
        findViewById(R.id.about_us).setOnClickListener(this);
        findViewById(R.id.terms_and_conditions).setOnClickListener(this);
        findViewById(R.id.privacy_policies).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.token_sign_in:
                idTokenSignInClick();
                break;
            case R.id.about_us:
                gotoUrl("https://shopmate200.blogspot.com/2021/06/about-us.html");
                overridePendingTransition(0,0);
                break;
            case R.id.terms_and_conditions:
                gotoUrl("https://shopmate200.blogspot.com/2021/06/window.html");
                overridePendingTransition(0,0);
                break;
            case R.id.privacy_policies:
                gotoUrl("https://shopmate200.blogspot.com/2021/06/privacy-policy.html");
                overridePendingTransition(0,0);
                break;

            default:
                break;
        }

    }

    private void idTokenSignInClick() {
        authParams=new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken().createParams();
        service = HuaweiIdAuthManager.getService(LogInScreen.this, authParams);
        Task<AuthHuaweiId> task=service.silentSignIn();

        task.addOnSuccessListener(new OnSuccessListener<AuthHuaweiId>() {
            @Override
            public void onSuccess(AuthHuaweiId authHuaweiId) {
                Toast.makeText(LogInScreen.this,"Signed in successfully.",Toast.LENGTH_SHORT).show();
                showResultInfo(authHuaweiId);
                Intent intent1=new Intent(LogInScreen.this, ListMenu.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                overridePendingTransition(0,0);


            }
        });

        task.addOnFailureListener((e) -> {
            startActivityForResult(service.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN);
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constant.REQUEST_SIGN_IN_LOGIN){
            Task<AuthHuaweiId> authHuaweiIdTask=HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()){
                AuthHuaweiId huaweiAccount=authHuaweiIdTask.getResult();
                showResultInfo(huaweiAccount);
                //Toast.makeText(MainActivity.this,""+authAccountTask.getResult()+authAccount.getAuthorizationCode(),Toast.LENGTH_LONG).show();

            }
        }


    }

    public void showResultInfo(AuthHuaweiId huaweiAccount) {
        String displayName =huaweiAccount.getFamilyName();
        Log.d(TAG,displayName);

        displayNameTextView.setText("displayName: "+displayName);

        Uri avatarUri=huaweiAccount.getAvatarUri();
        Log.d(TAG, "avatarUri"+avatarUri.toString());

    }

    public void gotoUrl(String s){
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }


}
