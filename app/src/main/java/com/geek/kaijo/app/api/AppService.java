package com.geek.kaijo.app.api;

import android.support.annotation.NonNull;

import com.geek.kaijo.mvp.model.entity.Banner;
import com.geek.kaijo.mvp.model.entity.BaseArrayResult;
import com.geek.kaijo.mvp.model.entity.BaseResult;
import com.geek.kaijo.mvp.model.entity.Case;
import com.geek.kaijo.mvp.model.entity.CaseAttribute;
import com.geek.kaijo.mvp.model.entity.CaseInfo;
import com.geek.kaijo.mvp.model.entity.Grid;
import com.geek.kaijo.mvp.model.entity.ServiceBean;
import com.geek.kaijo.mvp.model.entity.SocialThing;
import com.geek.kaijo.mvp.model.entity.Street;
import com.geek.kaijo.mvp.model.entity.Thing;
import com.geek.kaijo.mvp.model.entity.UploadFile;
import com.geek.kaijo.mvp.model.entity.UploadTest;
import com.geek.kaijo.mvp.model.entity.User;
import com.geek.kaijo.mvp.model.entity.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * 服务器请求Service
 * Created by LiuLi on 2017/10/24.
 */

public interface AppService {

    /**
     * 登录(8766端口)
     *
     * @param username 用户名/账号
     * @param password 密码
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/user/loginForApp.json")
    Observable<BaseResult<User>> login(@Query("username") String username, @Query("password") String password);

    /**
     * 根取当前登录用户，所属街道、社区、网格(8766端口)
     *
     * @param userId 用户ID
     */
    @NonNull
    @POST("/streetCommunity/findStreetCommunityAndWorkunitDefaultById.json")
    Observable<BaseResult<UserInfo>> findStreetById(@Query("userId") String userId);

    /**
     * 文件上传(8088端口)
     * file: 文件对象
     * fileName: 文件名称
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.FILE_UPLOAD_DOMAIN_NAME})
    @Multipart
    @POST("filecloud-service/file/upload.json")
    Observable<BaseResult<UploadTest>> uploadFile(@Part("files") RequestBody requestBody, @PartMap Map<String, Object> map);

    /**
     * 获得案件属性列表(8766端口) -
     * - 部件属性：0；
     * --物件属性：1
     *
     * @param caseCategory: 案件属性
     */
    @NonNull
    @GET("/case/findCaseCategoryListByCategoryAttribute.json")
    Observable<BaseResult<List<CaseAttribute>>> findCaseCategoryListByAttribute(@Query("categoryAttribute") String caseCategory);

    /**
     * 案件类型关键字搜索接口(8766端口)
     *
     * @param caseKey: 案件类型关键字
     */
    @NonNull
    @GET("/case/findCaseCategoryListByText.json")
    Observable<BaseResult<User>> findCaseCategoryListByText(@Query("text") String caseKey);

    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/case/findCaseInfoList")
    Observable<BaseResult<BaseArrayResult<CaseInfo>>> findCaseInfoList(@Body RequestBody body);

    /**
     * 获取所有街道社区列表接口(8766端口)
     */
    @NonNull
    @GET("/area/findAllStreetCommunity.json")
    Observable<BaseResult<List<Street>>> dfindAllStreetCommunity(@Query("type") int type);

    /**
     * 根据社区id，获取网格列表(8766端口)
     *
     * @param communityId 社区id
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @GET("/grid/findGridListByCommunityId.json")
    Observable<BaseResult<List<Grid>>> findGridListByCommunityId(@Query("communityId") String communityId);

    /**
     * 区域信息保存接口(8766端口)
     */
    @NonNull
    @GET("/area/saveOrUpdateAreaInfo.json")
    Observable<BaseResult<User>> saveOrUpdateAreaInfo();

