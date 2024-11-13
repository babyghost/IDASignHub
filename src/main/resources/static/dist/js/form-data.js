var loadDataFrom = (_form_data) => {}
var loadDataFromTAB_3 = (_form_data) => {
	console.log(_form_data);
	if(_form_data != null && _form_data !== "") {
		data_congtaccapnuocchuachayngoainha = _form_data.data_congtaccapnuocchuachayngoainha
		_func_loadTableFormCongTacCapNuocChuaChayNgoaiNha('#tab_3_table_thongtincapnuocchuachayngoainha_body', data_congtaccapnuocchuachayngoainha);
	}
}
var loadDataFromTAB_4 = (_form_data) => {

	if(_form_data != null && _form_data !== "") {
		data_thongtinvehangmucnha_congtrinhcoso = _form_data.data_thongtinvehangmucnha_congtrinhcoso
		_func_loadTableFormThongTinveHangMucNhaCongTrinhCoSo('#tab_4_table_body', data_thongtinvehangmucnha_congtrinhcoso);
	}
}
var loadDataFromTAB_5 = (_form_data) => {
	if(_form_data != null && _form_data !== "") {
		data_congtactuyentruyen_huanluyen = _form_data.data_congtactuyentruyen_huanluyen
		_func_loadTableFormCongTacTuyenTruyenHuanLuyen('#tab_5_table_body', data_congtactuyentruyen_huanluyen);
	}
}
var loadDataFromTAB_6 = (_form_data) => {
	if(_form_data != null && _form_data !== "") {
		data_congtackiemtravePCCC = _form_data.data_congtackiemtravePCCC
		_func_loadTableFormCongTacKiemTraPCCC('#tab_6_table_body', data_congtackiemtravePCCC);
	}
}
var loadDataFromTAB_7 = (_form_data) => {
	if(_form_data != null && _form_data !== "") {
		data_phuongAnPcccCNCH = _form_data.data_phuongAnPcccCNCH
		_func_loadTableFormPhuongAnPCCC('#tab_7_table_body', data_phuongAnPcccCNCH);

	}
}
var loadDataFromTAB_8 = (_form_data) => {
	console.log(_form_data);
	if(_form_data != null && _form_data !== "") {
		data_thucTapPhuongAnPcccCuaCongAn = _form_data.data_thucTapPhuongAnPcccCuaCongAn;
		data_xayDungPhuongAn = _form_data.data_xayDungPhuongAn;
		_func_loadTableFormXayDungPhuongAn('#tab_8_table_xaydungphuongan_body', data_xayDungPhuongAn);
		_func_loadTableFormThucTapPhuongAnChuaChayVaCuuHoCuuNan('#tab_8_table_thuctapphuongan_body', data_thucTapPhuongAnPcccCuaCongAn);
	}
}
var loadDataFromTAB_9 = (_form_data) => {
	if(_form_data != null && _form_data !== "") {
		data_tinhHinhChayNo = _form_data.data_tinhHinhChayNo
		_func_loadTableFormTinhHinhChayNo('#tab_9_table_congtackiemtrapccc-body', data_tinhHinhChayNo);
	}
}
var loadDataFromTAB_10 = (_form_data) => {
	if(_form_data != null && _form_data !== "") {
		data_nhungthaydoilienquan = _form_data.data_nhungthaydoilienquan
		_func_loadTableFormThayDoiLienQuan('#tab_10_table_thongtinthaydoi_body', data_nhungthaydoilienquan);
	}
}

var loadDataFromTAB_1 = (_form_data) => {
	let array_key = Object.keys(_form_data);
	// key  = id tag
	console.log("array_key");
	console.log(array_key);
	array_key.forEach(function(key, index){
		console.log("index = "+index);
		console.log("key  = "+key);
		console.log("datakey = "+_form_data[key]);
		if(document.querySelector("#"+key)){
			console.log($("#"+key).get(0).nodeName);
			if($("#"+key).get(0).nodeName == 'INPUT') {
				console.log("xu ly checkbox");
				if(_form_data[key] == 'on') {
					$("#"+key).prop( "checked", true );
				}else {
					$("#"+key).val(_form_data[key]);
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
							console.log("x.id = "+x.id + " - "+_form_data[key]);
							if(_form_data[key] != null && _form_data[key] == x.id){
								xse = "selected";
							} else {
								xse = '';
							}
							xhtml += '<option value="'+x.id+'" ' +xse+' >'+x.ten+'</option>';
						});
						$("#"+key).html(xhtml);
					}
				});
			} else if($("#"+key).get(0).nodeName == 'TEXTAREA') {
				$("#"+key).html(_form_data[key]);
			}
		} else if(key === 'danhSachLinhVucNganhNgheHoatDong'){
			console.log('danhSachLinhVucNganhNgheHoatDong');
			var danhSachLinhVucNganhNgheHoatDong = _form_data[key];
			_func_loadTableLinhVucList('#table_linhvucnganhnghehoatdong_body', danhSachLinhVucNganhNgheHoatDong);
		} else if(key === 'danhSachThamDuyetThietKeNghiemThu'){
			console.log('danhSachThamDuyetThietKeNghiemThu');
			console.log(_form_data[key]);
			var danhSachThamDuyetThietKeNghiemThu  = _form_data[key];
			_func_loadThamDuyetList('#table_thamdinhthietkenghiemthu_body',danhSachThamDuyetThietKeNghiemThu);
		}else if(key === "tab_1_cosothuochoackhongthuocthanhphanduan") {
			$("#"+key+"_"+_form_data[key]).attr('checked', true);
		} if(key === "thanhPhanHoSo") {
			let thanhPhanHoSoActive = _form_data[key];
			_drawThanhPhanHoSoWithDataActive(thanhPhanHoSoActive);
			console.log(thanhPhanHoSoActive);
		}
	})
}

