package com.example.hp.chatapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_secret_id, button_passkey, reset_password;
    RelativeLayout relative_layout_mobile_or_email, relative_layout_passkey;
    Spinner alphabets_spinner_forgorpassword;
    EditText numeric_code_forgot, passkey_ed, et_buzz_id;
    String security_code = "A";
    String alphabeticalletters;
    int length;
    ProgressDialog pd;
    String var7 = "";
    Spinner alphabets_spinner_numeric_code;
    int i;
    ArrayList<String> aListNumbers;
    ArrayAdapter<String> adapter;
    AlertDialog alertDialog;
    ImageView question_mark_hint;
    private String type = "Secret Id";
    private String buzz_id, numeric_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        question_mark_hint = (ImageView) findViewById(R.id.question_mark_hint);
        passkey_ed = findViewById(R.id.passkey);
        question_mark_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsQuestionMarkPopup();
            }
        });


        Integer[] testArray = new Integer[1000];
        aListNumbers = new ArrayList<String>();

        for (i = 0; i < testArray.length; i++) {
            testArray[i] = i;
            String formated = String.format("%03d", i);//here all list print one by one with loop i have to pass in spinner
            aListNumbers.add(formated);
        }


        alphabets_spinner_numeric_code = (Spinner) findViewById(R.id.alphabets_spinner_numeric_code);
        alphabets_spinner_numeric_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    /*Toast.makeText(CompleteYourProfile.this, ""+ adapter_interest.getItem(position).toString(), Toast.LENGTH_LONG).show();
                    var1=adapter_interest.getItem(position).toString();
                    arrayList_spinner_interest.add(var1);*/

                var7 = alphabets_spinner_numeric_code.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        et_buzz_id = (EditText) findViewById(R.id.et_buzz_id);
        reset_password = (Button) findViewById(R.id.reset_password);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buzz_id = et_buzz_id.getText().toString().trim();
                String value = passkey_ed.getText().toString().trim();
              /*  numeric_CODE= numeric_code_forgot.getText().toString();
                length = numeric_code_forgot.getText().length();*/
