"use strict";

let ctx = $("#ctx").text().replace(/\/$/, "");


jQuery(function () {
    setListeners();
});


function clickPageNum(pageNum) {
    doSearch(parseInt(pageNum) - 1);
}

function clickNavPage(id, pageCount) {

    let cpage = parseInt($("#current-page").text());

    cpage += (id === "next-page") ? 1 : (-1);
    cpage = (cpage < 1) ? 1 : cpage;
    cpage = (cpage > pageCount) ? pageCount : cpage;

    doSearch(parseInt(cpage) - 1);

}

function buildPageString(pageCount){

    let next = "&nbsp;<span class='pagenum' id='next-page' onClick='clickNavPage(this.id, "+pageCount+")'>next</span>&nbsp;";
    let prev = "&nbsp;<span class='pagenum' id='prev-page' onClick='clickNavPage(this.id,"+pageCount+")'>previous</span>&nbsp;";

    let pagestring = prev;
    for (let i = 1; i < pageCount + 1; i++) {
        pagestring += "&nbsp;<span class='pagenum' id='pageNum-" + i + "' onClick='clickPageNum(this.innerHTML)'>" + i + "</span>&nbsp;";
    }
    return pagestring + next;
}

function doSearch(page_num) {

    $("#first-visit").text("false");
    let query = $("#search-bar").val();
    let page_size = $('#page-size').find(":selected").text();

    let headers = {
        'query': query,
        'size': page_size,
        'page': page_num,
        'Content-Type': 'application/json'
    };

    $.ajax({
        url: ctx + "/challenges/search",
        type: "GET",
        headers: headers,
        success: function (data, status, xhr) {

            $("#results").html(JSON.stringify(data, undefined, 2));

            let cpage = parseInt(xhr.getResponseHeader('Current-Page')) + 1;
            let pagestring = buildPageString(parseInt(xhr.getResponseHeader('Page-Count')));
            let searchTime = xhr.getResponseHeader('Total-Time-Seconds');
            let rowCount = xhr.getResponseHeader('Result-Count');

            $("#paging-div").html(pagestring);
            $("#pageNum-"+ cpage).css('color', '#dc3545');
            $("#current-page").text(cpage);
            $("#stats-div").text(" " + rowCount + " total results.  Search completed in " + Number(searchTime).toFixed(5) + " seconds.")
            $("#search-footer").show();

        }
    });

}



function setListeners(){
    document.getElementById("search-button")
        .addEventListener("click", function(event) {
            event.preventDefault();
            doSearch(0);
        });
    document.getElementById("search-bar")
        .addEventListener("keydown", function(event) {
            if (event.keyCode === 13) {
                event.preventDefault();
                document.getElementById("search-button").click();
            }
        });
    document.getElementById("page-size")
        .addEventListener("change", function() {
            if ($("#first-visit").text() !== "true"){
                doSearch(0);
            }
        });
}