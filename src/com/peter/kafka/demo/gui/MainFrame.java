package com.peter.kafka.demo.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import com.peter.kafka.demo.consumer.MessageConsumer;
import com.peter.kafka.demo.producer.MessageProducer;

public class MainFrame extends JFrame implements ActionListener{
	
	JButton sendMessageButton;
	/*
	 * Future improvements: I want to mimic the behavior where if you click in this
	 * text box, then the text disappears. If i do NOT type anything, then the text reappears.
	 * Otherwise, nothing appears. 
	 */
	JLabel newTopicLabel;
	JLabel allTopicsLabel;
	JTextField sendMessageBox;
	JTextField newTopicBox;
	JPanel mainPanel;
	JScrollPane topicScroll;
	JList topicList;
	DefaultListModel listModel;
	SpringLayout layout;
	MessageConsumer consumer = new MessageConsumer();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame(){
		setupThisFrame();
		initializeComponents();
		addSprings();
		insertComponents();
		pack();
	}
	
	private void initializeComponents(){
		sendMessageButton 	= new JButton("submit");
		sendMessageBox		= new JTextField("add messages here");
		newTopicBox			= new JTextField(20);
		//we will now create the list box with topics in it...
		listModel = getTopicListModel();
		

		newTopicLabel = new JLabel("topic");
		allTopicsLabel = new JLabel("all topics");
		topicList = new JList(getTopicListModel());
		topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		topicScroll			= new JScrollPane(topicList);
		topicScroll.setPreferredSize(new Dimension(130,100));
		
		addListeners();
	}
	private void insertComponents(){
		mainPanel.add(sendMessageButton);
		mainPanel.add(sendMessageBox);
		mainPanel.add(topicScroll);
		mainPanel.add(newTopicBox);
		mainPanel.add(allTopicsLabel);
		mainPanel.add(newTopicLabel);
	}
	private void addSprings(){
		//this places the box 10 right and 10 down from the top-right of the mainPanel....
		layout.putConstraint(SpringLayout.WEST, sendMessageBox, 10, SpringLayout.WEST, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, sendMessageBox, 10, SpringLayout.NORTH, mainPanel);

		layout.putConstraint(SpringLayout.WEST, sendMessageButton, 10, SpringLayout.EAST, sendMessageBox);
		layout.putConstraint(SpringLayout.SOUTH, sendMessageButton, 0, SpringLayout.SOUTH, sendMessageBox);

		//put the the topic list 10 spaces below (and right-aligned) with the button
		layout.putConstraint(SpringLayout.WEST, newTopicLabel, 0, SpringLayout.WEST, sendMessageBox);
		layout.putConstraint(SpringLayout.NORTH, newTopicLabel, 10, SpringLayout.SOUTH, sendMessageBox);

		layout.putConstraint(SpringLayout.WEST, newTopicBox, 0, SpringLayout.WEST, newTopicLabel);
		layout.putConstraint(SpringLayout.NORTH, newTopicBox, 5, SpringLayout.SOUTH, newTopicLabel);

		layout.putConstraint(SpringLayout.WEST, allTopicsLabel, 0, SpringLayout.WEST, newTopicBox);
		layout.putConstraint(SpringLayout.NORTH, allTopicsLabel, 30, SpringLayout.SOUTH, newTopicBox);

		layout.putConstraint(SpringLayout.WEST, topicScroll, 0, SpringLayout.WEST, allTopicsLabel);
		layout.putConstraint(SpringLayout.NORTH, topicScroll, 30, SpringLayout.SOUTH, allTopicsLabel);

}
	private void setupThisFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		layout = new SpringLayout();
		setTitle("making kafka awesome!");
		setResizable(false);
		mainPanel = setupMainPanel();
		setContentPane(mainPanel);
		mainPanel.setVisible(true);
		setVisible(true);
		
	}
	private JPanel setupMainPanel(){
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(300,400)); //width,height
		layout = new SpringLayout();
		mainPanel.setLayout(layout);
		return mainPanel;
	}
	private void addListeners(){
		sendMessageButton.addActionListener(this);
	}
	public static void main(String args[]){
		MainFrame mofo = new MainFrame();
		System.out.println("done!");
	}
	/**
	 * 
	 * @param input
	 * @return null if there is nothing wrong with the String, otherwise a message related to what issues it has...
 	 */
	private String inspectMessage(String input){
		//just to be extra paranoid, we will implement a method that checks for common stuff that might mess up a 
		//Kafka server? can statements be sent that allow some kind of injection virus? just a thought...
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==sendMessageButton){
			Producer<String,String> producer = MessageProducer.getProducerSingleton();
			String message = sendMessageBox.getText();
			if((inspectMessage(message))!=null){
				//throw an error window..
			}
			String textContents = newTopicBox.getText();
			String newTopic = (newTopicBox.getText().compareTo("")!=0)
			?newTopicBox.getText():"test";
			

			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
												newTopic, 
												"1", 
												message);
			producer.send(data);
			
			/*
			 * This is to make it a bit more efficient so we don't refresh this
			 * list every time a message is submitted. This saves a bunch of processing.
			 */
			if(!listModel.contains(newTopic)){
				topicList.setModel(getTopicListModel());
			}

		}
	}
	private DefaultListModel getTopicListModel(){
		DefaultListModel model = new DefaultListModel();
		List<String> topics = consumer.getTopicNames();

		for(String current : topics){
			model.addElement(current);
		}
		return model;
	}
}
