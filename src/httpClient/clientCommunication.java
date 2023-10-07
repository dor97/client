package httpClient;

import DTO.*;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import okhttp3.*;

import javax.print.DocFlavor;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static httpClient.Configuration.HTTP_CLIENT;

public class clientCommunication {

    private String BASE_URL = "http://localhost:8080/Server_Web_exploded";
    private String userName = "user";
    private Boolean isAdmin = false;
    private String chosenSimulationName;
    private Integer chosenSimulationId;
    private DTOThreadPoolDetails threadPoolDetails = new DTOThreadPoolDetails();
    private DTORunningSimulationDetails runningSimulationDetails;
    private Object runningSimulationDetailsLock = new Object();
    private List<Integer> simulationFinishedId = new ArrayList<>();
    private Integer indexForFinishedSimulation = 0;
    private Integer prevIndexForFinishedSimulation = 0;
    private Integer indexForWorldsDefinition = 0;
    private List<String> worldDifenichanCollecen = new ArrayList<>();
    private Boolean isWorldDifenichanCollecenUpdated = true;
    private Map<Integer, DTOsimulationApprovementManager> manager = new HashMap<>();
    private List<newRequestData> newRequestDataList = new ArrayList<>();
    private Integer requestIdChosen;

    public void setRequestIdChosen(Integer requestIdChosen){
        this.requestIdChosen = requestIdChosen;
    }

    public Integer getRequestIdChosen(){
        return requestIdChosen;
    }

    public Boolean getIsWorldDifenichanCollecenUpdated(){
        Boolean res = false;
        synchronized (isWorldDifenichanCollecenUpdated){
            res = isWorldDifenichanCollecenUpdated;
            isWorldDifenichanCollecenUpdated = false;
        }
        return res;
    }

    public final List<String> getWorldDifenichanCollecen(){
        synchronized (worldDifenichanCollecen){
            return worldDifenichanCollecen;
        }
    }
    public void setChosenSimulationName(String chosenSimulationName){
        this.chosenSimulationName = chosenSimulationName;
    }

