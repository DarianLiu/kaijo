package com.geek.kaijo.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.geek.kaijo.app.Constant;
import com.geek.kaijo.app.MyApplication;
import com.geek.kaijo.app.api.Api;
import com.geek.kaijo.app.api.RxUtils;
import com.geek.kaijo.mvp.contract.ReportContract;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.Location;
import com.geek.kaijo.mvp.model.entity.Menu;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.UploadCaseFile;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.ui.activity.UploadActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.RxLifecycleUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import dao.CaseInfoDao;
import dao.DaoSession;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@ActivityScope
public class ReportPresenter extends BasePresenter<ReportContract.Model, ReportContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ReportPresenter(ReportContract.Model model, ReportContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获得案件属性列表
     *
     * @param caseCategory 案件类型
     */
    public void findCaseCategoryListByAttribute(int caseCategory) {
        mModel.findCaseCategoryListByAttribute(String.valueOf(caseCategory))
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<CaseAttribute>>(mErrorHandler) {
                    @Override
                    public void onNext(List<CaseAttribute> caseAttributeList) {
                        mRootView.setCaseAttributeList(caseAttributeList);
                    }
                });
    }

    /**
     * 获取所有街道社区列表
     */
    public void findAllStreetCommunity(int type) {
        mModel.findAllStreetCommunity(type).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                                disposable.dispose();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() {
                        mRootView.finishRefresh();//隐藏进度条
                    }
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Street>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Street> streetList) {
                        mRootView.setAllStreetCommunity(streetList);
//                        DataHelper.saveDeviceData(mApplication, Constant.SP_KEY_STREETLIST, streetList);

//                        DaoSession daoSession1 = MyApplication.get().getDaoSession();
//                        daoSession1.getCaseInfoDao()
//                        CaseInfoDao caseInfoDao = daoSession1.getCaseInfoDao();
//                        caseInfoDao.insertOrReplaceInTx(streetList);
                    }
                });
    }


    /**
     * 根据社区id，获取网格列表
     *
     * @param communityId 社区id
     */
    public void findGridListByCommunityId(String communityId) {
        mModel.findGridListByCommunityId(communityId).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<List<Grid>>(mErrorHandler) {
                    @Override
                    public void onNext(List<Grid> gridList) {
                        mRootView.setGridList(gridList);
                    }
                });
    }

    /**
     * 案件上报
     *
     * @param acceptDate            案发时间
     * @param streetId              街道ID
     * @param communityId           社区ID
     * @param gridId                网格ID
     * @param lat                   纬度
     * @param lng                   经度
     * @param source                来源 网格员默认17
     * @param address               地址
     * @param description           问题描述
     * @param caseAttribute         案件属性
     * @param casePrimaryCategory   案件大类
     * @param caseSecondaryCategory 案件小类
     * @param caseChildCategory     案件子类
     * @param handleType            直接处理传1 ，非直接处理传2
     * @param whenType              直接处理( 整改前的写1  整改后写2),  非直接处理 whenType 1
     * @param caseProcessRecordID   直接处理 caseProcessRecordID  19,  非直接处理 caseProcessRecordID  11
     */
    public void addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                    String gridId, String lat, String lng, String source,
                                    String address, String description, String caseAttribute,
                                    String casePrimaryCategory, String caseSecondaryCategory,
                                    String caseChildCategory, String handleType, String whenType,
                                    String caseProcessRecordID,List<UploadFile> uploadPhotoList) {
        mModel.addOrUpdateCaseInfo(userId,acceptDate, streetId, communityId, gridId, lat, lng, source,
                address, description, caseAttribute, casePrimaryCategory, caseSecondaryCategory,
                caseChildCategory, handleType, whenType, caseProcessRecordID,uploadPhotoList)
                .compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<CaseInfo>(mErrorHandler) {
                    @Override
                    public void onNext(CaseInfo caseInfoEntity) {
                        mRootView.uploadCaseInfoSuccess(caseInfoEntity);
//                        mRootView.killMyself();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 上传图片 单张图片
     */
    public void uploadFile(String filePath) {
//        File file = new File(pathUrl);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        File file = new File(filePath);//filePath 图片地址
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("fileName", file.getPath());//
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

        List<MultipartBody.Part> parts = builder.build().parts();

        mModel.uploadFile(parts).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResultResult(mApplication))
                .subscribeWith(new ErrorHandleSubscriber<UploadFile>(mErrorHandler) {
                    @Override
                    public void onNext(UploadFile uploadPhoto) {
                        mRootView.uploadSuccess(uploadPhoto);
                    }

                });
    }


    /**
     * 上传案件
     */
    public void addCaseAttach(List<UploadCaseFile> caseFileList) {
        if (caseFileList == null || caseFileList.size() == 0)
            return;
        String jsonString = new Gson().toJson(caseFileList, new TypeToken<List<UploadCaseFile>>() {
        }.getType());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
//        mRootView.showLoading();
        mModel.addCaseAttach(body).compose(RxUtils.applySchedulers(mRootView))
                .compose(RxUtils.handleBaseResult(mApplication))
                .subscribe(new ErrorHandleSubscriber<String>(mErrorHandler) {
                    @Override
                    public void onNext(String user) {
                       mRootView.showMessage("案件上报成功");
//                        mRootView.hideLoading();
                        mRootView.killMyself();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
//                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 经纬度偏移计算
     *
     * @param userId
     * @param lat
     * @param lng
     */
    private void httpUploadGpsLocation(String userId, double lat, double lng) {

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("lat", lat + "")
                .add("lng", lng + "")
                .build();

        Request request = new Request.Builder().url("http://host:port/GeService").post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(this.getClass().getName(), "11111111111111111111111上传位置信息的返回" + response.body().string());
            }
        });
    }

    /**
     * 经纬度偏移换算
     */
    public void httpUploadGpsLocation(double lng, double lat){

        Observable<Location> observable = Observable.create(new ObservableOnSubscribe<Location>() {
            @Override
            public void subscribe(ObservableEmitter<Location> emitter) throws Exception {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        Log.i(this.getClass().getName(),"rx_call==========线程名======="+Thread.currentThread().getName());
                        Location location = new Location();
                        String xml = gettRequest(1,1);
                        try {
                            byte[] xmlbyte = xml.toString().getBytes("UTF-8");

                            System.out.println(xml);
                            URL url = new URL("http://211.137.35.35:9213/GeService");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);// 允许输出
                            conn.setDoInput(true);
                            conn.setUseCaches(false);// 不使用缓存
                            conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestProperty("Content-Length",
                                    String.valueOf(xmlbyte.length));
                            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//                    conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

                            conn.getOutputStream().write(xmlbyte);
                            conn.getOutputStream().flush();
                            conn.getOutputStream().close();


                            if (conn.getResponseCode() != 200)
                                throw new RuntimeException("请求url失败");

                            InputStream is = conn.getInputStream();// 获取返回数据

                            // 使用输出流来输出字符(可选)
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = is.read(buf)) != -1) {
                                out.write(buf, 0, len);
                            }
                            String string = out.toString("UTF-8");
                            System.out.println("1111111111111111111111经纬度换算===="+string);
                            out.close();

                            // xml解析
                            String version = null;
                            String seqID = null;
                            XmlPullParser parser = Xml.newPullParser();
                            try {
                                parser.setInput(new ByteArrayInputStream(string.substring(1)
                                        .getBytes("UTF-8")), "UTF-8");
                                parser.setInput(is, "UTF-8");
                                int eventType = parser.getEventType();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                    if (eventType == XmlPullParser.START_TAG) {
                                        if ("Envelope".equals(parser.getName())) {
                                            version = parser.getAttributeValue(0);
                                        } else if ("SeqID".equals(parser.getName())) {
                                            seqID = parser.nextText();
                                        } else if ("ResultCode".equals(parser.getName())) {
//                                    resultCode = parser.nextText();
                                        }
                                    }
                                    eventType = parser.next();
                                }
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                                System.out.println(e);
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println(e);
                            }
                            System.out.println("version = " + version);
                            System.out.println("seqID = " + seqID);
//                    System.out.println("resultCode = " + resultCode);

                            location.setLng(1);
                            location.setLat(1);

                            emitter.onNext(location);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            System.out.println(e);
                            e.printStackTrace();
                        }

                        emitter.onComplete();

                    }
                }.start();

            }
        });
        //创建一个下游 Observer
        Observer<Location> observer = new Observer<Location>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Location menuList) {
//                mRootView.preListInfoMenuSuccess(menuList);
            }

            @Override
            public void onError(Throwable e) {
//                mRootView.preError();
            }

            @Override
            public void onComplete() {
            }
        };
        //建立连接
        observable.subscribe(observer);

    }

    public void httpXmlRequest(double lng, double lat){
        new Thread(){
            @Override
            public void run() {
                super.run();
                String xml = gettRequest(1,1);
                try {
                    byte[] xmlbyte = xml.toString().getBytes("UTF-8");

                    System.out.println(xml);
                    URL url = new URL("http://211.137.35.35:9213/GeService");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);// 允许输出
                    conn.setDoInput(true);
                    conn.setUseCaches(false);// 不使用缓存
                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("Content-Length",
                            String.valueOf(xmlbyte.length));
                    conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//                    conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

                    conn.getOutputStream().write(xmlbyte);
                    conn.getOutputStream().flush();
                    conn.getOutputStream().close();


                    if (conn.getResponseCode() != 200)
                        throw new RuntimeException("请求url失败");

                    InputStream is = conn.getInputStream();// 获取返回数据

                    // 使用输出流来输出字符(可选)
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                    String string = out.toString("UTF-8");
                    System.out.println("1111111111111111111111经纬度换算===="+string);
                    out.close();

                    // xml解析
                    String version = null;
                    String seqID = null;
                    XmlPullParser parser = Xml.newPullParser();
                    try {
                        parser.setInput(new ByteArrayInputStream(string.substring(1)
                                .getBytes("UTF-8")), "UTF-8");
                        parser.setInput(is, "UTF-8");
                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                if ("Envelope".equals(parser.getName())) {
                                    version = parser.getAttributeValue(0);
                                } else if ("SeqID".equals(parser.getName())) {
                                    seqID = parser.nextText();
                                } else if ("ResultCode".equals(parser.getName())) {
//                                    resultCode = parser.nextText();
                                }
                            }
                            eventType = parser.next();
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                    System.out.println("version = " + version);
                    System.out.println("seqID = " + seqID);
//                    System.out.println("resultCode = " + resultCode);

//            location.setLng(1);
//            location.setLat(1);
//
//            emitter.onNext(location);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//            System.out.println(e);
                }

            }
        }.start();

    }

    private String gettRequest(double lng,double lat){
        //组建xml数据
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
        xml.append("<Envelope>");
        xml.append("<Header>");
        xml.append("<id>LNASHC</id>");
        xml.append("<pwd>20190505_SeWy</pwd>");
        xml.append("</Header>");
        xml.append("<Body>");
        xml.append("<request>");
        xml.append("<property key=\"lng\">"+lng);
        xml.append("</property>");
        xml.append("<property key=\"lat\">"+lat);
        xml.append("</property>");
        xml.append("<property key=\"heit\">"+0);
        xml.append("</property>");
        xml.append("<property key=\"week\">"+1377);
        xml.append("</property>");
        xml.append("<property key=\"time\">"+252001542);
        xml.append("</property>");

        xml.append("</request>");
        xml.append("</Body>");
        xml.append("</Envelope>");
        return xml.toString();
    }


}
