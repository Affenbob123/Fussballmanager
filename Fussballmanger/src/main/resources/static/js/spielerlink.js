function openWindowInNewWindow(id) {
    var spielerId = id
    window.open('/spieler/' + spielerId,'spieler','resizable,height=700,width=700'); 
    return false;
}