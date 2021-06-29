package com.example.stressdetection;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stressdetection.fragments.HomeFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class AdapterBluet extends RecyclerView.Adapter<AdapterBluet.ViewHolder> {
    ArrayList<Integer> images;
    ArrayList<String> text;
    ArrayList<String> address;
    Context context;
    MyBluetooth blue_tooth=new MyBluetooth();
    HomeFragment userDisplay;



    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;


    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");





    public AdapterBluet(ArrayList<Integer> images, ArrayList<String> text,ArrayList<String> address,
                        Context context, MyBluetooth blue_tooth,HomeFragment userDisplay) {
        this.images = images;
        this.text = text;
        this.address=address;
        this.context = context;
        this.blue_tooth=blue_tooth;
        this.userDisplay=userDisplay;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_conn_row2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.imageView.setImageResource(images.get(position));
        holder.textView.setMovementMethod(new ScrollingMovementMethod());
        holder.textView.setText(text.get(position));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*userDisplay.waitAnim("Connecting");
                final Dialog dialog=new Dialog(context);
                dialog.setTitle("Connecting Bluetooth...");
                dialog.setContentView(R.layout.bt_conn_anim);

                dialog.getWindow().setBackgroundDrawableResource(R.color.whiteshade);
                dialog.show();

                dialog.setCancelable(false);
*/



                //Toast.makeText(context,"Bluetooth Connection Attempt!",Toast.LENGTH_SHORT).show();

                blue_tooth.myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = blue_tooth.myBluetooth.getRemoteDevice(address.get(position));//connects to the device's address and checks if it's available
                try {



                    blue_tooth.btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(blue_tooth.myUUID);//create a RFCOMM (SPP) connection
                } catch (IOException e) {
//                    userDisplay.dialogwait.dismiss();
                    e.printStackTrace();
                }
                try {

                    Toast.makeText(context,"Connecting...",Toast.LENGTH_LONG).show();

                    blue_tooth.btSocket.connect();







                } catch (IOException e) {
//                    userDisplay.dialogwait.dismiss();
//                    Toast.makeText(context,"Bluetooth Connection\nFailed!",Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
                }
                try {
                    //textView.setText("BT Name: "+name+"\nBT Address: "+bt_address);
                }
                catch(Exception e){}

                if (blue_tooth.btSocket.isConnected())
                {
//                    userDisplay.dialogwait.dismiss();
//                    dialog.setCancelable(true);
//                    dialog.dismiss();
//                    dialog.cancel();



                    try {
                        final String s="a";
                        OutputStream outputStream = null;
                        outputStream = blue_tooth.btSocket.getOutputStream();
                        outputStream.write(s.toString().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(context,"Bluetooth Connected\nScuccessfully with\n"+text.get(position),Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    userDisplay.dialogwait.dismiss();
//                    dialog.setCancelable(true);
//                    dialog.dismiss();
//                    dialog.cancel();
                    Toast.makeText(context,"Bluetooth Connection\nFailed!",Toast.LENGTH_SHORT).show();
                }






            }
        });



    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.txt_bluetooth);

            imageView=itemView.findViewById(R.id.img_bluetooth);
        }
    }
}

