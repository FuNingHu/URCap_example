package com.URPlus.SmartTrain.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class STProgramNodeView implements SwingProgramNodeView<STProgramNodeContribution>{ //在这里实现SwingProgramNodeView类

	private final ViewAPIProvider apiProvider;
	private final Style style;
	private JSlider durationSlider = new JSlider(); //本示例程序中添加了一个JSlider， 一个JComboBox，一个IP地址输入框，一个按钮，和一个图片。
	private JComboBox<Integer> ioComboBox = new JComboBox<Integer>();
	private JTextField ipAddress = new JTextField();
	private JButton resetButton = new JButton();
//	private JRadioButton radioButton_1 = new JRadioButton();
//	private JRadioButton radioButton_2 = new JRadioButton();
	
	private static final ImageIcon LOGO_ICON = new ImageIcon(STProgramNodeContribution.class.getResource("/logo/logo.png"));
	private Box tempBox;
	
	public STProgramNodeView(ViewAPIProvider apiProvider, Style style) { //构造函数
		// TODO Auto-generated constructor stub
		this.apiProvider = apiProvider;
		this.style = style;
	}
	@Override
	public void buildUI(JPanel panel, final ContributionProvider<STProgramNodeContribution> provider) {//Override表示是必须要实现的函数
		// TODO Auto-generated method stub
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //设置布局管理器，如果Y_AXIS表示组建从上到下依次排列;还有其他类型例如FlowLayout, BorderLayout, GridLayout.
//		panel.setLayout(null); //如果希望任意摆放组件位置，使用绝对布局
		ipAddress.setHorizontalAlignment(JTextField.RIGHT);  //如果是文本，需要靠右; 如果是数字，需要靠左
		panel.add(createDescription("Select which output to blink:")); //添加一个描述行，具体函数申明在下方
		panel.add(createVerticalSpacing());  //添加Y轴方向空间，值由style实例确定。
		panel.add(createIOComboBox(ioComboBox, provider)); //添加ComboBox组件，具体函数申明在下方
//		tempBox = createDurationSlider(durationSlider, 0, 20, provider);   //使用绝对布局方法添加组件
//		tempBox.setBounds(40, 120, 300, 30);
//		panel.add(tempBox);
//		tempBox = createIOComboBox(ioComboBox, provider);
//		tempBox.setBounds(40, 70, 300, 30);
//		panel.add(tempBox);
//		tempBox = createIcon(LOGO_ICON);
//		tempBox.setBounds(360, 20, 200, 50);
//		panel.add(tempBox);
		panel.add(createVerticalSpacing());
		panel.add(createDescription("Select the duration of blink:"));
		panel.add(createVerticalSpacing());
		panel.add(createDurationSlider(durationSlider, 0, 10, provider));
		panel.add(createVerticalSpacing());
		panel.add(createLabelInputField("IP Address: ", ipAddress, new MouseAdapter() {  //添加鼠标按钮事件触发
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getKeyboardForIpAddress(); //启动IP地址键盘，具体函数在contribution中申明 
				keyboardInput.show(ipAddress,provider.get().getCallbackForIpAddress()); //调用回调函数
			}
		}));
		panel.add(createVerticalSpacing());
		panel.add(createButton("Reset", resetButton, provider, new ActionListener() {  //添加按钮的鼠标按下时间触发，并调用在contribution中申明的onResetClicked函数
			public void actionPerformed(ActionEvent e) {
				provider.get().onResetClicked();
				System.out.println("reset button being clicked!!!");
			}
		}));
//		panel.add(createVerticalSpacing());
//		panel.add(createRatioButton("radio_option_1", radioButton_1)); //增加单选框示例
//		panel.add(createVerticalSpacing());
//		panel.add(createRatioButton("radio_option_2", radioButton_2));
//		
//		ButtonGroup group = new ButtonGroup(); //单选框的互斥逻辑需要设立ButtonGroup
//		group.add(radioButton_1);
//		group.add(radioButton_2);

		panel.add(createSpacer(0, 100));
		panel.add(createIcon(LOGO_ICON)); //面板添加图片示例
	}

	public void setIOComBoxItems(Integer[] items) { //ComboBox菜单设置下拉选项的函数,用于Contribution中的[Override]OpenView函数使用
		ioComboBox.removeAllItems();
		ioComboBox.setModel(new DefaultComboBoxModel<Integer>(items));
	}
	public void setIOComBoxSelection(Integer item) { //ComboBox设置选中项目，泛型参数还可以设置为string或其它
		ioComboBox.setSelectedItem(item);
	}
	public void setIpAddress(String value) { //设置ip文本框在键盘消失时显示的文本
		ipAddress.setText(value);
	}
	public void setDurationSlider(int value) { //设置划条的当前位置
		durationSlider.setValue(value);
	}
	private Box createDescription(String desc) {
		Box box = Box.createHorizontalBox();//示教器上显示的每一个组建都需要转换成Box或者Component类型，再由JPanel.add()添加到面板上，String或者JLabel或者其他组建类型不能直接添加
		box.setAlignmentX(Component.LEFT_ALIGNMENT);//设置组建Y方向左对齐
		JLabel label = new JLabel(desc); //将String装进JLabel中
		box.add(label); //将JLabel装进Box中
		return box;
	}
	private Box createIOComboBox(final JComboBox<Integer> combo,
			final ContributionProvider<STProgramNodeContribution> provider) {//这里要用到Contribution中的函数，所以参数表中添加ContributionProvider
		Box box = Box.createHorizontalBox(); //首先创建Box类型容器，等待装箱
		box.setAlignmentX(Component.LEFT_ALIGNMENT); //容器Y轴左对齐
		JLabel label = new JLabel("--digital_out--"); //添加一个JLabel组件，也可以作为形参导入。
		combo.setPreferredSize(new Dimension(110,30));//设置组件尺寸
		combo.setMaximumSize(combo.getPreferredSize());
		combo.addItemListener(new ItemListener() {  //添加事件触发
			
			@Override
			public void itemStateChanged(ItemEvent e) { 
				// TODO Auto-generated method stub
				if(e.getStateChange() == ItemEvent.SELECTED) {
					provider.get().onOutputSelection((Integer)e.getItem()); //onOutputSelection在Contribution中实现，更新dataModel
				}
			}
		});
		box.add(createHorizontalSpacing()); //将组件放入box容器
		box.add(label);
		box.add(createHorizontalSpacing()); 
		box.add(combo);
		return box;
	}
	private Box createDurationSlider(final JSlider slider, int min, int max,
			final ContributionProvider<STProgramNodeContribution> provider) {//这里要用到Contribution中的函数，所以参数表中添加ContributionProvider
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		slider.setMinimum(min);
		slider.setMaximum(max);
		slider.setOrientation(JSlider.HORIZONTAL); //设置JSlider方向为水平
		
		slider.setPreferredSize(new Dimension(300,30)); //设置JSlider尺寸
		slider.setMaximumSize(slider.getPreferredSize());
		final JLabel value = new JLabel();
		slider.addChangeListener(new ChangeListener() { //添加事件触发
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				value.setText(Integer.toString(slider.getValue())+" s"); //动态更新显示选中的值和单位
				provider.get().onDurationSelection(slider.getValue());  //onDurationSelection在Contribution中实现，更新dataModel
			}
		});
		box.add(slider); //将组件放入容器
		box.add(value);
		return box;
	}
	private Box createLabelInputField(String label, 
			JTextField inputField, 
			MouseAdapter mouseAdapter) { //创建文本输入框，用于IP地址输入
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel jLabel = new JLabel(label);
		inputField.setFocusable(false);
		inputField.setPreferredSize(style.getInputfieldSize());//文本框尺寸由style文件取值规定
		inputField.setMaximumSize(inputField.getPreferredSize());
		inputField.addMouseListener(mouseAdapter); //添加鼠标适配器
//		inputField.setBackground(Color.GREEN);//设置背景颜色
		horizontalBox.add(jLabel);//将组件放入box容器
		horizontalBox.add(inputField);
		return horizontalBox;
	}
	private Box createButton(String label, final JButton button, 
			final ContributionProvider<STProgramNodeContribution> provider, 
			ActionListener actionListener) { //创建按钮 
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.addActionListener(actionListener);;//添加事件监听器
		button.setText(label);
		button.setEnabled(true); //使能按钮
		buttonBox.add(button); //将组件放入box容器
		return buttonBox;
	}
	private Box createIcon(final ImageIcon image) {//创建图片
		Box box=Box.createHorizontalBox();
		final JLabel pic = new JLabel();
		image.setImage(image.getImage().getScaledInstance(image.getIconWidth()/3, image.getIconHeight()/3, Image.SCALE_DEFAULT));//缩放图片尺寸
		pic.setIcon(image);//将image放入JLabel
		
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(pic);//将JLabel放入box容器
		return box;
	}
	private Box createRatioButton(String label, final JRadioButton jRadioButton) {//创建单选按钮
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		jRadioButton.setText(label);//添加描述
		box.add(jRadioButton);//将组件放入box容器
		return box;
	}
	private Component createHorizontalSpacing() {//根据style创建指定尺寸的x轴隔断
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(),0));
	}
	private Component createVerticalSpacing() { //根据style创建制定尺寸的Y轴隔断
		return Box.createRigidArea(new Dimension(0,style.getVerticalSpacing()));
	}
	private Component createSpacer(int width, int height) {
		return Box.createRigidArea(new Dimension(width,height));
	}

}
