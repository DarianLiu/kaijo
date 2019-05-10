package com.geek.kaijo.app.api;

import android.text.TextUtils;

import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 参数封装工具类
 * Created by LiuLi on 2018/9/8.
 */

public class RequestParamUtils {

    /**
     * 保存案件信息
     */
    public static RequestBody addOrUpdateCaseInfo(String userId,String acceptDate, String streetId, String communityId,
                                                  String gridId, String lat, String lng, String source,
                                                  String address, String description, String caseAttribute,
                                                  String casePrimaryCategory, String caseSecondaryCategory,
                                                  String caseChildCategory, String handleType, String whenType,
                                                  String caseProcessRecordID,List<UploadFile> uploadPhotoList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("acceptDate", acceptDate + ":00");
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("source", source);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("caseAttribute", caseAttribute);
        jsonObject.addProperty("casePrimaryCategory", casePrimaryCategory);
        jsonObject.addProperty("caseSecondaryCategory", caseSecondaryCategory);
        jsonObject.addProperty("caseChildCategory", caseChildCategory);
        jsonObject.addProperty("handleType", handleType);
        jsonObject.addProperty("whenType", whenType);
        jsonObject.addProperty("caseProcessRecordID", caseProcessRecordID);
        jsonObject.add("attachList", new Gson().toJsonTree(uploadPhotoList));

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 查询案件信息列表
     */
//    public static RequestBody findCaseInfoPageList(int currPage, int pageSize, int caseListStatus,String caseCode,String caseAttribute,String casePrimaryCategory,String caseSecondaryCategory,String caseChildCategory) {
    public static RequestBody findCaseInfoPageList(int currPage, int pageSize,String userId,int handleType,int curNode,String caseCode,String caseAttribute,String casePrimaryCategory,String caseSecondaryCategory,String caseChildCategory) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("currPage", currPage);
        jsonObject.addProperty("pageSize", pageSize);
//        jsonObject.addProperty("caseListStatus", caseListStatus);

        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("handleType", handleType);  // 案件处理类型  1： 自行处理  2：非自行处理
        if(curNode>0){
            jsonObject.addProperty("curNode", curNode);  //12: 案件处理
        }
        if(!TextUtils.isEmpty(caseCode)){
            jsonObject.addProperty("caseCode", caseCode);
        }
        if(!TextUtils.isEmpty(caseAttribute) && !"0".equals(caseAttribute)){
            jsonObject.addProperty("caseAttribute", caseAttribute);
        }
        if(!TextUtils.isEmpty(casePrimaryCategory)){
            jsonObject.addProperty("casePrimaryCategory", casePrimaryCategory);
        }
        if(!TextUtils.isEmpty(caseSecondaryCategory)){
            jsonObject.addProperty("caseSecondaryCategory", caseSecondaryCategory);
        }
        if(!TextUtils.isEmpty(caseChildCategory)){
            jsonObject.addProperty("caseChildCategory", caseChildCategory);
        }

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 案件搜索
     *
     * @param caseCode              案件编号
     * @param caseAttribute         案件属性
     * @param casePrimaryCategory   案件大类
     * @param caseSecondaryCategory 案件小类
     * @param caseChildCategory     案件子类
     */
    public static RequestBody findCaseInfoList(String caseCode, String caseAttribute, String casePrimaryCategory,
                                               String caseSecondaryCategory, String caseChildCategory,String userId,int handleType,int currPage, int pageSize ) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("caseCode", caseCode);
        jsonObject.addProperty("caseAttribute", caseAttribute);
        jsonObject.addProperty("casePrimaryCategory", casePrimaryCategory);
        jsonObject.addProperty("caseSecondaryCategory", caseSecondaryCategory);
        jsonObject.addProperty("caseChildCategory", caseChildCategory);

        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("handleType", handleType);  // 案件处理类型  1： 自行处理  2：非自行处理
        jsonObject.addProperty("currPage", currPage);
        jsonObject.addProperty("pageSize", pageSize);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 获取文章列表（营商环境，惠民服务）
     *
     * @param title      文章标题，不是必传，根据文章标题查找时需要传入该参数
     * @param categoryId 文章分类菜单id, 必传, 查找某一分类菜单下的所有文章传入该参数
     *                   1 计划生育
     *                   2 婚姻登记
     *                   3 医疗卫生
     *                   4 社会救助
     *                   5 社会保障
     *                   6 死亡殡葬
     *                   7 养老服务
     *                   8 兵役
     *                   9 土地房产
     * @param currPage   当前页
     * @param pageSize   每页数量
     */
    public static RequestBody findCmsArticlePage(String title, String categoryId, int currPage, int pageSize) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("currPage", currPage);
        jsonObject.addProperty("pageSize", pageSize);
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }


    /**
     * 案件处理提交
     */
    public static RequestBody addOperate(String userId, String label, String content, String formId, String processId, String curNode, String nextUserId, String firstWorkunit, List<UploadFile> uploadPhotoList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("label", label);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("formId", formId);
        jsonObject.addProperty("processId", processId);
        jsonObject.addProperty("curNode", curNode);
        jsonObject.addProperty("nextUserId", nextUserId);
        jsonObject.addProperty("firstWorkunit", firstWorkunit);
        jsonObject.add("attachList", new Gson().toJsonTree(uploadPhotoList));

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }


