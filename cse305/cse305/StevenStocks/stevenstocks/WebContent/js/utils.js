var utils = {
	norm: function(value, min, max) {
		return (value - min) / (max - min);
	},

	lerp: function(norm, min, max) {
		return (max - min) * norm + min;
	},

	map: function(value, sourceMin, sourceMax, destMin, destMax) {
		return utils.lerp(utils.norm(value, sourceMin, sourceMax), destMin, destMax);
	},

	clamp: function(value, min, max) {
		return Math.min(Math.max(value, Math.min(min, max)), Math.max(min, max));
	},

	distance: function(p0, p1) {
		var dx = p1.x - p0.x,
			dy = p1.y - p0.y;
		return Math.sqrt(dx * dx + dy * dy);
	},

	distanceXY: function(x0, y0, x1, y1) {
		var dx = x1 - x0,
			dy = y1 - y0;
		return Math.sqrt(dx * dx + dy * dy);
	},

	circleCollision: function(c0, c1) {
		return utils.distance(c0, c1) <= c0.radius + c1.radius;
	},

	circlePointCollision: function(x, y, circle) {
		return utils.distanceXY(x, y, circle.x, circle.y) < circle.radius;
	},

	pointInRect: function(x, y, rect) {
		return utils.inRange(x, rect.x, rect.x + rect.width) &&
		       utils.inRange(y, rect.y, rect.y + rect.height);
	},

	inRange: function(value, min, max) {
		return value >= Math.min(min, max) && value <= Math.max(min, max);
	},

	rangeIntersect: function(min0, max0, min1, max1) {
		return Math.max(min0, max0) >= Math.min(min1, max1) &&
			   Math.min(min0, max0) <= Math.max(min1, max1);
	},

	rectIntersect: function(r0, r1) {
		return utils.rangeIntersect(r0.x, r0.x + r0.width, r1.x, r1.x + r1.width) &&
			   utils.rangeIntersect(r0.y, r0.y + r0.height, r1.y, r1.y + r1.height);
	},

	degreesToRads: function(degrees) {
		return degrees / 180 * Math.PI;
	},

	radsToDegrees: function(radians) {
		return radians * 180 / Math.PI;
	},

	randomRange: function(min, max) {
		return min + Math.random() * (max - min);
	},

	randomInt: function(min, max) {
		return Math.floor(min + Math.random() * (max - min + 1));
	},

	roundToPlaces: function(value, places) {
		var mult = Math.pow(10, places);
		return Math.round(value * mult) / mult;
	},

	roundNearest: function(value, nearest) {
		return Math.round(value / nearest) * nearest;
	},

	quadraticBezier: function(p0, p1, p2, t, pFinal) {
		pFinal = pFinal || {};
		pFinal.x = Math.pow(1 - t, 2) * p0.x +
				   (1 - t) * 2 * t * p1.x +
				   t * t * p2.x;
		pFinal.y = Math.pow(1 - t, 2) * p0.y +
				   (1 - t) * 2 * t * p1.y +
				   t * t * p2.y;
		return pFinal;
	},

	cubicBezier: function(p0, p1, p2, p3, t, pFinal) {
		pFinal = pFinal || {};
		pFinal.x = Math.pow(1 - t, 3) * p0.x +
				   Math.pow(1 - t, 2) * 3 * t * p1.x +
				   (1 - t) * 3 * t * t * p2.x +
				   t * t * t * p3.x;
		pFinal.y = Math.pow(1 - t, 3) * p0.y +
				   Math.pow(1 - t, 2) * 3 * t * p1.y +
				   (1 - t) * 3 * t * t * p2.y +
				   t * t * t * p3.y;
		return pFinal;
	},

  multicurve: function(points, context) {
		var p0, p1, midx, midy;

		context.moveTo(points[0].x, points[0].y);

		for(var i = 1; i < points.length - 2; i += 1) {
			p0 = points[i];
			p1 = points[i + 1];
			midx = (p0.x + p1.x) / 2;
			midy = (p0.y + p1.y) / 2;
			context.quadraticCurveTo(p0.x, p0.y, midx, midy);
		}
		p0 = points[points.length - 2];
		p1 = points[points.length - 1];
		context.quadraticCurveTo(p0.x, p0.y, p1.x, p1.y);
	},

  lineIntersect: function(p0, p1, p2, p3){
    var A1 = p1.y - p0.y;
    var B1 = p0.x - p1.x;
    var C1 = A1 * p0.x + B1 * p0.y;
    var A2 = p3.y - p2.y;
    var B2 = p2.x - p3.x;
    var C2 = A2 * p2.x + B2 * p2.y;
    var denominator = A1 * B2 - A2 * B1;

    return {
      x:(B2 * C1 - B1 * C2) / denominator,
      y:(A1 * C2 - A2 * C1) / denominator,
    };
  },

	segmentIntersect: function(p0, p1, p2, p3){
		var A1 = p1.y - p0.y,
			B1 = p0.x - p1.x,
			C1 = A1 * p0.x + B1 * p0.y,
			A2 = p3.y - p2.y,
			B2 = p2.x - p3.x,
			C2 = A2 * p2.x + B2 * p2.y,
			denominator = A1 * B2 - A2 * B1;

		if(denominator == 0) {
			return null;
		}

		var intersectX = (B2 * C1 - B1 * C2) / denominator,
			intersectY = (A1 * C2 - A2 * C1) / denominator,
			rx0 = (intersectX - p0.x) / (p1.x - p0.x),
			ry0 = (intersectY - p0.y) / (p1.y - p0.y),
			rx1 = (intersectX - p2.x) / (p3.x - p2.x),
			ry1 = (intersectY - p2.y) / (p3.y - p2.y);

		if(((rx0 >= 0 && rx0 <= 1) || (ry0 >= 0 && ry0 <= 1)) &&
		   ((rx1 >= 0 && rx1 <= 1) || (ry1 >= 0 && ry1 <= 1))) {
			return {
				x: intersectX,
				y: intersectY
			};
		}
		else {
			return null;
		}
  },

  linearTween: function(t, b, c, d){
    return c * t / d + b;
  },

  easeInQuad: function(t, b, c, d) {
		return c*(t/=d)*t + b;
	},

	easeOutQuad: function(t, b, c, d) {
		return -c *(t/=d)*(t-2) + b;
	},

	easeInOutQuad: function(t, b, c, d) {
		if ((t/=d/2) < 1)
      return c / 2 * t * t + b;

		return -c / 2 * ((--t) * (t-2) - 1) + b;
	},

  easeInOutBounce: function (t, b, c, d) {
  	if (t < d/2)
      return easeInBounce(t*2, 0, c, d) * .5 + b;
  	return easeOutBounce(t*2-d, 0, c, d) * .5 + c*.5 + b;
  }
};

function  easeInBounce(t, b, c, d) {
   return c - easeOutBounce (d-t, 0, c, d) + b;
}

function easeOutBounce(t, b, c, d) {
  if ((t/=d) < (1/2.75))
    return c*(7.5625*t*t) + b;

  else if (t < (2/2.75))
    return c*(7.5625*(t-=(1.5/2.75))*t + .75) + b;

   else if (t < (2.5/2.75))
    return c*(7.5625*(t-=(2.25/2.75))*t + .9375) + b;

  else
    return c*(7.5625*(t-=(2.625/2.75))*t + .984375) + b;

}
