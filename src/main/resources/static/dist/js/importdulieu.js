$(".btn-save-importdulieu_data").click(function() {
    let dataCauHinh = $("#form-import-dulieu").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    $.ajax(
        {
            url: "/api/congtac-pccc/import/pccc",
            method: 'POST',
            data: JSON.stringify(dataCauHinh),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                alert(result);
            }
        });
});