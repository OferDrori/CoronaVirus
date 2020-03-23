package com.example.coronavirus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.coronavirus.Keys.MESSAGE_GPS;
import static com.example.coronavirus.Keys.MESSAGE_INSTALLED_APP;
import static com.example.coronavirus.Keys.MESSAGE_NORMAL_SIZE;
import static com.example.coronavirus.Keys.MESSAGE_PERTRAIT_MODE;
import static com.example.coronavirus.Keys.MESSAGE_PUT_THIS_CODE;


public class Register extends AppCompatActivity {
    private Button loginButton;
    private EditText userName_editText;
    private EditText password_editText;
    private TextView massegeTextView;
    private TextView infoTextView;
    private TextView randomStringTextView;
    private EditText randomString_editText;
    private String user = "guy";
    private String pass = "1234";
    private int switchOption = 6;
    private boolean checkFlag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginButton = findViewById(R.id.register_button_signIn);
        userName_editText = findViewById(R.id.register_editText_User);
        infoTextView=findViewById(R.id.register_textView_info);
        password_editText = findViewById(R.id.register_editText_Password);
        massegeTextView =findViewById(R.id.register_textView_timeCount);
        randomStringTextView=findViewById(R.id.register_textView_randomString);
        randomString_editText=findViewById(R.id.register_editText_PutRandomString);
        massegeTextView.setVisibility(View.GONE);
        putInfoText();
        loginButton.setOnClickListener(enterUser);
    }
    public void putInfoText()
    {

        switch (switchOption%6) {
            case 0:
                infoTextView.setText("Time count");
                break;
            case 1:
                infoTextView.setText(MESSAGE_PERTRAIT_MODE);
                break;
            case 2:
                infoTextView.setText(MESSAGE_INSTALLED_APP);
                break;
            case 3:
                infoTextView.setText(MESSAGE_NORMAL_SIZE);
                break;
            case 4:
                infoTextView.setText(MESSAGE_GPS);
                break;
            case 5:
                infoTextView.setText(MESSAGE_PUT_THIS_CODE);
                randomStringTextView.setVisibility(View.VISIBLE);
                randomString_editText.setVisibility(View.VISIBLE);
                randomStringTextView.setText(RandomString.getAlphaNumericString(5));
                break;
        }


    }
    public void countDownUntilZero()
    {
        loginButton.setEnabled(false);
        massegeTextView.setVisibility(View.VISIBLE);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                massegeTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                massegeTextView.setText("done!");

                massegeTextView.setVisibility(View.INVISIBLE);
                loginButton.setEnabled(true);
            }
        }.start();
    }

    public void successMassege(){
        massegeTextView.setText("Sucess");
        massegeTextView.setVisibility(View.VISIBLE);
    }

    public boolean checkIfUserAndPassAreCorrect(){
        return userName_editText.getText().toString().equals(user) && password_editText.getText().toString().equals(pass);
    }

    private boolean appInstalledOrNot(String uri) { //check if an application is installed (given uri)

        PackageManager pm = getPackageManager();
        boolean app_installed;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed;
    }

    private int checkScreenSize(){
        return getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        //return options: Configuration.SCREENLAYOUT_SIZE_LARGE, Configuration.SCREENLAYOUT_SIZE_NORMAL, Configuration.SCREENLAYOUT_SIZE_SMALL
    }


    public boolean checkIfGPSEnabled()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        return false;
    }

    public void printWrongMassege(String str)
    {
        massegeTextView.setVisibility(View.VISIBLE);
        massegeTextView.setText(str);
        checkFlag = false;

    }

    View.OnClickListener enterUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkFlag=true;
            switch (switchOption%6) {
                case 0:
                    if (checkIfUserAndPassAreCorrect())
                        successMassege();
                    else
                      {
                        countDownUntilZero();
                        checkFlag = false;
                    }
                    break;
                case 1:
                    if(!(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )) //check if the screen is in portrait mode - ORIENTATION_PORTRAIT.LANDSCAPE for landscape positon
                        printWrongMassege("not in portrait mode!");

                    break;
                case 2:
                    if (!(appInstalledOrNot("com.whatsapp"))) //check if what's is installed in android - can do it on other apps
                        printWrongMassege("the application is not installed!");
                    break;
                case 3:
                    if (!(checkScreenSize() == Configuration.SCREENLAYOUT_SIZE_NORMAL))
                        printWrongMassege("The screen size is not as excpected");
                    break;
                case 4:
                    if (!(checkIfGPSEnabled()))
                        printWrongMassege("GPS is disabled");
                    break;
                case 5:
                    if (!(randomStringTextView.getText().toString().equals(randomString_editText.getText().toString()))) {
                        printWrongMassege("Incorrect Input");
                    }
                    else {
                        randomStringTextView.setVisibility(View.INVISIBLE);
                        randomString_editText.setVisibility(View.INVISIBLE);
                    }

                    break;
            }
            if (checkFlag)
            {
                if(checkIfUserAndPassAreCorrect())
                {
                    successMassege();
                    switchOption++;
                }
                else
                    printWrongMassege("user name or password is incorrect");
            }



            putInfoText();
        }

    };
}
