package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;



import android.widget.ArrayAdapter;


import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.ar.core.ArCoreApk;

public class MenuActivity extends AppCompatActivity
        {

        int CODE = 2;

     @SuppressLint("StaticFieldLeak")
     static Button buttonGetQR1;
     @SuppressLint("StaticFieldLeak")
     static Button buttonGetQR2;
     @SuppressLint("StaticFieldLeak")
     static AutoCompleteTextView spinner1;
     @SuppressLint("StaticFieldLeak")
     static AutoCompleteTextView spinner2;
     @SuppressLint("StaticFieldLeak")
     static TextInputLayout textInput1;
     @SuppressLint("StaticFieldLeak")
     static TextInputLayout textInput2;
     String Source ="";
     String Destination="";
     Chip chip_deprat;
     Chip chip_destination;
     CompoundButton.OnCheckedChangeListener checkedChangeListener_depart;
     CompoundButton.OnCheckedChangeListener checkedChangeListener_arrive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        Handler handler = new Handler(Looper.getMainLooper());

        // BUTTON 1
        Button button = this.findViewById(R.id.GETMODEL);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!Source.equals(Destination)){
                            Intent intent = new Intent(MenuActivity.this, Modelsviewer.class);
                            intent.putExtra("Source",Source);
                            intent.putExtra("Destination",Destination);
                            startActivity(intent);
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                            //Set Icon
                            builder.setIcon(R.drawable.ic_outline_warning_24);
                            // Set the message show for the Alert time
                            builder.setMessage("Vous avez choisi deux point de départ d'arrivé égaux");

                            // Set Alert Title
                            builder.setTitle("Alert");

                            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will not remain show
                            builder.setCancelable(true);

                            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                            builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                                // When the user click yes button then app will close
                                dialog.cancel();
                            });

                            // Create the Alert dialog
                            AlertDialog alertDialog = builder.create();
                            // Show the Alert Dialog box
                            alertDialog.show();
                        }
                    }
                });
            }
        });

        // show all arrows
        Button show_all = this.findViewById(R.id.buttonShowAll);
        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Modelsviewer.class);
                intent.putExtra("Source","all");
                intent.putExtra("Destination","all");
                startActivity(intent);
            }
        });

        // open profil
        Button profil = this.findViewById(R.id.buttonProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Profil.class);
                startActivity(intent);
            }
        });
        // show MUR
        Button wall = this.findViewById(R.id.buttonActu);
        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Actu.class);
                startActivity(intent);
            }
        });

        // GetQR1
        buttonGetQR1 = this.findViewById(R.id.GETQR);
        buttonGetQR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, QRscan.class);
                startActivityForResult(intent, 1);
            }
        });

        // GetQR2
        buttonGetQR2  = this.findViewById(R.id.GETQR2);
        buttonGetQR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, QRscan.class);
                startActivityForResult(intent, 2);
            }
        });

        //adapter
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.Places, R.layout.dropdown_item);
        adapter.setDropDownViewResource(R.layout.dropdown_item);

        // spiners
        spinner1=findViewById(R.id.autoCompleteTextView1);
        spinner1.setAdapter(adapter);
        spinner1.setDropDownHeight(500);
        //get Source selected
        spinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Source= adapter.getItem(i).toString();
                Log.i("Selected item SOURCE: ",Source);
            }
        });

        spinner2=findViewById(R.id.autoCompleteTextView2);
        spinner2.setAdapter(adapter);
        spinner2.setDropDownHeight(500);
        //get Destination selected
        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Destination = adapter.getItem(i).toString();
                Log.i("Selected item DEST: ",Destination);
            }
        });

        //spiners containers
        textInput1 = findViewById(R.id.TextIn1);
        textInput2 = findViewById(R.id.TextIn2);

        //Chips
        this.chip_deprat = this.findViewById(R.id.chip1);
        this.chip_destination = this.findViewById(R.id.chip2);
        //affecter listners
        this.checkedChangeListener_depart = new CheckedChangeListener_Depart();
        this.chip_deprat.setOnCheckedChangeListener(checkedChangeListener_depart);

        this.checkedChangeListener_arrive = new CheckedChangeListener_destination();
        this.chip_destination.setOnCheckedChangeListener(checkedChangeListener_arrive);


    }

    //get Destination and Source scanned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 1) {
                    if(resultCode == Activity.RESULT_OK){
                        Source =data.getStringExtra("result");
                        Log.i("Scanned Item Source",Source);
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        // Write your code if there's no result
                        Log.e("Scanned Item Source","NO SOURCE");
                    }
                }
                else {
                    if(resultCode == Activity.RESULT_OK){
                        Destination=data.getStringExtra("result");
                        Log.i("Scanned Item  Destination",Destination);
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        // Write your code if there's no result
                        Log.e("Scanned Item Destination","NO DESTINATION");
                    }

                }
    } //onActivityResult



    //definir les listener des Chips
    static class CheckedChangeListener_Depart implements  CompoundButton.OnCheckedChangeListener {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        buttonGetQR1.setVisibility(View.VISIBLE);
                        spinner1.setVisibility(View.INVISIBLE);
                        textInput1.setVisibility(View.INVISIBLE);

                    }
                    else{
                        buttonGetQR1.setVisibility(View.INVISIBLE);
                        spinner1.setVisibility(View.VISIBLE);
                        textInput1.setVisibility(View.VISIBLE);
                    }
                }
    }
    static class CheckedChangeListener_destination implements  CompoundButton.OnCheckedChangeListener {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        buttonGetQR2.setVisibility(View.VISIBLE);
                        spinner2.setVisibility(View.INVISIBLE);
                        textInput2.setVisibility(View.INVISIBLE);
                    }
                    else{
                        buttonGetQR2.setVisibility(View.INVISIBLE);
                        spinner2.setVisibility(View.VISIBLE);
                        textInput2.setVisibility(View.VISIBLE);
                    }
                }
    }

}