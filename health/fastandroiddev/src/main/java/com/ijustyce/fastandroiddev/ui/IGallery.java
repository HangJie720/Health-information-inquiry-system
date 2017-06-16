/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ijustyce.fastandroiddev.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Gallery;

import com.ijustyce.fastandroiddev.R;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;

public class IGallery extends Gallery {

    // =============================================================================
    // Constants
    // =============================================================================

    public static final int ACTION_DISTANCE_AUTO = Integer.MAX_VALUE;


    public static final float SCALEDOWN_GRAVITY_CENTER = 0.5f;

    /**
     * TODO: Doc
     */
    private float unselectedAlpha;

    /**
     * Camera used for view transformation.
     */
    private Camera transformationCamera;

    /**
     * TODO: Doc
     */
    private int maxRotation = 75;

    /**
     * Factor (0-1) that defines how much the unselected children should be scaled down. 1 means no scaledown.
     */
    private float unselectedScale;

    /**
     * TODO: Doc
     */
    private float scaleDownGravity = SCALEDOWN_GRAVITY_CENTER;

    /**
     * Distance in pixels between the transformation effects (alpha, rotation, zoom) are applied.
     */
    private int actionDistance;

    /**
     * Saturation factor (0-1) of items that reach the outer effects distance.
     */
    private float unselectedSaturation;

//    private int lastWidth, lastHeight;

    // =============================================================================
    // Constructors
    // =============================================================================

    public IGallery(Context context) {
        super(context);
        this.initialize();
    }

