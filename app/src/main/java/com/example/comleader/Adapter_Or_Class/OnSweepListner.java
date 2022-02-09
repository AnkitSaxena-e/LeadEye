package com.example.comleader.Adapter_Or_Class;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import android.view.View;

import java.lang.reflect.GenericDeclaration;

public class OnSweepListner implements OnTouchListener {

    private GestureDetector gestureDetector;

//    public GestureDetector getGestureDetector(){
//        return gestureDetector;
//    }

    public OnSweepListner(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private  final class GestureListener extends SimpleOnGestureListener{

        private static final int Sweep_Threshold = 100;
        private static final int Sweep_Velocity_Threshold = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            boolean re = false;

                float difY = e2.getY() - e1.getY();
                float difX = e2.getX() - e1.getX();

                if(Math.abs(difX) > Math.abs(difY)){

                    if(Math.abs(difX) > Sweep_Threshold && Math.abs(velocityX) > Sweep_Velocity_Threshold){

                        if(difX > 0){
                            onSweepRight();
                        } else{
                            onSweepLeft();
                        }
                        re = true;
                    }

                } else if(Math.abs(difY) > Sweep_Threshold && Math.abs(velocityY) > Sweep_Velocity_Threshold){

                    if(difY > 0){
                        onSweepBottom();
                    } else {
                        onSweepTop();
                    }
                    re = true;
                }

            return re;
        }



    }

    public void onSweepRight(){

    }

    public void onSweepLeft(){

    }

    public void onSweepTop(){

    }

    public void onSweepBottom(){

    }

    public void onClick(){

    }

    public void onDoubleClick(){

    }

    public void onLongClick(){

    }

}
