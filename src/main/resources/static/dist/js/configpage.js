$(document).ready(function () {
    $("#btnThemMauChuKy").click(function() {
        $("#themMoiMauChuKyClass").modal('toggle');
    });
    $("#signatureTableBody").on("click", '.trangThaiCauHinh', function(){
        let data = $(this).attr("data");
        let jsonData = {
            index : data
        }
        $.ajax({
            url: '/api/switch/trangthai',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(jsonData),
            success: function (data) {
                console.log(data);
            },
            error: function () {
                console.log("Error loading data.");
            }
        });
    });
    loadData();
});

function loadData() {
    $.ajax({
        url: '/api/list/config',
        type: 'GET',
        data: null,
        success: function (data) {
            renderTable(data);
        },
        error: function () {
            console.log("Error loading data.");
        }
    });
}

function renderTable(data) {
    var tableBody = $('#signatureTableBody');
    tableBody.empty();
    var list = data.requestObject.config;
    let xhtml = "";
    if(list !== undefined && list !== null && list.length > 0) {
        for (let i = 0; i < list.length; i++) {
            let item = list[i];
            let checkedzzz = item.trangThaiHoatDong === true ? "checked" : "";
            let uri = "";
            if(item.pathImageSignHubLocalType !== "") {
                uri = item.pathImageSignHubLocalType.replaceAll('\\', '/');
            }
            let imgz = uri === "" ? "" : "<img width=\"100\" height=\"100\" src=\"data:image/jpeg;base64, "+item.pathImageSignHubLocalType+"\" />";

            xhtml += "<tr>" +
                "<td>" + item.nameSignHubLocalType + "</td>" +
                "<td> " + imgz + "</td>" +
                "<td>" + item.showInfoSignHubLocalType + "</td>" +
                "<td>" +
                "<label><input "+checkedzzz+" class=\"trangThaiCauHinh\" data=\""+i+"\" type=\"radio\" name=\"trangThaiCauHinh\" class=\"flat-red\"></label>"+
                "</td>"+
                "</tr>";
        }
    }else {
        xhtml = "<tr><td colspan='4'>Chưa có dữ liệu</td></tr>";
    }
    tableBody.html("").html(xhtml);
}