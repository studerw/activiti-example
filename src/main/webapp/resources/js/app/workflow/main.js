$(function () {
    $('#groupSel').change(function () {
        console.log($(this).val());
        window.location = SERVLET_CONTEXT + '/workflow/index.htm?group=' + $(this).val();
    });
});
