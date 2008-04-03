function ecGet(id) {
    return typeof id == 'string' ? document.getElementById(id) : id;
}

function ecShow(id) {
    var e=ecGet(id);
    e.style.display = "";
}

function ecHide(id) {
    var e=ecGet(id);
    e.style.display = "none";
}

function setDisplay(id,value){
    if (document.getElementById){
        obj = document.getElementById(id);
        obj.style.display = value;
    }
}
function setClass(id,className){
    if (document.getElementById){
        obj = document.getElementById(id);
        obj.className = className;
    }
}
function showhide(id){
    if (document.getElementById){
        obj = document.getElementById(id);
        if (obj.style.display == "none"){
            obj.style.display = "";
        } else {
            obj.style.display = "none";
        }
    }
}
function toggleClass(id,className){
    if (document.getElementById){
        obj = document.getElementById(id);
        if (obj.className == className){
            obj.className = "";
        } else {
            obj.className = className;
        }
    }
}