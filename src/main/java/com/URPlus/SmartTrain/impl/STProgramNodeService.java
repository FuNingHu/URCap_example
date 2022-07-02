package com.URPlus.SmartTrain.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

public class STProgramNodeService implements SwingProgramNodeService<STProgramNodeContribution, STProgramNodeView>{

	@Override
	public String getId() {//Override表示是必须要实现的函数 //Override means a method must be implemented
		// TODO Auto-generated method stub
		return "ST-Node";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		configuration.setChildrenAllowed(false); //不允许生成子节点 //NOT allowed children node 
	}

	@Override
	public String getTitle(Locale locale) {//Override表示是必须要实现的函数//Override means a method must be implemented
		// TODO Auto-generated method stub
		String title = "Smart Train";
		if ("en".equals(locale.getLanguage())) //示例：根据系统语言设定显示标题 //example: show text in function of system language
			{title = "Smart Train";}
		else if ("zh".equals(locale.getLanguage()))
			{title = "智能培训";}
		else if ("tw".equals(locale.getLanguage()))
			{title ="智能培訓";}
		else if("fr".equals(locale.getLanguage()))
			{title = "Malin Formation";}
		System.out.println("current local: "+locale.getLanguage().toString());
		return title;//定义ProgramNode中URCap标签下的标题 //return the title shown under ProgramNode URCap tag
	}

	@Override
	public STProgramNodeView createView(ViewAPIProvider apiProvider) {//Override表示是必须要实现的函数 //Override means a method must be implemented
		// TODO Auto-generated method stub
		SystemAPI systemAPI = apiProvider.getSystemAPI(); 
		Locale local = systemAPI.getSystemSettings().getLocalization().getLocaleForProgrammingLanguage();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >=5? new V5Style() : new V3Style(); //获得系统版本，并匹配不同格式 
																										   //extract robot version, to assign suitable style file 
		return new STProgramNodeView(apiProvider, style, local);//返回ProgramNodeView 实例 //return ProgramNodeView instance
	}

	@Override
	public STProgramNodeContribution createNode(ProgramAPIProvider apiProvider, STProgramNodeView view, DataModel model,
			CreationContext context) {//Override表示是必须要实现的函数 ////Override means a method must be implemented
		// TODO Auto-generated method stub
		return new STProgramNodeContribution(apiProvider, view, model); //返回ProgramNodeContribution 实例 //context在这里没有使用
																		//return ProgramNodeContribution instance //context is NOT used here
	}

}