var _func_loadTableFormCongTacTuyenTruyenHuanLuyen =  ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_5_ngaytuyentruyen+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_noidungtuyentruyen+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_lucluongpccccoso_chuyennganh+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_canboquanlylanhdao+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_nguoilaodong+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_doituongkhac+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_soluonggiaycnhlduoccap+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_5_ghichu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab5-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab5-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormThongTinveHangMucNhaCongTrinhCoSo = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_4_khoinhakhuvuc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_quymo+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_bacchiulua+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_tinhnguyhiemchayno+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_nganchaylan_khoangcachtunha+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_nganchaylan_khoangcachgianphong+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_loithoatnan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_soluonghethong_pccc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_khuvucduoctrangbi+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_tinhtranghoatdong+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_4_ghichu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab4-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab4-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
//load table
var _func_loadTableFormThucTapPhuongAnChuaChayVaCuuHoCuuNan = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_8_thuctapphuongan_ngay_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_tinhhuongthuctap_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_lucluongthamgia_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_phuongtienthamgia_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_danhgiaketqua_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_ghichu_congan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update_tab8_thuctapphuongan_function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete_tab8_thuctapphuongan_function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	} else {
		xhtml += '<tr>';
		xhtml += '<td style="text-align:center" colspan="">'+index+'</td>';
		xhtml += '</tr>';
	}
	$($selector).html("").append(xhtml);
}

