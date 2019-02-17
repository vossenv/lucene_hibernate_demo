"use strict";

let ctx = $("#ctx").text().replace(/\/$/, "");


jQuery(function () {

    $( "#search-button" ).on("click", function(event) {event.preventDefault(); doSearch(); });

    $('input').on('keypress', (event)=> {
        if(event.which === 13){
        doSearch();
    }
});

});


function doSearch(){

    let page_size = $('#page-size').find(":selected").text();
    let query = $("#search-bar").val();

    let headers = {
        'query': query,
        'size': page_size,
        'Content-Type':'application/json'
    };

    console.log(headers);

    $.ajax({
        url: ctx + "/challenges/search",
        type: "GET",
        headers: headers,
        success: function(data) {


            $("#results").html(JSON.stringify(data, undefined, 2));

        }
    });

}




