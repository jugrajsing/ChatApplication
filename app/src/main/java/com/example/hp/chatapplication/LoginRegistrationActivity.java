package com.example.hp.chatapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.example.hp.chatapplication.ModelClasses.User;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.example.hp.chatapplication.VideoCall.services.CallService;
import com.example.hp.chatapplication.VideoCall.util.QBResRequestExecutor;
import com.example.hp.chatapplication.VideoCall.utils.Consts;
import com.example.hp.chatapplication.VideoCall.utils.QBEntityCallbackImpl;
import com.example.hp.chatapplication.VideoCall.utils.SharedPrefsHelper;
import com.example.hp.chatapplication.VideoCall.utils.UsersUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.helper.Utils;
import com.quickblox.users.model.QBUser;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class LoginRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String URL = BaseUrl_ConstantClass.BASE_URL;
    static String deviceId = "NA";
    protected QBResRequestExecutor requestExecutor;
    EditText current_user_id, current_user_passkey;
    Button sign_in, signIn_with_finger_print, sign_in_page_button;
    TextView sign_up_text;
    // TextView forgetPassword;
    String user_name, user_email, user_mobile;
    ProgressDialog progressDialog;
    ProgressDialog pd;
    AlertDialog alertDialog;
    TextView forget_password;
    LinearLayout user_login_layout, user_registration_layout;
    Button sign_up_button;
    EditText secret_ID, mobile_no, email, passkey;
    Button button_sign_in;
    //our URL
    String message;
    ImageView iv_view_passkey;
    PopupWindow popupWindow;
    ImageView country_code_popup_iv;
    RelativeLayout relativeLayout_login;
    ImageView hide_password;
    LinearLayout ll_secret_id, ll_Passkey;
    Button button_SignUp_regiter;
    TelephonyManager telephonyManager;
    View view;
    TextView Sign_in_SignUp_text;
    String user_id;

    SharedPreferences settings;
    String code12 = "+44";
    String country_code;
    SessionManager sessionManager;
    boolean firstRun;
    SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
    private Boolean isClicked = false;
    private QBUser userForSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestExecutor = new QBResRequestExecutor();


        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        String[] years = {"+44", "+355", "+213", "+1684", "+376", "+244", "+1264", "+672", " +1268", "+54", "+374", "+297", "+297", "+61", "+43", "+994", "+1242", "+973", "+880", "+1246", "+375",
                "+32", "+32", "+501", "+229", "+1441", "+975", "+591", "+387", "+267", "+55", "+246", "+1284", "+673", "+359", "+226", "+257", "+855", "+237", "+1", "+238", "+1345", "+236",
                "+235", "+56", "+86", "+61", "+57", "+269", "+682", "+506", "+385", "+53", "+599", "+357", "+420", "+243", "+45", "+253", "+1767", "+1809", "+670", "+593", "+20", "+503", "+240",
                "+224", "+245", "+592", "+509", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+53", "+441624", "+972", "+39", "+225", "+1876", "+81", "+441534", "+962",
                "+7", "+254", "+686", "+383", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60",
                "+960", "+223", "+356", "+692", "+222", "+230", "+262", "+52", "+691", "+373", "+377", "+976", "+382", "+1664", "+212", "+258", "+95", "+264", "+674", "+977", "+31", "+599",
                "+687", "+64", "+505", "+227", "+234", "+683", "+850", "+1670", "+47", "+968", "+92", "+680", "+970", "+507", "+675", "+595", "+51", "+63", "+64", "+48", "+351", "+1787",
                "+974", "+242", "+262", "+40", "+7", "+250", "+590", "+290", "+1869", "+1758", "+590", "+508", "+1784", "+685", "+378", "+239", "+966", "+221", "+381", "+248",
                "+232", "+65", "+1721", "+421", "+386", "+677", "+252", "+27", "+82", "+211", "+34", "+94", "+249",
                "+597", "+47", "+268", "+46", "+41", "+963", "+886", "+992", "+255", "+66", "+228", "+690", "+676", "+1868", "+216", "+90", "+993", "+1649", "+688", "+1340", "+256",
                "+380", "+971", "+93", "+1", "+598", "+998", "+678", "+379", "+58", "+84", "+967", "+260", "+263"};


        String[] value = new HashSet<String>(Arrays.asList(years)).toArray(new String[0]);
        Arrays.sort(value);


        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(LoginRegistrationActivity.this, R.layout.spinner_text, value);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);
        // arrCode = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.country_code)));

        sessionManager = new SessionManager(getApplicationContext());

      /*  country_code_spinner=(Spinner)findViewById(R.id.country_code_spinner);
        country_code_spinner.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        country_code_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                code12 = (String) parent.getItemAtPosition(position);
                //Toast.makeText(LoginRegistrationActivity.this, ""+code12, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
*/
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrCode);
        adapte"+291","+372","+251","+500","+298","+679","+358","+33","+689","+241","+220","+995","+49","+233","+350","+30","+299","+1473","+1671","+502","+441481",

        r.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_code_spinner.setAdapter(adapter);*/
      /*  List<String> list = new ArrayList<String>();
        list.add("A");list.add("B");list.add("C");list.add("D");list.add("E");list.add("F");list.add("G");list.add("H");
        list.add("I");list.add("J");list.add("K");list.add("L");list.add("M");list.add("N");list.add("O");list.add("P");
        list.add("Q");list.add("R");list.add("S");list.add("T");list.add("U");list.add("V");list.add("W");list.add("X");
        list.add("Y");list.add("Z");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_code_spinner.setAdapter(adapter);*/


        hideSoftKeyboard();

        Sign_in_SignUp_text = (TextView) findViewById(R.id.Sign_in_SignUp_text);

        button_SignUp_regiter = (Button) findViewById(R.id.button_SignUp_regiter);
        button_SignUp_regiter.setOnClickListener(this);

        iv_view_passkey = (ImageView) findViewById(R.id.iv_view_passkey);
        iv_view_passkey.setOnClickListener(this);

        /*
         * getDeviceId() returns the unique device ID.
         * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            //TODO
            deviceId = telephonyManager.getDeviceId();
        }

        ll_secret_id = (LinearLayout) findViewById(R.id.ll_secret_id);
        ll_secret_id.setOnClickListener(this);
        ll_Passkey = (LinearLayout) findViewById(R.id.ll_Passkey);
        ll_Passkey.setOnClickListener(this);
        hide_password = (ImageView) findViewById(R.id.hide_password);
        hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    current_user_passkey.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    current_user_passkey.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        iv_view_passkey = (ImageView) findViewById(R.id.iv_view_passkey);
        iv_view_passkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = isClicked ? false : true;
                if (isClicked) {
                    passkey.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passkey.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });


        relativeLayout_login = (RelativeLayout) findViewById(R.id.relativeLayout_login);

       /* country_code_list=(ImageView)findViewById(R.id.country_code_list);
        country_code_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupShow();

            }
        });*/

        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(this);

        //registration Activity PArt here
        // name=(EditText) findViewById(R.id.user_name);
        email = (EditText) findViewById(R.id.user_email);
        mobile_no = (EditText) findViewById(R.id.user_mobile);
        secret_ID = (EditText) findViewById(R.id.secret_id);
        passkey = (EditText) findViewById(R.id.user_passkey);
        // confirm_passkey=(EditText) findViewById(R.id.user_confirm_passkey);
       /* register=(Button)findViewById(R.id.button_SignUp);//sign up
        register.setOnClickListener(this);*/

        /////////////////////////////////////////////////////

        user_registration_layout = (LinearLayout) findViewById(R.id.user_registration_layout);
        user_login_layout = (LinearLayout) findViewById(R.id.user_login_layout);
        sign_in_page_button = (Button) findViewById(R.id.sign_in_page_button);
        sign_in_page_button.setOnClickListener(this);

        progressDialog = new ProgressDialog(LoginRegistrationActivity.this);
        progressDialog.setMessage("Loading..."); // Message
        //  progressDialog.setTitle("ProgressDialog"); // Title
        //   progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        signIn_with_finger_print = (Button) findViewById(R.id.signIn_with_finger_print);
        signIn_with_finger_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginRegistrationActivity.this, SignInWithFingerPrint.class);
                intent.putExtra("DEVICE_ID", deviceId);
                startActivity(intent);

            }
        });

       /* forget_password=(TextView)findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
            }
        });*/

        current_user_id = (EditText) findViewById(R.id.current_user_id);
        current_user_passkey = (EditText) findViewById(R.id.current_user_passkey);
        sign_in = (Button) findViewById(R.id.sign_in_button);

        sign_up_text = (TextView) findViewById(R.id.sign_up_button);
        sign_up_text.setOnClickListener(this);

        sign_in.setOnClickListener(this);

        if (SharedPrefManager.getInstance(this).isConnected() != null) {
            if (SharedPrefManager.getInstance(this).isConnected().equals("true")) {

                startActivity(new Intent(LoginRegistrationActivity.this, UserNavgation.class));
                finish();
            } else {
                SharedPrefManager.getInstance(LoginRegistrationActivity.this).logout();
            }
        }
        /*   current_user_id.setText(SharedPrefManager.getInstance(this).getUser().getUser_name());
           current_user_passkey.setText(SharedPrefManager.getInstance(this).getUser().getUser_mobile_no());*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    deviceId = telephonyManager.getDeviceId();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sign_in_button: {

                logInUser();

                break;
            }
            case R.id.sign_up_button: {
                current_user_id.setText("");
                current_user_id.setError(null);
                current_user_passkey.setText("");
                current_user_passkey.setError(null);
                user_registration_layout.setVisibility(View.VISIBLE);
                user_login_layout.setVisibility(View.GONE);
                // startActivity(new Intent(this,RegistrationActivity.class));
                sign_up_button.setBackgroundDrawable(ContextCompat.getDrawable(LoginRegistrationActivity.this, R.drawable.signin_button));
                sign_in_page_button.setBackgroundDrawable(ContextCompat.getDrawable(LoginRegistrationActivity.this, R.drawable.signin_button_voilet));
                sign_up_button.setTextColor((Color.parseColor("#000000")));
                sign_in_page_button.setTextColor((Color.parseColor("#FFFFFF")));
                Sign_in_SignUp_text.setText("Sign up");


                break;
            }
            case R.id.sign_in_page_button: {
                secret_ID.setError(null);
                mobile_no.setError(null);
                email.setError(null);
                passkey.setError(null);
                secret_ID.setText("");
                mobile_no.setText("");
                email.setText("");
                passkey.setText("");
                user_registration_layout.setVisibility(View.GONE);
                user_login_layout.setVisibility(View.VISIBLE);
                sign_up_button.setBackgroundDrawable(ContextCompat.getDrawable(LoginRegistrationActivity.this, R.drawable.signin_button_voilet));
                sign_up_button.setTextColor((Color.parseColor("#FFFFFF")));
                sign_in_page_button.setBackgroundDrawable(ContextCompat.getDrawable(LoginRegistrationActivity.this, R.drawable.signin_button));
                sign_in_page_button.setTextColor((Color.parseColor("#000000")));
                Sign_in_SignUp_text.setText("Sign in");


            }
            break;

            case R.id.ll_secret_id: {
                //startActivity(new Intent(LoginActivity.this,ChangePasswordActivity.class));
                Intent intent = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", "secret");
                startActivity(intent);

            }
            break;
            case R.id.ll_Passkey: {
                //startActivity(new Intent(LoginActivity.this,ChangePasswordActivity.class));
                Intent intent = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", "pass");
                startActivity(intent);

            }
            break;

            case R.id.button_SignUp_regiter: {
                registerNewUser();
            }
            break;
        }

    }

    private void changePassword() {
        //startActivity(new Intent(this,BlankActivity.class));
    }

    private void logInUser() {
        final String currentUser = current_user_id.getText().toString().trim();
        final String currentUserPasskey = current_user_passkey.getText().toString().trim();
        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;

        if (TextUtils.isEmpty(currentUser)) {
            current_user_id.setError("Buzz ID can't be empty ");
            current_user_id.requestFocus();
            return;
        } else if (TextUtils.isEmpty(currentUserPasskey)) {
            current_user_passkey.setError("PassKey can't  be empty");
            current_user_passkey.requestFocus();
            return;
        } else {
            //   progressDialog.show();
            pd = new ProgressDialog(this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Connecting...");
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

                                if (status.equals("true")) {
                                    JSONObject user_details = jsonObject.getJSONObject("user_details");
                                    user_id = user_details.getString("id");
                                    String name = user_details.getString("name");
                                    String email = user_details.getString("email");
                                    String mobile = user_details.getString("mobileno");
                                    String user_secrate_id = user_details.getString("secrate_id");
                                    String password = user_details.getString("password");
                                    String resident = user_details.getString("resident");
                                    //   String user_image=user_details.getString("user_img");

                                    sessionManager.createLoginSession(user_id);


                                    // Toast.makeText(LoginRegistrationActivity.this, "Name" + response, Toast.LENGTH_SHORT).show();

                                    User currentUser = new User(user_id, name, email, mobile, password, user_secrate_id, "", resident, "true");
                                    SharedPrefManager.getInstance(LoginRegistrationActivity.this).userLogin(currentUser);
                                    loginToChat1(createUserWithEnteredData());
                                    // startSignUpNewUser(createUserWithEnteredData());
                                    Intent intent = new Intent(LoginRegistrationActivity.this, UserNavgation.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(LoginRegistrationActivity.this);
                                    ab.setMessage("Please enter your valid Buzz ID or Passkey");
                                    ab.setCancelable(false);
                                    ab.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ab.setCancelable(true);
                                        }
                                    });
                                    ab.create();
                                    ab.show();

                                  /*  Snackbar snackbar1 = Snackbar.make(this, "Message is restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/

                                    // Toast.makeText(LoginRegistrationActivity.this, "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_server),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
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
                    logParams.put("action", "login");
                    logParams.put("secrate_id", currentUser);
                    logParams.put("password", currentUserPasskey);

                    return logParams;
                }
            };

            MySingleTon.getInstance(LoginRegistrationActivity.this).addToRequestQue(stringRequestLogIn);
            progressDialog.dismiss();
        }
    }

    private void startSignUpNewUser(final QBUser newUser) {

        requestExecutor.signInUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        loginToChat(result);
                        saveUserData(newUser);
                        callerid(newUser);

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser, true);
                        } else {


                        }
                    }
                }
        );
    }


    public void callerid(QBUser newUser) {

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, "bns.ooo/chat/chat.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");

                            if (status.equals("true")) {
                                JSONObject user_details = jsonObject.getJSONObject("user_details");
                                user_id = user_details.getString("id");

                                sessionManager.createLoginSession(user_id);

                            } else {

                                  /*  Snackbar snackbar1 = Snackbar.make(this, "Message is restored!", Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/

                                // Toast.makeText(LoginRegistrationActivity.this, "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
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
                logParams.put("action", "update_call");
                logParams.put("userid", user_id);
                logParams.put("call_id", newUser.getId().toString());

                return logParams;
            }
        };

        MySingleTon.getInstance(LoginRegistrationActivity.this).addToRequestQue(stringRequestLogIn);
        progressDialog.dismiss();

    }


    private void signInCreatedUser(final QBUser user, final boolean deleteCurrentUser) {

        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {

                //   if (deleteCurrentUser) {
                removeAllUserData(result);
                //  }

                //   removeAllUserData(result);

            }

            @Override
            public void onError(QBResponseException responseException) {

                Toast.makeText(LoginRegistrationActivity.this, R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeAllUserData(final QBUser user) {
        requestExecutor.deleteCurrentUser(user.getId(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                UsersUtils.removeUserData(getApplicationContext());
                startSignUpNewUser(createUserWithEnteredData());
            }

            @Override
            public void onError(QBResponseException e) {
                //  hideProgressDialog();
                Toast.makeText(LoginRegistrationActivity.this
                        , R.string.sign_up_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginToChat1(QBUser qbUser) {
        QBChatService.getInstance().login(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d("LOG", "login onSuccess");
                loginToChat(qbUser);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("LOG", "login onError " + e.getMessage());

            }
        });
    }

    private void loginToChat(final QBUser qbUser) {
        qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

        userForSave = qbUser;
        saveUserData(userForSave);
        startLoginService(qbUser);
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, CallService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        CallService.start(this, qbUser, pendingIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {

            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);

                signInCreatedUser(userForSave, false);
            } else {

            }

            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                if (SharedPrefManager.getInstance(LoginRegistrationActivity.this).isLoggedIn() == true) {
                    startActivity(new Intent(LoginRegistrationActivity.this, UserNavgation.class));
                    finish();
                } else {
                    SharedPrefManager.getInstance(LoginRegistrationActivity.this).logout();
                }
        /*   current_user_id.setText(SharedPrefManager.getInstance(this).getUser().getUser_name());
           current_user_passkey.setText(SharedPrefManager.getInstance(this).getUser().getUser_mobile_no());*/
            }
        }
    }


    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(SharedPrefManager.getInstance(LoginRegistrationActivity.this).getUser().getUser_secret_id(),
                String.valueOf("Buzz"));
    }

    private QBUser createQBUserWithCurrentData(String userName, String chatRoomName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(chatRoomName)) {
            StringifyArrayList<String> userTags = new StringifyArrayList<>();
            userTags.add(chatRoomName);
            qbUser = new QBUser();
            qbUser.setFullName(userName);
            qbUser.setLogin(getCurrentDeviceId());
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);
            qbUser.setTags(userTags);
            String id = SharedPrefManager.getInstance(LoginRegistrationActivity.this).getUser().getUser_id();
            qbUser.setId((Integer.valueOf(id)));


        }

        return qbUser;
    }

    private void saveUserData(QBUser qbUser) {

        sharedPrefsHelper.save(Consts.PREF_CURREN_ROOM_NAME, qbUser.getTags().get(0));
        sharedPrefsHelper.saveQbUser(qbUser);
        connectsendBirds();


    }

    private void connectsendBirds() {
        //    String secret_id=SharedPrefManager.getInstance(MainActivity.this).getUser().getUser_secret_id().toString();

        String userId = SharedPrefManager.getInstance(LoginRegistrationActivity.this).getUser().getUser_id().toString();
        if (sharedPrefsHelper.hasQbUser())
            userId = String.valueOf(sharedPrefsHelper.getQbUser().getId());
        userId = userId.replaceAll("\\s", "");

        String userNickname = SharedPrefManager.getInstance(LoginRegistrationActivity.this).getUser().getUser_name().toString();
        connectToSendBird(userId, userNickname);
    }

    private void connectToSendBird(String userId, final String userNickname) {

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(com.sendbird.android.User user, SendBirdException e) {
                if (e != null) {
                    // Error!
                    //    Toast.makeText(MainActivity.this, "" + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Show login failure snackbar
                    //  showSnackbar("Login to SendBird failed");
                    //   mConnectButton.setEnabled(true);
                    //    PreferenceUtils.setConnected(MainActivity.this, false);
                    return;
                }
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();
            }

        });
    }


    private void updateCurrentUserPushToken() {
        // Register Firebase Token
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(),
                new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            //          Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //    Toast.makeText(MainActivity.this, "Push token registered.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Updates the user's nickname.
     *
     * @param userNickname The new nickname of the user.
     */
    private void updateCurrentUserInfo(String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
//               //     Toast.makeText(
//                            MainActivity.this, "" + e.getCode() + ":" + e.getMessage(),
//                            Toast.LENGTH_SHORT)
//                            .show();
                    // Show update failed snackbar
                    //  showSnackbar("Update user nickname failed");

                    return;
                }

            }
        });
    }

    private String getCurrentDeviceId() {
        return Utils.generateDeviceId(this);
    }
