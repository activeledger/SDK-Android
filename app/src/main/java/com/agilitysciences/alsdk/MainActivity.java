package com.agilitysciences.alsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.utility.KeyType;

import java.security.KeyPair;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyType keyType = KeyType.RSA;

        ActiveLedgerSDK.getInstance().initSDK(this,"http","testnet-uk.activeledger.io","5260");
        KeyPair keyPair = ActiveLedgerSDK.getInstance().generateAndSetKeyPair(keyType,true);

        ActiveLedgerSDK.getInstance().onBoardKeys(keyPair,"mykey");


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



}
