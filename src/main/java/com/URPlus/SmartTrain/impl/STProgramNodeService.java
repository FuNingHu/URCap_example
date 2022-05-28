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
	public String getId() {//Override表示是必须要实现的函数
		// TODO Auto-generated method stub
		return "ST-Node";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		configuration.setChildrenAllowed(false); //不允许生成子节点
	}

	@Override
	public String getTitle(Locale locale) {//Override表示是必须要实现的函数
		// TODO Auto-generated method stub
		return "Smart Training";//定义ProgramNode中URCap标签下的标题
	}

	@Override
	public STProgramNodeView createView(ViewAPIProvider apiProvider) {//Override表示是必须要实现的函数 
		// TODO Auto-generated method stub
		SystemAPI systemAPI = apiProvider.getSystemAPI(); 
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >=5? new V5Style() : new V3Style(); //获得系统版本，并匹配不同格式
		return new STProgramNodeView(apiProvider, style);//返回ProgramNodeView 实例
	}

	@Override
	public STProgramNodeContribution createNode(ProgramAPIProvider apiProvider, STProgramNodeView view, DataModel model,
			CreationContext context) {//Override表示是必须要实现的函数
		// TODO Auto-generated method stub
		return new STProgramNodeContribution(apiProvider, view, model); //返回ProgramNodeContribution 实例 //context在这里没有使用
	}

}
