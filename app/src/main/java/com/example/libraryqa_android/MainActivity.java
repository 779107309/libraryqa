package com.example.libraryqa_android;

import android.graphics.PixelFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Base64;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bean.LvBean;
import com.example.bean.VoiceBean;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKFace;
import com.arcsoft.ageestimation.ASAE_FSDKVersion;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKVersion;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKVersion;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKFace;
import com.arcsoft.genderestimation.ASGE_FSDKGender;
import com.arcsoft.genderestimation.ASGE_FSDKVersion;
import com.guo.android_extend.GLES2Render;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.tools.CameraHelper;
import com.example.arcfacedemo.face.FaceDB;
import com.example.arcfacedemo.widget.CameraFrameData;
import com.example.arcfacedemo.widget.CameraGLSurfaceView;
import com.example.arcfacedemo.widget.CameraSurfaceView;
import com.example.arcfacedemo.CameraUtil;

import pl.droidsonroids.gif.GifImageView;


import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        CameraSurfaceView.OnCameraListener,
        Camera.AutoFocusCallback {
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private ImageView iv;
    private TextView tv_age;
    private TextView tv_sex;
    public static MainActivity instance = null;
    public String returngender;
    public String returnsex(){
        return returngender;
    }
    //人脸追踪版本信息
    AFT_FSDKVersion version = new AFT_FSDKVersion();
    //人脸追踪引擎
    AFT_FSDKEngine engine = new AFT_FSDKEngine();
    //年龄检测版本信息
    ASAE_FSDKVersion mAgeVersion = new ASAE_FSDKVersion();
    //年龄检测引擎
    ASAE_FSDKEngine mAgeEngine = new ASAE_FSDKEngine();
    //性别检测版本信息
    ASGE_FSDKVersion mGenderVersion = new ASGE_FSDKVersion();
    //性别检测引擎
    ASGE_FSDKEngine mGenderEngine = new ASGE_FSDKEngine();

    //AFT_FSDKFace类用来保存检测到的人脸信息
    List<AFT_FSDKFace> result = new ArrayList<>(); //人脸信息列表
    List<ASAE_FSDKAge> ages = new ArrayList<>(); //年龄信息列表
    List<ASGE_FSDKGender> genders = new ArrayList<>(); //性别信息列表

    byte[] mImageNV21 = null;
    private FRAbsLoop mFRAbsLoop;
    AFT_FSDKFace mAFT_FSDKFace = null;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;
    private int a = 0;
    private int mCameraMirror = GLES2Render.MIRROR_NONE;
    private int mSensorOrientation;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
        setContentView(R.layout.activity_main);
        //DisplayMetrics dm = getResources().getDisplayMetrics();
        //int width = dm.widthPixels;
        //int height = dm.heightPixels;
        mGLSurfaceView =(CameraGLSurfaceView)findViewById(R.id.glsurfaceView);
        //LinearLayout.LayoutParams params2= (LinearLayout.LayoutParams) mGLSurfaceView.getLayoutParams();
        //params2.width=width;
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        //mSurfaceView.setLayoutParams(params2);
        //mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        //iv = (ImageView) findViewById(R.id.iv);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        mGLSurfaceView.setOnTouchListener(MainActivity.this);
        mSurfaceView.setOnCameraListener(MainActivity.this);

        int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(String.valueOf(Camera.CameraInfo.CAMERA_FACING_FRONT));
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, getOrientation(displayRotation));
        mSurfaceView.debug_print_fps(true, false);
        mSurfaceView.setZOrderOnTop(true);
        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        engine.AFT_FSDK_GetVersion(version);

        mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.age_key);
        mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);

        mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.gender_key);
        mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);

        mFRAbsLoop = new FRAbsLoop();
        mFRAbsLoop.start();
        //设置波纹
        //final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        //rippleBackground.startRippleAnimation();
        //ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        //imageView.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {
                //跳转到会话界面
        //      Intent intent = new Intent(MainActivity.this,TwoActivity.class);
        //       Bundle bundle = new Bundle();
        //       bundle.putString("sex","性别未知");
        //       bundle.putString("age","年龄未知");
        //       bundle.putString("bmpstring","图像未知");
        //       intent.putExtras(bundle);
        //       startActivity(intent);
        //   }
        //});
    }
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();//从屏幕旋转转换为JPEG方向

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            //Log.d(TAG, "Camera Focus SUCCESS!");
        }else{
            mCamera.autoFocus(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        CameraHelper.touchFocus(mCamera, event, v, this);
        return false;
    }

    @Override
    public Camera setupCamera() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = CameraUtil.getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),
                    mGLSurfaceView.getWidth(),mGLSurfaceView.getHeight());
            int rw = size.width;
            int rh = size.height;
            parameters.setPreviewSize(rw, rh);
            int sw = mGLSurfaceView.getWidth();
            int sh = mGLSurfaceView.getHeight();

            if(rw > rh){
                sw = rw * sh / rh;
            }else if(rw == rh){
                sh = sw;
            }

            mGLSurfaceView.resize(sh,sw);
            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            //parameters.setPreviewFormat(mFormat);
            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            //parameters.setPreviewFpsRange(15000, 30000);
            //parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
            //parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            //parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            //parmeters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            //parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            //ToastUtil.showToast("摄像头参数异常");
            e.printStackTrace();
        }
        if (mCamera != null) {
            mWidth = mCamera.getParameters().getPreviewSize().width;
            mHeight = mCamera.getParameters().getPreviewSize().height;
        }
        return mCamera;
    }

    @Override
    public void setupChanged(int format, int width, int height) {

    }

    @Override
    public boolean startPreviewImmediately() {
        return true;
    }

    @Override
    public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
        //输入的data数据为NV21格式（如Camera里NV21格式的preview数据），其中height不能为奇数，人脸跟踪返回结果保存在result。
        AFT_FSDKError err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result);
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result.get(0).clone();
                mImageNV21 = data.clone();
            }
        }
        //copy rects
        Rect[] rects = new Rect[result.size()];
        for (int i = 0; i < result.size(); i++) {
            rects[i] = new Rect(result.get(i).getRect());
        }
        //clear result.
        result.clear();
        //return the rects for render.
        return rects;
    }

    @Override
    public void onBeforeRender(CameraFrameData data) {

    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    @Override
    public void onAfterRender(CameraFrameData data) {
        mGLSurfaceView.getGLES2Render().draw_rect((Rect[])data.getParams(), Color.GREEN, 2);
    }

    class FRAbsLoop extends AbsLoop {

        AFR_FSDKVersion version = new AFR_FSDKVersion();
        AFR_FSDKEngine engine = new AFR_FSDKEngine();
        AFR_FSDKFace result = new AFR_FSDKFace();
        List<ASAE_FSDKFace> face1 = new ArrayList<>();
        List<ASGE_FSDKFace> face2 = new ArrayList<>();

        @Override
        public void setup() {
            engine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key);
            engine.AFR_FSDK_GetVersion(version);
        }

        @Override
        public void loop() {
            if (mImageNV21 != null) {
                engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight,
                        AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                //age & gender
                face1.clear();
                face2.clear();
                a=a+1;
                face1.add(new ASAE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                face2.add(new ASGE_FSDKFace(mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree()));
                mAgeEngine.ASAE_FSDK_AgeEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face1, ages);
                mGenderEngine.ASGE_FSDK_GenderEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face2, genders);
                if(ages.size() > 0 && genders.size() > 0){
                    final String gender = genders.get(0).getGender() == -1 ? "性别未知" : (genders.get(0).getGender() == 0 ? "男" : "女");
                    final int age = ages.get(0).getAge();
                    //crop
                    byte[] data = mImageNV21;
                    YuvImage yuv = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
                    ExtByteArrayOutputStream ops = new ExtByteArrayOutputStream();

                    //这种方式只截取画面中的人脸部分图片。
                    //yuv.compressToJpeg(mAFT_FSDKFace.getRect(), 80, ops);

                    //截取当前整个摄像头图片。
                    yuv.compressToJpeg(new Rect(0,0,mWidth,mHeight), 70, ops); //不截图人脸

                    //获取到了当前的Bitmap图片，可进行一下操作，如保存到本地上传、显示等。
                    final Bitmap bmp = BitmapFactory.decodeByteArray(ops.getByteArray(), 0, ops.getByteArray().length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int displayRotation = getWindowManager().getDefaultDisplay().getRotation();
                            //iv.setRotation(getOrientation(displayRotation));
                            //iv.setImageBitmap(bmp);
                            //Bitmap image = ((BitmapDrawable) ((ImageView) iv).getDrawable()).getBitmap();
                            tv_age.setText(age+"岁");
                            tv_sex.setText(gender);
                            int width = bmp.getWidth();
                            int height = bmp.getHeight();

                            Matrix matrix = new Matrix();
                            matrix.postRotate(270); //椤烘椂閽熸棆杞?0搴︺€?
                            Bitmap rotatebmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
                            String bmpstring=bitmapToBase64(rotatebmp);
                            String age2= String.valueOf(age);;
                            returngender=gender;
                            if(a%6==1)
                            {
                                Intent intent = new Intent(MainActivity.this,TwoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("sex",returngender);
                                bundle.putString("age",age2);
                                bundle.putString("bmpstring",bmpstring);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    });
                }

                mImageNV21 = null;
            }

        }

        @Override
        public void over() {
            engine.AFR_FSDK_UninitialEngine();
        }

    }
}
