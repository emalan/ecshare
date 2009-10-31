	
tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	editor_selector : "mceEditor",
	editor_deselector : "mceNoEditor",
	
	theme_advanced_toolbar_location : "top",
	theme_advanced_toolbar_align : "left",
	theme_advanced_statusbar_location : "bottom",
	theme_advanced_resizing : true,
	
	theme_advanced_buttons1:"${button1}",
	theme_advanced_buttons2:"${button2}",
	theme_advanced_buttons3:"${button3}",
	
	cleanup_on_startup : true,
	cleanup: true
//	setup : function(ed) {
//    	ed.onChange.add(function(ed, l) {
//    		console.debug('Editor contents was modified. Contents: ' + l.content);
//    	});
//    
//    	ed.onKeyDown.add(function(ed, e) {
//    		console.debug('Key down event: ' + e.keyCode);
//    	});
//	}
});

