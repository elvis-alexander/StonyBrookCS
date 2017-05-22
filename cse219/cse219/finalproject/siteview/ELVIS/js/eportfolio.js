
window.onload = function() {





    // GRAB JSON FILE
    var filename = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);
    var jsonFile = "data/" + filename.substring(0, filename.indexOf('.')) + ".json";

    // GET JSON OBJECT
    $.getJSON(jsonFile, function(data) {
        handleTitle(data);
        handleFont(data);
        handleColorScheme(data);
        handleBanner(data);

        addNavBar(data.name, data.links);
        for (var i = 0; i < data.components.length; i++) {
            var component = data.components[i];
            var type = component.type;
            if (type == "image") {
                addImage(component);
            } else if (type == "video") {
                addVideo(component);
            } else if (type == "paragraph") {
                addParagraph(component);
            } else if (type == "header") {
                addHeader(component);
            } else if (type == "list") {
                addList(component);
            }
            else if (type == "slideshow") {
                addSlideShow(component);
            }
        }
        addFooter(data.footer);

    });

    function addNavBar(nameComponent, component) {
        for (var i = 0; i < component.length; i++) {
            var comp = component[i];
            var link = document.createElement('a');
            link.innerHTML = comp.src;
            link.href = comp.src + ".html";
            $("#navbar").append(link);
        }
        var name = document.createElement('a');
        name.innerHTML = nameComponent;
        name.style.cssFloat = "left";
        $("#name_div").append(name);
    }

    function handleTitle(jsonObj) {
        console.log(jsonObj.title);
        document.title = jsonObj.title;
    }

    function handleFont(jsonObj) {
        console.log(jsonObj.font);
        $("body").css({fontFamily: jsonObj.font});
    }

    function handleColorScheme(jsonObj) {
        console.log("color" + jsonObj.color)
        var pageColor = jsonObj.color;
        var colorFile = "css/" + pageColor + ".css";
        var colorStyleSheet = document.getElementById("colorscheme");
        console.log(colorFile);
        colorStyleSheet.href = colorFile;
    }

    function handleBanner(jsonObj) {
        console.log(jsonObj.banner);
        var bannerObj = jsonObj.banner;
        if (bannerObj.include === "yes") {
            var bannerTag = document.createElement("IMG");
            bannerTag.src = bannerObj.coolpath;
            console.log("coolpath: " + bannerObj.coolpath);
            $("#banner").append(bannerTag);
        }
    }

    function addImage(component) {
        var image = document.createElement("IMG");
        var cap = document.createElement("h1");
        image.src = component.coolpath;
        image.width = component.width;
        image.height = component.height;
        image.style.cssFloat = component.float;
        cap.innerHTML = component.caption;
        $("#content").append(cap);
        $("#content").append(image);
    }

    function addVideo(component) {
        var video = document.createElement("VIDEO");
        var cap = document.createElement("h1");
        video.src = component.coolpath;
        video.width = component.width;
        video.height = component.height;
        video.controls = true;
        cap.innerHTML = component.caption;
        $("#content").append(cap);
        $("#content").append(video);
    }

    function addParagraph(component) {
        var paragrapgh = document.createElement("p");
        paragrapgh.innerHTML = component.text;
        paragrapgh.setAttribute("class", component.font);
        $("#content").append(paragrapgh);
    }


    function addHeader(component) {
        var header = document.createElement("h1");
        header.innerHTML = component.text;
        header.className = component.font;
        $("#content").append(header);
    }

    function addFooter(component) {
        var footer = document.createElement("p");
        footer.innerHTML = component;
        $("#footer").append(footer);
    }

    function addList(component) {
        var listItems = document.createElement('ul');
        for (var i = 0; i < component.listitems.length; i++) {
            var item = document.createElement('li');
            item.style.fontFamily = component.font;
            item.innerHTML = component.listitems[i].caption;
            listItems.appendChild(item);
        }
        $("#content").append(listItems);
    }


    function Slide(initImgFile, initCaption) {
        this.imgFile = initImgFile;
        this.caption = initCaption;
    }


    function addSlideShow(component) {
        initSlideshow(component);
        var slideImg = document.createElement("IMG");//
        var slideCapDiv = document.createElement("div");//
        var prevBtnIn = document.createElement("button");
        var playBtnIn = document.createElement("button");//
        var nextBtnIn = document.createElement("button");
        slideCapDiv.setAttribute("id", "slide_caption");
        slideCapDiv.setAttribute("id", "slide_caption");
        slideImg.id = "slide_img";
        playBtnIn.id = "play_pause_button";

        prevBtnIn.innerHTML = "Prev";
        playBtnIn.innerHTML = "Play";
        nextBtnIn.innerHTML = "Next";



        prevBtnIn.onclick = function() {
            currentSlide--;
            if (currentSlide < 0)
                currentSlide = slides.length - 1;
            fadeInCurrentSlide();
        };

        nextBtnIn.onclick = function() {
            currentSlide++;
            if (currentSlide >= slides.length)
                currentSlide = 0;
            fadeInCurrentSlide();
        };

        playBtnIn.onclick = function() {
            processPlayPauseRequest();
        };

//        $("#content").append(listItems);

        var br1 = document.createElement("BR");
        var br2 = document.createElement("BR");
        var br3 = document.createElement("BR");
        $("#content").append(br1);
        $("#content").append(br2);
        $("#content").append(br3);
        
        $("#content").append(slideImg);
        $("#content").append(slideCapDiv);
        $("#content").append(prevBtnIn);
        $("#content").append(playBtnIn);
        $("#content").append(nextBtnIn);
    }

    function processPlayPauseRequest() {
        if (timer === null) {
            timer = setInterval(processNextRequest, SLIDESHOW_SLEEP_TIME);
        } else {
            clearInterval(timer);
            timer = null;
        }
    }

    function processNextRequest() {
        currentSlide++;
        if (currentSlide >= slides.length)
            currentSlide = 0;
        fadeInCurrentSlide();
    }


        function Slide(initImgFile, initCaption) {
            this.imgFile = initImgFile;
            this.caption = initCaption;
        }

        var IMG_WIDTH;
        var SCALED_IMAGE_HEIGHT;
        var FADE_TIME;
        var SLIDESHOW_SLEEP_TIME;

        var slides;
        var currentSlide;
        var timer;

        function initSlideshow(jsonObj) {
            IMG_WIDTH = 1000;
            SCALED_IMAGE_HEIGHT = 500.0;
            FADE_TIME = 1000;
            SLIDESHOW_SLEEP_TIME = 3000;
            slides = new Array();
            loadSlideShow(jsonObj);
            timer = null;
            initPage();
        }

        function initPage() {
            if (currentSlide >= 0) {
                $("#slide_caption").html(slides[currentSlide].caption);
                $("#slide_img").attr("src", slides[currentSlide].imgFile);
                $("#slide_img").one("load", function() {
                    autoScaleImage();
                });
            }
        }

        function loadSlideShow(obj) {
            for (var i = 0; i < obj.slidelist.length; i++) {
                var slide = obj.slidelist[i];
                var slide = new Slide("imgs/" + slide.coolpath, slide.caption);
                slides[i] = slide;
            }
            if (slides.length > 0)
                currentSlide = 0;
            else
                currentSlide = -1;
        }

        function fadeInCurrentSlide() {
            var filePath = slides[currentSlide].imgFile;
            $("#slide_img").fadeOut(FADE_TIME, function() {
                $(this).attr("src", filePath).bind('onreadystatechange load', function() {
                    if (this.complete) {
                        $(this).fadeIn(FADE_TIME);
                        $("#slide_caption").html(slides[currentSlide].caption);
                        autoScaleImage();
                    }
                });
            });
        }

        function autoScaleImage() {
            var origHeight = $("#slide_img").height();
            var scaleFactor = SCALED_IMAGE_HEIGHT / origHeight;
            var origWidth = $("#slide_img").width();
            var scaledWidth = origWidth * scaleFactor;
            $("#slide_img").height(SCALED_IMAGE_HEIGHT);
            $("#slide_img").width(scaledWidth);
            var left = (IMG_WIDTH - scaledWidth) / 2;
            $("#slide_img").css("left", left);
        }


}
;