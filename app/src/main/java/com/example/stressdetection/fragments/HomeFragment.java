package com.example.stressdetection.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.stressdetection.AdapterBluet;
import com.example.stressdetection.CustomDialogs.CustomDialog;
import com.example.stressdetection.LinkToLocal;
import com.example.stressdetection.MyBluetooth;
import com.example.stressdetection.guide;
import com.example.stressdetection.R;
import com.example.stressdetection.previousRecord;
import com.example.stressdetection.record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class HomeFragment extends  Fragment implements View.OnClickListener {
   private View root;
    public CardView guide,record, prevrecord, summary;

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1002;
    public static final String SENT_SMS_ACTION_NAME = "SMS_SENT";
    public static final String DELIVERED_SMS_ACTION_NAME = "SMS_DELIVERED";

    int BT_flag=2;

    Button btnRecord;

    HomeFragment instance;
    TextView txt_results;
    ArrayList<String> bluet_text;
    ArrayList<String> bluet_address;
    ArrayList<Integer> bluet_images;
    RecyclerView bluetooth_recycler;

    String bt_address = null , bt_name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static MyBluetooth blue_tooth=new MyBluetooth();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);


        guide = (CardView) root.findViewById(R.id.guide);
        record = (CardView) root.findViewById(R.id.record);
        prevrecord = (CardView) root.findViewById(R.id.prevrecord);
        summary = (CardView) root.findViewById(R.id.summary);
        guide.setOnClickListener(this);
        record.setOnClickListener(this);
        prevrecord.setOnClickListener(this);
        summary.setOnClickListener(this);

        if (!checkPermission(Manifest.permission.SEND_SMS))
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        instance=this;
        return root;
    }
    @Override
    public void onClick(View v) {
       Intent i;
       switch (v.getId()){
           case R.id.guide :
               i = new Intent(getActivity(),guide.class);
               startActivity(i);
               break;
           case R.id.record :
//               i = new Intent(getActivity(), record.class);
//               startActivity(i);

               setBtcode();


               break;
           case R.id.prevrecord :
               i = new Intent(getActivity(), previousRecord.class);
               startActivity(i);
               break;
           case R.id.summary :
               i = new Intent(getActivity(), com.example.stressdetection.summary.class);
               startActivity(i);
               break;

       }


    }

    private void setBtcode()
    {


        if(CheckBTState())
        {
//                    blue_tooth.getData(MainActivity.this);

            ArrayList<String> data=new ArrayList<String>();



            if (CustomDialog.ShowWait(getActivity())) {
                data=blue_tooth.GetSensorData(getActivity());


                if (data.size()>0)
                {
                    //fetal_ecg.graphView.setVisibility(View.VISIBLE);

                    String str=data.get(0);
                    for (int i=1;i<data.size();i++)
                    {
                        str+=","+data.get(i);
                    }

                    sendToServer(str);
    //                txt_results.setText(str);
                    Log.d("awais", "setBtcode: "+str);

    //                        userDisplay.tab1.dialogwait.dismiss();
    //                        userDisplay.tab1.sendDataToServer(str);
                    //fetal_ecg.setMyGraph(data);

                }
                else {
    //                        userDisplay.tab1.dialogwait.dismiss();
                }
            }


//                       ArrayList<String> data=new ArrayList<String>();
//                        data=blue_tooth.RecData(userDisplay);
//                        userDisplay.dialogwait.dismiss();
//                        sendDataToServer(data.toString());
//
//                    userDisplay.tab1.setResultsRecycler();
        }
    }


    public boolean CheckBTState()
    {


        try
        {
            blue_tooth.myBluetooth = BluetoothAdapter.getDefaultAdapter();

            if (blue_tooth.myBluetooth == null) {

                Toast.makeText(getActivity(), "Device doesnt Support Bluetooth!", Toast.LENGTH_SHORT).show();

                return false;
            }

            if(!(blue_tooth.myBluetooth.isEnabled()))
            {

                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter, 0);
                try {

                    Thread.sleep(1000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        catch(Exception we){}

        if (!(blue_tooth.BluetoothIsConnected1()))
        {
            Toast.makeText(getActivity(),"Connect To Bluetooth First",Toast.LENGTH_SHORT).show();
            if((blue_tooth.myBluetooth.isEnabled()))
            {
                bluetDialog();
            }

            return false;
        }
        else
        {
            return  true;
        }




    }


    private void bluetDialog(){



        try
        {
            blue_tooth.myBluetooth = BluetoothAdapter.getDefaultAdapter();

            if(!(blue_tooth.myBluetooth.isEnabled()))
            {
                Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableAdapter, 0);
                try {

                    Thread.sleep(1000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        catch(Exception we){}




        if ((blue_tooth.myBluetooth.isEnabled()))
        {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final Dialog dialog=new Dialog(getActivity());//,android.R.style.Theme_Light);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.bluetooth_alert_row);

                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    bluetooth_recycler = dialog.findViewById(R.id.recycler_bluetooth);

                    blue_tooth.bluet_images=new ArrayList<Integer>();
                    blue_tooth.bluet_text=new ArrayList<String>();
                    blue_tooth.bluet_address=new ArrayList<String>();


                    try {
                        bluetooth_connect_device();
                        //Toast.makeText(UserDisplay.this,"Bluetooth_Function_called",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    AdapterBluet adapter=new AdapterBluet(blue_tooth.bluet_images,blue_tooth.bluet_text,blue_tooth.bluet_address,
                            getActivity(),blue_tooth,instance);
                    bluetooth_recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    bluetooth_recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();




                    dialog.show();

                }
            });



        }


    }



    private void bluetooth_connect_device() throws IOException
    {
        try {
            blue_tooth.myBluetooth = BluetoothAdapter.getDefaultAdapter();

            if (blue_tooth.myBluetooth == null) {

                Toast.makeText(getActivity(), "Device doesnt Support Bluetooth!", Toast.LENGTH_SHORT).show();

            }

            else {


                BT_flag=1;
                bt_address = blue_tooth.myBluetooth.getAddress();
                blue_tooth.pairedDevices = blue_tooth.myBluetooth.getBondedDevices();
                if (blue_tooth.pairedDevices.size() > 0) {
                    for (BluetoothDevice bt : blue_tooth.pairedDevices) {
                        bt_address = bt.getAddress().toString();
                        bt_name = bt.getName().toString();

                        setBluetooths(bt_name, bt_address);

                        //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

                    }

                }
                else {
                    Toast.makeText(getActivity(), "Pair Your Device Firts!", Toast.LENGTH_SHORT).show();
                }

            }
        }
        catch(Exception we){}



    }


    private  void setBluetooths(final String b_name,final String b_address)
    {
        blue_tooth.bluet_text.add(b_name);
        blue_tooth.bluet_images.add(R.drawable.ic_bluetooth_blue);
        blue_tooth.bluet_address.add(b_address);


    }


    private void sendToServer(String data)
    {

        LinkToLocal linkToLocal=new LinkToLocal();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, linkToLocal.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        response=response.replace("\n", "").replace("\r", "");
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        if(response.equals("1"))
                        {
                            sendmessage1("03405874431","Stress Detected");
                        }
                        else
                        {
                            sendmessage1("03405874431","No Stress Detected");
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<String, String>();


                map.put("mydata",data);




                return map;
            }

        };
        stringRequest.setShouldCache(false);


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();

        requestQueue.add(stringRequest);

    }


    private boolean checkPermission(String sendSms) {

        int checkpermission= ContextCompat.checkSelfPermission(getActivity(),sendSms);
        // stopFlag=false;
        //isDialog=false;
        return checkpermission== PackageManager.PERMISSION_GRANTED;
    }



    private void sendMessage(final String number,final String mesg)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (checkPermission(Manifest.permission.SEND_SMS))
                {
                    try {
                        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT_SMS_ACTION_NAME), 0);
                        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(DELIVERED_SMS_ACTION_NAME), 0);


                        android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();
                        ArrayList<String> parts = sms.divideMessage(mesg);

                        ArrayList<PendingIntent> sendList = new ArrayList<>();
                        sendList.add(sentPI);

                        ArrayList<PendingIntent> deliverList = new ArrayList<>();
                        deliverList.add(deliveredPI);

                        sms.sendMultipartTextMessage(number, null,parts, sendList, deliverList);
                        //Toast.makeText(MainActivity.this, ""+number, Toast.LENGTH_SHORT).show();

                        Toast.makeText(getActivity(), "Message sent!", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Toast.makeText(getActivity(),"Message Not Sent!",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            SEND_SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }


    private void sendmessage1(String number,String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
    }


}