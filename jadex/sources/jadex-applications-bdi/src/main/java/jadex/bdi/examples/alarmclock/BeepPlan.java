package jadex.bdi.examples.alarmclock;

import jadex.bdi.planlib.GuiCreator;
import jadex.bdi.runtime.Plan;
import jadex.commons.SUtil;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *  Play a beep.
 */
public class BeepPlan extends Plan
{
	//-------- methods --------

	/**
	 * The body method is called on the
	 * instatiated plan instance from the scheduler.
	 */
	public void body()
	{
		Alarm	alarm	= (Alarm)getParameter("alarm").getValue();
		final JFrame	parent	= ((GuiCreator)getBeliefbase().getBelief("gui").getFact()).getFrame(); // Hack!
		final String	message	= alarm.getMessage()!=null ? SUtil.wrapText(alarm.getMessage()) : null;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(parent, message, "Alarm is due", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}
