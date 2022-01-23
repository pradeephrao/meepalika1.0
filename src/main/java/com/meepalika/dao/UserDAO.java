package com.meepalika.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	public List<User> findAll();

	public User findById(int id);

	public User findByUsername(String username);

	public User save(User user);

	public int countByUsername(String userName);

	public Page<User> fetchAllusersByAccountIdANDActive(long account_id, int activeDigit, Pageable paging);

	public Set<String> fetchAllSuperAdminEmails(int adminRoleId);

	@Query(value = "SELECT u FROM User u LEFT JOIN UserRole ur ON ur.user.id = u.id INNER JOIN Role r ON r.id = ur.role.id "
			+ " WHERE u.accountId = ?1 AND (ur.role.id = ?2 AND r.id = ?2) AND (u.active = ?3 AND ur.active = ?3)")
	public Page<User> findAllByAccountIdAndRoleIdAndActive(@Param("accountId") long accountId, @Param("roleId") long roleId,
														   @Param("active") int active, Pageable paging);

	public void deleteById(int id);

	public Page<User> fetchAllDoctorsByAccountIdANDActive(int account_id, int activeDigit, Pageable paging);

	public Page<User> fetchAllStaffByAccountIdANDActive(int account_id, int activeDigit, Pageable paging);

	@Query(value = "SELECT u FROM User u INNER JOIN Account a on a.id = u.accountId WHERE u.mobile_number = ?1 AND a.id = ?2 AND u.accountId = ?2")
	public User findByMobileNumberAndAccountId(@Param("mobile_number") String mobile_number, @Param("accountId") long accountId);

	@Query(value = "SELECT u FROM User u LEFT JOIN UserRole ur ON ur.user.id = u.id INNER JOIN Role r ON r.id = ur.role.id "
			+ " WHERE u.accountId = ?1 AND u.active = ?2 AND r.id NOT IN (4,6) GROUP BY u.id")
	public Page<User> fetchStaffByAccountIdANDActive(int accountId, int activeDigit, Pageable paging);


    @Query(value = "SELECT u.email FROM User u INNER JOIN UserRole ur ON ur.user.id = u.id INNER JOIN Role r ON r.id = ur.role.id "
    		+ "WHERE r.id = 1 AND u.accountId IN ?1" )	
	public Set<String> fetchAccountAdminEmails(int accountId);

    @Query(value = "SELECT u.email FROM User u where u.id in :ids")
	public Set<String> findByIdIn(List<Long> ids);

	public Page<User> findAll(Specification<User> userSpecification, Pageable paging);

}
