package minorproject.votingassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import static android.support.v4.widget.SearchViewCompat.getQuery;



public class LoginActivity extends AppCompatActivity implements TextWatcher {


    public Fragment frag;
    private EditText number;
    private Button verify;
    private RelativeLayout myLayout;
    private int RoleSelected, ID;
    private String Msg,Name,nam, loginID, areaID, Area;
    CoordinatorLayout snackbarCoordinatorLayout;
    Snackbar snackbar;
    Bundle loginData;
    String Success;
    private String  LoginURL = "http://bond-vehicles.000webhostapp.com/Voting/loginapi.php", Role = null, Number = null;
    String[] role = {"Election Commision Login", "DM Login", "SDM Login", "Zonal Login", "Booth Officers Login"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verify = (Button) findViewById(R.id.verifynumber);
        Spinner login = (Spinner) findViewById(R.id.spinnerlogin);
        number = (EditText) findViewById(R.id.mobilenumber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.logintoolbar);
        snackbarCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.snackbarCoordinatorLayout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.logo32);
        toolbar.setTitle("Voting Assistant");


        myLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myLayout.getWindowToken(), 0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        @Override
//        public void onBackPressed() {
//
//            }





        login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "ROLE SELECTED:::" + position, Toast.LENGTH_LONG).show();


                RoleSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, role);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        login.setAdapter(adapter);
//        Log.e("ADAPTER", "::::" + adapter);
        if (!role.equals(null)) {
            int spinnerPosition = adapter.getPosition(role);
            login.setSelection(spinnerPosition);
//            Log.e("SPINNER POSITION", "::::"+spinnerPosition);
        }

        number.addTextChangedListener(this);
        verify.addTextChangedListener(this);


        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                boolean valid = isValidPhoneNumber(number.getText());
                if (valid) {
                    switch (RoleSelected){
                        case 0: Role = "eclogin";
                                Number = "ec_number";
                                Name = "ec_name";
                                login();


                               break;

                        case 1: Role = "dmlogin";
                            loginID = "dm_id";
                            areaID = "district_id";
                            Number = "dm_number";
                            Name = "dm_name";
                            login();
                            break;

                        case 2: Role = "sdmlogin";
                            loginID = "sdm_id";
                            areaID = "subdistrict_id";
                            Number = "sdm_number";
                            Name = "sdm_name";
                            login();
                            break;

                        case 3: Role = "zonallogin";
                            loginID = "zonal_id";
                            areaID = "z_id";
                            Number = "zonal_number";
                            Name = "zonal_name";
                            login();
                            break;

                        case 4: Role = "boothlogin";
                            loginID = "booth_id";
                            areaID = "b_id";
                            Number = "booth_number";
                            Name = "booth_name";
                            login();
                            break;
                    }

                }
                else{
                    snackbar = Snackbar
                            .make(snackbarCoordinatorLayout, "Enter Valid Number !", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(snackBarView.getContext(), R.color.colorPrimaryDark));
//                          TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//                          textView.setTextColor(ContextCompat.getColor(snackBarView.getContext(), R.color.colorAccent));
                    snackbar.show();
                }
            }
        });
    }




      public void login(){


          final String loginNumber = number.getText().toString();

          StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginURL, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {

                  try {
                      JSONObject jsonObject = new JSONObject(response);
                      Msg = jsonObject.getString("msg");
                      Success = jsonObject.getString("success");

                      loginData = new Bundle();
//                      Toast.makeText(getApplicationContext(),"WELCOME:::"+nn,Toast.LENGTH_LONG).show();



                      if(Success.equals("eclogin") ){

                          nam =  jsonObject.getString(Name);
                          loginData.putString("name",nam);
                          gotoOTP(loginData);
                      }

                      else if(Success.equals("dmlogin") ){

                          nam =  jsonObject.getString(Name);
                          ID = jsonObject.getInt(loginID);
                          Area = jsonObject.getString(areaID);
                          loginData.putString("name",nam);
                          loginData.putInt("id",ID);
                          loginData.putString("area",Area);
                          gotoOTP(loginData);
                      }

                      else if(Success.equals("sdmlogin") ){

                          nam =  jsonObject.getString(Name);
                          ID = jsonObject.getInt(loginID);
                          Area = jsonObject.getString(areaID);
                          loginData.putString("name",nam);
                          loginData.putInt("id",ID);
                          loginData.putString("area",Area);
                          gotoOTP(loginData);
                      }

                      else if(Success.equals("zonallogin") ){

                          nam =  jsonObject.getString(Name);
                          ID = jsonObject.getInt(loginID);
                          Area = jsonObject.getString(areaID);
                          loginData.putString("name",nam);
                          loginData.putInt("id",ID);
                          loginData.putString("area",Area);
                          gotoOTP(loginData);
                      }

                      else if(Success.equals("boothlogin") ){

                          nam =  jsonObject.getString(Name);
                          ID = jsonObject.getInt(loginID);
                          Area = jsonObject.getString(areaID);
                          int seats = jsonObject.getInt("totalseats");
                          loginData.putString("name",nam);
                          loginData.putInt("id",ID);
                          loginData.putString("area",Area);
                          loginData.putInt("totalseats",seats);
                          gotoOTP(loginData);
                      }

                     else
                      {
                          snackbar = Snackbar
                                  .make(snackbarCoordinatorLayout, "Error: "+Msg, Snackbar.LENGTH_LONG);
                          View snackBarView = snackbar.getView();
                          snackBarView.setBackgroundColor(ContextCompat.getColor(snackBarView.getContext(), R.color.colorPrimaryDark));
//                          TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//                          textView.setTextColor(ContextCompat.getColor(snackBarView.getContext(), R.color.colorAccent));
                          snackbar.show();

                      }


                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

              }



          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

              }
          }){


              @Override
              protected Map<String, String> getParams() throws AuthFailureError {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put(Number, loginNumber);
                  params.put(Role,String.valueOf(true));
                  return params;
              }
          };

          RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
          requestQueue.add(stringRequest);




      }

      public void gotoOTP(Bundle bundle){
          Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
          bundle.putInt("Role",RoleSelected);
          intent.putExtras(bundle);
          startActivity(intent);
          finish();
      }
            


    /**
     * Validation of Phone Number
     */
    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 10 || target.length() > 10) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
        }


    @Override
    public void onBackPressed() {
            Log.e("getBackStackEntryCount", ":::" + getSupportFragmentManager().getBackStackEntryCount());
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                alertDialog();
            } else {
                super.onBackPressed();
            }

    }

    public boolean alertDialog() {
            final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Alert!");
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage("Are you sure you want to exit?");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialog.dismiss();
//                    int pid = android.os.Process.myPid();
//                    android.os.Process.killProcess(pid);
                    finish();


                }
            });
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                    dialog.dismiss();

                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


        if (s == number.getEditableText()) {
            boolean valid = isValidPhoneNumber(number.getText().toString());
            if (valid == false) {
                verify.setBackground(this.getResources().getDrawable(R.drawable.invalidbutton));

            } else {
                verify.setBackground(this.getResources().getDrawable(R.drawable.validbutton));

            }

        }
    }
}

