/*
* fadePopup
* Author : Eugene Malan
* 
* Simple Class that will identify popups on a page. the popups will fade
* in an out on mouseover and mouseoff.
* 
* Make sure the area that must popup is made initally invisible this way...
* style="display:none;"
* 
*  
*/
var Fadepopup = Class.create({
    initialize : function(element, options){
        this.options = Object.extend(Object.clone(Fadepopup.defaults), options || {});
	    this.triggerElement = $(element);
        this.options.popupId = $(element).id;
		this.targetArea = $(this.options.popupId + this.options.popupIdentifier);
        this.triggerElement.observe('mouseover', this.show.bind(this));
        this.triggerElement.observe('mouseout', this.hide.bind(this));    
    },
    show : function(){
        this.targetArea.appear({duration: this.options.duration});
    },
    hide : function(elm){
        this.targetArea.fade({duration: this.options.duration});
    }
});
Fadepopup.defaults = {
    selector : '.fadepopup',
    duration : 0.5,
    popupIdentifier : '-area',
    popupId : 'fadepopup'
};
Fadepopup.load = function() {
    $$(Fadepopup.defaults.selector).each(function(element) {
        return new Fadepopup(element);
    });
};
Event.observe(window, 'load', Fadepopup.load);
