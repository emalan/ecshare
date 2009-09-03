	tinyMCE.init({
		// General options
		mode : "textareas",
		theme : "advanced",
		plugins : "safari,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",

		// Theme options
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : false,

		//theme_advanced_buttons1:"bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect",
		//theme_advanced_buttons2:"bullist,numlist,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code",
		//theme_advanced_buttons3:"hr,removeformat,visualaid,|,sub,sup,|,charmap"

		theme_advanced_buttons1 : "\"newdocument,fullscreen,cleanup,removeformat,code,help,|,undo,redo,|,paste,pastetext,pasteword,|,link,unlink,anchor,image,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,/"",
		theme_advanced_buttons2 : "formatselect,fontselect,fontsizeselect|,forecolor,backcolor,|,tablecontrols",
		theme_advanced_buttons3 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,blockquote,|,hr,advhr,charmap,emotions,|,insertdate,inserttime"

		// Example content CSS (should be your site CSS)

	});