/*

                if (TextUtils.isEmpty(numeric_CODE))
                {
                    numeric_code_forgot.setError("Numeric code  Can't  be empty");
                    numeric_code_forgot.requestFocus();
                    return;
                }
                else if (length<3){
                    numeric_code_forgot.setError("Enter min 3 Numbers");
                    numeric_code_forgot.requestFocus();
                }
*/


                if (type.equalsIgnoreCase("Secret ID")) {
                    forgotPasswodApiPasskey();
                } else {

                    if ((!Patterns.EMAIL_ADDRESS.matcher(value).matches()) && ((!Patterns.PHONE.matcher(value).matches()) && (value.length() < 10))) {

                        passkey_ed.setError("Please enter valid email/number");
                    } else
                        forgotPasswodApiBuzzid(value);
                }


            }
        });


        // numeric_code_forgot=(EditText)findViewById(R.id.numeric_code_forgot);

        alphabets_spinner_forgorpassword = (Spinner) findViewById(R.id.alphabets_spinner_forgorpassword);

        alphabets_spinner_forgorpassword.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                alphabeticalletters = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
        list.add("K");
        list.add("L");
        list.add("M");
        list.add("N");
        list.add("O");
        list.add("P");
        list.add("Q");
        list.add("R");
        list.add("S");
        list.add("T");
        list.add("U");
        list.add("V");
        list.add("W");
        list.add("X");
        list.add("Y");
        list.add("Z");

        ArrayAdapter<String> adapter_alphabet = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, aListNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        alphabets_spinner_forgorpassword.setAdapter(adapter_alphabet);
        alphabets_spinner_numeric_code.setAdapter(adapter);


        relative_layout_mobile_or_email = (RelativeLayout) findViewById(R.id.relative_layout_mobile_or_email);
        relative_layout_passkey = (RelativeLayout) findViewById(R.id.relative_layout_passkey);


        button_passkey = (Button) findViewById(R.id.button_passkey);
        button_passkey.setOnClickListener(this);

        button_secret_id = (Button) findViewById(R.id.button_secret_id);
        button_secret_id.setOnClickListener(this);

        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        if (sessionId.equals("secret")) {
            type = "Secret Id";
            relative_layout_passkey.setVisibility(View.VISIBLE);
            relative_layout_mobile_or_email.setVisibility(View.GONE);
            // startActivity(new Intent(this,RegistrationActivity.class));
            button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button));
            button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button_voilet));
            button_secret_id.setTextColor((Color.parseColor("#000000")));
            button_passkey.setTextColor((Color.parseColor("#FFFFFF")));

        } else {
            type = "passkey";

            relative_layout_passkey.setVisibility(View.GONE);
            relative_layout_mobile_or_email.setVisibility(View.VISIBLE);
            button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button_voilet));
            button_secret_id.setTextColor((Color.parseColor("#FFFFFF")));
            button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button));
            button_passkey.setTextColor((Color.parseColor("#000000")));

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button_secret_id:
                relative_layout_passkey.setVisibility(View.VISIBLE);
                relative_layout_mobile_or_email.setVisibility(View.GONE);
                // startActivity(new Intent(this,Registratiobutton_passkeynActivity.class));
                button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button));
                button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button_voilet));
                button_secret_id.setTextColor((Color.parseColor("#000000")));
                button_passkey.setTextColor((Color.parseColor("#FFFFFF")));

                type = "Secret Id";
                break;


            case R.id.button_passkey:
                relative_layout_passkey.setVisibility(View.GONE);
                relative_layout_mobile_or_email.setVisibility(View.VISIBLE);
                button_secret_id.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button_voilet));
                button_secret_id.setTextColor((Color.parseColor("#FFFFFF")));
                button_passkey.setBackgroundDrawable(ContextCompat.getDrawable(ForgotPasswordActivity.this, R.drawable.signin_button));
                button_passkey.setTextColor((Color.parseColor("#000000")));

                type = "passkey";


                break;
        }

    }

    private void detailsQuestionMarkPopup() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("please select security code from the dropdowm list that was used on the profile screen");
        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void forgotPasswodApiBuzzid(String value) {
        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Please Wait...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (status.equals("false")) {

                                emailedSuccessDialog(message);

                            } else {
                                emailedSuccessDialog(message);

                                //Toast.makeText(LoginActivity.this, "Please Enter Valid User Id or Password", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String sec = "";
                sec = security_code + var7;
                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "forgot_password");
                logParams.put("email", value);
                logParams.put("securitycode", sec);
                logParams.put("rtype", type);
                return logParams;
            }
        };
        MySingleTon.getInstance(ForgotPasswordActivity.this).addToRequestQue(stringRequestLogIn);
    }

    private void forgotPasswodApiPasskey() {
        if (TextUtils.isEmpty(buzz_id)) {
            et_buzz_id.setError("Buzz ID  can't  be empty ");
            et_buzz_id.requestFocus();
            return;
        } else {
            final String PPASSKEY_URL = BaseUrl_ConstantClass.BASE_URL;
            pd = new ProgressDialog(this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            StringRequest stringRequestpasskey = new StringRequest(Request.Method.POST, PPASSKEY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            JSONObject jsonObject1 = null;
                            try {
                                jsonObject1 = new JSONObject(response);
                                String status = jsonObject1.getString("success");
                                String message = jsonObject1.getString("message");
                                if (status.equals("false")) {

                                    //   Toast.makeText(ForgotPasswordActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                                    emailedSuccessDialog(message);

                                    // finish();

                                } else {
                                    emailedSuccessDialog(message);
                                    //Toast.makeText(LoginActivity.this, "Please Enter Valid User Id or Password", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            pd.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_server),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(ForgotPasswordActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    String sec = "";
                    sec = security_code + var7;


                    Map<String, String> logParams = new HashMap<>();
                    logParams.put("action", "forgot_passkey");
                    logParams.put("buzzid", buzz_id);
                    logParams.put("securitycode", sec);


                    return logParams;
                }
            };

            MySingleTon.getInstance(ForgotPasswordActivity.this).addToRequestQue(stringRequestpasskey);


        }

    }

    private void emailedSuccessDialog(final String message) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (message.equals("Email send successfully...")) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginRegistrationActivity.class);
                    startActivity(intent);
                } else {
                    alertDialog.dismiss();
                }


            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
