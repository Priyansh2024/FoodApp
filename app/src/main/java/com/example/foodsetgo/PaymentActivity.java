package com.example.foodsetgo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.PixelCopy.SUCCESS;
import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class PaymentActivity extends AppCompatActivity implements PaymentStatusListener {
    private ImageView imageView;

    private TextView statusView;

    private Button payButton;
    private Button cod;

    private RadioGroup radioAppChoice;
    private RadioGroup radiobutton2;

    private EditText fieldPayeeVpa;
    private EditText fieldPayeeName;
    private EditText fieldPayeeMerchantCode;
    private EditText fieldTransactionId;
    private EditText fieldTransactionRefId;
    private EditText fieldDescription;
    private EditText fieldAmount;

    private EasyUpiPayment easyUpiPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);
        initViews();
        cod = (Button) findViewById(R.id.cod);
        cod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CashonD();
            }
        });

        payButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();

            }
        });
    }

    private void initViews() {
        imageView = findViewById(R.id.imageView);
        statusView = findViewById(R.id.textView_status);
        payButton = findViewById(R.id.button_pay);

        fieldPayeeVpa = findViewById(R.id.field_vpa);
        fieldPayeeName = findViewById(R.id.field_name);
        fieldPayeeMerchantCode = findViewById(R.id.field_payee_merchant_code);
        fieldTransactionId = findViewById(R.id.field_transaction_id);
        fieldTransactionRefId = findViewById(R.id.field_transaction_ref_id);
        fieldDescription = findViewById(R.id.field_description);
        fieldAmount = findViewById(R.id.field_amount);

        String transactionId = "TID" + System.currentTimeMillis();
        fieldTransactionId.setText(transactionId);
        fieldTransactionRefId.setText(transactionId);

        radioAppChoice = findViewById(R.id.radioAppChoice);

    }

    @SuppressLint("NonConstantResourceId")
    private void pay() {
        String payeeVpa = fieldPayeeVpa.getText().toString();
        String payeeName = fieldPayeeName.getText().toString();
        String payeeMerchantCode = fieldPayeeMerchantCode.getText().toString();
        String transactionId = fieldTransactionId.getText().toString();
        String transactionRefId = fieldTransactionRefId.getText().toString();
        String description = fieldDescription.getText().toString();
        String amount = fieldAmount.getText().toString();
        RadioButton paymentAppChoice = findViewById(radioAppChoice.getCheckedRadioButtonId());


        PaymentApp paymentApp;

        switch (paymentAppChoice.getId()) {
            case R.id.app_default:
                paymentApp = PaymentApp.ALL;
                break;
            case R.id.app_amazonpay:
                paymentApp = PaymentApp.AMAZON_PAY;
                break;
            case R.id.app_bhim_upi:
                paymentApp = PaymentApp.BHIM_UPI;
                break;
            case R.id.app_google_pay:
                paymentApp = PaymentApp.GOOGLE_PAY;
                break;
            case R.id.app_phonepe:
                paymentApp = PaymentApp.PHONE_PE;
                break;
            case R.id.app_paytm:
                paymentApp = PaymentApp.PAYTM;
                break;








            default:
                throw new IllegalStateException("Unexpected value: " + paymentAppChoice.getId());
        }


        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with(paymentApp)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setPayeeMerchantCode(payeeMerchantCode)
                .setDescription(description)
                .setAmount(amount);
        // END INITIALIZATION

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            toast("Error: " + exception.getMessage());
        }
    }


    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        statusView.setText(transactionDetails.toString());

        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }


    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user");
        imageView.setImageResource(R.drawable.ic_failed);
    }

    private void onTransactionSuccess() {
        // Payment Success
        toast("Success");
        imageView.setImageResource(R.drawable.ic_success);
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");
        imageView.setImageResource(R.drawable.ic_success);
    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");
        imageView.setImageResource(R.drawable.ic_failed);
    }


    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void CashonD() {
        toast("See you on delivery Friend");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(PaymentActivity.this, UserHome.class);
                startActivity(i);

            }
        }, 2000);
    }

}
