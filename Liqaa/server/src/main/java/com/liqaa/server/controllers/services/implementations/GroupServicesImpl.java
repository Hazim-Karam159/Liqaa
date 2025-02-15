package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.ConversationParticipantsRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.ConversationRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.GroupRepoImpl;
import com.liqaa.server.controllers.services.interfaces.GroupServices;
import com.liqaa.shared.models.entities.Conversation;
import com.liqaa.shared.models.entities.ConversationParticipant;
import com.liqaa.shared.models.entities.Group;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class GroupServicesImpl implements GroupServices
{
    private static GroupServicesImpl instance;
    private GroupServicesImpl() {}
    public static synchronized GroupServicesImpl getInstance()
    {
        if (instance == null)
            instance = new GroupServicesImpl();

        return instance;
    }
    @Override
    public void createGroup(Group group, ArrayList<Integer> groupMembers)
    {
        int groupId = GroupRepoImpl.getInstance().createGroup(group);
        group.setId(groupId);
        int conversationId = ConversationRepoImpl.getInstance().createGroupConversation( groupId);
        groupMembers.add(group.getCreatedBy());
        boolean result = ConversationParticipantsRepoImpl.getInstance().addParticipants(groupMembers, conversationId);
        if(!result)
        {
//            throw new GroupCreationException();
        }
    }

    @Override
    public void updateGroup(int groupId, int ownerId, String name, String description)
    {
        Group group = GroupRepoImpl.getInstance().getGroup(groupId);
        if(group.getCreatedBy() != ownerId)
        {
//            throw new NotOwnerException();
        }
        group.setName(name);
        group.setDescription(description);
        GroupRepoImpl.getInstance().updateGroup(group);
    }

    @Override
    public void updateGroupImage(int groupId, int ownerId, byte[] image)
    {
        Group group = GroupRepoImpl.getInstance().getGroup(groupId);
        if(group.getCreatedBy() != ownerId)
        {
//            throw new NotOwnerException();
        }
        group.setImage(image);
        GroupRepoImpl.getInstance().updateGroup(group);
    }

    @Override
    public void deleteGroup(int groupId, int ownerId)
    {
        int id = GroupRepoImpl.getInstance().getGroupOwnerId(groupId);
        if(id != ownerId)
        {
//            throw new NotOwnerException();
        }
        GroupRepoImpl.getInstance().deleteGroup(groupId);
    }

    @Override
    public void addMembers(int groupId, int ownerId, List<Integer> groupMembers)
    {
        int id = GroupRepoImpl.getInstance().getGroupOwnerId(groupId);
        if(id != ownerId)
        {
//            throw new NotOwnerException();
        }
        int conversationId= ConversationRepoImpl.getInstance().getConversationByGroup(groupId).getId();
        ConversationParticipantsRepoImpl.getInstance().addParticipants(groupMembers, conversationId);
    }

    @Override
    public void removeMembers(int groupId, int ownerId, List<Integer> groupMembers)
    {
        int id = GroupRepoImpl.getInstance().getGroupOwnerId(groupId);
        if(id != ownerId)
        {
//            throw new NotOwnerException();
        }
        int conversationId= ConversationRepoImpl.getInstance().getConversationByGroup(groupId).getId();
        ConversationParticipantsRepoImpl.getInstance().removeParticipants(groupMembers, conversationId);
    }
}
