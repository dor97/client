import httpClient.clientCommunication;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        clientCommunication clientCommunication = new clientCommunication();

        try {
            clientCommunication.loadSimulationDefinition("C:\\Users\\dorra\\IdeaProjects\\Ex1_V2\\src\\resources\\ex3-virus.xml");
            //clientCommunication._getWorldDifenichanCollecen();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}