function allowDrop(ev) {		    
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("src", event.currentTarget.id);
    var id = event.currentTarget.id;
}

function drop(ev) {
    ev.preventDefault();
    var src = document.getElementById(ev.dataTransfer.getData("src"));
    var srcParent = src.parentNode;
    var tgt = ev.currentTarget.firstElementChild;
    ev.currentTarget.replaceChild(src, tgt);
    srcParent.appendChild(tgt);
    
    var targetId = ev.currentTarget.id;
    if(src.id === "lsdrag") {
    	var einzuwechselnderSpieler = document.getElementById("lseinwechseln").value;
    } else if(src.id === "rsdrag") {
    	var einzuwechselnderSpieler = document.getElementById("rseinwechseln").value;
    } else if(src.id === "msdrag") {
    	var einzuwechselnderSpieler = document.getElementById("mseinwechseln").value;
    } else if(src.id === "omdrag") {
    	var einzuwechselnderSpieler = document.getElementById("omeinwechseln").value;
    } else if(src.id === "rmdrag") {
    	var einzuwechselnderSpieler = document.getElementById("rmeinwechseln").value;
    } else if(src.id === "zmdrag") {
    	var einzuwechselnderSpieler = document.getElementById("zmeinwechseln").value;
    } else if(src.id === "lmdrag") {
    	var einzuwechselnderSpieler = document.getElementById("lmeinwechseln").value;
    } else if(src.id === "dmdrag") {
    	var einzuwechselnderSpieler = document.getElementById("dmeinwechseln").value;
    } else if(src.id === "rvdrag") {
    	var einzuwechselnderSpieler = document.getElementById("rveinwechseln").value;
    } else if(src.id === "libdrag") {
    	var einzuwechselnderSpieler = document.getElementById("libeinwechseln").value;
    } else if(src.id === "lvdrag") {
    	var einzuwechselnderSpieler = document.getElementById("lveinwechseln").value;
    } else if(src.id === "rivdrag") {
    	var einzuwechselnderSpieler = document.getElementById("riveinwechseln").value;
    } else if(src.id === "livdrag") {
    	var einzuwechselnderSpieler = document.getElementById("liveinwechseln").value;
    } else if(src.id === "twdrag") {
    	var einzuwechselnderSpieler = document.getElementById("tweinwechseln").value;
    } else {
    	var srcId = src.id;
    	var einzuwechselnderSpieler = srcId.replace(/[^0-9\.]+/g, "");
    }
    document.getElementById("einzuwechselnderSpieler").value = einzuwechselnderSpieler;
    console.log(einzuwechselnderSpieler);
    
    if(targetId === "ls") {
    	var auszuwechselnderSpieler = document.getElementById("lsauswechseln").value;
    } else if(targetId === "rs") {
    	var auszuwechselnderSpieler = document.getElementById("rsauswechseln").value;
    } else if(targetId === "ms") {
    	var auszuwechselnderSpieler = document.getElementById("msauswechseln").value;
    } else if(targetId === "om") {
    	var auszuwechselnderSpieler = document.getElementById("omauswechseln").value;
    } else if(targetId === "rm") {
    	var auszuwechselnderSpieler = document.getElementById("rmauswechseln").value;
    } else if(targetId === "zm") {
    	var auszuwechselnderSpieler = document.getElementById("zmauswechseln").value;
    } else if(targetId === "lm") {
    	var auszuwechselnderSpieler = document.getElementById("lmauswechseln").value;
    } else if(targetId === "dm") {
    	var auszuwechselnderSpieler = document.getElementById("dmauswechseln").value;
    } else if(targetId === "rv") {
    	var auszuwechselnderSpieler = document.getElementById("rvauswechseln").value;
    } else if(targetId === "lib") {
    	var auszuwechselnderSpieler = document.getElementById("libauswechseln").value;
    } else if(targetId === "lv") {
    	var auszuwechselnderSpieler = document.getElementById("lvauswechseln").value;
    } else if(targetId === "riv") {
    	var auszuwechselnderSpieler = document.getElementById("rivauswechseln").value;
    } else if(targetId === "liv") {
    	var auszuwechselnderSpieler = document.getElementById("livauswechseln").value;
    } else if(targetId === "tw") {
    	var auszuwechselnderSpieler = document.getElementById("twauswechseln").value;
    } else {
    	var auszuwechselnderSpieler = targetId;
    }
    document.getElementById("aufstellungsPositionsTyp").value = auszuwechselnderSpieler;
    console.log(auszuwechselnderSpieler);
    document.getElementById("spielerwechseln").submit();
}