    public IGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
        this.applyXmlAttributes(attrs);
    }

    public IGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initialize();
        this.applyXmlAttributes(attrs);
    }

    private void initialize() {
        this.transformationCamera = new Camera();
        this.setSpacing(0);
    }

    /*  ---------------------------------------------
    TODO    这个很有学习的意义哦
    ---------------------------------------------   */
    private void applyXmlAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FancyCoverFlow);

        this.actionDistance = a.getInteger(R.styleable.FancyCoverFlow_actionDistance, ACTION_DISTANCE_AUTO);
        this.scaleDownGravity = a.getFloat(R.styleable.FancyCoverFlow_scaleDownGravity, 1.0f);
        this.maxRotation = a.getInteger(R.styleable.FancyCoverFlow_maxRotation, 45);
        this.unselectedAlpha = a.getFloat(R.styleable.FancyCoverFlow_unselectedAlpha, 0.3f);
        this.unselectedSaturation = a.getFloat(R.styleable.FancyCoverFlow_unselectedSaturation, 0.0f);
        this.unselectedScale = a.getFloat(R.styleable.FancyCoverFlow_unselectedScale, 0.75f);

        a.recycle();
    }

    /**
     * Sets the maximum rotation that is applied to items left and right of the center of the coverflow.
     *
     * @param maxRotation
     */
    public void setMaxRotation(int maxRotation) {
        this.maxRotation = maxRotation;
    }

    /**
     * TODO: Write doc
     *
     * @param unselectedScale
     */
    public void setUnselectedScale(float unselectedScale) {
        this.unselectedScale = unselectedScale;
    }

    /**
     * TODO: Doc
     *
     * @param scaleDownGravity
     */
    public void setScaleDownGravity(float scaleDownGravity) {
        this.scaleDownGravity = scaleDownGravity;
    }

    /**
     * TODO: Write doc
     *
     * @param actionDistance
     */
    public void setActionDistance(int actionDistance) {
        this.actionDistance = actionDistance;
    }

    /**
     * TODO: Write doc
     *
     * @param unselectedAlpha
     */
    @Override
    public void setUnselectedAlpha(float unselectedAlpha) {
        super.setUnselectedAlpha(unselectedAlpha);
        this.unselectedAlpha = unselectedAlpha;
    }

    /**
     * TODO: Write doc
     *
     * @param unselectedSaturation
     */
    public void setUnselectedSaturation(float unselectedSaturation) {
        this.unselectedSaturation = unselectedSaturation;
    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {

        final int childWidth = child.getWidth();
        final int childHeight = child.getHeight();

//        if (childWidth == lastWidth && childHeight == lastHeight){
//            return true;
//        }
//        lastWidth = childWidth;
//        lastHeight = childHeight;

        final float translateX = childWidth / 2.0f;
        final float translateY = childHeight * this.scaleDownGravity;

        // We can cast here because FancyCoverFlowAdapter only creates wrappers.
      //  View item = (View) child;

        // Since Jelly Bean childs won't get invalidated automatically, needs to be added for the smooth coverflow animation
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            child.invalidate();
        }

        final int coverFlowWidth = this.getWidth();
        final int coverFlowCenter = coverFlowWidth / 2;
        final int childCenter = child.getLeft() + childWidth / 2;

        // Use coverflow width when its defined as automatic.
        final int actionDistance = (this.actionDistance == ACTION_DISTANCE_AUTO) ? (int) ((coverFlowWidth + childWidth) / 2.0f) : this.actionDistance;

        // Calculate the abstract amount for all effects.
        final float effectsAmount = Math.min(1.0f, Math.max(-1.0f, (1.0f / actionDistance) * (childCenter - coverFlowCenter)));

        // Clear previous transformations and set transformation type (matrix + alpha).
        t.clear();
        t.setTransformationType(Transformation.TYPE_BOTH);

        // Alpha
        if (this.unselectedAlpha != 1) {
            final float alphaAmount = (this.unselectedAlpha - 1) * Math.abs(effectsAmount) + 1;
            t.setAlpha(alphaAmount);
        }

        // Saturation
        if (this.unselectedSaturation != 1) {
            // Pass over saturation to the wrapper.
            final float saturationAmount = (this.unselectedSaturation - 1) * Math.abs(effectsAmount) + 1;
        //    item.setSaturation(saturationAmount);
        }

        final Matrix imageMatrix = t.getMatrix();

        // Apply rotation.
        if (this.maxRotation != 0) {
            final int rotationAngle = (int) (-effectsAmount * this.maxRotation);
            this.transformationCamera.save();
            this.transformationCamera.rotateY(rotationAngle);
            this.transformationCamera.getMatrix(imageMatrix);
            this.transformationCamera.restore();
        }

        // Zoom.
        if (this.unselectedScale != 1) {
            final float zoomAmount = (this.unselectedScale - 1) * Math.abs(effectsAmount) + 1;
            // Calculate the scale anchor (y anchor can be altered)

            imageMatrix.preTranslate(-translateX, -translateY);
            imageMatrix.postScale(zoomAmount, zoomAmount);
            imageMatrix.postTranslate(translateX, translateY);
        }

        return true;
    }

    public int getSuggestSpace(Context context){

        int width = CommonTool.getScreenWidth(context);
        int height = CommonTool.getScreenHeight(context);

        int space = (5 * width) / 720;

        if (width == 480 && height == 800){

            space -= 10;
        }

        return space;
    }

    public float getSuggestScale(Context context){

        int width = CommonTool.getScreenWidth(context);
        int height = CommonTool.getScreenHeight(context);

        if (width == 1400 && height == 2560){
            return 0.78f;
        }

        if (width == 480 && height == 800){
            return 0.79f;
        }

        if (width == 480 && height == 854){
            return 0.79f;
        }

        if (width == 540 && height == 960){
            return 0.78f;
        }

        //  0.85f
        if (width == 1080 && height == 1920){
            return 0.85f;
        }

        if (width == 720 && height == 1280){
            return 0.85f;
        }

        if (width == 640 && height == 960){
            return 0.85f;
        }

        //  0.9f
        if (width == 1200 && height == 1920){
            return 0.92f;
        }

        if (width == 600 && height == 1024){
            return 0.93f;
        }

        if (width == 1200 && height == 800){
            return 0.9f;
        }

        if (width == 800 && height == 1280){
            return 0.9f;
        }
        return 0.85f;

    }

    //  ============================================================================
    //  add by yangchun ， one time scroll one picture
    //  ============================================================================



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
//        // TODO Auto-generated method stub
//        int kEvent;
//        if(isScrollingLeft(e1, e2)){ //Check if scrolling left
//            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
//        }
//        else{ //Otherwise scrolling right
//            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
//        }
//        onKeyDown(kEvent, null);
        return false;

    }

    // =============================================================================
    // Public classes
    // =============================================================================

    public static class LayoutParams extends Gallery.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
