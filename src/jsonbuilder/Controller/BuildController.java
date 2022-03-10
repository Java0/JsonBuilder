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
import java.util.Objects;
import java.util.Properties;

public class BuildController {

    @FXML
    private TextField ChineseName;

    @FXML
    private Button build;

    @FXML
    private Button back;

    @FXML
    private Button cnButton;

    @FXML
    private Button enButton;

    @FXML
    private TextField registerName;

    @FXML
    private TextField type;

    @FXML
    private Label warning;

    private String modName;

    private String resourcesPath;

    private String modID;


    private void buildItemJson() {

        String jsonPath = resourcesPath + "/models/" + type.getText() + "/" + registerName.getText() + ".json";

        if (!new File(jsonPath).exists()) {
            String content = "{\n" +
                    "  \"parent\": \"" + type.getText() + "/generated\",\n" +
                    "  \"textures\": {\n" +
                    "    \"layer0\": \"" + modID + ":" + type.getText() + "/" + registerName.getText() + "\"\n" +
                    "  }\n" +
                    "}";

            try {
                FileWriter fw = new FileWriter(jsonPath);
                fw.write(content.toCharArray());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            warning.setText("注册一个item所需的json皆已存在，不会进行覆盖");
        }


    }

    private void buildBlockJson() throws IOException {

        FileWriter fw;

        String blockStatesPath = resourcesPath + "/blockstates/" + registerName.getText() + ".json";

        String blockModelPath = resourcesPath + "/models/" + type.getText() + "/" + registerName.getText() + ".json";

        String itemModelPath = resourcesPath + "/models/item/" + registerName.getText() + ".json";

        if (!new File(blockModelPath).exists()) {

            String modelContent = "{\n" +
                    "  \"parent\": \"block/cube_all\",\n" +
                    "  \"textures\": {\n" +
                    "    \"all\": \"" + modID + ":" + type.getText() + "/" + registerName.getText() + "\"\n" +
                    "  }\n" +
                    "}";

            fw = new FileWriter(blockModelPath);
            fw.write(modelContent.toCharArray());
            fw.close();

        }

        if (!new File(blockStatesPath).exists()) {
            String blockStatesContent = "{\n" +
                    "    \"variants\": {\n" +
                    "        \"normal\": {\n" +
                    "            \"model\": \"" + modID + ":" + registerName.getText() + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            fw = new FileWriter(blockStatesPath);
            fw.write(blockStatesContent.toCharArray());
            fw.close();
        }

        if (!new File(itemModelPath).exists()) {
            String itemModelContent = "{\n" +
                    "  \"parent\": \"" + modID + ":" + type.getText() + "/" + registerName.getText() + "\"\n" +
                    "}";

            fw = new FileWriter(itemModelPath);
            fw.write(itemModelContent.toCharArray());
            fw.close();
        }

    }

    private void enLangBuilder(){
        String enLangPath = resourcesPath + "/lang/en_us.lang";

        String en;

        String enContent;

        if(registerName.getText().contains("_")){
            String[] temp = registerName.getText().split("_");

            StringBuilder sb = new StringBuilder();

            for (String s : temp) {
                s = s + " ";
                char[] cs = s.toCharArray();
                cs[0]-=32;

                sb.append(String.valueOf(cs));
            }
            en = sb.toString();

            enContent = type.getText()+"."+modID+"."+registerName.getText()+".name="+en;

            if(type.getText().equals("block")){
                enContent = "tile."+modID+"."+registerName.getText()+".name="+en;
            }

        }else {

            char[] cs = registerName.getText().toCharArray();
            cs[0] -= 32;
            en = String.valueOf(cs);

            enContent = type.getText()+"."+modID+"."+registerName.getText()+".name="+en;

            if(type.getText().equals("block")){
                enContent = "tile."+modID+"."+registerName.getText()+".name="+en;
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(enLangPath, true));
            bw.write(enContent.trim().toCharArray());
            bw.newLine();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void cnLangBuilder(){
        String cnLangPath = resourcesPath + "/lang/zh_cn.lang";

        String cnContent = type.getText()+"."+modID+"."+registerName.getText()+".name="+ChineseName.getText();;

        if(type.getText().equals("block")){
            cnContent = "tile."+modID+"."+registerName.getText()+".name="+ChineseName.getText();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(cnLangPath,true));
            bw.write(cnContent.trim().toCharArray());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setResourcesPath() {

        Properties config = new Properties();

        try {
            config.load(new FileInputStream("resources path.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config.getProperty(modName) == null) {
            warning.setText("未设置mod路径，请返回上一页面设置");
        } else {
            resourcesPath = config.getProperty(modName);
        }

    }

    private void init(){

        try {
            modName = Objects.requireNonNull(new BufferedReader(new FileReader("mod.txt")).readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        modID = modName.toLowerCase(Locale.ROOT);

        setResourcesPath();

    }


    @FXML
    void onBuild(ActionEvent event) throws IOException{

        init();

        if (registerName.getLength() == 0 || type.getLength() == 0) {
            warning.setText("请完整输入参数");
        } else {

            switch (type.getText()) {

                case "item":
                    buildItemJson();
                    break;

                case "block":
                    buildBlockJson();
                    break;
            }
        }

    }

    @FXML
    void buildCN(ActionEvent event) {
        init();
        cnLangBuilder();
    }

    @FXML
    void buildEN(ActionEvent event) {
        init();
        enLangBuilder();
    }




    @FXML
    void onBack(ActionEvent event) {
        try {
            Util.loadFXMLWithDefault(PrimaryPage.getStage(), "homepage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
