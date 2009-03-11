// Create Utils namespace
if (typeof(Utils) == "undefined")
	Utils = { 
	
//Extract fileName and suffix from a full path and name.
//allowed example : doc|pdf|odt|htm|shtml|php	
xtractFile : function(data, allowed){
	data = data.replace(/^\s|\s$/g, ""); //trims string

	if (data.match(/([^\/\\]+)\.(doc|pdf|htm|shtml|php)$/i) )
		return {filename: RegExp.$1, ext: RegExp.$2};
	else
		return {filename: "invalid", ext: null};
}
,
position : function(e){
	e = Wicket.$(e);
	return {x: e.offsetTop, y: e.offsetWidth};
}
,
hasClassName : function(element, className) {
	if (!(element = Wicket.$(element))) return;
	var elementClassName = element.className;
	return (elementClassName.length > 0 && (elementClassName == className ||
	new RegExp("(^|\\s)" + className + "(\\s|$)").test(elementClassName)));
}
,
addClassName : function(element, className) {
	if (!(element = Wicket.$(element))) return;
 	if (!Utils.hasClassName(element, className))
 	element.className += (element.className ? ' ' : '') + className;
 	return element;
}
,
removeClassName : function(element, className) {
 	if (!(element = Wicket.$(element))) return;
 	element.className = element.className.replace(
 	new RegExp("(^|\\s+)" + className + "(\\s+|$)"), ' ').strip();
 	return element;
}
,
strip : function() {
     return this.replace(/^\s+/, '').replace(/\s+$/, '');
}
};


//from Prototype.js
Object.extend = function(destination, source) {
  for (var property in source)
    destination[property] = source[property];
  return destination;
};

Object.extend(String.prototype, {

  truncate: function(length, truncation) {
    length = length || 30;
    truncation = Object.isUndefined(truncation) ? '...' : truncation;
    return this.length > length ?
      this.slice(0, length - truncation.length) + truncation : String(this);
  },

  strip: function() {
    return this.replace(/^\s+/, '').replace(/\s+$/, '');
  },

  stripTags: function() {
    return this.replace(/<\/?[^>]+>/gi, '');
  },

  stripScripts: function() {
    return this.replace(new RegExp(Prototype.ScriptFragment, 'img'), '');
  }
});
