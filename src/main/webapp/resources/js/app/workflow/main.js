var APP = {};
APP.currentGroup = null;
APP.users = [];
APP.groups = [];
APP.approvals = [];

APP.approvalsTpl = _.template(
    '<table class="table table-striped"> \
        <thead> \
            <tr> \
                <td>Position</td> \
                <td>Candidate Groups</td> \
                <td>Candidate Users</td> \
                <td>Description</td> \
                <td>Add</td> \
                <td>Delete</td> \
            </tr> \
        </thead> \
        <tbody>  \
            <% _.each(approvals, function(approval){ %> \
                <tr data-id="<%= approval.id %>" class="approval-row">  \
                    <td><%= approval.position %></td> \
                    <td>\
                        <select data-placeholder="Candidate Groups" class="chosen-select candidate-groups" data-position="<%= approval.position %>" multiple>\
                        <% _.each(groups, function(group){ %> \
                            <option value="<%= group.id %>" \
                            <% var selected = _.contains(approval.candidateGroups, group.id); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= group.name %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td>\
                        <select data-placeholder="Candidate Users" class="chosen-select candidate-users" data-position="<%= approval.position %>" multiple>\
                        <% _.each(users, function(user){ %> \
                            <option value="<%= user.userName %>" \
                            <% var selected = _.contains(approval.candidateUsers, user.userName); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= user.userName %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td> \
                       <div class="form-group"> \
                            <input type="text" class="form-control approval-name" id="approval-name-<%= approval.name %>" data-position="<%= approval.position %>" placeholder="Description" value="<%= approval.name %>"/>\
                        </div>\
                    </td>\
                    <td> \
                        <button type="button" class="btn btn-default add-button" data-position="<%= approval.position %>" data-id="<%= approval.id %>"> \
                            <span class="glyphicon glyphicon-plus"></span> \
                        </button>  \
                    </td> \
                    <td> \
                        <button type="button" class="btn btn-default delete-button" data-position="<%= approval.position %>" data-id="<%= approval.id %>"> \
                            <span class="glyphicon glyphicon-trash"></span> \
                       </button> \
                    </td> \
                </tr> \
            <% }); %> \
        </tbody> \
    </table>'
);

function addNewApprovalRow(pos) {
    var newList = [];
    _.each(APP.approvals, function (approval, index, list) {
        if (approval.position <= pos) {
            newList.push(approval);
        }
        if (approval.position === pos) {
            var newPos = pos + 1;
            newList.push({
                position: newPos,
                candidateGroups: [],
                candidateUsers: [],
                id: 'approveDocUserTask_' + newPos,
                name: 'Approve Document'
            });
        }
        if (approval.position > pos) {
            approval.position += 1;
            newList.push(approval);
        }
    });
    console.dir(newList);
    APP.approvals = newList;
}

function removeApprovalRow(pos) {
    if (APP.approvals.length < 2) {
        bootbox.alert("At least one approval is required.", function() {});
        return;
    }
    APP.approvals.splice(pos -1, 1);
    _.each(APP.approvals, function (approval, index, list) {
        approval.position = index + 1;
    });
}

function getGroups() {
    $.blockUI();
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT + '/groups',
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            if (!data.success) {
                alert("There was an error getting the app groups");
            }
            else {
                APP.groups = data.data;
                console.dir(APP.groups);
            }
        },
        error: function (error) {
            alert("There was an error getting the groups");
        }
    });

}

function getUsers() {
    $.blockUI();
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT + '/users',
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            if (!data.success) {
                alert("There was an error getting the app users");
            }
            else {
                APP.users = data.data;
                console.dir(APP.users);
            }

        },
        error: function (error) {
            alert("There was an error getting the app users");
        }
    });
}

function updateApprovals(group) {
    $.blockUI();
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT + '/workflow/approvals/' + group,
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            if (!data.success) {
                alert("There was an error updating the workflow");
            }
            APP.approvals = data.data;
            updateApprovalsTpl(APP.approvals);
        },
        error: function (error) {
            alert("There was an error updating the workflow");
        }
    });
}

function updateApprovalsTpl() {
    $('#approvals-panel').html(APP.approvalsTpl({
        approvals: APP.approvals,
        groups: APP.groups,
        users: APP.users
    }));
    $('.chosen-select', '#approvals-panel').chosen({}).change(function () {
//        console.dir($(this).val());
        var pos = parseInt($(this).attr('data-position'));
        var temp = $(this).val();
        var tempArray = _.isArray(temp) ? temp : [temp];
        if ($(this).hasClass('candidate-groups')) {
            APP.approvals[pos - 1].candidateGroups = tempArray;
        }
        else {
            APP.approvals[pos - 1].candidateUsers = tempArray;
        }
    });
    $('input.approval-name', '#approvals-panel').on('blur', function(){
        var pos = parseInt($(this).attr('data-position'));
        APP.approvals[pos - 1].name = $(this).val();
    });
    $('button.add-button', '#approvals-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        addNewApprovalRow(parseInt(pos));
        updateApprovalsTpl();
    });
    $('button.delete-button', '#approvals-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        removeApprovalRow(parseInt(pos));
        updateApprovalsTpl();
    });
}

function submitApprovals() {
    $.blockUI();
    $.ajax(SERVLET_CONTEXT + '/workflow/approvals/' + APP.currentGroup, {
        type: 'PUT',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(APP.approvals),
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_APPROVAL_ROOT_ID + '-' + APP.currentGroup;
            var rand = _.random(1, 100000000);
            newSrc = newSrc + '?rand=' + rand
            $('#proc-main-diagram').attr('src', newSrc);
            if (!data.success) {
                alert("There was an error getting the app users");
            }
            else {
                updateApprovals(APP.currentGroup);
            }

        },
        error: function (error) {
            alert("There was an error getting the app users");
        }

    });
}

$(function () {
    $(document).ajaxStop($.unblockUI);
    getGroups();
    getUsers();
    $('#update-button').on('click', function () {
        submitApprovals();
    });
    $('#groupSel').change(function () {
        APP.currentGroup = $(this).val();
        console.log(APP.currentGroup);
        if (APP.currentGroup !== '' && APP.currentGroup !== null) {
            $('#approvals').removeClass('hidden');
            var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_APPROVAL_ROOT_ID + '-' + APP.currentGroup;
            var rand = _.random(1, 100000000);
            newSrc = newSrc + '?rand=' + rand
            $('#proc-main-diagram').attr('src', newSrc);
            updateApprovals(APP.currentGroup);
            $('#groupTitle').text(APP.currentGroup);
        }
        else {
            $('#approvals').addClass('hidden');
            var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_APPROVAL_ROOT_ID;
            $('#proc-main-diagram').attr('src', newSrc);
            $('#groupTitle').text('Default');
        }

    });
    //set up JQuery choosen plugin
    var config = {
        '.chosen-select': {},
        '.chosen-select-deselect': {allow_single_deselect: true},
        '.chosen-select-no-single': {disable_search_threshold: 10},
        '.chosen-select-no-results': {no_results_text: 'Oops, nothing found!'}
//        '.chosen-select-width': {width: "95%"}
    }
    for (var selector in config) {
        $(selector).chosen(config[selector]);
    }

});


