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
        /* We are going to create some navigation html and DOM insert them in the page */
        if (this.elm.id) {
            
            /* Define common methods and add them to Element */
            var ControlMethods = {
                setControlStyle : function(element) {
                    element = $(element);
                    return element.setStyle({zIndex: '100',paddingRight: '15px', 
                        cursor:'pointer', fontWeight: 'normal', color : '#FFFFFF'});
                }
            }
            Element.addMethods(ControlMethods); 

            /* Main Div */
            var element = new Element('div', {id :'controls'});
            
            /* create previous element, bind to previous method and add to the main div */
            var prev = new Element('span', {id :'rotating-banner-previous'})
                .addClassName('control').update('&lt;').setControlStyle()
                .observe('click', this.previous.bind(this));
            
            element.appendChild(prev);
            
            /* For each slide create an element, bind to gotoSlide method and add to div */
            for ( var index = 0; index < this.slides.length; ++index) {
                var nav = new Element('span', {id :'rotating-banner-' + index})
                    .addClassName('control').update(index + 1).setControlStyle()
                    .observe('click', this.gotoSlide.bindAsEventListener( this, index));
                element.appendChild(nav);
            }
            
            /* create the next element, bind to the next method and add to the div */
            var next = new Element('span', {id :'rotating-banner-next'})
                .addClassName('control').update('&gt;').setControlStyle()
                .observe('click', this.next.bind(this));
            element.appendChild(next);
            
            /* add the controls div to the banner elelent */
            $($(elm).parentNode).appendChild(element);
            
            /* set the counter to the first one */
            this.setNav(0);
        }
        /* Start the timer */
        if (this.slides.length > 1) {setTimeout(this.start.bind(this),this.rndm((this.options.interval-1)*1000,(this.options.interval+1)*1000))};
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
        var me = this;
        var clicked = data[1]; 
        if (this.counter == clicked) { this.ready = true; return; }
        this.setNav(clicked)
        var prevSlide = this.slides[this.counter];
        var me = this; 
        nav = $(me.elm.id + '-' + this.counter );
        var nextSlide = this.slides[clicked];
        this.counter = clicked;
            this.loadSlide(nextSlide, me.options.transition.cycle(prevSlide, nextSlide, me));   
        },
    setNav : function(counter){
        for (var index = 0; index < this.slides.length; ++index){
            var nav = $(this.elm.id + '-' + index);
            if (nav) { nav.setStyle({fontWeight:'normal', color:'#FFFFFF'});}
        }
        var nav = $(this.elm.id + '-' + counter);
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