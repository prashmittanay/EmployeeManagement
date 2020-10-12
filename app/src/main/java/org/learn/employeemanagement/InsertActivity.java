package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {
    private static final String TAG = "InsertActivity";
    private Button mEmployeeInsertButton;
    private FrameLayout mCameraFrameLayout;
    private Intent mCameraServiceIntent;
    private TextView mProgressTextView;
    private int mDisplayOrientation;
    private boolean isReleaseRequired = true;

    private BroadcastReceiver mCameraBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pictureUri = intent.getStringExtra(CameraService.PICTURE_URI);
            mProgressTextView.setText("");
            String name = ((EditText) findViewById(R.id.insert_edit_name)).getText().toString();
            String department = ((EditText) findViewById(R.id.insert_edit_department)).getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put(EmployeeContentProvider.NAME, name);
            contentValues.put(EmployeeContentProvider.DEPARTMENT, department);

            Uri uri = getContentResolver().insert(EmployeeContentProvider.CONTENT_URI, contentValues);
            returnToCaller("New User created with ID: " + uri.getPathSegments().get(uri.getPathSegments().size() - 1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mEmployeeInsertButton = findViewById(R.id.button_employee_insert);
        mCameraFrameLayout = findViewById(R.id.frame_insert_camera_preview);
        mProgressTextView = findViewById(R.id.textview_insert_progress);
        mEmployeeInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReleaseRequired = false;
                mCameraServiceIntent = new Intent(getApplicationContext(), CameraService.class);
                mCameraServiceIntent.putExtra(CameraService.DISPLAY_ORIENTATION, mDisplayOrientation);
                startService(mCameraServiceIntent);
                mProgressTextView.setText("Storing Results...");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mCameraBroadcastReceiver, new IntentFilter(CameraService.BROADCAST_CAMERA_URL));
        CameraService.mCamera = CameraUtils.getCameraInstance();
        CameraService.mCameraPreview = new CameraPreview(this, CameraService.mCamera);
        mCameraFrameLayout.addView(CameraService.mCameraPreview);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mCameraBroadcastReceiver);
        if (mCameraServiceIntent != null) {
            stopService(mCameraServiceIntent);
        }
        if (isReleaseRequired)
            CameraUtils.releaseCameraInstance(CameraService.mCamera);
    }

    private void returnToCaller(String returnMessage) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", returnMessage);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mDisplayOrientation = CameraUtils.getCameraDisplayOrientation(InsertActivity.this,
                        Camera.CameraInfo.CAMERA_FACING_BACK, CameraService.mCamera);
                mCamera.setDisplayOrientation(mDisplayOrientation);
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mHolder.getSurface() == null) {
                return;
            }
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }
}