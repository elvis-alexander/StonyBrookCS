var data = "./json/sample.json";
window.onload = function () {
    var filename = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);
    var jsonFile = "./json/" + filename.substring(0, filename.indexOf('.')) + ".json";
    $.getJSON(jsonFile, function(data) {
        document.title = data.title;
        var includesBanner = data.hasOwnProperty("banner");
        if(includesBanner) {
            addBannerImage(data.banner);
        }
        addNavBar(data.navbar);
        // Include Components
        for(var i = 0; i < data.components.length; i++) {
            var component = data.components[i];
            var type = component.type;
            if(type == "image") {
                addImage(component);
            } else if(type == "video") {
                addVideo(component);
            } else if(type == "paragraph") {
                addParagraph(component);
            } else if(type == "header") {
                addHeader(component);
            }
        }
        
    });
    
    function addNavBar(component) {
        var ul = document.createElement('ul');
        for(var i = 0; i < component.length; i++) {
            var comp = component[i];
            var li = $('<li/>').appendTo(ul);
            var a = $('<a/>').text(comp.title).attr('href',comp.src).appendTo(li);
        }
        $("#navbar").append(ul);
    }
    
    function addBannerImage(component) {
        var banner = document.createElement("IMG");
        banner.src = component;
        $("#banner").append(banner);
    }
    
    function addImage(component) {
        var image = document.createElement("IMG");
        image.src = component.src;
        image.width = component.width;
        image.height = component.height;
        image.style.cssFloat = component.float;
        $("#content").append(image);
    }
    
    function addVideo(component) {
        var video = document.createElement("IFRAME");
        video.src = component.src;
        video.width = component.width;
        video.height = component.height;
        $("#content").append(video);
    }
    
    function addParagraph(component) {
        var paragrapgh = document.createElement("p");
        paragrapgh.innerHTML = component.text;
        $("#content").append(paragrapgh);
    }
    
    function addHeader(component) {
        var header = document.createElement("h1");
        header.innerHTML = component.text;
        $("#content").append(header);
    }

    
};