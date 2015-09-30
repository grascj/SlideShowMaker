/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssm.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import static ssm.StartupConstants.PATH_SITES;
import ssm.model.Slide;
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

    /*PATHS TO TEMPLATE STUFF FOR WEBSITE*/
    public static String PATH_TEMPLATE_FOLDER = "./websiteTemplate/";
    public static String PATH_TEMPLATE_CSS = PATH_TEMPLATE_FOLDER + "css/slideshow_style.css";

    //this will be put into the javascript file AFTER the array of slides is generated
    public static String JS_OBJECTS = "\n"
            + "slide = function(initCaption,initImage){\n"
            + "this.caption = initCaption;\nthis.image = initImage;\n"
            + "};\n\n"
            + "var index = 0;\n"
            + "var slides = [];\n\n";

    public static String JS_FUNCTIONS = "\n"
            + "function nextButton(){\n"
            + "\n"
            + "}\n\n"
            + ""
            + "function previousButton(){\n"
            + "\n"
            + "}\n\n"
            + "\n"
            + "function playSlideShow(){\n"
            + "\n"
            + "}\n\n";

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
            copyTemplate();
            generateJavascript();
        } catch (IOException ex) {
            //@TODO catch this IO error
        }

    }

    private void copyTemplate() throws IOException {
        File siteFolder = new File(pathSite);

        if (siteFolder.exists()) {
            destroy(siteFolder);
        }
        System.out.println(siteFolder.toString());
        System.out.println(siteFolder.getName());
        siteFolder.mkdirs();
        recursiveCopy(new File(PATH_TEMPLATE_FOLDER), new File(pathSite));
    }

    public void createFiles() throws IOException {

    }

    public void destroy(File fileToKill) {
        if (fileToKill.isDirectory() && fileToKill.list().length > 0) {
            for (String a : fileToKill.list()) {
                destroy(new File(fileToKill + "/" + a));
            }
        }
        fileToKill.delete();
    }

    public void recursiveCopy(File source, File target) throws IOException {
        if (source.isDirectory() && source.list().length > 0) {
            for (File a : source.listFiles()) {
                if (a.isDirectory()) {
                    Files.copy(a.toPath(), new File(target.toString() + "/" + a.getName()).toPath());
                    recursiveCopy(a, new File(target.toString() + "/" + a.getName()));
                } else {
                    Files.copy(a.toPath(), new File(target.toString() + "/" + a.getName()).toPath());
                }
            }
        }
    }

    private void generateJavascript() throws FileNotFoundException {
        String jsArrayBuilder = "";
        for (Slide a : slideShow.getSlides()) {
            jsArrayBuilder += "slides.push(slide(\"" + a.getImageCaption() + "\",\"" + a.getImageFileName() + "\"));\n";
        }
        jsArrayBuilder += "\n";

        //objects -> array -> functions
        PrintWriter jsWriter = new PrintWriter(pathJSFile);
        jsWriter.print(JS_OBJECTS);
        jsWriter.print(jsArrayBuilder);
        jsWriter.print(JS_FUNCTIONS);
        jsWriter.close();
    }

    //@TODO ERROR HANDLING?
    public void loadImages() {
//        for()
//        {
//            
//        }
    }

}
