package ssm.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.LanguagePropertyType.NAME_LABEL_TEXT;
import static ssm.LanguagePropertyType.TOOLTIP_ADD_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_EXIT;
import static ssm.LanguagePropertyType.TOOLTIP_LOAD_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_MOVE_DOWN;
import static ssm.LanguagePropertyType.TOOLTIP_MOVE_UP;
import static ssm.LanguagePropertyType.TOOLTIP_NEW_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_REMOVE_SLIDE;
import static ssm.LanguagePropertyType.TOOLTIP_SAVE_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_VIEW_SLIDE_SHOW;
import static ssm.LanguagePropertyType.TOOLTIP_NAME_SLIDE_SHOW;
import static ssm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON;
import static ssm.StartupConstants.CSS_CLASS_SELECTED_SLIDE_EDIT_VIEW;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_SHOW_EDITOR_SCROLLPANE;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_SHOW_EDITOR_VBOX;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_SHOW_EDIT_VBOX;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_SHOW_FILE_FLOWPANE;
import static ssm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_BUTTON;
import static ssm.StartupConstants.CSS_CLASS_WORKSPACE;
import static ssm.StartupConstants.ICON_ADD_SLIDE;
import static ssm.StartupConstants.ICON_EXIT;
import static ssm.StartupConstants.ICON_LOAD_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_MOVE_DOWN;
import static ssm.StartupConstants.ICON_MOVE_UP;
import static ssm.StartupConstants.ICON_NAME_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_NEW_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_REMOVE_SLIDE;
import static ssm.StartupConstants.ICON_SAVE_SLIDE_SHOW;
import static ssm.StartupConstants.ICON_VIEW_SLIDE_SHOW;
import static ssm.StartupConstants.PATH_ICONS;
import static ssm.StartupConstants.STYLE_SHEET_UI;
import static ssm.StartupConstants.WINDOW_ICON;
import ssm.controller.FileController;
import ssm.controller.SlideShowEditController;
import ssm.model.Slide;
import ssm.model.SlideShowModel;
import ssm.error.ErrorHandler;
import ssm.file.SlideShowFileManager;

/**
 * This class provides the User Interface for this application, providing
 * controls and the entry points for creating, loading, saving, editing, and
 * viewing slide shows.
 *
 * @author McKilla Gorilla & Chris Grasing
 */
public class SlideShowMakerView {

    // THIS IS THE MAIN APPLICATION UI WINDOW AND ITS SCENE GRAPH
    Stage primaryStage;
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane ssmPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newSlideShowButton;
    Button loadSlideShowButton;
    Button saveSlideShowButton;
    Button viewSlideShowButton;
    Button exitButton;
    TextField titleField;

    // WORKSPACE
    BorderPane workspace;

    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    VBox slideEditToolbar;
    Button addSlideButton;
    Button moveUpButton;
    Button moveDownButton;
    Button removeButton;

    // AND THIS WILL GO IN THE CENTER
    ScrollPane slidesEditorScrollPane;
    VBox slidesEditorPane;

    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    SlideShowModel slideShow;

    // THIS IS FOR SAVING AND LOADING SLIDE SHOWS
    SlideShowFileManager fileManager;

    // THIS CLASS WILL HANDLE ALL ERRORS FOR THIS PROGRAM
    private ErrorHandler errorHandler;

    // THIS CONTROLLER WILL ROUTE THE PROPER RESPONSES
    // ASSOCIATED WITH THE FILE TOOLBAR
    private FileController fileController;

    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private SlideShowEditController editController;

    //SELECTED SLIDE
    SlideEditView selection;

    /**
     * Default constructor, it initializes the GUI for use, but does not yet
     * load all the language-dependent controls, that needs to be done via the
     * startUI method after the user has selected a language.
     */
    public SlideShowMakerView(SlideShowFileManager initFileManager) {
        // FIRST HOLD ONTO THE FILE MANAGER
        fileManager = initFileManager;

        // MAKE THE DATA MANAGING MODEL
        slideShow = new SlideShowModel(this);

        titleField = new TextField();

        // WE'LL USE THIS ERROR HANDLER WHEN SOMETHING GOES WRONG
        errorHandler = new ErrorHandler(this);
    }

    // ACCESSOR METHODS
    public SlideShowModel getSlideShow() {
        return slideShow;
    }

