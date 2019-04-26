"use strict";

let ctx = $("#ctx").text().replace(/\/$/, "");


jQuery(function () {
    setListeners();
});


function clickPageNum(pageNum) {
    doSearch(parseInt(pageNum));
}

function clickNavPage(id, pageCount) {

    let cpage = parseInt($("#current-page").text());

    cpage += (id === "next-page") ? 1 : (-1);
    cpage = (cpage < 1) ? 1 : cpage;
    cpage = (cpage > pageCount) ? pageCount : cpage;

    doSearch(parseInt(cpage));

}

function buildPageString(pageCount) {

    let next = "&nbsp;<span class='pagenum' id='next-page' onClick='clickNavPage(this.id, " + pageCount + ")'>next</span>&nbsp;";
    let prev = "&nbsp;<span class='pagenum' id='prev-page' onClick='clickNavPage(this.id," + pageCount + ")'>previous</span>&nbsp;";

    let pagestring = prev;
    for (let i = 1; i < pageCount + 1; i++) {
        pagestring += "&nbsp;<span class='pagenum' id='pageNum-" + i + "' onClick='clickPageNum(this.innerHTML)'>" + i + "</span>&nbsp;";
    }
    return pagestring + next;
}

function doSearch(page_num) {
    $("#search-footer").hide();
    $("#results").empty();
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
        url: ctx + "/movies/search",
        type: "GET",
        headers: headers,
        success: function (data, status, xhr) {

            if ('_embedded' in data) {
                let r = $("#results");
                $.each(data['_embedded']['data'], function (i, m) {

                    let movie = m['movie'];
                    let d = new Date(+(new Date()) - Math.floor(Math.random() * 10000000000));
                    let infodiv = "<div class='row'><div class='col-md-2'>";

                    r.append("<span class='title'>" + movie['title'] + " <img alt='' class='avatar' src='" + movie['avatar'] + "/>'</div>");
                    r.append("<div class='fieldname'>Description</div>");
                    r.append("<div class='field'>" + movie['description'] + "</div><br/>");

                    infodiv += "<a href='" + movie['cover'] + "' target='_blank'><img class='poster' alt='image' src='" + movie['cover'] + "?" + d + "'></a><br/>";
                    infodiv += "</div><div class='col-sm-10'>";
                    infodiv += "<div class='field'><span class='fieldname'>Rating:</span> " + movie['rating'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Genre:</span> " + movie['genre'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Catch phrase:</span> " + movie['catchPhrase'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Country:</span> " + movie['country'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Producer:</span> " + movie['producer'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Movie ID:</span>&nbsp<a target='_blank' href='" + m['_links']['self']['href'] + "'>" + movie['movieId'] + "</a></div>";
                    infodiv += "<div class='field'><span class='fieldname'>Link:</span> <a target='_blank' href='" + movie['url'] + "'>" + movie['url'] + "</a> </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Added:</span> " + movie['createdDate'] + " </div>";
                    infodiv += "<div class='field'><span class='fieldname'>Last modified:</span> " + movie['lastModifiedDate'] + " </div>";
                    infodiv += "</div></div><br/><br/>";

                    r.append(infodiv);
                    $("#search-footer").show();

                });
            }

            let cpage = parseInt(xhr.getResponseHeader('Current-Page'));
            let pagestring = buildPageString(parseInt(xhr.getResponseHeader('Page-Count')));
            let searchTime = xhr.getResponseHeader('Total-Time-Seconds');
            let rowCount = xhr.getResponseHeader('Result-Count');

            $("#paging-div").html(pagestring);
            $("#pageNum-" + cpage).css('color', '#dc3545');
            $("#current-page").text(cpage);
            $("#stats-div").text(" " + rowCount + " total results.  Search completed in " + Number(searchTime).toFixed(5) + " seconds.");
        },

        error: function (xhr) {
            $("#results").css('color', '#dc3545');
            $("#results").html(JSON.stringify(JSON.parse(xhr.responseText), undefined, 2));

        }
    });
}

function setListeners() {
    document.getElementById("search-button")
        .addEventListener("click", function (event) {
            event.preventDefault();
            doSearch(1);
        });
    document.getElementById("search-bar")
        .addEventListener("keydown", function (event) {
            if (event.keyCode === 13) {
                event.preventDefault();
                document.getElementById("search-button").click();
            }
        });
    document.getElementById("page-size")
        .addEventListener("change", function () {
            if ($("#first-visit").text() !== "true") {
                doSearch(1);
            }
        });
}