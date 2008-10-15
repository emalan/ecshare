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
    	    onDrop: function(dragged, dropped, event){
    		    console.log(dragged);
    		    console.log(dropped);
    		    console.log(event);
    		    //Event.trigger(dropped,"drop");
    		    var wicketUrl = "'?wicket:interface=:8:albumAdminPanel:albumDisplay:albumForm:albumTreeTable:i:0:sideColumns:0:nodeLink::IBehaviorListener:0:"
    	        //var wicketUrl = "?wicket:interface=:1:albumAdminPanel:albumDisplay:albumForm::IActivePageBehaviorListener:0:&amp;wicket:ignoreIfNotActive=true";
    	        //console.log("url="+wicketUrl);
    	        var wcall = wicketAjaxGet(wicketUrl);
    	    }
    	});    	
    });
};
Event.observe(window, 'load', Dragdrop.load);
