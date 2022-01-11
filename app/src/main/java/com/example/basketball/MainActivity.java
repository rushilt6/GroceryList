package com.example.basketball;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

//api: d0b18b0cd8e54a57bff0ccd3d1a9f912
public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference("server");
    TextView editFood;
    Button editButton;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView item4;
    ImageView image1,image2,image3,image4;
    TextView editName,editList,finalEdit,grocerylist;
    Button nameButton,eButton,finalButton;
    String name;
    String c;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editFood = findViewById(R.id.editFood);
        editButton = findViewById(R.id.foodButton);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item4 = findViewById(R.id.item4);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        editName = findViewById(R.id.editName);
        nameButton = findViewById(R.id.nameButton);
        editList = findViewById(R.id.editList);
        finalEdit = findViewById(R.id.finalEdit);
        eButton = findViewById(R.id.editButton);
        finalButton = findViewById(R.id.finalButon);
        grocerylist = findViewById(R.id.grocerylist);
        eButton.setVisibility(View.INVISIBLE);
        finalButton.setVisibility(View.INVISIBLE);
        editList.setVisibility(View.INVISIBLE);
        finalEdit.setVisibility(View.INVISIBLE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFilesTask().execute();
            }
        });
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();
                i=0;
                editList.setVisibility(View.VISIBLE);
                eButton.setVisibility(View.VISIBLE);
            }
        });
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = editList.getText().toString();
                if(c.equalsIgnoreCase("Get"))
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            grocerylist.setText(snapshot.child(name).getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                if(c.equalsIgnoreCase("add") || c.equalsIgnoreCase("Delete")) {
                    finalButton.setVisibility(View.VISIBLE);
                    finalEdit.setVisibility(View.VISIBLE);
                }
            }
        });
        finalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c.equalsIgnoreCase("Add")){
                    myRef.child(name).child(editFood.getText().toString()).setValue(finalEdit.getText().toString());

                }
                if(c.equalsIgnoreCase("Delete")){
                    myRef.child(name).child(editFood.getText().toString()).removeValue();
                }
            }
        });

    }

    private class DownloadFilesTask extends AsyncTask<URL, Void, JSONObject> {
        protected JSONObject doInBackground(URL... URL) {
            URL url = null;
            try {
                url = new URL("https://api.spoonacular.com/food/products/search?query=" + editFood.getText() + "&apiKey=d0b18b0cd8e54a57bff0ccd3d1a9f912");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("TAG2", "URL");
            }
            URLConnection urlConnection = null;
            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG2", "urlConnection");
            }
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TAG2", "inputStream");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(line);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONArray array = jsonObject.getJSONArray("products");
                for(int i =0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(i==0){
                        item1.setText(object.getString("title"));
                        Picasso.with(MainActivity.this).load(object.getString("image")).into(image1);
                    }
                    if(i==1){
                        item2.setText(object.getString("title"));
                        Picasso.with(MainActivity.this).load(object.getString("image")).into(image2);
                    }
                    if(i==2){
                        item3.setText(object.getString("title"));
                        Picasso.with(MainActivity.this).load(object.getString("image")).into(image3);
                    }
                    if(i==3){
                        item4.setText(object.getString("title"));
                        Picasso.with(MainActivity.this).load(object.getString("image")).into(image4);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}