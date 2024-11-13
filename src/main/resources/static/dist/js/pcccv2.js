// TAB 1 table add row
_btn_table_linhvucnganhnghehoatdong_addrow.click(function() {
    let stt = data_linhvuchanhnghe.length + 1;
    let item = {
        input_col_one_name: 'select_linhvucnganhnghehoatdong_'+stt,
        input_col_two_name: 'checkbox_linhvucnganhnghehoatdong_'+stt
    };
    data_linhvuchanhnghe.push(item);
    _func_loadTableFormLinhVucNganhNgheHoatDong('#table_linhvucnganhnghehoatdong_body', item, data_linhvuchanhnghe.length);
    _func_loadDanhMuc('.table_linhvucnganhnghehoatdon', 'NGANHNGHEHOATDONG', null);
    $("#table_linhvucnganhnghehoatdong_body").attr("data", data_linhvuchanhnghe.length);
});
_btn_table_linhvucnganhnghehoatdong_delete_row.click(function() {
    let stt = data_linhvuchanhnghe.length + 1;
    data_linhvuchanhnghe.splice(-1);
    let data = $("#table_linhvucnganhnghehoatdong_body").attr("data");
    $("#table_linhvucnganhnghehoatdong_body").attr('data', data - 1);
    $(".table_linhvucnganhnghehoatdong_"+data).remove();
});
_btn_table_thamduyetthietkenghiemthu_addrow.click(function() {
    console.log("btn-table_thamduyetthietkenghiemthu-addrow");
    let stt = data_thamdinhthietke.length + 1;
    let item = {
        input_col_one_name: 'tab_1_table_thamduyetthietkenghiemthu_tenvanban_'+stt,
        input_col_two_name: 'tab_1_table_thamduyetthietkenghiemthu_sovanban_'+stt,
        input_col_three_name: 'tab_1_table_thamduyetthietkenghiemthu_ngaycap_'+stt,
        input_col_four_name: 'tab_1_table_thamduyetthietkenghiemthu_coquancap_'+stt,
        input_col_five_name: 'tab_1_table_thamduyetthietkenghiemthu_ghichu_'+stt,
    };
    data_thamdinhthietke.push(item);
    _func_loadTableThamDuyet('#table_thamdinhthietkenghiemthu_body', item, data_thamdinhthietke.length);
    $("#table_thamdinhthietkenghiemthu_body").attr("data", data_thamdinhthietke.length);
});
_btn_table_thamduyetthietkenghiemthu_delete_row.click(function() {
    let stt = data_thamdinhthietke.length + 1;
    data_thamdinhthietke.splice(-1);
    let data = $("#table_thamdinhthietkenghiemthu_body").attr("data");
    $("#table_thamdinhthietkenghiemthu_body").attr('data', data - 1);
    $(".table_thamdinhthietkenghiemthu_"+data).remove();
});
//end tab 1
//end code

// update menu
_btn_updateMenu.click(function(){
    $.ajax(
        {
            url: "/api/add/menu",
            method: 'POST',
            success: function(result){
                alert("Update thành công ... ");
            }
        });
});
// load tab
_loadTabCCCP.click(function(){
    console.log("_loadTabCCCP");
    let page_type = $(this).attr("data-tabvalue");
    var id = $("#page_pccc_id").val();
    _loadDataPcccForEditPage(id, page_type);
});
_loadTab.click(function(){
    let type = $(this).attr("data-tabvalue");
    if(type === "DIENCOSO") {
        _func_loadTableDanhMuc("#table_diencoso_body", "DIENCOSO");
    }else if(type === "CAPQUANLY") {
        _func_loadTableDanhMuc("#table_capquanly_body", "CAPQUANLY");
    } else if(type === "HINHTHUCDAUTU") {
        _func_loadTableDanhMuc("#table_hinhthucdautu_body", "HINHTHUCDAUTU");
    } else if(type === "THANHPHANKINHTE") {
        _func_loadTableDanhMuc("#table_thanhphankinhte_body", "THANHPHANKINHTE");
    } else if(type === "NGANHNGHEHOATDONG") {
        _func_loadTableDanhMuc("#table_nganhnghehoatdong_body", "NGANHNGHEHOATDONG");
    }else if(type === "KHUVUC") {
        _func_loadTableDanhMuc("#table_khuvuc_body", "KHUVUC");
    }else if(type === "NAMHOATDONG") {
        _func_loadTableDanhMuc("#table_namhoatdong_body", "NAMHOATDONG");
    }
});

