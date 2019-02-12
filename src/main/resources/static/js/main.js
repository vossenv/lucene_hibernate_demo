"use strict";

var ctx = $("#ctx").text().replace(/\/$/, "");

jQuery(function () {


    $( "#search-button" ).click(function() { doSearch(); });

});


function doSearch(){



    $.ajax({
        url: ctx + "/challenges/search",
        type: "GET",
        beforeSend: function(xhr){xhr.setRequestHeader('query', $("#search-bar").val());},
        success: function(data) {


            $("#results").html(JSON.stringify(data, undefined, 2));

        }
    });

}




