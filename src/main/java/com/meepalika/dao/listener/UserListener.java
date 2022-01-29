package com.meepalika.dao.listener;

import com.meepalika.dao.UserDAO;
import com.meepalika.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.Optional;

public class UserListener {

	@Autowired
	UserDAO userDAO;
	@PrePersist
	public void userPrePersist(User ob) {
		System.out.println("Listening User Pre Persist : " + ob.getFirst_name());
	}
	@PostPersist
	public void userPostPersist(User ob) {
		System.out.println("Listening User Post Persist : " + ob.getFirst_name());

	}
	@PostLoad
	public void userPostLoad(User ob) {

	}
	@PreUpdate
	public void userPreUpdate(User ob) {
		/*User user = null;
		System.out.println("Listening User Post Update : " + ob.getFirst_name());
		Optional<User> userOpt = userDAO.findById(ob.getId());
		user = userOpt.get();
		//User userFromDB = userFromDBOpt.get();
		ob.setPassword(user.getPassword());*/

	}
	@PostUpdate
	public void userPostUpdate(User ob) {

	}
	@PreRemove
	public void userPreRemove(User ob) {

	}
	@PostRemove
	public void userPostRemove(User ob) {

	}
}
