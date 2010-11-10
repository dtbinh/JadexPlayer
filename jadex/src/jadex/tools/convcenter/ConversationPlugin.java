package jadex.tools.convcenter;

import jadex.adapter.fipa.AMSAgentDescription;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.adapter.fipa.SFipa;
import jadex.runtime.IMessageEvent;
import jadex.runtime.IParameterSet;
import jadex.tools.common.AgentTreeTable;
import jadex.tools.common.GuiProperties;
import jadex.tools.common.jtreetable.DefaultTreeTableNode;
import jadex.tools.common.plugin.IAgentListListener;
import jadex.tools.common.plugin.IMessageListener;
import jadex.tools.jcc.AbstractJCCPlugin;
import jadex.tools.starter.StarterPlugin;
import jadex.util.SGUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.*;

/**
 *  The conversation plugin
 */
public class ConversationPlugin extends AbstractJCCPlugin implements IAgentListListener, IMessageListener
{
	//-------- constants --------
	
	/** The property storing the last state of the message panel. */ 
	public static final String	LAST_MESSAGE	= "lastmessage";

	/** The property storing sent messages (from 0..4). */ 
	public static final String	SENT_MESSAGE	= "sentmessage";

	/** String used to store the message type in encoded messages. */
	public static final String	ENCODED_MESSAGE_TYPE	= "encoded-message-type";

	/** The image icons. */
	protected static final UIDefaults icons = new UIDefaults(new Object[]
	{
		"conversation",	SGUI.makeIcon(ConversationPlugin.class, "/jadex/tools/common/images/new_conversation.png"),
		"conversation_sel", SGUI.makeIcon(StarterPlugin.class, "/jadex/tools/common/images/new_conversation_sel.png"),
		"help",	SGUI.makeIcon(ConversationPlugin.class, "/jadex/tools/common/images/help.gif"),
	});

	//-------- attributes --------
	
	/** The agent tree table. */
	protected AgentTreeTable agents;

	/** The conversation center panel. */
	protected FipaConversationPanel convcenter;

	/**
	 * @return "Conversation Center"
	 * @see jadex.tools.common.plugin.IControlCenterPlugin#getName()
	 */
	public String getName()
	{
		return "Conversation Center";
	}

	/**
	 * @return the conversation icon
	 * @see jadex.tools.common.plugin.IControlCenterPlugin#getToolIcon()
	 */
	public Icon getToolIcon(boolean selected)
	{
		return selected? icons.getIcon("conversation_sel"): icons.getIcon("conversation");
	}


	final AbstractAction SEND_MESSAGE = new AbstractAction("Send Message", icons.getIcon("conversation"))
	{
		public void actionPerformed(ActionEvent e)
		{
			DefaultTreeTableNode node = (DefaultTreeTableNode)agents.getTreetable().getTree().getSelectionPath().getLastPathComponent();
			AMSAgentDescription desc = (AMSAgentDescription)node.getUserObject();
			// Use clone, as added agent id might be modified by user.
			AgentIdentifier	receiver	= (AgentIdentifier)desc.getName().clone();
			IMessageEvent	message	= convcenter.getMessagePanel().getMessage();
			IParameterSet rcvs = message.getParameterSet(SFipa.RECEIVERS);
			if(rcvs.containsValue(receiver))
			{
				rcvs.removeValue(receiver);
			}
			else
			{
				rcvs.addValue(receiver);
			}
			convcenter.getMessagePanel().setMessage(message);
		}
	};
	
	/**
	 *  Create main panel.
	 *  @return The main panel.
	 */
	public JComponent createView()
	{
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		split.setOneTouchExpandable(true);

		agents = new AgentTreeTable();
		agents.setMinimumSize(new Dimension(0, 0));
		split.add(agents);
		agents.getTreetable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		agents.getNodeType(AgentTreeTable.NODE_AGENT).addPopupAction(SEND_MESSAGE);

		split.add(convcenter = new FipaConversationPanel(getJCC().getAgent(), null));

		GuiProperties.setupHelp(split, "tools.conversationcenter");

		agents.getTreetable().getSelectionModel().setSelectionInterval(0, 0);
		split.setDividerLocation(150);

		agents.getTreetable().addMouseListener(new MouseAdapter()
		{
			/**
			 * invoke SEND_MESSAGE action
			 * @param e
			 */
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount()==2)
				{
					SEND_MESSAGE.actionPerformed(null);
				}
			}
		});

		jcc.addAgentListListener(this);
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				agents.adjustColumnWidths();
			}
		});
		jcc.addMessageListener(this);

		return split;
	}

	/**
	 * @param me
	 * @return true if the message event is not from tool_management ontology
	 */
	public boolean processMessage(IMessageEvent message)
	{
		boolean	processed	= false;
		try
		{
			String onto = (String)message.getParameter("ontology").getValue();
			if (onto==null || !onto.startsWith("jadex.tools"))
			{
				convcenter.addMessage(message);
				processed	= true;
			}
		}
		catch(Exception e)
		{
		}
		return processed;
	}
	


	/** 
	 * @param ps
	 * @see jadex.tools.jcc.AbstractJCCPlugin#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties props)
	{
		convcenter.setProperties(props);
	}

	/** 
	 * @param ps
	 * @see jadex.tools.jcc.AbstractJCCPlugin#getProperties(java.util.Properties)
	 */
	public void getProperties(Properties props)
	{
		convcenter.getProperties(props);
	}

	/**
	 * @param ad
	 */
	public void agentDied(final AMSAgentDescription ad)
	{
		// Update components on awt thread.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				agents.removeAgent(ad);
			}
		});
	}

	/**
	 * @param ad
	 */
	public void agentBorn(final AMSAgentDescription ad)
	{
		// Update components on awt thread.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				agents.addAgent(ad);
			}
		});
	}
	
	/**
	 * @param ad
	 */
	public void agentChanged(final AMSAgentDescription ad)
	{
		// nop?
		// Update components on awt thread.
		/*SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				agents.addAgent(ad);
			}
		});*/
	}

	/** 
	 * @return the help id of the perspective
	 * @see jadex.tools.jcc.AbstractJCCPlugin#getHelpID()
	 */
	public String getHelpID()
	{
		return "tools.conversationcenter";
	}
	
	/**
	 *  Reset the conversation center to an initial state
	 */
	public void	reset()
	{
		convcenter.reset();
	}
}