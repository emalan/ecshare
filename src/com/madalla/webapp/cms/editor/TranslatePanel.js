	

google.load("language", "1");

google.setOnLoadCallback(init);

var destLang = '${destLang}';

function init() {
      google.language.getBranding('branding', { type : 'vertical' });
}

function changeLanguage(lang){
	destLang = lang;
}

function translateContent(result) {
	var source = document.getElementById('${sourceDiv}');
    var value = source.innerHTML;
    console.log(value);
    var srcLang = 'en';
    google.language.translate(value, srcLang, destLang, translateResult);
    return false;
}

function translateResult(result) {
    if (result.translation) {
    	tinyMCE.get('text').setContent(result.translation);
    } else {
    	tinyMCE.get('text').setContent('<span style="color:red">Error Translating</span>');
    }
}
