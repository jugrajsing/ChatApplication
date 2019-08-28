package com.example.hp.chatapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class AccountVarificationViaMobile extends AppCompatActivity {

    EditText user_mobile, et_email;
    ProgressDialog progressDialog;
    String otp;
    RelativeLayout relative_mobile, relative_email;
    Button button_secret_id, button_passkey, validate_button;
    ProgressBar varifyaccount_social;

    //  private String type="mobile";
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_varification_via_mobile);

        relative_email = (RelativeLayout) findViewById(R.id.relative_email);
        varifyaccount_social = (ProgressBar) findViewById(R.id.varifyaccount_social);
        //relative_mobile=(RelativeLayout)findViewById(R.id.relative_mobile) ;

      /*  button_passkey=(Button) findViewById(R.id.button_passkey);
        button_passkey.setOnClickListener(this);

        button_secret_id=(Button) findViewById(R.id.button_secret_id);
        button_secret_id.setOnClickListener(this);*/

        et_email = (EditText) findViewById(R.id.et_email);
        String email = SharedPrefManager.getInstance(AccountVarificationViaMobile.this).getUser().getUser_email().toString();
        et_email.setText(email);


       /* user_mobile=(EditText)findViewById(R.id.user_mobile);
        String mobile_no=SharedPrefManager.getInstance(AccountVarificationViaMobile.this).getUser().getUser_mobile_no().toString();
        user_mobile.setText(mobile_no);
*/
        validate_button = (Button) findViewById(R.id.validate_button);
        validate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateViaMobile();

            /*   if (type.equals("mobile"))
               {
                   validateViaMobile(type);
               }
               else
                   {
                   validateViaMobile(type);

               }*/
            }
        });

        // Toast.makeText(this, "OTP will be sent to your registered Email id", Toast.LENGTH_LONG).show();
        alertDialogAccountVarificationViaMobile();

    }

    private void validateViaMobile() {
        varifyaccount_social.setVisibility(View.VISIBLE);
        final String user_id = SharedPrefManager.getInstance(AccountVarificationViaMobile.this).getUser().getUser_id().toString();
        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Connecting...");
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
                            varifyaccount_social.setVisibility(View.INVISIBLE);
                            String status = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (status.equals("true")) {

                                otp = jsonObject.getString("otp");
                                // Toast.makeText(AccountVarificationViaMobile.this, "Your Otp is:" + message + otp, Toast.LENGTH_SHORT).show();
                                alertDialogAccountVarification();
                            } else {
                                //  Toast.makeText(AccountVarificationViaMobile.this, "Please enter your valid Secret Id or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AccountVarificationViaMobile.this, "" + getResources().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(AccountVarificationViaMobile.this, "" + getResources().getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(AccountVarificationViaMobile.this, "" + getResources().getString(R.string.error_network_timeout),
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
                logParams.put("action", "send_otp");
                logParams.put("userid", user_id);
                logParams.put("actiontype", "mobile");


                return logParams;
            }
        };

        MySingleTon.getInstance(AccountVarificationViaMobile.this).addToRequestQue(stringRequestLogIn);
        progressDialog.dismiss();
    }

    /*public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.button_secret_id :
            {
                relative_email.setVisibility(View.GONE);
                relative_mobile.setVisibility(View.VISIBLE);
                    // startActivity(new Intent(this,RegistrationActivity.class));
                    button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(AccountVarificationViaMobile.this, R.drawable.signin_button));
                    button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(AccountVarificationViaMobile.this, R.drawable.signin_button_voilet));
                    button_secret_id.setTextColor((Color.parseColor("#000000")));
                    button_passkey.setTextColor((Color.parseColor("#FFFFFF")));

                    type="mobile";
                    break;
            }
            case R.id.button_passkey: {

                    relative_email.setVisibility(View.VISIBLE);
                    relative_mobile.setVisibility(View.GONE);
                    button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(AccountVarificationViaMobile.this, R.drawable.signin_button_voilet));
                    button_secret_id.setTextColor((Color.parseColor("#FFFFFF")));
                    button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(AccountVarificationViaMobile.this, R.drawable.signin_button));
                    button_passkey.setTextColor((Color.parseColor("#000000")));

                    type="email";

                    break;
                }
            }
        }*/

    private void alertDialogAccountVarification() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // alertDialogBuilder.setTitle("We will be verifying your Email ID");
        alertDialogBuilder.setMessage("We will be verifying your Email ID");
        alertDialogBuilder.setMessage("Would you like to continue");

        alertDialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AccountVarificationViaMobile.this, OptVerificationsCustom.class);
                intent.putExtra("OTP", otp);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void alertDialogAccountVarificationViaMobile() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("OTP will be sent to your registered Email id");
        alertDialogBuilder.setNegativeButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // startActivity(new Intent(AccountVarificationViaMobile.this, OptVerificationsCustom.class));
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}

