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

var Fadepopup = Class.create(
    popupId : '',
    initialize : function(elm){
        this.popupId = $(elm).id;
        $(elm).observe('mouseout', this.show.bind(this));
        $(elm).observe('mouseover', this.hide.bind(this));
    },
    show : function(){
        $(this.popupId + Fadepopup.defaults.popupIdentifier).appear({duration: Fadepopup.defaults.duration});
    },
    hide : function(elm){
        $(this.popupId + Fadepopup.defaults.popupIdentifier).fade({duration: Fadepopup.defaults.duration});
    }
);
Fadepopup.defaults = {
    selector : '.fadepopup',
    duration : 0.5,
    popupIdentifier : '-area'
};
Fadepopup.load = function() {
    $$(Fadepopup.defaults.selector).each(function(c) {
        return new Fadepopup(c);
    });
};
Event.observe(window, 'load', Fadepopup.load);