    public Stage getWindow() {
        return primaryStage;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public Slide getSelectedSlide() {
        return selection.slide;
    }

    //MUTATOR METHODS
    public void removeSelection() {
        selection = null;
    }

    /**
     * Initializes the UI controls and gets it rolling.
     *
     * @param initPrimaryStage The window for this application.
     *
     * @param windowTitle The title for this window.
     */
    public void startUI(Stage initPrimaryStage, String windowTitle) {
        // THE TOOLBAR ALONG THE NORTH
        initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
        // TO THE WINDOW YET
        initWorkspace();

        // NOW SETUP THE EVENT HANDLERS
        initEventHandlers();

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        // KEEP THE WINDOW FOR LATER
        primaryStage = initPrimaryStage;
        initWindow(windowTitle);
    }

    // UI SETUP HELPER METHODS
    private void initWorkspace() {
        // FIRST THE WORKSPACE ITSELF, WHICH WILL CONTAIN TWO REGIONS
        workspace = new BorderPane();
        workspace.getStyleClass().add(CSS_CLASS_WORKSPACE);

        // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
        slideEditToolbar = new VBox();
        slideEditToolbar.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDIT_VBOX);
        addSlideButton = this.initChildButton(slideEditToolbar, ICON_ADD_SLIDE, TOOLTIP_ADD_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        moveUpButton = this.initChildButton(slideEditToolbar, ICON_MOVE_UP, TOOLTIP_MOVE_UP, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        moveDownButton = this.initChildButton(slideEditToolbar, ICON_MOVE_DOWN, TOOLTIP_MOVE_DOWN, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);
        removeButton = this.initChildButton(slideEditToolbar, ICON_REMOVE_SLIDE, TOOLTIP_REMOVE_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON, true);

        // AND THIS WILL GO IN THE CENTER
        slidesEditorPane = new VBox();
        slidesEditorPane.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDITOR_VBOX);
        slidesEditorScrollPane = new ScrollPane(slidesEditorPane);
        slidesEditorScrollPane.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDITOR_SCROLLPANE);

        
        // NOW PUT THESE TWO IN THE WORKSPACE
        workspace.setLeft(slideEditToolbar);
        workspace.setCenter(slidesEditorScrollPane);
    }