var _func_loadTableFormThayDoiLienQuan = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_10_ngaythaydoi+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_10_noidungthaydoi+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_10_canbotheogioi+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_10_xacnhancualanhdaodonvi+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_10_ghichu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab10-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab10-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormTinhHinhChayNo = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_9_ngayxayrasuco+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_9_hangmucchayno+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_9_thiethainguoitaisan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_9_congtacxulysauvuchay+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab9-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab9-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormXayDungPhuongAn = ($selector, $list) => {
	console.log($list);
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_8_loaiphuonganpccc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_phuonganpccc_ngay+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_loaiphuonganpccc_coquanpheduyet+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_phuonganpccc_ngaypheduyet+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_noidungchinhly+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_ngaythangnamchinhlypheduyet+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_8_ghichu_form2+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update_tab8_xaydungphuongan_function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete_tab8_xaydungphuongan_function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	} else {
		xhtml += '<tr>';
		xhtml += '<td colspan="9" style="text-align:center">Chưa có dữ liệu</td>';
		xhtml += '</tr>';
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormPhuongAnPCCC = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_7_loaiphuongan+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_phuongan_ngay+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_coquanpheduyet+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_pheduyet_ngay+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_noidungchinhly+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_ngaychinhlypheduyet+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_7_phuonganthuctap+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab7-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab7-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormCongTacKiemTraPCCC = ($selector, $list) => {
	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<span>'+e.tab_6_ngaykiemtra+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_hinhthuckiemtraStr+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_sotontaivipham+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_bienbanviphampccc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_quyetdinhxuphat+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_sotienphat+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_quyetdinhtamdinhchi_dinhchihoatdong+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<a href="'+e.fileURL+'">'+e.fileName+'</a>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_6_ghichu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab6-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab6-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}
var _func_loadTableFormCongTacCapNuocChuaChayNgoaiNha = ($selector, $list) => {

	let xhtml = "";
	let index = 0;
	if($list.length > 0) {
		$list.forEach(function(e) {
			index = index + 1;
			xhtml += '<tr>';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
				xhtml += '<span>'+e.tab_3_nguonnuoc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_donviquanlysohuu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_soluongtrucapnuoc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_vitritenduong+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_tinhtranghoatdongStr+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_luuluongtrucapnuoc+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_khananghutnuoccuaxe+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>'+e.tab_3_ghichu+'</span>';
			xhtml += '</td>';
			xhtml += '<td style="text-align: center;">';
			xhtml += '<span>';
			xhtml += '<div class="btn-group">';
			xhtml += '<button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
			xhtml += 'Hành động';
			xhtml += '<span class="sr-only">Chức năng</span>';
			xhtml += '</button>';
			xhtml += '<ul class="dropdown-menu" role="menu">';
			xhtml += '<li>';
			xhtml += '<a class="update-tab3-function" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a>';
			xhtml += '</li>';
			xhtml += '<li>';
			xhtml += '<a class="delete-tab3-function" href="javascript:void(0)"  index="'+index+'">Xóa</a>';
			xhtml += '</li>';
			xhtml += '</ul>';
			xhtml += '</div>';
			xhtml += '</span>';
			xhtml += '</td>';
			xhtml += '</tr>';
		});
	}
	$($selector).html("").append(xhtml);
}

var _func_loadThamDuyetList = ($selector, list) => {
	console.log("_func_loadTableThamDuyet");
	data_thamdinhthietke = list;
	let index = 0;
	if(list.length > 0) {
		let xhtml = "";
		list.forEach(function(row){
			index = index + 1;
			let clzztr = 'table_thamdinhthietkenghiemthu_'+index;
			xhtml += '<tr class="'+clzztr+'">';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
			xhtml += '<input type="text" class="form-control" id="'+row.input_col_one_name+'" name="'+row.input_col_one_name+'" class="table_col_one" placeholder=""  value="'+(row.input_col_one_value == undefined? "" : row.input_col_one_value )+'">';
			xhtml += '</td>';
			xhtml += '<td>';
			xhtml += '<input type="text" id="'+row.input_col_two_name+'" name="'+row.input_col_two_name+'" class="form-control" placeholder=""  value="'+(row.input_col_two_value == undefined ? "" : row.input_col_two_value) +'">';
			xhtml += '</td>';
			xhtml += '<td>';
			xhtml += '<input type="text" id="'+row.input_col_three_name+'" name="'+row.input_col_three_name+'" class="form-control" placeholder=""  value="'+(row.input_col_three_value == undefined ? "" :row.input_col_three_value )+'">';
			xhtml += '</td>';
			xhtml += '<td>';
			xhtml += '<input type="text" id="'+row.input_col_four_name+'" name="'+row.input_col_four_name+'" class="form-control" placeholder="" value="'+(row.input_col_four_value == undefined ? "" : row.input_col_four_value)+'">';
			xhtml += '</td>';
			xhtml += '<td>';
			xhtml += '<input type="text" id="'+row.input_col_five_name+'" name="'+row.input_col_five_name+'" class="form-control" placeholder=""  value="'+(row.input_col_five_value == undefined ? "" :row.input_col_five_value )+'"></td>';
			xhtml += '</tr>';
		});
		$($selector).attr("data",list.length);
		$($selector).html("").append(xhtml);
	}
};
var _func_loadTableLinhVucList = ($selector, list) => {
	let xhtml = "";
	let index = 0;
	data_linhvuchanhnghe = list;
	_func_loadDanhMucCallback('NGANHNGHEHOATDONG', function(result){
		if(list.length > 0) {
			list.forEach(function(row){
				index = index + 1;
				let clzztr = 'table_linhvucnganhnghehoatdong_'+index;
				console.log("0000----");
				console.log(row.input_col_two_value);
				let checkedzzz = row.input_col_two_value == true ? 'checked' : '';
				xhtml += '<tr class="'+clzztr+'">';
				xhtml += '<td style="text-align:center">'+index+'</td>';
				xhtml += '<td style="text-align:center">';
				xhtml += renderSelectHtml(result.data, row.input_col_one_value, row.input_col_one_name, row.input_col_one_name, 'table_linhvucnganhnghehoatdon form-control');
				// xhtml += '<select id="'+row.input_col_one_name+'" class="table_linhvucnganhnghehoatdon form-control" name="'+row.input_col_one_name+'"></select>';
				xhtml += '</td>';
				xhtml += '<td style="text-align: center;">';
				xhtml += '<div class="checkbox"><label><input type="checkbox"  id="'+row.input_col_two_name+'" name="'+row.input_col_two_name+'" '+checkedzzz+'></label></div>';
				xhtml += '</td>';
				xhtml += '</tr>';
			});
			$($selector).attr("data",list.length);
			$($selector).html("").append(xhtml);
		}
	});
}

var renderSelectHtml = (list, current, id, name, clazz) => {

	let xselect = '<select id="'+id+'" class="'+clazz+'" name="'+name+'">';
	xselect += '<option value="0"> -Hãy chọn danh muc-</option>';
	let xselected = '';
	list.forEach(function(row) {
		if(row.id == current) {
			xselected = 'selected';
		} else {
			xselected = '';
		}
		console.log(xselected);
		xselect += '<option value="'+row.id+'" '+xselected+'>'+row.ten+'</option>';
	});
	xselect += '</select>';
	return xselect;
}

var _func_loadTableFormLinhVucNganhNgheHoatDong = ($selector, row, index) => {
	let xhtml = "";
	let clzztr = 'table_linhvucnganhnghehoatdong_'+index;
	xhtml += '<tr class="'+clzztr+'">';
	xhtml += '<td style="text-align:center">'+index+'</td>';
	xhtml += '<td style="text-align:center">';
		xhtml += '<select id="'+row.input_col_one_name+'" class="table_linhvucnganhnghehoatdon form-control" name="'+row.input_col_one_name+'"></select>';
	xhtml += '</td>';
	xhtml += '<td style="text-align: center;">';
		xhtml += '<div class="checkbox"><label><input type="checkbox"  id="'+row.input_col_two_name+'" name="'+row.input_col_two_name+'" ></label></div>';
	xhtml += '</td>';
	xhtml += '</tr>';
	if(index === 1) {
		$($selector).html("").append(xhtml);
	} else {
		$($selector).append(xhtml);
	}
}

var _func_loadTableThamDuyet = ($selector, row, index) => {
    console.log("_func_loadTableThamDuyet");
	let xhtml = "";
		let clzztr = 'table_thamdinhthietkenghiemthu_'+index;
			xhtml += '<tr class="'+clzztr+'">';
			xhtml += '<td style="text-align:center">'+index+'</td>';
			xhtml += '<td style="text-align:center">';
				xhtml += '<input type="text" class="form-control" id="'+row.input_col_one_name+'" name="'+row.input_col_one_name+'" class="table_col_one" placeholder=""  value="'+row.input_col_one_value+'">';
			xhtml += '</td>';
		    xhtml += '<td>';
		    	xhtml += '<input type="text" id="'+row.input_col_two_name+'" name="'+row.input_col_two_name+'" class="form-control" placeholder=""  value="'+row.input_col_two_value+'">';
		    xhtml += '</td>';
		    xhtml += '<td>';
		    	xhtml += '<input type="text" id="'+row.input_col_three_name+'" name="'+row.input_col_three_name+'" class="form-control" placeholder=""  value="'+row.input_col_three_value+'">';
		    xhtml += '</td>';
		    xhtml += '<td>';
		    	xhtml += '<input type="text" id="'+row.input_col_four_name+'" name="'+row.input_col_four_name+'" class="form-control" placeholder="" value="'+row.input_col_four_value +'">';
		    xhtml += '</td>';
		    xhtml += '<td>';
		    xhtml += '<input type="text" id="'+row.input_col_five_name+'" name="'+row.input_col_five_name+'" class="form-control" placeholder=""  value="'+row.input_col_five_value+'"></td>';
		    xhtml += '</tr>';
	if(index === 1) {
		$($selector).html("").append(xhtml);
	} else {
		$($selector).append(xhtml);
	}
};
var _func_loadTableDanhMuc = ($id_table_body, $type) => {
	let k = ($type !== undefined && $type !== null && $type !== "") ? $type : "DIENCOSO";
	let searchParams = new URLSearchParams(window.location.search);

	let start = searchParams.has('start') ? searchParams.get('start') : 0;
	let end = searchParams.has('end') ? searchParams.get('end') : 20;
	var request_data = {
		type: k,
		start: start,
		end: end,
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
				let xhtml = "";
				let begin = result.start;
				let endd = result.end;
				let total = result.total;
				let data = result.data;
				if(data.length > 0) {
					data.forEach(function(x, index){
						xhtml += '<tr class="row_'+index +'"><td style="text-align:center">'+(index + 1)+'</td><td  class="td_row_ten_'+index+'" style="text-align:center" td-data="'+x.id+'">'+x.ten+'</td><td  style="text-align:center"  class="td_row_ma_'+index+'"  td-data="'+x.id+'">'+x.ma+'</td>';
						xhtml += '<td  style="text-align:center" td-data="'+x.id+'">';
						xhtml += '<div class="btn-group"><button type="button" class="btn btn-warning">Hành động</button><button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
						xhtml += '<span class="caret"></span><span class="sr-only">Chức năng</span></button>';
						xhtml += '<ul class="dropdown-menu" role="menu"><li><a class="update-function" data="'+x.id+'" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a></li><li><a class="delete-function" href="javascript:void(0)" data="'+x.id+'" index="'+index+'">Xóa</a></li></ul></div></td></tr>';
					});
				} else {
					xhtml = '<tr><td colspan="4">Chưa có dữ liệu</td></tr>';
				}
				$($id_table_body).html("").html(xhtml);
				_draw_pagination(".pagination_table_capquanly", begin, endd, total);
			}
		});
}


