
tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	
	// Theme options
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	theme_advanced_resizing : false,
	
	theme_advanced_buttons1:"bold,italic,underline,strikethrough,|,sub,sup,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect",
	theme_advanced_buttons2:"bullist,numlist,|,hr,charmap,removeformat,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,cleanup,code",
	theme_advanced_buttons3:"",
		
	setup : function(ed) {
	    ed.onChange.add(function(ed, l) {
	        console.debug('Editor contents was modified. Contents: ' + l.content);
	    });
	    
	    ed.onKeyDown.add(function(ed, e) {
	          console.debug('Key down event: ' + e.keyCode);
	     });
	}

});