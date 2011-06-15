/*
* Crossfade
* Version 4.1 30/03/2007
* 
* Copyright (c) 2007 Millstream Web Software http://www.millstream.com.au
* 
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use, copy,
* modify, merge, publish, distribute, sublicense, and/or sell copies
* of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
* BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
* ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
* CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* * 
* Changes by Eugene Malan
* Changed to using animator.js instead of Scriptaculous. Remove effects I did not need.
* Did "fixes".
* !!! Thanks Millstream !!!
* 
*/

var Crossfade = Class.create({
	loaded : false,
	initialize : function(elm, options) {
		this.transition = Crossfade.Transition;
		var me = this, next, prev;
		this.elm = $(elm);
		this.counter = 0;
		this.prevSlide = null;
		this.options = Object.extend(Object.clone(Crossfade.defaults),options || {});
		this.elm.style.height = this.options.height + 20 + 'px';
        this.elm.style.width = this.options.width + 20 + 'px';
		this.options.interval = Math.max(2,this.options.interval);
		this.elm.makePositioned();
		this.slides = this.elm.immediateDescendants();
		if(this.options.random || this.elm.hasClassName(this.options.randomClassName)){
			this.slides.sort(function(a,b){
				return me.rndm(-1,1);
			});
		}
		if(this.slides.length > 1) {
			this.loadSlide(this.slides[0],function() {
				me.transition.prepare(me);
			});
			this.loadSlide(this.slides[1]);
			
		
			if(this.options.autoStart) { 
            	setTimeout(this.start.bind(this),this.rndm((this.options.interval-1)*1000,(this.options.interval+1)*1000)); 
        	}
		}
		
	},
	start : function() {
		this.ready = true;
		this.cycle()
		return this.timer = new PeriodicalExecuter(this.cycle.bind(this), this.options.interval); 
	},
	stop : function() {
		if (this.timer) {this.timer.stop();}; 
	},
	next : function(){
		this.stop();
		this.cycle();
	},
	previous : function() {
		this.stop();
		this.cycle(-1);
	},
	cycle : function(dir) {
		if(!this.ready) { return; }
		this.ready = false;
		dir = (dir === -1) ? dir : 1;
		var me = this, prevSlide, nextSlide, opt, fade;
		prevSlide = this.slides[this.counter];
		this.counter = this.loopCount(this.counter + dir);
		if(this.counter == 0){
			this.loaded = true;
		}
		nextSlide = this.slides[this.counter];
		this.loadSlide(nextSlide, me.transition.cycle(prevSlide, nextSlide, me));
		if(!this.loaded) {
			this.loadSlide(this.slides[this.loopCount(this.counter+1)]);
		}
	},
	loadSlide : function(slide, onload){
		var loaders = [], me = this, img, pnode, onloadFunction;
		onload = typeof onload === 'function' ? onload : function(){};
		onloadFunction = function() {
			onload();
			//me.ready = true;
		};
		slide = $(slide);
		loaders = Selector.findChildElements(slide,[this.options.imageLoadSelector]);
		if(loaders.length && loaders[0].href !== ''){
			img = document.createElement('img');
			img.className = 'loadimage';
			img.onload = onloadFunction;
			img.src = loaders[0].href;
			loaders[0].parentNode.replaceChild(img,loaders[0]);
		} else {
			loaders = [];
			loaders = Selector.findChildElements(slide, [this.options.ajaxLoadSelector]);
			if(loaders.length && loaders[0].href !== ''){
				new Ajax.Updater(slide, loaders[0].href, {method:'get',onComplete:onloadFunction});
			} else {
				onloadFunction();
			}
		}
	},
	loopCount : function(c){
		if(c >= this.slides.length){
			c = 0;
		} else if (c < 0) {
			c = this.slides.length - 1
		}
		return c;
	},
	rndm : function(min, max){
		return Math.floor(Math.random() * (max - min + 1) + min);
	},
	timer : null,effect : null,ready : false
    
});

Crossfade.Transition = {
	cycle : function(prev, next, show) {
		var opt = show.options;
		show.effect = new Animator({duration: opt.duration, onComplete: function(){show.ready = true;}})
		.addSubject(new NumericalStyleSubject(prev, 'opacity', 1 , 0))
		.addSubject(new NumericalStyleSubject(next, 'opacity', 0, 1))
		.toggle();
	},
	prepare : function(show){
		show.slides.each(function(s,i){
			$(s).setStyle({opacity:(i === 0 ? 1 : 0),visibility:'visible'});
		});	
	}
};

Crossfade.defaults = {
	autoLoad : true,
	autoStart : true,
	random : false,
	//width : 450, height : 325, //in pixels - note: frame will be 20 px larger
	randomClassName : 'random',
	selectors : ['.crossfade'],
	imageLoadSelector : 'a.loadimage',
	ajaxLoadSelector : 'a.load',
	interval : 5,
	duration : 1000 /* milliseconds */
};
Crossfade.setup = function(options) {
	Object.extend(Crossfade.defaults,options);
};
Crossfade.load = function() {
	if(Crossfade.defaults.autoLoad) {
		Crossfade.defaults.selectors.each(function(s){
			$$(s).each(function(c){
				return new Crossfade(c);
			});
		});
	}
};

if(window.FastInit) {
	FastInit.addOnLoad(Crossfade.load);
} else {
	Event.observe(window, 'load', Crossfade.load);
}