// var _func_loadFormHieuChinhCongTacPCCC = () => {
// 	let id = $(".pccc_id").val();
// 	if(id !== undefined && id !== null && id > 0 ) {
// 		// load form hiệu chỉnh
// 		let data1 = {id: id};
// 		$.ajax({
// 			url: "/api/congtac-pccc/getData",
// 			method: 'POST',
// 			data: JSON.stringify(data1),
// 			contentType: "application/json; charset=utf-8",
// 			traditional: true,
// 			success: function(result){
// 				if(result != null) {
// 					let jsonDataForm1 = result.jsonDataForm1 !== null ? JSON.parse(result.jsonDataForm1) : '';
// 					let jsonDataForm2 = result.jsonDataForm2 !== null ? JSON.parse(result.jsonDataForm2) : '';
// 					let jsonDataForm3 = result.jsonDataForm3 !== null ? JSON.parse(result.jsonDataForm3) : '';
// 					let jsonDataForm4 = result.jsonDataForm4 !== null ? JSON.parse(result.jsonDataForm4) : '';
// 					let jsonDataForm5 = result.jsonDataForm5 !== null ? JSON.parse(result.jsonDataForm5) : '';
// 					let jsonDataForm6 = result.jsonDataForm6 !== null ? JSON.parse(result.jsonDataForm6) : '';
// 					let jsonDataForm7 = result.jsonDataForm7 !== null ? JSON.parse(result.jsonDataForm7) : '';
// 					let jsonDataForm8 = result.jsonDataForm8 !== null ? JSON.parse(result.jsonDataForm8) : '';
// 					let jsonDataForm9 = result.jsonDataForm9 !== null ? JSON.parse(result.jsonDataForm9) : '';
// 					let jsonDataForm10 = result.jsonDataForm10 !== null ? JSON.parse(result.jsonDataForm10) : '';
// 					loadDataFrom(jsonDataForm1);
// 				}
// 			}
// 		});
// 	}
// }
var _func_save_tam = ($obj, type, $id) => {
	var data = JSON.stringify($obj);
	let idform = $("#page_pccc_id").val();
	if($id == null && idform !== undefined && idform !== null && idform !== '') {
		$id = idform;
	}
	var data1 = {param: data, id: $id, type : type};
	$.ajax(
		{
			url: "/api/congtac-pccc/save",
			method: 'POST',
			data: JSON.stringify(data1),
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				alert("Lưu dữ liệu thành công");
				$("#page_pccc_id").val(result);
				if (confirm("Bạn muốn quay trở lại danh sách ?") === true) {
					window.location = '/quan-ly-co-so-pccc-cnch';
				}
			}
		});
}
var _func_save_CauHinh = ($obj, $id) => {
	$obj['id'] = $id;
	$.ajax(
		{
			url: "/api/congtac-pccc/cau-hinh/save",
			method: 'POST',
			data: JSON.stringify($obj),
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				alert("Lưu dữ liệu thành công");
				window.location = '/cau-hinh-thanh-phan-ho-so';
			}
		});
}
var _func_save_danhmuc = ($obj, $id, $form_reset, $id_modal, $id_table_body) => {
	//var id = $("#id-danhmuc").val();
	var data = JSON.stringify($obj);
	var data1 = {data: data, id: $id};
	$.ajax(
		{
			url: "/api/congtac-pccc/save/danhmuc",
			method: 'POST',
			data: JSON.stringify(data1),
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				alert("Lưu dữ liệu thành công");
				// $($hidden_id).val(result);
				$($form_reset)[0].reset();
				$($id_modal).modal('toggle');
				_func_loadTableDanhMuc($id_table_body, $obj.type);
			}
		});
}

