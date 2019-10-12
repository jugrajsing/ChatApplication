package com.example.hp.chatapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DateFormatSymbols;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.Utils.BaseUrl_ConstantClass;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


@RequiresApi(api = Build.VERSION_CODES.N)
public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_PREFS_NAME = "resident";
    final Calendar c = Calendar.getInstance();
    final int mMonth = c.get(Calendar.MONTH);
    Button closePopupBtn;
    PopupWindow popupWindow;
    ImageView settings_image_view, pop_up_close;
    LinearLayout linearLayout1;
    Button button_public, button_private;
    AlertDialog alertDialog;
    ImageView choose_image_iv, iv_gallery_choose, dialog_imageview;
    CircleImageView user_profile_image_view;
    TextView user_profile_Mobileno, user_profile_email, user_profile_secret_id, User_name;
    RelativeLayout relative_log_text;
    Context context;
    int mDay = c.get(android.icu.util.Calendar.DAY_OF_MONTH);
    int mYear = c.get(android.icu.util.Calendar.YEAR);
    ImageView image_view_select_date, iv_anniversary;
    EditText et_anniversary;
    TextView et_date_of_birth, tv_recidence, tv_gender, work_for_tv, status_user, tv_securityCode;
    RelativeLayout relative_change_password, account_status;
    String secret_id;
    String user_id;
    TextView tv_all_interests;
    String data;
    String profile_pic;
    Spinner spinner_all_interests;
    ArrayAdapter<String> adapter;
    RelativeLayout relative_anniversary;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String token;

    // SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
    @Override
    protected void onStart() {
        super.onStart();
        loadAllDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        relative_anniversary = (RelativeLayout) findViewById(R.id.relative_anniversary);

        tv_all_interests = (TextView) findViewById(R.id.tv_all_interests);
        tv_recidence = (TextView) findViewById(R.id.tv_recidence);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        work_for_tv = (TextView) findViewById(R.id.work_for_tv);
        status_user = (TextView) findViewById(R.id.status_user);
        tv_securityCode = (TextView) findViewById(R.id.user_profile_security_code);
        //spinner_all_interests=(Spinner) findViewById(R.id.spinner_all_interests);
        user_id = SharedPrefManager.getInstance(UserDetailsActivity.this).getUser().getUser_id().toString();
        //  user_image=SharedPrefManager.getInstance(UserDetailsActivity.this).getUser().getUser_image().toString();
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(SessionManager.KEY_Token);

        account_status = (RelativeLayout) findViewById(R.id.account_status);
        account_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, CompleteYourProfile.class);
                intent.putExtra("CHECK", "0");
                intent.putExtra("SECURITY_CODE", tv_securityCode.getText().toString());
                startActivity(intent);

            }
        });

        relative_change_password = (RelativeLayout) findViewById(R.id.relative_change_password);
        relative_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);

            }
        });

        init();
        loadAllDetails();


    }


    private void init() {
        user_profile_image_view = (CircleImageView) findViewById(R.id.user_profile_image_view);
        user_profile_image_view.setOnClickListener(this);

        et_date_of_birth = (TextView) findViewById(R.id.et_date_of_birth);

        et_anniversary = (EditText) findViewById(R.id.et_anniversary);

        iv_anniversary = (ImageView) findViewById(R.id.iv_anniversary);
        iv_anniversary.setOnClickListener(this);


        image_view_select_date = (ImageView) findViewById(R.id.image_view_select_date);
        image_view_select_date.setOnClickListener(this);

        User_name = (TextView) findViewById(R.id.User_name);

        user_profile_Mobileno = (TextView) findViewById(R.id.user_profile_Mobileno);
        String mobile_no = SharedPrefManager.getInstance(UserDetailsActivity.this).getUser().getUser_mobile_no().toString();
        user_profile_Mobileno.setText(mobile_no);

        user_profile_email = (TextView) findViewById(R.id.user_profile_email);
        String email = SharedPrefManager.getInstance(UserDetailsActivity.this).getUser().getUser_email().toString();
        user_profile_email.setText(email);

        user_profile_secret_id = (TextView) findViewById(R.id.user_profile_secret_id);
        secret_id = SharedPrefManager.getInstance(UserDetailsActivity.this).getUser().getUser_secret_id().toString();
        user_profile_secret_id.setText(secret_id);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);

        settings_image_view = (ImageView) findViewById(R.id.settings_image_view);
        settings_image_view.setOnClickListener(this);

        button_public = (Button) findViewById(R.id.button_public);
        button_public.setOnClickListener(this);

        button_private = (Button) findViewById(R.id.button_private);
        button_private.setOnClickListener(this);

        relative_log_text = (RelativeLayout) findViewById(R.id.relative_log_text);
        relative_log_text.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_private: {
                alertDialogPrivate();
                button_private.setBackgroundDrawable(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.signin_button));
                button_public.setBackgroundDrawable(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.signin_button_voilet));
                button_private.setTextColor((Color.parseColor("#000000")));
                button_public.setTextColor((Color.parseColor("#FFFFFF")));

            }
            break;

            case R.id.button_public: {
                alertDialogPublic();
                button_private.setBackgroundDrawable(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.signin_button_voilet));
                button_private.setTextColor((Color.parseColor("#FFFFFF")));
                button_public.setBackgroundDrawable(ContextCompat.getDrawable(UserDetailsActivity.this, R.drawable.signin_button));
                button_public.setTextColor((Color.parseColor("#000000")));

            }
            break;

            case R.id.settings_image_view:
                popupShow();
                break;

            case R.id.relative_log_text:
                logout();
                break;

            case R.id.user_profile_image_view:
                openImageOnPopUp();
                break;

            case R.id.iv_anniversary:
                break;

        }

    }

    private void popupShow() {
        if (checkPermission1()) {
            //instantiate the popup.xml layout file
            LayoutInflater layoutInflater = (LayoutInflater) UserDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.popup_setting_layout, null);

            pop_up_close = (ImageView) customView.findViewById(R.id.pop_up_close);


            //instantiate popup window
            popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //display the popup window
            popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
            p.setMargins(30, 0, 30, 0);


            //close the popup window on button click
            pop_up_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });

            choose_image_iv = (ImageView) customView.findViewById(R.id.choose_image_iv);
            choose_image_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);*/
                    Intent intent = new Intent(UserDetailsActivity.this, ImageCroperActivty.class);
                    intent.putExtra("DATA", "One");
                    startActivity(intent);
                    popupWindow.dismiss();


                }
            });
            iv_gallery_choose = (ImageView) customView.findViewById(R.id.iv_gallery_choose);
            iv_gallery_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);*/
                    Intent intent = new Intent(UserDetailsActivity.this, ImageCroperActivty.class);
                    intent.putExtra("DATA", "Two");
                    startActivity(intent);
                    popupWindow.dismiss();

                }
            });


        } else {
            requestPermission1();
        }


    }

    private void alertDialogPublic() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are visible, your friends can easily find you on Buzz!");
        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void alertDialogPrivate() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are invisible, your friends can't find you on Buzz!");
        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logout() {

        if (SharedPrefManager.getInstance(UserDetailsActivity.this).isLoggedIn() == false) {
            startActivity(new Intent(UserDetailsActivity.this, LoginRegistrationActivity.class));
            finish();
        } else {
            SharedPrefManager.getInstance(UserDetailsActivity.this).logout();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    private void dobPicker() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,

                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {
                        //String month=c.getDisplayName(monthOfYear,Calendar.SHORT, Locale.US);
                        String month = new DateFormatSymbols().getMonths()[(monthOfYear + 1) - 1];
                        //    String mon=setFullMonthName(month);
                        //  date_time = year + "-" + (mon) + "-" + dayOfMonth;
                        //*************Call Time Picker Here ********************
                        //tiemPicker();

                        // et_date_of_birth.setText(year + "-" + (month) + "-" + dayOfMonth);
                        et_date_of_birth.setText(dayOfMonth + "-" + (month) + "-" + year);

                    }

                }, mDay, mYear, mYear);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }

    private void annversaryPickerDialog() {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,

                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //String month=c.getDisplayName(monthOfYear,Calendar.SHORT, Locale.US);
                        String month = new DateFormatSymbols().getMonths()[(monthOfYear + 1) - 1];
                        //    String mon=setFullMonthName(month);
                        //  date_time = year + "-" + (mon) + "-" + dayOfMonth;
                        //*************Call Time Picker Here ********************
                        //tiemPicker();

                        et_anniversary.setText(year + "-" + (month) + "-" + dayOfMonth);

                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }


    private void loadAllDetails() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Userres", response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equals("true")) {
                                JSONObject user_details = jsonObject.getJSONObject("user_details");
                                String user_id = user_details.optString("id");
                                String secrate_id = user_details.optString("secrate_id");
                                String mobileno = user_details.optString("mobileno");
                                String email = user_details.optString("email");
                                String name = user_details.optString("name");
                                String marital = user_details.optString("marital");
                                String gender = user_details.optString("gender");
                                String dob = user_details.optString("dob");
                                String anniversary = user_details.optString("anniversary");
                                String workfor = user_details.optString("workfor");
                                String resident = user_details.optString("resident");
                                String security_code = user_details.optString("security_code");
                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("residentid", resident);
                                editor.apply();

                                profile_pic = user_details.optString("user_img");

                                if (profile_pic != null) {
                                    updateCurrentUserInfo(name, profile_pic);
                                    Glide.with(UserDetailsActivity.this).load(profile_pic).into(user_profile_image_view);

                                } else {
                                    user_profile_image_view.setBackgroundResource(R.drawable.app_buzz_logo);


                                }

                                String instrests = user_details.optString("instrests");
                                JSONArray jsonArray = new JSONArray(instrests);
                                String[] strArr = new String[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    strArr[i] = jsonArray.getString(i);
                                }

                                System.out.println(Arrays.toString(strArr));
                                String strArray[] = strArr;

                                List<String> interest = new ArrayList<String>(Arrays.asList(strArray));

                                StringBuilder sb = new StringBuilder();
                                int size = interest.size();
                                boolean appendSeparator = false;
                                for (int y = 0; y < size; y++) {
                                    if (appendSeparator)
                                        sb.append(','); // a comma
                                    appendSeparator = true;
                                    sb.append(interest.get(y));
                                }
                                tv_all_interests.setText(sb.toString());

                              /*  List<String> interest = new ArrayList<String>(Arrays.asList(strArray));
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UserDetailsActivity.this,   android.R.layout.simple_spinner_item, interest);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spinner_all_interests.setAdapter(spinnerArrayAdapter);
*/


                                User_name.setText(name);
                                et_date_of_birth.setText(dob);
                                work_for_tv.setText(workfor);
                                tv_recidence.setText(resident);
                                tv_gender.setText(gender);
                                status_user.setText(marital);

                                if (status_user.getText().toString().equals("Single")) {
                                    relative_anniversary.setVisibility(View.GONE);
                                } else {
                                    relative_anniversary.setVisibility(View.VISIBLE);
                                }

                                tv_securityCode.setText(security_code);


                            } else {
                                Toast.makeText(UserDetailsActivity.this, "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(UserDetailsActivity.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(UserDetailsActivity.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(UserDetailsActivity.this, "" + getString(R.string.error_network_timeout),
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
                logParams.put("action", "getuser");
                logParams.put("userid", user_id);
                return logParams;
            }
        };

        MySingleTon.getInstance(UserDetailsActivity.this).addToRequestQue(stringRequestLogIn);

    }

    private void updateCurrentUserInfo(String userNickname, String image_url) {

        SendBird.updateCurrentUserInfo(userNickname, image_url, new SendBird.UserInfoUpdateHandler() {
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

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    Intent intent=new Intent(UserDetailsActivity.this,ImageCroperActivty.class);
                    intent.putExtra("DATA", "One");
                    startActivity(intent);

                   *//* user_profile_image_view.setImageBitmap(bitmap);
                   popupWindow.dismiss();*//*

               }

               break;
           case 1:
               if (resultCode == RESULT_OK) {

                   Intent intent=new Intent(UserDetailsActivity.this,ImageCroperActivty.class);
                   intent.putExtra("DATA", "Two");
                   startActivity(intent);
                  *//* Intent intent=new Intent(UserDetailsActivity.this,ImageCroperActivty.class);
                   intent.putExtra("Selected_Image_From_Gallery", "data");
                   startActivity(intent);*//*
     *//* Uri selectedImage = data.getData();
                   user_profile_image_view.setImageURI(selectedImage);
                   popupWindow.dismiss();*//*
               }
               break;
       }
   }*/
    private void requestPermission1() {
        ActivityCompat.requestPermissions(UserDetailsActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, 5);


    }

    public boolean checkPermission1() {

        int result8 = ContextCompat.checkSelfPermission(UserDetailsActivity.this, READ_EXTERNAL_STORAGE);
        int result9 = ContextCompat.checkSelfPermission(UserDetailsActivity.this, WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(UserDetailsActivity.this, CAMERA);
        return result8 == PackageManager.PERMISSION_GRANTED &&
                result9 == PackageManager.PERMISSION_GRANTED &&
                result == PackageManager.PERMISSION_GRANTED;
//
    }

    private void openImageOnPopUp() {

        final android.app.AlertDialog.Builder alertadd = new android.app.AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);

        final View view = factory.inflate(R.layout.image_on_popup, null);
        dialog_imageview = (ImageView) view.findViewById(R.id.dialog_imageview);
        //   Glide.with(UserDetailsActivity.this).load(profile_pic).into(dialog_imageview);
        if (profile_pic != null) {
            Glide.with(UserDetailsActivity.this).load(profile_pic).into(dialog_imageview);

        } else {
            user_profile_image_view.setBackgroundResource(R.drawable.app_buzz_logo);


        }


        alertadd.setView(view);
        alertadd.show();

    }

}