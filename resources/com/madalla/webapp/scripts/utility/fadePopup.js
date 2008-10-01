/*
* fadePopup
* Author : Eugene Malan
* 
* Simple Class that will identify popups on a page. the popups will fade
* in an out on mouseover and mouseoff.
* 
* Make sure the are that must popup is made initally invisible this way...
* style="display:none;"
* 
*  
*/

var Fadepopup = Class.create({
    initialize : function(elm){
	    this.options = Object.extend(Object.clone(Fadepopup.defaults));
        this.options.popupId = $(elm).id;
        $(elm).observe('mouseover', this.show.bind(this));
        $(elm).observe('mouseout', this.hide.bind(this));
    },
    show : function(){
        $(this.options.popupId + this.options.popupIdentifier).appear({duration: Fadepopup.defaults.duration});
    },
    hide : function(elm){
        $(this.options.popupId + this.options.popupIdentifier).fade({duration: Fadepopup.defaults.duration});
    }
});
Fadepopup.defaults = {
    selector : '.fadepopup',
    duration : 0.5,
    popupIdentifier : '-area',
    popupId : 'fadepopup'
};
Fadepopup.load = function() {
    $$(Fadepopup.defaults.selector).each(function(c) {
        return new Fadepopup(c);
    });
};
Event.observe(window, 'load', Fadepopup.load);
