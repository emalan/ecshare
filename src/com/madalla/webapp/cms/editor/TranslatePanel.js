	

google.load("language", "1");

google.setOnLoadCallback(init);

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