    public String getChosenSimulationName(){
        return chosenSimulationName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setIsAdmin(Boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAdmin(){
        return isAdmin;
    }

    public DTOException loadSimulationDefinition(String xmlFile) throws UnsupportedFileTypeException, NoSuchFileException, Exception{
        if(!isAdmin){
            return null;
        }
        File file = new File(xmlFile);
        if (file.exists() && file.isFile()) {
            // Check if the file extension is .xml
            String fileName = file.getName();
            if (!fileName.endsWith(".xml")) {
                throw new UnsupportedFileTypeException("Not an XML file: " + xmlFile);
            }
        } else {
            throw new NoSuchFileException("File does not exist: " + xmlFile);
        }

        String RESOURCE = "/simulationDefinition/WorldDifenichanCollecen";


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart(null, null, RequestBody.create(file, MediaType.parse("text/xml")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        RequestBody body = RequestBody.create(file, MediaType.parse("text/xml"));


        Request request = new Request.Builder()
                .url(BASE_URL + RESOURCE)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        System.out.println(response.body().string());
        Gson gson = new Gson();
        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;

    }

    public DTOWorldDifenichanCollecen _getWorldDifenichanCollecen(Integer index) throws Exception {

        String RESOURCE = "/simulationDefinition/WorldDifenichanCollecen";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("index", index.toString());


        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());
        Gson gson = new Gson();
        DTOWorldDifenichanCollecen worldDifenichanCollecen = gson.fromJson(response.body().string(), DTOWorldDifenichanCollecen.class);
        return worldDifenichanCollecen;
    }

    public DTOSimulationsDetails getAllSimulationDetails() throws Exception{
        String RESOURCE = "/getAllSimulationsDetails";


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        Request request = new Request.Builder()
                .url(BASE_URL + RESOURCE)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());
        Gson gson = new Gson();
        DTOSimulationsDetails simulationsDetails = gson.fromJson(response.body().string(), DTOSimulationsDetails.class);
        return simulationsDetails;
    }

    public DTOAllSimulationStatus getAllSimulationStatus() throws Exception{
        String RESOURCE = "/getAllSimulationStatus";


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        Request request = new Request.Builder()
                .url(BASE_URL + RESOURCE)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOAllSimulationStatus allSimulationStatus = gson.fromJson(response.body().string(), DTOAllSimulationStatus.class);
        return allSimulationStatus;

    }

    public DTOSimulationDetails getASimulationDetails(String simulationName) throws Exception{
        String RESOURCE = "/getASimulationDetails";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationName", simulationName);

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOSimulationDetails simulationDetails = gson.fromJson(response.body().string(), DTOSimulationDetails.class);
        return simulationDetails;

    }

    public DTODataForReRun getDataForRerun(Integer simulationId) throws Exception{
        String RESOURCE = "/getDataForRerun";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTODataForReRun dataForReRun = gson.fromJson(response.body().string(), DTODataForReRun.class);
        return dataForReRun;
    }

    public DTOIsSimulationGotError getIsSimulationGotError(Integer simulationId) throws Exception{
        String RESOURCE = "/getIsSimulationGotError";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOIsSimulationGotError isSimulationGotError = gson.fromJson(response.body().string(), DTOIsSimulationGotError.class);
        return isSimulationGotError;
    }

    public DTOSimulationError getSimulationError(Integer simulationId) throws Exception{
        String RESOURCE = "/getSimulationError";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOSimulationError simulationError = gson.fromJson(response.body().string(), DTOSimulationError.class);
        return simulationError;
    }

    public DTOSimulationDetailsPostRun getPostRunData(Integer simulationId) throws Exception{
        String RESOURCE = "/getPostRunData";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOSimulationDetailsPostRun simulationDetailsPostRun = gson.fromJson(response.body().string(), DTOSimulationDetailsPostRun.class);
        return simulationDetailsPostRun;
    }

    public DTORunningSimulationDetails getRunningSimulationDTO(Integer simulationId) throws Exception{
        String RESOURCE = "/getRunningSimulationDTO";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTORunningSimulationDetails runningSimulationDetails = gson.fromJson(response.body().string(), DTORunningSimulationDetails.class);
        return runningSimulationDetails;
    }

    public DTOSimulationStatus getSimulationStatus(Integer simulationId) throws Exception{
        String RESOURCE = "/getSimulationStatus";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOSimulationStatus simulationStatus = gson.fromJson(response.body().string(), DTOSimulationStatus.class);
        return simulationStatus;
    }

    public DTOAllRequestData GetSystemData() throws Exception{
        String RESOURCE = "/GetSystemData";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
//        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOAllRequestData allRequestData = gson.fromJson(response.body().string(), DTOAllRequestData.class);
        return allRequestData;
    }

    public DTOThreadPoolDetails getThreadPoolDetails() throws Exception{
        String RESOURCE = "/getThreadPoolDetails";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
//        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOThreadPoolDetails threadPoolDetails = gson.fromJson(response.body().string(), DTOThreadPoolDetails.class);
        return threadPoolDetails;
    }

    public DTOIsSimulationRunning isSimulationRunning(Integer simulationId)throws Exception{
        String RESOURCE = "/isSimulationRunning";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTOIsSimulationRunning isSimulationRunning = gson.fromJson(response.body().string(), DTOIsSimulationRunning.class);
        return isSimulationRunning;
    }

    public DTONumOfCounterRunningSimulation NumOfCounterRunningSimulation(String simulationName) throws Exception{
        String RESOURCE = "/NumOfCounterRunningSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationName", simulationName);

        String url = urlBuilder.build().toString();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

//        DTOSimulationName DtoSimulationName = new DTOSimulationName();
//        DtoSimulationName.setName(simulationName);
//        Gson gson = new Gson();
//        String json = gson.toJson(DtoSimulationName);
//
//        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());

        Gson gson = new Gson();
        DTONumOfCounterRunningSimulation numOfCounterRunningSimulation = gson.fromJson(response.body().string(), DTONumOfCounterRunningSimulation.class);
        return numOfCounterRunningSimulation;
    }

    public DTOException pauseSimulation(Integer simulationId) throws Exception{
        String RESOURCE = "/pauseSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOSimulationId dtoSimulationId = new DTOSimulationId();
        dtoSimulationId.setSimulationId(simulationId);

        String json = gson.toJson(dtoSimulationId);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)  //TODO changed to get, also in the server
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;
    }

    public DTOException prepareSimulation(Integer requestId, String userName, Map<String, String> environmentsValues, Map<String, Integer> entitiesPopulation) throws Exception{
        String RESOURCE = "/prepareSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOPrepareSimulationData prepareSimulationData = new DTOPrepareSimulationData(requestId, userName, environmentsValues, entitiesPopulation);

        String json = gson.toJson(prepareSimulationData);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;

    }

