package com.liqaa.server.controllers.services.interfaces;

import com.liqaa.shared.models.entities.Group;
import com.liqaa.shared.models.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface GroupServices
{
    public void createGroup(Group group, ArrayList<Integer> groupMembers);
    public void updateGroup(int groupId, int ownerId, String name, String description);
    public void updateGroupImage(int groupId, int ownerId, byte[] image);
    public void deleteGroup(int groupId, int ownerId);
    public void addMembers(int groupId, int ownerId, List<Integer> groupMembers);
    public void removeMembers(int groupId, int ownerId, List<Integer> groupMembers);
}
