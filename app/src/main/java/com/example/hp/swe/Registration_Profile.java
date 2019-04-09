package com.example.hp.swe;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class Registration_Profile extends AppCompatActivity {

    String id,pass;
    ImageView profilepic;
    Button photobutton,register_profile_signup;
    public static final int PICK_IMAGE = 2;
    EditText name,blood,dob,phone_number,email,emergency_number;
    String s_name,s_blood,s_dob,s_phone_number,s_email,s_emergency_number;
    private FirebaseAuth mAuth;

    String URL= Connection.ROOT_URL+"register.php";
    JSONParser jsonParser=new JSONParser();

    private StorageReference nStorage;
    Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__profile);
        Intent i = getIntent();
        id = i.getStringExtra("ID");
        pass = i.getStringExtra("PASS");
        mAuth = FirebaseAuth.getInstance();

        nStorage = FirebaseStorage.getInstance().getReference();
        profilepic = findViewById(R.id.profilepic);
        photobutton = findViewById(R.id.photobutton);

        register_profile_signup = findViewById(R.id.register_profile_signup);
        name = findViewById(R.id.input_name);
        blood = findViewById(R.id.input_blood);
        dob = findViewById(R.id.input_dob);
        phone_number = findViewById(R.id.input_phone_number);
        email = findViewById(R.id.input_email);
        emergency_number = findViewById(R.id.input_emergency_number);




        photobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);//
                //Toast.makeText(Registration_Profile.this,"Feature Not added Sorry",Toast.LENGTH_LONG).show();

            }
        });









        register_profile_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_blood = blood.getText().toString();
                s_dob = dob.getText().toString();
                s_email = email.getText().toString();
                s_emergency_number = emergency_number.getText().toString();
                s_name = name.getText().toString();
                s_phone_number = phone_number.getText().toString();
                if(s_name.equals("") || s_blood.equals("") || s_dob.equals("") || s_phone_number.equals("") || s_email.equals("") || s_emergency_number.equals("") || Uri.EMPTY.equals(selectedImage)){
                    Toast.makeText(Registration_Profile.this,"Fill all Field",Toast.LENGTH_LONG).show();

                }
                else {

                    AttemptLogin attemptLogin= new AttemptLogin();
                    attemptLogin.execute(s_name,pass,s_blood,id,s_dob,s_phone_number,s_email,s_emergency_number);

//                    attemptLogin.execute(s_name,pass,s_blood);


                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == PICK_IMAGE && resultCode== RESULT_OK && data != null) {
             selectedImage= data.getData();

            profilepic.setImageURI(selectedImage);


        }
    }

//
private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

    @Override

    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override

    protected JSONObject doInBackground(String... args) {



        String blood = args[2];
        String password = args[1];
        String name= args[0];
        String registration = args[3];
        String dob = args[4];
        String phone = args[5];
        String email = args[6];
        String emergency = args[7];

        ArrayList params = new ArrayList();

        params.add(new BasicNameValuePair("username", name));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("registration_number", registration));
        params.add(new BasicNameValuePair("DateOfBirth", dob));
        params.add(new BasicNameValuePair("BloodGroup", blood));
        params.add(new BasicNameValuePair("PhoneNumber", phone));
        params.add(new BasicNameValuePair("EmergencyNumber" , emergency));
        params.add(new BasicNameValuePair("Email" , email));

        JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

        return json;
    }

    protected void onPostExecute(JSONObject result) {

        // dismiss the dialog once product deleted
        //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        try {
            if (result != null) {
                if(result.getInt("success") == 1){
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Registration_Profile.this,Login.class);
                    startActivity(i);

                }
                else{
                    Toast.makeText(getApplicationContext(),"mara",Toast.LENGTH_LONG).show();

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

