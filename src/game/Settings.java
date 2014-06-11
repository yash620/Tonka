package game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Startable;
import weapon.Weapon.WeaponList;

	
	public class Settings extends JPanel implements ChangeListener {
		private ArrayList<JSlider> sliders;
		private ArrayList<JLabel> labels;
		private Startable frame;
		private double frequency;

		public Settings(Startable fr){
			this.frame = fr;
			sliders = new ArrayList<JSlider>();
			labels = new ArrayList<JLabel>();
			ArrayList<Object[]> randWeaps = Game.getRandWeaps();
			for (Object[] o : randWeaps) {
				JSlider slider = new JSlider(0,100, (int)(((Double)o[1]).doubleValue()*100));
				slider.setName(((WeaponList)o[0]).toString());
				sliders.add(slider);
				slider.addChangeListener(this);
				this.add(slider);
				slider.setPaintLabels(true);
				slider.setMajorTickSpacing(20);
				slider.setPaintTicks(true);
				labels.add(new JLabel(o[0].toString() + ": " + o[1].toString()));
			}
			JSlider frequency = new JSlider(0,100,3);
			frequency.setName("Frequency");
			sliders.add(frequency);
			labels.add(new JLabel("Frequency" + ": " + .03));
			frequency.addChangeListener(this);
			frequency.setMajorTickSpacing(20);
			frequency.setPaintTicks(true);
			this.add(frequency);
			SpringLayout sl = this.createLayout();
			JButton button = new JButton();
			button.setText("PLAY");
			this.add(button);
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.startGame();
				}
				
			});
			button.setPreferredSize(new Dimension(100,30));
			sl.putConstraint(SpringLayout.SOUTH, button, -10, SpringLayout.SOUTH, this);
			sl.putConstraint(SpringLayout.WEST, button, 150, SpringLayout.WEST, this);
			
			this.setLayout(sl);
			this.setPreferredSize(new Dimension(400,50 + sliders.size() * 50));
		}
		
		private SpringLayout createLayout() {
			SpringLayout layout = new SpringLayout();
			
			JLabel previouslbl = this.labels.get(0);
			JSlider previousSlider = sliders.get(0);
			layout.putConstraint(SpringLayout.WEST, previouslbl, 10, SpringLayout.WEST, this);		//Initial Label
			layout.putConstraint(SpringLayout.NORTH, previouslbl, 10, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.EAST, previousSlider, -10, SpringLayout.EAST, this);	//Initial field
			layout.putConstraint(SpringLayout.NORTH, previousSlider, 10, SpringLayout.NORTH, this);
			this.add(previousSlider);
			this.add(previouslbl);
			previouslbl.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			for (int i = 1;i<sliders.size();i++){					//Loop to do the rest of the boxes
				JLabel templabel = labels.get(i);
				JSlider tempfield = sliders.get(i);
				layout.putConstraint(SpringLayout.NORTH, templabel, 50, SpringLayout.NORTH, previouslbl);				//Set the label
				layout.putConstraint(SpringLayout.WEST, templabel, 0, SpringLayout.WEST, previouslbl);
				
				layout.putConstraint(SpringLayout.WEST, tempfield, 0, SpringLayout.WEST, previousSlider);					//Set the field
				layout.putConstraint(SpringLayout.NORTH, tempfield, 0, SpringLayout.NORTH, templabel);
				
				
				templabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				this.add(templabel);
				this.add(tempfield);
				previouslbl = templabel;				//Next boxes
				previousSlider = tempfield;
			}
			return layout;
		}

		@Override
		public void stateChanged(ChangeEvent arg0) {
			JSlider slider = (JSlider) arg0.getSource();
			for (Object[] o : Game.getRandWeaps()) {
				if (slider.getName() == o[0].toString()) {
					o[1] = slider.getValue()/100.0;
					System.out.println(o[1]);
				}
			}
			if (slider.getName() == "Frequency") {
				frequency = slider.getValue()/100.0;
				if (frequency == 1) {
					frequency = .99;
				}
			}
			int index = sliders.indexOf(slider);
			labels.get(index).setText(slider.getName() + ": " + slider.getValue()/100.0);
		}
		public double getFrequency() {
			return frequency;
		}
	}