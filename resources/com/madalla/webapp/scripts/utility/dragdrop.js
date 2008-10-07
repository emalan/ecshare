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
};
Event.observe(window, 'load', Dragdrop.load);
