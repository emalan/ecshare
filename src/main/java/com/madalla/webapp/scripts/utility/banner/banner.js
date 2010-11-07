/*
* Banner
* Author : Eugene Malan
* 
* The Banner class extends Crossfade to supply a navigation bar that allows
* you to navigate from one fade element to another.
* 
* We are extending the Crossfade class found in crossfade.js. Crossfade supplies the 
* functionality that fades form one html element to another. Crossfade depends on 
* the Animator.js and Prototype libraries.
* 
*/

var Banner = Class.create(Crossfade, {
	initialize : function($super, elm, options) {
		$super(elm, Object.extend(Object.clone(Banner.defaults),options || {}));
		this.bannerOptions = Object.extend(Object.clone(Banner.defaults),options || {});
		$(elm).addClassName(this.bannerOptions.bannerStyle);
		//elm.style.height = this.bannerOptions.height + 20 + 'px';
        //elm.style.width = this.bannerOptions.width + 20 + 'px';
		this.bannerId =  $(elm).id || 'banner';
		var element = new Element('div', {id : this.bannerId+'-controls'});
		element.addClassName(this.bannerOptions.controlsStyle);
		var prev = new Element('span', {id : this.bannerId+'-previous'})
			.addClassName(this.bannerOptions.controlStyle).update('<')
			.setStyle(Banner.controlStyleDefault)
			.observe('click', this.previous.bind(this));
		element.appendChild(prev);
		this.anim = [];
		for ( var index = 0; index < this.slides.length; ++index) {
			var nav = new Element('span', {id : this.bannerId + '-' + index})
			    .addClassName(this.bannerOptions.controlStyle)
				.update(index + 1)
			    .observe('click', this.gotoSlide.bindAsEventListener(	this, index));
			element.appendChild(nav);
			this.anim[index] = new Animator().addSubject(
				new CSSStyleSubject(nav,this.bannerOptions.controlStyleDefault, this.bannerOptions.controlStyleActive)
			);
		}
		var next = new Element('span', {id : this.bannerId + '-next'})
			.addClassName(this.bannerOptions.controlStyle)
            .update('>')
            .setStyle(Banner.controlStyleDefault)
		    .observe('click', this.next.bind(this));
		element.appendChild(next);
		$(elm).appendChild(element);
		this.setNav(0);
        if(!this.options.autoStart) { 
             setTimeout(this.start.bind(this),this.rndm((this.options.interval-1)*1000,(this.options.interval+1)*1000)); 
        }
	},
	cycle : function($super, dir) {
		var prev = this.counter;
		$super(dir);
		this.setNav(this.counter, prev);
	},	
    gotoSlide : function(e){
		this.stop();
		if(!this.ready) { return; }
		this.ready = false;
  		var data = $A(arguments);
		var clicked = data[1]; 
		var prev = this.counter;
		if (this.counter == clicked) { this.ready = true; return; }
		this.setNav(clicked, prev)
		var prevSlide = this.slides[this.counter];
		var me = this; 
		var nextSlide = this.slides[clicked];
		this.counter = clicked;
        	this.loadSlide(nextSlide, me.transition.cycle(prevSlide, nextSlide, me));	
        },
    setNav : function(counter, prev){
		if (this.anim[prev]) { this.anim[prev].toggle();}
		if (this.anim[counter]) { this.anim[counter].toggle();}
	}
});
Banner.defaults = {
	autoStart : false,
	selectors : ['.bannerAuto'],
	bannerStyle : 'banner',
	controlsStyle : 'bannerControls',
	controlStyle : 'bannerControl',
	controlStyleDefault : 'defaultBannerControl',
	controlStyleActive : 'activeBannerControl'
};

/* The autoStart is passed on to Crossfade, so the idea is that 
 * crossface won't autostart and we can start Banner. That way we don't have 2
 * timers. Ugly? Sorry!!
 */
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