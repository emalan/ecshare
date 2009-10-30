	
tinyMCE.init({
	mode : "textareas",
	theme : "advanced",
	
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

function removeEditors(){
    for (var ed in tinyMCE.editors)  {
    	console.log('removing editor : ' + ed);
        ed.execCommand('mceRemoveControl',false);
    }
}

function addEditors() {
    
    var nodeList = document.getElementsByTagName("textarea");
    console.log('found textareas : ' + nodeList.length);
    var selector = tinyMCE.getParam("editor_selector");
    var elementRefAr = new Array();
    for (var i=0; i<nodeList.length; i++) {
       var elm = nodeList.item(i);

       if (selector != '' && !new RegExp('\\b' + selector + '\\b').test(tinyMCE.getAttrib(elm, "class")))
           continue;

       elementRefAr[elementRefAr.length] = elm;
       tinyMCE.addMCEControl(elm, elm.name);
       console.log('added editor');
    } 

}

//google.load("language", "1");

//google.setOnLoadCallback(init);

function init() {
      var src = document.getElementById('src');
      var dst = document.getElementById('dst');

      ${dstlangs}
      
      google.language.getBranding('branding', { type : 'vertical' });

      //submitChange();
}

function submitChange() {
	    
      var value = tinyMCE.get('text').getContent();
      var src = '';
      var dest = document.getElementById('dst').value;
      google.language.translate(value, src, dest, translateResult);
      return false;
}

function translateResult(result) {
      var resultBody = document.getElementById("results_body");
      if (result.translation) {
        resultBody.innerHTML = result.translation;
      } else {
        resultBody.innerHTML = '<span style="color:red">Error Translating</span>';
      }
}

function copyupResult() {
	var resultBody = document.getElementById("results_body");
	tinyMCE.get('text').setContent(resultBody.innerHTML);
	
}