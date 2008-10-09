/*
* fadePopup
* Author : Eugene Malan
* 
* Simple Class that will identify draggables and droppable areas on a page.
* 
* Depends on Scriptaculous drag
*  
*/

var Dragdrop = Class.create({
    initialize : function(elm){

    }
});
Dragdrop.defaults = {
    draggable : '.draggable',
    droppable : '.droppable',
    snap : 50
};
Dragdrop.load = function() {
    $$(Dragdrop.defaults.draggable).each(function(c) {
        new Draggable(c,{ghosting: true, revert: true});
        c.setStyle({ cursor: 'move'});
    });
    $$(Dragdrop.defaults.droppable).each(function(c) {
    	Droppables.add(c, { 
    	    accept: 'draggable',
    	    hoverclass: 'hover',
    	    onDrop: function(element){
    	        var wicketUrl = "?wicket:interface=:1:albumAdminPanel:albumDisplay:albumForm::IActivePageBehaviorListener:0:&amp;wicket:ignoreIfNotActive=true";
    	        console.log("url="+wicketUrl);
    	        var wcall = wicketAjaxGet(wicketUrl);
    	    }
    	});    	
    });
};
Event.observe(window, 'load', Dragdrop.load);
