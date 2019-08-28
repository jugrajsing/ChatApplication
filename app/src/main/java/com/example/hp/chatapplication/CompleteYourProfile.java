package com.example.hp.chatapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormatSymbols;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CompleteYourProfile extends AppCompatActivity implements View.OnClickListener {

    final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
    final int mMonth = c.get(android.icu.util.Calendar.MONTH);
    Spinner alphabetical_letters;
    RelativeLayout anniversary_relative;
    RadioGroup radioGrp_sex, radioGrp_marital;
    RadioButton radioSexButton, radiomaritalstatus;
    int selectedId;
    int mYear = c.get(android.icu.util.Calendar.YEAR);
    int mDay = c.get(android.icu.util.Calendar.DAY_OF_MONTH);
    EditText et_anniversary, et_user_name, et_work_for, et_resident;
    ImageView date_anniversary;
    List<String> gameList = new ArrayList<>();
    Spinner spinner_interest_one, spinner_interest_two, spinner_interest_three, spinner_interest_four, spinner_interest_five, spinner_interest_six;
    ArrayAdapter<String> adapter_interest;
    String var1 = "", var2 = "", var3 = "", var4 = "", var5 = "", var6 = "", var7 = "";
    ArrayList<String> arrayList_spinner_interest = new ArrayList<>();
    TextView date_of_birth;
    String gender1 = "Male";
    String user_id, mobile_number, email, alphabeticalletters;

    String marital_status = "Single";

    // String []security_combined= new String[] {alphabeticalletters,numeric_CODE};
    String security_code = "A";
    Button send_data;

    Spinner number_spinner;
    int i;
    ArrayList<String> aListNumbers;
    ArrayAdapter<String> adapter;

    String check;

    LinearLayout liner_security_code_hide;

    RadioButton radioM, radioF, radio_Single, radio_married;


    String secret_id;

    String sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_your_profile);

        secret_id = SharedPrefManager.getInstance(CompleteYourProfile.this).getUser().getUser_secret_id().toString();

        radioF = (RadioButton) findViewById(R.id.radioF);
        radioM = (RadioButton) findViewById(R.id.radioM);

        radio_married = (RadioButton) findViewById(R.id.radio_married);
        radio_Single = (RadioButton) findViewById(R.id.radio_Single);

        liner_security_code_hide = (LinearLayout) findViewById(R.id.liner_security_code_hide);

        check = getIntent().getStringExtra("CHECK");
        if (check.equals("0")) {
            liner_security_code_hide.setVisibility(View.GONE);
            sec = getIntent().getStringExtra("SECURITY_CODE");

        } else {
            liner_security_code_hide.setVisibility(View.VISIBLE);
        }


        Integer[] testArray = new Integer[1000];
        aListNumbers = new ArrayList<String>();

        for (i = 0; i < testArray.length; i++) {
            testArray[i] = i;
            String formated = String.format("%03d", i);//here all list print one by one with loop i have to pass in spinner
            aListNumbers.add(formated);
        }


        number_spinner = (Spinner) findViewById(R.id.number_spinner);
        number_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!adapter.isEmpty()) {
                    // var1=adapter.getItem(position).toString();
                    //arrayList_spinner_interest.add(var1);

                    var7 = number_spinner.getSelectedItem().toString();
                    sec = alphabeticalletters + var7;

                    // Toast.makeText(CompleteYourProfile.this, ""+var1, Toast.LENGTH_SHORT).show();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_interest_one = (Spinner) findViewById(R.id.spinner_interest_one);
        spinner_interest_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var1 = spinner_interest_one.getSelectedItem().toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_interest_two = (Spinner) findViewById(R.id.spinner_interest_two);
        spinner_interest_two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var2 = (String) parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_interest_three = (Spinner) findViewById(R.id.spinner_interest_three);
        spinner_interest_three.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var3 = (String) parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_interest_four = (Spinner) findViewById(R.id.spinner_interest_four);
        spinner_interest_four.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var4 = (String) parent.getItemAtPosition(position);


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_interest_five = (Spinner) findViewById(R.id.spinner_interest_five);
        spinner_interest_five.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var5 = (String) parent.getItemAtPosition(position);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_interest_six = (Spinner) findViewById(R.id.spinner_interest_six);
        spinner_interest_six.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                var6 = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        user_id = SharedPrefManager.getInstance(CompleteYourProfile.this).getUser().getUser_id().toString();
        mobile_number = SharedPrefManager.getInstance(CompleteYourProfile.this).getUser().getUser_mobile_no().toString();
        email = SharedPrefManager.getInstance(CompleteYourProfile.this).getUser().getUser_email().toString();


        radioGrp_marital = (RadioGroup) findViewById(R.id.radioGrp_marital);
        radioGrp_marital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                selectedId = radioGrp_marital.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);

                if (radioSexButton.getText().toString().equals("Single")) {
                    //    Toast.makeText(CompleteYourProfile.this,radiomaritalstatus.getText(),Toast.LENGTH_SHORT).show();
                    marital_status = "Single";
                    anniversary_relative.setVisibility(View.GONE);
                } else if (radioSexButton.getText().toString().equals("Married")) {
                    //   Toast.makeText(CompleteYourProfile.this,radiomaritalstatus.getText(),Toast.LENGTH_SHORT).show();
                    marital_status = "Married";
                    anniversary_relative.setVisibility(View.VISIBLE);
                }


            }
        });

        alphabetical_letters = (Spinner) findViewById(R.id.alphabets_spinner);
        alphabetical_letters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                alphabeticalletters = (String) parent.getItemAtPosition(position);
                // Toast.makeText(CompleteYourProfile.this, ""+alphabeticalletters, Toast.LENGTH_SHORT).show();
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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alphabetical_letters.setAdapter(adapter1);
        getAllInterests();

        genderFun();
        loadAllDetails();
        init();
        setSpinnerData();
        //radioMaritalStatus();


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


                        et_anniversary.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);

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

    private void dateOfBirth() {
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

                        date_of_birth.setText(year + "-" + (monthOfYear) + "-" + dayOfMonth);


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


    private void getAllInterests() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    gameList.add(jsonArray.getString(i));
                                    adapter_interest.notifyDataSetChanged();
                                    // Toast.makeText(CompleteYourProfile.this, ""+gameList, Toast.LENGTH_SHORT).show();
                                }
                                //  Intent intent=new Intent(CompleteYourProfile.this,UserNavgation.class);
                                //startActivity(intent);

                            } else {
                                Toast.makeText(CompleteYourProfile.this, "Please enter your valid Secret Id or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
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
                logParams.put("action", "get_intrest");


                return logParams;
            }
        };

        MySingleTon.getInstance(CompleteYourProfile.this).addToRequestQue(stringRequestLogIn);

    }

    private void setSpinnerData() {
        adapter_interest = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gameList);
        adapter_interest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aListNumbers);


        spinner_interest_one.setAdapter(adapter_interest);
        spinner_interest_two.setAdapter(adapter_interest);
        spinner_interest_three.setAdapter(adapter_interest);
        spinner_interest_four.setAdapter(adapter_interest);
        spinner_interest_five.setAdapter(adapter_interest);
        spinner_interest_six.setAdapter(adapter_interest);
        number_spinner.setAdapter(adapter);

    }

    private void sendData() {

        //   final String numeric_CODE= numeric_code.getText().toString().trim();
        final String recident = et_resident.getText().toString().trim();
        final String user_name = et_user_name.getText().toString().trim();
        final String DOB = date_of_birth.getText().toString().trim();
        final String gender = radioSexButton.getText().toString().trim();
        final String work_for = et_work_for.getText().toString().trim();
        //   final int length = numeric_code.getText().length();
        final String anniversary_date = et_anniversary.getText().toString().trim();

        if (TextUtils.isEmpty(user_name)) {
            et_user_name.setError("User name Can't be empty");
            et_user_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(work_for)) {
            et_work_for.setError("Work for Can't be empty");
            et_work_for.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(recident)) {
            et_resident.setError("Resident Can't be empty");
            et_resident.requestFocus();
            return;
        }
        genderFun();

       /* if (TextUtils.isEmpty(numeric_CODE)||length<3)
        {
            numeric_code.setError("Numeric code Can't be less than 3");
            numeric_code.requestFocus();
            return;

        }*/

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;
        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            //      Toast.makeText(CompleteYourProfile.this, ""+status, Toast.LENGTH_SHORT).show();
                            //   JSONObject user_details= jsonObject.getJSONObject("user_details");

                            if (status.equals("true")) {

                                // user_id = user_details.getString("id");

                            } else {
                                Toast.makeText(CompleteYourProfile.this, "Please enter your valid Secret Id or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }
                }
        ) {
            @Override


            protected Map<String, String> getParams() throws AuthFailureError {
                //  String interset = "" ;


                //   String sec  = "";
                String[] all_interest = new String[]{var1, var2, var3, var4, var5, var6};

                JSONArray mJSONArray = new JSONArray(Arrays.asList(all_interest));

                Map<String, String> logParams = new HashMap<>();
                logParams.put("action", "update_profile");
                logParams.put("userid", user_id);
                logParams.put("mobileno", mobile_number);
                logParams.put("email", email);
                logParams.put("name", user_name);
                logParams.put("marital", marital_status);
                logParams.put("gender", gender1);
                logParams.put("dob", DOB);
                logParams.put("anniversary", anniversary_date);
                logParams.put("workfor", work_for);
                logParams.put("resident", recident);
                logParams.put("instrests", mJSONArray.toString());
                logParams.put("security_code", sec);

                User currentUser = new User(user_id, user_name, email, mobile_number, " ", secret_id, " ", recident, "true");
                SharedPrefManager.getInstance(CompleteYourProfile.this).userLogin(currentUser);

                return logParams;

            }
        };

        MySingleTon.getInstance(CompleteYourProfile.this).addToRequestQue(stringRequestLogIn);
        Intent intent = new Intent(CompleteYourProfile.this, UserNavgation.class);
        startActivity(intent);
        finish();

    }

    public void genderFun() {

        radioGrp_sex = (RadioGroup) findViewById(R.id.radioGrp_sex);
        // get selected radio button from radioGroup
        int selectedId = radioGrp_sex.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        // RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGrp_sex);
        radioGrp_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.radioM) {
                    //some code
                    gender1 = "Male";
                } else {
                    //some code
                    gender1 = "Female";
                }
            }
        });
        // Toast.makeText(CompleteYourProfile.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();
        if (radioSexButton.getText().toString().equals("Male")) {
            //    Toast.makeText(CompleteYourProfile.this,radiomaritalstatus.getText(),Toast.LENGTH_SHORT).show();

            gender1 = "Male";
        } else if (radioSexButton.getText().toString().equals("Female")) {
            //   Toast.makeText(CompleteYourProfile.this,radiomaritalstatus.getText(),Toast.LENGTH_SHORT).show();
            gender1 = "Female";

        }

    }

    private void loadAllDetails() {

        final String LOGIN_URL = BaseUrl_ConstantClass.BASE_URL;

        StringRequest stringRequestLogIn = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int i = 0;
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
                                //        JSONArray security_code1=user_details.getJSONArray("instrests");
                                et_user_name.setText(name);
                                date_of_birth.setText(dob);
                                et_work_for.setText(workfor);
                                et_resident.setText(resident);

                                if (gender.equals("Male")) {
                                    radioM.setChecked(true);
                                } else {
                                    radioF.setChecked(true);
                                }
                                if (marital.equals("Single")) {
                                    radio_Single.setChecked(true);
                                } else {
                                    radio_married.setChecked(true);
                                }


                                JSONArray jsonArray = user_details.getJSONArray("instrests");
                                for (i = 0; i <= jsonArray.length(); i++) {
                                    String data = jsonArray.optString(i);

                                }


                            } else {
                                Toast.makeText(CompleteYourProfile.this, "Please enter your valid Secret ID or Passkey", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_server),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(CompleteYourProfile.this, "" + getString(R.string.error_network_timeout),
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

        MySingleTon.getInstance(CompleteYourProfile.this).addToRequestQue(stringRequestLogIn);

    }

    private void init() {

        anniversary_relative = (RelativeLayout) findViewById(R.id.anniversary_relative);
        et_anniversary = (EditText) findViewById(R.id.et_anniversary);
        date_anniversary = (ImageView) findViewById(R.id.date_anniversary);
        date_anniversary.setOnClickListener(this);


        send_data = (Button) findViewById(R.id.send_data);
        send_data.setOnClickListener(this);
        // numeric_code=(EditText)findViewById(R.id.numeric_code);
        et_resident = (EditText) findViewById(R.id.et_resident);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_work_for = (EditText) findViewById(R.id.et_work_for);

        date_of_birth = (TextView) findViewById(R.id.date_of_birth);
        date_of_birth.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_data: {
                sendData();
                //loadAllDetails();
            }
            break;

            case R.id.date_of_birth: {
                dateOfBirth();
            }
            break;

            case R.id.date_anniversary: {
                annversaryPickerDialog();

            }
            break;
        }

    }
}
