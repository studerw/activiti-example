package com.studerw.activiti.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * Basic model of a single approval task within the document (or other) workflow.
 * User: studerw
 * Date: 5/29/14
 */
public class Approval implements Comparable<Approval>{

    private String id;
    private String name;
    private Integer position;
    private List<String> candidateUsers = Lists.newArrayList();
    private List<String> candidateGroups = Lists.newArrayList();

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

    public Integer getPosition() {
        return position;
    }

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
    public int compareTo(Approval o) {
        return ObjectUtils.compare(this.position, o.position);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("position", position)
                .add("candidateUsers", candidateUsers)
                .add("candidateGroups", candidateGroups)
                .toString();
    }
}
