var APP = APP ||  {};
APP.currentGroup = null;
APP.users = [];
APP.groups = [];
APP.userTasks = [];

APP.userTasksTpl = _.template(
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
            <% _.each(userTasks, function(userTask){ %> \
                <tr data-id="<%= userTask.id %>" class="userTask-row">  \
                    <td><%= userTask.position %></td> \
                    <td>\
                        <select data-placeholder="Candidate Groups" class="chosen-select candidate-groups" data-position="<%= userTask.position %>" multiple>\
                        <% _.each(groups, function(group){ %> \
                            <option value="<%= group.id %>" \
                            <% var selected = _.contains(userTask.candidateGroups, group.id); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= group.name %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td>\
                        <select data-placeholder="Candidate Users" class="chosen-select candidate-users" data-position="<%= userTask.position %>" multiple>\
                        <% _.each(users, function(user){ %> \
                            <option value="<%= user.userName %>" \
                            <% var selected = _.contains(userTask.candidateUsers, user.userName); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= user.userName %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td> \
                       <div class="form-group"> \
                            <input type="text" class="form-control userTask-name" id="userTask-name-<%= userTask.name %>" data-position="<%= userTask.position %>" placeholder="Description" value="<%= userTask.name %>"/>\
                        </div>\
                    </td>\
                    <td> \
                        <button type="button" class="btn btn-default add-button" data-position="<%= userTask.position %>" data-id="<%= userTask.id %>"> \
                            <span class="glyphicon glyphicon-plus"></span> \
                        </button>  \
                    </td> \
                    <td> \
                        <button type="button" class="btn btn-default delete-button" data-position="<%= userTask.position %>" data-id="<%= userTask.id %>"> \
                            <span class="glyphicon glyphicon-trash"></span> \
                       </button> \
                    </td> \
                </tr> \
            <% }); %> \
        </tbody> \
    </table>'
);

function addNewUserTaskRow(pos) {
    var newList = [];
    _.each(APP.userTasks, function (userTask, index, list) {
        if (userTask.position <= pos) {
            newList.push(userTask);
        }
        if (userTask.position === pos) {
            var newPos = pos + 1;
            newList.push({
                position: newPos,
                candidateGroups: [],
                candidateUsers: [],
                id: 'approveDocUserTask_' + newPos,
                name: 'Approve Document'
            });
        }
        if (userTask.position > pos) {
            userTask.position += 1;
            newList.push(userTask);
        }
    });
    console.dir(newList);
    APP.userTasks = newList;
}

function removeUserTaskRow(pos) {
    if (APP.userTasks.length < 2) {
        bootbox.alert("At least one userTask is required.", function() {});
        return;
    }
    APP.userTasks.splice(pos -1, 1);
    _.each(APP.userTasks, function (userTask, index, list) {
        userTask.position = index + 1;
    });
}

function getGroups() {
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

function updateUserTasks(group) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT + '/workflow/userTasks/' + group,
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            if (!data.success) {
                alert("There was an error updating the workflow");
            }
            APP.userTasks = data.data;
            updateuserTasksTpl(APP.userTasks);
        },
        error: function (error) {
            alert("There was an error updating the workflow");
        }
    });
}

function updateUserTasksTpl() {
    $('#userTasks-panel').html(APP.userTasksTpl({
        userTasks: APP.userTasks,
        groups: APP.groups,
        users: APP.users
    }));
    $('.chosen-select', '#userTasks-panel').chosen({}).change(function () {
//        console.dir($(this).val());
        var pos = parseInt($(this).attr('data-position'));
        var temp = $(this).val();
        var tempArray = _.isArray(temp) ? temp : [temp];
        if ($(this).hasClass('candidate-groups')) {
            APP.userTasks[pos - 1].candidateGroups = tempArray;
        }
        else {
            APP.userTasks[pos - 1].candidateUsers = tempArray;
        }
    });
    $('input.userTask-name', '#userTasks-panel').on('blur', function(){
        var pos = parseInt($(this).attr('data-position'));
        APP.userTasks[pos - 1].name = $(this).val();
    });
    $('button.add-button', '#userTasks-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        addNewuserTaskRow(parseInt(pos));
        updateuserTasksTpl();
    });
    $('button.delete-button', '#userTasks-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        removeuserTaskRow(parseInt(pos));
        updateuserTasksTpl();
    });
}

function submitUserTasks() {
    $.ajax(SERVLET_CONTEXT + '/workflow/userTasks/' + APP.currentGroup, {
        type: 'PUT',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(APP.userTasks),
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_userTask_ROOT_ID + '-' + APP.currentGroup;
            var rand = _.random(1, 100000000);
            newSrc = newSrc + '?rand=' + rand
            $('#proc-main-diagram').attr('src', newSrc);
            if (!data.success) {
                alert("There was an error getting the app users");
            }
            else {
                updateUserTasks(APP.currentGroup);
            }

        },
        error: function (error) {
            alert("There was an error getting the app users");
        }

    });
}

$(function () {
    $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
    getGroups();
    getUsers();
    $('#update-button').on('click', function () {
        submitUserTasks();
    });
    $('#docTypeSel').change(function(){
        //$("#my-Select option[text=" + myText +"]").attr("selected","selected") ;
        //$("#groupSel option[text=" + "Choose a Document Type" + "]").attr("selected", "selected");
        $("#groupSel").val("");
        var val = $(this).val();
        if (_.isEmpty(val)){
            $('#groupSelForm').addClass('hidden');//.find('option').remove().end();//.append('<option value="whatever">text</option>').val('whatever');
        }
        else {
            $('#groupSelForm').removeClass('hidden');
        }
    });

    $('#groupSel').change(function () {
        APP.currentGroup = $(this).val();
        var docType = $('#docTypeSel').val();
        console.log(APP.currentGroup);
        alert("DocType =" + docType + ", group =" + APP.currentGroup);
        //if (!_.isEmpty(APP.currentGroup)){
        //    $('#userTasks').removeClass('hidden');
        //    var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_userTask_ROOT_ID + '-' + APP.currentGroup;
        //    //need to add random param to avoid caching of the image
        //    var rand = _.random(1, 100000000);
        //    newSrc = newSrc + '?rand=' + rand
        //    $('#proc-main-diagram').attr('src', newSrc);
        //    updateUserTasks(APP.currentGroup);
        //    $('#groupTitle').text(APP.currentGroup);
        //}
        //else {
        //    $('#userTasks').addClass('hidden');
        //    var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_userTask_ROOT_ID;
        //    $('#proc-main-diagram').attr('src', newSrc);
        //    $('#groupTitle').text('Default');
        //}

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


