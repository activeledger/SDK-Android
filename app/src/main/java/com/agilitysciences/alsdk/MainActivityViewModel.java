package com.agilitysciences.alsdk;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import com.example.activeledgersdk.ActiveLedgerSDK;
import com.example.activeledgersdk.event.Event;
import com.example.activeledgersdk.event.SSEUtil;
import com.example.activeledgersdk.model.Territoriality;
import com.example.activeledgersdk.utility.KeyType;
import com.example.activeledgersdk.utility.PreferenceManager;
import com.example.activeledgersdk.utility.Utility;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyPair;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {


    public LiveData<Event> eventLiveData = SSEUtil.getInstance().eventLiveData;
    private Disposable disposable;
    private KeyPair key_Pair = null;
    private KeyType keyType = KeyType.RSA;
    private MutableLiveData<String> publickey = new MutableLiveData<>();
    private MutableLiveData<String> privatekey = new MutableLiveData<>();
    private String keyname;
    private MutableLiveData<String> onBoardId;
    private MutableLiveData<String> onBoardName;
    private MutableLiveData<String> showToast = new MutableLiveData<>();

    public void generatekeys(View view) {

        ActiveLedgerSDK.getInstance().generateAndSetKeyPair(keyType, true, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KeyPair>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        Log.d("MainActivity", "onComplete");

                        try {
                            setPublickey(ActiveLedgerSDK.readFileAsString((Utility.getPublicKeyFileName(""))));
                            setPrivatekey(ActiveLedgerSDK.readFileAsString((Utility.getPrivateKeyFileName(""))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(KeyPair keyPair) {
                        setKey_Pair(keyPair);
                    }
                });
    }


    public void onboardkeys(View view) {

        if (key_Pair != null) {

            ActiveLedgerSDK.getInstance().onBoardKeys(key_Pair, keyname, "")
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String response) {
                            try {
                                Utility.getInstance().extractID(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("----->", response);

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                            setOnBoardId(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_id)));
                            setOnBoardName(PreferenceManager.getInstance().getStringValueFromKey(Utility.getInstance().getContext().getString(R.string.onboard_name)));
                        }
                    });

        } else {
            setShowToast("Generate Keys First");
        }

    }


    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public KeyPair getKey_Pair() {
        return key_Pair;
    }

    public void setKey_Pair(KeyPair key_Pair) {
        this.key_Pair = key_Pair;
    }

    public void activityOnDestroy() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
        ActiveLedgerSDK.getInstance().tearDown();
    }


    public MutableLiveData<String> getPublickey() {
        if (publickey == null) {
            publickey = new MutableLiveData<>();
        }
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey.postValue(publickey);
    }

    public MutableLiveData<String> getPrivatekey() {
        if (privatekey == null) {
            privatekey = new MutableLiveData<>();
        }
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey.postValue(privatekey);
    }


    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public MutableLiveData<String> getOnBoardId() {
        if (onBoardId == null) {
            onBoardId = new MutableLiveData<>();
        }
        return onBoardId;
    }

    public void setOnBoardId(String onBoardId) {
        this.onBoardId.postValue(onBoardId);
    }

    public MutableLiveData<String> getOnBoardName() {
        if (onBoardName == null) {
            onBoardName = new MutableLiveData<>();
        }
        return onBoardName;
    }

    public void setOnBoardName(String onBoardName) {
        this.onBoardName.postValue(onBoardName);
    }

    public MutableLiveData<String> getShowToast() {
        if (showToast == null) {
            showToast = new MutableLiveData<>();
        }
        return showToast;
    }

    public void setShowToast(String message) {
        this.showToast.postValue(message);
    }

    public void getTerritorialityList() {
        ActiveLedgerSDK.getInstance().getTerritorialityStatus()
                .subscribe(new Observer<Territoriality>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Territoriality response) {
                        //Territoriality object has a list of neighbours and reference to left and right node
                        Log.e("Territoriality --->", response.getStatus());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getTransactionData(String id) {
        ActiveLedgerSDK.getInstance().getTransactionData(id)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String response) {
                        //Territoriality object has a list of neighbours and reference to left and right node
                        Log.e("transaction data --->", response);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void subscribeToEvent(String protocol, String ip, String port, String url) {
        //null to use default listener and observe the messages through 'eventLiveData' or create own listener to observe
        ActiveLedgerSDK.getInstance().subscribeToEvent(protocol, ip, port, url, null);
    }

}
