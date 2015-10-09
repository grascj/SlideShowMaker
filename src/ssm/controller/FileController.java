package ssm.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static javafx.scene.input.DataFormat.HTML;
import static javafx.scene.input.DataFormat.URL;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static ssm.LanguagePropertyType.CANCEL_BUTTON_TEXT;
import static ssm.LanguagePropertyType.ERROR_LOADING_JSON_DIALOGUE;
import static ssm.LanguagePropertyType.ERROR_LOADING_JSON_TITLE;
import static ssm.LanguagePropertyType.ERROR_SAVE_SLIDESHOW_DIALOGUE;
import static ssm.LanguagePropertyType.ERROR_SAVE_SLIDESHOW_TITLE;
import static ssm.LanguagePropertyType.NO_BUTTON_TEXT;
import static ssm.LanguagePropertyType.SAVE_LABEL_TEXT;
import static ssm.LanguagePropertyType.YES_BUTTON_TEXT;
import static ssm.StartupConstants.CSS_CLASS_PROMPT_CHILDREN;
import static ssm.StartupConstants.CSS_CLASS_PROMPT_PANE;
import ssm.model.SlideShowModel;
import ssm.error.ErrorHandler;
import ssm.file.SlideShowFileManager;
import ssm.view.SlideShowMakerView;
import static ssm.StartupConstants.PATH_SLIDE_SHOWS;
import static ssm.StartupConstants.STYLE_SHEET_UI;
import ssm.file.SiteBuilder;

/**
 * This class serves as the controller for all file toolbar operations, driving
 * the loading and saving of slide shows, among other things.
 *
 * @author McKilla Gorilla & Chris Grasing
 */
public class FileController {

    // WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
    private boolean saved;

    // THE APP UI
    private SlideShowMakerView ui;

    // THIS GUY KNOWS HOW TO READ AND WRITE SLIDE SHOW DATA
    private SlideShowFileManager slideShowIO;

    private SlideShowModel slideShow;

    boolean flag;

    /**
     * This default constructor starts the program without a slide show file
     * being edited.
     *
     * @param initSlideShowIO The object that will be reading and writing slide
     * show data.
     */
    public FileController(SlideShowMakerView initUI, SlideShowFileManager initSlideShowIO) {
        // NOTHING YET
        saved = true;
        ui = initUI;
        slideShowIO = initSlideShowIO;
    }

    public SlideShowMakerView getUI() {
        return ui;
    }
    


    public void markAsEdited() {
        saved = false;
        ui.updateToolbarControls(saved);
    }

