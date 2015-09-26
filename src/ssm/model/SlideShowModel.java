package ssm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import ssm.view.SlideShowMakerView;

/**
 * This class manages all the data associated with a slideshow.
 * 
 * @author McKilla Gorilla & Chris Grasing
 */
public class SlideShowModel {
    SlideShowMakerView ui;
    String title;
    ObservableList<Slide> slides;
    Slide selectedSlide;
    
    public SlideShowModel(SlideShowMakerView initUI) {
	ui = initUI;
	slides = FXCollections.observableArrayList();
	reset();	
    }

    // ACCESSOR METHODS
    public boolean isSlideSelected() {
	return selectedSlide != null;
    }
    
    public ObservableList<Slide> getSlides() {
	return slides;
    }
    
    public Slide getSelectedSlide() {
	return selectedSlide;
    }

    public String getTitle() { 
	return title; 
    }
    
    public SlideShowMakerView getUI()
    {
        return ui;
    }
    
    // MUTATOR METHODS
    public void setSelectedSlide(Slide initSelectedSlide) {
	selectedSlide = initSelectedSlide;
    }
    
    public void setTitle(String initTitle) { 
	title = initTitle; 
    }

    // SERVICE METHODS
    
    /**
     * Resets the slide show to have no slides and a default title.
     */
    public void reset() {
	slides.clear();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	title = props.getProperty(LanguagePropertyType.DEFAULT_SLIDE_SHOW_TITLE);
	selectedSlide = null;
    }

    /**
     * Adds a slide to the slide show with the parameter settings.
     * @param initImageFileName File name of the slide image to add.
     * @param initImagePath File path for the slide image to add.
     */
    public void addSlide(   String initImageFileName,
			    String initImagePath,
                            String caption) {
	Slide slideToAdd = new Slide(initImageFileName, initImagePath, caption);
	slides.add(slideToAdd);
	selectedSlide = slideToAdd;
	ui.reloadSlideShowPane(this);
    }
    
    /*
    *removes the slide from the list of slides
    */
    public void removeSlide(Slide trash){
        slides.remove(trash);
        ui.removeSelection();
        ui.reloadSlideShowPane(this);
    }
    
    public void moveSlideUp(Slide selected) {
        if(slides.size()>1 && selected!=slides.get(0))
        {
            int loc = slides.indexOf(selected);
            Slide temp = slides.get(loc-1);
            slides.set(loc-1,selected);
            slides.set(loc, temp);
            ui.reloadSlideShowPane(this);

        }

    }
    
    public void moveSlideDown(Slide selected) {
        if(slides.size()>1 && selected!=slides.get(slides.size()-1))
        {
            int loc = slides.indexOf(selected);
            Slide temp = slides.get(loc+1);
            slides.set(loc+1,selected);
            slides.set(loc, temp);
            ui.reloadSlideShowPane(this);
    
        }
    }
    
}