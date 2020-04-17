package com.attors.educations.Helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.attors.educations.BuildConfig;
import com.attors.educations.Modal.Keymodal;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Modal.Test_Save;
import com.attors.educations.Modal.Validation_custome;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.activity.Login;
import com.attors.educations.activity.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Utlity {
    //db variables
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static Gson gson = new Gson();
    public static int login = 2;
    public static Socket socket;
    // for user details
    public static String selected_vendor = "";
    public static String user_type = "";
    public static Location currentlocation = null;
    //to save address in  local
    private static LocationManager locationManager;
    private static Location location;
    private static SharedPreferences local_db;
    private static SharedPreferences.Editor tbl_user;
    private static String Custom_Security = "hT$e?~nsemPxCE{-UrhT$e?~nsemPxCE{-Ur";
    private static String Profile_data = "Profile_data";
    private static  String language_code="";
    private static Dialog dialog;
    //private static GPSTracker tracker;
    private  static Thread thread;
    private static LatLng current_location=null;


    //to call okkhtpp
    public static Request post(Activity activity, HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = keys.get(key);
            if(!TextUtils.isEmpty(value)) {
                body.add(key.toString(), value);
            }
            else {
                body.add(key.toString(), "");
            }
        }
        Utlity.show_progress(activity);
        String xpikey = "", xpiid = "";
        RequestBody parmetrs = body.build();
        return new Request.Builder()
                .url(Apis.Base_url+api_name)
                .header("Custom_Security", Custom_Security)
                .post(parmetrs)
                .build();
    }


    public static Request get(Context context, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        return new Request.Builder()
                .url(Apis.Base_url+api_name)
                .get()
                .header("Custom_Security", Custom_Security)
                .build();
    }

    // to show  full  screen activity
    public static void full_screen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //    //to show progress
    @SuppressLint("NewApi")
    public static Dialog show_progress(Activity activity) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_progress);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
         dialog.show();
        return dialog;
    }

    //to dismis dilog
    public static void dismiss_dilog(Activity activity) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static String getlatlngstate(AppCompatActivity activity, Double MyLat, Double MyLong) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses.get(0).getAdminArea();
    }

    //to show toast
    public static void show_toast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    //to check text is empty or not
    public static boolean is_empty(String value) {
        return TextUtils.isEmpty(value);
    }

    //to check is  network is available or not
    public static boolean is_online(Context activity) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            //we are connected to a network
            connected = true;
        else {
            connected = false;
        }
        return connected;
    }



    /*public static LatLng getcurrtlocation(final Activity final_hire) {

        if(!isGPSEnabled(final_hire))
        {
            displayLocationSettingsRequest(final_hire);
            thread=new Thread()
            {
                public void run()
                {
                    try {
                        sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (isGPSEnabled(final_hire)) {
                            tracker=new GPSTracker(final_hire);
                            if(tracker.latitude!=0.0&&tracker.longitude!=0.0) {
                                current_location=new LatLng(tracker.latitude,tracker.longitude);
                            }
                        }
                    }
                }
            };
            thread.start();
        }
        else
        {
            tracker=new GPSTracker(final_hire);
            if(tracker.latitude!=0.0&&tracker.longitude!=0.0) {
                current_location=new LatLng(tracker.latitude,tracker.longitude);
            }

        }
        return current_location;

    }

     */





    //to set image in image view from url

    public static void Set_image(String url, ImageView img) {
        if (!TextUtils.isEmpty(url));
           Picasso.get().load(Apis.uplad+url).error(R.drawable.noimagefound).placeholder(R.drawable.noimagefound).fit().into(img);
    }

    //for chk validation
    public static boolean isValidMobile(String phone) {
        String regEx = "^[0-9]{10,10}$";
        return phone.matches(regEx);
    }

    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // display location


    // current city name
    public static String get_address(Activity activity, Double lat, Double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(addresses.size()>0)
            return addresses.get(0).getAddressLine(0);

        else
            return "";

    }

    //to check to value is  equal
    public static boolean is_equal(String value1, String value2) {
        boolean is_valid = false;
        if (value1.equals(value2)) {
            is_valid = true;
        }
        return is_valid;
    }
    // to show toast 1

    //to show toast1


    // to save user  detail
    public static void user_db(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("user_db", user_info);
        tbl_user.apply();
        Utlity.set_login(activity, true);
    }


    // to save user  detail
    public static void user_test(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("user_test", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("user_test", user_info);
        tbl_user.apply();
    }

    // to save user  detail
    public static ArrayList<Test_Save> get_all_test(Activity activity) {
        local_db = activity.getSharedPreferences("user_test", Context.MODE_PRIVATE);
        return  Utlity.gson.fromJson(local_db.getString("user_test",""), new TypeToken<List<Test_Save>>() {}.getType());
    }

    public static void vechile_db1(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("vechel_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("vechel_db", user_info);
        tbl_user.apply();
    }



    // to save user  detail
    public static boolean get_rent(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        return  local_db.getBoolean("user_rent",false);

    }

    public static String get_profile_base(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        return  local_db.getString("profile_base","");
    }



    public static Keymodal get_user(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(local_db.getString("user_db","")))
        return Utlity.gson.fromJson(local_db.getString("user_db",""), Keymodal.class);
        else
            return new Keymodal();
    }




    public static void user_type(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("user_db_type", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("user_db_type", user_info);
        tbl_user.apply();
        Utlity.set_login(activity, true);
    }
    public static void user_verfry(Activity activity, boolean is_verfy) {
        local_db = activity.getSharedPreferences("user_db_type_verfy", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean("user_db_type_verfy", is_verfy);
        tbl_user.apply();
        Utlity.set_login(activity, true);
    }
    public static Boolean get_type_verfy(Activity activity) {
        local_db = activity.getSharedPreferences("user_db_type_verfy", Context.MODE_PRIVATE);
        return local_db.getBoolean("user_db_type_verfy",false);
    }

    public static String get_type(Activity activity) {
        local_db = activity.getSharedPreferences("user_db_type", Context.MODE_PRIVATE);
       return local_db.getString("user_db_type","");
    }

    public static void user_employee_db(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("user_db", user_info);
        tbl_user.apply();
        Utlity.set_login(activity, true);
    }

   /* public static User get_user_employee(Activity activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(local_db.getString("user_db","")))
            return Utlity.gson.fromJson(local_db.getString("user_db",""), User.class);
        else
            return new User();
    }

    */

    public static String get_pass(AppCompatActivity activity, String username) {
        local_db = activity.getSharedPreferences("user_pass", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(local_db.getString(username, "")))
            return local_db.getString(username, "");
        else
            return "";
    }

    //to clear user  db
    public static void clear_db(Context activity) {
        local_db = activity.getSharedPreferences("user_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();

        local_db = activity.getSharedPreferences("user_db_type", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();


        local_db = activity.getSharedPreferences("user_db_type_verfy", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();


        local_db = activity.getSharedPreferences("user_test", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();
    }

    //to set user is login
    public static void set_login(Context activity, boolean value) {
        local_db = activity.getSharedPreferences("login_db", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean("is_login", value);
        tbl_user.apply();
    }


    //to set user is login
    public static void driver_on(Context activity, boolean value) {
        local_db = activity.getSharedPreferences("driveron", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean("driver", value);
        tbl_user.apply();
    }

    //to set user is login
    public static Boolean driver_get(Context activity) {
        local_db = activity.getSharedPreferences("driveron", Context.MODE_PRIVATE);
        return local_db.getBoolean("driver", false);

    }

    //to set user is login
    public static void driver_pickup(Context activity, boolean value) {
        local_db = activity.getSharedPreferences("pickup", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean("pickup", value);
        tbl_user.apply();
    }

    //to set user is login
    public static Boolean driver__pickup_get(Context activity) {
        local_db = activity.getSharedPreferences("pickup", Context.MODE_PRIVATE);
        return local_db.getBoolean("pickup", false);

    }


    //to set user is login
    public static void save_notification(Context activity) {
        local_db = activity.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("notifi", String.valueOf(Integer.parseInt(get_notification(activity)) + 1));
        tbl_user.putString("notifi", String.valueOf(Integer.parseInt(get_notification(activity)) + 1));
        tbl_user.apply();
    }

    //to set user is login
    public static String get_notification(Context activity) {
        local_db = activity.getSharedPreferences("notifi", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(local_db.getString("notifi", "")))
            return local_db.getString("notifi", "");
        else
            return "0";
    }
    public static Location getLocationFromAddress(AppCompatActivity activity, String strAddress) {

        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        Location location_ = new Location("");
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            location_.setLatitude(location.getLatitude());
            location_.setLongitude(location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location_;
    }

    public static boolean isGPSEnabled(Activity mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String date_conva(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy H:m:s");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("DD-MMM-YYYY");
        return targetFormat.format(sourceDate);
    }


    public static String date_conva2(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM");
        return targetFormat.format(sourceDate);
    }

    public static String date_convarter(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        return targetFormat.format(sourceDate);
    }


    public static String date_convarter2(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return targetFormat.format(sourceDate);
    }

    public static String hour_convart(String hour) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(hour);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat("hh");
        return targetFormat.format(sourceDate);
    }

    public static void save_address(AppCompatActivity activity, String address) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("address", address);
        editor.apply();
    }

    public static void clear_address(Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    //to  get  last saved address
    public static String get_address(AppCompatActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("save_address", Context.MODE_PRIVATE);
        return preferences.getString("address", "");
    }


    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }



    @SuppressLint("MissingPermission")
    public static void call(Context activity, String no) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    2);
        } else {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + no));
            activity.startActivity(intent);
        }
    }

   /* public static void show_lanuage_dilog(final Activity activity)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
         // inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.mylanguage, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        dialog.setContentView(layout);
        Button btnsave=(Button)layout.findViewById(R.id.btnsave);
        Button btncancel=(Button)layout.findViewById(R.id.btncancel);
        final RadioGroup languagegroup=(RadioGroup)layout.findViewById(R.id.rglng);
        final RadioButton rben=(RadioButton)layout.findViewById(R.id.rdeng);
        final RadioButton rbhi=(RadioButton)layout.findViewById(R.id.rdhi);
        if(Utlity.get_user_lang(activity).equalsIgnoreCase("en"))
        {
            rben.setChecked(true);
        }
        else if (Utlity.get_user_lang(activity).equalsIgnoreCase("hi"))
        {
            rbhi.setChecked(true);
        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked_id=languagegroup.getCheckedRadioButtonId();
                if(checked_id==R.id.rdeng)
                {
                    language_code="en";
                }
                else if(checked_id==R.id.rdhi)
                {
                    language_code="hi";

                }
                //for set app language
                if(!TextUtils.isEmpty(language_code))
                {
                    save_user_language(activity,language_code);
                    setLocale(activity,language_code,true);
                }


            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dialog.dismiss();
            }
        });
        dialog.show();

    }
    */
    private static void save_user_language(Activity activity, String values) {
        SharedPreferences local_db = activity.getSharedPreferences("lng", Context.MODE_PRIVATE);
        SharedPreferences.Editor tbl_user = local_db.edit();
        tbl_user.putString("lng", values);
        tbl_user.apply();
    }

    //to get user info
    public static String get_user_lang(Context activity) {
        SharedPreferences local_db = activity.getSharedPreferences("lng", Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(local_db.getString("lng", ""))) {
            return local_db.getString("lng", "");
        } else {
            return "";
        }
    }
    public static void setLocale(Context activity, String lang,boolean is_lunche) {
        if (!TextUtils.isEmpty(lang)) {
            Locale myLocale = new Locale(lang);
            Resources res = activity.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            if(is_lunche) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }

        }
    }

    public static void setLocale_defult(Context activity, String lang) {
        if (!TextUtils.isEmpty(lang)) {
            Locale myLocale = new Locale(lang);
            Resources res = activity.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }

   /* public static void show_error(Context activity, String msg) {

        final Dialog dialog = new Dialog(activity, R.style.NewDialog);
        dialog.setContentView(R.layout.errorpop);
        TextView msgtv = dialog.findViewById(R.id.msg);
        msgtv.setText(msg);
        Button btnok = dialog.findViewById(R.id.btnok);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    */
  /* public static String get_random(Context context) {
       String[] array = context.getResources().getStringArray(R.array.mapkeys);
       return array[new Random().nextInt(array.length)];
   }

   */

    public static ArrayList<String> get_detail(AppCompatActivity activity) {
        ArrayList<String> detail = new ArrayList<>();
        String Appversion = String.valueOf(BuildConfig.VERSION_CODE);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        @SuppressLint({"MissingPermission", "HardwareIds"})
        String device_id = telephonyManager.getDeviceId();
        detail.add(Appversion);//app version 0
        detail.add(manufacturer);//device maker 1
        detail.add(model);// device model 2
        detail.add(device_data()); // sdk or version 3
        detail.add(device_id); //emi detail 4
        return detail;
    }

    private static String device_data() {
        StringBuilder builder = new StringBuilder();
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append("Android Version:").append(fieldName).append(" ");
                builder.append("Android Sdk:").append(fieldValue);
            }
        }
        return builder.toString();
    }

    public static void send_mail(Context context, String to) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.setType("message/rfc822");
        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    // display location
     public static void displayLocationSettingsRequest(final Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    public static void show_date_picker(Activity activity, final TextView tv) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, (month));
                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                tv.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.YEAR, 0);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
        dialog.show();
    }

    public static String get_current_date() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }




    public static Calendar get_currentdate_calender() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    public static String get_perfix(String str) {
        if (str.length() > 4)
            return str.substring(0, 4).toLowerCase()+"_";
        else
            return "";
    }

    public static String get_mobile_no(String str) {
        if (str.length() > 10)
            return str.replaceAll("[^0-9]", "");
        else
            return "";
    }


    public static void hide_app(AppCompatActivity activity)
    {
        PackageManager p = activity.getPackageManager();
        p.setComponentEnabledSetting(activity.getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    public static void unhide_app(AppCompatActivity activity)
    {
        PackageManager p = activity.getPackageManager();
        p.setComponentEnabledSetting(activity.getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static Cache get_catch(Context context)
    {
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(httpCacheDirectory, cacheSize);
    }

    public static boolean validation(Activity activity,ArrayList<Validation_custome> fileds) {
        boolean is_valid=true;

        for(Validation_custome validation_custome: fileds)
        {
            if(validation_custome.getType().equalsIgnoreCase("text")&&TextUtils.isEmpty(validation_custome.getEditText().getText().toString()))
            {
                validation_custome.getEditText().setError(validation_custome.getEditText().getHint()+" "+activity.getResources().getString(R.string.reguired));
                is_valid=false;
                break;
            }

            else  if((validation_custome.getType().equalsIgnoreCase("email")&&!Utlity.isValidMail(validation_custome.getEditText().getText().toString())))
            {
                validation_custome.getEditText().setError(activity.getResources().getString(R.string.invalidemail));
                is_valid=false;
                break;
            }
            else  if((validation_custome.getType().equalsIgnoreCase("number")&&validation_custome.getEditText().getText().length()!=10))
            {
                validation_custome.getEditText().setError(activity.getResources().getString(R.string.invalidno));
                is_valid=false;
                break;
            }

        }
        return is_valid;
    }

    public static void Logout(final Context context) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(R.string.logoutmsg);
            alertDialogBuilder.setPositiveButton(context.getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            Utlity.clear_db(context);
                            context.startActivity(new Intent(context, Login.class));

                        }
                    });

            alertDialogBuilder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create();
            alertDialogBuilder.show();

    }






    public static String convart_string(String total_amount) {

        return new DecimalFormat("##.##").format(Double.parseDouble(total_amount)).toString();
    }


    public static  String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }
        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount);
    }

    public static  boolean checkis_valid_date(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        boolean is_valid=true;
        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (createdConvertedDate.before(expireCovertedDate)) {
            is_valid=true;
        }
        else
        {
            is_valid=false;
        }
        return is_valid;
    }


}
