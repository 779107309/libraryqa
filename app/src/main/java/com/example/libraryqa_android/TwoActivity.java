package com.example.libraryqa_android;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
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
import android.util.Base64;
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

import java.util.ArrayList;
import java.util.List;
import com.example.arcfacedemo.CameraUtil;

import pl.droidsonroids.gif.GifImageView;

public class TwoActivity extends AppCompatActivity implements View.OnTouchListener,
        CameraSurfaceView.OnCameraListener,
        Camera.AutoFocusCallback {
    private ArrayList<LvBean> dataList = new ArrayList<>();
    private HttpPost httpPost;
    private HttpPost2 httpPost2;
    private Info info = new Info();
    private static Handler handler = new Handler();
    //显示回答列表
    private ListView mLv_chat;
    //存储回答列表
    private TwoActivity.MyAdapter mAdapter;
    //显示二维码
    private ImageView mImageView;
    private ImageView mmapImage;
    private Integer aswlength=0;
    //显示用户问题
    private TextView tvAsk;
    //显示二维码"扫一扫"
    private TextView swTextview;
    //用户问题字符串
    private String kgAsk;
    //判断是否有动作
    private boolean isTouch = true;
    //存储post返回的search_answer
    private List<Info.SearchAsw> searchAnswers;
    //post返回的search_answer中前三条问题
    private SpannableString searchAsk1, searchAsk2, searchAsk3;
    //计时
    private CountTimer countTimerView;
    //显示gif动画
    private GifImageView gifImageView;
    //二维码中间logo
    private Bitmap logo;
    private Button bt;
    private EditText et;
    private String sex;
    private String age;
    private String bmpstring;
    private String mm=null;
    String url;
    private CameraSurfaceView mSurfaceView;
    private CameraGLSurfaceView mGLSurfaceView;
    private ImageView iv;
    private TextView tv_age;
    private TextView tv_sex;

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
    byte[] mImageNV211 = null;
    private FRAbsLoop mFRAbsLoop;
    AFT_FSDKFace mAFT_FSDKFace = null;
    private Camera mCamera;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mCameraMirror = GLES2Render.MIRROR_NONE;
    private int mSensorOrientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏显示
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
        setContentView(R.layout.two_activity);
        Bundle bunde = this.getIntent().getExtras();
        sex = bunde.getString("sex");
        age = bunde.getString("age");
        bmpstring =bunde.getString("bmpstring");
        initVoice();
        //获取需要的view
        initView();
        mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
        mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
        //iv = (ImageView) findViewById(R.id.iv);
        //tv_age = (TextView) findViewById(R.id.tv_age);
        //
        //
        //tv_sex = (TextView) findViewById(R.id.tv_sex);

        mGLSurfaceView.setOnTouchListener(TwoActivity.this);
        mSurfaceView.setOnCameraListener(TwoActivity.this);

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

        engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5);
        engine.AFT_FSDK_GetVersion(version);

        mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.age_key);
        mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion);

        mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.gender_key);mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion);

        mFRAbsLoop = new FRAbsLoop();
        mFRAbsLoop.start();
    }
    private void initVoice() {
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c88a03c");
    }

    private void initView() {
        //获取gifImageView
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        //设置gif动画
        gifImageView.setImageResource(R.drawable.picture1);
        //获取lv_chat
        mLv_chat = (ListView) findViewById(R.id.lv_chat);
        //获取tv_ask1
        tvAsk = (TextView) findViewById(R.id.tv_ask1);
        //获取sw_tv
        swTextview = (TextView) findViewById(R.id.sw_tv);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mmapImage = (ImageView) findViewById(R.id.mapImage);
        //获取二维码logo
        logo= BitmapFactory.decodeResource(super.getResources(),R.drawable.lib_logo);
        mAdapter = new TwoActivity.MyAdapter();
        //设置计时器为一分钟
        countTimerView = new CountTimer(60000,1000,
                TwoActivity.this);
        //刷新mLv_chat
        mLv_chat.setAdapter(mAdapter);
        //设置问候语
        String greet= "您好，有什么能为您服务？";
        if(sex.equals("男")){
            greet = "先生您好，有什么能为您服务？";
        }
        if(sex.equals("女")){
            greet = "女士您好，有什么能为您服务？";
        }
        LvBean greetBean = new LvBean(greet, 0, -1);
        //问候语加入会话列表
        dataList.add(greetBean);
        //刷新lv
        mAdapter.notifyDataSetChanged();
        //lv滚动到最后
        mLv_chat.setSelection(dataList.size() - 1);
        //语音问候
        readAsw(greet);
        bt = (Button)findViewById(R.id.bt);
        et = (EditText)findViewById(R.id.url);
        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //获取EditText控件ledShow的输入内容，并用ledShow显示
                url = et.getText().toString();
                bt.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
                httpPost2 = new HttpPost2();
                httpPost2.hPost(age, sex,bmpstring, url);
            }
        });


    }

    public void startVoice() {
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        final RecognizerDialog iatDialog = new RecognizerDialog(this, null);
        //2.设置听写参数
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //把解析返回的文本拼接起来
        final StringBuilder sb = new StringBuilder();
        //3.设置回调接口
        iatDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                //使用gson处理语音识别结果并返回字符串
                String voiceStr = parseData(recognizerResult.getResultString());
                //在录音完成时拼接
                sb.append(voiceStr);
                if(isLast) {
                    kgAsk = sb.toString();
                    //显示用户问题
                    tvAsk.setVisibility(View.VISIBLE);
                    tvAsk.setText(kgAsk);
                    //等待服务器返回结果
                    HttpPost httpPost = new HttpPost();
                    //向服务器发送post请求
                    httpPost.hPost(kgAsk, info, url);
                    while (info.getFirst() == null||info.getFirst().equals("repeat")) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    dataList.clear();
                    //获取服务器返回的graph_answer
                    String asw = info.getFirst();
                    if(asw.length() == 0){
//                        asw += "您的问题难倒我了，下面的问题是否感兴趣？";
//                        LvBean aswBean = new LvBean(asw, -1, -1);
//                        dataList.add(aswBean);
                        //graph_answer为空，返回search_answer
                        asw += "您的问题难倒我了" + "\n" + "下面的问题点一点有惊喜哦！";
                        searchAnswers = info.getSearch_answer();
                        int id = -1;
                        LvBean aswBean = new LvBean(asw, -1, id);
                        LvBean searchBean1  = new LvBean(searchAnswers.get(0).getQuestion(), 2, -1);
                        LvBean searchBean2  = new LvBean(searchAnswers.get(1).getQuestion(), 3, -1);
                        LvBean searchBean3 = new LvBean(searchAnswers.get(2).getQuestion(), 4, -1);
                        dataList.add(aswBean);
                        dataList.add(searchBean1);
                        dataList.add(searchBean2);
                        dataList.add(searchBean3);
                        searchAsk1 = new SpannableString(searchAnswers.get(0).getQuestion());
                        searchAsk2 = new SpannableString(searchAnswers.get(1).getQuestion());
                        searchAsk3 = new SpannableString(searchAnswers.get(2).getQuestion());
                    }
                    else {
                        LvBean aswBean = new LvBean(asw, -1, -1);
                        dataList.add(aswBean);
                    }
                    //dataList.add(askBean);
                    //刷新lv
                    mAdapter.notifyDataSetChanged();
                    //lv滚动到最后
                    mLv_chat.setSelection(dataList.size() - 1);
                    //合成出回答语音
                    gifImageView.setImageResource(R.drawable.picture3);
                    aswlength=asw.length();
                    Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(kgAsk + "\n" + asw, 230, "UTF-8", "H", "0", Color.RED, Color.WHITE, null, logo, 0.2F);
                    String base64data= info.getSecond();
                    if(base64data!=null){
                    byte[] byteIcon = Base64.decode(base64data, Base64.DEFAULT);
                    //调整异常数据
                    for (int i = 0; i < byteIcon.length; ++i) {
                        if (byteIcon[i] < 0) {
                            byteIcon[i] += 256;
                        }
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
                    mmapImage.setImageBitmap(bitmap2);}
                    mImageView.setImageBitmap(mBitmap);
                    swTextview.setVisibility(View.VISIBLE);
                    readAsw(asw);
                    info.setSecondNull();
                    info.setfirstNull();
                    mm=null;
                }
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        //4.开始听写
        iatDialog.show();
    }

    public void readAsw(String text) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "70");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(text, null);
        Integer time11=70*aswlength+400;
        Timer timer = new Timer();

             //延迟1000ms执行程序
              timer.schedule(new TimerTask() {
                  @Override
            public void run() { mImageNV211=null;
                     }
          }, time11);

    }

    private String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(resultString, VoiceBean.class);
        ArrayList<VoiceBean.STT> ws = voiceBean.ws;

        StringBuilder stringBuilder = new StringBuilder();
        for (VoiceBean.STT stt : ws) {
            String voice = stt.cw.get(0).w;
            stringBuilder.append(voice);
        }
        return stringBuilder.toString();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(TwoActivity.this, R.layout.item_layout, null);
                vh = new ViewHolder();
                vh.tv_ask = (TextView) convertView.findViewById(R.id.tv_ask);
                vh.ll_asw = (LinearLayout) convertView.findViewById(R.id.ll_asw);
                vh.tv_asw = (ScrollTextView) convertView.findViewById(R.id.tv_asw);
                vh.ll_ask = (LinearLayout) convertView.findViewById(R.id.ll_ask);
                vh.tv_search1 = (TextView) convertView.findViewById(R.id.tv_search1);
                vh.tv_search2 = (TextView) convertView.findViewById(R.id.tv_search2);
                vh.tv_search3 = (TextView) convertView.findViewById(R.id.tv_search3);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            LvBean bean = dataList.get(position);
            if (bean.isAsk == 1) {
                vh.ll_ask.setVisibility(View.VISIBLE);
                vh.ll_asw.setVisibility(View.GONE);
                vh.tv_ask.setText(bean.text);
            }
            if(bean.isAsk == 0){
                //vh.ll_greet.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.ll_asw.setVisibility(View.VISIBLE);
                vh.tv_asw.initScrollTextView(getWindowManager(), bean.text, 0);
                vh.tv_asw.setText(bean.text);
                vh.tv_asw.starScroll();
                //vh.tv_greet.setText(bean.text);
            }
            if(bean.isAsk == -1){
                vh.ll_asw.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.initScrollTextView(getWindowManager(), bean.text, 6);
                vh.tv_asw.setText("");
                vh.tv_asw.starScroll();
            }
            if(bean.isAsk == 2) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.VISIBLE);
                vh.tv_search2.setVisibility(View.GONE);
                vh.tv_search3.setVisibility(View.GONE);
                //search_answer添加点击事件，可点击查看答案
                searchAsk1 = new SpannableString(bean.text);
                searchAsk1.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean askBean  = new LvBean(searchAnswers.get(0).getQuestion(), -1, -1);
                        LvBean aswBean = new LvBean(searchAnswers.get(0).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(askBean);
                        dataList.add(aswBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                //设置问题字体颜色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk1.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search1.setText(searchAsk1);
                vh.tv_search1.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(bean.isAsk == 3) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                //vh.ll_greet.setVisibility(View.GONE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.GONE);
                vh.tv_search2.setVisibility(View.VISIBLE);
                vh.tv_search3.setVisibility(View.GONE);
                searchAsk2 = new SpannableString(bean.text);
                searchAsk2.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean aswBean  = new LvBean(searchAnswers.get(1).getQuestion(), -1, -1);
                        LvBean askBean = new LvBean(searchAnswers.get(1).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(aswBean);
                        dataList.add(askBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk2.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search2.setText(searchAsk2);
                vh.tv_search2.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(bean.isAsk == 4) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                //vh.ll_greet.setVisibility(View.GONE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.GONE);
                vh.tv_search2.setVisibility(View.GONE);
                vh.tv_search3.setVisibility(View.VISIBLE);
                searchAsk3 = new SpannableString(bean.text);
                searchAsk3.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean aswBean  = new LvBean(searchAnswers.get(2).getQuestion(), -1, -1);
                        LvBean askBean = new LvBean(searchAnswers.get(2).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(aswBean);
                        dataList.add(askBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk3.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search3.setText(searchAsk3);
                vh.tv_search3.setMovementMethod(LinkMovementMethod.getInstance());

            }
            return convertView;
        }
    }

    //**********************无操作一段时间后显示屏保***********************//
    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();

            }
        });
    }

    //主要的方法，重写dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                isTouch = true;
                countTimerView.start();
                break;
            //否则其他动作计时取消
            default:
                isTouch = false;
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {

        super.onResume();
        timeStart();
    }

    public class CountTimer extends CountDownTimer {
        private Context context;

        /**
         * 参数 millisInFuture       倒计时总时间（如60S，120s等）
         * 参数 countDownInterval    渐变时间（每次倒计1s）
         */
        public CountTimer(long millisInFuture, long countDownInterval,Context context) {
            super(millisInFuture, countDownInterval);
            this.context=context;
        }
        // 计时完毕时触发
        @Override
        public void onFinish() {
            if (isTouch) {
                isTouch = false;
                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {
        }
    }
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();//从屏幕旋转转换为JPEG方向

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * 从指定的屏幕旋转中检索照片方向
     *
     * @param rotation 屏幕方向
     * @return 照片方向（0,90,270,360）
     */
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
            if (mImageNV21 != null&&mImageNV21!= mImageNV211) {
                engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight,
                        AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace.getRect(), mAFT_FSDKFace.getDegree(), result);
                //age & gender
                face1.clear();
                face2.clear();
                mImageNV211=mImageNV21;
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
                            if(url!=null){
                            startVoice();}
                            //mFRAbsLoop=null;
                        }
                    });
                }
                //mImageNV21 = null;
            }

        }

        @Override
        public void over() {
            engine.AFR_FSDK_UninitialEngine();
        }
    }

}
