package com.agilitysciences.alsdk;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.agilitysciences.alsdk.databinding.ActivityMainBinding;
import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.utility.KeyType;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    private ProgressBar progressBar;

    public MainActivityViewModel mainActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActiveLedgerSDK.getInstance().initSDK(getApplicationContext(),"http","testnet-uk.activeledger.io","5260");

        mainActivityViewModel =  ViewModelProviders.of(this).get(MainActivityViewModel.class);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);

        activityMainBinding.setViewmodel(mainActivityViewModel);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        initLayout();

        mainActivityViewModel.getShowToast().observe(this, new android.arch.lifecycle.Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                    showToast(message);
            }
        });

    }


    public void initLayout(){


        spinner = (Spinner) findViewById(R.id.keytype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.keytype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }


    public void showProgressbar(){
        if(progressBar.getVisibility() == View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    public void hideProgressbar(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

         if(pos == 0){
             mainActivityViewModel.setKeyType(KeyType.RSA);
         }
         else{
             mainActivityViewModel.setKeyType(KeyType.EC);
         }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityViewModel.activityOnDestroy();
    }


    public void showToast(String message){
        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
    }


}
