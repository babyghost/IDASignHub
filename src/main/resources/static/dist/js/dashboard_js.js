var configArray = [];
var currentConfig = {};
$(document).ready(function () {
    // Đăng ký action cho các block
    $(".btn_kySoFolderModal").click(function () {
        loadDataConfigFormKySo();
        $("#kySoFolderModal").modal('toggle');
    });
    $(".btn_kySoFolderAutoModal").click(function () {
        $("#kySoFolderAutoModal").modal('toggle');
    });

    $(".btn_kySoZipModal").click(function () {
        // loadDataConfigFormKySo();
        $("#kySoZipModal").modal('toggle');
    })
    $(".btn_kySoZipModal").click(function () {
        // loadDataConfigFormKySo();
        $("#kySoZipModal").modal('toggle');
    })
    $(".btn_CountFileFromFolderAutoModal").click(function () {
        $("#countPathModal").modal('toggle');
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


    $(".btn_CreateJsonDataFromFolderExcel").click(function () {
       console.log("btn_CreateJsonDataFromFolderExcel");
        $.ajax({
            url: 'api/pdf/data-excel-json', // Địa chỉ endpoint của backend
            type: 'POST',
            data: null,
            processData: false, // Không xử lý data (FormData tự xử lý)
            contentType: false, // Để contentType là false để jQuery không đặt header mặc định
            success: function (response) {
                alert("Đã xong.");
            },
            error: function (xhr, status, error) {
                alert("Thất bại.");
            }
        });
    });
$(".btn_KichHoatFixHoSo").click(function () {
    $.ajax({
        url: 'api/pdf/fix-import-hoso', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("Thất bại.");
        }
    });
});
$(".btn_KichHoatKySo").click(function () {
    $.ajax({
        url: '/api/runAutoScan', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        before: function () {
            alert("Khởi động tiến trình ký số.");
        },
        success: function (response) {
            alert("Đã hoàn thành.");
        },
        error: function (xhr, status, error) {
            alert("Ký số thất bại.");
        }
    });
})
$(".btn_KichHoatNenPDF").click(function () {
    $.ajax({
        url: '/api/pdf/compress', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã khởi động tool.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});
$(".btn_KichHoatExportFileTemplateExcel").click(function() {
    $.ajax({
        url: '/api/create/file-import', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});
$(".btn_TachFilePDF").click(function() {
    $.ajax({
        url: '/api/pdf/file-pdf-split', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});
$(".btn_LocFilePDF").click(function() {
    $.ajax({
        url: '/api/pdf/file-pdf-filter', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});
$(".btn_fixFileExcel").click(function() {
    $.ajax({
        url: '/api/pdf/fix-file-excel', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});
$(".btn_copyFile").click(function() {
    $.ajax({
        url: '/api/pdf/file-copy-from-folder', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});

$(".btn_KichHoatExportFileTemplateExcelVanBan").click(function() {
    $.ajax({
        url: '/api/create/file-import-vanban', // Địa chỉ endpoint của backend
        type: 'POST',
        data: null,
        processData: false, // Không xử lý data (FormData tự xử lý)
        contentType: false, // Để contentType là false để jQuery không đặt header mặc định
        success: function (response) {
            alert("Đã xong.");
        },
        error: function (xhr, status, error) {
            alert("thất bại.");
        }
    });
});

$(".btn-kyso-folder").click(function () {
    $(".btn-kyso-folder").text("Đang thực hiện ký số ...");
    // bước chuẩn bị dữ liệu ký
    let path_folder_kyso = $("#path_folder_kyso").val();
    let vitrikyso = $("#vitri_trentrang").val();
    var hienthi = $('input[name="thongtin_hienthi"]:checked').val();
    var loai_kyso = $('input[name="loai_kyso"]:checked').val();
    var files = $('#folder_ky')[0].files;
    if (files.length === 0) {
        alert("Please select files to zip.");
        return;
    }
    console.log(files.length);
    var zip = new JSZip();

    Array.from(files).forEach(file => {
        zip.file(file.name, file);
    });
    setTimeout(() => {
        alert("Ký số thành công. Bạn có thể chọn folder để ký tiếp.");
        $(".btn-kyso-folder").text("Ký số");
        $("#kySoFolderModal").modal('toggle');
    }, 2000);

    // zip.generateAsync({type: "blob"}).then(function (content) {
    //     var formData = new FormData();
    //     formData.append('file', content, 'files.zip');
    //     // formData.append('xLocation', xLocation);
    //     // formData.append('yLocation', null);
    //     // formData.append('height', null);
    //     // formData.append('width', null);
    //     formData.append('pageSign', kyTaiTrangSo);
    //     formData.append('showType', hienthi);
    //     formData.append('signatureType', loai_kyso);
    //     formData.append('locationSign', vitrikyso);
    //
    //     // formData.append('signatureImg', null);
    //
    //     // Gửi zip file lên backend qua AJAX
    //     // $.ajax({
    //     //     url: '/api/signPDFs', // Địa chỉ endpoint của backend
    //     //     type: 'POST',
    //     //     data: formData,
    //     //     processData: false, // Không xử lý data (FormData tự xử lý)
    //     //     contentType: false, // Để contentType là false để jQuery không đặt header mặc định
    //     //     success: function (response) {
    //     //         alert("Ký số thành công. Bạn có thể chọn folder để ký tiếp.");
    //     //     },
    //     //     error: function (xhr, status, error) {
    //     //         alert("Ký số thất bại.");
    //     //     }
    //     // });
    //     // end tab
    // }); // end tab generateAsync
    }); // end click

    $(".btn-kyso-folder-auto").click(function () {
        $(".btn-kyso-folder-auto").text("Đang thực hiện ký số ...");
        // bước chuẩn bị dữ liệu ký
        let $path_folder_kyso = $("#path_folder_kyso").val();
        $.ajax({
            url: '/api/signFolderByPath', // Địa chỉ endpoint của backend
            type: 'POST',
            contentType: 'application/json; charset=UTF-8', // tell server you’re sending JSON
            dataType: 'json',
            data: JSON.stringify({
                pathFolder: $path_folder_kyso
            }),
            success: function (response) {
                alert("Ký số thành công. Bạn có thể chọn folder để ký tiếp.");
            },
            error: function (xhr, status, error) {
                alert("Ký số thất bại.");
            }
        });
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


$(".btn_pupperTeer").click(function () {
    $.ajax({
        url: '/api/run-pupperteer', // Địa chỉ endpoint của backend
        type: 'POST',
        contentType: 'application/json; charset=UTF-8', // tell server you’re sending JSON
        dataType: 'json',
        data: JSON.stringify({
            pathFolder: ""
        }),
        success: function (response) {
            alert("Đã kích hoạt....");
        },
        error: function (xhr, status, error) {
            alert("Action thất bại.");
        }
    });
});
$(".btn-count-file-from-folder").click(function () {
    $(".btn-count-file-from-folder").text("Đang thực hiện ký số ...");
    // bước chuẩn bị dữ liệu ký
    let $path = $("#countPathModal_auto").val();
    console.log($path);

    $.ajax({
        url: '/api/countDataFromFolderRoot', // Địa chỉ endpoint của backend
        type: 'POST',
        contentType: 'application/json; charset=UTF-8', // tell server you’re sending JSON
        dataType: 'json',
        data: JSON.stringify({
            pathFolder: $path
        }),
        success: function (response) {
            alert("Đã count xong.");
        },
        error: function (xhr, status, error) {
            alert("Ký số thất bại.");
        }
    });
});