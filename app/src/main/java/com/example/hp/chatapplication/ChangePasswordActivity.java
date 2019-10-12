package com.example.hp.chatapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private final static String CHANGE_PASSWORD_URL = BaseUrl_ConstantClass.BASE_URL;
    EditText current_password, new_password, confirm_password;
    Button update_password_btn;
    String old_pass, new_pass, cNew_pass;
    String user_id = SharedPrefManager.getInstance(ChangePasswordActivity.this).getUser().getUser_id().toString();
    ImageView hide_password, hide_new_password, hide_password_confirm;
    private Boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        current_password = (EditText) findViewById(R.id.current_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        update_password_btn = (Button) findViewById(R.id.update_password);
        update_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        hide_new_password = (ImageView) findViewById(R.id.hide_new_password);
        hide_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        hide_password_confirm = (ImageView) findViewById(R.id.hide_password_confirm);
        hide_password_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });


        hide_password = (ImageView) findViewById(R.id.hide_password);
        hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    current_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    current_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });


    }

    private void updatePassword() {

        old_pass = current_password.getText().toString();
        new_pass = new_password.getText().toString();
        cNew_pass = confirm_password.getText().toString();


        if (TextUtils.isEmpty(old_pass)) {
            current_password.setError("Current Passkey can't be Empty ");
            current_password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(new_pass)) {
            new_password.setError("New Passkey can't be Empty ");
            new_password.requestFocus();
            return;
        } else if (!new_pass.equals(cNew_pass)) {
            Toast.makeText(ChangePasswordActivity.this, "Confirm Passkey doesn't Matches ", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!old_pass.equals(old_pass)) {
//            Toast.makeText(ChangePasswordActivity.this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
//            return;
//        }
        else {

            StringRequest changePassStringRequest = new StringRequest(Request.Method.POST, CHANGE_PASSWORD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("success");
                                String message = jsonObject.getString("msg");
                                if (status.equals("true")) {
                                    Toast.makeText(ChangePasswordActivity.this, "Message:" + message, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, "User Not Exists",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(ChangePasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                Toast.makeText(ChangePasswordActivity.this, "" + getString(R.string.error_server),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(ChangePasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> changePasswordParams = new HashMap<>();

                    changePasswordParams.put("action", "change_password");
                    changePasswordParams.put("userid", user_id);
                    changePasswordParams.put("oldpassword", old_pass);
                    changePasswordParams.put("newpassword", cNew_pass);

                    return changePasswordParams;
                }
            };

            MySingleTon.getInstance(ChangePasswordActivity.this).addToRequestQue(changePassStringRequest);
        }
    }
}