    public DTOException requestsApproval(Integer requestId, String requestStatus) throws Exception{
        if(!isAdmin){
            return null;
        }
        String RESOURCE = "/simulationDefinition/requestsApproval";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTORequestsApproval requestsApproval = new DTORequestsApproval(requestId, requestStatus);

        String json = gson.toJson(requestsApproval);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;
    }

    public DTOException resumeSimulation(Integer simulationId) throws Exception{
        String RESOURCE = "/resumeSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOSimulationId dtoSimulationId = new DTOSimulationId();
        dtoSimulationId.setSimulationId(simulationId);

        String json = gson.toJson(dtoSimulationId);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)  //TODO changed to get, also in the server
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;
    }

    public DTOAllSimulationApprovementManager _getApprovementManager() throws Exception{
        String RESOURCE = "/simulationDefinition/simulationsRequests";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("userName", userName);

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOAllSimulationApprovementManager allSimulationApprovementManager = gson.fromJson(response.body().string(), DTOAllSimulationApprovementManager.class);
        return allSimulationApprovementManager;
    }

    public DTORequestId _askToRunASimulation(String simulationName, Integer amountToRun, Integer ticks, Integer sec)throws Exception{
        String RESOURCE = "/simulationDefinition/simulationsRequests";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOAskToRunASimulation askToRunASimulation = new DTOAskToRunASimulation(simulationName, userName, amountToRun, ticks, sec);

        String json = gson.toJson(askToRunASimulation);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTORequestId requestId = gson.fromJson(response.body().string(), DTORequestId.class);
        return requestId;
    }


    public DTOSimulationId startSimulation() throws Exception{
        String RESOURCE = "/startSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOStartSimulation startSimulation = new DTOStartSimulation(userName);

        String json = gson.toJson(startSimulation);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOSimulationId dtoSimulationId = gson.fromJson(response.body().string(), DTOSimulationId.class);
        return dtoSimulationId;
    }

    public DTOException stopSimulation(Integer simulationId) throws Exception{
        String RESOURCE = "/stopSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("simulationId", simulationId.toString());

        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();

        DTOSimulationId dtoSimulationId = new DTOSimulationId(simulationId);

        String json = gson.toJson(dtoSimulationId);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;
    }

    public DTOFinishedSimulation updateNewlyFinishedSimulation(Integer index) throws Exception{
        String RESOURCE = "/updateNewlyFinishedSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("userName", userName);
        urlBuilder.addQueryParameter("index", index.toString());


        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOFinishedSimulation finishedSimulation = gson.fromJson(response.body().string(), DTOFinishedSimulation.class);
        return finishedSimulation;
    }

    public DTOFinishedSimulationForAdmin updateAdminNewlyFinishedSimulation(Integer index)throws Exception{
        String RESOURCE = "/updateAdminNewlyFinishedSimulation";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("index", index.toString());


        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOFinishedSimulationForAdmin finishedSimulationForAdmin = gson.fromJson(response.body().string(), DTOFinishedSimulationForAdmin.class);
        return finishedSimulationForAdmin;
    }

    public DTOException moveOneStep(Integer simulationId)throws Exception{
        String RESOURCE = "/moveOneStep";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        //urlBuilder.addQueryParameter("index", index.toString());


        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();
        DTOSimulationId dtoSimulationId = new DTOSimulationId();
        dtoSimulationId.setSimulationId(simulationId);
        String json = gson.toJson(dtoSimulationId);

        RequestBody body = RequestBody.create(json, MediaType.parse("text/plain"));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOException exception = gson.fromJson(response.body().string(), DTOException.class);
        return exception;
    }

    public DTOMap getMap(Integer simulationId)throws Exception{
        String RESOURCE = "/getMap";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + RESOURCE).newBuilder();

        // Add query parameters to the URL
        urlBuilder.addQueryParameter("simulationId", simulationId.toString());


        String url = urlBuilder.build().toString();
        Gson gson = new Gson();


