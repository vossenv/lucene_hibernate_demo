"use strict";

var ctx = $("#ctx").text().replace(/\/$/, "");


jQuery(function () {

    $( "#search-button" ).on("click", function() { doSearch(); });

});


function doSearch(){

    var page_size = $('#page-size').find(":selected").text();
    var query = $("#search-bar").val();

    var headers = {
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




