package com.asus.zenshot.ui.gimbal;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asus.zenshot.R;
import com.sightour.gimbal.GimbalDevice;
import com.sightour.gimbal.GimbalManager;
import com.sightour.gimbal.SimpleGimbalListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GimbalActivity extends AppCompatActivity {
    private static final String TAG = "aaa";
    GimbalManager mGimbalManager;
    View SearchingDevice,NoDevice, DeviceList;
    ImageView imageView1,imageView2;
    List<GimbalDevice>list;
    Button SearchAgain,Skip,connect;
    MyAdapter adapter;
    RecyclerView recyclerView;
    TextView textView,textView1,textView2,textView3;
    Handler mainHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gimbal);
        SearchingDevice = findViewById(R.id.fragment1);
        DeviceList = findViewById(R.id.fragment2);
        NoDevice = findViewById(R.id.fragment3);
        Toolbar toolbar2 = findViewById(R.id.toolbar);
        SearchAgain=findViewById(R.id.search2);
        Skip=findViewById(R.id.skip2);
        connect=findViewById(R.id.connect);

        SearchAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPage(Status.Searching);
                mGimbalManager.addListener(simpleGimbalListener);
            }
        });
        imageView1=findViewById(R.id.iv_wave_);
        imageView2=findViewById(R.id.iv_wave_3);

        textView=findViewById(R.id.help);
        textView1=findViewById(R.id.textView8);
        textView2=findViewById(R.id.textView11);
        mainHandler = new Handler(Looper.getMainLooper());

        adapter=new MyAdapter();
        recyclerView=findViewById(R.id.btpaired);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGimbalManager = GimbalManager.instance(this);
        setSupportActionBar(toolbar2);
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
        switchPage(Status.Searching);
        }
    }
    // TODO: search
    // TODo: nodevice
    // TODO: has device
    enum Status {
        DEVICE_LIST,
        NO_DEVICE,
        Searching
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void switchPage(final Status status) {
        switch (status) {
            default:
            case Searching:

                Log.d(TAG, "start");
                SearchingDevice.setVisibility(View.VISIBLE);
                NoDevice.setVisibility(View.GONE);
                DeviceList.setVisibility(View.GONE);
                setAnim1();
                setAnim2();
                mGimbalManager.addListener(simpleGimbalListener);
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switchPage(Status.NO_DEVICE);
                        Log.d(TAG, "OOPS!");
                    }
                }, 5000);
                break;
            case NO_DEVICE:
                SearchingDevice.setVisibility(View.GONE);
                NoDevice.setVisibility(View.VISIBLE);
                DeviceList.setVisibility(View.GONE);
                break;
            case DEVICE_LIST:
                SearchingDevice.setVisibility(View.GONE);
                NoDevice.setVisibility(View.GONE);
                DeviceList.setVisibility(View.VISIBLE);
                break;
        }
    }
    //update
    SimpleGimbalListener simpleGimbalListener = new SimpleGimbalListener() {
        @Override
        public void gimbalListUpdate(List<GimbalDevice> deviceList) {
            list=mGimbalManager.getDeviceList();
            Log.d(TAG, "ListUpdate");
            adapter.setData(deviceList);
            if(deviceList.size()==0) {
                switchPage(Status.NO_DEVICE);
            }
          else{
              switchPage(Status.DEVICE_LIST);
              Log.d(TAG, "adpter"+deviceList.size());
          }
        }
        @Override
        public void gimbalConnected(GimbalDevice dev) {
            Log.d(TAG, "Connected");
            finish();
        }
        @Override
        public void gimbalConnectError(GimbalDevice dev, int error) {
            Log.d(TAG, "ConnectError");
        }
        @Override
        public void gimbalDisconnected() {
            Log.d(TAG, "Disconnected");
        }
        @Override
        public void gimbalPowerUpdated(int power) {
        }
    };
    private void setAnim1() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        imageView1.startAnimation(as);
    }
    private void setAnim2() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.8f, 1.4f, 1.8f,
        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.1f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        as.setDuration(800);
        as.addAnimation(scaleAnimation);
        as.addAnimation(alphaAnimation);
        imageView2.startAnimation(as);
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }
        }
        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }
}