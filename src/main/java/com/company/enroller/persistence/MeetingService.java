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
		System.out.println(">>>>>>> " + id + " <<<<<<<<<");
//		String hql = "FROM Meeting M WHERE M.id=" + id;
//		Query query = connector.getSession().createQuery(hql);
//		return query.list();
		return (Meeting) connector.getSession().get(Meeting.class, id);
	}
	
	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}
	
	
	
}