    /**
     * 提交案件信息保存接口(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/case/addOrUpdateCaseInfo.json")
    Observable<BaseResult<CaseInfo>> addOrUpdateCaseInfo(@Body RequestBody requestBody);

    /**
     * 获取案件列表接口(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/case/findCaseInfoPageList.json")
    Observable<BaseResult<BaseArrayResult<Case>>> findCaseInfoPageList(@Body RequestBody requestBody);

    /**
     * 查看案件信息接口(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST(" /case/findCaseInfoByMap.json")
    Observable<BaseResult<Case>> findCaseInfoByMap(@Query("caseId") String caseId, @Query("caseAttribute") String caseAttribute);

    /**
     * 文件上传(8088端口)
     * file: 文件对象
     * fileName: 文件名称
     * http://221.180.255.233:8088/filecloud-service/file/upload.htm
     */
//    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.FILE_UPLOAD_DOMAIN_NAME})
    @Multipart
//    @POST("http://221.180.255.233:8088/filecloud-service/file/upload.htm")
    @POST("/filecloud-service/file/upload.htm")
//    Observable<BaseResult<User>> uploadFile(@Part MultipartBody.Part file, @Part("age") RequestBody age);
    Observable<UploadFile> uploadFile(@Part List<MultipartBody.Part> partList);

    /**
     * 下一步案件上传(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/case/addCaseAttachList.json")
    Observable<BaseResult<String>> addCaseAttach(@Body RequestBody info);

    /**
     * 获取所有轮播图列表(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @GET("/banner/findAllBannerList.json")
    Observable<BaseResult<List<Banner>>> findAllBannerList();

    /**
     * 查询所有的巡查项目(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @GET("/thing/findThingPage.json")
    Observable<BaseResult<BaseArrayResult<Thing>>> findAllThingList(@Query("currPage") int currPage, @Query("pageSize") int pageSize);

    /**
     * 添加或修改巡查项目(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/thing/saveOrUpdateThing.json")
    Observable<BaseResult<Thing>> saveOrUpdateThing(@Body RequestBody body);

    /**
     * 删除巡查项目(8763端口)
     */
    @NonNull
    @Headers({DOMAIN_NAME_HEADER + Api.USER_DOMAIN_NAME})
    @POST("/thing/delThing.json")
    Observable<BaseResult<Thing>> delThings(@Query("thingIds") String thingIds);


    /**
     * 惠民服务 -获取文章列表接口(8766端口)
     * <p>
     * //@param title      文章标题，不是必传，根据文章标题查找时需要传入该参数
     * //@param categoryId 文章分类菜单id, 必传, 查找某一分类菜单下的所有文章传入该参数
     * 1 计划生育
     * 2 婚姻登记
     * 3 医疗卫生
     * 4 社会救助
     * 5 社会保障
     * 6 死亡殡葬
     * 7 养老服务
     * 8 兵役
     * 9 土地房产
     * 12 办事指南
     * 13 扶持政策
     * //@param currPage   当前页
     * //@param pageSize   每页数量
     */
    @NonNull
    @POST("/cmsArticle/findCmsArticlePage.json")
    Observable<BaseResult<BaseArrayResult<ServiceBean>>> findCmsArticlePage(@Body RequestBody body);

    /**
     * 物件点位列表
     *
     * @param assortId    是	Long	分类ID
     * @param streetId    否	Long	所属街道
     * @param communityId 否	Long	所属社区
     * @param gridId      否	Long	所属网格
     * @param thingType   是	Integer	物件类型
     * @param name        否	String	物件商店名称
     */
    @NonNull
    @POST("/thing/findThingPositionList.json")
    Observable<BaseResult<List<SocialThing>>> findThingPositionList(@Query("currPage") int currPage, @Query("pageSize") int pageSize, @Query("assortId") long assortId, @Query("streetId") long streetId, @Query("communityId") long communityId, @Query("gridId") long gridId, @Query("thingType") int thingType, @Query("name") String name);
}
