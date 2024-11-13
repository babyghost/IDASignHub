var _draw_pagination = ($selector, $begin, $end, $total) => {
   let row_data = 0;
    let xhtml = "";
   if ($total > $end) {
       row_data = $total / $end;
   }
    if(row_data > 0) {
        xhtml = '<div class="row">';
        xhtml +='<div class="col-sm-5">';
        xhtml +='<div class="dataTables_info" id="example2_info" role="status" aria-live="polite">';
        xhtml +='Hiển thị '+$begin+' tới '+$end+' trong '+$total+' dữ liệu';
        xhtml +='</div>';
        xhtml +='</div>';
        xhtml +='<div class="col-sm-7">';
        xhtml +='<div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">';
        xhtml +='<ul class="pagination">';
        xhtml +='<li class="paginate_button previous disabled" id="example2_previous">';
        xhtml +='<a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Trước</a>';
        xhtml +='</li>';
        if(row_data > 0) {
            for(let i = 0; i < row_data; i++) {
                xhtml +='<li class="paginate_button">';
                xhtml +='<a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">1</a>';
                xhtml +='</li>';
            }
        }
        xhtml +='<li class="paginate_button next" id="example2_next">';
        xhtml +='<a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Tiếp</a>';
        xhtml +='</li>';
        xhtml +='</ul>';
        xhtml +='</div>';
        xhtml +='</div>';
        xhtml +='</div>';
    }
    $($selector).empty().html(xhtml);
}