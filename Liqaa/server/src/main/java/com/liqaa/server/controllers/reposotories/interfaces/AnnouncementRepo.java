package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.Announcement;

import java.sql.SQLException;
import java.util.List;

public interface AnnouncementRepo {
    public boolean createAnnouncement(Announcement announcement) throws SQLException;
    public Announcement getAnnouncementById(int id) throws SQLException;
    public List<Announcement> getAllAnnouncements() throws SQLException;
    public boolean updateAnnouncement(Announcement announcement) throws SQLException;
    public boolean deleteAnnouncement(int id) throws SQLException;
}