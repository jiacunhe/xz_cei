package com.hyrt.cei.ui.personcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.InformationActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 用户信息
 * 
 * @author Administrator
 * 
 */
public class PersonInfo extends Activity {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	public static PersonCenterInf p;
	private TextView lgoinname, name, sex, certype, cardno, phone, email,
			unitname;
	private ColumnEntry columnEntry;
	private String userId;

	public static PersonCenterInf getPersonCenterInf() {
		return p;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personinfo);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		userId = columnEntry.getUserId();
		init();
	}

	private void init() {
		lgoinname = (TextView) findViewById(R.id.personinfo_loginname);
		name = (TextView) findViewById(R.id.personinfo_name);
		sex = (TextView) findViewById(R.id.personinfo_sex);
		certype = (TextView) findViewById(R.id.personinfo_zhengjian);
		cardno = (TextView) findViewById(R.id.personinfo_zhengjiannum);
		phone = (TextView) findViewById(R.id.personinfo_phonenum);
		email = (TextView) findViewById(R.id.personinfo_email);
		unitname = (TextView) findViewById(R.id.personinfo_danwei);
		refreshListData();
	}

	/**
	 * 获取数据
	 */
	private void refreshListData() {
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (((CeiApplication) PersonInfo.this
						.getApplication()).isNet()) {
					String rs = "";
					rs = Service.queryUserInfo(userId);
					WriteOrRead.write(rs, MyTools.nativeData, "PersonCenter.xml");
					p = null;
					try {
						p = XmlUtil.personCenter(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					String rss = WriteOrRead.read(
							MyTools.nativeData, "PersonCenter.xml");
					try {
						p = XmlUtil.personCenter(rss);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(p!=null){
				if (p.getLgoinname() != null) {
					lgoinname.setText(p.getLgoinname());
				} else {
					lgoinname.setText("");
				}

				if (p.getName() != null) {
					name.setText(p.getName());
				} else {
					name.setText("");
				}

				if (p.getSex() != null) {
					sex.setText(p.getSex());
				} else {
					sex.setText("");
				}

				if (p.getCertype() != null) {
					certype.setText(p.getCertype());
				} else {
					certype.setText("");
				}

				if (p.getCardno() != null) {
					cardno.setText(p.getCardno());
				} else {
					cardno.setText("");
				}

				if (p.getPhone() != null) {
					phone.setText(p.getPhone());
				} else {
					phone.setText("");
				}

				if (p.getEmail() != null) {
					email.setText(p.getEmail());
				} else {
					email.setText("");
				}

				if (p.getUnitname() != null) {
					unitname.setText(p.getUnitname());
				} else {
					unitname.setText("");
				}
			}
		}
	};
}
