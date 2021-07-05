package com.example.stressdetection;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.stressdetection.CustomDialogs.CustomDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MyBluetooth  extends Activity implements View.OnClickListener {


    public String address = null , name=null;
    public  BluetoothAdapter myBluetooth = null;
    public BluetoothSocket btSocket = null;
    public Set<BluetoothDevice> pairedDevices;



    public ArrayList<String> bluet_text=new ArrayList<String>();
    public ArrayList<String> bluet_address=new ArrayList<String>();
    public ArrayList<Integer> bluet_images=new ArrayList<Integer>();
   public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public MyBluetooth() {

        //myBluetooth = BluetoothAdapter.getDefaultAdapter();
        //pairedDevices = myBluetooth.getBondedDevices();

    }

    public boolean MybtSocket(BluetoothDevice dispositivo,Context context)
    {

        try {

            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            btSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }

        if (btSocket.isConnected())
        {

            return true;
        }
        else
        {
            return false;
        }
    }




    public void checkBluetoothAdapter()
    {


        if(!myBluetooth.isEnabled())
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

    private  void setBluetooths1(final String b_name,final String b_address)
    {
        bluet_text.add(b_name);
        bluet_images.add(R.drawable.ic_bluetooth_blue);
        bluet_address.add(b_address);


    }

    public void SetBluetooths()
    {

        if (myBluetooth.isEnabled())
        {
            try
            {

                if (pairedDevices.size()>0)
                {
                    for(BluetoothDevice bt : pairedDevices)
                    {
                        address = bt.getAddress().toString();
                        name = bt.getName().toString();

                        setBluetooths1(name, address);


                    }
                }

            }
            catch(Exception we){}

        }

    }


    public ArrayList<String> GetSensorData(Context context)
    {

        ArrayList<String> data=new ArrayList<String>();
        try {
            final String s="f";
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(s.toString().getBytes());
            Log.d("amjad", "GetSensorData: "+"Hello");

        } catch (IOException e) {
            CustomDialog.HideWaitAnim();
            e.printStackTrace();
            Log.d("amjad", "GetSensorData: "+e.getMessage());
        }
        InputStream inputStream = null;
        final String str="";
        Log.d("amjad", "GetSensorData: Outside Stream");
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            int x=0;
            int count=1;
            char[] y=new char[3];

//            byte b1 = (byte) inputStream.read();
//            Log.d("amjad", "GetSensorData: Inside Stream"+b1);
            for (int i=0;i<12600;i++) {

                if (i>5) {
//                byte[] rawBytes = new byte[3];
                    Log.d("amjad", "GetSensorData: "+"This is a test");
//                inputStream.read(rawBytes);
//                String string = new String(rawBytes, "UTF-8");
                    byte b = (byte) inputStream.read();
                    y[x]=(char)b;
                    x++;

                    if(x>2)
                    {
                        x=0;
//                        Log.d("CheckBT", "CheckBT: "+count+":\t" +Character.getNumericValue(y[0])+"."+Character.getNumericValue(y[2])+""+Character.getNumericValue(y[3]));
                        count++;
                        String s=""+Character.getNumericValue(y[0])+""+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]);

                        float val=Float.valueOf(s);
                        val=val/1024;
                        s=""+val;
                        Log.d("ali", "GetSensorData: "+val);


                        data.add(s);

                    }
                    char test=(char)b;

                    //Log.d("CheckBT", "CheckBT: "+Character.getNumericValue(test));
                    //System.out.println((char) b);
                } else {
                }

            }
            CustomDialog.HideWaitAnim();

        } catch (IOException e) {
            CustomDialog.HideWaitAnim();
            e.printStackTrace();
        }

        return data;



    }



    public ArrayList<String> GetSensorDataEMG()
    {
        ArrayList<String> data=new ArrayList<String>();
        try {
            final String s="m";
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(s.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        final String str="";
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            int x=0;
            int count=1;
            char[] y=new char[3];
            for (int i=0;i<18300;i++) {

//                byte[] rawBytes = new byte[3];
//                inputStream.read(rawBytes);
//                String string = new String(rawBytes, "UTF-8");
                byte b = (byte) inputStream.read();
                y[x]=(char)b;
                x++;

                if(x>2)
                {
                    x=0;
                    // Log.d("CheckBT", "CheckBT: "+count+":\t" +Character.getNumericValue(y[0])+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]));
                    count++;
                    String s=""+Character.getNumericValue(y[0])+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]);



                    data.add(s);

                }
                char test=(char)b;

                //Log.d("CheckBT", "CheckBT: "+Character.getNumericValue(test));
                //System.out.println((char) b);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;



    }


    public ArrayList<String> GetSensorDataEEG()
    {
        ArrayList<String> data=new ArrayList<String>();
        try {
            final String s="m";
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(s.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        final String str="";
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            int x=0;
            int count=1;
            char[] y=new char[3];
            for (int i=0;i<600;i++) {

//                byte[] rawBytes = new byte[3];
//                inputStream.read(rawBytes);
//                String string = new String(rawBytes, "UTF-8");
                byte b = (byte) inputStream.read();
                y[x]=(char)b;
                x++;

                if(x>2)
                {
                    x=0;
                    // Log.d("CheckBT", "CheckBT: "+count+":\t" +Character.getNumericValue(y[0])+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]));
                    count++;
                    String s=""+Character.getNumericValue(y[0])+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]);



                    data.add(s);

                }
                char test=(char)b;

                //Log.d("CheckBT", "CheckBT: "+Character.getNumericValue(test));
                //System.out.println((char) b);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;



    }




    public void CheckBT()
    {
        try {
            final String s="f";
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(s.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        final String str="";
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            int x=0;
            int count=1;
            char[] y=new char[3];
            for (int i=0;i<75;i++) {

//                byte[] rawBytes = new byte[3];
//                inputStream.read(rawBytes);
//                String string = new String(rawBytes, "UTF-8");
                byte b = (byte) inputStream.read();
                y[x]=(char)b;
                x++;

                if(x>2)
                {
                    x=0;
                    Log.d("CheckBT", "CheckBT: "+count+":\t" +Character.getNumericValue(y[0])+""+Character.getNumericValue(y[1])+""+Character.getNumericValue(y[2]));
                    count++;
                }
                char test=(char)b;

                //Log.d("CheckBT", "CheckBT: "+Character.getNumericValue(test));
                //System.out.println((char) b);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    public boolean ConnectToBluetooth(final String bt_address, Context context)
    {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = myBluetooth.getBondedDevices();

        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(bt_address);//connects to the device's address and checks if it's available
        try {

            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            btSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }

        if (btSocket.isConnected())
        {

            return true;
        }
        else
        {
            return false;
        }


    }

    public boolean BluetoothIsConnected1()
    {
        if (btSocket!=null)
        {
            return true;
        }
        else
        {
            return false;
        }

    }



    public boolean BluetoothIsConnected()
    {
        if (btSocket.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }

    }


    private void bluetooth_connect_device() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();
                    name = bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();

                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();

    }


    //Recieve Data from Arduino Via Bluetooth
//    public   ArrayList<String> RecData(UserDisplay userDisplay)
//    {
//        String data="";
//        //Toast.makeText(userDisplay.getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//
//
//        userDisplay.waitAnim("Getting Data");
//
//
//
//
//
//
////        led_on_off("d");
//        /*final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                try {
//                    Thread.sleep(15000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, 0);*/
//        ArrayList<String> rec_data1 = new ArrayList<String>();
//        try {
//            ArrayList<String> rec_data = new ArrayList<String>();
//
//            String temp="";
//            int i=0;
//            while(true)
//            {
//
//
//                if (btSocket != null) {
//                    InputStream inputStream = btSocket.getInputStream();
//                    int byteCount = inputStream.available();
//                    if (byteCount > 0) {
//                        byte[] rawBytes = new byte[byteCount];
//                        inputStream.read(rawBytes);
//                        String string = new String(rawBytes, "UTF-8");
//                        temp=string;
//
//                        if(rec_data.size()!=0 && rec_data.get(i-1).length()==1)
//                        {
//                            string=rec_data.get(i-1)+string;
//
//                        }
//                        data = string;
//                        i++;
//                        if (string.length()>2)
//                        {
//                            rec_data1.add(string);
//                            //rec_data1.add("a");
//                            Log.d("RecData", "Data: "+temp);
//                            Thread.sleep(20);
//
//                        }
//                        rec_data.add(temp);
//
//
//
//
//
//                        // Handler handler=new Handler();
//                        userDisplay.dialogwait.dismiss();
//                        if (rec_data1.size()==1500)
//                        {
//                            break;
//
//                        }
//
//                    }
//
//                }
////                Log.d("RecData", "Length: "+rec_data.size());
////                Log.d("RecData", "Data: "+rec_data);
//            }
//
//
//
//
//        } catch (Exception e) {
//            userDisplay.dialogwait.dismiss();
//            Toast.makeText(getApplicationContext(),"Check Bluetooth Connection/Retry", Toast.LENGTH_SHORT).show();
//            //e.printStackTrace();
//        }
//
//
//        //Log.d("ECG", "RecData: "+data);
//        String display="";
//        StringBuffer sb=new StringBuffer();
//        for (int i=0;i<rec_data1.size();i++)
//        {
//            sb.append(rec_data1.get(i));
//            sb.append("@");
//        }
//        display=sb.toString();
//        Toast.makeText(userDisplay.getApplicationContext(), display, Toast.LENGTH_LONG).show();
//        Log.d("incomming", "RecData: "+rec_data1.size());
//
//        return rec_data1;
//
//    }






    private void led_on_off(String i)
    {
        try
        {
            if (btSocket!=null)
            {

                btSocket.getOutputStream().write(i.toString().getBytes());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Check Bluetooth Connection/Retry", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }








    public int getData(Context context){
        int distance;
        if (btSocket != null) {
            try {
                InputStream data = btSocket.getInputStream();
                String dataToString = getStringFromInputStream(data);
                distance = Integer.parseInt(dataToString);
                Log.d("amjad", "getData: "+distance);
                return distance;
            } catch (IOException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                return  0;
            }

        } else{
            return 0;
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }


    @Override
    public void onClick(View v) {

    }
}
