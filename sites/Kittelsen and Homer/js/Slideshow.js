
slide = function(initCaption,initImage){
this.caption = initCaption;
this.image = initImage;
};

var index = 0;
var slides = [];

slides.push(slide("muntin","6934fc2db4.jpg"));
slides.push(slide("hvar","hakketspett.jpg"));
slides.push(slide("fog war","Winslow_Homer_-_The_Fog_Warning_-_Google_Art_Project.jpg"));


function nextButton(){
index++;
if(index >= slides.length){index = 0;}

alert("next");
}

function previousButton(){
index--;
if(index < 0){index = slides.length-1;}


alert("previous");
}

function playSlideShow(){
alert("play");
}

