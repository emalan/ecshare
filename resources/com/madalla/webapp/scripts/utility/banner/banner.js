/*
* Banner
* Author : Eugene Malan
* 
* The Banner class extends Crossfade to supply a navigation bar that allows
* you to navigate from one fade element to another.
* 
* We are extending the Crossfade class found in crossfade.js. Crossfade supplies the 
* functionality that fades form one html element to another. Crossfade depends on 
* the Scriptaculous (effects) and Prototype libraries.
* 
*/

var Banner = Class.create(Crossfade, {
	initialize : function($super, elm, options) {
		$super(elm, Object.extend(Object.clone(Banner.defaults),options || {}));
		
		var element = new Element('div', {id :'controls'});
		var prev = new Element('span', {id : $(elm).className + '-previous'})
			.addClassName('control').update('<')
			.setStyle(Banner.controlStyle)
			.observe('click', this.previous.bind(this));
		element.appendChild(prev);
		for ( var index = 0; index < this.slides.length; ++index) {
			var nav = new Element('span', {id : $(elm).className + '-' + index})
				.addClassName('control').update(index + 1).setStyle(Banner.controlStyle)
			    .observe('click', this.gotoSlide.bindAsEventListener(	this, index));
			element.appendChild(nav);
		}
		var next = new Element('span', {id : $(elm).className + '-next'})
			.addClassName('control')
            .update('>')
            .setStyle(Banner.controlStyle)
		    .observe('click', this.next.bind(this));
		element.appendChild(next);
		$(elm).appendChild(element);
		this.setNav(0);
        if(!this.options.autoStart) { 
             setTimeout(this.start.bind(this),this.rndm((this.options.interval-1)*1000,(this.options.interval+1)*1000)); 
        }
	},
	cycle : function($super, dir) {
		$super(dir);
		this.setNav(this.counter);
	},	
    gotoSlide : function(e){
		if(!this.ready) { return; }
 		this.stop();
		this.ready = false;
  		var data = $A(arguments);
		var clicked = data[1]; 
		if (this.counter == clicked) { this.ready = true; return; }
		this.setNav(clicked)
		var prevSlide = this.slides[this.counter];
		var me = this; 
		var nextSlide = this.slides[clicked];
		this.counter = clicked;
        	this.loadSlide(nextSlide, me.options.transition.cycle(prevSlide, nextSlide, me));	
        },
    setNav : function(counter){
		for (var index = 0; index < this.slides.length; ++index){
			var nav = $(this.elm.className + '-' + index);
			if (nav) { nav.setStyle({fontWeight:'normal', color:'#FFFFFF'});}
		}
		var nav = $(this.elm.className + '-' + counter);
		if (nav) { new Effect.Pulsate(nav,{pulses:1, duration:0.5}); nav.setStyle({fontWeight:'bold', color:'#D3D3D3'});} 
	}
});
Banner.defaults = {
	autoStart : false,
	selectors : ['.banner']
};
Banner.controlStyle = {
	zIndex: '100', 
	cursor:'pointer', 
	fontSize:'14px', 
	marginRight:'10px',
	fontWeight: 'normal', 
	color : '#FFFFFF'
};
Banner.load = function() {
	Banner.defaults.selectors.each(function(s){
		$$(s).each(function(c){
             return new Banner(c,{autoStart:false});
		});
	});
};

if(window.FastInit) {
	FastInit.addOnLoad(Banner.load);
} else {
	Event.observe(window, 'load', Banner.load);
}