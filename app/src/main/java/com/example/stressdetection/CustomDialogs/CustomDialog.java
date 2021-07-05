package com.example.stressdetection.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.stressdetection.R;

public class CustomDialog {
    private static Dialog waitAnim;



    public static boolean ShowWait( Context context)
    {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                waitAnim=new Dialog(context);//,android.R.style.Theme_Light);

                waitAnim.requestWindowFeature(Window.FEATURE_NO_TITLE);
                waitAnim.setContentView(R.layout.dialog_wait_anim);

                WindowManager.LayoutParams params = waitAnim.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                waitAnim.getWindow().setGravity(Gravity.CENTER);
                waitAnim.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                waitAnim.getWindow().getAttributes().windowAnimations= R.style.DialogAnimation_2;
                waitAnim.setCancelable(false);


//                TextView txt_mesg=dialog.findViewById(R.id.txt_mesg);



                waitAnim.show();

            }
        });

        return true;

    }

    public static void HideWaitAnim()
    {
        waitAnim.dismiss();

    }

}