    private void initEventHandlers() {

        titleField.setOnKeyReleased(e -> {
            slideShow.setTitle(titleField.getText());
            fileController.markAsEdited();
        });

        // FIRST THE FILE CONTROLS
        fileController = new FileController(this, fileManager);
        newSlideShowButton.setOnAction(e -> {
            fileController.handleNewSlideShowRequest();
        });
        loadSlideShowButton.setOnAction(e -> {
            fileController.handleLoadSlideShowRequest();
        });
        saveSlideShowButton.setOnAction(e -> {
            fileController.handleSaveSlideShowRequest();
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest();
        });
        viewSlideShowButton.setOnAction(e -> {
            fileController.handleViewRequest();
        });
        /*        nameSlideShowButton.setOnAction(e -> {
         fileController.handleNameSlideShowRequest();
         }); */

        // THEN THE SLIDE SHOW EDIT CONTROLS
        editController = new SlideShowEditController(this);
        addSlideButton.setOnAction(e -> {
            if (selection != null) {
                selection.setStyle("-fx-border-color: none;");
            }
            editController.processAddSlideRequest();
            fileController.markAsEdited();
        });
        moveUpButton.setOnAction(e -> {
            editController.processMoveUpRequest();
            fileController.markAsEdited();
        });
        moveDownButton.setOnAction(e -> {
            editController.processDownUpRequest();
            fileController.markAsEdited();
        });
        removeButton.setOnAction(e -> {
            editController.processRemoveRequest();
            fileController.markAsEdited();
        });

        /*  for (Object a : slidesEditorPane.getChildren()) {
         (SlideEditView) a.getClass();
         }*/
    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolbarPane = new FlowPane();
        fileToolbarPane.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_FILE_FLOWPANE);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newSlideShowButton = initChildButton(fileToolbarPane, ICON_NEW_SLIDE_SHOW, TOOLTIP_NEW_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        loadSlideShowButton = initChildButton(fileToolbarPane, ICON_LOAD_SLIDE_SHOW, TOOLTIP_LOAD_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        saveSlideShowButton = initChildButton(fileToolbarPane, ICON_SAVE_SLIDE_SHOW, TOOLTIP_SAVE_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
        viewSlideShowButton = initChildButton(fileToolbarPane, ICON_VIEW_SLIDE_SHOW, TOOLTIP_VIEW_SLIDE_SHOW, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, true);
        exitButton = initChildButton(fileToolbarPane, ICON_EXIT, TOOLTIP_EXIT, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
    }

    private void initWindow(String windowTitle) {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(windowTitle);

        //SET WINDOW ICON
        primaryStage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOW_ICON));

        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // SETUP THE UI, NOTE WE'LL ADD THE WORKSPACE LATER
        ssmPane = new BorderPane();
        ssmPane.setTop(fileToolbarPane);
        primaryScene = new Scene(ssmPane);

        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
        // WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
        primaryScene.getStylesheets().add(STYLE_SHEET_UI);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    /**
     * This helps initialize buttons in a toolbar, constructing a custom button
     * with a customly provided icon and tooltip, adding it to the provided
     * toolbar pane, and then returning it.
     */
    public Button initChildButton(
            Pane toolbar,
            String iconFileName,
            LanguagePropertyType tooltip,
            String cssClass,
            boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_ICONS + iconFileName;
        Image buttonImage = new Image(imagePath);
        Button button = new Button();
        button.getStyleClass().add(cssClass);
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
        return button;
    }

    /**
     * Updates the enabled/disabled status of all toolbar buttons.
     *
     * @param saved
     */
    public void updateToolbarControls(boolean saved) {

        // FIRST MAKE SURE THE WORKSPACE IS THERE
        ssmPane.setCenter(workspace);

        // NEXT ENABLE/DISABLE BUTTONS AS NEEDED IN THE FILE TOOLBAR
        saveSlideShowButton.setDisable(saved);

        //disable the viewer when theres no slides
        if (slideShow.getSlides().size() == 0) {
            saveSlideShowButton.setDisable(true);
            viewSlideShowButton.setDisable(true);
        } else {

            //saveSlideShowButton.setDisable(false);
            viewSlideShowButton.setDisable(false);
        }

        // AND THE SLIDESHOW EDIT TOOLBAR
        addSlideButton.setDisable(false);
        //disable the editor buttons when theres no selection
        if (selection != null) {
            removeButton.setDisable(false);
            if (slideShow.getSlides().size() < 1) {
                moveUpButton.setDisable(true);
                moveDownButton.setDisable(true);
            } else {

                if (slideShow.getSlides().indexOf(getSelectedSlide()) == 0) {
                    moveUpButton.setDisable(true);
                    if (slideShow.getSlides().size() == 1) {
                        moveDownButton.setDisable(true);
                    } else {
                        moveDownButton.setDisable(false);
                    }
                } else if (slideShow.getSlides().indexOf(getSelectedSlide()) == slideShow.getSlides().size() - 1) {
                    moveUpButton.setDisable(false);
                    moveDownButton.setDisable(true);
                } else {
                    moveUpButton.setDisable(false);
                    moveDownButton.setDisable(false);
                }

            }
        } else {
            removeButton.setDisable(true);
            moveUpButton.setDisable(true);
            moveDownButton.setDisable(true);

        }
    }

    /**
     * Uses the slide show data to reload all the components for slide editing.
     *
     * @param slideShowToLoad SLide show being reloaded.
     */
    public void reloadSlideShowPane(SlideShowModel slideShowToLoad) {
        slidesEditorPane.getChildren().clear();
        titleField.setText(slideShow.getTitle());
        slidesEditorPane.getChildren().add(titleField);
        for (Slide slide : slideShowToLoad.getSlides()) {
            SlideEditView slideEditor = new SlideEditView(slide, fileController);

            slidesEditorPane.getChildren().add(slideEditor);
            if (selection != null && slide == getSelectedSlide()) {
                selection = slideEditor;
                slideEditor.getStyleClass().add(CSS_CLASS_SELECTED_SLIDE_EDIT_VIEW);
                updateToolbarControls(fileController.isSaved());
            } else {
                slideEditor.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
            }

            slideEditor.setOnMouseClicked(e -> {
                selection = slideEditor;
                reloadSlideShowPane(slideShow);
            });
        }
    }

}
