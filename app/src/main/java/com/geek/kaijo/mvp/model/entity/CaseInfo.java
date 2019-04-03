package com.geek.kaijo.mvp.model.entity;

import java.util.List;

/**
 * 案件
 * Created by LiuLi on 2018/9/8.
 */

public class CaseInfo {
    
    private int currPage;
    private int pageSize;
    private String caseId;
    private long createTime;
    private String createUser;
    private long modifyTime;
    private String modifyUser;
    private String acceptDate;
    private String address;
    private String streetId;
    private String caseChildCategory;
    private String caseNumber;
    private String casePrimaryCategory;
    private String caseSecondaryCategory;
    private String caseAttribute;
    private String closeCaseDisposalEntity;
    private String description;
    private String dispatchUserId;
    private String element;
    private String emergencyDegree;
    private String endNode;
    private String grade;
    private String knottyType;
    private String lat;
    private String lightType;
    private String lng;
    private String gridId;
    private String managePoint;
    private String personName;
    private String personTel;
    private String redLightStart;
    private String communityId;
    private String source;
    private String status;
    private String subWorkflowDisposalEntity;
    private String taskId;
    private String urgeFlag;
    private String widgetNumber;
    private String workflowId;
    private String yellowLightStart;
    private String delFlag;
    private String startDate;
    private String endDate;
    private String handleResult;
    private String handleResultDescription;
    private String handerId;
    private List<Attach> attachList;
    private String caseCode;
    private int curNode;
    private Object curNodeList;
    private String caseStatus;
    private String state;
    private int handleType;
    private String userId;
    private int processId;
    private String caseListStatus;
    private int caseType;
    private String roleId;
    private String district;
    private String firstWorkunit;
    private String secondWorkunit;
    private String cityWorkunit;
    private String redLightStartTime;
    private String yellowLightStartTime;
    private String queryFlag;

    public void setHandleType(int handleType) {
        this.handleType = handleType;
    }

    public int getHandleType() {
        return handleType;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public String getCaseListStatus() {
        return caseListStatus;
    }

    public void setCaseListStatus(String caseListStatus) {
        this.caseListStatus = caseListStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCityWorkunit(String cityWorkunit) {
        this.cityWorkunit = cityWorkunit;
    }

    public String getCityWorkunit() {
        return cityWorkunit;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setFirstWorkunit(String firstWorkunit) {
        this.firstWorkunit = firstWorkunit;
    }

    public String getFirstWorkunit() {
        return firstWorkunit;
    }

    public void setQueryFlag(String queryFlag) {
        this.queryFlag = queryFlag;
    }

    public String getQueryFlag() {
        return queryFlag;
    }

    public void setRedLightStartTime(String redLightStartTime) {
        this.redLightStartTime = redLightStartTime;
    }

    public String getRedLightStartTime() {
        return redLightStartTime;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setSecondWorkunit(String secondWorkunit) {
        this.secondWorkunit = secondWorkunit;
    }

    public String getSecondWorkunit() {
        return secondWorkunit;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setYellowLightStartTime(String yellowLightStartTime) {
        this.yellowLightStartTime = yellowLightStartTime;
    }

    public String getYellowLightStartTime() {
        return yellowLightStartTime;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getCaseChildCategory() {
        return caseChildCategory;
    }

    public void setCaseChildCategory(String caseChildCategory) {
        this.caseChildCategory = caseChildCategory;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCasePrimaryCategory() {
        return casePrimaryCategory;
    }

    public void setCasePrimaryCategory(String casePrimaryCategory) {
        this.casePrimaryCategory = casePrimaryCategory;
    }

    public String getCaseSecondaryCategory() {
        return caseSecondaryCategory;
    }

    public void setCaseSecondaryCategory(String caseSecondaryCategory) {
        this.caseSecondaryCategory = caseSecondaryCategory;
    }

    public String getCaseAttribute() {
        return caseAttribute;
    }

    public void setCaseAttribute(String caseAttribute) {
        this.caseAttribute = caseAttribute;
    }

    public String getCloseCaseDisposalEntity() {
        return closeCaseDisposalEntity;
    }

    public void setCloseCaseDisposalEntity(String closeCaseDisposalEntity) {
        this.closeCaseDisposalEntity = closeCaseDisposalEntity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDispatchUserId() {
        return dispatchUserId;
    }

    public void setDispatchUserId(String dispatchUserId) {
        this.dispatchUserId = dispatchUserId;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getEmergencyDegree() {
        return emergencyDegree;
    }

    public void setEmergencyDegree(String emergencyDegree) {
        this.emergencyDegree = emergencyDegree;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getKnottyType() {
        return knottyType;
    }

    public void setKnottyType(String knottyType) {
        this.knottyType = knottyType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLightType() {
        return lightType;
    }

    public void setLightType(String lightType) {
        this.lightType = lightType;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getManagePoint() {
        return managePoint;
    }

    public void setManagePoint(String managePoint) {
        this.managePoint = managePoint;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonTel() {
        return personTel;
    }

    public void setPersonTel(String personTel) {
        this.personTel = personTel;
    }

    public String getRedLightStart() {
        return redLightStart;
    }

    public void setRedLightStart(String redLightStart) {
        this.redLightStart = redLightStart;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubWorkflowDisposalEntity() {
        return subWorkflowDisposalEntity;
    }

    public void setSubWorkflowDisposalEntity(String subWorkflowDisposalEntity) {
        this.subWorkflowDisposalEntity = subWorkflowDisposalEntity;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUrgeFlag() {
        return urgeFlag;
    }

    public void setUrgeFlag(String urgeFlag) {
        this.urgeFlag = urgeFlag;
    }

    public String getWidgetNumber() {
        return widgetNumber;
    }

    public void setWidgetNumber(String widgetNumber) {
        this.widgetNumber = widgetNumber;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getYellowLightStart() {
        return yellowLightStart;
    }

    public void setYellowLightStart(String yellowLightStart) {
        this.yellowLightStart = yellowLightStart;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleResultDescription() {
        return handleResultDescription;
    }

    public void setHandleResultDescription(String handleResultDescription) {
        this.handleResultDescription = handleResultDescription;
    }

    public String getHanderId() {
        return handerId;
    }

    public void setHanderId(String handerId) {
        this.handerId = handerId;
    }

    public List<Attach> getAttachList() {
        return attachList;
    }

    public void setAttachList(List<Attach> attachList) {
        this.attachList = attachList;
    }


    public String getCaseCode() {
        return caseCode;
    }

    public int getCurNode() {
        return curNode;
    }

    public void setCurNode(int curNode) {
        this.curNode = curNode;
    }

    public Object getCurNodeList() {
        return curNodeList;
    }

    public void setCurNodeList(Object curNodeList) {
        this.curNodeList = curNodeList;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getCaseType() {
        return caseType;
    }

    public void setCaseType(int caseType) {
        this.caseType = caseType;
    }

    public Object getRoleId() {
        return roleId;
    }

}