// action save form 1 tab1
var _btn_submit = $(".btn-save-tab-1-form1");
_btn_submit.click(function() {
    let dataForm1 = $("#tab1_form_1").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let dataForm3a = $("#tab-1-form-3-a").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let dataForm4 = $("#tab-1-form-4").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    // let dataForm5 = $("#tab-1-form-5").serializeArray().reduce(function(obj, item) {
    //     obj[item.name] = item.value;
    //     return obj;
    // }, {});
    let dataForm6 = $("#tab-1-form-6").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let dataForm6_2 = $("#tab-1-form-6_2").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let danhSachLinhVucNganhNgheHoatDong = [];
    let so_row = $("#table_linhvucnganhnghehoatdong_body").attr("data");
    for(let i = 1; i <= so_row; i++) {
        let select_linhvuc = 'select_linhvucnganhnghehoatdong_'+i;
        let value_select = $("#"+select_linhvuc).val();
        let checkboxganhnghechinh = 'checkbox_linhvucnganhnghehoatdong_'+i;
        let value_check = $("#"+checkboxganhnghechinh).is(":checked") ? true : false;
        let obj = {
            input_col_one_name : select_linhvuc,
            input_col_one_value : value_select,
            input_col_two_name : checkboxganhnghechinh,
            input_col_two_value : value_check
        }
        danhSachLinhVucNganhNgheHoatDong.push(obj);
    }
    let danhSachThamDuyetThietKeNghiemThu = [];
    let so_row_thamduyet = $("#table_thamdinhthietkenghiemthu_body").attr("data");
    for(let i = 1; i <= so_row_thamduyet; i++) {
        let tenVanBan = 'tab_1_table_thamduyetthietkenghiemthu_tenvanban_'+i;
        let tenVanBanValue = $("#"+tenVanBan).val();

        let soVanBan = 'tab_1_table_thamduyetthietkenghiemthu_sovanban_'+i;
        let soVanBanValue = $("#"+soVanBan).val();

        let ngayCap = 'tab_1_table_thamduyetthietkenghiemthu_ngaycap_'+i;
        let ngayCapValue = $("#"+ngayCap).val();

        let coQuanCap = 'tab_1_table_thamduyetthietkenghiemthu_coquancap_'+i;
        let coQuanCapValue = $("#"+coQuanCap).val();

        let ghiChu = 'tab_1_table_thamduyetthietkenghiemthu_ghichu_'+i;
        let ghiChuValue = $("#"+ghiChu).val();

        let obj = {
            input_col_one_name : tenVanBan,
            input_col_one_value : tenVanBanValue,
            input_col_two_name : soVanBan,
            input_col_two_value : soVanBanValue,
            input_col_three_name : ngayCap,
            input_col_three_value : ngayCapValue,
            input_col_four_name : coQuanCap,
            input_col_four_value : coQuanCapValue,
            input_col_five_name : ghiChu,
            input_col_five_value : ghiChuValue
        }
        danhSachThamDuyetThietKeNghiemThu.push(obj);
    }
    let dataForm3 = $("#tab-1-form-3").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let dataForm2_b = $("#form_2_b").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let checkedSlect = [];
    let arrayThanhPhanDuocChon = [];
    $("input:checkbox[name='chkDaNhan[]']:checked").each(function() {
        checkedSlect.push($(this).val());
    });
    checkedSlect.forEach(function(r) {
        let thanhPhanOb = {id : r };
        let fileInput = 'fileId_'+r;
        let arr =  document.getElementsByClassName(fileInput);
        let data_arr = [];
        for(let i = 0; i < arr.length; i++) {
            data_arr.push(arr[i].value);
        }
        thanhPhanOb['fileIds'] = data_arr;
        arrayThanhPhanDuocChon.push(thanhPhanOb);
    });

    var formData = Object.assign(dataForm1,dataForm2_b, dataForm3a, dataForm3, dataForm4, dataForm6, dataForm6_2);
    let selectedNamStr = $("#tab_1_namduavaohoatdong").find(":selected").text();
    let selectedKhuvucStr = $("#tab_1_diabankhuvuctrongdiem").find(":selected").text();
    formData["tab_1_namduavaohoatdongStr"] = selectedNamStr;
    formData["tab_1_diabankhuvuctrongdiemStr"] = selectedKhuvucStr;
    formData["thanhPhanHoSo"] = arrayThanhPhanDuocChon;
    formData['danhSachLinhVucNganhNgheHoatDong'] = danhSachLinhVucNganhNgheHoatDong;
    formData['danhSachThamDuyetThietKeNghiemThu'] = danhSachThamDuyetThietKeNghiemThu;
    console.log(formData);
    _func_save_tam(formData, "TAB_1", null);
});

