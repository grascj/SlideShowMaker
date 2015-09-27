/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssm.file;

import java.io.File;
import java.io.IOException;
import static ssm.StartupConstants.PATH_SITES;
import ssm.model.SlideShowModel;

/**
 *
 * @author chrisgrasing
 */
public class SiteBuilder {

    SlideShowModel slideShow;
    String pathSite;
    String pathCSS;
    String pathCSSFile;
    String pathJS;
    String pathJSFile;
    String pathImages;
    String pathHTML;

    public SiteBuilder(SlideShowModel slideShow) {
        this.slideShow = slideShow;
        pathSite = PATH_SITES + this.slideShow.getTitle() + "/";
        pathCSS = pathSite + "css/";
        pathCSSFile = pathCSS + "slideshow_style.css";
        pathJS = pathSite + "js/";
        pathJSFile = pathJS + "Slidesshow.js";
        pathImages = pathSite + "img/";
        pathHTML = pathSite + "index.html";
        
        try {
            createDirectories();
        } catch (IOException ex) {
            //@TODO catch this IO error
        }

    }

    private void createDirectories() throws IOException {
        File siteFolder = new File(pathSite);

        //DELETE FILES
        if (siteFolder.exists()) {
            destroy(siteFolder);
        }
        
        boolean siteFolderCreated = siteFolder.mkdirs();
        if(siteFolderCreated)
        {
            File htmlFile = new File(pathHTML);
            htmlFile.createNewFile();
          
            
            //@TODO probably should do something to keep an eye on these as well 
            File cssFolder = new File(pathCSS);
            cssFolder.mkdir();
            File cssFile = new File(pathCSSFile);
            cssFile.createNewFile();
            File jsFolder = new File(pathJS);
            jsFolder.mkdir();
            File jsFile = new File(pathJSFile);
            jsFile.createNewFile();
            File imgFolder = new File(pathImages);
            imgFolder.mkdir();
        } 
    }

    public void destroy(File fileToKill) {
        if (fileToKill.isDirectory() && fileToKill.list().length > 0) {
            for (String a : fileToKill.list()) {
                destroy(new File(fileToKill + "/" + a));
            }
        }
        fileToKill.delete();
    }

    //@TODO ERROR HANDLING?
    public void loadImages() {

    }

    public void generateCSS() {

    }

    public void generateHTML() {

    }

}
