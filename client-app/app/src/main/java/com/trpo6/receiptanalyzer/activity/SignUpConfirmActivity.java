package com.trpo6.receiptanalyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.api.ApiService;
import com.trpo6.receiptanalyzer.api.RetroClient;
import com.trpo6.receiptanalyzer.model.ConfirmSignUpBody;
import com.trpo6.receiptanalyzer.model.RegistrationResponse;
import com.trpo6.receiptanalyzer.model.SignUpBody;
import com.trpo6.receiptanalyzer.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lessc on 07.11.2017.
 */

public class SignUpConfirmActivity extends AppCompatActivity {
    //private EditText username;
    //private EditText phone;
    //private EditText email;
    private EditText ftsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_confirm);

        ftsKey = (EditText) findViewById(R.id.ftsKeyTextEdit);
    }

    /** действия, совершаемые после нажатия на кнопку */
    public void menuOpen(View view) {
        // Проверяем, все ли поля заполнены
        String _ftsKey = ftsKey.getText().toString();
        if (_ftsKey.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Заполните поле ", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // Создаем объект Intent для вызова новой Activity
        final Intent intent = new Intent(this, MainActivity.class);
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            ApiService api = RetroClient.getApiService();
            //User user  = new User("89112356232","pass");
            ConfirmSignUpBody body = new ConfirmSignUpBody(
                    SignUpActivity.signUpBody.getName(),SignUpActivity.signUpBody.getPhone(),Integer.parseInt(_ftsKey),SignUpActivity.signUpBody.getPassword());
            //SignUpBody signUpBody = new SignUpBody(_username,_email,_phone);
            Call<RegistrationResponse> call = api.registerUser(body);
            //Call<RegistrationResponse> call = api.signUp(signUpBody);

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        Log.i("success", response.message().toString());

                        // запуск activity
                        startActivity(intent);
                    } else {
                        Log.e("err0", response.toString());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error: "+response.message(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    Log.e("err1", t.toString());
                }
            });

        } else {
            Log.e("error","cant connect");
        }


    }
}