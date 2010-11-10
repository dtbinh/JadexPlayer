package jadex.tools.dfbrowser;

import jadex.adapter.fipa.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ServiceDescriptionPanel
 */
public class ServiceDescriptionPanel extends JPanel
{
	//-------- attributes --------
	
	protected JTextField name;
	protected JTextField type;
	protected JTextField owner;
	protected JTextField agent;
	protected JList onto;
	protected JList lang;
	protected JList proto;
	protected JList props;

	//-------- constructors --------

	/**
	 * Constructor for ServiceDescriptionPanel.
	 */
	public ServiceDescriptionPanel()
	{
		super(new GridBagLayout());

		this.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Service Properties"));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 1, 1, 1);

		this.name = addTextField(c, "Name", "", "Service name");
		this.type = addTextField(c, "Type", "", "Service type");
		this.owner = addTextField(c, "Ownership", "", "Service ownership");
		this.agent = addTextField(c, "Agent", "", "The agent providing this service");
		JPanel panel = new JPanel(new GridLayout(1, 4));
		this.onto = addList(panel, "Ontologies", "Ontologies understood by this service");
		this.lang = addList(panel, "Languages", "Languages understood by this service");
		this.proto = addList(panel, "Protocols", "Protocols utilized by this service");
		this.props = addList(panel, "Properties", "Properties of this service");

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(panel, c);
	}

	//-------- methods --------
	
	/**
	 *  Set the service description.
	 *  @param ad The agent description.
	 *  @param sd The service description.
	 */
	void setService(AgentDescription ad, ServiceDescription sd)
	{
		name.setText(sd==null? "": sd.getName());
		name.setToolTipText(sd==null? "": sd.getName());
		type.setText(sd==null? "": sd.getType());
		type.setToolTipText(sd==null? "": sd.getType());
		owner.setText(sd==null? "": sd.getOwnership());
		owner.setToolTipText(sd==null? "": sd.getOwnership());
		agent.setText(ad==null? "": ad.getName().getName());
		
		if(ad!=null)
		{
			String[] addrs = ad.getName().getAddresses();
			String tooltip = "<html>" + ad.getName().getName();
			for(int addr = 0; addr < addrs.length; addr++)
				tooltip += "<br>" + addrs[addr];
			agent.setToolTipText(tooltip);
		}
		
		update(onto, sd==null? new String[0]: sd.getOntologies());
		update(lang, sd==null? new String[0]: sd.getLanguages());
		update(proto, sd==null? new String[0]: sd.getProtocols());

		DefaultListModel model = (DefaultListModel)props.getModel();
		model.clear();
		Property[] items = sd==null? new Property[0]: sd.getProperties();
		for(int i=0; i<items.length; i++)
			model.addElement(items[i].getName() + '=' + items[i].getValue());
	}

	/**
	 * @param list
	 * @param items
	 */
	protected void update(JList list, String[] items)
	{
		DefaultListModel model = (DefaultListModel)list.getModel();
		model.clear();
		for(int i = 0; i < items.length; i++)
		{
			model.addElement(items[i]);
		}
	}

	/**
	 * @param panel
	 * @param name
	 * @param tooltip
	 */
	protected JList addList(JPanel panel, String name, String tooltip)
	{
		DefaultListModel model = new DefaultListModel();
		JList list = new JList(model);
		JPanel tp = new JPanel(new BorderLayout());
		tp.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), name));
		list.setToolTipText(tooltip);
		JScrollPane scroll = new JScrollPane(list);
		tp.add("Center", scroll);
		panel.add(tp);
		return list;
	}

	/**
	 * @param c
	 * @param label
	 * @param value
	 * @param tooltip
	 */
	protected JTextField addTextField(GridBagConstraints c, String label, String value, String tooltip)
	{
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 0;
		c.weighty = 0;
		JLabel l = new JLabel(label + ':');
		add(l, c);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = GridBagConstraints.RELATIVE;
		c.weightx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JTextField tf = new JTextField(value, value.length());
		tf.setEditable(false);
		add(tf, c);
		l.setLabelFor(tf);
		tf.setToolTipText(tooltip);
		return tf;
	}

}