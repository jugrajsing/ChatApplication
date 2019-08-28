package com.example.hp.chatapplication.FingerPrintHandler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
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
import com.example.hp.chatapplication.ForgotPasswordActivity;
import com.example.hp.chatapplication.LoginRegistrationActivity;
import com.example.hp.chatapplication.ModelClasses.User;
import com.example.hp.chatapplication.MySingleTon;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.SharedPrefManager;
import com.example.hp.chatapplication.UserNavgation;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.hp.chatapplication.VideoCall.utils.ResourceUtils.getString;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private    String device_id1 ;
    ;
    // Constructor

    public FingerprintHandler(Context mContext , String device_id) {
        context = mContext;
        device_id1 = device_id;
    }
    public FingerprintHandler(Context mContext ) {
        context = mContext;

    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);

    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("", true);

        login();
        //   Intent intent=new Intent(context,UserNavgation.class);
        // context.startActivity(intent);

    }

    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }
    }

    public void signInWithFingerPrintApi(final String device_id, final Context context){


        //  device_id1 = device_id;
        //device_id2 = device_id;

    }

    private void login()
    {
        final String LOGIN_URL= BaseUrl_ConstantClass.BASE_URL;
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equals("true")) {

                                JSONObject details= jsonObject.getJSONObject("user_details");
                                String id=details.getString("id");
                                String secrate_id=details.getString("secrate_id");
                                String mobileno=details.getString("mobileno");
                                String email=details.getString("email");
                                User user=new User(id,"User Name",email,mobileno," ",secrate_id,"","" ,"true");
                                SharedPrefManager.getInstance(context).userLogin(user);
                                Intent i = new Intent(context, UserNavgation.class); //Replace HomeActivity with the name of your activity
                                context.startActivity(i);

                            }
                            else {


                                Toast.makeText(context, "unable to validate fingerprint", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, ""+getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(context, ""+getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context, ""+getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }                           }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "finger_login");
                logParams.put("device_id",device_id1);
                return logParams;
            }
        };

        MySingleTon.getInstance(context).addToRequestQue(stringRequestLogIn);
    }

}
