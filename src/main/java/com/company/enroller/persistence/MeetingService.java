package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting getByID(Long id) {
//		String hql = "FROM Meeting M WHERE M.id=" + id;
//		Query query = connector.getSession().createQuery(hql);
//		return query.list();
		return (Meeting) connector.getSession().get(Meeting.class, id);
	}
	
	public void remove(Meeting meeting) {		
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}
	
	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}
	
	public void addParticipant(Participant participant, Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.addParticipant(participant);
		connector.getSession().update(meeting);
		transaction.commit();
	}
	
	public void removeParticipant(Participant participant, Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.removeParticipant(participant);
		connector.getSession().update(meeting);
		
//		String hql = "delete from meeting_participant where participant_login = :participant_login";
//		Query query = connector.getSession().createQuery(hql);
//		query.setParameter("participant_login", participant.getLogin());
//		query.executeUpdate();		
		
		transaction.commit();
	}
		
	public Collection<Participant> participantsList(Meeting meeting) {		
		return meeting.getParticipants();
	}
	
	public Participant getParticipantByLogin(Meeting meeting, String login) {
		for (Participant participant : participantsList(meeting)) {
			if (participant.getLogin().equals(login)) {
				return participant;
			}
		}
		return null;
	}
	
	public void updateMeeting(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}

}