package com.poly_store_online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poly_store_online.R;
import com.poly_store_online.retrofit.ApiBanHang;
import com.poly_store_online.retrofit.RetrofitClient;
import com.poly_store_online.utils.Utils;

import java.util.Arrays;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    TextView txtdangki, txtresetMK;
    EditText email, matKhau;
    AppCompatButton btndangnhap;
    ImageView btnFB, btnGG;
    GoogleSignInClient googleSignInClient;
    GoogleSignInOptions googleSignInOptions;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        //facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        //google
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);



        initView();
        initControll();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //google
        if (requestCode ==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToMainActivity();

            }catch (ApiException ex){
                Toast.makeText(getApplicationContext(), "Khong thanh cong", Toast.LENGTH_SHORT).show();
            }

        }else{
            //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //google chuyen den Main
    void navigateToMainActivity(){
        Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initControll(){
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
            }
        });

        txtresetMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuenMatKhauActivity.class);
                startActivity(intent);
            }
        });

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                String str_matKhau = matKhau.getText().toString().trim();

                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Ban chua nhap Email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(str_matKhau)){
                    Toast.makeText(getApplicationContext(), "Ban chua nhap mat khau", Toast.LENGTH_SHORT).show();
                }else {
                    //save

                    Paper.book().write("email",str_email);
                    Paper.book().write("matKhau",str_matKhau);

                    if (user != null){
                        //user da co dang nhap fire base
                        dangNhap(str_email, str_matKhau);
                    }else{
                        //user da signout
                        firebaseAuth.signInWithEmailAndPassword(str_email, str_matKhau)
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            dangNhap(str_email, str_matKhau);
                                        }
                                    }
                                });
                    }
                }
            }

        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().logInWithReadPermissions(DangNhapActivity.this, Arrays.asList("public_profile"));
            }
        });

        btnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void initView(){
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdangky);
        txtresetMK = findViewById(R.id.txtresetMK);
        btnFB = findViewById(R.id.facebook_login);
        email = findViewById(R.id.email);
        matKhau = findViewById(R.id.matKhau);
        btndangnhap = findViewById(R.id.btndangnhap);
        btnGG = findViewById(R.id.google_login);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //read data
        if (Paper.book().read("email") != null && Paper.book().read("matKhau") != null){
            email.setText(Paper.book().read("email"));
            matKhau.setText(Paper.book().read("matKhau"));
            if (Paper.book().read("isLogin") != null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
               //             dangNhap(Paper.book().read("email"), Paper.book().read("matKhau"));
                        }
                    }, 100);
                }
            }
        }


    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    private void dangNhap(String email, String matKhau) {
        compositeDisposable.add(apiBanHang.dangNhap(email, matKhau)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nguoiDungModel -> {
                            if (nguoiDungModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin",isLogin);
                                Utils.nguoidung_current = nguoiDungModel.getResult().get(0);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.nguoidung_current.getEmail() != null && Utils.nguoidung_current.getMatKhau() != null){
            email.setText(Utils.nguoidung_current.getEmail());
            matKhau.setText(Utils.nguoidung_current.getMatKhau());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}