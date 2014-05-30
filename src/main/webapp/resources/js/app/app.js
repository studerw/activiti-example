(function ($) {
    function acknowledgeAlert(alertId) {
        $.ajax({
            type: 'DELETE',
            dataType: 'json',
            url: SERVLET_CONTEXT + '/alerts/' + alertId,
            headers: {
                Accept: "application/json"
            },
            success: function (data) {
                console.dir(data);
                var sel = $('#alertCount');
                var updated = parseInt(sel.text()) - 1;
                sel.text(updated);
                if (updated <= 0) {
                    $('#alertMenuGroup').html('').append('' +
                        '<li class="list-group-item">' +
                        '<div class="alert alert-info alert-dropdown-div">You have no alerts</div>' +
                        '</li>');
                }
            },
            error: function (error) {
                alert("There was an error updating the alerts");
            }
        });
    }


    $(document).ready(function () {
        if ($('#alertMenuGroup').length) {
            $('li.alert-dismissable', '#alertMenuGroup').
                bind('close.bs.alert', function () {
                    var alertId = $(this).attr('data-id');
                    acknowledgeAlert(alertId);
                });
        }

    });
})(jQuery);

