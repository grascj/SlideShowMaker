package ssm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import static ssm.LanguagePropertyType.TITLE_WINDOW;
import static ssm.StartupConstants.EN_ERROR_DATA_MESSAGE;
import static ssm.StartupConstants.EN_ERROR_DATA_TITLE;
import static ssm.StartupConstants.EN_ERROR_PROPERTIES_MESSAGE;
import static ssm.StartupConstants.EN_ERROR_PROPERTIES_TITLE;
import static ssm.StartupConstants.GER_ERROR_DATA_MESSAGE;
import static ssm.StartupConstants.GER_ERROR_DATA_TITLE;
import static ssm.StartupConstants.GER_ERROR_PROPERTIES_MESSAGE;
import static ssm.StartupConstants.GER_ERROR_PROPERTIES_TITLE;
import static ssm.StartupConstants.PATH_DATA;
import static ssm.StartupConstants.PATH_ICONS;
import static ssm.StartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import static ssm.StartupConstants.UI_PROPERTIES_FILE_NAME;
import static ssm.StartupConstants.WINDOW_ICON;
import ssm.error.ErrorHandler;
import ssm.file.SlideShowFileManager;
import ssm.view.SlideShowMakerView;

/**
 * SlideShowMaker is a program for making custom image slideshows. It will allow
 * the user to name their slideshow, select images to use, select captions for
 * the images, and the order of appearance for slides.
 *
 * @author McKilla Gorilla & Chris Grasing
 */
public class SlideShowMaker extends Application {

    // THIS WILL PERFORM SLIDESHOW READING AND WRITING
    SlideShowFileManager fileManager = new SlideShowFileManager();

    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    SlideShowMakerView ui = new SlideShowMakerView(fileManager);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //making a stage to prompt the user for language
        Stage languageStage = new Stage();
        ComboBox language = new ComboBox();
        language.getItems().addAll("English", "German");
        language.setValue("English");
        Button btn = new Button("Select");
        VBox vbox = new VBox();
        vbox.getChildren().addAll(language, btn);
        Scene scene = new Scene(vbox, 200, 200);
        languageStage.setTitle("Select a language");
        languageStage.setScene(scene);
        languageStage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOW_ICON));

        //when the selection is made we can carry on with our lives
        btn.setOnAction((ActionEvent e) -> {
            languageStage.hide();
            // LOAD APP SETTINGS INTO THE GUI AND START IT UP
            if (language.getValue().equals("German")) {
                UI_PROPERTIES_FILE_NAME = "properties_GER.xml";
            } else if (language.getValue().equals("English")) {
                UI_PROPERTIES_FILE_NAME = "properties_EN.xml";
            }
        });
            languageStage.showAndWait();


            boolean success = loadProperties();
            if (success) {
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                String appTitle = props.getProperty(TITLE_WINDOW);

                // NOW START THE UI IN EVENT HANDLING MODE
                ui.startUI(primaryStage, appTitle);
            } // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
            else {
                // LET THE ERROR HANDLER PROVIDE THE RESPONSE
                ErrorHandler errorHandler = ui.getErrorHandler();

                if (UI_PROPERTIES_FILE_NAME.equals("properties_EN.xml")) {
                    errorHandler.processPreXMLError(EN_ERROR_DATA_TITLE, EN_ERROR_DATA_MESSAGE);
                } else if (UI_PROPERTIES_FILE_NAME.equals("properties_GER.xml")) {
                    errorHandler.processPreXMLError(GER_ERROR_DATA_TITLE, GER_ERROR_DATA_MESSAGE);
                }

            }
        

    }

    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties() {
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
        } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            ErrorHandler eH = ui.getErrorHandler();

            if (UI_PROPERTIES_FILE_NAME.equals("properties_EN.xml")) {
                eH.processPreXMLError(EN_ERROR_PROPERTIES_TITLE, EN_ERROR_PROPERTIES_MESSAGE);
            } else if (UI_PROPERTIES_FILE_NAME.equals("properties_GER.xml")) {
                eH.processPreXMLError(GER_ERROR_PROPERTIES_TITLE, GER_ERROR_PROPERTIES_MESSAGE);
            }

            return false;
        }
    }

    /**
     * This is where the application starts execution. We'll load the
     * application properties and then use them to build our user interface and
     * start the window in event handling mode. Once in that mode, all code
     * execution will happen in response to user requests.
     *
     * @param args This application does not use any command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
