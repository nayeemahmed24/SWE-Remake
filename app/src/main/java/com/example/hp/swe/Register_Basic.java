package com.example.hp.swe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register_Basic extends AppCompatActivity {

    TextView login;
    EditText registration_number,password,confirm_password;
    String reg_num,pass,con_pass;
    Button signup;
    String URL= Connection.ROOT_URL + "check.php";
    JSONParser jsonParser=new JSONParser();
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__basic);
        login = findViewById(R.id.link_login);
        signup = findViewById(R.id.btn_signup);
        registration_number = findViewById(R.id.input_registration_number);
        password = findViewById(R.id.input_password);
        confirm_password = findViewById(R.id.input_confirm_password);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_num = registration_number.getText().toString().trim();
                pass = password.getText().toString().trim();
                con_pass = confirm_password.getText().toString().trim();
                 i = new Intent(Register_Basic.this,Registration_Profile.class);
                i.putExtra("ID",reg_num);
                i.putExtra("PASS",pass);
//                startActivity(i);
                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(reg_num);


            }
        });





    }
    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {


            //    s_name,s_blood,s_dob,s_phone_number,s_email,s_emergency_number;

            String registration = args[0];


            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("registration_number", registration));


            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                   // Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();

                    if(result.getInt("success") == 0){
                        Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();

                    }
                    else{
                        startActivity(i);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
