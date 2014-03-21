package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.EconDataAdapter;
import com.hyrt.cei.adapter.EconSecondDataAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.information.funId;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import android.widget.ImageView;

public class EconDateNumberActivity extends Activity implements OnClickListener {
	private ColumnEntry columnEntry;
	private List<List<New>> childDataList;
	private List<New> nowSecondData = new ArrayList<New>();
	private ImageView zxImg, szImg, zjImg, fxImg, zbImg, sjImg, homeImg,
			backImg, shuax;
	private List<ColumnEntry> groupDataList;
	private ListView expList;
	private WebView webView;
	private EconDataAdapter adapter;
	private EconSecondDataAdapter secondAdapter;
	private TextView topName;
	private LinearLayout prolayout;
	private String nowID, pid, pName, numberId;
	private CeiApplication application;
	private boolean isToSecond,flage;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==400){
				webView.loadDataWithBaseURL("about:blank",
						"没有数据！", "text/html", "utf-8", null);
				prolayout.setVisibility(View.GONE);
				return;
			}
			if (childDataList == null) {
				return;
			}
			if (nowID != null && !nowID.equals("")) {
				if (((CeiApplication) getApplication()).isNet()) {
					String htmlUrl = MyTools.newsHtml + nowID;
					webView.loadUrl(htmlUrl);
				} else {
					webView.loadDataWithBaseURL("about:blank",
							"现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null);
				}
			} else {
				MyTools.exitShow(EconDateNumberActivity.this,
						findViewById(R.id.econ_data_number_all), "没有数据！");
			}
			if (pid != null && !pid.equals("")) {
				topName = (TextView) findViewById(R.id.top_name);
				topName.setVisibility(View.VISIBLE);
				isToSecond = true;
				topName.setText(pName);
				secondAdapter = new EconSecondDataAdapter(
						EconDateNumberActivity.this, nowSecondData, nowID);
				expList.setAdapter(secondAdapter);
				topName.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						topName.setVisibility(View.GONE);
						isToSecond = false;
						adapter = new EconDataAdapter(
								EconDateNumberActivity.this, groupDataList, pid);
						expList.setAdapter(adapter);
					}
				});
			} else {
				adapter = new EconDataAdapter(EconDateNumberActivity.this,
						groupDataList, pid);
				expList.setAdapter(adapter);
			}
			expList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					topName = (TextView) findViewById(R.id.top_name);
					topName.setVisibility(View.VISIBLE);
					if (isToSecond) {
						TextView text = (TextView) arg1
								.findViewById(R.id.econ_data_ea_item_child_tv);
						text.setTextColor(Color.BLUE);
						EconDataMain.childViews.add(text);
						for (TextView views : EconDataMain.childViews) {
							if (views == arg1
									.findViewById(R.id.econ_data_ea_item_child_tv)) {
								views.setTextColor(Color.BLUE);
							} else {
								views.setTextColor(Color.BLACK);
							}
						}
						String funid = columnEntry.getColByName(topName.getText().toString(),columnEntry.getColByName(EconDataMain.MODEL_NAME).getId()).getId();
						if (isHasfunID(funid)
								|| ((New) arg0.getAdapter().getItem(arg2))
										.getIsfree().equals("1")) {
							nowID = ((New) arg0.getAdapter().getItem(arg2))
									.getId();
							String htmlUrl = MyTools.newsHtml + nowID;
							if (((CeiApplication) getApplication()).isNet()) {
								webView.loadUrl(htmlUrl);
							} else {
								webView.loadDataWithBaseURL("about:blank",
										"现在是离线状态，请链接网络后获取数据！", "text/html",
										"utf-8", null);
							}
						} else {
							MyTools.exitShow(EconDateNumberActivity.this,
									findViewById(R.id.econ_data_number_all),
									"没有购买，请购买！");
						}
					} else {
						isToSecond = true;
						nowSecondData = childDataList.get(arg2);
						topName.setText(groupDataList.get(arg2).getName());
						secondAdapter = new EconSecondDataAdapter(
								EconDateNumberActivity.this, nowSecondData,
								nowID);
						expList.setAdapter(secondAdapter);
						topName.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								topName.setVisibility(View.GONE);
								isToSecond = false;
								adapter = new EconDataAdapter(
										EconDateNumberActivity.this,
										groupDataList, groupDataList.get(arg2)
												.getId());
								expList.setAdapter(adapter);
							}
						});
					}
				}
			});
			flage=true;
			prolayout.setVisibility(View.GONE);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_number);
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		nowID = getIntent().getStringExtra("id");
		initView();

		prolayout.setVisibility(View.VISIBLE);
		initData();
	}

	private void initData() {
		if (EconDataMain.allColBg != null
				&& EconDataMain.allColBg.getId() != null
				&& !EconDataMain.allColBg.getId().equals("")) {
			String allBgId = EconDataMain.allColBg.getId();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				if (columnEntry.getName().equals("数字快讯")) {
					numberId = columnEntry.getId();
				}
			}
		}
		// 获取数据
		childDataList = new ArrayList<List<New>>();
		new Thread() {

			@Override
			public void run() {
				if (numberId != null && !numberId.equals("")) {
					groupDataList = columnEntry
							.getEntryChildsForParent(numberId);
					for (ColumnEntry columnEntry : groupDataList) {
						try {
							List<New> dataList = null;
							if (((CeiApplication) getApplication()).isNet()) {
								String zhib = Service.querydbsByFunctionId(
										columnEntry.getId(), "40");
								dataList = XmlUtil.getNews(zhib);
								for (New new1 : dataList) {
									if (getIntent().getStringExtra("id") != null
											&& new1.getId().equals(getIntent().getStringExtra("id"))
											&& dataList.size() > 0) {
										nowID =getIntent().getStringExtra("id");
										pid = columnEntry.getId();
										pName = columnEntry.getName();
										nowSecondData.clear();
										nowSecondData.addAll(dataList);
									} else if (nowSecondData.size() == 0) {
										nowID = new1.getId();
										pid = columnEntry.getId();
										pName = columnEntry.getName();
										nowSecondData.clear();
										nowSecondData.addAll(dataList);
									}
								}
								WriteOrRead.write(zhib, MyTools.nativeData,
										columnEntry.getId());
							} else {
								String zhib = WriteOrRead
										.read(MyTools.nativeData,
												columnEntry.getId());
								dataList = XmlUtil.getNews(zhib);
							}

							childDataList.add(dataList);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (handler != null) {
						handler.sendEmptyMessage(1);
					}
				}else{
					if (handler != null) {
						handler.sendEmptyMessage(400);
					}
				}
			}

		}.start();
	}

	private void initView() {
		prolayout = (LinearLayout) findViewById(R.id.econ_data_number_pro);
		expList = (ListView) findViewById(R.id.econ_data_number_list);
		webView = (WebView) findViewById(R.id.econ_data_number_web);
		// 低栏图标
		shuax = (ImageView) findViewById(R.id.econ_data_number_shuax);
		shuax.setOnClickListener(this);
		zxImg = (ImageView) findViewById(R.id.econ_data_number_zx);
		zxImg.setOnClickListener(this);
		szImg = (ImageView) findViewById(R.id.econ_data_number_sz);
		szImg.setOnClickListener(this);
		zjImg = (ImageView) findViewById(R.id.econ_data_number_zz);
		zjImg.setOnClickListener(this);
		fxImg = (ImageView) findViewById(R.id.econ_data_number_fx);
		fxImg.setOnClickListener(this);
		zbImg = (ImageView) findViewById(R.id.econ_data_number_zb);
		zbImg.setOnClickListener(this);
		sjImg = (ImageView) findViewById(R.id.econ_data_number_sj);
		sjImg.setOnClickListener(this);
		homeImg = (ImageView) findViewById(R.id.econ_data_number_home);
		homeImg.setOnClickListener(this);
		backImg = (ImageView) findViewById(R.id.econ_data_number_back);
		backImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == zxImg) {
			// 最新页面
			Intent intent = new Intent(this, EconDataMain.class);
			startActivity(intent);
		} else if (v == szImg) {
			return;
			// 数字快讯
		} else if (v == zjImg) {
			// 中经指数
			Intent intent = new Intent(this, EconZZDataActivity.class);
			startActivity(intent);
		} else if (v == fxImg) {
			// 分析预测
			Intent intent = new Intent(this, EconFXDataActivity.class);
			startActivity(intent);
		} else if (v == zbImg) {
			// 指标查询
			Intent intent = new Intent(this, EconZBQueryActivity.class);
			startActivity(intent);
		} else if (v == sjImg) {
			// 数据查询
			Intent intent = new Intent(this, EconDataQueryActivity.class);
			startActivity(intent);
		} else if (v == homeImg) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
		} else if (v == backImg) {
			// 返回键
			this.finish();
			return;
		} else if (v == shuax) {
			if (flage) {
				flage=false;
				Intent intent = new Intent(this, EconDateNumberActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				return;
			}
		}
	}

	private boolean isHasfunID(String funid) {
		CeiApplication application = (CeiApplication) getApplication();
		for (funId id : application.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}
		return false;
	}

}
