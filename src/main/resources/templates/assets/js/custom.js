$('#navbar-search-input').keypress(function(event){
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        let q = $(this).val();
        let url = '';
        if (q.length > 0) url = `search?q=${q}`;
        else url = 'search'

        window.location.href = url;
    }
    event.stopPropagation();
});

function openDiscovery (element) {
    let url = `discovery?q=${element.getAttribute('data-id')}`;
    console.log(url, 'rud')
    window.location.href = url;
}