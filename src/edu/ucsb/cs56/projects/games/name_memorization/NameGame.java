package edu.ucsb.cs56.projects.games.name_memorization;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.*;
  /**
   * Preliminary engine for running a name memorization game
   *
   *@author Anthony Hoang, Colin Biafore
   *@version  for cs56, Spring 2014
   */
public class NameGame extends JFrame{

    //Top Control Panel
    private JPanel north;
    private JButton add;
    private JButton edit;
    private JButton delete;
    private JButton next;
    private JButton previous;

    //Bottom Control Panel
    private JPanel south;
    private JButton toFront;
    private JButton toBack;
    
    private DirectoryLister dir;

    private Image pic;


    //Current Card Viewer
    private JPanel currentCard;
    private JTextArea cardText;
    private int current;
    private Deck d;

    //Anthony's Label's
    private JFrame thisframe = this;
    private JLabel deckName;
    private JLabel deckSize;
    private JLabel sizeLabel;
    private JPanel east;
    private JLabel picture;

    //Label for Card Number:
    private JLabel cardNum;
    //UI Card Index
    private JLabel cNum;

    /**
     * No arg constructor for the name game. Initializes everyting in a JFrame
     * (Buttons, pics, etc)
     */
    public NameGame(){
	
	//Set Frame Layout
	this.getContentPane().setLayout(new BorderLayout());

	//Initialize North Control Panel
	north = new JPanel();
	north.setVisible(true);
	add = new JButton("Add");
	edit = new JButton("Edit");
	delete = new JButton("Delete");
	previous = new JButton("Previous");
	next = new JButton("Next");
	north.add(add);
	north.add(edit);
	north.add(delete);
	north.add(previous);
	north.add(next);
	north.setBackground(Color.ORANGE);
	this.add(north,BorderLayout.NORTH);
	
	//Initialize South Control Panel
	south = new JPanel();
	south.setVisible(true);
	toFront = new JButton("Show Front");
	toBack = new JButton("Show Back");
	south.add(toFront);
	south.add(toBack);
	south.setBackground(Color.ORANGE);
	this.add(south, BorderLayout.SOUTH);

	//Initialize Card Viewer
	currentCard = new JPanel();
	currentCard.setVisible(true);
	cardText = new JTextArea();
	Font cardFont = new Font("Verdana",Font.BOLD,24);
	cardText.setFont(cardFont);
	cardText.setEditable(false);
	currentCard.add(cardText);
	currentCard.setBackground(Color.WHITE);
	this.add(currentCard, BorderLayout.CENTER);
	

	//Create a new deck
	d = new Deck("First Deck");
	

	//	dir = new DirectoryLister("src/people");
     
	
	
	
	
	deckName = new JLabel(d.getName());
	deckName.setForeground(Color.WHITE);
	deckName.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	this.add(deckName,BorderLayout.WEST);
	
	sizeLabel = new JLabel("Deck Size :");
	sizeLabel.setForeground(Color.WHITE);
	sizeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	deckSize = new JLabel( Integer.toString(d.size()));
	deckSize.setForeground(Color.WHITE);
	deckSize.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	
	cardNum = new JLabel("Card Number:");
	cardNum.setForeground(Color.WHITE);
	cardNum.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	cNum = new JLabel(Integer.toString(current));
	cNum.setForeground(Color.WHITE);
	cNum.setFont(new Font("Lucida Grande",Font.PLAIN, 18));

	JPanel eastCenter = new JPanel();
	eastCenter.setBackground(Color.BLUE);
	eastCenter.add(cardNum);
	eastCenter.add(cNum);


	east = new JPanel();
	east.setLayout(new BorderLayout());
	east.setBackground(Color.BLUE);
	JPanel top = new JPanel();
	top.setBackground(Color.BLUE);
	top.add(sizeLabel, BorderLayout.NORTH);
	top.add(deckSize, BorderLayout.NORTH);
	
	//east.add(cardNum, BorderLayout.CENTER);
	east.add(eastCenter,BorderLayout.EAST);
	east.add(top,BorderLayout.NORTH);
	this.add(east,BorderLayout.EAST);

	//BUTTON LISTENERS -- Uncommented = implemented and functioning!!
	//Currently you can add as many cards as you want,
	//Go to the next or previous card in the deck,
	//And see both sides of the current card 

	//Initialize Add Button Listener
	addButtonListener addListener = new addButtonListener();
	add.addActionListener(addListener);

	//Initialize Edit Button Listener
	editButtonListener editListener = new editButtonListener();
	edit.addActionListener(editListener);
	
	//Initialize Delete Button Listener
	deleteButtonListener deleteListener = new deleteButtonListener();
	delete.addActionListener(deleteListener);

	//Initialize Previous Button Listener
	previousButtonListener previousListener = new previousButtonListener();
	previous.addActionListener(previousListener);
	
	
	//Initialize picture JLabel that is used in next listener
	picture = new JLabel();
	//Initialize Next Button Listener
	nextButtonListener nextListener = new nextButtonListener();
	next.addActionListener(nextListener);
	

	//Initialize Front Button Listener
	frontButtonListener frontListener = new frontButtonListener();
	toFront.addActionListener(frontListener);

	//Initialize Back Button Listener
	backButtonListener backListener = new backButtonListener();
	toBack.addActionListener(backListener);
	
	this.pack();
    }  

 	//this method will be called with next/previous button if card has a pic
    public void setPic(Card c){
    	cardText.setVisible(false);
    	currentCard.remove(picture);
		picture=c.getPic();
		picture.setVisible(true);		    
		currentCard.add(picture, BorderLayout.CENTER);
		thisframe.getContentPane().validate();
		thisframe.getContentPane().repaint();
    }