var _loadDashBoard = () => {
	var data1 = {data: null};
	$.ajax(
		{
			url: "/api/congtac-pccc/getSoHoSo",
			method: 'POST',
			data: JSON.stringify(data1),
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				console.log(result);
				$('.tongHoSo').html(result.tongHoSo);
				$('.tongHoSoTrongNgay').html(result.tongHoSoTrongNgay);

				console.log(result.listThongKeTheoDonVi.length);
				if(result.listThongKeTheoDonVi.length > 0) {
					result.listThongKeTheoDonVi.forEach(function(x) {
						let zzdonvi = x.donvi;
						let claszz = zzdonvi + '_count';
						$('.'+claszz).html(x.donvi_count);
					});
				}
				if(result.listThongKeTheoDonViTrongNgay.length > 0) {
					result.listThongKeTheoDonViTrongNgay.forEach(function(x) {
						let zzdonvi = x.donvi;
						let claszz = zzdonvi + '_count_per_day';
						$('.'+claszz).html(x.donvi_count + "hồ sơ/ngày");
					});
				}

			}
		});
}

var _loadCauHinhThanhPhanPage = () => {
	let $selector_result = "#table_thanhphanhoso > tbody";
	// let k = ($dataSearch !== undefined && $dataSearch !== null && $dataSearch !== "") ? $dataSearch : 0;
	var request_data = {
		key: '',
		start: 0,
		end: 20,
		sortBy: 'id',
		sortType: 'DESC'
	}
	var data = JSON.stringify(request_data);
	$.ajax(
		{
			url: "/api/congtac-pccc/getThanhPhanHoSo",
			method: 'POST',
			data: data,
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				let xhtml = "";

				if(result.length > 0) {
					result.forEach(function(x, index){
							let data = x;
							console.log(data);
							let stylezzz = '';
							if(index == 0) {
								stylezzz = 'style="color: #1f6ed6;font-weight: 600;"';
							}
							xhtml += '<tr '+stylezzz+'>';
							xhtml += '<td style="text-align:center">' +(index +1)+ '</td>';
							xhtml += '<td style="text-align:center">' +data.ten+ '</td>';
							xhtml += '<td style="text-align:center">' +data.ma+ '</td>';
							xhtml += '<td style="text-align:center"><a href="'+data.urlFileMau+'">' +data.fileName+ '</a></td>';
							xhtml += '<td style="text-align:center">' +(data.trangThai == 1 ? "Đang hoạt động": "Không hoạt động")+ '</td>';
							xhtml += '<td style="text-align:center">';
							xhtml += '<div class="btn-group"><button type="button" class="btn btn-warning">Hành động</button><button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
							xhtml += '<span class="caret"></span><span class="sr-only">Chức năng</span></button>';
							xhtml += '<ul class="dropdown-menu" role="menu"><li><a class="update-pccc-function" data="'+data.id+'" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a></li><li><a class="delete-thanhphan-function" href="javascript:void(0)" data="'+x.id+'" index="'+index+'">Xóa</a></li></ul></div></td></tr>';
					});
				} else {
					xhtml = '<tr><td colspan="8" style="text-align:center">Chưa có dữ liệu</td></tr>';
				}
				$($selector_result).html("").html(xhtml);
			}
		});
}
var _func_searchCongTacPCCC = ($search, $start, $end) => {
	let $selector_result = "#table_quanlythongtincosopccc_body";
   // let k = ($dataSearch !== undefined && $dataSearch !== null && $dataSearch !== "") ? $dataSearch : 0;
   var role = $("#role").val();
   let st = $start == null ? 0 : $start;
	let e = $end == null ? 20 : $end;
	var request_data = {
       key: '',
       start: st,
       end: e,
       sortBy: 'id',
       sortType: 'DESC'
   }
   var data = JSON.stringify(request_data);
   $.ajax(
       {
           url: "/api/congtac-pccc/list",
           method: 'POST',
           data: data,
           contentType: "application/json; charset=utf-8",
           traditional: true,
           success: function(result){
               let xhtml = "";
			   data = result.data;
               if(data.length > 0) {
				   let stt = result.currentPage * 20;
				   data.forEach(function(x, index){
					   if(x.jsonDataForm1 !== null) {
						   let data = JSON.parse(x.jsonDataForm1);
						   console.log(data);
						   let stylezzz = '';
						   if(index == 0) {
							   stylezzz = 'style="color: #1f6ed6;font-weight: 600;"';
						   }
						   xhtml += '<tr '+stylezzz+'>';
						   xhtml += '<td style="text-align:center">' +(stt + index +1)+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_macoso+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_tencosocanhan+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_diachi+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_namduavaohoatdongStr+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_email+ '</td>';
						   //  xhtml += '<td style="text-align:center">' +data.tab_1_website+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_dienthoai+ '</td>';
						   xhtml += '<td style="text-align:center">' +data.tab_1_diabankhuvuctrongdiemStr+ '</td>';
						   xhtml += '<td style="text-align:center">' +(data.tab_1_trangthaihoatdong == 1156 ? "Hoạt động": "Ngưng hoạt động")+ '</td>';
						   xhtml += '<td style="text-align:center">';
						   if(role == 'ADMIN' || role == 'SUBMOD' || role == 'USER') {
							   xhtml += '<div class="btn-group"><button type="button" class="btn btn-warning">Hành động</button><button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">';
							   xhtml += '<span class="caret"></span><span class="sr-only">Chức năng</span></button>';
							   xhtml += '<ul class="dropdown-menu" role="menu"><li><a class="update-pccc-function" data="'+x.id+'" index="'+index+'" href="javascript:void(0);">Hiệu chỉnh</a></li><li><a class="delete-pccc-function" href="javascript:void(0)" data="'+x.id+'" index="'+index+'">Xóa</a></li></ul></div>';
						   }
						   xhtml += '</td></tr>';
					   } else {
						   xhtml += '<tr>';
						   xhtml += '<td colspan="10" style="text-align:center"></td>';
						   xhtml += '</tr>';
					   }

                   });
               } else {
					xhtml = '<tr><td colspan="11" style="text-align:center">Chưa có dữ liệu</td></tr>';
				}
			   let xhtmlPage = "";
			   xhtmlPage += '<tr>';
			   xhtmlPage += '<td colspan="11" style="text-align:left">';
			   xhtmlPage += '<div>Trang <input type="text" class="page-pagi" style="width: 35px;text-align: center;" name="page-pagi" value="'+result.currentPage+'" /> / '+result.pages+' <a href="javascript:void(0)" class="trangtruoc">Trang trước</a>   |   <a href="javascript:void(0)" class="trangsau">Trang sau</a>  <span class="tongso" style="float:right;">Tổng số dữ liệu : '+result.total+'</span></div>';
			   xhtmlPage += '<input type="hidden" class="pages" value="'+result.pages+'"/>';
			   xhtmlPage += '</td>';
			   xhtmlPage += '</tr>';
			   xhtml += xhtmlPage;
               $($selector_result).html("").html(xhtml);
           }
   });
}
$("#table_quanlythongtincosopccc").on('click', '.trangtruoc', function(){
	var current_page = $(".page-pagi").val();
	var next_page = Number(current_page) - 1;
	if(Number(next_page) < 0) {
		sstart = 0;
	}
	let sstart = next_page * 20;
	_func_searchCongTacPCCC(null, sstart, sstart + 20);
});
$("#table_quanlythongtincosopccc").on('click', '.trangsau', function(){
	var current_page = $(".page-pagi").val();
	var pages = $(".pages").val();
	if(Number(current_page) > Number(pages)) {
		current_page = 0;
	}
	var next_page = Number(current_page) + 1;
	let sstart =  next_page * 20;
	_func_searchCongTacPCCC(null, sstart, sstart + 20);
});
$("#table_quanlythongtincosopccc").on('change', '.page-pagi', function(){
	var current_page = $(".page-pagi").val();
	var pages = $(".pages").val();
	console.log(current_page);
	console.log(pages);
	if(Number(current_page) > Number(pages)) {
		current_page = 0;
	}
	var next_page = Number(current_page);
	let sstart =  next_page * 20;
	_func_searchCongTacPCCC(null, sstart, sstart + 20);
});
/*function get data for page*/
var _loadFormAddPage = () => {
	console.log("_loadFormAddPage");
	let d1 = $("#tab1_form_1").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d3a = $("#tab-1-form-3-a").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d4 = $("#tab-1-form-4").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d5 = $("#tab-1-form-5").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d6 = $("#tab-1-form-6").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d62 = $("#tab-1-form-6_2").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d3 = $("#tab-1-form-3").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	let d2b = $("#form_2_b").serializeArray().reduce(function(obj, item) {
		obj[item.name] = item.value;
		return obj;
	}, {});
	var formData = Object.assign(d1,d3, d4, d5, d6, d62, d3a, d2b);
	let array_key = Object.keys(formData);
	array_key.forEach(function(key, index){
		if(document.querySelector("#"+key)){
			if($("#"+key).get(0).nodeName == 'SELECT') {
				let dataType = $("#"+key).attr("data-type");
				console.log("i here = "+dataType);
				_func_loadDanhMucCallback(dataType, function(a){
					let xhtml = "";
					let activeStr = "";
					xhtml += '<option value="0">--Hãy chọn danh mục--</option>';
					if(a.data.length > 0) {
						let xse = "";
						a.data.forEach(function(x){
							if(dataType === 'TRANGTHAIHOATDONG') {
								activeStr = x.id === 1156 ? 'selected' : ''
							}
							xhtml += '<option value="'+x.id+'" '+activeStr+' >'+x.ten+'</option>';
						});
						$("#"+key).html(xhtml);
					}
				});
			}
		}

	});
}
var _loadDataPcccForEditPage = ($id, $tab) => {
	// get data PCCC
	console.log("$id = " +$id);
	console.log("$tab = " +$tab);

	if($id !== undefined && $id !== null && $id > 0 ) {
		// load form hiệu chỉnh
		let data1 = {id: $id};
		$.ajax({
			url: "/api/congtac-pccc/getData",
			method: 'POST',
			data: JSON.stringify(data1),
			contentType: "application/json; charset=utf-8",
			traditional: true,
			success: function(result){
				if(result != null) {
					let jsonDataForm1 = result.jsonDataForm1 !== null ? JSON.parse(result.jsonDataForm1) : '';
					// let jsonDataForm2 = result.jsonDataForm2 !== null ? JSON.parse(result.jsonDataForm2) : '';
					let jsonDataForm3 = result.jsonDataForm3 !== null ? JSON.parse(result.jsonDataForm3) : '';
					let jsonDataForm4 = result.jsonDataForm4 !== null ? JSON.parse(result.jsonDataForm4) : '';
					let jsonDataForm5 = result.jsonDataForm5 !== null ? JSON.parse(result.jsonDataForm5) : '';
					let jsonDataForm6 = result.jsonDataForm6 !== null ? JSON.parse(result.jsonDataForm6) : '';
					let jsonDataForm7 = result.jsonDataForm7 !== null ? JSON.parse(result.jsonDataForm7) : '';
					let jsonDataForm8 = result.jsonDataForm8 !== null ? JSON.parse(result.jsonDataForm8) : '';
					let jsonDataForm9 = result.jsonDataForm9 !== null ? JSON.parse(result.jsonDataForm9) : '';
					let jsonDataForm10 = result.jsonDataForm10 !== null ? JSON.parse(result.jsonDataForm10) : '';
						console.log($tab);
						if($tab === 'TAB_1') {
							loadDataFromTAB_1(jsonDataForm1);
						}else if($tab === 'TAB_3') {
							loadDataFromTAB_3(jsonDataForm3);
						}else if($tab === 'TAB_4') {
							loadDataFromTAB_4(jsonDataForm4);
						}else if($tab === 'TAB_5') {
							loadDataFromTAB_5(jsonDataForm5);
						}else if($tab === 'TAB_6') {
							loadDataFromTAB_6(jsonDataForm6);
						}else if($tab === 'TAB_7') {
							loadDataFromTAB_7(jsonDataForm7);
						}else if($tab === 'TAB_8') {
							loadDataFromTAB_8(jsonDataForm8);
						}else if($tab === 'TAB_9') {
							loadDataFromTAB_9(jsonDataForm9);
						}else if($tab === 'TAB_10') {
							loadDataFromTAB_10(jsonDataForm10);
						}
				}
			}
		});
	}
}
/*End function get data for page*/

