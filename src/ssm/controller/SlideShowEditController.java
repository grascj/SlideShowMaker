package ssm.controller;

import properties_manager.PropertiesManager;
import static ssm.StartupConstants.DEFAULT_IMAGE_CAPTION;
import static ssm.StartupConstants.DEFAULT_SLIDE_IMAGE;
import static ssm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import ssm.model.SlideShowModel;
import ssm.view.SlideShowMakerView;

/**
 * This controller provides responses for the slideshow edit toolbar,
 * which allows the user to add, remove, and reorder slides.
 * 
 * @author McKilla Gorilla & Chris Grasing
 */
public class SlideShowEditController {
    // APP UI
    private SlideShowMakerView ui;
    
    /**
     * This constructor keeps the UI for later.
     */
    public SlideShowEditController(SlideShowMakerView initUI) {
	ui = initUI;
    }
    
    /**
     * Provides a response for when the user wishes to add a new
     * slide to the slide show.
     */
    public void processAddSlideRequest() {
	SlideShowModel slideShow = ui.getSlideShow();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	slideShow.addSlide(DEFAULT_SLIDE_IMAGE, PATH_SLIDE_SHOW_IMAGES, DEFAULT_IMAGE_CAPTION);
    }
    
    
    /**
     * Provides a response for when the user wishes to move
     * their selection up a slide
     */
    public void processMoveUpRequest() {
        	SlideShowModel slideShow = ui.getSlideShow();
                slideShow.moveSlideUp(ui.getSelectedSlide());
    }
    
    /**
     * Provides a response for when the user wishes to move
     * their selection down a slide
     */
    public void processDownUpRequest() {
        	SlideShowModel slideShow = ui.getSlideShow();
                slideShow.moveSlideDown(ui.getSelectedSlide());
    }
    
    /**
     * Provides a response for when the user wishes to remove
     * their selection from the slide show
     */
    public void processRemoveRequest() {
        	SlideShowModel slideShow = ui.getSlideShow();
                slideShow.removeSlide(ui.getSelectedSlide());
    }
    
}
