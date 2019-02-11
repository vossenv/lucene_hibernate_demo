"use strict";
jQuery(function () {



    $( "#search-button" ).click(function() {
        doSearch();
    });

});


function doSearch(){


    $.ajax({
        url: "/challenges/search",
        type: "GET",
        beforeSend: function(xhr){xhr.setRequestHeader('query', $("#search-bar").val());},
        success: function(data) {

            console.log(data);
            $("#results").text(JSON.stringify(data))

        }
    });

}




