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
public class TestRW {

	@Autowired
	private RwDao dao;
	
	@Test
	public void test1(){
		
		dao.update("主从读写分离4");//写主
		
		User u = dao.get(""); //读从
		System.out.println(u);
		
		u = dao.get2();//读从
		System.out.println(u);
	}
}
