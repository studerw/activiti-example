var APP = APP || {};
APP.users = [];
APP.groups = [];
APP.dynamicTasks = [];
APP.BASE_URL = SERVLET_CONTEXT + '/workflow/dynamicTasks/';

APP.dynamicTasksTpl = _.template(
    '<table id="taskListTable" class="table table-striped"> \
        <thead> \
            <tr> \
                <td>Position</td> \
                <td>Candidate Groups</td> \
                <td>Candidate Users</td> \
                <td>Description</td> \
                <td>&nbsp;</td> \
                <td>&nbsp;</td> \
            </tr> \
        </thead> \
        <tbody>  \
            <% _.each(dynamicTasks, function(dynamicTask){ %> \
                <tr data-id="<%= dynamicTask.id %>" class="dynamicTask-row">  \
                    <td><%= dynamicTask.index %></td> \
                    <td>\
                        <select data-placeholder="Groups" class="chosen-select candidate-groups" data-position="<%= dynamicTask.index %>" multiple>\
                        <% _.each(groups, function(group){ %> \
                            <option value="<%= group.id %>" \
                            <% var selected = _.contains(dynamicTask.candidateGroups, group.id); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= group.name %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td>\
                        <select data-placeholder="Users" class="chosen-select candidate-users" data-position="<%= dynamicTask.index %>" multiple>\
                        <% _.each(users, function(user){ %> \
                            <option value="<%= user.userName %>" \
                            <% var selected = _.contains(dynamicTask.candidateUsers, user.userName); %>\
                            <% if(selected){ %>\
                                selected \
                                <% } %>\
                            ><%= user.userName %></option>\
                         <% }); %>\
                        </select>\
                    </td> \
                    <td> \
                       <div class="form-group"> \
                        <p class="form-control-static"><%= dynamicTask.name %></p>\
                        </div>\
                    </td>\
                    <td>\
                        <div class="btn-group pull-right">\
                            <button type="button" class="btn btn-default dropdown-toggle btn-primary" data-toggle="dropdown" aria-expanded="false">\
                                <span class="glyphicon glyphicon-plus"/>\
                            </button>\
                            <ul class="dropdown-menu" role="menu">\
                                <li><a class="add-button" data-position="<%= dynamicTask.index %>" data-id="<%= dynamicTask.id %>" data-taskType="APPROVE_REJECT" href="#">Approval</a></li>\
                                <li><a class="add-button" data-position="<%= dynamicTask.index %>" data-id="<%= dynamicTask.id %>" data-taskType="COLLABORATION" href="#">Collaboration</a></li>\
                            </ul>\
                        </div>\
                    </td>\
                    <td> \
                        <button type="button" class="btn btn-danger delete-button" data-position="<%= dynamicTask.index %>" data-id="<%= dynamicTask.id %>"> \
                            <span class="glyphicon glyphicon-trash"></span> \
                       </button> \
                    </td> \
                </tr> \
            <% }); %> \
        </tbody> \
    </table>'
);

function refreshTpl() {
    if (_.isEmpty(APP.dynamicTasks)) {
        $('#emptyTaskListAddBtn').show();
    }
    else {
        $('#emptyTaskListAddBtn').hide();
    }
    $('#userTasks-panel').html(APP.dynamicTasksTpl({
        dynamicTasks: APP.dynamicTasks,
        groups: APP.groups,
        users: APP.users
    }));
    if (_.isEmpty(APP.dynamicTasks)) {
        $('#taskListTable').hide();
    }
    else {
        $('#taskListTable').show();
    }
    $('.chosen-select', '#userTasks-panel').chosen({width: '100%'}).change(function () {
        var pos = parseInt($(this).attr('data-position'));
        var temp = $(this).val();
        var tempArray = _.isArray(temp) ? temp : [temp];
        if ($(this).hasClass('candidate-groups')) {
            APP.dynamicTasks[pos].candidateGroups = tempArray;
        }
        else {
            APP.dynamicTasks[pos].candidateUsers = tempArray;
        }
    });
    $('input.dynamicTask-name', '#userTasks-panel').on('blur', function () {
        var pos = parseInt($(this).attr('data-position'));
        APP.dynamicTasks[pos].name = $(this).val();
    });
    $('a.add-button', '#userTasks-panel').on('click', function (evt) {
        //$(this).parents('ul.dropdown-menu').toggle();
        var _pos = parseInt($(this).attr('data-position'));
        var _taskType = $(this).attr('data-taskType');
        addTaskRow(_pos, _taskType);
        return false;
    });
    $('button.delete-button', '#userTasks-panel').on('click', function () {
        var pos = parseInt($(this).attr('data-position'));
        removeTaskRow(pos);
        return false;
    });
}

//function countByType() {
//    var counts = {};
//    _.each(APP.dynamicTaskTypes, function (taskType, index, list) {
//        var temp = _.filter(APP.dynamicTasks, function (task) {
//            return task.dynamicUserTaskType === taskType;
//        });
//        counts[taskType] = temp.length;
//    });
//    return counts;
//}
//
//function getTaskName(pos, taskType) {
//    var current = pos+1;
//    var total = countByType()[taskType];
//    if (_.isUndefined(total)) {
//        return taskType + " " + current;
//    }
//    else {
//        return taskType + " " + current + " of " +total;
//    }
//}

