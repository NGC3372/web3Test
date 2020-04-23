package com.guohaolalala.web3test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button getNumber;
    private String URL = "https://ropsten.infura.io/v3/83b3315113a246e88abb1268847b4a5b";
    private String address = "0x76e0eC050466c4dddBbAA25C3f069C18F026AF95";
    private String privateKey = "DDA1EC2A2312151AC7D82CB9842B72C93EDCAD903B44A185387CD53FD2C29625";
    private String otherAddress = "0x61bEa2058e8d5a01F1f9898E49F53Cc3cc8E6520";


    Admin web3j ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web3j = Admin.build(new HttpService(URL));

        getNumber = findViewById(R.id.number);
        getNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //System.out.println(getNonce(address));
                new Thread(){
                    @Override
                    public void run() {
                        String result = null;
                        try {
                            result = transaction();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (result != null){
                            System.out.println(result);
                        }else
                            System.out.println("null!!!!!!!");
                    }
                }.start();

            }
        });

    }

    private String transaction() throws ExecutionException, InterruptedException {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction transaction = RawTransaction.
                createEtherTransaction(getNonce(address),BigInteger.valueOf(1000000000),
                        BigInteger.valueOf(300000),otherAddress,BigInteger.valueOf(100000000000000000L));
        byte[] signedMessage = TransactionEncoder.signMessage(transaction,credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        return transactionHash;
    }

    private BigInteger getNonce(String address)  {
        EthGetTransactionCount get  = null;
        try {
            get = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        BigInteger nonce = get.getTransactionCount();
        return nonce;
    }


    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    break;
            }
        }
    };
}
