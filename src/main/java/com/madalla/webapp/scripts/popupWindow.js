var ECPopupWindow = {

	showResponseText : false,

	popupWindowId : "popupWindowId",
	
	popupWindowHandle : "popupWindowHandle",
	
	popupWindowContent : "popupWindowContent",
	
	getPopupWindow : function() {
		ECPopupWindow.init();
	    ecGet(ECPopupWindow.popupWindowId);
	},
	
	showPopupWindow : function() {
		ECPopupWindow.init();
	    ecShow(ECPopupWindow.popupWindowId);
	},
	
	hidePopupWindow : function() {
		ECPopupWindow.init();
	    ecHide(ECPopupWindow.popupWindowId);
	},
	
	showPopupWindow : function(content, title, posx, posy) {
		ECPopupWindow.init(posx,posy);
		ECPopupWindow.setContent(content);
		ECPopupWindow.setTitle(title);		
        ecShow(ECPopupWindow.popupWindowId);
    },
    
	setContent : function(content) {
	    var contentParent = ecGet(ECPopupWindow.popupWindowContent);
	    var nodes = contentParent.childNodes;
		for (i=0;i<nodes.length;i++) {
			contentParent.removeChild(nodes[i]);
  		}
  		var content = ecGet(content);
  		var contentCopy = content.cloneNode(true);
  		contentCopy.style.display = "";
        contentParent.appendChild(contentCopy);
    },   

   	setTitle : function(title) {
        var d = ecGet(ECPopupWindow.popupWindowHandle);
        d.innerHTML = title;
    },

	init : function(posx, posy) {		
        	var wad=ECPopupWindow;
        	var dwid=wad.popupWindowId;
        	var dwdhid=wad.popupWindowHandle;
        	var dwcontentid=wad.popupWindowContent;
			var firstTime = document.getElementById(dwid) == null;

			if (firstTime) {
				
				var posLeft = typeof posx != 'undefined' ? posx : 350;
				var posRight = typeof posy != 'undefined' ? posy : 100;
	            var html = 	        	
					"<div class='popup' id='"+dwid+"' style='left: "+posLeft+"px; top: "+posRight+"px; display: none; position: absolute; z-index: 1000;'>"+
					"	<div class='popupBorder' style='border-style: solid; border-width: 1px;'>"+
					"		<div style='overflow: auto; width: 100%'>"+
					"			<div class='popupLinks' style='float: right; padding: 0.2em; padding-right: 1em;'>"+
					"				<a href='javascript:ECPopupWindow.hidePopupWindow()' style='color:blue'>close</a>"+
					"			</div>"+
					"			<div id='"+dwdhid+"' class='popupTitle' style='padding: 0.2em; padding-left: 1em; margin-right: 4em; cursor: move;'>"+
					"				Popup window title"+
					"			</div>"+
					"           <div id='"+dwcontentid+"' class='popupContent' style='width: 100%; background-color: white; overflow: auto; white-space: nowrap'>"+
					"		    </div>"+
					"		</div>"+					
					"	</div>" +
					"</div>";
				
				ECPopupWindow.addElement(html);
				Drag.init(ecGet(dwdhid), ecGet(dwid));
			}
        
	},
	
	addElement : function(html) {
		var element = document.createElement("div");				
		element.innerHTML = html;
		document.body.appendChild(element);
	},

	addEvent: function(o, evType, fn) { 
		var obj = ecGet(o); 
		if (obj.addEventListener) { 
			obj.addEventListener(evType, fn, false); 
			return true; 
		} else if (obj.attachEvent) { 
			var r = obj.attachEvent("on"+evType, fn); 
   			return r; 
		} else { 
   			return false; 
		} 
	}
};

//ECPopupWindow.addEvent(window, "load", ECPopupWindow.init);

