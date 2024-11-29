package com.example.qingting.Setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qingting.MainActivity;
import com.example.qingting.R;
import com.example.qingting.Utils.DialogUtils;
import com.example.qingting.Utils.ToastUtils;
import com.example.qingting.data.SP.LoginSP;

public class SettingActivity extends AppCompatActivity {



    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        logoutBtn = findViewById(R.id.logout_btn);
        init();
    }

    private void init() {
        initLogoutBtn();
    }
    
    
    private void initLogoutBtn() {
        logoutBtn.setOnClickListener((v)->{
            Context context = v.getContext();
            DialogUtils.showEnsureDialog(context,
                    context.getString(R.string.setting_logout_title),
                    context.getString(R.string.setting_logout_msg),
                    context.getString(R.string.sure_message),
                    new DialogUtils.DialogCallback() {
                        @Override
                        public void doSth(View view) {
                            // 清空登录信息
                            LoginSP.clearToken(view.getContext());
                            // 回到主页
                            ToastUtils.makeShortText(view.getContext(), view.getContext().getString(R.string.setting_logout_success));
                            Intent intent = new Intent(view.getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }, v);
        });
    }
}