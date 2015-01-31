var APP = APP || {};
APP.users = [];
APP.groups = [];
APP.dynamicTasks = [];

APP.dynamicTasksTpl = _.template(
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
            <% _.each(dynamicTasks, function(dynamicTask){ %> \
                <tr data-id="<%= dynamicTask.id %>" class="dynamicTask-row">  \
                    <td><%= dynamicTask.position %></td> \
                    <td>\
                        <select data-placeholder="Candidate Groups" class="chosen-select candidate-groups" data-position="<%= dynamicTask.position %>" multiple>\
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
                        <select data-placeholder="Candidate Users" class="chosen-select candidate-users" data-position="<%= dynamicTask.position %>" multiple>\
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
                            <input type="text" class="form-control dynamicTask-name" id="dynamicTask-name-<%= dynamicTask.name %>" data-position="<%= dynamicTask.position %>" placeholder="Description" value="<%= dynamicTask.name %>"/>\
                        </div>\
                    </td>\
                    <td> \
                        <button type="button" class="btn btn-default add-button" data-position="<%= dynamicTask.position %>" data-id="<%= dynamicTask.id %>"> \
                            <span class="glyphicon glyphicon-plus"></span> \
                        </button>  \
                    </td> \
                    <td> \
                        <button type="button" class="btn btn-default delete-button" data-position="<%= dynamicTask.position %>" data-id="<%= dynamicTask.id %>"> \
                            <span class="glyphicon glyphicon-trash"></span> \
                       </button> \
                    </td> \
                </tr> \
            <% }); %> \
        </tbody> \
    </table>'
);

function addNewDynamicTaskRow(pos) {
    var newList = [];
    _.each(APP.dynamicTasks, function (dynamicTask, index, list) {
        if (dynamicTask.position <= pos) {
            newList.push(dynamicTask);
        }
        if (dynamicTask.position === pos) {
            var newPos = pos + 1;
            newList.push({
                position: newPos,
                candidateGroups: [],
                candidateUsers: [],
                id: 'approveDocDynamicTask_' + newPos,
                name: 'Approve Document'
            });
        }
        if (dynamicTask.position > pos) {
            dynamicTask.position += 1;
            newList.push(dynamicTask);
        }
    });
    console.dir(newList);
    APP.dynamicTasks = newList;
}

function removeDynamicTaskRow(pos) {
    if (APP.dynamicTasks.length < 2) {
        bootbox.alert("At least one dynamicTask is required.", function() {});
        return;
    }
    APP.dynamicTasks.splice(pos -1, 1);
    _.each(APP.dynamicTasks, function (dynamicTask, index, list) {
        dynamicTask.position = index + 1;
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

function getDynamicTasks(group, docType) {

    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: SERVLET_CONTEXT+'/workflow/'+docType +'/'+group+'/dynamicTasks/',
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            if (!data.success) {
                alert("There was an error updating the workflow");
            }
            APP.dynamicTasks = data.data;
            updateDynamicTasksTpl(APP.dynamicTasks);
        },
        error: function (error) {
            alert("There was an error updating the workflow");
        }
    });
}

function updateDynamicTasksTpl() {
    $('#dynamicTasks-panel').html(APP.dynamicTasksTpl({
        dynamicTasks: APP.dynamicTasks,
        groups: APP.groups,
        users: APP.users
    }));
    $('.chosen-select', '#dynamicTasks-panel').chosen({}).change(function () {
//        console.dir($(this).val());
        var pos = parseInt($(this).attr('data-position'));
        var temp = $(this).val();
        var tempArray = _.isArray(temp) ? temp : [temp];
        if ($(this).hasClass('candidate-groups')) {
            APP.dynamicTasks[pos - 1].candidateGroups = tempArray;
        }
        else {
            APP.dynamicTasks[pos - 1].candidateUsers = tempArray;
        }
    });
    $('input.dynamicTask-name', '#dynamicTasks-panel').on('blur', function(){
        var pos = parseInt($(this).attr('data-position'));
        APP.dynamicTasks[pos - 1].name = $(this).val();
    });
    $('button.add-button', '#dynamicTasks-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        addNewDynamicTaskRow(parseInt(pos));
        updateDynamicTasksTpl();
    });
    $('button.delete-button', '#dynamicTasks-panel').on('click', function () {
        var pos = $(this).attr('data-position');
        removeDynamicTaskRow(parseInt(pos));
        updateDynamicTasksTpl();
    });
}

function updateDynamicTasks(group, docType) {
    var _group = group;
    var _docType = docType;
    $.ajax(SERVLET_CONTEXT + '/workflow/' + _docType + '/' + _group + '/dynamicTasks/', {
        type: 'PUT',
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(APP.dynamicTasks),
        headers: {
            Accept: "application/json"
        },
        success: function (data) {
            console.dir(data);
            //var newSrc = SERVLET_CONTEXT + '/workflow/' + _group + '/diagrams/' + DOC_dynamicTask_ROOT_ID + '-' + ;
            var newSrc ="http://placehold.it/800x200.png";
            var rand = _.random(1, 100000000);
            //newSrc = newSrc + '?rand=' + rand
            $('#proc-main-diagram').attr('src', newSrc);
            if (!data.success) {
                alert("There was an error updating the workflow.");
            }
            else {
                updateDynamicTasks(_group, _docType);
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
        var group = $("#groupSel").val();
        var docType = $('#docType').val();
        if (!_.isEmpty(group) && !_.isEmpty(docType)) {
            updateDynamicTasks(group);
        }
    });
    //hide and show the group select based on docType
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
        var group =  $(this).val();
        var docType = $('#docTypeSel').val();
        if (!_.isEmpty(group) && !_.isEmpty(docType)){
            alert("DocType =" + docType + ", group =" + group);
        }
        //    $('#dynamicTasks').removeClass('hidden');
        //    var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_dynamicTask_ROOT_ID + '-' + group;
        //    //need to add random param to avoid caching of the image
        //    var rand = _.random(1, 100000000);
        //    newSrc = newSrc + '?rand=' + rand
        //    $('#proc-main-diagram').attr('src', newSrc);
        getDynamicTasks(group, docType);
        //    $('#groupTitle').text(group);
        //}
        //else {
        //    $('#dynamicTasks').addClass('hidden');
        //    var newSrc = SERVLET_CONTEXT + '/workflow/diagrams/' + DOC_dynamicTask_ROOT_ID;
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


