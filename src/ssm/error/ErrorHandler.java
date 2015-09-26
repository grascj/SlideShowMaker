package ssm.error;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.StartupConstants.CSS_CLASS_PROMPT_CHILDREN;
import static ssm.StartupConstants.CSS_CLASS_PROMPT_PANE;
import static ssm.StartupConstants.STYLE_SHEET_UI;
import ssm.view.SlideShowMakerView;

/**
 * This class provides error messages to the user when the occur. Note that
 * error messages should be retrieved from language-dependent XML files and
 * should have custom messages that are different depending on the type of error
 * so as to be informative concerning what went wrong.
 *
 * @author McKilla Gorilla & Chris Grasing
 */
public class ErrorHandler {

    LanguagePropertyType errorType;

    // APP UI
    private SlideShowMakerView ui;

    // KEEP THE APP UI FOR LATER
    public ErrorHandler(SlideShowMakerView initUI) {
        ui = initUI;
    }

    /**
     * This method provides all error feedback. It gets the feedback text, which
     * changes depending on the type of error, and presents it to the user in a
     * dialog box.
     *
     * @param errorType Identifies the type of error that happened, which allows
     * us to get and display different text for different errors.
     */
    public void processError(LanguagePropertyType errorDialogTitle, LanguagePropertyType errorDialogMessage) {
        //need a save error of some sort
        errorType = errorDialogTitle;

        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        Stage errorStage = new Stage();
        errorStage.setTitle(props.getProperty(errorDialogTitle).toString());
        VBox vbox = new VBox();

        Label errorDialog = new Label(props.getProperty(errorDialogMessage).toString());
        Button okBtn = new Button();
        okBtn.setText("OK"); //same in english and german
        
        vbox.getStyleClass().add(CSS_CLASS_PROMPT_PANE);
        okBtn.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);   
        errorDialog.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);        
        
        vbox.getChildren().addAll(errorDialog, okBtn);
        Scene errorScene = new Scene(vbox, 600, 50);
        errorScene.getStylesheets().add(STYLE_SHEET_UI);
        errorStage.setScene(errorScene);
        errorStage.setAlwaysOnTop(true);

        okBtn.setOnAction(e -> {
            errorStage.hide();
        });
        errorStage.showAndWait();

    }

    public void processPreXMLError(String languageDependentErrorTitleConstant, String languageDependentErrorDialogueConstant) {
        Stage errorStage = new Stage();
        errorStage.setTitle(languageDependentErrorTitleConstant);
        VBox vbox = new VBox();

        Label errorDialog = new Label(languageDependentErrorDialogueConstant);
        Button okBtn = new Button();
        okBtn.setText("OK"); //same in english and german

        vbox.getStyleClass().add(CSS_CLASS_PROMPT_PANE);
        okBtn.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);
        errorDialog.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);
        
        vbox.getChildren().addAll(errorDialog, okBtn);
        Scene errorScene = new Scene(vbox, 600, 50);
        errorScene.getStylesheets().add(STYLE_SHEET_UI);
        errorStage.setScene(errorScene);
        errorStage.setAlwaysOnTop(true);

        okBtn.setOnAction(e -> {
            //these errors are fatal, only god can save us now
            System.exit(0);
        });

        errorStage.show();

    }
}
