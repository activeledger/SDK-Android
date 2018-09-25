package com.agilitysciences.alsdk;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.Interface.OnTaskCompleted;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.PreferenceManager;
import com.example.activeledgersdk.utility.Utility;

import java.io.IOException;
import java.security.KeyPair;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextView tv_pubkeys;
    TextView tv_prikeys;
    TextView onBoardId_tv;
    Spinner spinner;

    KeyType keyType = null;
    KeyPair keyPair = null;

    EditText keyname_et;
    TextView onBoardName_tv;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);



        initLayout();

//        KeyType keyType = KeyType.RSA;
//
//        ActiveLedgerSDK.getInstance().initSDK(this,"http","testnet-uk.activeledger.io","5260");
//        KeyPair keyPair = ActiveLedgerSDK.getInstance().generateAndSetKeyPair(keyType,true);
//
//        ActiveLedgerSDK.getInstance().onBoardKeys(keyPair,"mykey");


//       String json = " {\n" +
//               " \t\"$tx\": {\n" +
//               " \t\t\"$contract\": \"onboard\",\n" +
//               " \t\t\"$namespace\": \"default\",\n" +
//               " \t\t\"$i\": {\n" +
//               " \t\t\t\"hamid\": {\n" +
//               " \t\t\t\t\"publicKey\": \"-----BEGIN PUBLIC KEY-----\\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmyiDsioah6cY7MJ4LybSH3O44\\nGmfTuYmlniYN6ewP64Uh/v49T2ir5ThtBRiaKpaSow2I9DhcQPTrgrV3WjuCa+t8\\nOZxDcp1k5J/jzVZM4Ljtx6MPTT8yK6kDgw1RZpOioa5JYGhg5QVY0H/MVAI7MKVm\\n1efgzGEuOeAtZOfgIwIDAQAB\\n-----END PUBLIC KEY-----\\n\",\n" +
//               " \t\t\t\t\"type\": \"rsa\"\n" +
//               " \t\t\t}\n" +
//               " \t\t}\n" +
//               " \t},\n" +
//               " \t\"$selfsign\": true,\n" +
//               " \t\"$sigs\": {\n" +
//               " \t\t\"hamid\": \"tE5Fs47LUKH2tU6yD5YnfHMW4F/SAHepmhcUw/02wAxtNZw2EVgYEeiSVL4c0qw2QVB8IGKweeHM\\naFAS1G+eFNWC4ffNb+7MgSUuw8NVjKak4kSl7tp0KdU7FRsIf6SYQ9hfLe1NQIKn4WbI73E+zfFK\\nR4g88eHgBwTm2SvcdUM=\\n\"\n" +
//               " \t}\n" +
//               " }";
//
//        String response = ActiveLedgerSDK.getInstance().executeTransaction(json);
//
//        Log.e("--->",response);



            //              ContractUploading.createTXObject(null,null,null,null,null,null);
//              ContractUploading.createBaseTransaction(null,null,null,null,null,null,null,null);


////        String createContracttransaction =  ContractUploading.createContractUploadTransaction(KeyType.RSA);
//        String createContracttransaction =  ContractUploading.createFundsTransferTransaction(KeyType.RSA);
//
//        UploadContractAPI uploadContractAPI = new UploadContractAPI(createContracttransaction,this);
//        uploadContractAPI.execute();




    }


    public void initLayout(){


        spinner = (Spinner) findViewById(R.id.keytype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.keytype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        tv_pubkeys = (TextView) findViewById(R.id.pubkey);
        tv_prikeys = (TextView) findViewById(R.id.prikey);
        onBoardId_tv = (TextView) findViewById(R.id.onBoardId_tv);

        keyname_et = (EditText) findViewById(R.id.keyname_et);

        onBoardName_tv =(TextView) findViewById(R.id.onBoardName_tv);

    }


    public void generatekeys(View view) {

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

//        showProgressbar();

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



        ActiveLedgerSDK.getInstance().initSDK(this,"http","testnet-uk.activeledger.io","5260");
        keyPair = ActiveLedgerSDK.getInstance().generateAndSetKeyPair(keyType,true);


        try {
            tv_pubkeys.setText(ActiveLedgerSDK.readFileAsString(Utility.PUBLICKEY_FILE));
            tv_prikeys.setText(ActiveLedgerSDK.readFileAsString(Utility.PRIVATEKEY_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }



//        hideProgressbar();

//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);





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
             keyType = KeyType.RSA;
         }
         else{
             keyType = KeyType.EC;
         }

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void onboardkeys(View view) {

        String keyname = keyname_et.getText().toString();

        if (keyPair != null) {
            ActiveLedgerSDK.getInstance().onBoardKeys(keyPair, keyname,  new OnTaskCompleted() {

                @Override
                public void onTaskCompleted() {
                    onBoardId_tv.setText(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)));
                    onBoardName_tv.setText(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_name)));
                }

            });

        } else {
            Toast.makeText(this, "Generate Keys First", Toast.LENGTH_SHORT).show();
        }

    }





}
