//load du lieu danh muc
//selector_result : id or class element
// $key : key category for data list
// $value_selected : value for selected

var _func_loadDanhMucCallback = ($key, $callback) => {
    let k = ($key !== undefined && $key !== null && $key !== "") ? $key : 0;
    var request_data = {
        type: k,
        start: 0,
        end: 20,
        sortBy: 'id',
        sortType: 'DESC'
    }
    var data = JSON.stringify(request_data);
    $.ajax(
        {
            url: "/api/danhmuc/list",
            method: 'POST',
            data: data,
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                $callback(result);
            }
        });
}

var _func_loadDanhMucForSelectBox = ($key, $callBack) => {
    let k = ($key !== undefined && $key !== null && $key !== "") ? $key : 0;
    var request_data = {
        type: k,
        start: 0,
        end: 20,
        sortBy: 'id',
        sortType: 'DESC'
    }
    var data = JSON.stringify(request_data);
    $.ajax(
        {
            url: "/api/danhmuc/list",
            method: 'POST',
            data: data,
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                $callBack(result);
            }
        });
}

var _func_loadDanhMuc = ($selector_result, $key, $value_selected, $arrayGlobal) => {
    let k = ($key !== undefined && $key !== null && $key !== "") ? $key : 0;
    var request_data = {
        type: k,
        start: 0,
        end: 20,
        sortBy: 'id',
        sortType: 'DESC'
    }
    var data = JSON.stringify(request_data);
    $.ajax(
        {
            url: "/api/danhmuc/list",
            method: 'POST',
            data: data,
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                console.log($value_selected);
                let xhtml = "";
                xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                if(result.data.length > 0) {
                    let xse = "";
                    if($arrayGlobal !== null) {
                        $arrayGlobal = result.data;
                        console.log("load DM "+$arrayGlobal.length);
                    }
                    result.data.forEach(function(x){
                        if($value_selected != null && $value_selected === x.id){
                            xse = "selected";
                        }
                        xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                    });
                }
                if($selector_result !== '') {
                    $($selector_result).html("").html(xhtml);
                }
            }
        });
}
// khai bao khi load giao
var _btn_updateMenu = $(".btnUpdateMenu");
var _btn_save_tab_form = $(".btn-save-tab-4-form");

var _btn_table_linhvucnganhnghehoatdong_addrow = $(".btn-table_linhvucnganhnghehoatdong-addrow");
var _btn_table_linhvucnganhnghehoatdong_delete_row = $(".btn-table_linhvucnganhnghehoatdong-delete-row");

var _btn_table_thamduyetthietkenghiemthu_addrow = $(".btn-table_thamduyetthietkenghiemthu-addrow");
var _btn_table_thamduyetthietkenghiemthu_delete_row = $(".btn-table_thamduyetthietkenghiemthu-delete-row");

var _table_thamduyetthietkenghiemthu = $("#table_thamduyetthietkenghiemthu");
var _loadTab = $(".loadTab");
var _loadTabCCCP = $(".loadTabPCCC");


var data_linhvuchanhnghe = [];
var data_thamdinhthietke = [];
var data_congtaccapnuocchuachayngoainha = [];
var data_thongtinvehangmucnha_congtrinhcoso = [];
var data_congtactuyentruyen_huanluyen = [];
var data_congtackiemtravePCCC = [];
var data_phuongAnPcccCNCH = [];
var data_xayDungPhuongAn = [];
var data_thucTapPhuongAnPcccCuaCongAn = [];
var data_tinhHinhChayNo = [];
var data_nhungthaydoilienquan = [];



// console.log(linhvuchoatdongDanhMuc);
console.log("KET THUC NAP DANH MUC");
//end load danh muc


