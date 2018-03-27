package test_dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mvw.mybatis.dao.RwDao;
import com.mvw.mybatis.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test.xml" })
public class TestSubmeter {

	@Autowired
	private RwDao dao;
	
	@Test
	public void test1(){
		System.out.println(dao.test1("11"));;
		System.out.println(dao.test2(new User(13)));
		System.out.println(dao.test3(new User(15,14),"11"));
	}
}
