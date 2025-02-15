package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.Group;

import java.util.List;

public interface GroupRepo
{
    public int createGroup(Group group);
    public Group getGroup(int id);
    public int getGroupOwnerId(int groupId);
    public boolean updateGroup(Group group);
    public boolean deleteGroup(int groupId);
    public List<Group> getGroups(Integer[] ids);
    public List<Group> getGroups(int id);
}