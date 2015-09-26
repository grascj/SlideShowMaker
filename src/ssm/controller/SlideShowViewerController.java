/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssm.controller;

import java.io.File;
import java.net.URL;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static ssm.LanguagePropertyType.ERROR_IMAGE_RETRIEVAL_DIALOGUE;
import static ssm.LanguagePropertyType.ERROR_IMAGE_RETRIEVAL_TITLE;
import static ssm.LanguagePropertyType.NEXT_BUTTON_TEXT;
import static ssm.LanguagePropertyType.PREVIOUS_BUTTON_TEXT;
import static ssm.LanguagePropertyType.TOOLTIP_NEXT_BUTTON;
import static ssm.LanguagePropertyType.TOOLTIP_PREVIOUS_BUTTON;
import static ssm.StartupConstants.DEFAULT_SLIDE_ERROR_IMAGE;
import static ssm.StartupConstants.PATH_ICONS;
import static ssm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import static ssm.StartupConstants.WINDOW_ICON;
import ssm.error.ErrorHandler;
import static ssm.file.SlideShowFileManager.SLASH;
import ssm.model.Slide;
import ssm.model.SlideShowModel;

/**
 *
 * @author chrisgrasing
 */
public class SlideShowViewerController {

    Stage viewStage;
    ImageView slideImager;
    Button previousBtn;
    Button nextBtn;
    BorderPane mainPane;
    BorderPane bottom;
    SlideShowModel slideShow;   
    Label captionLabel;
    Label titleLabel;

    int slideNumber = 0;
    int NUM_SLIDES = 0;
    
    SlideShowViewerController(SlideShowModel slideShow) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        this.slideShow = slideShow;
        NUM_SLIDES = slideShow.getSlides().size();
        viewStage = new Stage();
        slideImager = new ImageView();
        captionLabel = new Label();
        titleLabel = new Label();
        titleLabel.setText(slideShow.getTitle());
        previousBtn = new Button();
        previousBtn.setText(props.getProperty(PREVIOUS_BUTTON_TEXT.toString()));
        previousBtn.setTooltip(new Tooltip(props.getProperty(TOOLTIP_PREVIOUS_BUTTON.toString())));
        nextBtn = new Button();
        nextBtn.setText(props.getProperty(NEXT_BUTTON_TEXT.toString()));
        nextBtn.setTooltip(new Tooltip(props.getProperty(TOOLTIP_NEXT_BUTTON.toString())));
        mainPane = new BorderPane();
        bottom = new BorderPane();
        mainPane.setBottom(bottom);
        viewStage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOW_ICON));


        //SETTING UP THE BUTTONS AND CAPTION
        bottom.setTop(captionLabel);
        bottom.setLeft(previousBtn);
        bottom.setRight(nextBtn);
        mainPane.setTop(titleLabel);

        //SETTING UP THE IMAGE VIEWER
        mainPane.setCenter(slideImager);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        viewStage.setX(bounds.getMinX());
        viewStage.setY(bounds.getMinY());
        viewStage.setWidth(bounds.getWidth());
        viewStage.setHeight(bounds.getHeight());

        Scene scene = new Scene(mainPane);
        viewStage.setTitle(slideShow.getTitle());
        viewStage.setScene(scene);
        viewStage.show();
        displaySlide(slideNumber);
        
        //SET UP THE BUTTON LISTENERS
        nextBtn.setOnAction(e -> {
            next();
        });
        previousBtn.setOnAction(e ->{
            previous();
        });
        
        
    }

    public void displaySlide(int index) {
        Slide current = slideShow.getSlides().get(index);
        String imagePath = current.getImagePath() + SLASH + current.getImageFileName();
        File file = new File(imagePath);
       try {
            // GET AND SET THE IMAGE
            URL fileURL = file.toURI().toURL();
            Image slideImage = new Image(fileURL.toExternalForm());
            
            slideImager.setImage(slideImage);
            if(slideImage.isError())
                throw slideImage.getException();
            
            slideImager.setFitWidth(800);
            slideImager.setFitHeight(600);
            
            captionLabel.setText(current.getImageCaption());
            
       } catch (Exception e) {
            ErrorHandler eH = slideShow.getUI().getErrorHandler();
            eH.processError(ERROR_IMAGE_RETRIEVAL_TITLE, ERROR_IMAGE_RETRIEVAL_DIALOGUE);
            current.setImagePath(PATH_SLIDE_SHOW_IMAGES);
            current.setImageFileName(DEFAULT_SLIDE_ERROR_IMAGE);
            displaySlide(index);
            slideShow.getUI().reloadSlideShowPane(slideShow);
       }
    }

    public void next()
    {
        slideNumber = (slideNumber+1)%NUM_SLIDES;
        displaySlide(slideNumber);
    }
    
    public void previous()
    {
        slideNumber = (slideNumber-1)%NUM_SLIDES;
        if(slideNumber < 0)
            slideNumber=NUM_SLIDES-1;
        displaySlide(slideNumber);
    }
    
    
    
    
    
    
}