    //this method will be called with next/previous if card is text
    public void setPrint(Card c, int side){
    	picture.setVisible(false);
		cardText.setVisible(true);
		currentCard.remove(picture);
		if(side==1){
			cardText.setText(c.getSide1());	
		}else if(side==2){
			cardText.setText(c.getSide2());	
		}

    }

    public void setDeck(Deck d) {
	this.d = d;
    }

    public Deck getDeck() {
	return d;
    }

    public void updateSize(int deckSize) {
	
	this.deckSize.setText(Integer.toString(deckSize));
    }

    public void setCardNum() {
	if (d.size() < 1) {
	    this.cNum.setText("0");
	} else
	    this.cNum.setText("1");
    }






    /**
     * addButtonListener, Brings up a window to add a card
     */
    private class addButtonListener implements ActionListener {

	CardEditor editor;


        public void actionPerformed(ActionEvent event) {
	    
	    //Creates a new card editor

	    editor = new CardEditor();
	    JButton confirm = new JButton("Confirm");
	    confirm.setBounds(260,400,100,30);
	    editor.getContentPane().add(confirm);
	    confirmButtonListener confirmListener = new confirmButtonListener();
	    confirm.addActionListener(confirmListener);
	    
	}
	
	// Only adds a card once confirm has been pressed
	private class confirmButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
		String side1 = editor.getFrontText();
		String side2 = editor.getBackText();
		
		d.addCard(side1,side2,editor.isPic());
		editor.dispose();
		current = d.size() - 1;
		Card h = (Card) d.get(current);
		if(h.isPic()){
			/*
		    cardText.setVisible(false);
		    picture=editor.getPic();
		    picture.setVisible(true);		    
		    currentCard.add(picture, BorderLayout.CENTER);
		    */
		    setPic(h);
		}//if(h.isPic())
		else{/*
		    picture.setVisible(false);
		    cardText.setVisible(true);
		    currentCard.remove(picture);;
		    cardText.setText(h.getSide1());
		    */
		    setPrint(h,1);
		}
		next.setEnabled(true);
		previous.setEnabled(true);
		deckSize.setText(Integer.toString(d.size()));
		cNum.setText(Integer.toString(current+1));
		
		
	    }
	}
    }

    private class editButtonListener implements ActionListener {

	CardEditor editor;

	public void actionPerformed(ActionEvent e) {
	    
	    if(d.size() == 0) {
		JOptionPane.showMessageDialog(null, "Deck is currently empty","Error", JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    editor = new CardEditor();
	    JButton confirm = new JButton("Confirm");
	    confirm.setBounds(260,400,100,30);
	    editor.getContentPane().add(confirm);
	    confirmButtonListener confirmListener = new confirmButtonListener();
	    confirm.addActionListener(confirmListener);
	}
	
	private class confirmButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
		String side1 = editor.getFrontText();
		String side2 = editor.getBackText();
		Card h = (Card) d.get(current);

		d.editCard(h, side1, side2);
		if(h.isPic()){
		   	setPic(h);
	    }
	    else{	
	       	setPrint(h,1);
	    }

		editor.dispose();
	    }

	}
    }

    private class deleteButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if(d.size() == 0) {
		JOptionPane.showMessageDialog(null, "Deck is currently empty","Error", JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if(d.size() == 1) {
		d.remove(0);
		currentCard.removeAll();
		cardText.setText("Deck is Empty!");
		currentCard.add(cardText);
		thisframe.getContentPane().validate();
		thisframe.getContentPane().repaint();

		current = 0;
	      
	    }
	    if(d.size() > 1) {
		if(current == 0) {
		    Card h = (Card) d.get(current+1);
		    // cardText.setText(h.getSide1());
		    if(h.isPic()){
		    	setPic(h);
		    }
		    else{
		    	setPrint(h,1);
		    }
		    d.remove(current);
		    
		    
		}
		else {
		    d.remove(current);
		    current--;
		    Card h = (Card) d.get(current);
		    // cardText.setText(h.getSide1());
		    if(h.isPic()){
		    	setPic(h);
		    }
		    else{
		    	setPrint(h,1);
		    }
		}
	    }

	    if(d.size() == 0) 
		cNum.setText("0");
	    else
		cNum.setText(Integer.toString(current+1));

	    deckSize.setText(Integer.toString(d.size()));

	}
    }

    private class nextButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if(d.size() == 0) {
		return;
	    }
	    current++;
	    if(current == d.size()) {
		current = 0;
            }
    
	    Card h = (Card) d.get(current);
	    if(h.isPic()){
	    	setPic(h);
	    }
	    else{
	    	setPrint(h,1);
	    }
	    cNum.setText(Integer.toString(current+1));
	    

	    
	}
    }

    private class previousButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {

	    if(d.size() == 0) {
		return;
	    }
	    current--;
	    
	    if(current == -1) {
		current = d.size() - 1;
	    }





	    Card h = (Card) d.get(current);

	     if(h.isPic()){
	    	setPic(h);
	    }
	    else{
	    	setPrint(h,1);
	    }
	    cNum.setText(Integer.toString(current+1));
	}
    }

    private class frontButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    if(d.size() == 0) {
			return;
		    }
		    Card h = (Card) d.get(current);
		    if(h.isPic()){
		    	setPic(h);
		    }
		    else{
		    	setPrint(h,1);
		    }

		}
    }

    private class backButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    if(d.size() == 0) {
			return;
		    }
		    Card h = (Card) d.get(current);
		    setPrint(h,2);
		    

		}
    }

    
}