    /**
     * 特种设备采集
     */
    public static RequestBody thingInsertInfo(String streetId,String communityId,String gridId,String lat,String lng,String photos,
                                              String checkRecord,String danweiName,String tezhongshebei,String farenName,String address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);
        jsonObject.addProperty("danweiName", danweiName);
        jsonObject.addProperty("tezhongshebei", tezhongshebei);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("address", address);
//        jsonObject.addProperty("caseProcessRecordID", caseProcessRecordID);
//        jsonObject.add("attachList", new Gson().toJsonTree(uploadPhotoList));

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 在建工地
     */
    public static RequestBody thingInsertInfo_gd(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                 String shoulishuNo,String name,String address,String danweiName,String isXukezheng,String zongzaojiaSum,String status,
                                                 String jianzhuSum,String startTime,String endTime,String jianshedanweiName,String jianlidanweiName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);
        jsonObject.addProperty("shoulishuNo", shoulishuNo);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("danweiName", danweiName);
        jsonObject.addProperty("isXukezheng", isXukezheng);
        jsonObject.addProperty("zongzaojiaSum", zongzaojiaSum);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("jianzhuSum", jianzhuSum);
        jsonObject.addProperty("startTime", startTime);
        jsonObject.addProperty("endTime", endTime);
        jsonObject.addProperty("jianshedanweiName", jianshedanweiName);
        jsonObject.addProperty("jianlidanweiName", jianlidanweiName);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 危化品
     */
    public static RequestBody thingInsertInfo_whp(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                 String name,String address,String qita,String type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("qita", qita);
        jsonObject.addProperty("type", type);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 食品
     */
    public static RequestBody thingInsertInfo_food(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                  String jingyingzheName,String farenName,String jingjixingzhiName,String address,String xukezhengNo,String zhutiyetai,
                                                   String isNetwork,String youxiaoTime,String type,String jianguanjigouName,String fengxiandengjiName,String contact,String mobile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("jingyingzheName", jingyingzheName);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("jingjixingzhiName", jingjixingzhiName);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("xukezhengNo", xukezhengNo);
        jsonObject.addProperty("zhutiyetai", zhutiyetai);
        jsonObject.addProperty("isNetwork", isNetwork);
        jsonObject.addProperty("youxiaoTime", youxiaoTime);
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("jianguanjigouName", jianguanjigouName);
        jsonObject.addProperty("fengxiandengjiName", fengxiandengjiName);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("mobile", mobile);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 药品
     */
    public static RequestBody thingInsertInfo_drug(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String name,String address,String farenName,String zhudianyaoshiName,String xukezhengNo,String xukezhengTime,
                                                   String youxiaoTime,String jingyingfangshiName,String jingyingfanweiName,String contact,String mobile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("zhudianyaoshiName", zhudianyaoshiName);
        jsonObject.addProperty("xukezhengNo", xukezhengNo);
        jsonObject.addProperty("xukezhengTime", xukezhengTime);
        jsonObject.addProperty("youxiaoTime", youxiaoTime);
        jsonObject.addProperty("jingyingfangshiName", jingyingfangshiName);
        jsonObject.addProperty("jingyingfanweiName", jingyingfanweiName);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("mobile", mobile);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }


    /**
     * 森林防火
     */
    public static RequestBody thingInsertInfo_senl(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 防台防汛
     */
    public static RequestBody thingInsertInfo_ftfx(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String type,String status,String address,String danweiName,String farenName,String contact,
                                                   String mobile,String jiedaozerenName,String jiedaozerenMobile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("type", type);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("danweiName", danweiName);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("jiedaozerenName", jiedaozerenName);
        jsonObject.addProperty("jiedaozerenMobile", jiedaozerenMobile);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 冬季除雪
     */
    public static RequestBody thingInsertInfo_dongjichuxue(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String address,String isPodao) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("address", address);
        jsonObject.addProperty("isPodao", isPodao);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 文明祭祀
     */
    public static RequestBody thingInsertInfo_jisi(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                           String address,String farenName,String contact,String zerenquRemark) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("address", address);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("zerenquRemark", zerenquRemark);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }


    /**
     * 网吧
     */
    public static RequestBody thingInsertInfo_wangba(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String name,String address,String farenName,String contact) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("contact", contact);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 文物保护单位
     */
    public static RequestBody thingInsertInfo_wenwu(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                   String name,String danweiName,String farenName,String chanquanDanweiName,String contact,String guanlishiyongDanweiName,
                                                   String guanlishiyongFarenName,String guanlishiyongLianxiName,String guanlishiyongContact) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("danweiName", danweiName);
        jsonObject.addProperty("farenName", farenName);
        jsonObject.addProperty("chanquanDanweiName", chanquanDanweiName);
        jsonObject.addProperty("contact", contact);
        jsonObject.addProperty("guanlishiyongDanweiName", guanlishiyongDanweiName);
        jsonObject.addProperty("guanlishiyongFarenName", guanlishiyongFarenName);
        jsonObject.addProperty("guanlishiyongLianxiName", guanlishiyongLianxiName);
        jsonObject.addProperty("guanlishiyongContact", guanlishiyongContact);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }

    /**
     * 演出场所
     */
    public static RequestBody thingInsertInfo_yanchu(String streetId,String communityId,String gridId,String lat,String lng,String photos, String checkRecord,
                                                     String name,String address,String jingyingzheName,String contact) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("streetId", streetId);
        jsonObject.addProperty("communityId", communityId);
        jsonObject.addProperty("gridId", gridId);
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("lng", lng);
        jsonObject.addProperty("photos", photos);
        jsonObject.addProperty("checkRecord", checkRecord);

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("jingyingzheName", jingyingzheName);
        jsonObject.addProperty("contact", contact);

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                new Gson().toJson(jsonObject));
    }




}