/*
    @Override
    public void onBackPressed() {

        //  startActivity(new Intent(LoginActivity.this, MainActivity.class));
        super.onBackPressed();
    }
*/

    private void registerNewUser() {

        //  final String user_name=name.getText().toString().trim();
        final String user_mobile = mobile_no.getText().toString().trim();
        final String user_email = email.getText().toString().trim();
        final String user_secret_ID = secret_ID.getText().toString().trim();
        final String user_passkey = passkey.getText().toString().trim();

        if (TextUtils.isEmpty(user_secret_ID)) {
            secret_ID.setError("User Buzz ID Can't  be empty");
            secret_ID.requestFocus();
            return;

        } else if (TextUtils.isEmpty(user_mobile)) {
            mobile_no.setError("Mobile Number can't be empty");
            mobile_no.requestFocus();
            return;
        } else if (TextUtils.isEmpty(user_email)) {

            email.setError("User Email Can't  be empty");
            email.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            email.setError("Please Enter Valid Email");
            email.requestFocus();
            return;
        } else if (TextUtils.isEmpty(user_passkey)) {
            passkey.setError("Passkey can't be empty");
            passkey.requestFocus();
            return;
        }
      /*  else if(TextUtils.isEmpty(user_mobile)&&TextUtils.isEmpty(user_email)&&TextUtils.isEmpty(user_secret_ID)&&TextUtils.isEmpty(user_passkey)){
            passkey.setError("Please fill all details");
            passkey.requestFocus();
            return;

        }*/
        else {
            pd = new ProgressDialog(this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("success");
                                message = jsonObject.getString("message");
                                if (status.equals("true")) {
                                    String userId = jsonObject.getString("userid");
                                    String secrate_id = jsonObject.getString("secrate_id");

                                    User currentUser = new User(userId, " ", email.getText().toString(), mobile_no.getText().toString(), "", secrate_id, "", "", "false");
                                    SharedPrefManager.getInstance(LoginRegistrationActivity.this).userLogin(currentUser);
                                    startSignUpNewUser(createUserWithEnteredData());
                                    // Toast.makeText(LoginRegistrationActivity.this, "You Have Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginRegistrationActivity.this, OptVerificationsCustom.class));
                                } else {
                                    alertDialog(message);
                                    // Toast.mfakeText(LoginRegistrationActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                //TODO
                            } else if (error instanceof ServerError) {
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_server),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(LoginRegistrationActivity.this, "" + getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }

                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //  String mobile1 = code12 + user_mobile;
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "register");
                    params.put("user_name", "UserName");
                    params.put("secrate_id", user_secret_ID);
                    params.put("email", user_email);
                    params.put("mobile", user_mobile);
                    params.put("password", user_passkey);
                    params.put("device_id", deviceId);
                    return params;
                }
            };
            MySingleTon.getInstance(LoginRegistrationActivity.this).addToRequestQue(stringRequest);

            // finish();
        }
    }

    private void alertDialog(String message) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void popupShow() {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) LoginRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.country_code_pop_up, null);
        country_code_popup_iv = (ImageView) customView.findViewById(R.id.country_code_popup_iv);
        //instantiate popup window
        popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow.showAtLocation(relativeLayout_login, Gravity.CENTER, 0, 0);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
        p.setMargins(30, 0, 30, 0);
        //close the popup window on button click
        country_code_popup_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void alertDialogSuccess() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You have registered successfully");
        alertDialogBuilder.setNegativeButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(LoginRegistrationActivity.this, AccountVarificationViaMobile.class));

                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void StartAnimations() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstRun == false) //if running for first time
                {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstRun", true);
                    editor.commit();

                 /*   Intent intent_mainpage = new Intent(SplashActivity.this, LoginActivity.class);
                    intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_mainpage.putExtra("company_logo", company_logo);
                    startActivity(intent_mainpage);
                    overridePendingTransition(R.anim.translation2, R.anim.translation);*/

                    //  SplashActivity.this.finish();
                } else {

                    if (!sessionManager.isLoggedIn()) {
                       /* // user is not logged in redirect him to Login Activity
                        Intent intent_mainpage = new Intent(SplashActivity.this, LoginActivity.class);
                        intent_mainpage.putExtra("company_logo", company_logo);
                        intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_mainpage);
                        overridePendingTransition(R.anim.translation2, R.anim.translation);*/
                    } else {
                        Intent intent_mainpage = new Intent(LoginRegistrationActivity.this, UserNavgation.class);
                        intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_mainpage);
                        // overridePendingTransition(R.anim.translation2, R.anim.translation);
                    }
                }
                //  finish();
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }


}
