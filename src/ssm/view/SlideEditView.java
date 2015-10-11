package ssm.view;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import ssm.LanguagePropertyType;
import static ssm.LanguagePropertyType.ERROR_IMAGE_RETRIEVAL_DIALOGUE;
import static ssm.LanguagePropertyType.ERROR_IMAGE_RETRIEVAL_TITLE;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_TEXT;
import static ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static ssm.StartupConstants.DEFAULT_SLIDE_ERROR_IMAGE;
import static ssm.StartupConstants.DEFAULT_SLIDE_IMAGE;
import static ssm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import static ssm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import ssm.controller.FileController;
import ssm.controller.ImageSelectionController;
import ssm.error.ErrorHandler;
import ssm.model.Slide;
import static ssm.file.SlideShowFileManager.SLASH;

/**
 * This UI component has the controls for editing a single slide
 * in a slide show, including controls for selected the slide image
 * and changing its caption.
 * 
 * @author McKilla Gorilla & Chris Grasing
 */
public class SlideEditView extends HBox {
    // SLIDE THIS COMPONENT EDITS
    Slide slide;
    
    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;
    
    // CONTROLS FOR EDITING THE CAPTION
    VBox captionVBox;
    Label captionLabel;
    TextField captionTextField;
    
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;
    
    FileController fc;
    
    public SlideShowMakerView getUI()
    {
        return fc.getUI();
    }
    /**
     * THis constructor initializes the full UI for this component, using
     * the initSlide data for initializing values./
     * 
     * @param initSlide The slide to be edited by this component.
     */
    public SlideEditView(Slide initSlide, FileController fileController) {
        fc = fileController;
	// FIRST SELECT THE CSS STYLE CLASS FOR THIS CONTAINER
	
	// KEEP THE SLIDE FOR LATER
	slide = initSlide;
	
	// MAKE SURE WE ARE DISPLAYING THE PROPER IMAGE
	imageSelectionView = new ImageView();
	updateSlideImage();

	// SETUP THE CAPTION CONTROLS
	captionVBox = new VBox();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	captionLabel = new Label(props.getProperty(LanguagePropertyType.LABEL_CAPTION));
	captionTextField = new TextField();
        captionLabel.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_TEXT);
        captionTextField.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_TEXT);
	captionVBox.getChildren().add(captionLabel);
	captionVBox.getChildren().add(captionTextField);
        captionTextField.setText(slide.getImageCaption());

	// LAY EVERYTHING OUT INSIDE THIS COMPONENT
	getChildren().add(imageSelectionView);
	getChildren().add(captionVBox);

	// SETUP THE EVENT HANDLERS
	imageController = new ImageSelectionController();
	imageSelectionView.setOnMousePressed(e -> {
	    imageController.processSelectImage(slide, this, fc);
            fc.markAsEdited();
	});
        
        captionTextField.setOnKeyReleased(e -> {
            updateCaption();
            fc.markAsEdited();
        });
        
    }
    
    /**
     * 
     */
    public void updateCaption() {
        slide.setCaption(captionTextField.getText());        
    }
    
    
    
    /**
     * This function gets the image for the slide and uses it to
     * update the image displayed.
     */
    public void updateSlideImage() {
	String imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
	File file = new File(imagePath);
	try {
	    // GET AND SET THE IMAGE
	    URL fileURL = file.toURI().toURL();

	    Image slideImage = new Image(fileURL.toExternalForm());
            imageSelectionView.setImage(slideImage);
            if(slideImage.isError())
                throw slideImage.getException();
	    
	    // AND RESIZE IT
	    double scaledWidth = DEFAULT_THUMBNAIL_WIDTH;
	    double perc = scaledWidth / slideImage.getWidth();
	    double scaledHeight = slideImage.getHeight() * perc;
	    imageSelectionView.setFitWidth(scaledWidth);
	    imageSelectionView.setFitHeight(scaledHeight);
	} catch (Exception e) {
            ErrorHandler eH = fc.getUI().getErrorHandler();
            eH.processError(ERROR_IMAGE_RETRIEVAL_TITLE, ERROR_IMAGE_RETRIEVAL_DIALOGUE);
            slide.setImagePath(PATH_SLIDE_SHOW_IMAGES);
            slide.setImageFileName(DEFAULT_SLIDE_ERROR_IMAGE);
            updateSlideImage();
        }
    }    
}