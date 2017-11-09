package user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import context.AppContext;
import user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppContext.class)
public class UserDaoTest {
	@Autowired
	UserDao userDao;
	@Autowired
	PlatformTransactionManager transactionManager;
	User user1;
	User user2;
	User user3;
	TransactionStatus txStatus;
	
	@Before
	public void init() {
		TransactionDefinition txDefinition = new DefaultTransactionDefinition();
		 txStatus = transactionManager.getTransaction(txDefinition);
		
		user1 = new User("user1", "1234", "유저1", "1234@gg.com", "subName1", "010-1111-1111");
		user2 = new User("user2", "2222", "유저2", "2222@gg.com", "subName2", "010-2222-2222");
		user3 = new User("user3", "3333", "유저2", "3333@gg.com", "subName3", "010-3333-3333");
	}
	
	@After
	public void destroy() {
		transactionManager.rollback(txStatus);
	}
	
	@Test
	public void UserAddAndGet() {
		userDao.join(user1);
		User getUser1 = userDao.getUser(user1.getId());
		isSameUser(user1, getUser1);
		
		userDao.join(user2);
		User getUser2 = userDao.getUser(user2.getId());
		isSameUser(user2, getUser2);
		
		userDao.join(user3);
		User getUser3 = userDao.getUser(user3.getId());
		isSameUser(user3, getUser3);
	}
	
	@Test
	public void userCount() {
		userDao.join(user1);
		assertThat(userDao.userCount(), is(1L));
		
		userDao.join(user2);
		assertThat(userDao.userCount(), is(2L));
		
		userDao.join(user3);
		assertThat(userDao.userCount(), is(3L));
	}
	
	@Test(expected=DuplicateKeyException.class)
	public void doublePrimaryKey() {
		userDao.join(user1);
		userDao.join(user1);
		fail();
	}
	
	@Test
	public void update() {
		userDao.join(user1);
		User getUser1 = userDao.getUser(user1.getId());
		isSameUser(user1, getUser1);
		
		user1.getUserInfo().setName("수정");
		assertThat(user1.getUserInfo().getName(), is(not(getUser1.getUserInfo().getName())));
		
		userDao.update(user1);
		getUser1 = userDao.getUser(user1.getId());
		
		assertThat(user1.getUserInfo().getName(), is(getUser1.getUserInfo().getName()));
		
	}
	
		
	private void isSameUser(User user, User getUser) {
		assertThat(user.getId(), is(getUser.getId()));
		assertThat(user.getUserInfo().getName(), is(getUser.getUserInfo().getName()));
		assertThat(user.getUserInfo().getEmail(), is(getUser.getUserInfo().getEmail()));
		assertThat(user.getUserInfo().getSubName(), is(getUser.getUserInfo().getSubName()));
		assertThat(user.getUserInfo().getPhoneNumber(), is(getUser.getUserInfo().getPhoneNumber()));
	}
	
}
