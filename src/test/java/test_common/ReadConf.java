package test_common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 日志收集器
 * 
 * @author gaotingping
 */
public class ReadConf {

	@Test
	public void test1() throws Exception {
		
		Map<String, String> map = readConfAsMap("useMaster.conf");
		System.out.println(map);
		
	}

	private Map<String, String> readConfAsMap(String fileName) throws IOException {

		InputStream in = null;
		BufferedReader bio = null;

		try {
			
			Map<String, String> map = new HashMap<>();
			in = ReadConf.class.getClassLoader().getResourceAsStream(fileName);
			bio = new BufferedReader(new InputStreamReader(in));
			String str = null;
			
			while ((str = bio.readLine()) != null) {
				str = str.trim();/*过滤掉空，这很有必要*/
				if (!str.startsWith("#")) {/*注释忽略*/
					map.put(str, null);
				}
			}

			return map;

		} catch (Exception e) {
			throw new RuntimeException("读取配置文件失败:" + e.getMessage(), e);
		} finally {
			// close
			if (in != null) {
				in.close();
			}
			if (bio != null) {
				bio.close();
			}
		}
	}
}