/* Form load thanh phan duoc cau hinh*/
/*
$arr_active = [{
	id :17,
	fileIds : [1,2,3]
}]
 */
var _drawThanhPhanHoSoWithDataActive = function ($arr_active) {
	let data1 = {arr_thanhphanActive:$arr_active};
	$.ajax({
		type: "POST",
		url: "/api/congtac-pccc/getThanhPhanHoSoWithDataActive",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data1),
		success: function(data)
		{


			let dsThanhPhan = data;
			let len = 0;
			if(dsThanhPhan !== undefined){
				len = dsThanhPhan.length;
			}
			if(len>0){
				var htmlThanhPhan = '';
				for ( var i = 0; i < len; i++) {
					let htmlFile = "";
					var thanhPhanHoSo = dsThanhPhan[i];
					if(thanhPhanHoSo.fileDinhKems !== undefined && thanhPhanHoSo.fileDinhKems !== null){
						let lengFileDinhKem = thanhPhanHoSo.fileDinhKems.length;

						for(var j = 0; j < lengFileDinhKem ; j++ ){
							let file = thanhPhanHoSo.fileDinhKems[j];
							if(file !== undefined && file.url!=""){
								htmlFile+= '<div class="input-container">';
								htmlFile+='<input type="hidden" class="fileId_'+thanhPhanHoSo.id+'" name="fileId_'+thanhPhanHoSo.id+'" value="'+file.id+'">';
								htmlFile+='<a href="'+file.url+'" title="Click để tải file đính kèm">- ' +file.fileName+ '</a>';
								htmlFile+= '<span class="btn-del-file file_'+thanhPhanHoSo.id+'" attachmentid="'+thanhPhanHoSo.id+'" data-file="'+file.id+'" style="color: #151111; cursor: pointer;">Xóa</span>';
								htmlFile+= '<div>';
							}
						}

					}

					htmlThanhPhan+='<tr>';
					htmlThanhPhan+='<td class="center" style="line-height: 29px; vertical-align: middle; text-align:center;">'+(i+1)+'</td>';
					htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">';
					let xxchecked = "";
					if(thanhPhanHoSo.active === true) {
						xxchecked = "checked";
					} else {
						xxchecked = "";
					}
					htmlThanhPhan+='<input type="checkbox" value="'+thanhPhanHoSo.id+'" name="chkDaNhan[]" '+xxchecked+' class="daNhan-'+thanhPhanHoSo.id+'"><span class="lbl"></span>';
					htmlThanhPhan+='</td>';
					htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">'+thanhPhanHoSo.ten + '</td>';
					htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">';
					htmlThanhPhan+= '<a href="'+thanhPhanHoSo.urlFileMau+'" target="_blank">' +thanhPhanHoSo.fileName+ '</a>';
					htmlThanhPhan+= '</td>';
					htmlThanhPhan+='<td class="center" id="td_'+thanhPhanHoSo.id+'" style="vertical-align: middle; text-align:center;">';
					htmlThanhPhan+='<label class="fileContainer">Đính kèm tệp tin<input id="fileDinhKem_'+thanhPhanHoSo.id+'" style="display:none;" objectId="'+thanhPhanHoSo.id+'" class="uploadfile" type="file"/></label>';
					htmlThanhPhan+='<div class="pccc-fileuploader-items'+thanhPhanHoSo.id+'" style="display: none"><div id="filelist'+thanhPhanHoSo.id+'"></div></div>';
					htmlThanhPhan += htmlFile;
					htmlThanhPhan+='</td>';
					htmlThanhPhan+='</tr>';
				}
				$('table#tab_1_thanhphangiayto tbody').empty().append(htmlThanhPhan);

			}else{
				console.log("KHÔNG CÓ THÀNH PHẦN HỒ SƠ ..");
			}
		}
	}).error(function(xhr) {

	});
}
var _drawThanhPhanHoSo = function() {
	let data1 = {type:'PCCC_ThanhPhanHoSo'};
	$.ajax({
		type: "POST",
		url: "/api/congtac-pccc/getThanhPhanHoSoActive",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(data1),
	success: function(data)
	{

		let htmlFile = "";
		let dsThanhPhan = data;
		let len = 0;
		if(dsThanhPhan !== undefined){
			len = dsThanhPhan.length;
		}
		if(len>0){
			var htmlThanhPhan = '';
			for ( var i = 0; i < len; i++) {
				var thanhPhan = dsThanhPhan[i];
				// if(fileDinhKem !== undefined){
				// 	let lengFileDinhKem = fileDinhKem.length;
				// 	for(var j = 0; j < lengFileDinhKem ; j++ ){
				// 		file = fileDinhKem[j];
				// 		if(file !== undefined && file.fileURL!=""){
				// 			htmlFile+='<input type="hidden" name="fileId_'+thanhPhan.id+'" value="'+file.id+'">';
				// 			htmlFile+='<a href="'+file.fileURL+'" title="Click để tải file đính kèm">- ' +file.fileName+ '</a>';
				// 			htmlFile+='&nbsp;&nbsp;<a href="#" data-hosothanhphan="'+file.hoSoThanhPhanId+'" data-file="'+file.id+'" title="Click để xóa file đính kèm" class="text-error btnDeleteFile"><i class="icon-remove"></i></a><br>';
				// 		}
				// 	}
				// }

				htmlThanhPhan+='<tr>';
				htmlThanhPhan+='<td class="center" style="line-height: 29px; vertical-align: middle; text-align:center;">'+(i+1)+'</td>';
				htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">';
				htmlThanhPhan+='<input type="checkbox" value="'+thanhPhan.id+'" name="chkDaNhan[]" class="daNhan-'+thanhPhan.id+'"><span class="lbl"></span>';
				htmlThanhPhan+='</td>';
				htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">'+thanhPhan.ten + '</td>';
				htmlThanhPhan+='<td style="line-height: 29px; vertical-align: middle; text-align:center;">';
				htmlThanhPhan+= '<a href="'+thanhPhan.urlFileMau+'" target="_blank">' +thanhPhan.fileName+ '</a>';
				htmlThanhPhan+= '</td>';
				htmlThanhPhan+='<td class="center" id="td_'+thanhPhan.id+'" style="vertical-align: middle; text-align:center;">';
				htmlThanhPhan+='<label class="fileContainer">Đính kèm tệp tin<input id="fileDinhKem_'+thanhPhan.id+'" style="display:none;" objectId="'+thanhPhan.id+'" class="uploadfile" type="file"/></label>';
				htmlThanhPhan+='<div class="pccc-fileuploader-items'+thanhPhan.id+'" style="display: none"><div id="filelist'+thanhPhan.id+'"></div></div>';
				htmlThanhPhan+='</td>';
				htmlThanhPhan+='</tr>';
			}
			$('table#tab_1_thanhphangiayto tbody').empty().append(htmlThanhPhan);

		}else{
			console.log("KHÔNG CÓ THÀNH PHẦN HỒ SƠ ..");
		}
	}
	}).error(function(xhr) {

	});
};
function sendRequestWithCallback(fullName, $file, $callback) {

	var data = new FormData();

	data.append('folderName', 'PCCC_NHANHOSO');
	data.append('file', $file);
	data.append('fileName', fullName);
	data.append('appCode', "APP_PCCC");


	$.ajax({
		xhr: function () {
			var xhr = new window.XMLHttpRequest();
			// iPreviousBytesLoaded = 0;
			xhr.upload.addEventListener("progress", function (evt) {
				console.log("Dang tiến hành upload ...");
			}, false);
			return xhr;
		},
		async: true,
		url: "/api/file/upload",
		type: "POST",
		processData: false,
		contentType: false,
		data: data,
		success: function (result) {
			$callback(result);
		},
		error: function (result) {
			console.log("ERROR....");
		}
	});
}
function sendRequest(fullName, $file, objectId) {

	var data = new FormData();

	data.append('folderName', 'PCCC_NHANHOSO');
	data.append('file', $file);
	data.append('fileName', fullName);
	data.append('appCode', "APP_PCCC");


	$.ajax({
		xhr: function () {
			var xhr = new window.XMLHttpRequest();
			// iPreviousBytesLoaded = 0;
			xhr.upload.addEventListener("progress", function (evt) {
				console.log("Dang tiến hành upload ...");
			}, false);
			return xhr;
		},
		async: true,
		url: "/api/file/upload",
		type: "POST",
		processData: false,
		contentType: false,
		data: data,
		success: function (result) {
			var jo = result;
			if (jo) {
				var countSTT = $('#filelist_' + objectId + '> div').size() + 1;
				var htmlListFile = '<div class="title-file-items file_' + jo.id + '">';
				htmlListFile += '<a class="title-a-items file_' + jo.id + '" href="' + jo.url + '" target="_blank" data="'+jo.id+'" title="' + jo.fileName + '">' + " - " + '. ' + jo.fileName + '</a>';
				htmlListFile += '<input name="fileId_' + objectId + '" value="' + jo.id + '" type="hidden" class="fileId_'+objectId+'" /> '
				htmlListFile += '<span class="btn-del-file file_' + jo.id + '" attachmentid="' + jo.id + '" style="color: #151111; cursor: pointer;">Xóa</span>';
				htmlListFile += '</div>';
				$("#filelist" + objectId).append(htmlListFile);
				$('.daNhan-' + objectId).prop("checked", true);
				$('.pccc-fileuploader-items' + objectId).show();
			}
		},
		error: function (result) {
			console.log("ERROR....");
		}
	});
}
/*load page*/
var loadPage = () => {
	var page_type = $("#page_type").val();
	var page_tab = $("#page_tab").val();
	var id = $("#page_pccc_id").val();
	console.log("_loadDataPcccForEditPage");
	console.log(page_tab);
	console.log(page_type);
	console.log(id);
	if (id > 0 && page_tab !== '' && page_type === 'EDIT_QUANLY_CONGTAC_PCCC') {
		_loadDataPcccForEditPage(id, page_tab);
	} else if (page_type === 'ADD_QUANLY_CONGTAC_PCCC') {
		_loadFormAddPage();
		_drawThanhPhanHoSo();
	} else if (page_type === 'PAGE_CAU_HINH_THANHPHAN') {
		_loadCauHinhThanhPhanPage();
	} else if (page_type === 'HOME_DASHBOARD') {
		_loadDashBoard();
	}
}




