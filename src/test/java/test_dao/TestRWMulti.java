package test_dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mvw.mybatis.dao.RwDao;
import com.mvw.mybatis.model.User;

/**
 * 测试读从读写
 * 
 * @author gaotingping
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-multi.xml" })
public class TestRWMulti {

	@Autowired
	private RwDao dao;
	
	@Test
	public void test1(){
		
		dao.update("主从读写分离6");//写主
		
		for(int i=0;i<20;i++){
			
			User u = dao.get(""); //读从
			System.out.println(u);
			
			u = dao.get2();//读从 强制走主
			System.out.println(u);
		}
	}
}
