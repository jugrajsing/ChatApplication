package com.example.hp.chatapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class OptVerificationsCustom extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button button_validate_OTP, button_resend_otp;
    String email_user_saved;
    AlertDialog alertDialog;
    String get_entered_otp;
    ProgressBar otp_progress;
    private String OTP;
    private EditText et_OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification_custom);
        et_OTP = findViewById(R.id.et_OTP);
        otp_progress = findViewById(R.id.otp_progress);


        email_user_saved = SharedPrefManager.getInstance(OptVerificationsCustom.this).getUser().getUser_email().toString();


        button_resend_otp = (Button) findViewById(R.id.button_resend_otp);
        button_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_entered_otp = et_OTP.getText().toString();
                et_OTP.setText("");
                et_OTP.setError(null);
                validateYourOTP();

            }
        });

        button_validate_OTP = (Button) findViewById(R.id.button_validate_OTP);
        button_validate_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_entered_otp = et_OTP.getText().toString();
                validateYourOTP();
            }
        });

       /* et_OTP=(TextView) findViewById(R.id.et_OTP);
        OTP = getIntent().getStringExtra("OTP");
        et_OTP.setText(email_user_saved);*/
    }

    private void validateYourOTP() {

        final String user_id = SharedPrefManager.getInstance(OptVerificationsCustom.this).getUser().getUser_id().toString();
        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;

        if (TextUtils.isEmpty(get_entered_otp)) {
            et_OTP.setError("OTP can't  be empty ");
            et_OTP.requestFocus();
            return;
        } else {
            otp_progress.setVisibility(View.VISIBLE);
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Verifying...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                otp_progress.setVisibility(View.INVISIBLE);
                                String status = jsonObject.getString("success");
                                String message = jsonObject.getString("message");

                                if (status.equals("true")) {
                              /*  Intent intent = new Intent(OptVerificationsCustom.this,CompleteYourProfile.class);
                                Toast.makeText(OptVerificationsCustom.this, "Message"+message, Toast.LENGTH_SHORT).show();
                                startActivity(intent);*/
                                    alertVarificationSuccess();
                                } else {
                                    Toast.makeText(OptVerificationsCustom.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(OptVerificationsCustom.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                Toast.makeText(OptVerificationsCustom.this, "" + getString(R.string.error_server),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(OptVerificationsCustom.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> logParams = new HashMap<>();
                    logParams.put("action", "validate_otp");
                    logParams.put("userid", user_id);
                    logParams.put("otp", get_entered_otp);

                    return logParams;
                }
            };

            MySingleTon.getInstance(OptVerificationsCustom.this).addToRequestQue(stringRequestLogIn);
            progressDialog.dismiss();


        }

    }

    private void alertVarificationSuccess() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Your account is verified Successfully");
        alertDialogBuilder.setNegativeButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OptVerificationsCustom.this, CompleteYourProfile.class);
                intent.putExtra("CHECK", "1");
                startActivity(intent);
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
