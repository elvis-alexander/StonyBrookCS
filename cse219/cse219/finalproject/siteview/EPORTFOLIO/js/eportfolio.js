
window.onload = function () {
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
        for(var i = 0; i < data.components.length; i++) {
            var component = data.components[i];
            var type = component.type;
            if(type == "image") {
                addImage(component);
            } else if(type == "video") {
                addVideo(component);
            } else if (type == "paragraph") {
                addParagraph(component);
            }else if (type == "header") {
                addHeader(component);
            } else if(type == "list") {
                addList(component);
            } 
//            else if(type == "slideshow") {
//                addSlideShow(component);
//            }
        }
        addFooter(data.footer);
        
    });
    
    function addNavBar(nameComponent, component) {
        for(var i = 0; i < component.length; i++) {
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
        $("body").css({fontFamily:jsonObj.font});
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
        if(bannerObj.include  === "yes") {
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
        paragrapgh.setAttribute("class",component.font);
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
        for(var i = 0; i < component.listitems.length; i++) {
            var item = document.createElement('li');
            item.style.fontFamily = component.font;
            item.innerHTML = component.listitems[i].caption;
            listItems.appendChild(item);
        }
        $("#content").append(listItems);
    }
    
    function addSlideShow(component) {
        
        
        
    }

    
};