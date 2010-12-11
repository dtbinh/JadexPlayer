package jadex.bdi.examples.shop;

import jadex.bdi.runtime.AgentEvent;
import jadex.bdi.runtime.IBDIExternalAccess;
import jadex.bdi.runtime.IBeliefListener;
import jadex.bdi.runtime.IBeliefSetListener;
import jadex.bdi.runtime.IEAGoal;
import jadex.commons.concurrent.SwingDefaultResultListener;
import jadex.commons.service.SServiceProvider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *  Customer gui that allows buying items at different shops.
 */
public class CustomerPanel extends JPanel
{
	//-------- attributes --------
	
	protected IBDIExternalAccess agent;
	protected List shoplist = new ArrayList();
	protected JCheckBox remote;
	protected JTable shoptable;
	protected AbstractTableModel shopmodel = new ItemTableModel(shoplist);
	
	protected List invlist = new ArrayList();
	protected AbstractTableModel invmodel = new ItemTableModel(invlist);
	protected JTable invtable;
	protected Map	shops;
	
	//-------- constructors --------
	
	/**
	 *  Create a new gui.
	 */
	public CustomerPanel(final IBDIExternalAccess agent)
	{
//		super(agent.getComponentName());
		this.agent = agent;
		this.shops	= new HashMap();
		
		final JComboBox shopscombo = new JComboBox();
		shopscombo.addItem("none");
		shopscombo.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(shops.get(shopscombo.getSelectedItem()) instanceof IShop)
				{
					refresh((IShop)shops.get(shopscombo.getSelectedItem()));
				}
			}
		});
		
		remote = new JCheckBox("Remote");
		remote.setToolTipText("Also search remote platforms for shops.");
		final JButton searchbut = new JButton("Search");
		searchbut.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	searchbut.setEnabled(false);
		    	SServiceProvider.getServices(agent.getServiceProvider(), IShop.class, remote.isSelected(), true)
					.addResultListener(new SwingDefaultResultListener(CustomerPanel.this)
				{
					public void customResultAvailable(Object source, Object result)
					{
				    	searchbut.setEnabled(true);
//						System.out.println("Customer search result: "+result);
						Collection coll = (Collection)result;
						((DefaultComboBoxModel)shopscombo.getModel()).removeAllElements();
						shops.clear();
						if(coll!=null && coll.size()>0)
						{
							for(Iterator it=coll.iterator(); it.hasNext(); )
							{
								IShop	shop	= (IShop)it.next();
								shops.put(shop.getName(), shop);
								((DefaultComboBoxModel)shopscombo.getModel()).addElement(shop.getName());
							}
						}
						else
						{
							((DefaultComboBoxModel)shopscombo.getModel()).addElement("none");
						}					
					}
					
					public void customExceptionOccurred(Object source, Exception exception)
					{
				    	searchbut.setEnabled(true);
						super.customExceptionOccurred(source, exception);
					}
				});
		    }
		});

		final NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		final JTextField money = new JTextField(5);
		agent.getBeliefbase().getBeliefFact("money")
			.addResultListener(new SwingDefaultResultListener(CustomerPanel.this)
		{
			public void customResultAvailable(Object source, Object result)
			{
				money.setText(df.format(result));
			}
		});
		money.setEditable(false);
		agent.getBeliefbase().addBeliefListener("money", new IBeliefListener()
		{
			public void beliefChanged(final AgentEvent ae)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						money.setText(df.format(ae.getValue()));
					}
				});
			}
		});
		
		JPanel selpanel = new JPanel(new GridBagLayout());
		selpanel.setBorder(new TitledBorder(new EtchedBorder(), "Properties"));
		int x=0;
		int y=0;
		selpanel.add(new JLabel("Money: "), new GridBagConstraints(
			x,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		x++;
		selpanel.add(money, new GridBagConstraints(
			x,y,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		x++;
		selpanel.add(new JLabel("Available shops: "), new GridBagConstraints(
			x,y,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		x++;
		selpanel.add(shopscombo, new GridBagConstraints(
			x,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		x++;
		selpanel.add(searchbut, new GridBagConstraints(
			x,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		x++;
		selpanel.add(remote, new GridBagConstraints(
			x,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
		
		JPanel shoppanel = new JPanel(new BorderLayout());
		shoppanel.setBorder(new TitledBorder(new EtchedBorder(), "Shop Catalog"));
		shoptable = new JTable(shopmodel);
		shoptable.setPreferredScrollableViewportSize(new Dimension(600, 120));
		shoptable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shoppanel.add(BorderLayout.CENTER, new JScrollPane(shoptable));

		JPanel invpanel = new JPanel(new BorderLayout());
		invpanel.setBorder(new TitledBorder(new EtchedBorder(), "Customer Inventory"));
		invtable = new JTable(invmodel);
		invtable.setPreferredScrollableViewportSize(new Dimension(600, 120));
		invpanel.add(BorderLayout.CENTER, new JScrollPane(invtable));

		agent.getBeliefbase().addBeliefSetListener("inventory", new IBeliefSetListener()
		{
			public void factRemoved(final AgentEvent ae)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						invlist.remove(ae.getValue());
						invmodel.fireTableDataChanged();
					}
				});
			}
			
			public void factChanged(final AgentEvent ae)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						invlist.remove(ae.getValue());
						invlist.add(ae.getValue());
						invmodel.fireTableDataChanged();
					}
				});
			}
			
			public void factAdded(final AgentEvent ae)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						invlist.add(ae.getValue());
						invmodel.fireTableDataChanged();
					}
				});
			}
		});
		
		JPanel butpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		butpanel.setBorder(new TitledBorder(new EtchedBorder(), "Actions"));
		JButton buy = new JButton("Buy");
		final JTextField item = new JTextField(8);
		item.setEditable(false);
		butpanel.add(new JLabel("Selected item:"));
		butpanel.add(item);
		butpanel.add(buy);
		buy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int sel = shoptable.getSelectedRow();
				if(sel!=-1)
				{
					final String name = (String)shopmodel.getValueAt(sel, 0);
					final Double price = (Double)shopmodel.getValueAt(sel, 1);
					final IShop shop = (IShop)shops.get(shopscombo.getSelectedItem());
//					System.out.println("buying: "+name+" at: "+shop.getName());
					agent.createGoal("buy").addResultListener(new SwingDefaultResultListener(CustomerPanel.this)
					{
						public void customResultAvailable(Object source, Object result)
						{
							IEAGoal buy = (IEAGoal)result;
							buy.setParameterValue("name", name);
							buy.setParameterValue("shop", shop);
							buy.setParameterValue("price", price);
							agent.dispatchTopLevelGoalAndWait(buy).addResultListener(new SwingDefaultResultListener(CustomerPanel.this)
							{
								public void customResultAvailable(Object source, Object result)
								{
									// Update number of available items
									refresh(shop);
								}
							});
						}
					});
					
				}
			}
		});
		
		shoptable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				int sel = shoptable.getSelectedRow();
				if(sel!=-1)
				{
					item.setText(""+shopmodel.getValueAt(sel, 0));
				}
			}
		});
		
		setLayout(new GridBagLayout());
		x=0;
		y=0;
		add(selpanel, new GridBagConstraints(
			x,y++,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
		add(shoppanel, new GridBagConstraints(
			x,y++,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
		add(invpanel, new GridBagConstraints(
			x,y++,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
		add(butpanel, new GridBagConstraints(
			x,y++,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
		
//		refresh();
	}
	
	/**
	 *  Create a customer gui frame.
	 * /
	public static void createCustomerGui(final IBDIExternalAccess agent)
	{
		final JFrame f = new JFrame();
		f.add(new CustomerPanel(agent));
		f.pack();
		f.setLocation(SGUI.calculateMiddlePosition(f));
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				agent.killAgent();
			}
		});
		agent.addAgentListener(new IAgentListener() 
		{
			public void agentTerminating(AgentEvent ae) 
			{
				f.setVisible(false);
				f.dispose();
			}
			
			public void agentTerminated(AgentEvent ae) 
			{
			}
		});
	}*/
	
	/**
	 * Method to be called when goals may have changed.
	 */
	public void refresh(IShop shop)
	{
		if(shop!=null)
		{
			shop.getCatalog().addResultListener(new SwingDefaultResultListener(CustomerPanel.this)
			{
				public void customResultAvailable(Object source, Object result)
				{
					int sel = shoptable.getSelectedRow();
					ItemInfo[] aitems = (ItemInfo[])result;
					shoplist.clear();
					for(int i = 0; i < aitems.length; i++)
					{
						if(!shoplist.contains(aitems[i]))
						{
//							System.out.println("added: "+aitems[i]);
							shoplist.add(aitems[i]);
						}
					}
					shopmodel.fireTableDataChanged();
					if(sel!=-1 && sel<aitems.length)
						((DefaultListSelectionModel)shoptable.getSelectionModel()).setSelectionInterval(sel, sel);
				}
			});
		}
		else
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					shoplist.clear();
					shopmodel.fireTableDataChanged();
				}
			});
		}
	}
		
}

class ItemTableModel extends AbstractTableModel
{
	protected List list;
	
	public ItemTableModel(List list)
	{
		this.list = list;
	}
	
	public int getRowCount()
	{
		return list.size();
	}

	public int getColumnCount()
	{
		return 3;
	}

	public String getColumnName(int column)
	{
		switch(column)
		{
			case 0:
				return "Name";
			case 1:
				return "Price";
			case 2:
				return "Quantity";
			default:
				return "";
		}
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	public Object getValueAt(int row, int column)
	{
		Object value = null;
		ItemInfo ii = (ItemInfo)list.get(row);
		if(column == 0)
		{
			value = ii.getName();
		}
		else if(column == 1)
		{
			value = new Double(ii.getPrice());
		}
		else if(column == 2)
		{
			value = new Integer(ii.getQuantity());
		}
		return value;
	}
};