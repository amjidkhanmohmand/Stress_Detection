package com.example.stressdetection;

import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.jetbrains.annotations.NotNull;

        import java.io.IOException;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.FormBody;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

public class summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText num1 = findViewById(R.id.one);
                EditText num2 = findViewById(R.id.two);
                EditText num3 = findViewById(R.id.three);
                EditText num4 = findViewById(R.id.four);

                String value1=num1.getText().toString();
                String value2=num2.getText().toString();
                String value3=num3.getText().toString();
                String value4=num4.getText().toString();

                if(value1 != null && value2!=null && value3 !=null && value4 != null) {

                    OkHttpClient okHttpClient = new OkHttpClient();



                    RequestBody formbody = new FormBody.Builder().add("num1", value1).add("num2", value2).add("num3", value3).add("num4", value4).build();
                    try {

                        Request request = new Request.Builder().url("http://192.168.43.235:5000/model").post(formbody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(summary.this,e.getMessage() , Toast.LENGTH_LONG).show();

                                    }
                                });




                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                TextView textView = findViewById(R.id.textview);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            textView.setText(response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });


                            }
                        });
                    }
                    catch (Exception e) {

                    }
                }

            }
        });

    }
}