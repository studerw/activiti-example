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
public class DynamicUserTask implements Comparable<DynamicUserTask> {

    private String id;
    private String name;
    private Integer index;
    private List<String> candidateUsers = Lists.newArrayList();
    private List<String> candidateGroups = Lists.newArrayList();
    private DynamicUserTaskType dynamicUserTaskType;

    public DynamicUserTaskType getDynamicUserTaskType() {
        return dynamicUserTaskType;
    }

    public void setDynamicUserTaskType(DynamicUserTaskType dynamicUserTaskType) {
        this.dynamicUserTaskType = dynamicUserTaskType;
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

    public Integer getIndex() {return index;}

    public void setIndex(Integer index) {
        this.index = index;
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
    public int compareTo(DynamicUserTask o) {
        return ObjectUtils.compare(this.index, o.index);
    }


    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("UserTask{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", index=").append(index);
        sb.append(", candidateUsers=").append(candidateUsers);
        sb.append(", candidateGroups=").append(candidateGroups);
        sb.append(", userTaskType=").append(dynamicUserTaskType);
        sb.append('}');
        return sb.toString();
    }
}
