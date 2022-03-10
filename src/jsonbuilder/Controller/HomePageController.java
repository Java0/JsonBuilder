package jsonbuilder.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jsonbuilder.PrimaryPage;
import jsonbuilder.Util;


import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class HomePageController  {

    @FXML
    private Button toBuild;

    @FXML
    private TextField modName;

    @FXML
    private TextField modPath;

    @FXML
    private Label tip;

    @FXML
    private Button check;

    private void buildConfig(){

        String modID = modName.getText().toLowerCase(Locale.ROOT);
        if(modName.getLength() !=0 && modPath.getLength() !=0){

            File configFile = new File("resources path.properties");
            if(! configFile.exists()){
                try {
               configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Properties config = new Properties();
            try {
                config.load(new FileReader(configFile));

                String path = modPath.getText().replaceAll("\\\\","/");

                config.setProperty(modName.getText(), path+"/src/main/resources/assets/"+ modID +"/");

                config.store(new FileWriter(configFile),null);

                File file = new File("mod.txt");

                file.createNewFile();

                PrintStream ps = new PrintStream("mod.txt");
                ps.print(modName.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @FXML
    void onCheck(ActionEvent event) {
        File file = new File("mod.txt");
        if(file.exists() && file.length()!=0){
            try {
                String name = new BufferedReader(new FileReader(file)).readLine();
                tip.setText("该软件当前作用于mod: [ "+name+" ]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void toBuildPage(ActionEvent event) {
        buildConfig();
        try {
            Util.loadFXMLWithDefault(PrimaryPage.getStage(), "build.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
