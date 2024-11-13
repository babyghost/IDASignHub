// begin code



$(".btn-save-namhoatdong_modal_form").click(function() {
    let data = $("#namhoatdong_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let $id = $("#namhoatdong_form_hidden_id").val();
    console.log(data);

    data['type'] = 'NAMHOATDONG';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#namhoatdong_form", "#myModalForm_DanhMucNamHoatDong", '#table_namhoatdong_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal
});
$(".table").on('click', '.update-pccc-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");
    window.location = '/hieu-chinh-cong-tac-pccc/'+id;
});
$(".table").on('click', '.delete-pccc-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");

    let clazztr = '.row_'+index;

    // xoa danh muc ajax
    var data1 = {id: id};
    $.ajax(
        {
            url: "/api/congtac-pccc/delete",
            method: 'POST',
            data: JSON.stringify(data1),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                if(result== 'OKE') {
                    $(clazztr).remove();
                } else {
                    alert("Không thể xóa được dữ liệu này. ")
                }

            }
        });

});
$(".table").on('click', '.delete-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");

    let clazztr = '.row_'+index;

    // xoa danh muc ajax
    var data1 = {id: id};
    $.ajax(
        {
            url: "/api/danhmuc/delete",
            method: 'POST',
            data: JSON.stringify(data1),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                if(result== 'OKE') {
                    $(clazztr).remove();
                } else {
                    alert("Không thể xóa được dữ liệu này. ")
                }

            }
        });
});