var configArray = [];
var currentConfig = {};
$(document).ready(function () {
    // Đăng ký action cho các block
    $(".btn_kySoFolderModal").click(function () {
        loadDataConfigFormKySo();
        $("#kySoFolderModal").modal('toggle');
    });
    $(".btn_kySoZipModal").click(function () {
        // loadDataConfigFormKySo();
        $("#kySoZipModal").modal('toggle');
    })
    $(".btn_TaoPDF2Lop").click(function () {
        $.ajax({
            url: '/api/makePDF2Layer', // Địa chỉ endpoint của backend
            type: 'GET',
            data: null,
            processData: false, // Không xử lý data (FormData tự xử lý)
            contentType: false, // Để contentType là false để jQuery không đặt header mặc định
            success: function (response) {
                alert("Ký số thành công. Bạn có thể chọn folder để ký tiếp.");
            },
            error: function (xhr, status, error) {
                alert("Ký số thất bại");
            }
        });
    });
    //End block

    $("#mau_kyso").change(function() {
        console.log("....");
        console.log(configArray);
       let index = $(this).val();
        console.log(index);
        currentConfig = configArray[index];
        console.log(currentConfig);
        //load config form
        loadConfigForm(currentConfig);
    });


    $(".btn-kyso-folder").click(function () {
        // bước chuẩn bị dữ liệu ký
        let kyTaiTrangSo = $("#kysotaitrang").val();
        let vitrikyso = $("#vitri_trentrang").val();
        var hienthi = $('input[name="thongtin_hienthi"]:checked').val();
        var loai_kyso = $('input[name="loai_kyso"]:checked').val();
        // bước nén danh sách file nhận được từ input để gửi lên
        var files = $('#folder_ky')[0].files; // Lấy danh sách file từ input
        if (files.length === 0) {
            alert("Please select files to zip.");
            return;
        }
        console.log(files.length);
        var zip = new JSZip();

        // Duyệt qua các file và thêm vào zip
        Array.from(files).forEach(file => {
            zip.file(file.name, file);
        });
        // bước 2 sử dụng ajax gửi data lên api
        zip.generateAsync({type: "blob"}).then(function (content) {
            var formData = new FormData();
            formData.append('file', content, 'files.zip');
            // formData.append('xLocation', xLocation);
            // formData.append('yLocation', null);
            // formData.append('height', null);
            // formData.append('width', null);
            formData.append('pageSign', kyTaiTrangSo);
            formData.append('showType', hienthi);
            formData.append('signatureType', loai_kyso);
            formData.append('locationSign', vitrikyso);

            // formData.append('signatureImg', null);

            // Gửi zip file lên backend qua AJAX
            $.ajax({
                url: '/api/signPDFs', // Địa chỉ endpoint của backend
                type: 'POST',
                data: formData,
                processData: false, // Không xử lý data (FormData tự xử lý)
                contentType: false, // Để contentType là false để jQuery không đặt header mặc định
                success: function (response) {
                    alert("Ký số thành công. Bạn có thể chọn folder để ký tiếp.");
                },
                error: function (xhr, status, error) {
                    alert("Ký số thất bại.");
                }
            });
            // end tab
        }); // end tab generateAsync
    }); // end click
});
function loadDataConfigFormKySo() {
    $.ajax({
        url: '/api/list/config',
        type: 'GET',
        data: null,
        success: function (data) {
            let xhtml    = "";
            let requestObject   = data.requestObject;
            configArray = requestObject.config;

            let i = 0;
            for(i = 0; i < configArray.length; i++) {
                let checker = '';
                let config = requestObject.config[i];
                if(config.trangThaiHoatDong === true) {
                    currentConfig =  config;
                    checker = 'selected';
                    //load config form
                    loadConfigForm(currentConfig);
                }
                xhtml += '<option value="'+i+'" '+checker+'>'+ config.nameSignHubLocalType +'</option>';
            }
            $("#mau_kyso").html("").append(xhtml);


        },
        error: function () {
            console.log("Error loading data.");
        }
    }); // end ajax function
} // end function

function loadConfigForm($config) {
// fill dữ liệu vào form config
    if($config !== undefined) {
        let img_mauky = "";
        if($config.pathImageSignHubLocalType !== "") {
            img_mauky = '<img width="100" height="100" src="data:image/jpeg;base64, '+$config.pathImageSignHubLocalType+'" />';
        }
        $(".mau_ky_img").html(img_mauky);
        //Hien thi
        $('input[name="thongtin_hienthi"][value="'+$config.showInfoSignHubLocalType+'"]').prop('checked', true);
        // ký tại trang số
        $('#kysotaitrang').val($config.pageNumberSignHubLocalType);
        //vị trí trên trang
        $('#vitri_trentrang').val($config.locationSignHubLocalType);
    }

}