package ssm;

/**
 * This class provides setup constants for initializing the application that are
 * NOT language dependent.
 *
 * @author McKilla Gorilla & Chris Grasing
 */
public class StartupConstants {

    // WE'LL LOAD ALL THE UI AND LANGUAGE PROPERTIES FROM FILES,
    // BUT WE'LL NEED THESE VALUES TO START THE PROCESS
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String UI_PROPERTIES_FILE_NAME = "properties_EN.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    public static String PATH_DATA = "./data/";
    public static String PATH_SLIDE_SHOWS = PATH_DATA + "slide_shows/";
    public static String PATH_IMAGES = "./images/";
    public static String PATH_ICONS = PATH_IMAGES + "icons/";
    public static String PATH_SLIDE_SHOW_IMAGES = PATH_IMAGES + "slide_show_images/";
    public static String PATH_CSS = "ssm/style/";
    public static String STYLE_SHEET_UI = PATH_CSS + "SlideShowMakerStyle.css";
    
    //SITE RELATED PATHS AND STUFF
    public static String PATH_SITES = "./sites/";    
    

    // HERE ARE THE LANGUAGE INDEPENDENT GUI ICONS
    public static String ICON_NEW_SLIDE_SHOW = "New.png";
    public static String ICON_LOAD_SLIDE_SHOW = "Load.png";
    public static String ICON_SAVE_SLIDE_SHOW = "Save.png";
    public static String ICON_VIEW_SLIDE_SHOW = "View.png";
    public static String ICON_EXIT = "Exit.png";
    public static String ICON_ADD_SLIDE = "Add.png";
    public static String ICON_REMOVE_SLIDE = "Remove.png";
    public static String ICON_MOVE_UP = "MoveUp.png";
    public static String ICON_MOVE_DOWN = "MoveDown.png";
    public static String ICON_PREVIOUS = "Previous.png";
    public static String ICON_NEXT = "Next.png";
    public static String ICON_NAME_SLIDE_SHOW = "glyphicons-352-book-open.png";

    // WINDOW ICON
    public static String WINDOW_ICON = "murica.png";

    // DEFAULT SLIDE STUFF
    public static String DEFAULT_SLIDE_IMAGE = "DefaultStartSlide.png";
    public static String DEFAULT_IMAGE_CAPTION = "";
    public static int DEFAULT_THUMBNAIL_WIDTH = 200;
    public static int DEFAULT_SLIDE_SHOW_HEIGHT = 500;
    public static String DEFAULT_SLIDE_ERROR_IMAGE = "Red_x.png";

    // CSS STYLE SHEET CLASSES
    public static String CSS_CLASS_VERTICAL_TOOLBAR_BUTTON = "vertical_toolbar_button";
    public static String CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON = "horizontal_toolbar_button";
    public static String CSS_CLASS_SLIDE_SHOW_EDIT_VBOX = "slide_show_edit_vbox";
    public static String CSS_CLASS_SLIDE_EDIT_VIEW = "slide_edit_view";
    public static String CSS_CLASS_SELECTED_SLIDE_EDIT_VIEW = "selected_slide_edit_view";
    public static String CSS_CLASS_SLIDE_SHOW_FILE_FLOWPANE = "slide_show_file_flowplane";
    public static String CSS_CLASS_SLIDE_SHOW_EDITOR_SCROLLPANE = "slide_show_editor_scrollpane";
    public static String CSS_CLASS_SLIDE_SHOW_EDITOR_VBOX = "slide_show_editor_vbox";
    public static String CSS_CLASS_WORKSPACE = "workspace";
    
    //style sheets for other windows
    public static String CSS_CLASS_PROMPT_PANE = "prompt_pane";
    public static String CSS_CLASS_PROMPT_CHILDREN = "prompt_children";
    
    
    // UI LABELS
    public static String LABEL_SLIDE_SHOW_TITLE = "slide_show_title";

    //ERROR STUFF THAT CANNOT POSSIBLY BE LOADED BY THE XML
    public static String EN_ERROR_DATA_TITLE = "DATA ERROR";
    public static String EN_ERROR_DATA_MESSAGE = "There was an issue loading the data files.";
    public static String EN_ERROR_PROPERTIES_TITLE = "PROPERTIES ERROR";
    public static String EN_ERROR_PROPERTIES_MESSAGE = "There was an issue loading the property files.";

    //SAME AS ABOVE BUT IN GERMAN
    public static String GER_ERROR_DATA_TITLE = "Datenfehler";
    public static String GER_ERROR_DATA_MESSAGE = "Es gab ein Problem Laden der Daten-Dateien.";
    public static String GER_ERROR_PROPERTIES_TITLE = "Immobilienfehler";
    public static String GER_ERROR_PROPERTIES_MESSAGE = "es gab ein Problem Laden der Eigenschaftsdateien.";

}