// TAB_2
var _btn_tag_2_submit = $(".btn-save-tab-2-form");
_btn_tag_2_submit.click(function() {
    $id = $("#page_pccc_id").val();
    let dataForm2 = $("#tab2_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    _func_save_tam(dataForm2, 'TAB_2', $id);
});
// TAB_3
// luu thay doi
var _btn_tag_3_luu_thay_doi = $(".btn-luu-thay-doi-tab-3-form");
_btn_tag_3_luu_thay_doi.click(function(){
    console.log("_btn_tag_3_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm4 = {
        'data_congtaccapnuocchuachayngoainha' : data_congtaccapnuocchuachayngoainha
    }
    _func_save_tam(dataForm4,"TAB_3", id);
});
//-- Nut luu modal form
var _btn_tag_3_submit = $(".btn-save-tab-3-form");
_btn_tag_3_submit.click(function() {
    console.log("_btn_tag_3_submit");
    let id = $("#page_pccc_id").val();
    let type_form = $("#Tab_3_Form_Modal_Type").val();
    let index = $("#Tab_3_Form_Modal_Index").val();
    let dataForm3 = $("#tab3_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let selectedStr = $("#tab_3_tinhtranghoatdong").find(":selected").text();
    dataForm3['tab_3_tinhtranghoatdongStr'] = selectedStr;
    if(type_form == "HIEU_CHINH") {
        data_congtaccapnuocchuachayngoainha[(index - 1)] = dataForm3;

    } else {
        data_congtaccapnuocchuachayngoainha.push(dataForm3);
        console.log(data_congtaccapnuocchuachayngoainha);
    }
    _func_loadTableFormCongTacCapNuocChuaChayNgoaiNha('#tab_3_table_thongtincapnuocchuachayngoainha_body', data_congtaccapnuocchuachayngoainha);
    $("#tab3_form_modal")[0].reset();
    $('#myModalForm3_Modal').modal('toggle');
});
//---- End TAB_3
//----TAB_4
var _btn_tag_4_submit = $(".btn-save-tab-4-form");
_btn_tag_4_submit.click(function() {
    console.log("_btn_tag_4_submit");
    let id = $("#page_pccc_id").val();

    let dataForm4 = $("#tab4_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let type_form = $("#Tab_4_Form_Modal_Type").val();
    let index = $("#Tab_4_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_thongtinvehangmucnha_congtrinhcoso[(index - 1)] = dataForm4;

    } else {
        data_thongtinvehangmucnha_congtrinhcoso.push(dataForm4);
    }

    _func_loadTableFormThongTinveHangMucNhaCongTrinhCoSo('#tab_4_table_body', data_thongtinvehangmucnha_congtrinhcoso);
    $("#tab4_form_modal")[0].reset();
    $('#myModalForm4_Modal').modal('toggle');
});

var _btn_tag_4_luu_thay_doi = $(".btn-luu-thay-doi-tab-4-form");
_btn_tag_4_luu_thay_doi.click(function(){
    console.log("_btn_tag_4_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm4 = {
        'data_thongtinvehangmucnha_congtrinhcoso' : data_thongtinvehangmucnha_congtrinhcoso
    }
    _func_save_tam(dataForm4,"TAB_4", id);
});
// ---END TAB_4
//----TAB_5
var _btn_tag_5_submit = $(".btn-save-tab-5-form");
_btn_tag_5_submit.click(function() {
    let id = $("#page_pccc_id").val();
    let dataForm5 = $("#tab5_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let type_form = $("#Tab_5_Form_Modal_Type").val();
    let index = $("#Tab_5_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_congtactuyentruyen_huanluyen[(index - 1)] = dataForm5;

    } else {
        data_congtactuyentruyen_huanluyen.push(dataForm5);
    }
    _func_loadTableFormCongTacTuyenTruyenHuanLuyen('#tab_5_table_body', data_congtactuyentruyen_huanluyen);
    $("#tab5_form_modal")[0].reset();
    $('#myModalForm5_Modal').modal('toggle');
});

var _btn_tag_5_luu_thay_doi = $(".btn-luu-thay-doi-tab-5-form");
_btn_tag_5_luu_thay_doi.click(function(){
    let id = $("#page_pccc_id").val();
    let dataForm5 = {
        'data_congtactuyentruyen_huanluyen' : data_congtactuyentruyen_huanluyen
    }
    _func_save_tam(dataForm5,"TAB_5", id);
});
// ---END TAB_5
//----TAB_6
var _btn_tag_6_submit = $(".btn-save-tab-6-form");
_btn_tag_6_submit.click(function() {
    console.log("_btn_tag_6_submit");
    let id = $("#page_pccc_id").val();
    console.log(id);
    let dataForm6 = $("#tab6_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let selectedStr = $("#tab_6_hinhthuckiemtra").find(":selected").text();
    dataForm6['tab_6_hinhthuckiemtraStr'] = selectedStr;
    let ds_classNameFile = document.getElementsByClassName('fileId_vanbankiennghi');

    let type_form = $("#Tab_6_Form_Modal_Type").val();
    let index = $("#Tab_6_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_congtackiemtravePCCC[(index - 1)] = dataForm6;

    } else {
        data_congtackiemtravePCCC.push(dataForm6);
    }
    _func_loadTableFormCongTacKiemTraPCCC('#tab_6_table_body', data_congtackiemtravePCCC);
    $("#tab6_form_modal")[0].reset();
    $("#filelist_vanbankiennghi").empty();
    $('#myModalForm6_Modal').modal('toggle');
});

var _btn_tag_6_luu_thay_doi = $(".btn-luu-thay-doi-tab-6-form");
_btn_tag_6_luu_thay_doi.click(function(){
    console.log("_btn_tag_6_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm6 = {
        'data_congtackiemtravePCCC' : data_congtackiemtravePCCC
    }

    _func_save_tam(dataForm6,"TAB_6", id);
});
// ---END TAB_6
//----TAB_7
var _btn_tag_7_submit = $(".btn-save-tab-7-form");
_btn_tag_7_submit.click(function() {
    let id = $("#page_pccc_id").val();
    let dataForm7 = $("#tab7_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let type_form = $("#Tab_7_Form_Modal_Type").val();
    let index = $("#Tab_7_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_phuongAnPcccCNCH[(index - 1)] = dataForm7;

    } else {
        data_phuongAnPcccCNCH.push(dataForm7);
    }
    _func_loadTableFormPhuongAnPCCC('#tab_7_table_body', data_phuongAnPcccCNCH);
    $("#tab7_form_modal")[0].reset();
    $('#myModalForm7_Modal').modal('toggle');
});

var _btn_tag_7_luu_thay_doi = $(".btn-luu-thay-doi-tab-7-form");
_btn_tag_7_luu_thay_doi.click(function(){
    console.log("_btn_tag_7_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm7 = {
        'data_phuongAnPcccCNCH' : data_phuongAnPcccCNCH
    }

    _func_save_tam(dataForm7,"TAB_7", id);
});
// ---END TAB_7
// --- TAB_8
var _btn_tag_8_submit_2 = $(".btn-save-tab-8-form-modal_thuctapphuongan");
var _btn_tag_8_submit_1 = $(".btn-save-tab-8-form-modal_xaydungphuongan");

_btn_tag_8_submit_1.click(function() {
    console.log("btn-save-tab-8-form-modal1");
    let id = $("#page_pccc_id").val();
    console.log(id);
    let dataForm8 = $("#tab8-modal1_form2").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let type_form = $("#Tab_8_Form_XayDungPhuongAnPccc_Modal_Type").val();
    let index = $("#Tab_8_Form_XayDungPhuongAnPccc_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_xayDungPhuongAn[(index - 1)] = dataForm8;

    } else {
        data_xayDungPhuongAn.push(dataForm8);
    }
    _func_loadTableFormXayDungPhuongAn('#tab_8_table_xaydungphuongan_body', data_xayDungPhuongAn);
    $("#tab8-modal1_form2")[0].reset();
    $('#tab8_form_modal_xaydungphuongan').modal('toggle');
});
_btn_tag_8_submit_2.click(function() {
    console.log("_btn_tag_8_submit_2");
    let id = $("#page_pccc_id").val();
    console.log(id);
    let dataForm82 = $("#tab8_thuctapphupngan_form").serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let type_form = $("#Tab_8_Form_ThucTapPhuongAnPccc_Modal_Type").val();
    let index = $("#Tab_8_Form_ThucTapPhuongAnPccc_Modal_Index").val();
    if (type_form == "HIEU_CHINH") {
        data_thucTapPhuongAnPcccCuaCongAn[(index - 1)] = dataForm82;

    } else {
        data_thucTapPhuongAnPcccCuaCongAn.push(dataForm82);
    }
    _func_loadTableFormThucTapPhuongAnChuaChayVaCuuHoCuuNan('#tab_8_table_thuctapphuongan_body', data_thucTapPhuongAnPcccCuaCongAn);
    $("#tab8_thuctapphupngan_form")[0].reset();
    $('#tab8_form_modal_thuctapphuongan').modal('toggle');
});

var _btn_tag_8_luu_thay_doi = $(".btn-luu-thay-doi-tab-8-form");
_btn_tag_8_luu_thay_doi.click(function () {
    console.log("_btn_tag_8_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm8 = {
        'data_thucTapPhuongAnPcccCuaCongAn' : data_thucTapPhuongAnPcccCuaCongAn,
        'data_xayDungPhuongAn' : data_xayDungPhuongAn
    }

    _func_save_tam(dataForm8,"TAB_8", id);
});

// TAB_9
var _btn_tag_9_submit = $(".btn-save-tab-9-form");
_btn_tag_9_submit.click(function() {
    console.log("_btn_tag_9_submit");
    let id = $("#page_pccc_id").val();
    let dataForm9 = $("#tab9_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let type_form = $("#Tab_9_Form_Modal_Type").val();
    let index = $("#Tab_9_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_tinhHinhChayNo[(index - 1)] = dataForm9;

    } else {
        data_tinhHinhChayNo.push(dataForm9);
    }
    // data_tinhHinhChayNo.push(dataForm9);
    _func_loadTableFormTinhHinhChayNo('#tab_9_table_congtackiemtrapccc-body', data_tinhHinhChayNo);
    $("#tab9_form_modal")[0].reset();
    $('#myModalForm9_Modal').modal('toggle');
});

var _btn_tag_9_luu_thay_doi = $(".btn-luu-thay-doi-tab-9-form");
_btn_tag_9_luu_thay_doi.click(function(){
    console.log("_btn_tag_9_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm9 = {
        'data_tinhHinhChayNo' : data_tinhHinhChayNo
    }

    _func_save_tam(dataForm9,"TAB_9", id);
});
// ---END TAB_9
// TAB_10
var _btn_tag_10_submit = $(".btn-save-tab-10-form");
_btn_tag_10_submit.click(function() {
    let id = $("#page_pccc_id").val();
    let dataForm10 = $("#tab10_form_modal").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let type_form = $("#Tab_10_Form_Modal_Type").val();
    let index = $("#Tab_10_Form_Modal_Index").val();
    if(type_form == "HIEU_CHINH") {
        data_nhungthaydoilienquan[(index - 1)] = dataForm10;
    } else {
        data_nhungthaydoilienquan.push(dataForm10);
    }
    // data_nhungthaydoilienquan.push(dataForm10);
    _func_loadTableFormThayDoiLienQuan('#tab_10_table_thongtinthaydoi_body', data_nhungthaydoilienquan);
    $("#tab10_form_modal")[0].reset();
    $('#myModalForm10_Modal').modal('toggle');
});

var _btn_tag_10_luu_thay_doi = $(".btn-luu-thay-doi-tab-10-form");
_btn_tag_10_luu_thay_doi.click(function(){
    console.log("_btn_tag_10_luu_thay_doi");
    let id = $("#page_pccc_id").val();
    let dataForm10 = {
        'data_nhungthaydoilienquan' : data_nhungthaydoilienquan
    }
    _func_save_tam(dataForm10,"TAB_10", id);
});
var _btn_save_cauhinhthanhphanhoso_modal_form = $(".btn-save-cauhinhthanhphanhoso_data");
_btn_save_cauhinhthanhphanhoso_modal_form.click(function() {
    let dataCauHinh = $("#thanhphanhoso_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let ds_classNameFile = document.getElementsByClassName('fileId_cauHinhThanhPhan');
    let arr_fileID = [];
    for(let i =0; i < ds_classNameFile.length; i++) {
        arr_fileID.push(ds_classNameFile[i].value);
    }
    _func_save_CauHinh(dataCauHinh, null);
});
// ---END TAB_9M

//SAVE DANH MUC

//Luu modal form dien co so

$(".btn-save-doimatkhau_modal_form").click(function() {
    let data = $("#thaydoimatkhau_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    $.ajax(
        {
            url: "/api/update/password",
            method: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            dataType: "html",
            success: function(result){
                console.log(result);
                if(result == '200') {
                    alert("Lưu dữ liệu thành công");
                    $("#thaydoimatkhau_form")[0].reset();
                    $('#myModalForm_ThayDoiMatKhau').modal('toggle');
                } else {
                    alert("Lưu dữ liệu không thành công, kiểm tra lại mật khẩu cũ");
                    $("#thaydoimatkhau_form")[0].reset();
                }
            },
            error: function(e) {
                console.log(e);
            }
        });
});

$(".btn_save_user").click(function() {
    let data = $("#form-add-user").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    $.ajax(
        {
            url: "/api/add/user",
            method: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            dataType: "html",
            success: function(result){
                console.log(result);
                alert("Lưu dữ liệu thành công");
                $("#form-add-user")[0].reset();
            },
            error: function(e) {
                console.log(e);
            }
         });
});

$(".btn-save-diencoso_modal_form").click(function() {
    let data = $("#diencoso_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let $id = $("#diencoso_form_hidden_id").val();
    console.log(data);
    data['type'] = 'DIENCOSO';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#diencoso_form", "#myModalForm_DanhMucDienCoSo", '#table_diencoso_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal

});
$(".btn-save-capquanly_modal_form").click(function() {
    let data = $("#capquanly_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let $id = $("#capquanly_form_hidden_id").val();
    data['type'] = 'CAPQUANLY';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#capquanly_form", "#myModalForm_DanhMucCapQuanLy", '#table_capquanly_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal
});
$(".btn-save-hinhthucdautu_modal_form").click(function() {
    let data = $("#hinhthucdautu_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let $id = $("#hinhthucdautu_form_hidden_id").val();
    console.log(data);
    data['type'] = 'HINHTHUCDAUTU';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#hinhthucdautu_form", "#myModalForm_DanhMucHinhThucDauTu", '#table_hinhthucdautu_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal
});
$(".btn-save-nganhnghehoatdong_modal_form").click(function() {
    let data = $("#nganhnghehoatdong_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});

    let $id = $("#nganhnghehoatdong_form_hidden_id").val();
    console.log(data);
    data['type'] = 'NGANHNGHEHOATDONG';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#nganhnghehoatdong_form", "#myModalForm_DanhMucNganhNgheHoatDong", '#table_nganhnghehoatdong_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal
});
$(".btn-save-thanhphankinhte_modal_form").click(function() {
    let data = $("#thanhphankinhte_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let $id = $("#thanhphankinhte_form_hidden_id").val();
    console.log(data);
    data['type'] = 'THANHPHANKINHTE';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#thanhphankinhte_form", "#myModalForm_DanhMucThanhPhanKinhTe", '#table_thanhphankinhte_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal
});
$(".btn-save-khuvuc_modal_form").click(function() {
    let data = $("#khuvuc_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let $id = $("#khuvuc_form_hidden_id").val();
    console.log(data);
    data['type'] = 'KHUVUC';
    if($id !== undefined && $id !== null && $id !== "") {
        data['id'] = $id;
    }
    _func_save_danhmuc(data, $id, "#khuvuc_form", "#myModalForm_DanhMucKhuVuc", '#table_khuvuc_body'); // data = data luu, $id = id cua du lieu truong hop update, id form de reset form, id modal de close modal

});

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
//END SAVE DANH MUC
// DELETE DANH MUC
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
// HIEU CHINH DANH MUC
$(".table").on('click', '.update-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");
    // xoa danh muc ajax
    var data1 = {id: id};

    $.ajax(
        {
            url: "/api/danhmuc/getData",
            method: 'POST',
            data: JSON.stringify(data1),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                if(result != null) {
                    $("#tenhieuchinh").val(result.ten);
                    $("#mahieuchinh").val(result.ma);
                    $("#idhieuchinh").val(result.id);
                    $("#typehieuchinh").val(result.type);
                    $("#index").val(index);
                    $('.myModalFormUpdateDanhMucClz').modal('toggle');
                }
            }
        });

});
$(".btn-save-update-danhmuc").click(function(){
    let dataForm = $("#form-update-danhmuc").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    console.log(dataForm);
    $.ajax(
        {
            url: "/api/danhmuc/hieuchinh",
            method: 'POST',
            data: JSON.stringify(dataForm),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                if(result== 'OKE') {
                    let tenzz = '.td_row_ten_'+dataForm['index'];
                    let mazz = '.td_row_ma_'+dataForm['index'];
                    $(tenzz).html(dataForm['tenhieuchinh']);
                    $(mazz).html(dataForm['mahieuchinh']);
                    $("#form-update-danhmuc")[0].reset();
                    $(".myModalFormUpdateDanhMucClz").modal('toggle');
                } else {
                    alert("Dữ liệu không thể hiệu chỉnh")
                }
            }
        });
});


// mở modal form

$(".btn-open-modal-thaydoimatkhay").click(function() {
    $('#myModalForm_ThayDoiMatKhau').modal('toggle');
});
$(".btn_tab10_addModalDataforRow").click(function() {
    $("#Tab_10_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm10_Modal').modal('toggle');
});
$(".btn_tab9_addModalDataforRow").click(function() {
    $("#Tab_9_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm9_Modal').modal('toggle');
});
$(".btn_tab7_addModalDataforRow").click(function() {
    $("#Tab_7_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm7_Modal').modal('toggle');
});
$(".btn_tab6_addModalDataforRow").click(function() {
    $("#Tab_6_Form_Modal_Type").val("THEM_MOI");

    $('#myModalForm6_Modal').modal('toggle');
});
$(".btn_tab5_addModalDataforRow").click(function() {
    $("#Tab_5_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm5_Modal').modal('toggle');
});
$(".btn_tab4_addModalDataforRow").click(function() {
    $("#Tab_4_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm4_Modal').modal('toggle');
});
$(".btn_tab3_addModalDataforRow").click(function() {
    $("#Tab_3_Form_Modal_Type").val("THEM_MOI");
    $('#myModalForm3_Modal').modal('toggle');
});
// --- TAB_8
$(".btn-table_phuongan-pccc-cnch-addrow").click(function() {
    $("#Tab_8_2_Form_Modal_Type").val("THEM_MOI");
    $('#tab8_form_modal_xaydungphuongan').modal('toggle');
});
$(".btn-table_thuctap-phuongan-pccc-cnch-addrow").click(function() {
    $('#tab8_form_modal_thuctapphuongan').modal('toggle');
});
$(".btn-add-form-diencoso-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucDienCoSo').modal('toggle');
});
$(".btn-add-form-capquanly-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucCapQuanLy').modal('toggle');
});
$(".btn-add-form-hinhthucdautu-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucHinhThucDauTu').modal('toggle');
});
$(".btn-add-form-nganhnghehoatdong-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucNganhNgheHoatDong').modal('toggle');
});
$(".btn-add-form-thanhphankinhte-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucThanhPhanKinhTe').modal('toggle');
});
$(".btn-add-form-khuvuc-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucKhuVuc').modal('toggle');
});
$(".btn-add-form-namhoatdong-content").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_DanhMucNamHoatDong').modal('toggle');
});
/*CAU HINH PCCC*/
$(".btn-save-cauhinhthanhphanhoso_modal_form").click(function() {
    // $("#diencoso_form")[0].reset();
    $('#myModalForm_CauHinhThanhPhanHoSo').modal('toggle');
});

$(".table").on('click', ".update-tab3-function", function(){
    console.log("update-tab3-function");
    let i = $(this).attr("index");
    console.log(data_congtaccapnuocchuachayngoainha[(i - 1)]);
    let ob = data_congtaccapnuocchuachayngoainha[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                console.log("xu ly checkbox");
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                console.log("xu ly select");
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-3-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_3_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_3_Form_Modal_Index").val(i);
    $('#myModalForm3_Modal').modal('toggle');

});
$(".table").on('click', '.delete-tab3-function', function(){
    console.log("delete-tab3-function");
    let i = $(this).attr("index");
    console.log(data_congtaccapnuocchuachayngoainha[(i - 1)]);
    data_congtaccapnuocchuachayngoainha.splice((i - 1), 1);
    _func_loadTableFormCongTacCapNuocChuaChayNgoaiNha('#tab_3_table_thongtincapnuocchuachayngoainha_body', data_congtaccapnuocchuachayngoainha);
});

$(".table").on('click', ".update-tab4-function", function(){
    let i = $(this).attr("index");
    let ob = data_thongtinvehangmucnha_congtrinhcoso[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                console.log("xu ly checkbox");
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                console.log("xu ly select");
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-4-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_4_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_4_Form_Modal_Index").val(i);
    $('#myModalForm4_Modal').modal('toggle');

});
$(".table").on('click', '.delete-tab4-function', function(){
    let i = $(this).attr("index");
    data_thongtinvehangmucnha_congtrinhcoso.splice((i - 1), 1);
    _func_loadTableFormThongTinveHangMucNhaCongTrinhCoSo('#tab_4_table_body', data_thongtinvehangmucnha_congtrinhcoso);
});

$(".table").on('click', ".update-tab5-function", function(){
    let i = $(this).attr("index");
    let ob = data_congtactuyentruyen_huanluyen[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                console.log("xu ly checkbox");
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                console.log("xu ly select");
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-5-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_5_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_5_Form_Modal_Index").val(i);
    $('#myModalForm5_Modal').modal('toggle');

});
$(".table").on('click', '.delete-tab5-function', function(){
    let i = $(this).attr("index");
    data_congtactuyentruyen_huanluyen.splice((i - 1), 1);
    _func_loadTableFormCongTacTuyenTruyenHuanLuyen('#tab_5_table_body', data_congtactuyentruyen_huanluyen);
});

$(".table").on('click', ".update-tab6-function", function(){
    let i = $(this).attr("index");
    let ob = data_congtackiemtravePCCC[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)) {
            if ($("#" + key).get(0).nodeName == 'INPUT') {
                console.log("xu ly checkbox");
                if (ob[key] == 'on') {
                    $("#" + key).prop("checked", true);
                } else {
                    $("#" + key).val(ob[key]);
                }
            } else if ($("#" + key).get(0).nodeName == 'SELECT') {
                console.log("xu ly select");
                let dataType = $("#" + key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function (result) {
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if (result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function (x) {
                            console.log("x.id = " + x.id + " - " + ob[key]);
                            if (ob[key] != null && ob[key] == x.id) {
                                xse = "selected";
                            }
                            xhtml += '<option value="' + x.id + '" ' + xse + ' >' + x.ten + '</option>';
                        });
                        $("#" + key).html(xhtml);
                    }
                });
            } else if ($("#" + key).get(0).nodeName == 'TEXTAREA') {
                $("#" + key).html(ob[key]);
            }
        } else if(key == 'fileId') {
            var  htmlFile = '<div class="input-container">';
            htmlFile+='<input type="hidden" class="fileId" name="fileId" value="'+ob["fileId"]+'">';
            htmlFile+='<input type="hidden" class="fileURL" name="fileURL" value="'+ob["fileURL"]+'">';
            htmlFile+='<input type="hidden" class="fileName" name="fileName" value="'+ob["fileName"]+'">';
            htmlFile+='<a href="'+ob["fileURL"]+'" title="Click để tải file đính kèm">- ' +ob["fileName"]+ '</a>';
            htmlFile+= '<span class="btn-del-file file_delete-vanbankiennghi" attachmentid="'+ob["fileId"]+'" data-file="'+ob["fileId"]+'" style="color: #151111; cursor: pointer;">Xóa</span>';
            htmlFile+= '<div>';
            $("#filelist_vanbankiennghi").empty().html(htmlFile);
            $('.pccc-fileuploader-items_vanbankiennghi').show();
        }
    });
    $(".btn-save-tab-6-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_6_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_6_Form_Modal_Index").val(i);
    $('#myModalForm6_Modal').modal('toggle');

});

$("#filelist_vanbankiennghi").on('click', '.file_delete-vanbankiennghi', function(){
   console.log("vanbankiennghi");
    $("#filelist_vanbankiennghi").html("");
});
$(".table").on('click', '.delete-tab6-function', function(){
    let i = $(this).attr("index");
    data_congtackiemtravePCCC.splice((i - 1), 1);
    _func_loadTableFormCongTacKiemTraPCCC('#tab_6_table_body', data_congtackiemtravePCCC);
});
$(".table").on('click', ".update-tab7-function", function(){
    let i = $(this).attr("index");
    let ob = data_phuongAnPcccCNCH[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-7-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_7_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_7_Form_Modal_Index").val(i);
    $('#myModalForm7_Modal').modal('toggle');

});
$(".table").on('click', '.delete-tab7-function', function(){
    let i = $(this).attr("index");
    data_phuongAnPcccCNCH.splice((i - 1), 1);
    _func_loadTableFormPhuongAnPCCC('#tab_7_table_body', data_phuongAnPcccCNCH);
});

$(".table").on('click', ".update_tab8_xaydungphuongan_function", function(){
    console.log("update_tab8_xaydungphuongan_function");
    let i = $(this).attr("index");
    let ob = data_xayDungPhuongAn[(i - 1)];
    let array_key = Object.keys(ob);
    console.log(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-8-form-modal_xaydungphuongan").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_8_Form_XayDungPhuongAnPccc_Modal_Type").val("HIEU_CHINH");
    $("#Tab_8_Form_XayDungPhuongAnPccc_Modal_Index").val(i);
    $('#tab8_form_modal_xaydungphuongan').modal('toggle');

});

$(".table").on('click', '.delete_tab8_xaydungphuongan_function', function(){
    let i = $(this).attr("index");
    data_xayDungPhuongAn.splice((i - 1), 1);
    _func_loadTableFormXayDungPhuongAn('#tab_8_table_xaydungphuongan_body', data_xayDungPhuongAn);
});


//--------------------------------------------------------------------
$(".table").on('click', ".update_tab8_thuctapphuongan_function", function(){
    console.log("update_tab8_thuctapphuongan_function");
    let i = $(this).attr("index");
    let ob = data_thucTapPhuongAnPcccCuaCongAn[(i - 1)];
    let array_key = Object.keys(ob);
    console.log(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-8-form-modal_thuctapphuongan").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_8_Form_ThucTapPhuongAnPccc_Modal_Type").val("HIEU_CHINH");
    $("#Tab_8_Form_ThucTapPhuongAnPccc_Modal_Index").val(i);
    $('#tab8_form_modal_thuctapphuongan').modal('toggle');

});
$(".table").on('click', '.delete_tab8_thuctapphuongan_function', function(){
    let i = $(this).attr("index");
    data_thucTapPhuongAnPcccCuaCongAn.splice((i - 1), 1);
    _func_loadTableFormThucTapPhuongAnChuaChayVaCuuHoCuuNan('#tab_8_table_thuctapphuongan_body', data_thucTapPhuongAnPcccCuaCongAn);
});
//-----------------------------


$(".table").on('click', ".update-tab9-function", function(){
    let i = $(this).attr("index");
    let ob = data_tinhHinhChayNo[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-9-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_9_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_9_Form_Modal_Index").val(i);
    $('#myModalForm9_Modal').modal('toggle');
});

$(".table").on('click', '.delete-tab9-function', function(){
    let i = $(this).attr("index");
    data_tinhHinhChayNo.splice((i - 1), 1);
    _func_loadTableFormTinhHinhChayNo('#tab_9_table_congtackiemtrapccc-body', data_tinhHinhChayNo);
});

$(".table").on('click', ".update-tab10-function", function(){
    let i = $(this).attr("index");
    let ob = data_nhungthaydoilienquan[(i - 1)];
    let array_key = Object.keys(ob);
    array_key.forEach(function(key, index){
        if(document.querySelector("#"+key)){
            if($("#"+key).get(0).nodeName == 'INPUT') {
                if(ob[key] == 'on') {
                    $("#"+key).prop( "checked", true );
                }else {
                    $("#"+key).val(ob[key]);
                }
            } else if($("#"+key).get(0).nodeName == 'SELECT') {
                let dataType = $("#"+key).attr("data-type");
                _func_loadDanhMucCallback(dataType, function(result){
                    let xhtml = "";
                    xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
                    if(result.data.length > 0) {
                        let xse = "";
                        result.data.forEach(function(x){
                            console.log("x.id = "+x.id + " - "+ob[key]);
                            if(ob[key] != null && ob[key] == x.id){
                                xse = "selected";
                            }
                            xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
                        });
                        $("#"+key).html(xhtml);
                    }
                });
            } else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
                $("#"+key).html(ob[key]);
            }
        }
    });
    $(".btn-save-tab-10-form").empty().html("Hiệu chỉnh dữ liệu");
    $("#Tab_10_Form_Modal_Type").val("HIEU_CHINH");
    $("#Tab_10_Form_Modal_Index").val(i);
    $('#myModalForm10_Modal').modal('toggle');
});

$(".table").on('click', '.delete-tab10-function', function(){
    let i = $(this).attr("index");
    data_nhungthaydoilienquan.splice((i - 1), 1);
    _func_loadTableFormThayDoiLienQuan('#tab_10_table_thongtinthaydoi_body', data_nhungthaydoilienquan);
});
/**DANH SACH PCCC**/
/** LOAD DANH SACH **/
_func_searchCongTacPCCC(null,null,null);
$(".table").on('click', '.update-pccc-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");
    window.location = '/hieu-chinh-cong-tac-pccc/'+id + '/TAB_1';
});

    $(".table").on('click', '.delete-thanhphan-function', function(){
    let index = $(this).attr("index");
    let id 	= $(this).attr("data");

    let clazztr = '.row_'+index;

    // xoa danh muc ajax
    var data1 = {id: id};
    $.ajax(
        {
            url: "/api/congtac-pccc/thanhphan/delete",
            method: 'POST',
            data: JSON.stringify(data1),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            success: function(result){
                console.log(result);
                if(result== 'OKE') {
                    console.log('reload...')
                    _loadCauHinhThanhPhanPage();
                } else {
                    alert("Không thể xóa được dữ liệu này. ")
                }
            }
        });

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
                console.log(result);
                if(result== 'OKE') {
                    console.log('reload...')
                    _func_searchCongTacPCCC(null,null,null);
                } else {
                    alert("Không thể xóa được dữ liệu này. ")
                }
            }
        });

});
/*END DANH SACH PCCC*/

/*UPLOAD FILE VAN BAN KIEN NGHI...*/
$("body").delegate(".uploadfile_vanbankiennghi", "change",function(){
    var files = $(this).prop('files');
    var countSTT = $('#filelist_vanbankiennghi > div').size() + 1;
    console.log("upload action ...");
    var objectId = $(this).attr('objectId');

    var isDinhDangDefault = false;
    if(countSTT <= 1) {
        if (files.length > 0) {
            // check typefile
            let indexExtFile = files[0].name.lastIndexOf(".");
            let extFile = files[0].name.substring(indexExtFile+1, files[0].name.length);
            // isDinhDangDefault = dinhDang.includes(extFile.toLowerCase());
            let tenF = files[0].name;
            // var reader = new FileReader();
            //kiem tra trinh duyet co ho tro File API
            sendRequestWithCallback(tenF, files[0], function(result){
                console.log("...sendRequestWithCallback");
                var jo = result;
                if (jo) {

                    var htmlListFile = '<div class="title-file-items file_' + jo.id + '">';
                    htmlListFile += '<a class="title-a-items file_' + jo.id + '" href="' + jo.url + '" target="_blank" data="'+jo.id+'" title="' + jo.fileName + '">' + " - " + '. ' + jo.fileName + '</a>';
                    htmlListFile += '<input name="fileId" value="' + jo.id + '" type="hidden" class="fileId" /> '
                    htmlListFile += '<input name="fileURL" value="' + jo.url + '" type="hidden" class="fileURL" /> '
                    htmlListFile += '<input name="fileName" value="' + jo.fileName + '" type="hidden" class="fileURL" /> '
                    htmlListFile += '<span class="btn-del-file file_' + jo.id + '" attachmentid="' + jo.id + '" style="color: #151111; cursor: pointer;">Xóa</span>';
                    htmlListFile += '</div>';
                    $("#filelist_vanbankiennghi").append(htmlListFile);
                    $('.pccc-fileuploader-items_vanbankiennghi').show();
                }
            });
        }
    } else {
        alert("Chỉ được đính kèm 1 file");
    }

    $(this).val("");
});

/*UPLOAD FILE ...*/
$("body").delegate(".uploadfile_formcauhinh", "change",function(){
    var files = $(this).prop('files');
    var countSTT = $('#filelist_cauHinhThanhPhanHoSo > div').size() + 1;
    console.log("upload action ...");
    var objectId = $(this).attr('objectId');

    var isDinhDangDefault = false;
    if(countSTT <= 1) {
        if (files.length > 0) {
            // check typefile
            let indexExtFile = files[0].name.lastIndexOf(".");
            let extFile = files[0].name.substring(indexExtFile+1, files[0].name.length);
            // isDinhDangDefault = dinhDang.includes(extFile.toLowerCase());
            let tenF = files[0].name;
            // var reader = new FileReader();
            //kiem tra trinh duyet co ho tro File API
            sendRequestWithCallback(tenF, files[0], function(result){
                console.log("...sendRequestWithCallback");
                var jo = result;
                if (jo) {

                    var htmlListFile = '<div class="title-file-items file_' + jo.id + '">';
                    htmlListFile += '<a class="title-a-items file_' + jo.id + '" href="' + jo.url + '" target="_blank" data="'+jo.id+'" title="' + jo.fileName + '">' + " - " + '. ' + jo.fileName + '</a>';
                    htmlListFile += '<input name="fileId" value="' + jo.id + '" type="hidden" class="fileId" /> '
                    htmlListFile += '<input name="fileURL" value="' + jo.url + '" type="hidden" class="fileURL" /> '
                    htmlListFile += '<input name="fileName" value="' + jo.fileName + '" type="hidden" class="fileURL" /> '
                    htmlListFile += '<span class="btn-del-file file_' + jo.id + '" attachmentid="' + jo.id + '" style="color: #151111; cursor: pointer;">Xóa</span>';
                    htmlListFile += '</div>';
                    $("#filelist_cauHinhThanhPhanHoSo").append(htmlListFile);
                    $('.pccc-fileuploader-items_cauhinh').show();
                }
            });
        }
    } else {
        alert("Chỉ được đính kèm 1 file");
    }

    $(this).val("");
});
$("body").delegate(".uploadfile", "change",function(){
    var files = $(this).prop('files');
    console.log("upload action ...");
    var objectId = $(this).attr('objectId');

    var isDinhDangDefault = false;
    if (files.length > 0) {
        // check typefile
        let indexExtFile = files[0].name.lastIndexOf(".");
        let extFile = files[0].name.substring(indexExtFile+1, files[0].name.length);
        // isDinhDangDefault = dinhDang.includes(extFile.toLowerCase());
        let tenF = files[0].name;
        // var reader = new FileReader();
        //kiem tra trinh duyet co ho tro File API
        sendRequest(tenF, files[0], objectId);
    }
    $(this).val("");
});
/** LOADING PAGE **/
loadPage();
