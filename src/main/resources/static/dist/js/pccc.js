




// update danh muc
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

// delete function
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





/* load table dien co so */


//_func_searchCongTacPCCC("#table_quanlythongtincosopccc_body", "DIENCOSO");
// _func_loadFormHieuChinhCongTacPCCC();

//Luu modal form dien co so
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



//var _func_searchCongTacPCCC = ($selector_result, $dataSearch) => {
//    let k = ($dataSearch !== undefined && $dataSearch !== null && $dataSearch !== "") ? $dataSearch : 0;
//    var request_data = {
//        key: $dataSearch,
//        start: 0,
//        end: 20,
//        sortBy: 'id',
//        sortType: 'DESC'
//    }
//    var data = JSON.stringify(request_data);
//    $.ajax(
//        {
//            url: "/api/congtac-pccc/list",
//            method: 'POST',
//            data: data,
//            contentType: "application/json; charset=utf-8",
//            traditional: true,
//            success: function(result){
//                let xhtml = "";
//                if(result.length > 0) {
//                    result.forEach(function(x, index){
//                        let data = JSON.parse(x.jsonDataForm1);
//                        console.log(data);
//                        let stylezzz = '';
//                        if(index == 0) {
//							stylezzz = 'style="color: #1f6ed6;font-weight: 600;"';
//						}
//                        xhtml += '<tr '+stylezzz+'>';
//                        xhtml += '<td style="text-align:center">' +(index +1)+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_macoso+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_tencosocanhan+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_diachi+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_namduavaohoatdong+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_email+ '</td>';
//                      //  xhtml += '<td style="text-align:center">' +data.tab_1_website+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_dienthoai+ '</td>';
//                        xhtml += '<td style="text-align:center">' +data.tab_1_diabankhuvuctrongdiem+ '</td>';
//                        xhtml += '<td style="text-align:center">' +(data.tab_1_trangthaihoatdong == 1? "Hoạt động": "Ngưng hoạt động")+ '</td>';
//                        xhtml += '<td style="text-align:center">';
//                        xhtml += '<div class="btn-group"><button type="button" class="btn btn-warning">Hành động</button><button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
//                        xhtml += '<span class="caret"></span><span class="sr-only">Chức năng</span></button>';
//                        xhtml += '<ul class="dropdown-menu" role="menu"><li><a class="update-pccc-function" data="'+x.id+'" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a></li><li><a class="delete-pccc-function" href="javascript:void(0)" data="'+x.id+'" index="'+index+'">Xóa</a></li></ul></div></td></tr>';
//                    });
//                } else {
//					xhtml = '<tr><td colspan="11" style="text-align:center">Chưa có dữ liệu</td></tr>';
//				}
//                $($selector_result).html("").html(xhtml);
//            }
//    });
//}
var _btn_tag_4_submit = $(".btn-save-tab-4-form");
_btn_tag_4_submit.click(function() {
    let id = $("pccc_id").val();
    let dataForm3 = $("#tab3_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    data_congtaccapnuocchuachayngoainha.push(dataForm3);
    _func_loadTableFormCongTacCapNuocChuaChayNgoaiNha('btn-save-tab-3-form', data_congtaccapnuocchuachayngoainha)
    // _func_save_tam(dataForm2,"TAB_2", id);

});
var _btn_tag_3_submit = $(".btn-save-tab-3-form");
_btn_tag_3_submit.click(function() {
    let id = $("pccc_id").val();
    console.log(id);
    let dataForm3 = $("#tab3_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    data_congtaccapnuocchuachayngoainha.push(dataForm3);
    _func_loadTableFormCongTacCapNuocChuaChayNgoaiNha('tab_3_table_thongtincapnuocchuachayngoainha_body', data_congtaccapnuocchuachayngoainha)
    // _func_save_tam(dataForm2,"TAB_2", id);

});
// action save tab 2
var _btn_tag_2_submit = $(".btn-save-tab-2-form");
_btn_tag_2_submit.click(function() {
    $id = $("#pccc-id").val();
    let dataForm2 = $("#tab2_form").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    _func_save_tam(dataForm2, 'TAB_2', $id);
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
    let dataForm5 = $("#tab-1-form-5").serializeArray().reduce(function(obj, item) {
        obj[item.name] = item.value;
        return obj;
    }, {});
    let dataForm6 = $("#tab-1-form-6").serializeArray().reduce(function(obj, item) {
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
    var formData = Object.assign(dataForm1, dataForm3a, dataForm4, dataForm5, dataForm6);
    formData['danhSachLinhVucNganhNgheHoatDong'] = danhSachLinhVucNganhNgheHoatDong;
    formData['danhSachThamDuyetThietKeNghiemThu'] = danhSachThamDuyetThietKeNghiemThu;
   	_func_save_tam(formData, 1);
});



/* load table dien co so */
_func_loadTableDanhMuc("#table_diencoso_body", "DIENCOSO");

//_func_searchCongTacPCCC("#table_quanlythongtincosopccc_body", "DIENCOSO");
_func_loadFormHieuChinhCongTacPCCC();


//Luu modal form dien co so

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
    console.log(data);
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


