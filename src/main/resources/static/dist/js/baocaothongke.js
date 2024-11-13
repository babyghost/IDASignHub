function exportToExcel() {
    let table = document.getElementById("table_baocaothongkethang"); // Get the table element
    TableToExcel.convert(table, {
        name: "baocaothongke_pccc.xlsx", // Set the name of the exported file
        sheet: {
            name: "Sheet1" // Name of the Excel sheet
        }
    });
}
function exportToExcelChiTiet() {
    let table = document.getElementById("table_chitietBaoCao"); // Get the table element
    TableToExcel.convert(table, {
        name: "chitiet_pccc.xlsx", // Set the name of the exported file
        sheet: {
            name: "Sheet1" // Name of the Excel sheet
        }
    });
}


function loadNamBaoCao() {
    let xhttml = '<option value="">Hãy chọn năm hoạt động</option>';
    let current_year = new Date().getFullYear();
    console.log(current_year);
    for(let i =2030; i >= 2015; i--) {
        let select_check = "";
        if(current_year == i) {
            select_check = "selected";
        }
        xhttml+= '<option value="'+i+'"'+select_check+'>Năm '+i+'</option>';
    }
    $("#thongkenamhoatdong").empty().append(xhttml);
}
$('#thongkenamhoatdong').change(function(){
    loadBaoCaoThongKe();
});
$('#table_baocaothongkethang_body').on('click', '.btn_chitiet', function () {
    let donvi = $(this).attr("donvi");
    let data = $(this).attr("data");
    var nn = $('#thongkenamhoatdong option:selected').val();
    var data1 = {
        "donvi" : donvi,
        "thoigian": data,
        "nam": nn
    };
    $.ajax({
        type: "POST",
        url: "/api/congtac-pccc/getBaoCaoThongKeChiTiet",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data1),
        success: function(data)
        {
            var xxhtml = "";
            var arrayDonVi = [
                {id: 'pccc_tanchinh', value: 'UBND phường Tân Chính'},
                {id: 'pccc_xuanha', value: 'UBND phường Xuân Hà'},
                {id: 'pccc_vinhtrung', value: 'UBND phường Vĩnh Trung'},
                {id: 'pccc_thanhkhetay', value: 'UBND phường Thanh Khê Tây'},
                {id: 'pccc_thanhkhedong', value: 'UBND phường Thanh Khê Đông'},
                {id: 'pccc_chinhgian', value: 'UBND phường Chính Gián'},
                {id: 'pccc_ankhe', value: 'UBND phường An Khê'},
                {id: 'pccc_tamthuan', value: 'UBND phường Tam Thuận'},
                {id: 'pccc_thacgian', value: 'UBND phường Thạc Gián'},
                {id: 'pccc_hoakhe', value: 'UBND phường Hòa Khê'}];

                let report = data.data;
                let xhtml = "";
                if(report.length > 0) {
                    report.forEach(function(x, index){
                        // console.log(x);
                        if(x.jsonDataForm1 !== null) {
                            let a = JSON.parse(x.jsonDataForm1);
                            let stylezzz = '';
                            if(index == 0) {
                                stylezzz = 'style="color: #1f6ed6;font-weight: 600;"';
                            }
                            xhtml += '<tr '+stylezzz+'>';
                            xhtml += '<td style="text-align:center">' +(index +1)+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_macoso+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_tencosocanhan+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_diachi+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_namduavaohoatdongStr+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_email+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_dienthoai+ '</td>';
                            xhtml += '<td style="text-align:center">' +a.tab_1_diabankhuvuctrongdiemStr+ '</td>';
                            xhtml += '<td style="text-align:center">' +(a.tab_1_trangthaihoatdong == 1156 ? "Hoạt động": "Ngưng hoạt động")+ '</td>';
                            xhtml += '<td style="text-align:center">';
                            xhtml += '</td></tr>';
                        } else {
                            xhtml += '<tr>';
                            xhtml += '<td colspan="10" style="text-align:center"></td>';
                            xhtml += '</tr>';
                        }

                    });
                } else {
                    xhtml = '<tr><td colspan="10" style="text-align:center">Chưa có dữ liệu</td></tr>';
                }
            $('#table_chitietBaoCao_body').empty().append(xhtml);
        }
    }).error(function (xhr) {

    });
    $("#myModalChiTietBaoCao_Modal").modal('toggle');
});
function loadBaoCaoThongKe() {
    console.log("load baocao");
   var nn = $('#thongkenamhoatdong option:selected').val();
    console.log("nn "+nn);
    var data1 = {
        "nam" : nn
    };
    $.ajax({
        type: "POST",
        url: "/api/congtac-pccc/getBaoCaoThongKe",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data1),
        success: function(data)
        {
            console.log(data);
            var xxhtml = "";
            var arrayDonVi = [
                {id: 'pccc_tanchinh', value: 'UBND phường Tân Chính'},
                {id: 'pccc_xuanha', value: 'UBND phường Xuân Hà'},
                {id: 'pccc_vinhtrung', value: 'UBND phường Vĩnh Trung'},
                {id: 'pccc_thanhkhetay', value: 'UBND phường Thanh Khê Tây'},
                {id: 'pccc_thanhkhedong', value: 'UBND phường Thanh Khê Đông'},
                {id: 'pccc_chinhgian', value: 'UBND phường Chính Gián'},
                {id: 'pccc_ankhe', value: 'UBND phường An Khê'},
                {id: 'pccc_tamthuan', value: 'UBND phường Tam Thuận'},
                {id: 'pccc_thacgian', value: 'UBND phường Thạc Gián'},
                {id: 'pccc_hoakhe', value: 'UBND phường Hòa Khê'}];
            let tongHoSo = data.tongHoSo;
            if(data.reportBaoCaoThongKe.length > 0)
            {
                let report = data.reportBaoCaoThongKe;


                for(let i = 0; i < report.length; i++) {
                    let phuong = "";
                    let xx = arrayDonVi.find(x=> x.id === report[i].donVi);
                    if(xx !== undefined) {
                        phuong =  xx.value;
                    }

                    xxhtml += "<tr>";
                    xxhtml += "<td style=\"text-align: center;\">" +(i + 1)+ "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+phuong+ "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].tongSo > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='tong'>" : "") + report[i].tongSo + (report[i].tongSo > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang1 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='1'>" : "") +report[i].thang1+ (report[i].thang1 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang2 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='2'>" : "") +report[i].thang2+ (report[i].thang2 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang3 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='3'>" : "") +report[i].thang3+ (report[i].thang3 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang4 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='4'>" : "") +report[i].thang4+ (report[i].thang4 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang5 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='5'>" : "") +report[i].thang5+ (report[i].thang5 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang6 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='6'>" : "") +report[i].thang6+ (report[i].thang6 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang7 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='7'>" : "") +report[i].thang7+ (report[i].thang7 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang8 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='8'>" : "") +report[i].thang8+ (report[i].thang8 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang9 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='9'>" : "") +report[i].thang9+ (report[i].thang9 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang10 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='10'>" : "") +report[i].thang10+ (report[i].thang10 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang11 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='11'>" : "") +report[i].thang11+ (report[i].thang11 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "<td style=\"text-align: center;\">"+ (report[i].thang12 > 0 ? "<a class='btn_chitiet' href='javascript:void(0)' donvi='"+report[i].donVi+"' data='12'>" : "") +report[i].thang12+ (report[i].thang12 > 0 ? "<\a>" : "") + "</td>+";
                    xxhtml += "</tr>";
                }
            } else {
                xxhtml ="<tr><td colspan='15'>Chưa có dữ liệu</td></tr>"
            }
            $(".tongHoSo").empty().append(tongHoSo);
            $('#table_baocaothongkethang_body').empty().append(xxhtml);
        }
    }).error(function (xhr) {

    });
}

loadNamBaoCao();
loadBaoCaoThongKe();