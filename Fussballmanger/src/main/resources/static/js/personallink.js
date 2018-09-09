function openWindowInNewWindowPersonal(id) {
    var personalId = id
    window.open('/personal/' + personalId,'personal','resizable,height=700,width=700'); 
    return false;
}