//        RequestBody body =
//                new MultipartBody.Builder()
//                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
//                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
//                        .build();


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        Response response = call.execute();

        //System.out.println(response.body().string());


        DTOMap map = gson.fromJson(response.body().string(), DTOMap.class);
        return map;
    }


    public void setChosenSimulationId(Integer chosenSimulationId){
        this.chosenSimulationId = chosenSimulationId;
    }

    public Integer getChosenSimulationId(){
        return chosenSimulationId;
    }
    public Integer getIndexForFinishedSimulation(){
        return indexForFinishedSimulation;
    }

    public Integer getPrevIndexForFinishedSimulation(){
        synchronized (simulationFinishedId) {
            return prevIndexForFinishedSimulation;
        }
    }

    public Integer getIndexForWorldsDefinition(){
        return indexForWorldsDefinition;
    }

    public List<Integer> getNewlyFinishedSimulation(Integer startIndex){
        synchronized (simulationFinishedId) {
            return simulationFinishedId.stream().skip(startIndex).collect(Collectors.toList());
        }
    }

    public DTOThreadPoolDetails getThreadPoolDetailsOnClient(){
        synchronized (threadPoolDetails){
            return threadPoolDetails;
        }
    }

    public void fetchDataFromServer() throws Exception{
        if(isAdmin && !userName.equals("admin")){
            userName = "admin";
        }

        new Thread(() -> loopFetchRunningSimulationDetails()).start();
        new Thread(() -> loopFetchSimulationFinishedId()).start();
        new Thread(() -> loopFetchWorldDifenichanCollecen()).start();
        if(isAdmin) {
            new Thread(() -> loopFetchThreadPoolDetails()).start();
        }
        new Thread(() -> loopFetchSimulationApprovementManager()).start();

    }

    public void loopFetchRunningSimulationDetails(){
        while (true){
            try {
                fetchRunningSimulationDetails();
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
    }

    public void loopFetchSimulationFinishedId(){
        while (true){
            try {
                fetchSimulationFinishedId();
                Thread.sleep(1000);
            }catch (Exception e){

            }

        }
    }

    public void loopFetchWorldDifenichanCollecen() {
        while (true){
            try {
                fetchWorldDifenichanCollecen();
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
    }

    public void loopFetchThreadPoolDetails() {
        if(!isAdmin){
            return;
        }
        while (true){
            try {
                fetchThreadPoolDetails();
                Thread.sleep(1000);
            }catch (Exception e){

            }

        }
    }

    public void loopFetchSimulationApprovementManager() {
        while (true){
            try {
                fetchSimulationApprovementManager();
                Thread.sleep(1000);
            }catch (Exception e){

            }

        }
    }

    public void fetchRunningSimulationDetails() throws Exception{
        if(chosenSimulationId != null) {
            DTORunningSimulationDetails dtoRunningSimulationDetails = getRunningSimulationDTO(chosenSimulationId);
            synchronized (runningSimulationDetailsLock){
                runningSimulationDetails = dtoRunningSimulationDetails;
            }
        }
    }

    public void fetchSimulationFinishedId() throws Exception{
        if (isAdmin) {
            synchronized (simulationFinishedId) {
                prevIndexForFinishedSimulation = indexForFinishedSimulation;
                DTOFinishedSimulationForAdmin finishedSimulationForAdmin = updateAdminNewlyFinishedSimulation(indexForFinishedSimulation);
                finishedSimulationForAdmin.getSimulationIdAndUserNames().stream().forEach(dtoSimulationIdAndUserName -> simulationFinishedId.add(dtoSimulationIdAndUserName.getId()));
                indexForFinishedSimulation += finishedSimulationForAdmin.getSimulationIdAndUserNames().size();
            }
        } else {
            synchronized (simulationFinishedId) {
                prevIndexForFinishedSimulation = indexForFinishedSimulation;
                DTOFinishedSimulation finishedSimulation = updateNewlyFinishedSimulation(indexForFinishedSimulation);
                finishedSimulation.getFinishedSimulation().forEach(id -> simulationFinishedId.add(id));
                indexForFinishedSimulation += finishedSimulation.getFinishedSimulation().size();
            }
        }
    }

    public void fetchWorldDifenichanCollecen() throws Exception{
        synchronized (worldDifenichanCollecen) {
            DTOWorldDifenichanCollecen dtoworldDifenichanCollecen = _getWorldDifenichanCollecen(indexForWorldsDefinition);
            if(dtoworldDifenichanCollecen.getWorldDifenichanCollecen().size() > 0){
                synchronized (isWorldDifenichanCollecenUpdated) {
                    isWorldDifenichanCollecenUpdated = true;
                }
            }
            indexForWorldsDefinition += dtoworldDifenichanCollecen.getWorldDifenichanCollecen().size();
            dtoworldDifenichanCollecen.getWorldDifenichanCollecen().stream().forEach(simulationDefinitionName -> worldDifenichanCollecen.add(simulationDefinitionName));
        }
    }

    public void fetchThreadPoolDetails() throws Exception{
        if(isAdmin) {
            DTOThreadPoolDetails dtoThreadPoolDetails = getThreadPoolDetails();
            synchronized (threadPoolDetails){
                threadPoolDetails = dtoThreadPoolDetails;
            }
        }
    }

    public void fetchSimulationApprovementManager() throws Exception{
        DTOAllSimulationApprovementManager allSimulationApprovementManager = _getApprovementManager();
        synchronized (manager){
            for(DTOsimulationApprovementManager request : allSimulationApprovementManager.getManager().values()){
                if(manager.containsKey(request.getId())){
                    newRequestData requestData = new newRequestData();
                    requestData.setId(request.getId());
                    requestData.setType(typeOfNewRequestData.UPDATED);
                    requestData.setSimulationName(requestData.getSimulationName());
                    requestData.setAmountToRun(requestData.getAmountToRun());
                    if(!(manager.get(request.getId()).getStatus().equals(request.getStatus()))){
                        manager.get(request.getId()).setStatus(request.getStatus());
                        requestData.setStatusChanged(true);
                        requestData.setNewStatus(request.getStatus());
                    }
                    if(!(manager.get(request.getId()).getAmountRun().equals(request.getAmountRun()))){
                        manager.get(request.getId()).setAmountRun(request.getAmountRun());
                        requestData.setAmountRunChanged(true);
                        requestData.setNewAmountRun(request.getAmountRun());
                    }
                    if(!(manager.get(request.getId()).getCurrentRun().equals(request.getCurrentRun()))){
                        manager.get(request.getId()).setCurrentRun(request.getCurrentRun());
                        requestData.setCurrentRunChanged(true);
                        requestData.setNewCurrentRun(request.getCurrentRun());
                    }
                    if(!(manager.get(request.getId()).getDone().equals(request.getDone()))){
                        manager.get(request.getId()).setDone(request.getDone());
                        requestData.setDoneChanged(true);
                        requestData.setNewDone(request.getDone());
                    }
                    if(requestData.getStatusChanged() || requestData.getAmountRunChanged() || requestData.getCurrentRunChanged() || requestData.getDoneChanged()){
                        synchronized (newRequestDataList) {
                            newRequestDataList.add(requestData);
                        }
                    }

                }else {
                    synchronized (newRequestDataList) {
                        newRequestDataList.add(new newRequestData(typeOfNewRequestData.NEW, request.getSimulationName(), request.getId(), request.getAmountToRun(), true, request.getStatus(), true, request.getAmountToRun(), true, request.getCurrentRun(), true, request.getDone()));
                    }
                    manager.put(request.getId(), request);
                }
            }
        }
    }

    public List<newRequestData> getNewRequestDataList(){
        List<newRequestData> res;
        synchronized (newRequestDataList){
            res = newRequestDataList.stream().collect(Collectors.toList());
            newRequestDataList.clear();
        }
        return res;
    }

    public DTORunningSimulationDetails getRunningSimulationData(){
        return runningSimulationDetails;
    }

    public Object getRunningSimulationDataLock(){
        return runningSimulationDetailsLock;
    }

//    public void threadPoolDetails2(ObservableList<QueueManagement> threadPoolList){
//        DTOThreadPoolDetails threadPool;
//        synchronized (threadPoolDetails) {
//            threadPool = threadPoolDetails;
//        }
//        if(threadPool.getException().equals("allGood")){
//            Integer poolSize = threadPool.getThreadPoolDetails().get("Running");
//            Integer finedSimulation = threadPool.getThreadPoolDetails().get("Finished");
//            Integer witting = threadPool.getThreadPoolDetails().get("Waiting");
////            synchronized (this.poolSize){
////                poolSize = this.poolSize;
////            }
////            synchronized (this) {
////                finedSimulation = worldsList.size();
////            }
//
//            Platform.runLater(() -> threadPoolList.clear());
//            setThreadPoolProperties(threadPoolList, witting, "Waiting");
//            setThreadPoolProperties(threadPoolList, poolSize, "Running");
//            setThreadPoolProperties(threadPoolList, finedSimulation, "Finished");
//        }
//    }
//
//    private void setThreadPoolProperties(ObservableList<QueueManagement> threadPoolList, Integer value, String status){
//        Platform.runLater(() -> threadPoolList.add(new QueueManagement(status, value)));
//    }

}
