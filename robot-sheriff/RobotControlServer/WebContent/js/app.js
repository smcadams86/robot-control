document.body.addEventListener('touchmove', function(event) {
  event.preventDefault();
}, false);

document.body.addEventListener('touchstart', function(event) {
	event.preventDefault();
}, false); 

App = Ember.Application.create({
	rootElement: '#application'
});

App.ApplicationController = Ember.Controller.extend({
	direction: 'Stopped',
	photoURL: "rest/control/photo",
	pollCount: 0,
	swipeLeft: function() {
		this.sendCommand(2);
	},
	swipeRight: function() {
		this.sendCommand(1);
	},
	swipeUp: function() {
		this.sendCommand(4);
	},
	swipeDown: function() {
		this.sendCommand(3);
	},
	tap: function() {
		this.sendCommand(5);
	},
	sendCommand: function(command) {
		$.ajax({
		    type: 'post',
		    url: 'rest/control/command',
		    dataType: "json",
		    data: JSON.stringify({component: command, value: 5, issueTime: Date.now()}),
		    contentType: "application/json",
		    success: function(result) {
		      command.log('successfully send command')
		    }
  		});
	},
	poll: function() {
		var controller = this,
			pollCount = this.get('pollCount');

		//Throttle image updating
		if(pollCount % 5 === 0) {
			this.set('photoURL', 'rest/control/photo?t=' + Date.now().toString());
		}

		$.get('rest/control/command', function(data) {
			controller.setDirection(data.component);
		});

		this.set('pollCount', pollCount + 1);
	},
	setDirection: function(component) {
		var direction = "";
		switch(component) {
			case 1:
				direction = "Right";
				break;
			case 2:
				direction = "Left";
				break;
			case 3:
				direction = "Reverse";
				break;
			case 4:
				direction = "Forward";
				break;
			case 5:
			default:
				direction = "Stopped";
				break;
		}

		this.set('direction', direction);
	}

});

App.ApplicationView = Ember.View.extend({
	classNames: "full",
	xStart: 0,
	xEnd: 0,
	yStart: 0,
	yEnd: 0,
	click: function(event) {
		console.log('click');
		debugger;
	},
	touchStart: function(event) {
		this.xStart = event.originalEvent.targetTouches[0].pageX;
		this.xEnd = event.originalEvent.targetTouches[0].pageX;
		this.yStart = event.originalEvent.targetTouches[0].pageY;
		this.yEnd = event.originalEvent.targetTouches[0].pageY;
	},
	touchMove: function(event) {
		this.xEnd = event.originalEvent.targetTouches[0].pageX;
		this.yEnd = event.originalEvent.targetTouches[0].pageY;
	},
	touchEnd: function(event) {
		var xDelta = this.xStart - this.xEnd,
			yDelta = this.yStart - this.yEnd,
			xAbs = Math.abs(xDelta),	
			yAbs = Math.abs(yDelta);		


		if(xAbs > 30 || yAbs > 30) {
			if(xAbs > yAbs) {
				this.handleHoriontalSwipe(xDelta);
			} else {
				this.handleVerticalSwipe(yDelta);
			}
		}
		else {
			this.get('controller').tap();
		}
	},
	handleHoriontalSwipe: function(xDelta) {
		if(xDelta > 0) {
			this.get('controller').swipeLeft();
		} else {
			this.get('controller').swipeRight();
		}
	},
	handleVerticalSwipe: function (yDelta) {
		if(yDelta > 0) {
			this.get('controller').swipeUp();
		} else {
			this.get('controller').swipeDown();		
		}
	},
	didInsertElement: function() {
		var controller = this.get('controller');
		setInterval(function() {
			controller.poll();
		}, 1000);
	}
});