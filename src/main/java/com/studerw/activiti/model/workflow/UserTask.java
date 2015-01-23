package com.studerw.activiti.model.workflow;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Basic model of a single dynamic user task within the document's workflow
 * that can be modified within the dynamic sub process.
 * @author William Studer
 * Date: 5/29/14
 */
public class UserTask implements Comparable<UserTask> {

    private String id;
    private String name;
    private Integer position;
    private List<String> candidateUsers = Lists.newArrayList();
    private List<String> candidateGroups = Lists.newArrayList();
    private UserTaskType userTaskType;

    public UserTaskType getUserTaskType() {
        return userTaskType;
    }

    public void setUserTaskType(UserTaskType userTaskType) {
        this.userTaskType = userTaskType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPosition() {return position;}

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    @Override
    public int compareTo(UserTask o) {
        return ObjectUtils.compare(this.position, o.position);
    }


    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("UserTask{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", position=").append(position);
        sb.append(", candidateUsers=").append(candidateUsers);
        sb.append(", candidateGroups=").append(candidateGroups);
        sb.append(", userTaskType=").append(userTaskType);
        sb.append('}');
        return sb.toString();
    }
}
