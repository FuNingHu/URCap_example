package com.URPlus.SmartTrain.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class STProgramNodeContribution implements ProgramNodeContribution{

	private final ProgramAPIProvider apiProvider;
	private final STProgramNodeView view;
	private final DataModel model;
	private final UndoRedoManager undoRedoManager;
	private final InputValidationFactory validatorFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	
	private static final String OUTPUT_KEY = "output";
	private static final Integer OUTPUT_DEFAULT = 2;
	private static final String DURATION_KEY = "duration";
	private static final int DURATION_DEFAULT = 2;
	private static final String IP_ADDRESS_KEY = "IpAddress";
	private static final String IP_ADDRESS_DEFAULT = "10.10.10.10";
	
	public STProgramNodeContribution(ProgramAPIProvider apiProvider, STProgramNodeView view,
			DataModel model) { //contribution构造函数
		// TODO Auto-generated constructor stub
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		this.keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.validatorFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
	}
	
	public void onOutputSelection(final Integer output){//在view中调用，在选取对应项后执行的数据操作
		undoRedoManager.recordChanges(new UndoableChanges() {//undoRedoManager记录变化，并可以向后/向前撤销动作
			
			@Override
			public void executeChanges() {
				// TODO Auto-generated method stub
				model.set(OUTPUT_KEY, output);
			}
		});
	}
	public void onDurationSelection(final int duration) {//在view中调用，在拖动滑块后执行的数据操作
		undoRedoManager.recordChanges(new UndoableChanges() {//undoRedoManager记录变化，并可以向后/向前撤销动作
			
			@Override
			public void executeChanges() {
				// TODO Auto-generated method stub
				model.set(DURATION_KEY, duration);
			}
		});
	}
	public void onResetClicked() { //在view中调用，在单击按钮后执行的数据操作
		undoRedoManager.recordChanges(new UndoableChanges() { //undoRedoManager记录变化，并可以向后/向前撤销动作
			
			@Override
			public void executeChanges() {
				// TODO Auto-generated method stub
				model.set(IP_ADDRESS_KEY, IP_ADDRESS_DEFAULT);
				view.setIpAddress(IP_ADDRESS_DEFAULT);
				model.set(OUTPUT_KEY, OUTPUT_DEFAULT);
				view.setIOComBoxSelection(OUTPUT_DEFAULT);
				model.set(DURATION_KEY,DURATION_DEFAULT);
				view.setDurationSlider(DURATION_DEFAULT);
			}
		});
	}
	public KeyboardTextInput getKeyboardForIpAddress() { //调用IP地址文本框键盘，在view中调用
		KeyboardTextInput keyboard = keyboardInputFactory.createIPAddressKeyboardInput();
		keyboard.setInitialValue(model.get(IP_ADDRESS_KEY, IP_ADDRESS_DEFAULT));
		return keyboard;
	}
	public KeyboardInputCallback<String> getCallbackForIpAddress(){ //返回IP地址回调函数，在view中调用
		return new KeyboardInputCallback<String>() {

			@Override
			public void onOk(String value) {
				// TODO Auto-generated method stub
				model.set(IP_ADDRESS_KEY, value);
				view.setIpAddress(value);
			}
		};
	}
	private Integer[] getOutputItems() { //生成的ComboBox中下拉菜单选项
		Integer[] items = new Integer[8];
		for(int i=0;i<8;i++) {
			items[i]=i;
		}
		return items;
	}
	private Integer getOutput() { //从dataModel中取值
		return model.get(OUTPUT_KEY, OUTPUT_DEFAULT);
	}
	private int getDuration() {
		return model.get(DURATION_KEY, DURATION_DEFAULT);
	}
	private String getIpAddress() {
		return model.get(IP_ADDRESS_KEY, IP_ADDRESS_DEFAULT);
	}
	@Override
	public void openView() { //Override表示是必须要实现的函数
		// TODO Auto-generated method stub
		view.setIOComBoxItems(getOutputItems()); //在ProgramNodeView中实现的函数
		view.setIOComBoxSelection(getOutput());
		view.setDurationSlider(getDuration());
		view.setIpAddress(getIpAddress());
	}

	@Override
	public void closeView() {//必须实现函数
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() { //必须实现函数
		// TODO Auto-generated method stub
		return "ST: DO"+getOutput()+" t="+getDuration(); //程序树中显示的对应标题
	}

	@Override
	public boolean isDefined() { //必须实现函数 //是否完成当前程序所有设置，如果是false，在程序树中会显示黄色
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) { //必须实现函数，生成机器人脚本程序
		// TODO Auto-generated method stub
		writer.appendLine("set_standard_digital_out("+getOutput()+",True)"); //具体参考机器人脚本手册
		writer.sleep(getDuration());
		writer.appendLine("set_standard_digital_out("+getOutput()+",False)");
		writer.sleep(getDuration());
	}

}
