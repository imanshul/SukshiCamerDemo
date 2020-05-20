package com.atechgeek.facedetectlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.atechgeek.facedetectlibrary.FaceDect.previewFaceDetector;


public class CameraActivity extends AppCompatActivity implements FaceDect.OnMultipleFacesDetectedListener, FaceDect.OnCaptureListener, FaceDect.OnFaceDetectedListener {

    private static final String TAG = "Custom Camera";
    private Context context;
    public CameraSource mCameraSource;

    public static String FILE_PATH_KEY = "filePath";

    // CAMERA VERSION ONE DECLARATIONS
    FaceDect faceDect;

    // COMMON TO BOTH CAMERAS
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private boolean wasActivityResumed = false;

    String username = "vishwam";
    ImageView camera;
    ImageView previewImages;
    TextView tvMessage;
    public static boolean takePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = getApplicationContext();
        takePicture = false;

        camera = findViewById(R.id.camera);

        previewImages = findViewById(R.id.preview);
        RelativeLayout relativeLayout = findViewById(R.id.camRLayout);
        tvMessage = findViewById(R.id.tvMessage);

        mPreview = findViewById(R.id.previewAuth);
        mGraphicOverlay = findViewById(R.id.faceOverlayAuth);
        createCameraSourceFront();
        startCameraSource();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture = true;
            }
        });

        FaceGraphic.mHintOutlinePaint = new Paint();
        FaceGraphic.mHintOutlinePaint.setColor(getResources().getColor(R.color.colorAccent));
        FaceGraphic.mHintOutlinePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void onMultipleFacesDetected(int n) {
        Log.i("anshul", "hello " + n);
    }

    @Override
    public void onCapture(byte[] data, int angle) {

        stopCameraSource();
        Bitmap OriginalBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedbitmap = Bitmap.createBitmap(OriginalBitmap, 0, 0, OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), matrix, true);
        saveFile(rotatedbitmap);
    }


    public void saveFile(Bitmap bitmap) {

        File file = getOutputMediaFile();
        String path = file.getPath();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(FILE_PATH_KEY, file.getAbsolutePath());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCameraSourceFront() {
        faceDect = new FaceDect(this, mGraphicOverlay);

        mCameraSource = new CameraSource.Builder(context, previewFaceDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();

        startCameraSource();
    }

    private void startCameraSource() {

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                // Log.e(TAG, "Unable to start caera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void stopCameraSource() {
        mPreview.stop();
    }

    @Override
    public void onFaceDetected(boolean isFaceDetected) {
        mPreview.onFaceDetected(isFaceDetected);
        camera.setEnabled(isFaceDetected);
        if (isFaceDetected) {
            camera.setAlpha(1.0f);
            tvMessage.setVisibility(View.GONE);
        } else {
            camera.setAlpha(0.4f);
            tvMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasActivityResumed) {
            createCameraSourceFront();
        }
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();

        wasActivityResumed = true;
        stopCameraSource();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCameraSource();
    }

    public File getOutputMediaFile() {

        final String TAG = "CameraPreview";

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Camera");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        long time = System.currentTimeMillis();
        File file = new File(mediaStorageDir.getPath() + File.separator + time + ".jpg");

        return file;
    }
}
