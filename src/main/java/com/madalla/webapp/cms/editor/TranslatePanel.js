	

google.load("language", "1");

google.setOnLoadCallback(init);

var destLang = '${destLang}';

function init() {
      google.language.getBranding('branding', { type : 'vertical' });
}

function changeLanguage(lang, text){
	destLang = lang;
	tinyMCE.activeEditor.setContent(text);
}

function translateContent(result) {
	var source = document.getElementById('${sourceDiv}');
    var value = source.innerHTML;
    var srcLang = 'en';
    google.language.translate(value, srcLang, destLang, translateResult);
    return false;
}

function translateResult(result) {
    if (result.translation) {
    	tinyMCE.activeEditor.setContent(result.translation);
    } else {
    	if (result.error){
    		tinyMCE.activeEditor.setContent('<span style="color:red">Error Translating - ' + result.error.message + '</span>');
    	} else {
    		tinyMCE.activeEditor.setContent('<span style="color:red">Error Translating</span>');
    	}
    }
}

