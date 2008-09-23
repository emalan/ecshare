/*
* Banner
* Author : Eugene Malan
* 
* This Class will use Crossfade to crossfade from one block of html
* to another. It will also add controls that allow you to select a
* block.
* 
* We are extending the Crossfade class found in crossfade.js
* the Crossfade class depends on the Scriptaculous and Prototype libraries 
* 
* * 
*/

var Banner = Class.create(Crossfade, {
	initialize : function($super, elm, options) {
		$super(elm, options);
		Function.prototype.getInfo = function(){
			alert(this);
		}
		var ControlMethods = {
			setControlStyle : function(element) {
				element = $(element);
			    return element.setStyle({zIndex: '100', 
					cursor:'pointer', fontSize:'14px', marginRight:'10px',
				    fontWeight: 'normal', color : '#FFFFFF'});
			}
		}
		Element.addMethods(ControlMethods);	
		var element = new Element('div', {id :'controls'});
		var prev = new Element('span', {id : $(elm).className + '-previous'})
			.addClassName('control').update('<').setControlStyle()
			.observe('click', this.previous.bind(this));
		element.appendChild(prev);
		for ( var index = 0; index < this.slides.length; ++index) {
			var nav = new Element('span', {id : $(elm).className + '-' + index})
				.addClassName('control').update(index + 1).setControlStyle()
			    .observe('click', this.gotoSlide.bindAsEventListener(	this, index));
			element.appendChild(nav);
		}
		var next = new Element('span', {id : $(elm).className + '-next'})
			.addClassName('control').update('>').setControlStyle()
		    .observe('click', this.next.bind(this));
		element.appendChild(next);
		$($(elm).parentNode).appendChild(element);
		this.setNav(0);
		setTimeout(this.start.bind(this),this.rndm((this.options.interval-1)*1000,(this.options.interval+1)*1000));
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

Banner.load = function() {
	Crossfade.defaults.selectors.each(function(s){
		$$(s).each(function(c){
			return new Banner(c);
		});
	});
};

if(window.FastInit) {
	FastInit.addOnLoad(Banner.load);
} else {
	Event.observe(window, 'load', Banner.load);
}