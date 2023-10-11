package httpClient;

import DTO.approvementStatus;

public class newRequestData {
    private typeOfNewRequestData type;

    private Integer id;

    private Boolean isStatusChanged = false;
    private approvementStatus newStatus;
    private Boolean isAmountRunChanged = false;
    private Integer newAmountRun;

    private Boolean isCurrentRunChanged = false;
    private Integer newCurrentRun;

    private Boolean isDoneChanged = false;
    private Integer newDone;
    private Integer amountToRun;
    private String simulationName;
    private String userName;
    private Integer sec;
    private Integer tick;

    public newRequestData(typeOfNewRequestData type, String simulationName, Integer id, Integer amountToRun, Boolean isStatusChanged, approvementStatus newStatus, Boolean isAmountRunChanged, Integer newAmountRun, Boolean isCurrentRunChanged, Integer newCurrentRun, Boolean isDoneChanged, Integer newDone, String userName, Integer tick, Integer sec) {
        this.type = type;
        this.simulationName = simulationName;
        this.id = id;
        this.amountToRun = amountToRun;
        this.isStatusChanged = isStatusChanged;
        this.newStatus = newStatus;
        this.isAmountRunChanged = isAmountRunChanged;
        this.newAmountRun = newAmountRun;
        this.isCurrentRunChanged = isCurrentRunChanged;
        this.newCurrentRun = newCurrentRun;
        this.isDoneChanged = isDoneChanged;
        this.newDone = newDone;
        this.userName = userName;
        this.tick = tick;
        this.sec = sec;
    }

    public newRequestData(){

    }

    public void setAmountToRun(Integer amountToRun){
        this.amountToRun = amountToRun;
    }

    public void setSimulationName(String simulationName){
        this.simulationName = simulationName;
    }

    public Integer getAmountToRun(){
        return amountToRun;
    }

    public String getSimulationName(){
        return simulationName;
    }


    public void setStatusChanged(Boolean statusChanged) {
        isStatusChanged = statusChanged;
    }

    public void setNewStatus(approvementStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setAmountRunChanged(Boolean amountRunChanged) {
        isAmountRunChanged = amountRunChanged;
    }

    public void setNewAmountRun(Integer newAmountRun) {
        this.newAmountRun = newAmountRun;
    }

    public void setCurrentRunChanged(Boolean currentRunChanged) {
        isCurrentRunChanged = currentRunChanged;
    }

    public void setNewCurrentRun(Integer newCurrentRun) {
        this.newCurrentRun = newCurrentRun;
    }

    public void setDoneChanged(Boolean doneChanged) {
        isDoneChanged = doneChanged;
    }

    public void setNewDone(Integer newDone) {
        this.newDone = newDone;
    }

    public Boolean getStatusChanged() {
        return isStatusChanged;
    }

    public approvementStatus getNewStatus() {
        return newStatus;
    }

    public Boolean getAmountRunChanged() {
        return isAmountRunChanged;
    }

    public Integer getAmountRun() {
        return newAmountRun;
    }

    public Boolean getCurrentRunChanged() {
        return isCurrentRunChanged;
    }

    public Integer getCurrentRun() {
        return newCurrentRun;
    }

    public Boolean getDoneChanged() {
        return isDoneChanged;
    }

    public Integer getDone() {
        return newDone;
    }

    public typeOfNewRequestData getType(){
        return type;
    }

    public void setType(typeOfNewRequestData type){
        this.type = type;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSec() {
        return sec;
    }

    public void setSec(Integer sec) {
        this.sec = sec;
    }

    public Integer getTick() {
        return tick;
    }

    public void setTick(Integer tick) {
        this.tick = tick;
    }
}
