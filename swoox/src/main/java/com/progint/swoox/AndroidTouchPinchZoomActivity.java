package com.progint.swoox;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AndroidTouchPinchZoomActivity extends Activity {

    TextView myTouchEvent;
    ImageView myImageView;
    Bitmap bitmap;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        myTouchEvent = (TextView)findViewById(R.id.touchevent);
        myImageView = (ImageView)findViewById(R.id.imageview);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        myImageView.setImageBitmap(bitmap);

        myImageView.setOnTouchListener(MyOnTouchListener);
    }

    OnTouchListener MyOnTouchListener
            = new OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub

            switch(event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    //A pressed gesture has started, the motion contains the initial starting location.
                    myTouchEvent.setText("ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //A non-primary pointer has gone down.
                    myTouchEvent.setText("ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
                    myTouchEvent.setText("ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    //A pressed gesture has finished.
                    myTouchEvent.setText("ACTION_UP");
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //A non-primary pointer has gone up.
                    myTouchEvent.setText("ACTION_POINTER_UP");
                    break;
            }

            return true;
        }

    };
}