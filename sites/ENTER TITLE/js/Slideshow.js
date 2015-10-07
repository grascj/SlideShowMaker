
slide = function(initCaption,initImage){
this.caption = initCaption;
this.image = initImage;
};

var index = 0;
var slides = [];
var update;
var name;

name = "ENTER TITLE";

slides.push(new slide("1 ","DefaultStartSlide.png"));
slides.push(new slide("2 ","DefaultStartSlide.png"));
slides.push(new slide("3 ","DefaultStartSlide.png"));


function nextButton(){
index++;
if(index >= slides.length){index = 0;}
changeSlide(index);
}

function previousButton(){
index--;
if(index < 0){index = slides.length-1;}
changeSlide(index);
}

function initPage(){
document.getElementById("title").innerHTML = (name);
document.getElementById("caption_text").innerHTML = (slides[index]).caption;
document.getElementById("slideshow_img").setAttribute("src","./img/" + (slides[index]).image);
}

function playSlideShow(){
var mode = document.getElementById("slideShowPlayImage");
if(mode.src.match("./img/button_images/play.png")){
mode.src = "./img/button_images/pause.png";
 update = setInterval(nextButton,3000);}
else {
mode.src = "./img/button_images/play.png";
 clearInterval(update);}
}

function changeSlide(newLoc) {
document.getElementById("caption_text").innerHTML = (slides[index]).caption;
document.getElementById("slideshow_img").setAttribute("src","./img/" + (slides[index]).image);
}

