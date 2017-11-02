package cn.dong.jobs.countData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class CountData {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Value("${job.countData.stime}")
	private String stime;
	@Value("${job.countData.etime}")
	private String etime;
	private static final Log log = LogFactory.getLog(CountData.class);
	public void countData(){
		if("".equals(stime)||"".equals(etime)){
			log.info("开始时间或结束时间为空！");
			return;
		}
		String countSql = "insert into TT_DEVICE_FAULT(xjbh,JCSJ,FX,JCJG)"
				+ " select d.kkbh,sysdate,d.xsfx,'02' from d_device_p d left join("
				+ " select t.sbbh,count(*) num"
				+ " from TT_VEHICLE_T t"
				+ " where t.rksj > to_date(to_char(sysdate,'yyyy-mm-dd')||' "+stime+"', 'yyyy-mm-dd hh24:mi:ss')"
				+ "and t.rksj < to_date(to_char(sysdate,'yyyy-mm-dd')||' "+etime+"', 'yyyy-mm-dd hh24:mi:ss') group by t.sbbh,t.xsfx"
				+ ") a on d.kkbh=a.sbbh where num is null";
		log.info("countData : "+countSql);
		try {
			jdbcTemplate.update(countSql);
		} catch (Exception e) {
			log.info("统计过车数据出错！");
		}
		
	}
}
