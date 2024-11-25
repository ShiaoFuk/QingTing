package com.example.qingting.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qingting.Bean.User;
import com.example.qingting.MainActivity;
import com.example.qingting.R;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.SP.LoginSP;
import com.example.qingting.net.cyper.AESUtils;
import com.example.qingting.net.request.LoginRequest;
import com.example.qingting.net.request.RegisterRequest;
import com.example.qingting.net.request.RequestListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getName();
    EditText usernameTextView;
    EditText pwdTextView;

    TextView usernameTip;
    TextView pwdTip;
    Button loginBtn;
    TextView switchModeBtn;
    boolean isRegisterMode;
    Context context;

    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        usernameTextView = findViewById(R.id.user_name);
        pwdTextView = findViewById(R.id.pwd);
        loginBtn = findViewById(R.id.login_btn);
        switchModeBtn = findViewById(R.id.switch_mode_btn);
        usernameTip = findViewById(R.id.user_name_tip);
        pwdTip = findViewById(R.id.pwd_tip);
        context = this;
        init();
    }

    private void init() {
        loginMode();
        initUserName();
        initPwd();
        // 默认加载登录
        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRegisterMode) {
                    loginMode();
                    switchModeBtn.setText(v.getResources().getString(R.string.no_user));
                    loginBtn.setText(v.getResources().getString(R.string.login));
                } else {
                    registerMode();
                    switchModeBtn.setText(v.getResources().getString(R.string.go_to_login));
                    loginBtn.setText(v.getResources().getString(R.string.register));
                }
                isRegisterMode = !isRegisterMode;
            }
        });
    }


    private void loginMode() {
        initBtn(Mode.LOGIN_MODE);
    }

    private void registerMode() {
        // 检测账号位数，密码位数和规则（包含数字和字母)
        initBtn(Mode.REGISTER_MODE);

    }

    private void initUserName() {
        boolean isUser = true;
        usernameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CheckValid.isUserNameValid(s.toString())) {
                    usernameTextView.setBackgroundResource(R.drawable.login_edit_text_green_bg);
                    initTip(isUser, false);
                } else {
                    usernameTextView.setBackgroundResource(R.drawable.login_edit_text_red_bg);
                    initTip(isUser, true);
                }
            }
        });
        usernameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (CheckValid.isUserNameValid(usernameTextView.getText().toString())) {
                        v.setBackgroundResource(R.drawable.login_edit_text_green_bg);
                        initTip(isUser, false);
                    } else {
                        v.setBackgroundResource(R.drawable.login_edit_text_red_bg);
                        initTip(isUser, true);
                    }
                } else {
                    v.setBackgroundResource(R.drawable.login_edit_text_bg);
                    initTip(isUser, false);
                }
            }
        });
    }

    private void initTip(boolean isUserName, boolean isRed) {
        if (isRed) {
            if (isUserName) {
                usernameTip.setVisibility(View.VISIBLE);
            } else {
                pwdTip.setVisibility(View.VISIBLE);
            }
        } else {
            if (isUserName) {
                usernameTip.setVisibility(View.GONE);
            } else {
                pwdTip.setVisibility(View.GONE);
            }
        }
    }

    private void initPwd() {
        boolean isUser = false;
        pwdTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CheckValid.isPwdValid(s.toString())) {
                    pwdTextView.setBackgroundResource(R.drawable.login_edit_text_green_bg);
                    initTip(isUser, false);
                } else {
                    pwdTextView.setBackgroundResource(R.drawable.login_edit_text_red_bg);
                    initTip(isUser, true);
                }
            }
        });
        pwdTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (CheckValid.isPwdValid(pwdTextView.getText().toString())) {
                        v.setBackgroundResource(R.drawable.login_edit_text_green_bg);
                        initTip(isUser, false);
                    } else {
                        v.setBackgroundResource(R.drawable.login_edit_text_red_bg);
                        initTip(isUser, true);
                    }
                } else {
                    v.setBackgroundResource(R.drawable.login_edit_text_bg);
                    initTip(isUser, false);
                }
            }
        });
    }


    private void initBtn(Mode mode) {
        if (mode == null) return;
        loginBtn.setOnClickListener((v) -> {
            // 发起请求
            // 做防抖
            if (!(CheckValid.isPwdValid(pwdTextView.getText().toString()) && CheckValid.isUserNameValid(usernameTextView.getText().toString()))) {
                // 不满足要求
                ToastUtils.makeShortText(this, getResources().getString(R.string.user_pwd_invalid));
                return;
            }
            loginBtn.setClickable(false);
            loginBtn.setEnabled(false);
            RequestListener requestListener = new RequestListener() {
                @Override
                public Object onPrepare(Object object) {
                    if (!(object instanceof User)) return null;
                    User user = (User) object;
//                    // 对密码加密
//                    try {
//                        user.setPassword(AESUtils.encrypt(context, user.getPassword()));
//                    } catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                        return null;
//                    }
                    return user;
                }

                @Override
                public void onRequest() {

                }

                @Override
                public void onReceive() {

                }

                @Override
                public void onSuccess(JsonElement element) throws Exception {
                    JsonObject obj = element.getAsJsonObject();
                    JsonElement code = obj.get("code");
                    if (code == null) {
                        throw new Exception(context.getResources().getString(R.string.login_error));
                    }
                    if (code.getAsInt() == 200) {
                        String token = obj.get("data").getAsString();
                        LoginSP.setToken(context, token);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return;
                    }
                    throw new Exception(context.getResources().getString(R.string.login_fail));
                }

                @Override
                public void onError(Exception e) {
                    ToastUtils.makeShortText(context, e.getMessage());
                }

                @Override
                public void onFinish() {
                    handler.post(()->{
                        loginBtn.setClickable(true);
                        loginBtn.setEnabled(true);
                    });
                }
            };
            User user = new User(usernameTextView.getText().toString(), pwdTextView.getText().toString());
            if (mode == Mode.LOGIN_MODE) {
                LoginRequest.login(requestListener, user);
                return;
            }
            if (mode == Mode.REGISTER_MODE) {
                RegisterRequest.register(requestListener, user);
                return;
            }
        });

    }
}

enum Mode {
    LOGIN_MODE,
    REGISTER_MODE
}

class CheckValid {
    static boolean isUserNameValid(String name) {
        int len = name.length();
        return len >= 6 && len <= 20;
    }

    static boolean isPwdValid(String password) {
        int len = password.length();
        return len >= 6 && len <= 20 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }
}