function addTaskRow(pos, taskType) {
    var updated = [];
    if (_.isEmpty(APP.dynamicTasks)) {
        var strPos = pos + 1;
        updated.push({
            index: pos,
            candidateGroups: [],
            candidateUsers: [],
            name: taskType,
            dynamicUserTaskType: taskType
        });
    }
    else {
        _.each(APP.dynamicTasks, function (dynamicTask, index, list) {
            if (dynamicTask.index <= pos) {
                updated.push(dynamicTask);
            }
            if (dynamicTask.index === pos) {
                var strPos = pos + 1;
                updated.push({
                    index: pos + 1,
                    candidateGroups: [],
                    candidateUsers: [],
                    name: taskType,
                    dynamicUserTaskType: taskType
                });
            }
            if (dynamicTask.index > pos) {
                dynamicTask.index += 1;
                updated.push(dynamicTask);
            }
        });
    }
    console.dir(updated);
    APP.dynamicTasks = updated;
    refreshTpl();
}

function removeTaskRow(pos) {
    APP.dynamicTasks.splice(pos, 1);
    _.each(APP.dynamicTasks, function (dynamicTask, index, list) {
        dynamicTask.index = index;
    });
    refreshTpl();
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

function getDynamicTasks(group, docType) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: APP.BASE_URL + docType + '/' + group + '',
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            if (!data.success) {
                alert(data.message);
                return;
            }
            $('#tasksGroupLabel').text(data.message);
            APP.dynamicTasks = data.data;
            refreshTpl(APP.dynamicTasks);
            $('#dynamicTasks').removeClass('hidden').addClass('show');
            $('#proc-main-diagram').attr('src', randPlaceholder("800x200"));
            updateDiagram();
        },
        error: function (error) {
            alert("There was an error updating the workflow");
        }
    });
}


function updateDynamicTasks(group, docType) {
    if (!validateTasks()) {
        bootbox.alert("Either Candidate Users and/or Candidate Groups must be assigned for each Task.", function () {
        });
        return false;
    }
    $.ajax({
        url: APP.BASE_URL + docType + '/' + group,
        type: 'PUT',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(APP.dynamicTasks),
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            if (!data.success) {
                bootbox.alert("There was an error updating the workflow - " + data.message, function () {

                });
                return;
            }
            //var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + _docType + '/' + _group;
            //var newSrc = "http://placehold.it/800x200.png";
            //var rand = _.random(1, 100000000);
            //newSrc = newSrc + '?rand=' + rand;
            getDynamicTasks(group, docType);

        },
        error: function (error) {
            bootbox.alert("There was an error updating the workflow", function () {

            });
        }

    });
}

function updateDiagram() {
    var rand = _.random(1, 100000000);
    var group = $("#groupSel").val();
    var docType = $('#docTypeSel').val();
    var url = SERVLET_CONTEXT + '/workflow/diagram/'+docType +'/'+ group
    url += ('?rand=' + rand);
    $('#proc-main-diagram').attr('src', url);
    //var url = SERVLET_CONTEXT + '/workflow/diagram/dynamicTasks?base64=true&rand=' + rand;
    //$.ajax({
    //    url: url,
    //    type: 'POST',
    //    dataType: 'json',
    //    contentType: "application/json; charset=utf-8",
    //    data: JSON.stringify(APP.dynamicTasks),
    //    headers: {
    //        //Accept: "application/json"
    //    },
    //    success: function (data) {
    //        if (!data.success) {
    //            bootbox.alert("There was an error updating the workflow - " + data.message, function () {
    //            });
    //            return;
    //        }
    //        console.dir(data);
    //        var base64 = 'data:image/png;base64,' + data.data;
    //        $('#proc-main-diagram').attr('src', base64);
    //    },
    //    error: function (error) {
    //        debugger;
    //        bootbox.alert("There was an error updating the workflow image", function () {
    //        });
    //    }
    //});
}

function randPlaceholder(dimensions) {
    var rand = _.random(1, 100000000);
    //http://placehold.it/300&text=placehold.it+rocks!
    var url = 'http://placehold.it/' + dimensions + '&text=' + rand + '.png';
    return url;
}

function validateTasks() {
    var emptyCandidates = _.some(APP.dynamicTasks, function (current, idx) {
        return _.isEmpty(current.candidateGroups) && _.isEmpty(current.candidateUsers);
    });
    return !emptyCandidates;
}

$(function () {
    $(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
    getGroups();
    getUsers();
    $('#update-button').on('click', function () {
        var group = $("#groupSel").val();
        var docType = $('#docTypeSel').val();
        if (!_.isEmpty(group) && !_.isEmpty(docType)) {
            updateDynamicTasks(group, docType);
        }
    });
    //hide and show the group select based on docType
    $('#docTypeSel').change(function () {
        //$("#my-Select option[text=" + myText +"]").attr("selected","selected") ;
        //$("#groupSel option[text=" + "Choose a Document Type" + "]").attr("selected", "selected");
        $("#groupSel").val('');
        $("#groupSel").trigger("change");
        var val = $(this).val();
        if (_.isEmpty(val)) {
            $('#dynamicTasks').removeClass('show').addClass('hidden');
            $('#groupSelForm').addClass('hidden');//.find('option').remove().end();//.append('<option value="whatever">text</option>').val('whatever');
        }
        else {
            $('#groupSelForm').removeClass('hidden');
        }
    });

    $('#groupSel').change(function () {
        var group = $(this).val();
        var docType = $('#docTypeSel').val();
        if (_.isEmpty(group) || _.isEmpty(docType)) {
            $('#dynamicTasks').removeClass('show').addClass('hidden');
            return;
        }
        //alert("DocType =" + docType + ", group =" + group);
        getDynamicTasks(group, docType);
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

    $('#emptyCollabBtn').click(function (evt) {
        //$('.dropdown-menu', $('#emptyTaskListAddBtn')).toggle();
        addTaskRow(0, 'COLLABORATION');
        return false;
    });
    $('#emptyApproveBtn').click(function (evt) {
        //$('.dropdown-menu', $('#emptyTaskListAddBtn')).toggle();
        addTaskRow(0, 'APPROVE_REJECT');
        return false;
    });

});


