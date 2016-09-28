package com.naxesa.slowly;

import android.os.*;

/**
 * Created by Lee young teak on 2016-09-18.
 */
public class ProgressThread extends Thread {
    Handler mHandler;
    final static int STATE_DONE = 0;
    final static int STATE_RUNNING = 1;
    int mState;

    ProgressThread(Handler handler){
        mHandler = handler;
    }

    public void run(){
        mState = STATE_RUNNING;
        while(mState == STATE_RUNNING){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(mState == STATE_RUNNING){
                mHandler.sendEmptyMessage(mState);
            }
        }
        mHandler.sendEmptyMessage(mState);
    }

    public void setmState(int state){
        mState = state;
    }
}