    /**
     * This method starts the process of editing a new slide show. If a pose is
     * already being edited, it will prompt the user to save it first.
     */
    public void handleNewSlideShowRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToMakeNew = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToMakeNew = promptToSave();
            }

            // IF THE USER REALLY WANTS TO MAKE A NEW COURSE
            if (continueToMakeNew) {
                // RESET THE DATA, WHICH SHOULD TRIGGER A RESET OF THE UI
                SlideShowModel slideShow = ui.getSlideShow();
                slideShow.reset();
                saved = false;

                // REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
                // THE APPROPRIATE CONTROLS
                ui.updateToolbarControls(saved);
                ui.reloadSlideShowPane(slideShow);

            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(ERROR_SAVE_SLIDESHOW_TITLE, ERROR_SAVE_SLIDESHOW_DIALOGUE);
        }
    }

    /**
     * This method lets the user open a slideshow saved to a file. It will also
     * make sure data for the current slideshow is not lost.
     */
    public void handleLoadSlideShowRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A POSE
            if (continueToOpen) {
                // GO AHEAD AND PROCEED MAKING A NEW POSE
                promptToOpen();
            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(ERROR_SAVE_SLIDESHOW_TITLE, ERROR_SAVE_SLIDESHOW_DIALOGUE);
        }
    }

    /**
     * This method will save the current slideshow to a file. Note that we
     * already know the name of the file, so we won't need to prompt the user.
     */
    public boolean handleSaveSlideShowRequest() {
        try {
            // GET THE SLIDE SHOW TO SAVE
            SlideShowModel slideShowToSave = ui.getSlideShow();

            // SAVE IT TO A FILE
            slideShowIO.saveSlideShow(slideShowToSave);

            // MARK IT AS SAVED
            saved = true;

            // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
            // THE APPROPRIATE CONTROLS
            ui.updateToolbarControls(saved);
            return true;
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(ERROR_SAVE_SLIDESHOW_TITLE, ERROR_SAVE_SLIDESHOW_DIALOGUE);
            return false;
        }
    }

    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     */
    public void handleExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            } //NEED TO FIX THIS 

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
            ErrorHandler eH = ui.getErrorHandler();
            eH.processError(ERROR_SAVE_SLIDESHOW_TITLE, ERROR_SAVE_SLIDESHOW_DIALOGUE);

        }
    }

    public void handleViewRequest() {
        SiteBuilder sb = new SiteBuilder(ui.getSlideShow());

        flag = true;

        WebView site = new WebView();
        
        site.getEngine().load("file://" + new File(sb.getURL()).getAbsolutePath());
        //
        site.getEngine().getLoadWorker().stateProperty().addListener(e -> {
            if(site.getEngine().getLoadWorker().getState() == State.SUCCEEDED && flag == true)
            {
                flag = false;
                site.getEngine().reload();
            }
        });
        //

        Screen screen = Screen.getPrimary();

        Rectangle2D bounds = screen.getVisualBounds();

        //site.getEngine().load("file://" + new File(sb.getURL()).getAbsolutePath());

        Stage webStage = new Stage();

        webStage.setX(bounds.getMinX());
        webStage.setY(bounds.getMinY());
        webStage.setWidth(bounds.getWidth());
        webStage.setHeight(bounds.getHeight());

        Scene webScene = new Scene(site);
        webStage.setScene(webScene);
        webStage.show();


    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating a new
     * pose, or opening another pose, or exiting. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is retuned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
        slideShow = ui.getSlideShow();
        if (slideShow != null && slideShow.getSlides().size() > 0) {

            PropertiesManager props = PropertiesManager.getPropertiesManager();
            Stage saveStage = new Stage();
            VBox vbox = new VBox();
            Label saveLabel = new Label();
            saveLabel.setText(props.getProperty(SAVE_LABEL_TEXT));
            Button yesBtn = new Button();
            yesBtn.setText(props.getProperty(YES_BUTTON_TEXT));
            Button noBtn = new Button();
            noBtn.setText(props.getProperty(NO_BUTTON_TEXT));

            vbox.getStyleClass().add(CSS_CLASS_PROMPT_PANE);
            yesBtn.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);
            noBtn.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);
            saveLabel.getStyleClass().add(CSS_CLASS_PROMPT_CHILDREN);

            vbox.getChildren().addAll(saveLabel, yesBtn, noBtn);
            Scene saveScene = new Scene(vbox, 200, 200);
            saveScene.getStylesheets().add(STYLE_SHEET_UI);
            saveStage.setScene(saveScene);
            saveStage.setAlwaysOnTop(true);

            yesBtn.setOnAction(e -> {
                try {
                    saveStage.close();
                    slideShowIO.saveSlideShow(slideShow);
                } catch (IOException ex) {
                    ErrorHandler eH = ui.getErrorHandler();
                    eH.processError(ERROR_SAVE_SLIDESHOW_TITLE, ERROR_SAVE_SLIDESHOW_DIALOGUE);
                }
            });
            noBtn.setOnAction(e -> {
                saveStage.close();
            });

            saveStage.showAndWait();
        }
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
        // AND NOW ASK THE USER FOR THE COURSE TO OPEN
        FileChooser slideShowFileChooser = new FileChooser();
        slideShowFileChooser.setInitialDirectory(new File(PATH_SLIDE_SHOWS));
        File selectedFile = slideShowFileChooser.showOpenDialog(ui.getWindow());

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {
                SlideShowModel slideShowToLoad = ui.getSlideShow();
                slideShowIO.loadSlideShow(slideShowToLoad, selectedFile.getAbsolutePath());
                // ui.reloadSlideShowPane(slideShowToLoad);
                saved = true;
                ui.updateToolbarControls(saved);
            } catch (Exception e) {
                ErrorHandler eH = ui.getErrorHandler();
                eH.processError(ERROR_LOADING_JSON_TITLE, ERROR_LOADING_JSON_DIALOGUE);
            }
        }
    }

    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the pose is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current pose has been saved
     * since it was last editing. If the current file matches the pose data,
     * we'll return true, otherwise false.
     *
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }

}
