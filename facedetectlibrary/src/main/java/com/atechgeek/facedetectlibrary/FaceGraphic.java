package com.atechgeek.facedetectlibrary;

/*
 * Vishwam Corp CONFIDENTIAL

 * Vishwam Corp 2018
 * All Rights Reserved.

 * NOTICE:  All information contained herein is, and remains
 * the property of Vishwam Corp. The intellectual and technical concepts contained
 * herein are proprietary to Vishwam Corp
 * and are protected by trade secret or copyright law of U.S.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Vishwam Corp
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.Log;

import com.google.android.gms.vision.face.Face;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
public class FaceGraphic extends GraphicOverlay.Graphic {

    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;


    private Bitmap marker;

    private BitmapFactory.Options opt;
    private Resources resources;

    private int faceId;
    Context context1;

    float isSmilingProbability = -1;
    float eyeRightOpenProbability = -1;
    float eyeLeftOpenProbability = -1;

    public static Paint mHintOutlinePaint;

    public Paint mHintTextPaint;

    private volatile Face mFace;

    public static Float faceArea;

    public static boolean faceIsInTheBox = false, faceRatioOk;

    PointF facePosition;
    PointF faceCenter;
    PointF leftEyePos = null;
    PointF rightEyePos = null;
    PointF noseBasePos = null;
    PointF leftMouthCorner = null;
    PointF rightMouthCorner = null;
    PointF mouthBase = null;
    PointF leftEar = null;
    PointF rightEar = null;
    PointF leftEarTip = null;
    PointF rightEarTip = null;
    PointF leftCheek = null;
    PointF rightCheek = null;

    float eulerZ;
    float eulerY;
    float faceWidth;
    float faceHeight;


    public FaceGraphic(GraphicOverlay overlay, Context context) {
        super(overlay);
        this.context1 = context;
        opt = new BitmapFactory.Options();
        opt.inScaled = false;
        resources = context.getResources();
        marker = BitmapFactory.decodeResource(resources, R.drawable.marker, opt);
        initializePaints(resources);
    }

    public void setId(int id) {
        faceId = id;
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    public void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    private void initializePaints(Resources resources) {
    }

    public void goneFace() {
        mFace = null;
    }

    float left = 0, right = 0, top = 0, bottom = 0;


    @Override
    public void draw(Canvas canvas) {

        Face face = mFace;
        if (face == null) {

            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            isSmilingProbability = -1;
            eyeRightOpenProbability = -1;
            eyeLeftOpenProbability = -1;
            return;
        }



        /*
        float centerX = translateX(face.getPosition().x + face.getWidth() / 2.0f);
        float centerY = translateY(face.getPosition().y + face.getHeight() / 2.0f);
        float offsetX = scaleX(face.getWidth() / 2.0f);
        float offsetY = scaleY(face.getHeight() / 2.0f);


        canvas.drawText("id: " + faceId, centerX + ID_X_OFFSET, centerY + ID_Y_OFFSET, mHintOutlinePaint);
        canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), centerX - ID_X_OFFSET, centerY - ID_Y_OFFSET, mHintOutlinePaint);
        canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), centerX + ID_X_OFFSET * 2, centerY + ID_Y_OFFSET * 2, mHintOutlinePaint);
        canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), centerX - ID_X_OFFSET * 2, centerY - ID_Y_OFFSET * 2, mHintOutlinePaint);
        */


       // Log.i("anshul", "Face Id : " + faceId + " box " + faceIsInTheBox + " ratio" + faceRatioOk + " Mouth BAse=" + mouthBase.x + " " + mouthBase.y);
        Log.i("anshul_face", "Euler y = " + face.getEulerY() + " Euler z : " + face.getEulerZ() + " width=" + face.getWidth() + " left=" + face.getIsLeftEyeOpenProbability() + " right=" + face.getIsRightEyeOpenProbability());


        if (face.getIsLeftEyeOpenProbability() > 0.90 && face.getIsRightEyeOpenProbability() > 0.90 &&
                (face.getEulerY() > -22 && face.getEulerY() < 22) &&
                (face.getEulerZ() > -22 && face.getEulerZ() < 22)) {
            Log.i("anshul", "success");
            FaceDect.OnFaceDetectedListener.onFaceDetected(true);

        } else {
            Log.i("anshul", "fail");
            FaceDect.OnFaceDetectedListener.onFaceDetected(false);
        }

     /*
        // Draw a box around the face.
        float left = centerX - offsetX * 0.75f;
        float right = centerX + offsetX * 0.75f;
        float top = centerY - offsetY * 0.75f;
        float bottom = centerY + offsetY * 0.75f;
   if (mHintOutlinePaint != null) {
            canvas.drawRect(left, top, right, bottom, mHintOutlinePaint);
        }*/


    }
}