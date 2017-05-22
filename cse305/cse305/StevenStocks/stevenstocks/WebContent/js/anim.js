var randomScalingFactor = function(){ return Math.round(50 + Math.random()*100)};
var randomScalingFactor_ = function(){ return Math.round(200+Math.random()*50)};

var barChartData = {
  labels : ["Facebook","Google","Apple","SnapChat","Twitter","Spotify", "Uber"],
  datasets : 
  [
    {
      fillColor : "rgba(0,175,94,1)",
      strokeColor : "rgba(220,220,220,1)",
      highlightFill: "rgba(220,220,220,1)",
      highlightStroke: "rgba(220,220,220,1)",
      data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
    },
    {
      fillColor : "rgba(51,51,51,1)",
      strokeColor : "rgba(151,187,205,1)",
      highlightFill : "rgba(151,187,205,1)",
      highlightStroke : "rgba(151,187,205,1)",
      data : [randomScalingFactor_(),randomScalingFactor_(),randomScalingFactor_(),randomScalingFactor_(),randomScalingFactor_(),randomScalingFactor_(),randomScalingFactor_()]
    }
  ]
};
window.onload = function () {
  var canvas = document.getElementById('background-canvas');
  var context = canvas.getContext('2d');
  var width = canvas.width = window.innerWidth;
  var height = canvas.height = window.innerHeight;
  var translate_x = width * 0.75;
  var translate_y = height * 0.50;
  var focalPoint = 400;
  var ctx = document.getElementById("canvas").getContext("2d");
    window.myBar = new Chart(ctx).Bar(barChartData, {responsive : true});

  var stockSymbols = ["IBM", "F", "GM", "AAPL", "FB", "AMZN", "NFLX", "GOOGL",
                      "STAF", "XTNT" , "LGCYP", "ARGS", "CTRV", "ARNT", "AMTX",
                      "AZUR", "ZSAN", "GE", "PFE", "INTC", "NOK", "TTHI", "IOC",
                      "BRK-A", "HBANP", "IRDMB", "PCLN", "KBIO", "CPXX", "ERII",
                      "AMCN", "OCLR"];

  var textList = createTextList(stockSymbols);

  context.translate(translate_x, translate_y);
  render();

  function render() {
    context.clearRect(-translate_x, -translate_y, width, height);
    context.save();


    for(var i = 0; i < textList.length; i++){
      var temp = textList[i];
      temp.project(context, focalPoint);
      rotateY(temp);
    }

    context.restore();
    requestAnimationFrame(render);
  }

  function createTextList(){
    var list = [];

    for (var i = 0; i < stockSymbols.length * 1; i++) {
      var x = randomRange(-500, 500); 
      var y = randomRange(-500, 500);
      var z = randomRange(100, 300);

      var newText = _Text.create(stockSymbols[i % stockSymbols.length], x , y, z);
      list.push(newText); 
    }

    return list;
  }
};

var _Text = {
  x: 0,
  y: 0,
  z: 0,
  angle: 0,
  text: null,

  create: function(text, x, y, z){
    var obj = Object.create(this);
    obj.x = x;
    obj.y = y;
    obj.z = z || 1;
    obj.angle = randomRange(-0.01, 0.01);
    if(Math.abs(obj.angle) < 0.005)
      if(obj.angle > 0)
        obj.angle = 0.005;
      else
        obj.angle = -0.005;

    obj.text = text;

    return obj;
  },

  draw: function(context){
    context.beginPath();
    context.font = "20px monospace";
    context.fillText(this.text, this.x, this.y);
    context.fill();
  },

  project: function(context, fp){
    var scale = fp / (fp + this.z);
    var sx = this.x * scale;
    var sy = this.y * scale;

    context.beginPath();
    context.font = (32 * scale) + "px monospace";

    context.fillText(this.text, sx, sy);
    context.fill();
  }

};

function randomRange(min, max) {
    return min + Math.random() * (max - min);
  }

function rotateX(text){
  var cos = Math.cos(text.angle);
  var sin = Math.sin(text.angle);

  var y = text.y * cos - text.z * sin;
  var z = text.z * cos + text.y * sin;
  text.y = y;
  text.z = z;

}

function rotateY(text){
  var cos = Math.cos(text.angle);
  var sin = Math.sin(text.angle);

  var x = text.x * cos - text.z * sin;
  var z = text.z * cos + text.x * sin;
  text.x = x;
  text.z = z;

}

function rotateZ(text){
  var cos = Math.cos(text.angle);
  var sin = Math.sin(text.angle);

  var x = text.x * cos - text.y * sin;
  var y = text.y * cos + text.x * sin;
  text.x = x;
  text.y = y;
  
}

function translateX(text, dx){
  text.x = dx;
}

function translateY(text, dy){
  text.y += dy;
}

function translateZ(text, dz){
  text.z = dz;
}