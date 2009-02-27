// Create namespace
if (typeof(Utils) == "undefined")
	Utils = { };
	

//Extract fileName and suffix from a full path and name.
//allowed example : doc|pdf|odt|htm|shtml|php	
Utils.xtractFile = function(data, allowed){
	data = data.replace(/^\s|\s$/g, ""); //trims string

	if (data.match(/([^\/\\]+)\.(doc|pdf|htm|shtml|php)$/i) )
		return {filename: RegExp.$1, ext: RegExp.$2};
	else
		return {filename: "invalid", ext: null};
}

Utils.position = function(e){
	e = Wicket.fixEvent(e);
	return {x: e.clientX, y: e.clientX};
}

